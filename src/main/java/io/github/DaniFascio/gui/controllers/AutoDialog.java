package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.TipoGomme;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.regex.Pattern;

public class AutoDialog extends Dialog<Auto> {

	private static final Pattern targaPattern = Pattern.compile("[a-zA-Z]{2}\\d{3}[a-zA-Z]{2}");
	private static final Pattern kmPattern = Pattern.compile("\\d+");
	private static final Pattern anyPattern = Pattern.compile(".+");

	@FXML
	private TextField targaField;
	@FXML
	private TextField modelloField;
	@FXML
	private TextField kmField;
	@FXML
	private TextField misuraGommeField;
	@FXML
	private ChoiceBox<TipoGomme> tipoGommeBox;
	@FXML
	private TextArea noteArea;

	private final boolean editable;
	private Auto auto;

	private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

	public AutoDialog(boolean editable) {
		this.editable = editable;
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AutoAddDialog.fxml"));
			loader.setController(this);
			loader.setRoot(getDialogPane());
			loader.load();
			getDialogPane().setMinSize(640, 480);

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

		tipoGommeBox.getItems().addAll(TipoGomme.values());
		tipoGommeBox.setValue(tipoGommeBox.getItems().get(0));
		tipoGommeBox.setConverter(new StringConverter<TipoGomme>() {
			@Override
			public String toString(TipoGomme tipoGomme) {
				return tipoGomme.getDescrizione();
			}

			@Override
			public TipoGomme fromString(String string) {
				return TipoGomme.get(string);
			}
		});

		setResizable(true);
		setResultConverter(btnType -> {
			Auto auto = null;

			try {
				auto = new Auto.Builder().setTarga(targaField.getText())
						.setModello(modelloField.getText())
						.setKm(Integer.parseInt(kmField.getText()))
						.setMisuraGomme(misuraGommeField.getText())
						.setNote(noteArea.getText())
						.setTipoGomme(tipoGommeBox.getValue())
						.build();
			} catch(Exception e) {
				e.printStackTrace();
			}

			return auto;
		});

		// DATA VALIDATORS
		targaField.textProperty()
				.addListener((observable, oldValue, newValue) -> targaField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !targaPattern
						.matcher(newValue)
						.matches()));
		kmField.textProperty()
				.addListener((observable, oldValue, newValue) -> kmField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !kmPattern
						.matcher(newValue)
						.matches()));
		modelloField.textProperty()
				.addListener((observable, oldValue, newValue) -> modelloField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !anyPattern
						.matcher(newValue)
						.matches()));
		misuraGommeField.textProperty()
				.addListener((observable, oldValue, newValue) -> misuraGommeField
						.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !anyPattern
								.matcher(newValue)
								.matches()));

	}

	public AutoDialog setAuto(Auto auto) {
		this.auto = auto;
		return this;
	}

}
