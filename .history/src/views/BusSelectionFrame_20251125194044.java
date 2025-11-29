package views;

import com.config.DBConfig;
import java.sql.*;
import javax.swing.*;

public class BusSelectionFrame extends JFrame {

    private JComboBox<String> busCombo;
    private JComboBox<String> jadwalCombo;

    private String currentUser;
    private int routeId;

    public BusSelectionFrame(String username, int routeId) {
        this.currentUser = username;
        this.routeId = routeId;

        setTitle("Pilih Bus & Jadwal");
        setSize(520, 320);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel busLabel = new JLabel("Pilih Bus:");
        busLabel.setBounds(40, 40, 120, 25);
        add(busLabel);

        busCombo = new JComboBox<>();
        busCombo.setBounds(160, 40, 300, 25);
        add(busCombo);

        JLabel jadwalLabel = new JLabel("Pilih Jadwal:");
        jadwalLabel.setBounds(40, 100, 120, 25);
        add(jadwalLabel);

        jadwalCombo = new JComboBox<>();
        jadwalCombo.setBounds(160, 100, 300, 25);
        add(jadwalCombo);

        JButton lanjutBtn = new JButton("LANJUT");
        lanjutBtn.setBounds(200, 200, 120, 40);
        add(lanjutBtn);

        // load data
        loadBuses();
        // listener: saat ganti bus, load jadwal
        busCombo.addActionListener(e -> loadSchedules());
        // agar jadwal muncul untuk item pertama
        if (busCombo.getItemCount() > 0) {
            loadSchedules();
        }

        lanjutBtn.addActionListener(e -> openSeatSelection());
    }

    /**
     * Load daftar bus yang melayani routeId menggunakan tabel bus_routes (many-to-many)
     */
    private void loadBuses() {
        String sql =
            "SELECT DISTINCT b.id, b.nama_bus, b.harga " +
            "FROM buses b " +
            "JOIN bus_routes br ON b.id = br.bus_id " +
            "WHERE br.route_id = ? " +
            "ORDER BY b.nama_bus";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                busCombo.removeAllItems();
                while (rs.next()) {
                    String name = rs.getString("nama_bus");
                    int price = rs.getInt("harga");
                    // tampilkan nama + harga agar pengguna mudah memilih
                    busCombo.addItem(name + "  (Rp " + price + ")");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat daftar bus:\n" + ex.getMessage());
        }
    }

    /**
     * Load jadwal berdasarkan bus yang dipilih.
     * Karena item busCombo menampilkan nama+harga, kita ambil nama bus dengan memotong bagian ' (Rp ...)'
     */
    private void loadSchedules() {
        try (Connection conn = DBConfig.getConnection()) {
            jadwalCombo.removeAllItems();

            if (busCombo.getSelectedItem() == null) return;

            String selected = busCombo.getSelectedItem().toString();
            // ambil nama bus saja (sebelum '  (Rp')
            String busName = selected.split("  \\(Rp")[0].trim();

            // dapatkan bus id
            String sqlBus = "SELECT id FROM buses WHERE nama_bus = ?";
            try (PreparedStatement psBus = conn.prepareStatement(sqlBus)) {
                psBus.setString(1, busName);
                try (ResultSet rsBus = psBus.executeQuery()) {
                    if (!rsBus.next()) {
                        // tidak ditemukan bus
                        return;
                    }
                    int busId = rsBus.getInt("id");

                    // ambil jadwal untuk busId
                    String sql = "SELECT id, jam FROM schedules WHERE bus_id = ? ORDER BY jam";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, busId);
                        try (ResultSet rs = ps.executeQuery()) {
                            boolean any = false;
                            while (rs.next()) {
                                String jam = rs.getString("jam");
                                jadwalCombo.addItem(jam);
                                any = true;
                            }
                            if (!any) {
                                // tampilkan placeholder jika tidak ada jadwal
                                jadwalCombo.addItem("-- Tidak ada jadwal --");
                            }
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal:\n" + ex.getMessage());
        }
    }

    private void openSeatSelection() {
        if (busCombo.getSelectedItem() == null || jadwalCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih bus dan jadwal terlebih dahulu!");
            return;
        }

        String busDisplay = busCombo.getSelectedItem().toString();
        String busName = busDisplay.split("  \\(Rp")[0].trim();
        String jam = jadwalCombo.getSelectedItem().toString();

        // jika placeholder muncul
        if (jam.equals("-- Tidak ada jadwal --")) {
            JOptionPane.showMessageDialog(this, "Tidak ada jadwal untuk bus ini.");
            return;
        }

        // Panggil SeatSelectionFrame dengan nama bus dan jam (SeatSelectionFrame akan konversi ke id)
        new SeatSelectionFrame(currentUser, busName, jam).setVisible(true);
        this.dispose();
    }
}
