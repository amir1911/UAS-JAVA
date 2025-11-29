package services;
import com.config.DBConfig;
import java.sql.*;

public class AuthService {

    // REGISTER USER
    public boolean register(String username, String password) {

        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // sangat penting untuk melihat error
            return false;
        }
    }

    // LOGIN USER
    public boolean login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
