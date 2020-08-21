package io.github.danifascio.gui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CustomDialog<T> extends Stage {

	private Consumer<T> onResult;
	private ResultConverter<T> resultConverter;
	private final BorderPane borderPane;
	private final HBox buttonBox;
	private final JFXDecorator decorator;

	protected CustomDialog(String title, @Nullable String iconSVGPath, Modality modality) {
		super(StageStyle.UNDECORATED);
		initModality(modality);
		SVGGlyph glyph;

		resultConverter = () -> null;
		onResult = t -> {};

		buttonBox = new HBox(30f);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(30));

		borderPane = new BorderPane();
		borderPane.setBottom(buttonBox);
		borderPane.getStylesheets().add("/css/Root.css");
		borderPane.requestFocus();

		setTitle(title);
		setScene(new Scene(decorator = new JFXDecorator(this, borderPane, false, false, true)));
		getScene().getStylesheets().add("/css/Root.css");

		if(iconSVGPath != null) {
			glyph = new SVGGlyph(iconSVGPath, Color.WHITE);
			glyph.setSize(12);
			decorator.setGraphic(glyph);
		}
	}

	protected void setResultConverter(ResultConverter<T> converter) {
		resultConverter = converter;
	}

	protected void setContent(Region node) {
		decorator.setMinSize(node.getMinWidth(), node.getMinHeight() + 40);
		borderPane.setCenter(node);
	}

	protected Button addButton(String text, EventHandler<ActionEvent> onEvent) {

		JFXButton button = new JFXButton(text);
		button.setButtonType(JFXButton.ButtonType.RAISED);
		button.getStyleClass().add("button-raised");
		button.setOnAction(onEvent);

		buttonBox.getChildren().add(button);
		return button;
	}

	public CustomDialog<T> onResult(Consumer<T> consumer) {
		onResult = consumer;
		return this;
	}

	protected void setResult(T t) {
		close();
		onResult.accept(t);
	}

	protected void done() {
		close();
		onResult.accept(resultConverter.convert());
	}

	public interface ResultConverter<T> {

		T convert();

	}

}
