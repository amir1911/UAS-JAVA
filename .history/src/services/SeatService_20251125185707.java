package services;

import com.config.DBConfig;
import java.sql.*;
import java.util.*;

public class SeatService {

    // get seats by bus id
    public List<Map<String,String>> getSeatsByBus(int busId) {
        List<Map<String,String>> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE bus_id = ? ORDER BY id";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,String> m = new HashMap<>();
                    m.put("id", String.valueOf(rs.getInt("id")));
                    m.put("seat_no", rs.getString("seat_no"));
                    m.put("status", rs.getString("status"));
                    seats.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // book a seat (only if available)
    public boolean bookSeat(int busId, String seatNo) {
        String sql = "UPDATE seats SET status='BOOKED' WHERE bus_id=? AND seat_no=? AND status='AVAILABLE'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, busId);
            ps.setString(2, seatNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // save ticket record
    public boolean saveTicket(String userName, int busId, int scheduleId, String seatNo, int harga) {
        String sql = "INSERT INTO tickets(user_name, bus_id, schedule_id, seat_no, harga) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setInt(2, busId);
            ps.setInt(3, scheduleId);
            ps.setString(4, seatNo);
            ps.setInt(5, harga);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // helper: get price per bus
    public int getBusPrice(int busId) {
        String sql = "SELECT harga FROM buses WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("harga");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // helper: get bus id by its name (returns -1 if not found)
    public int getBusIdByName(String busName) {
        String sql = "SELECT id FROM buses WHERE nama_bus = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, busName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // helper: get schedule id by busId and jam (returns -1 if not found)
    public int getScheduleId(int busId, String jam) {
        String sql = "SELECT id FROM schedules WHERE bus_id = ? AND jam = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, busId);
            ps.setString(2, jam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
