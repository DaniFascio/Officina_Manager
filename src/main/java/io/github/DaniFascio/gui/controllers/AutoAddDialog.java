package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.UncheckedIOException;
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

	private final static Pattern targaPattern = Pattern.compile("[a-zA-Z]{2}\\d{3}[a-zA-Z]{2}");

	public AutoAddDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AutoAddDialog.fxml"));
			loader.setController(this);
			loader.setRoot(getDialogPane());
			loader.load();

			setResizable(true);
			setResultConverter(btnType -> {

				Auto auto = null;

				return auto;

			});

			hookValidation(targaField);

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private boolean validate() {
		return false;
	}

	private void hookValidation(Control input) {

		if(input instanceof TextField) {
			TextField textField = (TextField) input;
			textField.textProperty()
					.addListener((observable, oldValue, newValue) -> textField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, newValue
							.length() == 0));

			System.out.println("Set hook on " + input.getId());
		}

	}

}
