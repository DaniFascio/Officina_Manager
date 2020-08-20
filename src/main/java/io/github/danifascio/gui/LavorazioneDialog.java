package io.github.danifascio.gui;

import io.github.danifascio.Gui;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class LavorazioneDialog extends CustomDialog<Lavorazione> {

	private static final Properties icons;

	@FXML
	private TextField spesaField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextArea descrizioneArea;

	private static final Logger logger = LoggerFactory.getLogger(LoginPane.class);

	private static ResourceBundle lang;



	static {
		icons = new Properties();

		try(InputStream input = AutoDialog.class.getClassLoader().getResourceAsStream("glyphs.xml")) {

			if(input != null)
				icons.loadFromXML(input);
			else
				logger.error("Couldn't load icons properties");

		} catch(IOException e) {
			logger.error("Error during loading icons", e);
		}
	}

	public LavorazioneDialog(ViewMode viewMode, Auto auto, @Nullable Lavorazione lavorazione) {
		super((viewMode.equals(ViewMode.ADD) ? "Aggiungi" : viewMode.equals(ViewMode.EDIT) ? "Modifica" : "Visualizza") + " lavorazione",
				icons.getProperty(viewMode.equals(ViewMode.ADD) ? "action.add" : viewMode.equals(ViewMode.EDIT) ? "action.edit" : "view"),
				viewMode.equals(ViewMode.ADD) || viewMode.equals(ViewMode.EDIT) ? Modality.APPLICATION_MODAL : Modality.NONE);

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneDialog.fxml"));
			loader.setResources(lang = Gui.lang());
			loader.setRoot(new AnchorPane());
			loader.setController(this);
			setContent(loader.load());

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		if(!viewMode.equals(ViewMode.ADD)) {
			if(!auto.equals(Objects.requireNonNull(lavorazione).getAuto()))
				throw new RuntimeException("Auto & Auto lavorazione mismatch");

			spesaField.setText(lavorazione.getSpesa().toString());
			descrizioneArea.setText(lavorazione.getDescrizione());
			datePicker.setValue(lavorazione.getData());

		} else
			datePicker.setValue(LocalDate.now());

		if(!viewMode.equals(ViewMode.VIEW)) {
			setResultConverter(() -> new Lavorazione.Builder().setData(datePicker.getValue())
					.setSpesa(Float.parseFloat(spesaField.getText()))
					.setDescrizione(descrizioneArea.getText())
					.setAuto(auto)
					.build());

			addButton(lang.getString("action.cancel"), event -> setResult(null));
			Button doneButton = addButton(viewMode.equals(ViewMode.ADD) ? "Aggiungi" : "Modifica", event -> done());

			ChangeListener<String> listener = (observable, oldValue, newValue) -> validate(doneButton);
			validate(doneButton);
			spesaField.textProperty().addListener(listener);

		} else {
			// TODO: UNEDITABLE FIELDS FOR VIEW VIEWMODE
			addButton("Chiudi", event -> setResult(null));
		}
	}

	private void validate(Node disableable) {
		boolean spesaError;

		try {
			spesaError = Float.parseFloat(spesaField.getText()) < 0;
		} catch(NumberFormatException e) {
			spesaError = true;
		}

		spesaField.pseudoClassStateChanged(Gui.ERROR_PSEUDO_CLASS, spesaError);

		disableable.setDisable(spesaError);
	}

	public enum ViewMode {
		ADD,
		EDIT,
		VIEW
	}

}
