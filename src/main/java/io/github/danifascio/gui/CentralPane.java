package io.github.danifascio.gui;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import io.github.danifascio.DatabaseManager;
import io.github.danifascio.Gui;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import io.github.danifascio.dao.AutoDao;
import io.github.danifascio.dao.LavorazioneDao;
import io.github.danifascio.gui.cells.AutoCell;
import io.github.danifascio.gui.cells.LavorazioneCell;
import io.github.danifascio.gui.dialogs.AutoDialog;
import io.github.danifascio.gui.dialogs.LavorazioneDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CentralPane extends BorderPane implements Initializable {

	@FXML
	private BorderPane root;
	@FXML
	private ListView<Auto> listaAutoView;
	@FXML
	private ListView<Lavorazione> listaLavorazioniView;
	@FXML
	private Label welcomeLabel;
	@FXML
	private TextField targaLabel;
	@FXML
	private TextField modelloLabel;
	@FXML
	private TextField kmLabel;
	@FXML
	private TextField misuraGommeLabel;
	@FXML
	private TextField tipoGommeLabel;
	@FXML
	private TextArea noteArea;
	@FXML
	private HBox searchBox;

	@FXML
	private Button searchAutoButton;
	@FXML
	private TextField searchAutoField;
	@FXML
	private Button addAutoButton;
	@FXML
	private Button editAutoButton;
	@FXML
	private Button deleteAutoButton;
	@FXML
	private Button refreshAutoButton;
	@FXML
	private Button addLavorazioneButton;
	@FXML
	private Button editLavorazioneButton;
	@FXML
	private Button deleteLavorazioneButton;
	@FXML
	private Button refreshLavorazioneButton;

	private final ResourceBundle lang;
	private final List<Auto> autoList;
	private StackPane rootPane;
	private Auto selectedAuto;

	public CentralPane() {
		super();
		autoList = Collections.synchronizedList(new LinkedList<>());

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/Central.fxml"));
			loader.setResources(lang = Gui.lang());
			loader.setController(this);
			loader.setRoot(this);
			loader.load();

			welcomeLabel.setText(DatabaseManager.getUsername());

			Tooltip.install(searchBox, new Tooltip(lang.getString("auto.search.criteria")));

			listaAutoView.setCellFactory(listView -> new AutoCell());
			listaAutoView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(listaAutoView.getItems().size() == 0)
					return;

				selectedAuto = newValue;

				targaLabel.setText(newValue.getTarga());
				modelloLabel.setText(newValue.getModello());
				kmLabel.setText(newValue.getKm().toString());
				misuraGommeLabel.setText(newValue.getMisuraGomme());
				tipoGommeLabel.setText(newValue.getTipoGomme().getDescrizione());
				noteArea.setText(newValue.getNote());

				onRefreshLavorazione(null);
			});

			listaLavorazioniView.setCellFactory(listView -> new LavorazioneCell());

			onRefreshAuto(null);
			Platform.runLater(() -> rootPane = (StackPane) getScene().lookup("#rootPane"));
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@FXML
	private void onAddAuto(ActionEvent event) {

		new AutoDialog(AutoDialog.ViewMode.ADD, null).onResult(auto -> {
			if(auto != null) {
				AutoDao autoDao = new AutoDao();

				if(autoDao.save(auto) == 1) {

					// TODO: REFACTOR ALERT (INFO) INTO TOAST (SNACKBAR)
					new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("auto.add.success")),
							Duration.seconds(Gui.TOAST_DURATION)));

				} else {

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(lang.getString("auto.add2"));
					alert.setHeaderText(lang.getString("auto.add.error"));
					alert.setContentText(autoDao.errorMessage());
					alert.showAndWait();

				}

				onRefreshAuto(event);
			}
		}).showAndWait();

	}

	@FXML
	private void onEditAuto(ActionEvent event) {
		Auto selectedAuto = listaAutoView.getSelectionModel().getSelectedItem();

		new AutoDialog(AutoDialog.ViewMode.EDIT, selectedAuto).onResult(auto -> {

			if(auto != null) {
				AutoDao dao = new AutoDao();

				if(dao.update(selectedAuto, auto.values()) == 0) {

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(lang.getString("auto.edit"));
					alert.setHeaderText(lang.getString("auto.edit.error"));
					alert.setContentText(dao.errorMessage());
					alert.showAndWait();

				} else
					new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("auto.edit.success")),
							Duration.seconds(Gui.TOAST_DURATION)));

				onRefreshAuto(event);
			}
		}).showAndWait();

	}

	@FXML
	private void onDeleteAuto(ActionEvent event) {

		Auto auto = listaAutoView.getSelectionModel().getSelectedItem();
		String targa = auto.getTarga();

		new Alert(Alert.AlertType.CONFIRMATION,
				String.format(lang.getString("auto.delete.confirm"), targa),
				ButtonType.YES,
				ButtonType.NO).showAndWait().filter(ButtonType.YES::equals).ifPresent(buttonType -> {

			AutoDao dao = new AutoDao();

			if(dao.delete(auto) == 1) {

				new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("auto.delete.success")),
						Duration.seconds(Gui.TOAST_DURATION)));

			} else {

				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(lang.getString("auto.delete"));
				alert.setHeaderText(lang.getString("auto.delete.error"));
				alert.setContentText(dao.errorMessage());
				alert.showAndWait();

			}

			onRefreshAuto(event);
		});
	}

	@FXML
	private void onRefreshAuto(ActionEvent event) {
		AutoDao dao = new AutoDao();
		List<Auto> list = dao.getAll();

		autoList.clear();
		autoList.addAll(list);
		listaAutoView.getItems().clear();
		listaAutoView.getItems().addAll(list);

		if(dao.error()) {

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(lang.getString("action.error"));
			alert.setHeaderText(lang.getString("auto.refresh.error"));
			alert.setContentText(dao.errorMessage());
			alert.showAndWait();

		}

		if(listaAutoView.getSelectionModel().getSelectedItem() == null && listaAutoView.getItems().size() != 0)
			Platform.runLater(() -> listaAutoView.getSelectionModel().select(0));
	}

	@FXML
	private void onSearchAuto(Event event) {

		Platform.runLater(() -> {

			String keyword = searchAutoField.getText().toUpperCase();
			listaAutoView.getItems().clear();

			if(keyword.length() != 0)
				autoList.stream()
						.filter(auto -> auto.getTarga().toUpperCase().contains(keyword) || auto.getModello()
								.toUpperCase()
								.contains(keyword))
						.forEach(auto -> listaAutoView.getItems().add(auto));
			else
				listaAutoView.getItems().addAll(autoList);

		});

	}

	@FXML
	private void onRefreshLavorazione(ActionEvent event) {
		LavorazioneDao dao = new LavorazioneDao(selectedAuto);

		listaLavorazioniView.getItems().clear();
		listaLavorazioniView.getItems().addAll(dao.getAll());

		if(dao.error()) {

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(lang.getString("action.error"));
			alert.setHeaderText(lang.getString("lavorazione.refresh.error"));
			alert.setContentText(dao.errorMessage());
			alert.showAndWait();

		}
	}

	@FXML
	private void onAddLavorazione(ActionEvent event) {

		new LavorazioneDialog(LavorazioneDialog.ViewMode.ADD, selectedAuto, null).onResult(lavorazione -> {
			if(lavorazione != null) {

				LavorazioneDao dao = new LavorazioneDao(lavorazione.getAuto());
				dao.save(lavorazione);

				if(dao.error()) {

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(lang.getString("action.error"));
					alert.setHeaderText(lang.getString("lavorazione.add.error"));
					alert.setContentText(dao.errorMessage());
					alert.showAndWait();

				} else
					new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("lavorazione.add.success")),
							Duration.seconds(Gui.TOAST_DURATION)));

			}
		}).showAndWait();

		onRefreshLavorazione(event);
	}

	@FXML
	private void onEditLavorazione(ActionEvent event) {

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel().getSelectedItem();

		if(selectedLavorazione == null)
			new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("lavorazione.select_first"))));

		else
			new LavorazioneDialog(LavorazioneDialog.ViewMode.EDIT, selectedAuto, selectedLavorazione).onResult(lavorazione -> {

				if(lavorazione != null) {
					LavorazioneDao dao = new LavorazioneDao(lavorazione.getAuto());

					if(dao.update(selectedLavorazione, lavorazione.values()) == 0) {

						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle(lang.getString("action.error"));
						alert.setHeaderText(lang.getString("lavorazione.edit.error"));
						alert.setContentText(dao.errorMessage());
						alert.showAndWait();

					} else
						new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("lavorazione.edit.success")),
								Duration.seconds(Gui.TOAST_DURATION)));
				}

			}).showAndWait();
	}

	@FXML
	private void onDeleteLavorazione(ActionEvent event) {

		Lavorazione selectedLavorazione = listaLavorazioniView.getSelectionModel().getSelectedItem();

		if(selectedLavorazione == null)
			new Alert(Alert.AlertType.INFORMATION, lang.getString("lavorazione.select_first")).showAndWait();

		else
			new Alert(Alert.AlertType.CONFIRMATION,
					lang.getString("lavorazione.delete.confirm"),
					ButtonType.YES,
					ButtonType.NO).showAndWait().filter(ButtonType.YES::equals).ifPresent(buttonType -> {

				LavorazioneDao dao = new LavorazioneDao(selectedAuto);

				if(dao.delete(selectedLavorazione) == 0) {

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(lang.getString("lavorazione.delete"));
					alert.setHeaderText(lang.getString("lavorazione.delete.error"));
					alert.setContentText(dao.errorMessage());
					alert.showAndWait();

				} else
					new JFXSnackbar(rootPane).enqueue(new SnackbarEvent(new JFXSnackbarLayout(lang.getString("lavorazione.delete.success")),
							Duration.seconds(Gui.TOAST_DURATION)));

				onRefreshAuto(event);
			});
	}

	@FXML
	private void quit(Event event) {
		Stage stage = Gui.stage();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

	}

	@FXML
	private void backToLogin(Event event) {
		Gui.changeStage(lang.getString("menu.secondary"), new LoginPane(), false);
	}

	@FXML
	private void reportBug(Event event) {
		Desktop desktop = Desktop.getDesktop();
		String message = lang.getString("action.reportBug.Mail");
		URI uri = URI.create(message);

		try {

			desktop.mail(uri);

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		searchAutoButton.setGraphic(Gui.icon("search", Color.DIMGRAY, 18));

		addAutoButton.setGraphic(Gui.icon("add", Color.DIMGRAY, 18));
		editAutoButton.setGraphic(Gui.icon("edit", Color.DIMGRAY, 18));
		deleteAutoButton.setGraphic(Gui.icon("delete", Color.DIMGRAY, 18));
		refreshAutoButton.setGraphic(Gui.icon("refresh", Color.DIMGRAY, 18));

		addLavorazioneButton.setGraphic(Gui.icon("add", Color.DIMGRAY, 18));
		editLavorazioneButton.setGraphic(Gui.icon("edit", Color.DIMGRAY, 18));
		deleteLavorazioneButton.setGraphic(Gui.icon("delete", Color.DIMGRAY, 18));
		refreshLavorazioneButton.setGraphic(Gui.icon("refresh", Color.DIMGRAY, 18));
	}

}
