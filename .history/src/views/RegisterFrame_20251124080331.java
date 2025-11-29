package views;

import javax.swing.*;
import java.awt.*;
import services.AuthService;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame(AuthService auth, JFrame loginFrame) {
        setTitle("Register User Baru");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton registerBtn = new JButton("Register");

        registerBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong!");
                return;
            }

            auth.register(user, pass);
            JOptionPane.showMessageDialog(this, "Berhasil daftar!");

            loginFrame.setVisible(true);
            this.dispose();
        });

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel(""));
        add(registerBtn);
    }
}
