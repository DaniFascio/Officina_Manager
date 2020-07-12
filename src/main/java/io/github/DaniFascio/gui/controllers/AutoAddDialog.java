package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.TipoGomme;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.regex.Pattern;

public class AutoAddDialog extends Dialog<Auto> {

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

	private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

	public AutoAddDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AutoAddDialog.fxml"));
			loader.setController(this);
			loader.setRoot(getDialogPane());
			loader.load();
			getDialogPane().setMinSize(640, 480);

			tipoGommeBox.getItems().addAll(TipoGomme.values());
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
					auto = new Auto.AutoBuilder().setTarga(targaField.getText())
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

			targaField.textProperty()
					.addListener((observable, oldValue, newValue) -> targaField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !Pattern
							.matches("[a-zA-Z]{2}\\d{3}[a-zA-Z]{2}", newValue)));

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private boolean validate() {
		return false;
	}

}
