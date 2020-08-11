package io.github.danifascio.gui;

import com.jfoenix.controls.JFXDecorator;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
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

		initStyle(StageStyle.UNDECORATED);
		DialogPane dialogPane = getDialogPane();
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LavorazioneDialog.fxml"));
			loader.setRoot(new AnchorPane());
			loader.setController(this);
			dialogPane.setContent(new JFXDecorator((Stage) dialogPane.getScene()
					.getWindow(), loader.load(), false, false, true));

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		dialogPane.getButtonTypes()
				.add(new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE));

		setResizable(true);
		setResultConverter(btnType -> {
			Lavorazione lavorazione1 = null;
			ButtonBar.ButtonData buttonData = btnType.getButtonData();

			if(buttonData.equals(ButtonBar.ButtonData.OK_DONE) || buttonData.equals(ButtonBar.ButtonData.APPLY))
				try {
					lavorazione1 = new Lavorazione.Builder().setAuto(auto)
							.setData(datePicker.getValue())
							.setDescrizione(descrizioneArea.getText())
							.setSpesa(Float.parseFloat(spesaField.getText()))
							.build();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}

			return lavorazione1;
		});

		if(!viewMode.equals(ViewMode.NEW)) {
			spesaField.setText(lavorazione.getSpesa().toString());
			// TODO: LocalDate from Lavorazione
			datePicker.setValue(LocalDate.now());
			descrizioneArea.setText(lavorazione.getDescrizione());

			if(viewMode.equals(ViewMode.EDIT)) {
				initModality(Modality.WINDOW_MODAL);
				getDialogPane().getButtonTypes()
						.addAll(new ButtonType("Modifica", ButtonBar.ButtonData.APPLY));

			} else {
				// BUTTON: VIEW
				spesaField.setDisable(true);
				datePicker.setDisable(true);
				descrizioneArea.setDisable(true);
				initModality(Modality.NONE);
			}

		} else {
			// BUTTON: NEW
			getDialogPane().getButtonTypes()
					.addAll(new ButtonType("Aggiungi", ButtonBar.ButtonData.OK_DONE));
			initModality(Modality.WINDOW_MODAL);
		}
	}

	public enum ViewMode {
		NEW,
		EDIT,
		VIEW
	}

}
