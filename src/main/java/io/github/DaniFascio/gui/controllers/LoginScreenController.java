package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.DatabaseManager;
import io.github.DaniFascio.gui.Gui;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginScreenController {

	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	public static Pane loadRoot() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LoginScreenController.class.getResource("/fxml/loginScreen.fxml"));
		return fxmlLoader.load();
	}

	private BorderPane getRootPane() {
		return rootPane;
	}

	@FXML
	private void login(Event event) {
		
		if(event instanceof KeyEvent)
			if(!((KeyEvent) event).getCode().equals(KeyCode.ENTER))
				return;

		DatabaseManager.setUsername(usernameField.getText());
		DatabaseManager.setPassword(passwordField.getText());

		try {
			DatabaseManager.fromConfig(true);
		} catch(SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Impossibile connettersi al database");
			alert.setContentText(e.getMessage());
			alert.show();
			return;
		}

		Gui.getInstance().changeScreen(Gui.MANAGER);
	}

}
