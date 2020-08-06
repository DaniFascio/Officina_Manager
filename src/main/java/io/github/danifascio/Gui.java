package io.github.danifascio;

import com.jfoenix.controls.JFXDecorator;
import io.github.danifascio.gui.LoginPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Gui extends Application {

	private static Stage stage;

	public static void main(String[] args) {
		launch();
	}

	@FXML
	private static void quit(Event event) {
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage) {

		// TODO: Default uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
		});

		changeStage("Officina Manager", new LoginPane(), false);
	}

	public static Stage stage() {
		return stage;
	}

	public static void changeStage(String title, Region content, boolean resizable) {

		Stage newStage = new Stage();
		newStage.setScene(new Scene(new JFXDecorator(newStage, content, false, resizable, true)));
		newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setResizable(resizable);
		newStage.setTitle(title);
		newStage.setOnCloseRequest(event -> new Alert(Alert.AlertType.CONFIRMATION, "Vuoi uscire?", ButtonType.YES, ButtonType.NO)
				.showAndWait()
				.filter(ButtonType.NO::equals)
				.ifPresent(buttonType -> event.consume()));

		if(stage != null)
			stage.close();
		stage = newStage;
		stage.show();

	}

}
