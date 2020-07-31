package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.io.UncheckedIOException;

public class AutoCell extends ListCell<Auto> {

	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;

	private final Node view;

	public AutoCell() {
		super();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AutoCell.fxml"));
		loader.setController(this);
		try {
			view = loader.load();
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	protected void updateItem(Auto item, boolean empty) {
		super.updateItem(item, empty);
		if(empty) {
			setGraphic(null);
		} else {
			modelLabel.setText(item.getModello());
			plateLabel.setText("[" + item.getTarga() + "]");
			setGraphic(view);
		}
	}

}
