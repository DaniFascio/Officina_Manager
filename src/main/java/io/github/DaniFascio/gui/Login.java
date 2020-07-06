package io.github.DaniFascio.gui;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

	private JTextField userField;
	private JPanel rootPanel;
	private JLabel titleLabel;
	private JButton loginButton;
	private JLabel passLabel;
	private JLabel userLabel;
	private JPasswordField passField;
	private JPanel formPanel;

	public Login() {

		titleLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 48));

//		userField.setBorder(BorderFactory.createCompoundBorder(userField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 5, 3)));
//		passField.setBorder(BorderFactory.createCompoundBorder(passField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 5, 3)));


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
