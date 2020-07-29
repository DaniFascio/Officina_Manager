package io.github.DaniFascio.gui.anew;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.AutoDao;
import io.github.DaniFascio.DatabaseManager;
import io.github.DaniFascio.gui.components.AutoCell;
import io.github.DaniFascio.gui.controllers.AutoDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;

public class CentralPane extends AnchorPane {

	@FXML
	private AnchorPane root;
	@FXML
	private ListView<Auto> listaAutoView;
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

	public CentralPane() {
		super();

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/new/Central.fxml"));
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

						targaLabel.setText(newValue.getTarga());
						modelloLabel.setText(newValue.getModello());
						kmLabel.setText(newValue.getKm().toString());
						misuraGommeLabel.setText(newValue.getMisuraGomme());
						tipoGommeLabel.setText(newValue.getTipoGomme()
								.getDescrizione());
						noteArea.setText(newValue.getNote());
					});
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

		if(listaAutoView.getItems().size() != 0)
			Platform.runLater(() -> listaAutoView.getSelectionModel()
					.select(0));

	}

}
