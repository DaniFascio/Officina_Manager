package io.github.danifascio.gui;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import io.github.danifascio.DatabaseManager;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import io.github.danifascio.dao.AutoDao;
import io.github.danifascio.dao.LavorazioneDao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class CentralPane extends AnchorPane {

	@FXML
	private AnchorPane root;
	@FXML
	private ListView<Auto> listaAutoView;
	@FXML
	private ListView<Lavorazione> listaLavorazioniView;
	@FXML
	private Label welcomeLabel;
	@FXML
	private TextField targaLabel;
	@FXML
	private TextField modelloLabel;
	@FXML
	private TextField kmLabel;
	@FXML
	private TextField misuraGommeLabel;
	@FXML
	private TextField tipoGommeLabel;
	@FXML
	private TextArea noteArea;
	@FXML
	private HBox searchBox;

	private Auto selectedAuto;

	public CentralPane() {
		super();

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/Central.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();

			welcomeLabel.setText(DatabaseManager.getUsername());

			Tooltip.install(searchBox, new Tooltip("Cerca per targa o modello"));

			listaAutoView.setCellFactory(listView -> new AutoCell());
			listaAutoView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(listaAutoView.getItems().size() == 0)
					return;

				selectedAuto = newValue;

				targaLabel.setText(newValue.getTarga());
				modelloLabel.setText(newValue.getModello());
				kmLabel.setText(newValue.getKm().toString());
				misuraGommeLabel.setText(newValue.getMisuraGomme());
				tipoGommeLabel.setText(newValue.getTipoGomme().getDescrizione());
				noteArea.setText(newValue.getNote());

				onRefreshLavorazione(null);
			});

			listaLavorazioniView.setCellFactory(listView -> new LavorazioneCell());

			onRefreshAuto(null);
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@FXML
	private void onAddAuto(ActionEvent event) {

		new AutoDialog(AutoDialog.ViewMode.ADD, null).onResult(auto -> {
			if(auto != null) {
				AutoDao autoDao = new AutoDao();
				Dialog<ButtonType> dialog;

				if(autoDao.save(auto) == 1) {

					dialog = new Alert(Alert.AlertType.INFORMATION);
					dialog.setHeaderText("Auto aggiunta con successo");

				} else {

					String message = autoDao.errorMessage();
					int index = message.indexOf("Detail");
					if(index != -1)
						message = message.substring(index);

					dialog = new Alert(Alert.AlertType.ERROR);
					dialog.setHeaderText("Errore nell'inserimento dell'auto");
					dialog.setContentText(message);

				}

				dialog.setTitle("Aggiungi auto");
				dialog.showAndWait();

				onRefreshAuto(event);
			}
		}).showAndWait();

	}

	@FXML
	private void onEditAuto(ActionEvent event) {
		Auto selectedAuto = listaAutoView.getSelectionModel().getSelectedItem();

		new AutoDialog(AutoDialog.ViewMode.EDIT, selectedAuto).onResult(auto -> {

			if(auto != null) {
				AutoDao dao = new AutoDao();
				System.out.println(dao.update(selectedAuto, auto.values()));
				Alert alert;

				if(!dao.error()) {

					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Auto modificata con successo");

				} else {

					String message = dao.errorMessage();
					int index = message.indexOf("Detail");
					if(index != -1)
						message = message.substring(index);

					alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText("Errore nella modifica dell'auto");
					alert.setContentText(message);

				}

				alert.setTitle("Modifica auto");
				alert.showAndWait();
				onRefreshAuto(event);
			}
		}).showAndWait();

	}

	@FXML
	private void onDeleteAuto(ActionEvent event) {

		Auto auto = listaAutoView.getSelectionModel().getSelectedItem();
		String targa = auto.getTarga();

		new Alert(Alert.AlertType.CONFIRMATION,
				"Confermi di voler rimuovere l'auto con targa " + targa + "?",
				ButtonType.YES,
				ButtonType.NO).showAndWait().filter(ButtonType.YES::equals).ifPresent(buttonType -> {
			Alert alert;
			AutoDao dao = new AutoDao();

			if(dao.delete(auto) == 1) {
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Auto rimossa con successo");
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Errore nella rimozione dell'auto");
				alert.setContentText(dao.errorMessage());
			}

			alert.setTitle("Elimina auto");
			alert.showAndWait();
			onRefreshAuto(event);

		});
	}

	@FXML
	private void onRefreshAuto(ActionEvent event) {
		AutoDao dao = new AutoDao();
		List<Auto> list = dao.getAll();

		if(dao.error()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Errore nel caricamento delle auto");
			alert.setContentText(dao.errorMessage());
		}

		listaAutoView.getItems().clear();
		listaAutoView.getItems().addAll(list);

		if(listaAutoView.getSelectionModel().getSelectedItem() == null)
			if(listaAutoView.getItems().size() != 0)
				Platform.runLater(() -> listaAutoView.getSelectionModel().select(0));

	}

	@FXML
	private void onRefreshLavorazione(ActionEvent event) {
		LavorazioneDao dao = new LavorazioneDao(selectedAuto);

		listaLavorazioniView.getItems().clear();
		listaLavorazioniView.getItems().addAll(dao.getAll());

		if(dao.error())
			new Alert(Alert.AlertType.ERROR, dao.errorMessage()).showAndWait();
	}

	@FXML
	private void onAddLavorazione(ActionEvent event) {

		new CustomLavorazioneDialog(CustomLavorazioneDialog.ViewMode.ADD, selectedAuto, null).onResult(lavorazione -> {
			if(lavorazione != null) {

				LavorazioneDao dao = new LavorazioneDao(lavorazione.getAuto());
				dao.save(lavorazione);

				if(dao.error())
					throw new RuntimeException(dao.errorMessage());

			}
		}).showAndWait();

		onRefreshLavorazione(event);
	}

	@FXML
	private void onEditLavorazione(ActionEvent event) {

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel().getSelectedItem();

		if(selectedLavorazione == null)
			new JFXSnackbar((StackPane) getScene().lookup("#rootPane")).enqueue(new SnackbarEvent(new JFXSnackbarLayout(
					"Seleziona una lavorazione prima!")));

		else
			new CustomLavorazioneDialog(CustomLavorazioneDialog.ViewMode.EDIT, selectedAuto, selectedLavorazione).onResult(lavorazione -> {

				if(lavorazione != null) {
					JFXSnackbarLayout toastLayout;

					LavorazioneDao dao = new LavorazioneDao(lavorazione.getAuto());
					if(dao.update(selectedLavorazione, lavorazione.values()) == 1)
						toastLayout = new JFXSnackbarLayout("Lavorazione aggiornata con successo");
					else
						toastLayout = new JFXSnackbarLayout("Non sono riuscito ad aggiornare la lavorazione :(");

					if(dao.error())
						throw new RuntimeException(dao.errorMessage());

					new JFXSnackbar((StackPane) getScene().lookup("#rootPane")).enqueue(new SnackbarEvent(toastLayout));
				}

			}).showAndWait();

	}

	@FXML
	private void onRemoveLavorazione(ActionEvent event) {

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel().getSelectedItem();

		if(selectedLavorazione == null)
			new Alert(Alert.AlertType.INFORMATION, "Seleziona una lavorazione prima!").showAndWait();
		else


			new Alert(Alert.AlertType.CONFIRMATION,
					"Confermi di voler rimuovere la lavorazione selezionata?",
					ButtonType.YES,
					ButtonType.NO).showAndWait().filter(ButtonType.YES::equals).ifPresent(buttonType -> {
				Alert alert;
				LavorazioneDao dao = new LavorazioneDao(selectedAuto);

				if(dao.delete(selectedLavorazione) == 1) {
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("lavorazione rimossa con successo");
				} else {
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText("Errore nella rimozione della lavorazione");
					alert.setContentText(dao.errorMessage());
				}

				alert.setTitle("Elimina Lavorazione");
				alert.showAndWait();
				onRefreshAuto(event);

			});

	}

	@FXML
	private void quit(Event event) {
		{
			Platform.exit();
		}

	}

}
