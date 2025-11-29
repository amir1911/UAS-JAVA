package views;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import com.config.DBConfig;

public class BusSelectionFrame extends JFrame {

    private JComboBox<String> busCombo;
    private JComboBox<String> scheduleCombo;
    private ArrayList<Integer> busIdList = new ArrayList<>();
    private ArrayList<Integer> scheduleIdList = new ArrayList<>();
    private String currentUser;
    private int routeId; // route selected (Palembang->Indralaya OR Indralaya->Palembang)

    public BusSelectionFrame(String username, int routeId) {
        this.currentUser = username;
        this.routeId = routeId;

        setTitle("Pilih Bus & Jadwal (Route ID: " + routeId + ")");
        setSize(480, 340);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel busLabel = new JLabel("Pilih Bus:");
        busLabel.setBounds(20, 30, 120, 25);
        add(busLabel);

        busCombo = new JComboBox<>();
        busCombo.setBounds(140, 30, 300, 25);
        add(busCombo);

        JLabel scheduleLabel = new JLabel("Pilih Jadwal:");
        scheduleLabel.setBounds(20, 90, 120, 25);
        add(scheduleLabel);

        scheduleCombo = new JComboBox<>();
        scheduleCombo.setBounds(140, 90, 300, 25);
        add(scheduleCombo);

        JButton lanjutBtn = new JButton("LANJUT");
        lanjutBtn.setBounds(180, 220, 120, 40);
        add(lanjutBtn);

        loadBusList();

        busCombo.addActionListener(e -> loadScheduleList());

        lanjutBtn.addActionListener(e -> openSeatSelection());
    }

    // load buses that serve the selected route (join bus_routes)
    private void loadBusList() {
        String sql = "SELECT b.id, b.nama_bus, b.harga " +
                     "FROM buses b " +
                     "JOIN bus_routes br ON b.id = br.bus_id " +
                     "WHERE br.route_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                busCombo.removeAllItems();
                busIdList.clear();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("nama_bus");
                    int price = rs.getInt("harga");
                    busCombo.addItem(name + " - Rp " + price);
                    busIdList.add(id);
                }
            }

            if (busCombo.getItemCount() > 0) {
                loadScheduleList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadScheduleList() {
        int idx = busCombo.getSelectedIndex();
        if (idx < 0) return;

        int busId = busIdList.get(idx);

        String sql = "SELECT id, jam FROM schedules WHERE bus_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                scheduleCombo.removeAllItems();
                scheduleIdList.clear();
                while (rs.next()) {
                    scheduleCombo.addItem(rs.getString("jam"));
                    scheduleIdList.add(rs.getInt("id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openSeatSelection() {
        if (busCombo.getSelectedIndex() < 0 || scheduleCombo.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Pilih Bus dan Jadwal terlebih dahulu!");
            return;
        }

        int selectedBusId = busIdList.get(busCombo.getSelectedIndex());
        int selectedScheduleId = scheduleIdList.get(scheduleCombo.getSelectedIndex());

        new SeatSelectionFrame(currentUser, selectedBusId, selectedScheduleId).setVisible(true);
        this.dispose();
    }
}
