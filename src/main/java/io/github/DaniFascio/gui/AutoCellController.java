package io.github.DaniFascio.gui;

import io.github.DaniFascio.Auto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.UncheckedIOException;

public class AutoCellController {

	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;
	@FXML
	private Label ownerLabel;
	private final VBox node;

	private Auto auto;

	public AutoCellController() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AutoCell.fxml"));
			loader.setController(this);
			node = loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public void setAuto(Auto auto) {
		this.auto = auto;
		modelLabel.setText(auto.getModello());
		plateLabel.setText(auto.getTarga());
		ownerLabel.setText("Emanuele Scaccia");
	}

	public Node getView() {
		return node;
	}

}
