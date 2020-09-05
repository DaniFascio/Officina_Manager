package io.github.danifascio.gui.dialogs;

import io.github.danifascio.Gui;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

public class FatalDialog extends CustomDialog<Void> {

	public FatalDialog(String title, String context) {
		super(title, Gui.iconPaths().getProperty("warning"), Modality.APPLICATION_MODAL);
		setOnCloseRequest(event -> Platform.exit());
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));

		for(String string : context.split("[\n\r]")) {
			Label label = new Label(string);
			label.setFont(Font.font(17));
			vBox.getChildren().add(label);
		}

		addButton("OK", event -> fireEvent(new WindowEvent(getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
		setContent(vBox);
		showAndWait();
	}

}
