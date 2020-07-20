package io.github.DaniFascio.gui;

import io.github.DaniFascio.gui.anew.LoginPane;
import io.github.DaniFascio.gui.controllers.LoginScreen;
import io.github.DaniFascio.gui.controllers.ManagerScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

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

	@FXML
	private static void quit(Event event) {
		Platform.exit();
	}

}
