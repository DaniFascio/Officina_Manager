package io.github.danifascio;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.gui.LoginPane;
import io.github.danifascio.gui.dialogs.FatalDialog;
import io.github.danifascio.gui.dialogs.LightDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

// TODO: EDIT RIPPLER FOR ALL
// TODO: FOCUS COLOR
public class Gui extends Application {

	public static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
	public static final String DIR = System.getenv("APPDATA") + "\\Officina Manager";
	public static final int ICON_SIZE = 20;
	public static final int TOAST_DURATION = 3;

	private static Properties iconsPath;
	private static ResourceBundle lang;
	private static Logger logger;
	private static Stage stage;

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

			if(e instanceof SQLException)
				logger.error("Uncaught SQLE[Error Code " + ((SQLException) e).getSQLState() + "]", e);
			else
				logger.error("Uncaught Exception", e);

			new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
		});

		File dir = new File(DIR);
		if(!dir.exists())
			dir.mkdirs();

		// DELETE OLD LOGS
		long currentMillis = new Date().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Stream.of(dir.listFiles((dir1, name) -> {
			boolean b = false;

			try {
				if(name.endsWith(".log")) {
					long millis = currentMillis - sdf.parse(name.split("-")[0].trim()).getTime();
					b = TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS) > 30;
				}
			} catch(ParseException ignored) {
			}

			return b;
		})).forEach(File::delete);

		lang = ResourceBundle.getBundle("lang");
		if(lang == null)
			new FatalDialog("Fatal Error", "Couldn't get language resources.\nTry to reinstall the program.");

		iconsPath = new Properties();
		try(InputStream input = getClass().getResourceAsStream("/glyphs.xml")) {

			if(input != null)
				iconsPath.loadFromXML(input);
			else
				logger.error("Couldn't load icons");

		} catch(IOException e) {
			logger.error("Error during loading icons", e);
		}

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
		rootPane.setId("rootPane");

		double height = content.getMinHeight() + 36, width = content.getMinWidth() + 8;
		SVGGlyph glyph = new SVGGlyph(iconsPath.getProperty("gear-fill"), Color.WHITE);
		glyph.setSize(ICON_SIZE);

		JFXDecorator decorator = new JFXDecorator(newStage, rootPane, false, resizable, true);
		decorator.setGraphic(glyph);

		Scene scene = new Scene(decorator);
		scene.getStylesheets().add("/css/Root.css");

		newStage.setScene(scene);
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
		rootPane.setId("rootPane");

		double height = content.getMinHeight() + 36, width = content.getMinWidth() + 8;
		JFXDecorator decorator = new JFXDecorator(stage, rootPane, false, resizable, true);
		decorator.setGraphic(icon(icon, Color.WHITE, ICON_SIZE));

		Scene scene = new Scene(decorator);
		scene.getStylesheets().add("/css/Root.css");

		stage.setScene(scene);
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

	public static @Nullable Node icon(String glyph, Paint color, int size) {
		String path = iconsPath.getProperty(glyph);
		if(path == null)
			return null;

		Rectangle rectangle = new Rectangle(size, size);
		SVGGlyph svgGlyph = new SVGGlyph(path, color);
		StackPane stackPane = new StackPane(rectangle, svgGlyph);

		rectangle.setVisible(false);
		stackPane.setMaxSize(size, size);
		svgGlyph.setSize(size);

		return stackPane;
	}

}
