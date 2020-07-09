package io.github.DaniFascio.gui;

import io.github.DaniFascio.Auto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AutoCellController {

	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;
	@FXML
	private Label ownerLabel;
	private final AnchorPane anchorPane;

	private Auto auto;

	public AutoCellController() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlPanes/AutoPane.fxml"));
			loader.setController(this);
			anchorPane = loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public void setAuto(Auto auto) {
		this.auto = auto;
	}

	public Node getView() {
		return anchorPane;
	}

}
