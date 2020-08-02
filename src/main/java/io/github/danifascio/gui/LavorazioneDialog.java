package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

public class LavorazioneDialog extends Dialog<Lavorazione> {

	@FXML
	private TextField spesaField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextArea descrizioneArea;

	public LavorazioneDialog(@NotNull Auto auto, @Nullable Lavorazione lavorazione, ViewMode viewMode) {

		if(!viewMode.equals(ViewMode.NEW))
			if(!Objects.requireNonNull(lavorazione).getAuto().equals(auto))
				throw new RuntimeException("Auto and Lavorazione::auto mismatch");

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneDialog.fxml"));
			loader.setRoot(getDialogPane());
			loader.setController(this);
			loader.load();

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		setResizable(true);
		setResultConverter(btnType -> {

			Lavorazione lavorazione1 = null;
			if(btnType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
				try {
					lavorazione1 = new Lavorazione.Builder().setAuto(auto)
							.setData(datePicker.getValue())
							.setDescrizione(descrizioneArea.getText())
							.setSpesa(Float.parseFloat(spesaField.getText()))
							.build();
				} catch(Exception e) {
					e.printStackTrace();
				}

			return lavorazione1;
		});


		if(!viewMode.equals(ViewMode.NEW)) {

			spesaField.setText(lavorazione.getSpesa().toString());
			datePicker.setValue(lavorazione.getData());
			descrizioneArea.setText(lavorazione.getDescrizione());

			if(viewMode.equals(ViewMode.VIEW)) {
				spesaField.setDisable(true);
				datePicker.setDisable(true);
				descrizioneArea.setDisable(true);
			}

		}

	}

	public enum ViewMode {
		NEW,
		EDIT,
		VIEW,
		MISC
	}

}
