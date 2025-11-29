package views;

import javax.swing.*;
import java.awt.*;
import services.AuthService;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(AuthService auth) {
        setTitle("Login");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (auth.login(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
            } else {
                JOptionPane.showMessageDialog(this, "Username / Password salah!");
            }
        });

        registerBtn.addActionListener(e -> {
            new RegisterFrame(auth, this).setVisible(true);
            this.setVisible(false);
        });

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginBtn);
        add(registerBtn);
    }
}
