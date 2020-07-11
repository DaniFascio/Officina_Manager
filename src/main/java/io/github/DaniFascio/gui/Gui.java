package io.github.DaniFascio.gui;

import io.github.DaniFascio.gui.controllers.LoginScreen;
import io.github.DaniFascio.gui.controllers.ManagerScreen;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Gui extends Application {

	public static void main(String[] args) {
		launch();
	}

	private static Gui instance;
	private Scene scene;
	private Stage stage;

	public static Gui getInstance() {
		return instance;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		instance = this;
		Pane pane = Screen.LOGIN.loadView();
		scene = new Scene(pane);

		stage = primaryStage;
		stage.setScene(scene);

		stage.setTitle("Officina Manager");
		stage.setMinHeight(pane.getMinHeight());
		stage.setMinWidth(pane.getMinWidth());

		stage.show();
	}

	public void changeScreen(Screen screen) {
		Pane root = null;

		try {
			root = screen.loadView();
		} catch(Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Errore nel cambio di finestra");
			alert.setContentText(e.getMessage());
			alert.show();
			return;
		}

		if(root != null) {

			double x = stage.getX(), y = stage.getY();
			double w = root.getPrefWidth(), h = root.getPrefHeight();

			stage.setX(x - (w - scene.getWidth()) / 2d);
			stage.setY(y - (h - scene.getHeight()) / 2d);

			stage.setMinHeight(root.getMinHeight());
			stage.setMinWidth(root.getMinWidth());
			stage.setHeight(root.getPrefHeight());
			stage.setWidth(root.getPrefWidth());
			scene.setRoot(root);
		}
	}

	public enum Screen {

		LOGIN(LoginScreen.class),
		MANAGER(ManagerScreen.class);

		private final Class<? extends io.github.DaniFascio.gui.Screen> aClass;

		Screen(Class<? extends io.github.DaniFascio.gui.Screen> aClass) {
			this.aClass = aClass;
		}

		public Pane loadView() throws IllegalAccessException, InstantiationException {
			return aClass.newInstance().getView();
		}

	}

}
