package io.github.DaniFascio.gui;

import io.github.DaniFascio.Auto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
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

	public void onAddPressed(ActionEvent actionEvent) {
		leftList.getItems()
				.add(new Auto("XX000XX", "Fiat", 1000, "Boh", "Qualcosa"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leftList.setCellFactory(param -> new AutoCell());
	}

}
