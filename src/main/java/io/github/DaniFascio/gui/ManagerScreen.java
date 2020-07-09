package io.github.DaniFascio.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ManagerScreen {

	@FXML
	private ListView<Pane> rightList;
	@FXML
	private ListView<Pane> leftList;

	public static Pane loadRoot() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LoginScreen.class.getResource("/fxmlScreens/ManagerScreen.fxml"));
		return fxmlLoader.load();
	}

	public void onAddPressed(ActionEvent actionEvent) {
		try {
			Pane pane = AutoPane.load("Fiat", "XX000XX", "Emanuele Scaccia");
			pane.setPrefHeight(-1);
			pane.setPrefWidth(leftList.getWidth());
			leftList.getItems().add(pane);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
