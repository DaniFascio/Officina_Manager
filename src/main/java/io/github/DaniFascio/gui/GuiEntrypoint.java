package io.github.DaniFascio.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiEntrypoint extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setScene(LoginScene.loadScene());
		primaryStage.setTitle("Officina Manager");
		primaryStage.show();
	}

}
