package io.github.danifascio.gui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class MenuBarController {

	@FXML
	private void toImplement(Event event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"action.toimplement",
				new ButtonType("Oke", ButtonBar.ButtonData.OK_DONE));
		alert.setHeaderText("menu.patient");
		alert.showAndWait();
	}

	@FXML
	private void quit(Event event) {

		new Alert(Alert.AlertType.CONFIRMATION, "menu.exit_request", ButtonType.YES, ButtonType.NO).showAndWait()
				.filter(ButtonType.YES::equals)
				.ifPresent(buttonType -> Platform.exit());

	}

}
