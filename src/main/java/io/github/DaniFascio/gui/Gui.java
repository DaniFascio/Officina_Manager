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

	public static final String LOGIN = "login";
	public static final String MANAGER = "manager";

	public static Gui getInstance() {
		return instance;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		instance = this;
		screenMap = new HashMap<>();
		screenMap.put(LOGIN, LoginScreen.loadRoot());
		screenMap.put(MANAGER, ManagerScreen.loadRoot());
		scene = new Scene(screenMap.get(MANAGER));

		primaryStage.setScene(scene);
		primaryStage.setTitle("Officina Manager");
		primaryStage.show();
	}

	public void changeScreen(String screen) {
		Pane root = screenMap.get(screen);
		if(root != null)
			scene.setRoot(root);
	}

}
