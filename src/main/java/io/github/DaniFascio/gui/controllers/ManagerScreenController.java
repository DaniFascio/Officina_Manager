package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.AutoDao;
import io.github.DaniFascio.gui.AutoCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerScreenController implements Initializable {

	@FXML
	private ListView<?> rightList;
	@FXML
	private ListView<Auto> leftList;

	public static Pane loadRoot() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LoginScreenController.class.getResource("/fxml/ManagerScreen.fxml"));


		return fxmlLoader.load();
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

		leftList.getItems()
				.addAll(list);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leftList.setCellFactory(param -> new AutoCell());
	}

}
