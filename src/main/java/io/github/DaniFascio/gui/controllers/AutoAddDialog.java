package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.xml.soap.Text;
import java.io.IOException;
import java.io.UncheckedIOException;

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

			setResizable(true);
			setResultConverter(btnType -> {

				return null;

			});

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private void hookValidation(Control input) {

		if(input instanceof TextField) {
			return;
		}

	}

}
