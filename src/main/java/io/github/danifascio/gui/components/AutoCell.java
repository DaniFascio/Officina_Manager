package io.github.danifascio.gui.components;

import io.github.danifascio.beans.Auto;
import io.github.danifascio.gui.controllers.AutoCellController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class AutoCell extends ListCell<Auto> {

	private final AutoCellController autoCellController;
	private final Node view;

	public AutoCell() {
		super();
		autoCellController = new AutoCellController();
		view = autoCellController.getView();
	}

	@Override
	protected void updateItem(Auto item, boolean empty) {
		super.updateItem(item, empty);
		if(empty) {
			setGraphic(null);
		} else {
			autoCellController.setAuto(item);
			setGraphic(view);
		}
	}

}
