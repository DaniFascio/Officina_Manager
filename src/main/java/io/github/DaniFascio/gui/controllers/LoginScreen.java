package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.DatabaseManager;
import io.github.DaniFascio.gui.Gui;
import io.github.DaniFascio.gui.Screen;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginScreen implements Screen {

	@FXML
	private GridPane formPane;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	private final Pane view;

	public LoginScreen() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/LoginScreen.fxml"));
			loader.setController(this);
			view = loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	@FXML
	private void login(Event event) {

		if(event instanceof KeyEvent)
			if(!((KeyEvent) event).getCode().equals(KeyCode.ENTER))
				return;

		formPane.setDisable(true);

		DatabaseManager.setUsername(usernameField.getText());
		DatabaseManager.setPassword(passwordField.getText());

		try {
			DatabaseManager.fromConfig(true);
		} catch(SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Impossibile connettersi al database");
			alert.setContentText(e.getMessage());
			alert.setOnCloseRequest(event1 -> formPane.setDisable(false));
			alert.showAndWait();
			return;
		}

		Gui.getInstance().changeScreen(Gui.Screen.MANAGER);
	}

	@FXML
	private void ctrlDelete(KeyEvent event) {
		KeyCombination kc1 = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);
		KeyCombination kc2 = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN);
		if(kc1.match(event)) {
			TextField field = (TextField) event.getSource();
			String text = field.getText();
			int pos = field.getCaretPosition();

			field.setText(text.substring(pos));
			event.consume();
		} else if(kc2.match(event)) {
			TextField field = (TextField) event.getSource();
			String text = field.getText();
			int pos = field.getCaretPosition();

			field.setText(text.substring(0, pos));
			event.consume();
		}
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
