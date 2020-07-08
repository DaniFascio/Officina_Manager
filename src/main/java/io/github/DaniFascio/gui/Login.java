package io.github.DaniFascio.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Login extends JFrame {

	JFrame master;

	private JTextField userField;
	private JPanel rootPanel;
	private JLabel titleLabel;
	private JButton loginButton;
	private JLabel passLabel;
	private JLabel userLabel;
	private JPasswordField passField;
	private JPanel formPanel;
	private JSeparator rightSeparator;
	private JSeparator leftSeparator;

	public Login() {

		master = this;

		titleLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 48));

//		userField.setBorder(BorderFactory.createCompoundBorder(userField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 5, 3)));
//		passField.setBorder(BorderFactory.createCompoundBorder(passField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 5, 3)));

		TitledBorder titledBorder = BorderFactory.createTitledBorder("Login");
		titledBorder.setTitleFont(new Font("Lucida Calligraphy", Font.BOLD, 12));
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, BorderFactory
				.createEmptyBorder(6, 6, 6, 6));
		formPanel.setBorder(compoundBorder);

		leftSeparator.setBounds(leftSeparator.getX(), leftSeparator.getY(), leftSeparator
				.getWidth(), 10);

		ActionListener closeListener = actionEvent -> master.dispatchEvent(new WindowEvent(master, WindowEvent.WINDOW_CLOSING));
		loginButton.addActionListener(closeListener);
		userField.addActionListener(closeListener);
		passField.addActionListener(closeListener);

		add(rootPanel);
		setSize(1280, 720);
		validate();

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Login login = new Login();
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setVisible(true);

	}

}
