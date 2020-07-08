package io.github.DaniFascio.gui;

import javafx.scene.layout.StackPane;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Manager extends JFrame {

	private static final String[] fieldTitles = {
			"Modello", "Tipo gomme", "Lavorazioni totali"
	};
	private static final String[] fieldValues = {
			"Boh", "Qualcosa", "Un po'"
	};

	private JPanel rootPanel;
	private JPanel centerPane;
	private JPanel gridBagPanel;
	private JPanel leftPane;
	private JPanel leftPaneControls;

	public Manager() {

//		rightPane.setBorder(BorderFactory.createEmptyBorder());
//		leftPane.setBorder(BorderFactory.createEmptyBorder());

		add(rootPanel);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Manager Officina");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		gridBagPanel.setAlignmentX(GridBagConstraints.LINE_START);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		for(String title : fieldTitles) {
			gridBagPanel.add(new JLabel(title + ":"), c);
			c.gridy++;
		}

		c.gridx = 1;
		c.gridy = 0;
		gridBagPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		c.gridx = 2;
		c.gridy = 0;
		for(String value : fieldValues) {
			gridBagPanel.add(new JLabel(value), c);
			c.gridy++;
		}

	}

	public static void main(String[] args) {

		JFrame manager = new Manager();
		manager.setVisible(true);
		manager.setSize(1280, 720);
		manager.setMinimumSize(new Dimension(640, 360));

	}

}


