package io.github.danifascio.gui.controllers;

import io.github.danifascio.beans.Auto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.UncheckedIOException;

public class AutoCellController {

	private final Pane view;
	@FXML
	private Label modelLabel;
	@FXML
	private Label plateLabel;
	@FXML
	private Label ownerLabel;
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
