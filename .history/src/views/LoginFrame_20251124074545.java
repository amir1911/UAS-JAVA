package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import models.*;
import services.AuthService;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Komposisi: LoginFrame memiliki AuthService
    private AuthService authService;

    // Komposisi: objek dibuat langsung di sini
    private AbstractUser[] users;

    public LoginFrame() {
        setTitle("Login OOP Lengkap");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        authService = new AuthService();

        // Agregasi: LoginFrame memiliki list User (Admin + User Biasa)
        users = new AbstractUser[] {
            new Admin("admin", "12345"),
            new User("amir", "123")
        };

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userInput = usernameField.getText();
                String passInput = new String(passwordField.getPassword());

                boolean success = false;

                for (AbstractUser u : users) {
                    if (authService.login(u, userInput, passInput)) {
                        JOptionPane.showMessageDialog(null,
                            "Login Berhasil!\nRole: " + u.getRole());
                        success = true;
                        break;
                    }
                }

                if (!success) {
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
