package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.io.UncheckedIOException;

public class LavorazioneCell extends ListCell<Lavorazione> {

	private final Node view;
	@FXML
	private Label dateLabel;
	@FXML
	private Label spesaLabel;
	@FXML
	private Label descrizioneLabel;

	public LavorazioneCell() {
		super();

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneCell.fxml"));
			loader.setController(this);
			view = loader.load();
			setOnMouseClicked(event -> {
				if(event.getClickCount() == 2)
					// TODO: APERTURA LAVORAZIONEDIALOG CON DOPPIO CLICK
//					new LavorazioneDialog()
					;
			});

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	protected void updateItem(Lavorazione item, boolean empty) {
		super.updateItem(item, empty);
		if(empty) {
			setGraphic(null);
		} else {
			dateLabel.setText(item.getData().toString());
			spesaLabel.setText(item.getSpesa().toString());
			descrizioneLabel.setText(item.getDescrizione()
					.replaceAll("[\n\r\t]+", "")
					.substring(0, 100));
			setGraphic(view);
		}
	}

}
