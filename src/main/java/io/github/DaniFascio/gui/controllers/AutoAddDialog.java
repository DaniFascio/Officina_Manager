package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import io.github.DaniFascio.TipoGomme;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;

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
	private ComboBox<String> tipoGommeBox;
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

			ObservableList<String> list = tipoGommeBox.getItems();
			for(Map.Entry<String, TipoGomme> stringTipoGommeEntry : TipoGomme.entrySet())
				list.add(stringTipoGommeEntry.getKey());

			setResizable(true);
			setResultConverter(btnType -> {

				Auto auto = null;

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
