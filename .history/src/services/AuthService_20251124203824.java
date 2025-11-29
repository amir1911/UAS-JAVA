package services;


import com.config.DBConfig;
import java.sql.*;


public class AuthService {


public boolean register(String username, String password) {
String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
try (Connection conn = DBConfig.getConnection();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, username);
ps.setString(2, password);
ps.executeUpdate();
return true;
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}


public boolean login(String username, String password) {
String sql = "SELECT * FROM users WHERE username=? AND password=?";
try (Connection conn = DBConfig.getConnection();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, username);
ps.setString(2, password);
ResultSet rs = ps.executeQuery();
return rs.next();
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}
}