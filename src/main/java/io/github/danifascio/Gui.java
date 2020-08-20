package io.github.danifascio;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class Gui extends Application {

	public static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
	public static final int ICON_SIZE = 20;

	private static Properties iconsPath;
	private static ResourceBundle lang;
	private static Logger logger;
	private static Stage stage;


	static {
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

		logger = LoggerFactory.getLogger(getClass());
		Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
			logger.error("Uncaught Exception", e);
			new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
		});

		File dir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\db_officina");
		if(!dir.exists())
			dir.mkdirs();

		lang = ResourceBundle.getBundle("Strings");
		if(lang == null)
			new FatalDialog("Fatal Error", "Couldn't get language resources.\nTry to reinstall the program.");

		iconsPath = new Properties();
		try(InputStream input = getClass().getResourceAsStream("glyphs.xml")) {

			if(input != null)
				iconsPath.loadFromXML(input);
			else
				logger.error("Couldn't load icons");

		} catch(IOException e) {
			logger.error("Error during loading icons",e);
		}

		BundleManager.load("glyphs", true);
		GlyphFactory.init();

		if(BundleManager.get("glyphs") != null)
			logger.info("Glyphs bundle loaded");
		else
			logger.error("Couldn't load Glyphs bundle");

		changeStage(lang.getString("menu.secondary"), new LoginPane(), false);
	}

	public static Stage stage() {
		return stage;
	}

	public static ResourceBundle lang() {
		return lang;
	}

	public static Properties iconPaths() {
		return iconsPath;
	}

	public static void changeStage(String title, Region content, boolean resizable) {

		Stage newStage = new Stage();
		StackPane rootPane = new StackPane(content);
		rootPane.getStylesheets().add("/css/Root.css");
		rootPane.setId("rootPane");

		double height = content.getMinHeight() + 36, width = content.getMinWidth() + 8;
		SVGGlyph glyph = new SVGGlyph(iconsPath.getProperty("gear-fill"), Color.WHITE);
		glyph.setSize(ICON_SIZE);

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
			lightDialog.heading(lang.getString("menu.close"))
					.content(lang.getString("menu.exit_request.confirm"))
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

	public static Stage createStage(String title, String icon, Region content, boolean resizable, Modality modality) {

		Stage stage = new Stage();
		StackPane rootPane = new StackPane(content);
		rootPane.getStylesheets().add("/css/Root.css");
		rootPane.setId("rootPane");

		double height = content.getMinHeight() + 36, width = content.getMinWidth() + 8;
		JFXDecorator decorator = new JFXDecorator(stage, rootPane, false, resizable, true);
		decorator.setGraphic(GlyphFactory.create(icon, Color.WHITE, ICON_SIZE));

		stage.setScene(new Scene(decorator));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(modality);
		stage.setResizable(false);
		stage.setTitle(title);

		stage.setMinHeight(height);
		stage.setMinWidth(width);
		stage.setHeight(height);
		stage.setWidth(width);

		return stage;
	}

}
