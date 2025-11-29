import services.AuthService;
import views.LoginFrame;

public class Main {
    public static void main(String[] args) {
        AuthService auth = new AuthService();
        new LoginFrame(auth).setVisible(true);
    }
}
