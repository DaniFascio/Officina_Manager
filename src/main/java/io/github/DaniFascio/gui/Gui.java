package io.github.DaniFascio.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class Gui extends Application {

	public static void main(String[] args) {
		launch();
	}

	private static Gui instance;
	private HashMap<String, Pane> screenMap;
	private Scene scene;
	private Stage stage;

	public static final String LOGIN = "login";
	public static final String MANAGER = "manager";

	public static Gui getInstance() {
		return instance;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		instance = this;
		screenMap = new HashMap<>();
		screenMap.put(LOGIN, LoginScreenController.loadRoot());
		screenMap.put(MANAGER, ManagerScreenController.loadRoot());
		scene = new Scene(screenMap.get(LOGIN));

		stage = primaryStage;
		stage.setScene(scene);
		stage.setTitle("Officina Manager");
		stage.show();
	}

	public void changeScreen(String screen) {
		Pane root = screenMap.get(screen);

		if(root != null) {

			double x = stage.getX(), y = stage.getY();
			double w = root.getPrefWidth(), h = root.getPrefHeight();

			stage.setX(x - (w - scene.getWidth()) / 2d);
			stage.setY(y - (h - scene.getHeight()) / 2d);

			stage.setHeight(root.getPrefHeight());
			stage.setWidth(root.getPrefWidth());
			scene.setRoot(root);
		}
	}

}
