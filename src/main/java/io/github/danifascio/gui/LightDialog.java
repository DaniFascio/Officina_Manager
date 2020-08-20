package io.github.danifascio.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class LightDialog {

	private static final Map<StackPane, LightDialog> dialogMap = Collections.synchronizedMap(new HashMap<>());

	private final List<Button> actions;
	private final JFXDialogLayout layout;
	private final JFXDialog dialog;
	private final StackPane root;


	public LightDialog(StackPane rootPane, String heading, String content, boolean unfocusable) {
		root = rootPane;
		actions = new LinkedList<>();
		layout = new JFXDialogLayout();
		dialog = new JFXDialog(root, layout, DialogTransition.CENTER);

		dialog.setOverlayClose(unfocusable);
		dialog.getStyleClass().add("/css/Root.css");
		dialog.setOnDialogClosed(event -> dialogMap.remove(root));

		Label headingLabel = new Label(heading), contentLabel = new Label(content);
		headingLabel.setFont(Font.font(18));
		contentLabel.setFont(Font.font(13));
	}

	public LightDialog(StackPane rootPane, String header, String content) {
		this(rootPane, header, content, true);
	}

	public LightDialog(StackPane rootPane, boolean unfocusable) {
		this(rootPane, null, null, unfocusable);
	}

	public LightDialog addButton(String text, EventHandler<ActionEvent> onAction) {
		JFXButton button = new JFXButton(text);

		button.getStyleClass().add("button-raised");
		button.setOnAction(onAction);
		actions.add(button);

		return this;
	}

	public LightDialog addButton(String text, Function<Button, EventHandler<ActionEvent>> onAction) {
		JFXButton button = new JFXButton(text);

		button.getStyleClass().add("button-raised");
		button.setOnAction(onAction.apply(button));
		actions.add(button);

		return this;
	}

	public LightDialog heading(String heading) {
		Label label = new Label(heading);
		label.setFont(Font.font(18));
		layout.setHeading(label);

		return this;
	}

	public LightDialog content(String content) {
		Label label = new Label(content);
		label.setFont(Font.font(18));
		layout.setBody(label);

		return this;
	}


//	TODO FIX
	public LightDialog onClose(EventHandler<? super JFXDialogEvent> handler) {

		EventHandler<? super JFXDialogEvent> eventHandler = event -> {
			dialogMap.remove(root);
			try {

				Method method = handler.getClass().getMethod("handle", Event.class);
				method.setAccessible(true);
				method.invoke(handler, event);

			} catch(Exception e) {
				e.printStackTrace();
			}
		};

		dialog.setOnDialogClosed(eventHandler);
		return this;
	}

	public void show() {
		layout.setActions(actions);
		if(!dialogMap.containsKey(root)) {
			dialogMap.put(root, this);
			dialog.show();
		}
	}

	public void close() {
		dialog.close();
	}

}
