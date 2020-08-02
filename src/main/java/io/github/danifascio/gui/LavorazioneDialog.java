package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class LavorazioneDialog extends Dialog<Lavorazione> {

	private static final Pattern datePattern = Pattern.compile("^(?:\\d{1,2}/){2}\\d{4}");

	@FXML
	private TextField costoField;
	@FXML
	private TextArea descrizioneArea;
	@FXML
	private TextField dataField;

	private boolean dataError;

	public LavorazioneDialog(@NotNull Auto auto, @Nullable Lavorazione lavorazione, State state) {
		dataError = true;

		if(!state.equals(State.NEW))
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
							.setDescrizione(descrizioneArea.getText())
							.setSpesa(Float.parseFloat(costoField.getText()))
							.build();
				} catch(Exception e) {
					e.printStackTrace();
				}

			return lavorazione1;
		});

	}

	public enum State {
		NEW,
		EDIT,
		VIEW,
		MISC
	}

}
