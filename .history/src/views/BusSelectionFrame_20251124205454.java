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

    public BusSelectionFrame(String username) {
        this.currentUser = username;

        setTitle("Pilih Bus & Jadwal");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel busLabel = new JLabel("Pilih Bus:");
        busLabel.setBounds(20, 30, 120, 25);
        add(busLabel);

        busCombo = new JComboBox<>();
        busCombo.setBounds(140, 30, 200, 25);
        add(busCombo);

        JLabel scheduleLabel = new JLabel("Pilih Jadwal:");
        scheduleLabel.setBounds(20, 80, 120, 25);
        add(scheduleLabel);

        scheduleCombo = new JComboBox<>();
        scheduleCombo.setBounds(140, 80, 200, 25);
        add(scheduleCombo);

        JButton lanjutBtn = new JButton("LANJUT");
        lanjutBtn.setBounds(140, 150, 120, 40);
        add(lanjutBtn);

        loadBusList();

        busCombo.addActionListener(e -> loadScheduleList());

        lanjutBtn.addActionListener(e -> openSeatSelection());
    }

    private void loadBusList() {
        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM buses")) {

            busCombo.removeAllItems();
            busIdList.clear();

            while (rs.next()) {
                busCombo.addItem(rs.getString("nama_bus"));
                busIdList.add(rs.getInt("id"));
            }

            if (busCombo.getItemCount() > 0) {
                loadScheduleList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadScheduleList() {
        int index = busCombo.getSelectedIndex();
        if (index < 0) return;

        int busId = busIdList.get(index);

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedules WHERE bus_id=?")) {

            ps.setInt(1, busId);
            ResultSet rs = ps.executeQuery();

            scheduleCombo.removeAllItems();
            scheduleIdList.clear();

            while (rs.next()) {
                scheduleCombo.addItem(rs.getString("jam"));
                scheduleIdList.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSeatSelection() {
        if (busCombo.getSelectedIndex() < 0 || scheduleCombo.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Pilih Bus dan Jadwal terlebih dahulu!");
            return;
        }

        String busName = busCombo.getSelectedItem().toString();
        String schedule = scheduleCombo.getSelectedItem().toString();

        JOptionPane.showMessageDialog(this,
            "Bus: " + busName +
            "\nJadwal: " + schedule +
            "\nLanjut memilih kursi...");

        new SeatSelectionFrame(currentUser).setVisible(true);
        this.dispose();
    }
}
