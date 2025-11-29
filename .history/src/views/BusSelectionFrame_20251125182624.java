package views;

import com.config.DBConfig;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class BusSelectionFrame extends JFrame {

    private JComboBox<String> busCombo;
    private JComboBox<String> jadwalCombo;

    private ArrayList<Integer> busIdList = new ArrayList<>();
    private ArrayList<Integer> scheduleIdList = new ArrayList<>();

    private int selectedRouteId;
    private String currentUser;

    public BusSelectionFrame(String username, int routeId) {
        this.currentUser = username;
        this.selectedRouteId = routeId;

        setTitle("Pilih Bus & Jadwal");
        setSize(420, 260);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel busLabel = new JLabel("Pilih Bus:");
        busLabel.setBounds(40, 40, 120, 25);
        add(busLabel);

        busCombo = new JComboBox<>();
        busCombo.setBounds(150, 40, 200, 25);
        add(busCombo);

        JLabel jadwalLabel = new JLabel("Pilih Jadwal:");
        jadwalLabel.setBounds(40, 90, 120, 25);
        add(jadwalLabel);

        jadwalCombo = new JComboBox<>();
        jadwalCombo.setBounds(150, 90, 200, 25);
        add(jadwalCombo);

        JButton lanjutBtn = new JButton("LANJUT");
        lanjutBtn.setBounds(150, 150, 120, 40);
        add(lanjutBtn);

        loadBuses();

        busCombo.addActionListener(e -> loadSchedules());

        lanjutBtn.addActionListener(e -> openSeatSelection());
    }

    private void loadBuses() {
        try (Connection conn = DBConfig.getConnection()) {
            String sql = "SELECT * FROM buses WHERE route_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selectedRouteId);

            ResultSet rs = ps.executeQuery();

            busCombo.removeAllItems();
            busIdList.clear();

            while (rs.next()) {
                busCombo.addItem(rs.getString("nama_bus"));
                busIdList.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSchedules() {
        try (Connection conn = DBConfig.getConnection()) {
            jadwalCombo.removeAllItems();
            scheduleIdList.clear();

            int idx = busCombo.getSelectedIndex();
            if (idx < 0) return;

            int busId = busIdList.get(idx);

            String sql = "SELECT * FROM schedules WHERE bus_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, busId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jadwalCombo.addItem(rs.getString("jam"));
                scheduleIdList.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSeatSelection() {
        int busIdx = busCombo.getSelectedIndex();
        int schIdx = jadwalCombo.getSelectedIndex();

        if (busIdx < 0 || schIdx < 0) {
            JOptionPane.showMessageDialog(this, "Pilih bus dan jadwal dahulu!");
            return;
        }

        int busId = busIdList.get(busIdx);
        int scheduleId = scheduleIdList.get(schIdx);

        new SeatSelectionFrame(currentUser, busId, scheduleId).setVisible(true);
        this.dispose();
    }
}
