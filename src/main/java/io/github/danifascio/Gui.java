package io.github.danifascio;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.gui.AutoDialog;
import io.github.danifascio.gui.LoginPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class Gui extends Application {

	// TODO: STRING RESOURCE BUNDLE
	private static Stage stage;
	private static final Properties icons;

	static {
		icons = new Properties();

		try(InputStream input = AutoDialog.class.getClassLoader().getResourceAsStream("glyphs.xml")) {

			if(input != null)
				icons.loadFromXML(input);
			else
				System.err.println("Couldn't load icons properties");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

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
		StackPane rootPane = new StackPane(content);
		rootPane.getStylesheets().add("/css/Root.css");
		rootPane.setId("rootPane");

		SVGGlyph glyph = new SVGGlyph(icons.getProperty("gear-fill"), Color.WHITE);
		glyph.setSize(16);

		JFXDecorator decorator = new JFXDecorator(newStage, rootPane, false, resizable, true);
		decorator.setGraphic(glyph);

		newStage.setScene(new Scene(decorator));
		newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setResizable(resizable);
		newStage.setTitle(title);
		newStage.setOnCloseRequest(event -> {
			event.consume();
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
			dialog.setOverlayClose(true);
			dialog.getStylesheets().add("/css/Root.css");

			JFXButton yesButton = new JFXButton("Sì");
			JFXButton noButton = new JFXButton("No");

			AtomicReference<JFXButton> buttonReference = new AtomicReference<>();
			EventHandler<ActionEvent> eventHandler = event1 -> {
				buttonReference.set((JFXButton) event1.getSource());
				dialog.close();
			};

			yesButton.setOnAction(eventHandler);
			yesButton.getStyleClass().add("button-raised");
			noButton.setOnAction(eventHandler);
			noButton.getStyleClass().add("button-raised");
			dialog.setOnDialogClosed(event1 -> Optional.ofNullable(buttonReference.get())
					.filter(yesButton::equals)
					.ifPresent(button -> Platform.exit()));

			dialogLayout.setBody(new Label("Vuoi uscire?"));
			dialogLayout.setActions(yesButton, noButton);

			dialog.show();
		});

		if(stage != null)
			stage.close();
		stage = newStage;
		stage.show();

	}

}
