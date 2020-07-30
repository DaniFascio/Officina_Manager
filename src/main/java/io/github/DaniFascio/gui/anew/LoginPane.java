package io.github.DaniFascio.gui.anew;

import io.github.DaniFascio.DatabaseManager;
import io.github.DaniFascio.TipoGomme;
import io.github.DaniFascio.gui.Gui;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;

public class LoginPane extends BorderPane {

	@FXML
	private GridPane formPane;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	public LoginPane() {
		super();

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/new/LoginPane.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();

		} catch(IOException e) {
			Platform.exit();
			throw new UncheckedIOException(e);
		}
	}

	@FXML
	private void login(Event event) {

		if(event instanceof KeyEvent)
			if(!((KeyEvent) event).getCode().equals(KeyCode.ENTER))
				return;

		formPane.setDisable(true);

		DatabaseManager.setUsername(usernameField.getText().toLowerCase());
		DatabaseManager.setPassword(passwordField.getText());

		Platform.runLater(() -> {
			try {

				TipoGomme.reload();
				Gui.getInstance().changeScreen(new CentralPane());

			} catch(SQLException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Errore");
				alert.setHeaderText("Impossibile connettersi al database");
				alert.setContentText(e.getMessage());
				alert.setOnCloseRequest(event1 -> formPane.setDisable(false));
				alert.showAndWait();
				formPane.setDisable(false);
			}
		});
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
	private void toImplement(Event event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Da implementare", new ButtonType("Oke", ButtonBar.ButtonData.OK_DONE));
		alert.setHeaderText("Pazienta per favore");
		alert.showAndWait();
	}

	@FXML
	private void quit(Event event) {
		Platform.exit();
	}

}
