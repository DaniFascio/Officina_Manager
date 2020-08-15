package io.github.danifascio;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.gui.AutoDialog;
import io.github.danifascio.gui.LightDialog;
import io.github.danifascio.gui.LoginPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

		BundleManager.load("glyphs", true);
		if(BundleManager.get("glyphs") != null)
			System.out.println("Glyphs bundle loaded");
		else
			System.err.println("Couldn't load Glyphs bundle");

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

		double height = content.getMinHeight() + 36, width = content.getMinWidth() + 8;
		SVGGlyph glyph = new SVGGlyph(icons.getProperty("gear-fill"), Color.WHITE);
		glyph.setSize(18);

		JFXDecorator decorator = new JFXDecorator(newStage, rootPane, false, resizable, true);
		decorator.setGraphic(glyph);

		newStage.setScene(new Scene(decorator));
		newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setResizable(resizable);
		newStage.setTitle(title);
		newStage.setOnCloseRequest(event -> {
			event.consume();

			AtomicReference<Button> buttonReference = new AtomicReference<>(), yesButton = new AtomicReference<>();

			LightDialog lightDialog = new LightDialog(rootPane, true);
			lightDialog.heading("Chiudi Applicazione")
					.content("Confermi di voler uscire?")
					.onClose(event1 -> Optional.ofNullable(buttonReference.get())
							.filter(yesButton.get()::equals)
							.ifPresent(button -> Platform.exit()))
					.addButton("Sì", button -> {
						yesButton.set(button);
						return actionEvent -> {
							buttonReference.set(button);
							lightDialog.close();
						};
					})
					.addButton("No", button -> actionEvent -> {
						buttonReference.set(button);
						lightDialog.close();
					})
					.show();
		});
		newStage.setMinHeight(height);
		newStage.setMinWidth(width);
		newStage.setHeight(height);
		newStage.setWidth(width);

		if(stage != null)
			stage.close();
		stage = newStage;
		stage.show();

	}

}
