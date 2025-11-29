package views;

import javax.swing.*;
import java.sql.*;
import com.config.DBConfig;

public class BusSelectionFrame extends JFrame {

    private JComboBox<String> busCombo;
    private JComboBox<String> jadwalCombo;

    private String currentUser;
    private int routeId;

    public BusSelectionFrame(String username, int routeId) {
        this.currentUser = username;
        this.routeId = routeId;

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

    // LOAD BUS BERDASARKAN ROUTE_ID
    private void loadBuses() {
        try (Connection conn = DBConfig.getConnection()) {

            String sql = "SELECT nama_bus FROM buses WHERE route_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, routeId);

            ResultSet rs = ps.executeQuery();

            busCombo.removeAllItems();

            while (rs.next()) {
                busCombo.addItem(rs.getString("nama_bus"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD JADWAL BERDASARKAN NAMA BUS
    private void loadSchedules() {
        try (Connection conn = DBConfig.getConnection()) {

            jadwalCombo.removeAllItems();

            if (busCombo.getSelectedItem() == null) return;

            String busName = busCombo.getSelectedItem().toString();

            // Ambil bus_id dari nama bus
            String sqlBus = "SELECT id FROM buses WHERE nama_bus = ?";
            PreparedStatement psBus = conn.prepareStatement(sqlBus);
            psBus.setString(1, busName);

            ResultSet rsBus = psBus.executeQuery();
            int busId = -1;

            if (rsBus.next()) {
                busId = rsBus.getInt("id");
            }

            if (busId == -1) return;

            // Ambil jadwal berdasarkan bus_id
            String sql = "SELECT jam FROM schedules WHERE bus_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, busId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jadwalCombo.addItem(rs.getString("jam"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LANJUTKAN KE PEMILIHAN KURSI
    private void openSeatSelection() {
        if (busCombo.getSelectedItem() == null || jadwalCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih bus dan jadwal dahulu!");
            return;
        }

        String busName = busCombo.getSelectedItem().toString();
        String schedule = jadwalCombo.getSelectedItem().toString();

        new SeatSelectionFrame(currentUser, busName, schedule).setVisible(true);
        this.dispose();
    }
}
