package io.github.DaniFascio.gui;

import io.github.DaniFascio.Auto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AutoPane extends AnchorPane {

	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;
	@FXML
	private Label ownerLabel;

	private AutoPane(Node... nodes) {
		super(nodes);

	}

	public static AutoPane load(String model, String plate, String owner) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(AutoPane.class.getResource("/fxmlPanes/AutoPane.fxml"));
		AutoPane autoPane = new AutoPane((Node[]) fxmlLoader.<AnchorPane>load().getChildren()
				.toArray());
		autoPane.modelLabel.setText(model);
		autoPane.plateLabel.setText(plate);
		autoPane.ownerLabel.setText(owner);
		return autoPane;

	}

}
