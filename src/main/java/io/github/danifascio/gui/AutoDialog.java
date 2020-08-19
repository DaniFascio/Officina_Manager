package io.github.danifascio.gui;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.TipoGomme;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

public class AutoDialog extends CustomDialog<Auto> {

	private static final Pattern targaPattern = Pattern.compile("[a-zA-Z]{2} *\\d{3}[a-zA-Z]{2}");
	private static final Pattern kmPattern = Pattern.compile("\\d+");
	private static final Pattern anyPattern = Pattern.compile(".{3,}");
	// TODO: MOVE ERROR_PSEUDO_CLASS TO Gui CLASS
	private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
	private static final Properties icons;

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

	public AutoDialog(ViewMode viewMode, @Nullable Auto auto) {
		super(viewMode.equals(ViewMode.ADD) ? "Aggiungi auto" : "Modifica auto",
				icons.getProperty(viewMode.equals(ViewMode.ADD) ? "action.add" : "action.edit"),
				Modality.APPLICATION_MODAL);

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AutoDialog.fxml"));
			loader.setController(this);
			setContent(loader.load());

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

		if(viewMode.equals(ViewMode.EDIT)) {
			Objects.requireNonNull(auto);

			targaField.setText(auto.getTarga());
			modelloField.setText(auto.getModello());
			kmField.setText(auto.getKm().toString());
			misuraGommeField.setText(auto.getMisuraGomme());
			noteArea.setText(auto.getNote());
			tipoGommeBox.setValue(TipoGomme.get(auto.getTipoGomme().getDescrizione()));
		}

		setResultConverter(() -> new Auto.Builder().setTarga(targaField.getText().toUpperCase().replaceAll(" ", ""))
				.setModello(modelloField.getText())
				.setKm(Integer.parseInt(kmField.getText()))
				.setMisuraGomme(misuraGommeField.getText())
				.setNote(noteArea.getText())
				.setTipoGomme(tipoGommeBox.getValue())
				.build());

		addButton("Cancella", event -> setResult(null));
		Button doneButton = addButton(viewMode.equals(ViewMode.ADD) ? "Aggiungi" : "Modifica", event -> done());

		ChangeListener<String> listener = (observable, oldValue, newValue) -> validate(doneButton);
		validate(doneButton);

		targaField.textProperty().addListener(listener);
		kmField.textProperty().addListener(listener);
		modelloField.textProperty().addListener(listener);
		misuraGommeField.textProperty().addListener(listener);
	}

	private void validate(Node disableable) {
		boolean targaError;
		boolean kmError;
		boolean modelloError;
		boolean misuraGommeError;

		targaField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, targaError = !targaPattern.matcher(targaField.getText()).matches());
		kmField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, kmError = !kmPattern.matcher(kmField.getText()).matches());
		modelloField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, modelloError = !anyPattern.matcher(modelloField.getText()).matches());
		misuraGommeField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS,
				misuraGommeError = !anyPattern.matcher(misuraGommeField.getText()).matches());

		disableable.setDisable(targaError || kmError || modelloError || misuraGommeError);
	}

	public enum ViewMode {
		ADD,
		EDIT
	}

}
