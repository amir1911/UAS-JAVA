package services;


import java.sql.*;
import java.util.*;
import com.config.DBConfig;


public class SeatService {


public List<Map<String, String>> getAllSeats() {
List<Map<String,String>> seats = new ArrayList<>();
String sql = "SELECT * FROM seats ORDER BY id";
try (Connection conn = DBConfig.getConnection();
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery(sql)) {
while (rs.next()) {
Map<String,String> m = new HashMap<>();
m.put("id", String.valueOf(rs.getInt("id")));
m.put("seat_no", rs.getString("seat_no"));
m.put("status", rs.getString("status"));
seats.add(m);
}
} catch (SQLException e) {
e.printStackTrace();
}
return seats;
}


public boolean bookSeat(String seatNo) {
String sql = "UPDATE seats SET status='BOOKED' WHERE seat_no=? AND status='AVAILABLE'";
try (Connection conn = DBConfig.getConnection();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, seatNo);
return ps.executeUpdate() > 0;
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}


public boolean saveTicket(String nama, String seatNo, int harga) {
String sql = "INSERT INTO tickets(nama_penumpang, seat_no, harga) VALUES (?, ?, ?)";
try (Connection conn = DBConfig.getConnection();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, nama);
ps.setString(2, seatNo);
ps.setInt(3, harga);
ps.executeUpdate();
return true;
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}
}