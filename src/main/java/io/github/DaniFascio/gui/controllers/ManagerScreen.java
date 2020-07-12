package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.AutoDao;
import io.github.DaniFascio.gui.Screen;
import io.github.DaniFascio.gui.components.AutoCell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class ManagerScreen implements Screen {

	@FXML
	private ListView<?> rightList;
	@FXML
	private ListView<Auto> leftList;

	@FXML
	private Label targaLabel;
	@FXML
	private Label modelloLabel;
	@FXML
	private Label kmLabel;
	@FXML
	private Label misuraGommeLabel;
	@FXML
	private Label tipoGommeLabel;
	@FXML
	private Label totaleLavorazioniLabel;
	@FXML
	private Label noteLabel;

	private final Pane view;

	public ManagerScreen() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/ManagerScreen.fxml"));
			loader.setController(this);
			view = loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		AutoDao autoDao = new AutoDao();

		leftList.setCellFactory(param -> new AutoCell());
		leftList.getItems().addAll(autoDao.getAll());
		leftList.getSelectionModel()
				.selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					targaLabel.setText(newValue.getTarga());
					modelloLabel.setText(newValue.getModello());
					kmLabel.setText(newValue.getKm().toString());
					misuraGommeLabel.setText(newValue.getMisuraGomme());
					tipoGommeLabel.setText(newValue.getTipoGomme()
							.getDescrizione());
					totaleLavorazioniLabel.setText("0");
					noteLabel.setText(newValue.getNote());
				});
	}

	@FXML
	private void onAdd(ActionEvent actionEvent) {
		Dialog<Auto> dialog = new AutoAddDialog();

		dialog.showAndWait().ifPresent(auto -> {
			int res = new AutoDao().save(auto);

			Alert alert;
			if(res == 0) {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Auto non aggiunta.");
			} else {
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Auto aggiunta!");
			}
			alert.setTitle("Registra auto");
			alert.showAndWait();
		});
	}

	@FXML
	private void onReload(ActionEvent event) {
		AutoDao autoDao = new AutoDao();
		List<Auto> list = autoDao.getAll();

		leftList.getItems().clear();
		leftList.getItems().addAll(list);
	}

	@FXML
	private void quit(Event event) {
		Platform.exit();
	}

	@FXML
	private void toImplement(Event event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Da implementare", new ButtonType("Oke", ButtonBar.ButtonData.OK_DONE));
		alert.setHeaderText("Pazienta per favore");
		alert.showAndWait();
	}

	@Override
	public Pane getView() {
		return view;
	}

}
