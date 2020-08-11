package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Properties;

public class CustomLavorazioneDialog extends CustomDialog<Lavorazione> {

	private static final Properties icons;

	@FXML
	private TextField spesaField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextArea descrizioneArea;

	static {
		icons = new Properties();

		try(InputStream input = AutoDialog.class.getClassLoader().getResourceAsStream("glyphs.xml")) {

			if(input != null)
				icons.loadFromXML(input);
			else
				System.err.println("Couldn't load icons properties");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public CustomLavorazioneDialog(ViewMode viewMode, Auto auto, @Nullable Lavorazione lavorazione) {
		super(viewMode.equals(ViewMode.ADD) ? "Aggiungi lavorazione" : "Modifica lavorazione",
				icons.getProperty(viewMode.equals(ViewMode.ADD) ? "add" : "edit"));

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneDialog.fxml"));
			loader.setRoot(new AnchorPane());
			loader.setController(this);
			setContent(loader.load());

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		if(viewMode.equals(ViewMode.EDIT)) {
			if(!auto.equals(Objects.requireNonNull(lavorazione).getAuto()))
				throw new RuntimeException("Auto & Auto lavorazione mismatch");

			spesaField.setText(lavorazione.getSpesa().toString());
			descrizioneArea.setText(lavorazione.getDescrizione());
			datePicker.setValue(lavorazione.getData());

		} else
			datePicker.setValue(LocalDate.now());

		setResultConverter(() -> new Lavorazione.Builder().setData(datePicker.getValue())
				.setSpesa(Float.parseFloat(spesaField.getText()))
				.setDescrizione(descrizioneArea.getText())
				.build());

	}

	public enum ViewMode {
		ADD,
		EDIT
	}

}
