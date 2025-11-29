package views;


import java.awt.*;
import javax.swing.*;
import services.AuthService;


public class LoginFrame extends JFrame {


private JTextField usernameField;
private JPasswordField passwordField;


public LoginFrame(AuthService auth) {
setTitle("Login");
setSize(360, 230);
setLayout(new GridLayout(4,2,10,10));
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
new BusSelectionFrame(user).setVisible(true);

this.dispose();
} else {
JOptionPane.showMessageDialog(this, "Login Gagal!");
}
});


registerBtn.addActionListener(e -> {
new RegisterFrame(auth, this).setVisible(true);
this.setVisible(false);
});


add(new JLabel("Username:")); add(usernameField);
add(new JLabel("Password:")); add(passwordField);
add(loginBtn); add(registerBtn);
}
}