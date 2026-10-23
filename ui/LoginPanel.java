package ui;

import javax.swing.*;
import java.awt.*;
import service.AuthenticationService;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthenticationService authService;

    public LoginPanel(JFrame parentFrame) {
        authService = new AuthenticationService();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (authService.authenticate(user, pass)) {
                JOptionPane.showMessageDialog(parentFrame, "Login Successful!");
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}