package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.gui.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.UncheckedIOException;

public class AutoCellController implements Screen {

	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;
	@FXML
	private Label ownerLabel;

	private final Pane view;
	private Auto auto;

	public AutoCellController() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AutoCell.fxml"));
			loader.setController(this);
			view = loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public void setAuto(Auto auto) {
		this.auto = auto;
		modelLabel.setText(auto.getModello());
		plateLabel.setText("[" + auto.getTarga() + "]");
		ownerLabel.setText(auto.getKm().toString());
	}

	public Pane getView() {
		return view;
	}

}
