package io.github.danifascio.gui;

import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Optional;
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

	public LavorazioneCell() {
		super();
		lavorazioneReference = new AtomicReference<>();

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneCell.fxml"));
			loader.setController(this);
			view = loader.load();

			setOnMouseClicked(event -> Optional.of(event.getButton())
					.filter(MouseButton.PRIMARY::equals)
					.filter(mouseButton -> event.getClickCount() == 2)
					.flatMap(mouseButton -> Optional.ofNullable(lavorazioneReference.get()))
					.ifPresent(lavorazione -> new LavorazioneDialog(LavorazioneDialog.ViewMode.VIEW,
							lavorazione.getAuto(),
							lavorazione).show()));

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

			dateLabel.setText(LocalDate.now().toString());
			spesaLabel.setText(item.getSpesa().toString());
			descrizioneLabel.setText(descrizione);
			setGraphic(view);
		}
	}

}
