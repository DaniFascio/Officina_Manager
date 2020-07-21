package io.github.DaniFascio.gui.anew;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.AutoDao;
import io.github.DaniFascio.DatabaseManager;
import io.github.DaniFascio.gui.components.AutoCell;
import io.github.DaniFascio.gui.controllers.AutoAddDialog;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;

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

	public CentralPane() throws SQLException {
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
			listaAutoView.getItems().addAll(new AutoDao().getAll());
			listaAutoView.getSelectionModel()
					.selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> {
						targaLabel.setText(newValue.getTarga());
						modelloLabel.setText(newValue.getModello());
						kmLabel.setText(newValue.getKm().toString());
						misuraGommeLabel.setText(newValue.getMisuraGomme());
						tipoGommeLabel.setText(newValue.getTipoGomme()
								.getDescrizione());
						noteArea.setText(newValue.getNote());
					});

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@FXML
	private void onAdd(ActionEvent event) {

		new AutoAddDialog().showAndWait().ifPresent(auto -> {
			Dialog<ButtonType> dialog;
			if(new AutoDao().save(auto) == 1)
				dialog = new Alert(Alert.AlertType.INFORMATION, "Auto aggiunta con successo.");
			else
				dialog = new Alert(Alert.AlertType.ERROR, "Errore nell'inserimento dell'auto");
			dialog.setTitle("Aggiungi auto");
			dialog.showAndWait();
		});

	}

}
