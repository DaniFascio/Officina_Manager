package io.github.DaniFascio.gui;

import javax.swing.*;
import java.awt.*;

public class AutoPane extends JPanel {

	private String targa;
	private String modello;
	private String proprietario;

	private JLabel mainLabel;
	private JLabel proprietarioLabel;

	public AutoPane(String targa, String proprietario, String modello) {
		super(new BorderLayout());
		this.targa = targa;
		this.modello = modello;
		this.proprietario = proprietario;

		proprietarioLabel = new JLabel(proprietario, SwingConstants.LEFT);
		proprietarioLabel.setFont(new Font("Sans Serif", Font.ITALIC, 8));
		proprietarioLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		mainLabel = new JLabel(modello + ":" + targa, SwingConstants.LEFT);


		add(mainLabel, BorderLayout.CENTER);
		add(proprietarioLabel, BorderLayout.PAGE_END);
	}

}
