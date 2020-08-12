package io.github.danifascio.gui;

import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

public class LavorazioneCell extends ListCell<Lavorazione> {

	@FXML
	private Label dateLabel;
	@FXML
	private Label spesaLabel;
	@FXML
	private Label descrizioneLabel;

	private final Node view;
	private final AtomicReference<Lavorazione> lavorazioneReference;
	private Lavorazione lav;

	public LavorazioneCell() {
		super();
		lavorazioneReference = new AtomicReference<>();

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneCell.fxml"));
			loader.setController(this);
			view = loader.load();
			setOnMouseClicked(event -> {
				// TODO: APERTURA LAVORAZIONEDIALOG CON DOPPIO CLICK
				if(event.getClickCount() == 2)
					try {

						Lavorazione lavorazione = lavorazioneReference.get();

						new CustomLavorazioneDialog(CustomLavorazioneDialog.ViewMode.VIEW, lavorazione.getAuto(), lavorazione).show();

					} catch(NullPointerException e) {
						throw new RuntimeException("Lavorazione null in ListCell", e);
					}
			});

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	protected void updateItem(Lavorazione item, boolean empty) {
		super.updateItem(item, empty);
		lavorazioneReference.set(item);

		if(empty)
			setGraphic(null);

		else {
			String descrizione = item.getDescrizione().replaceAll("[\n\r\t]+", " ");
			if(descrizione.length() > 30)
				descrizione = descrizione.substring(0, 30);

			// TODO: Aggiungere data lavorazione nel dao
			dateLabel.setText(LocalDate.now().toString());
			spesaLabel.setText(item.getSpesa().toString());
			descrizioneLabel.setText(descrizione);
			setGraphic(view);
		}
	}

}
