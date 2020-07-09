package io.github.DaniFascio.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoginScreen {

	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	public static Pane loadRoot() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LoginScreen.class.getResource("/fxmlScreens/loginScreen.fxml"));
		return fxmlLoader.load();
	}

	private BorderPane getRootPane() {
		return rootPane;
	}

	@FXML
	private void closeWindow() {
		Platform.exit();
	}

	@FXML
	private void closeWindowEnter(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER))
			Platform.exit();
	}

}
