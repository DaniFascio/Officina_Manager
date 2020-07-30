package io.github.danifascio;

import io.github.danifascio.gui.LoginPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;

public class Gui extends Application {

	private static Gui instance;
	private Scene scene;
	private Stage stage;

	public static void main(String[] args) {
		launch();
	}

	public static Gui getInstance() {
		return instance;
	}

	@FXML
	private static void quit(Event event) {
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage) {
		instance = this;
		stage = primaryStage;
//		stage.initStyle(StageStyle.UNDECORATED);

		Pane pane = new LoginPane();

		stage.setScene(scene = new Scene(pane));
		stage.setMinHeight(pane.getMinHeight());
		stage.setMinWidth(pane.getMinWidth());
		stage.setTitle("Officina Manager");

		stage.show();
	}

	public void changeScreen(Region pane) {

		double x = stage.getX(), y = stage.getY();
		double w = pane.getPrefWidth(), h = pane.getPrefHeight();

		stage.setX(x - (w - scene.getWidth()) / 2d);
		stage.setY(y - (h - scene.getHeight()) / 2d);

		stage.setMinHeight(pane.getMinHeight());
		stage.setMinWidth(pane.getMinWidth());
		stage.setHeight(pane.getPrefHeight());
		stage.setWidth(pane.getPrefWidth());
		scene.setRoot(pane);

	}

}
