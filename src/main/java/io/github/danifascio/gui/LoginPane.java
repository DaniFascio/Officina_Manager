package io.github.danifascio.gui;

import io.github.danifascio.DatabaseManager;
import io.github.danifascio.Gui;
import io.github.danifascio.beans.TipoGomme;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

public class LoginPane extends BorderPane {

	@FXML
	private VBox formPane;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	public LoginPane() {
		super();

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/LoginPane.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		Platform.runLater(this::requestFocus);
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
				Gui.changeStage("Officina Manager", new CentralPane(), true);

			} catch(Exception e) {
				e.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Errore");
				alert.setHeaderText("Impossibile connettersi al database");
				alert.setContentText("Inserire correttamente le credenziali");
				alert.showAndWait();
				formPane.setDisable(false);
			}
		});
	}

	@FXML
	private void handleKey(KeyEvent event) {
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
			field.positionCaret(pos);
			event.consume();

		} else if(event.getCode().equals(KeyCode.ENTER))
			login(event);

	}

	@FXML
	private void reportBug(Event event) {
		Desktop desktop = Desktop.getDesktop();
		String message = "mailto:danifascio02@gmail.com?cc=dev_excale@hotmail.com&subject=%27db_officina%27%20Bug%20Report";
		URI uri = URI.create(message);

		try {

			desktop.mail(uri);

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@FXML
	private void openSettings(Event event) {
		Gui.createStage("Impostazioni", "settings", new SettingsPane(), false, Modality.APPLICATION_MODAL).showAndWait();
	}

	@FXML
	private void toImplement(Event event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Da implementare", new ButtonType("Oke", ButtonBar.ButtonData.OK_DONE));
		alert.setHeaderText("Pazienta per favore");
		alert.showAndWait();
	}

	@FXML
	private void quit(Event event) {

		Stage stage = Gui.stage();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

	}

}
