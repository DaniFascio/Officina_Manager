package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.AutoDao;
import io.github.DaniFascio.gui.Screen;
import io.github.DaniFascio.gui.components.AutoCell;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
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
				});
	}

	@FXML
	private void onAdd(ActionEvent actionEvent) {
		leftList.getItems()
				.add(new Auto("XX000XX", "Fiat", 1000, "Boh", "Qualcosa", null));
	}

	@FXML
	private void onReload(ActionEvent event) {
		AutoDao autoDao = new AutoDao();
		List<Auto> list = autoDao.getAll();

		leftList.getItems().addAll(list);
	}

	@Override
	public Pane getView() {
		return view;
	}

}
