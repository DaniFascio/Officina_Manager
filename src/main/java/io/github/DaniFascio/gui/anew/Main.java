package io.github.DaniFascio.gui.anew;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		LoginPane root = new LoginPane();
		stage = primaryStage;

		primaryStage.setMinHeight(root.getMinHeight());
		primaryStage.setMinWidth(root.getMinWidth());
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}

	public static Stage getStage() {
		return stage;
	}

}
