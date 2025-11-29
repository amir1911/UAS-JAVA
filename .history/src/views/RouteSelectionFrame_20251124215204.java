package views;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import com.config.DBConfig;

public class RouteSelectionFrame extends JFrame {

    private JComboBox<String> asalCombo;
    private JComboBox<String> tujuanCombo;
    private ArrayList<Integer> routeIdList = new ArrayList<>();
    private String currentUser;

    public RouteSelectionFrame(String username) {
        this.currentUser = username;

        setTitle("Pilih Rute Perjalanan");
        setSize(420, 260);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel asalLabel = new JLabel("Asal:");
        asalLabel.setBounds(40, 40, 120, 25);
        add(asalLabel);

        asalCombo = new JComboBox<>();
        asalCombo.setBounds(150, 40, 200, 25);
        add(asalCombo);

        JLabel tujuanLabel = new JLabel("Tujuan:");
        tujuanLabel.setBounds(40, 90, 120, 25);
        add(tujuanLabel);

        tujuanCombo = new JComboBox<>();
        tujuanCombo.setBounds(150, 90, 200, 25);
        add(tujuanCombo);

        JButton lanjutBtn = new JButton("LANJUT");
        lanjutBtn.setBounds(150, 150, 120, 40);
        add(lanjutBtn);

        loadRoutes();

        lanjutBtn.addActionListener(e -> openBusSelection());
    }

    private void loadRoutes() {
        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM routes")) {

            routeIdList.clear();
            asalCombo.removeAllItems();
            tujuanCombo.removeAllItems();

            while (rs.next()) {
                asalCombo.addItem(rs.getString("asal"));
                tujuanCombo.addItem(rs.getString("tujuan"));
                routeIdList.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBusSelection() {
        int idx = asalCombo.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Pilih rute dahulu!");
            return;
        }

        int routeId = routeIdList.get(idx);
        new BusSelectionFrame(currentUser, routeId).setVisible(true);
        this.dispose();
    }
}
