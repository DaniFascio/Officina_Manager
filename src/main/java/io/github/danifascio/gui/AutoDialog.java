package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.TipoGomme;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class AutoDialog extends Dialog<Auto> {

	private static final Pattern targaPattern = Pattern.compile("[a-zA-Z]{2} +\\d{3}[a-zA-Z]{2}");
	private static final Pattern kmPattern = Pattern.compile("\\d+");
	private static final Pattern anyPattern = Pattern.compile(".{3,}");
	private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

	@FXML
	private TextField targaField;
	private boolean targaError;
	@FXML
	private TextField modelloField;
	private boolean modelloError;
	@FXML
	private TextField kmField;
	private boolean kmError;
	@FXML
	private TextField misuraGommeField;
	private boolean misuraGommeError;
	@FXML
	private ChoiceBox<TipoGomme> tipoGommeBox;
	@FXML
	private TextArea noteArea;
	@FXML
	private Label mainLabel;
	@FXML
	private ButtonType doneButton;

	public AutoDialog(boolean editable, @Nullable Auto auto) {
		targaError = true;
		modelloError = true;
		kmError = true;
		misuraGommeError = true;
		Button doneButton;

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AutoDialog.fxml"));
			loader.setController(this);
			loader.setRoot(getDialogPane());
			loader.load();
			getDialogPane().setMinSize(640, 480);
			doneButton = (Button) getDialogPane().lookupButton(this.doneButton);

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
			Auto auto1 = null;

			if(btnType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
				try {
					auto1 = new Auto.Builder().setTarga(targaField.getText()
							.toUpperCase()
							.replaceAll(" ", ""))
							.setModello(modelloField.getText())
							.setKm(Integer.parseInt(kmField.getText()))
							.setMisuraGomme(misuraGommeField.getText())
							.setNote(noteArea.getText())
							.setTipoGomme(tipoGommeBox.getValue())
							.build();
				} catch(Exception e) {
					e.printStackTrace();
				}

			return auto1;
		});

		// FIELD INIT (FOR EDIT DIALOG)
		if(editable) {
			Objects.requireNonNull(auto);
			doneButton.setText("Modifica");
			mainLabel.setText("Modifica un'auto");

			targaField.setText(auto.getTarga());
			modelloField.setText(auto.getModello());
			kmField.setText(auto.getKm().toString());
			misuraGommeField.setText(auto.getMisuraGomme());
			noteArea.setText(auto.getNote());
			tipoGommeBox.setValue(TipoGomme.get(auto.getTipoGomme()
					.getDescrizione()));
		}

		// DATA VALIDATORS
		ChangeListener<String> listener = (observable, oldValue, newValue) -> validate(doneButton);
		validate(doneButton);

		targaField.textProperty().addListener(listener);
		kmField.textProperty().addListener(listener);
		modelloField.textProperty().addListener(listener);
		misuraGommeField.textProperty().addListener(listener);

	}

	private void validate(Node disableable) {
		targaField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, targaError = !targaPattern
				.matcher(targaField.getText())
				.matches());
		kmField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, kmError = !kmPattern
				.matcher(kmField.getText())
				.matches());
		modelloField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, modelloError = !anyPattern
				.matcher(modelloField.getText())
				.matches());
		misuraGommeField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, misuraGommeError = !anyPattern
				.matcher(misuraGommeField.getText())
				.matches());

		disableable.setDisable(targaError || kmError || modelloError || misuraGommeError);
	}

}
