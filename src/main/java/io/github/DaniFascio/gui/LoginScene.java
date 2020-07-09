package io.github.DaniFascio.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LoginScene {

	@FXML
	private BorderPane rootPane;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	public LoginScene() {

	}

	public static Scene loadScene() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LoginScene.class.getResource("/fxmlScenes/loginScene.fxml"));
		return new Scene(fxmlLoader.load());
	}

	private BorderPane getRootPane() {
		return rootPane;
	}

}
