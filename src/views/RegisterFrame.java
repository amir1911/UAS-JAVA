package views;


import java.awt.*;
import javax.swing.*;
import services.AuthService;


public class RegisterFrame extends JFrame {


private JTextField usernameField;
private JPasswordField passwordField;


public RegisterFrame(AuthService auth, JFrame loginFrame) {
setTitle("Register");
setSize(360, 230);
setLayout(new GridLayout(4,2,10,10));
setLocationRelativeTo(null);


usernameField = new JTextField();
passwordField = new JPasswordField();


JButton registerBtn = new JButton("Daftar");


registerBtn.addActionListener(e -> {
String user = usernameField.getText();
String pass = new String(passwordField.getPassword());
if (auth.register(user, pass)) {
JOptionPane.showMessageDialog(this, "Registrasi Berhasil!");
loginFrame.setVisible(true);
this.dispose();
} else {
JOptionPane.showMessageDialog(this, "Registrasi Gagal!");
}
});


add(new JLabel("Username:")); add(usernameField);
add(new JLabel("Password:")); add(passwordField);
add(new JLabel()); add(registerBtn);
}
}