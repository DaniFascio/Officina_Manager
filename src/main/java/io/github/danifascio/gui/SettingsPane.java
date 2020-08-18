package io.github.danifascio.gui;

import io.github.danifascio.DatabaseManager;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SettingsPane extends BorderPane {

	@FXML
	private TextField hostField;
	@FXML
	private TextField portField;

	private boolean save;

	public SettingsPane() {
		save = false;

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/Settings.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		hostField.setText(DatabaseManager.getHostname());
		portField.setText(DatabaseManager.getPort());

		Platform.runLater(() -> {
			Window window = getScene().getWindow();
			window.sizeToScene();

			window.setOnCloseRequest(event -> {
				if(save) {
					DatabaseManager.setHostname(hostField.getText());
					DatabaseManager.setPort(portField.getText());
					DatabaseManager.save();
				}
			});
		});
	}

	@FXML
	private void confirm(Event event) {
		save = true;
		close();
	}

	@FXML
	private void cancel(Event event) {
		close();
	}

	public void close() {
		Window window = getScene().getWindow();
		window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

}
