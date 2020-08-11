package io.github.danifascio.gui;

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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
			listaAutoView.getSelectionModel()
					.selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> {
						if(listaAutoView.getItems().size() == 0)
							return;

						selectedAuto = newValue;

						targaLabel.setText(newValue.getTarga());
						modelloLabel.setText(newValue.getModello());
						kmLabel.setText(newValue.getKm().toString());
						misuraGommeLabel.setText(newValue.getMisuraGomme());
						tipoGommeLabel.setText(newValue.getTipoGomme()
								.getDescrizione());
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

		new AutoDialog(false, null).showAndWait().ifPresent(auto -> {
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
		});

	}

	@FXML
	private void onEditAuto(ActionEvent event) {
		Auto selectedAuto = listaAutoView.getSelectionModel().getSelectedItem();

		new AutoDialog(true, selectedAuto).showAndWait().ifPresent(auto -> {
			AutoDao dao = new AutoDao();
			dao.update(selectedAuto, auto.values());
			Dialog<ButtonType> dialog;

			if(!dao.error()) {

				dialog = new Alert(Alert.AlertType.INFORMATION);
				dialog.setHeaderText("Auto modificata con successo");

			} else {

				String message = dao.errorMessage();
				int index = message.indexOf("Detail");
				if(index != -1)
					message = message.substring(index);

				dialog = new Alert(Alert.AlertType.ERROR);
				dialog.setHeaderText("Errore nella modifica dell'auto");
				dialog.setContentText(message);

			}

			dialog.setTitle("Modifica auto");
			dialog.showAndWait();
			onRefreshAuto(event);

		});

	}

	@FXML
	private void onDeleteAuto(ActionEvent event) {

		Auto auto = listaAutoView.getSelectionModel().getSelectedItem();
		String targa = auto.getTarga();

		new Alert(Alert.AlertType.CONFIRMATION, "Confermi di voler rimuovere l'auto con targa " + targa + "?", ButtonType.YES, ButtonType.NO)
				.showAndWait()
				.filter(ButtonType.YES::equals)
				.ifPresent(buttonType -> {
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
				Platform.runLater(() -> listaAutoView.getSelectionModel()
						.select(0));

	}

	@FXML
	private void onRefreshLavorazione(ActionEvent event) {
		LavorazioneDao dao = new LavorazioneDao(selectedAuto);
		listaLavorazioniView.getItems().clear();

		for(Lavorazione lavorazione : dao.getAll())
			listaLavorazioniView.getItems().addAll(lavorazione);

		if(dao.error())
			new Alert(Alert.AlertType.ERROR, dao.errorMessage()).showAndWait();
	}

	@FXML
	private void onAddLavorazione(ActionEvent event) {

		new LavorazioneDialog(selectedAuto, null, LavorazioneDialog.ViewMode.NEW)
				.showAndWait()
				.ifPresent(lavorazione -> {

					LavorazioneDao dao = new LavorazioneDao(lavorazione.getAuto());
					dao.save(lavorazione);

					if(dao.error())
						throw new RuntimeException(dao.errorMessage());

				});

		onRefreshLavorazione(event);
	}

	@FXML
	private void onEditLavorazione(ActionEvent event) {

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel()
				.getSelectedItem();

		if(selectedLavorazione == null)
			new Alert(Alert.AlertType.INFORMATION, "Seleziona una lavorazione prima!")
					.showAndWait();
		else

			// TODO: CONFERMA / REJECT MODIFICA
			new LavorazioneDialog(selectedAuto, selectedLavorazione, LavorazioneDialog.ViewMode.EDIT)
					.showAndWait()
					.ifPresent(lavorazione -> {

						LavorazioneDao dao = new LavorazioneDao(selectedAuto);
						dao.update(selectedLavorazione, lavorazione.values());

						if(dao.error())
							throw new RuntimeException(dao.errorMessage());

					});

	}


	@FXML
	private void onRemoveLavorazione(ActionEvent event){

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel().getSelectedItem();

		if(selectedLavorazione == null)
			new Alert(Alert.AlertType.INFORMATION, "Seleziona una lavorazione prima!")
					.showAndWait();
		else


	new Alert(Alert.AlertType.CONFIRMATION, "Confermi di voler rimuovere la lavorazione selezionata?", ButtonType.YES, ButtonType.NO)
					.showAndWait()
					.filter(ButtonType.YES::equals)
					.ifPresent(buttonType -> {
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
	private void quit(Event event){ {
		Platform.exit();
	}

}

}
