import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {

        // Aktifkan GUI modern
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        AuthService auth = new AuthService();
        new LoginFrame(auth).setVisible(true);
    }
}
