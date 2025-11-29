package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import models.User;
import services.AuthService;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;
    private User defaultUser;

    public LoginFrame() {
        authService = new AuthService();
        defaultUser = new User("admin", "12345");

        setTitle("Login Form");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText();
                String pass = new String(passwordField.getPassword());

                if (authService.login(defaultUser, user, pass)) {
                    JOptionPane.showMessageDialog(null, "Login Berhasil!");
                } else {
                    JOptionPane.showMessageDialog(null, "Login Gagal!");
                }
            }
        });

        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(new JLabel(""));
        add(loginBtn);
    }
}
