package views;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import services.SeatService;

public class SeatSelectionFrame extends JFrame {

    private SeatService seatService = new SeatService();
    private Map<String, JButton> seatButtons = new HashMap<>();
    private java.util.List<String> selectedSeats = new ArrayList<>();
    private JTextField namaField;
    private String currentUser;

    public SeatSelectionFrame(String username) {
        this.currentUser = username;
        setTitle("Pemesanan Tiket Bus - User: " + username);
        setSize(420, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel namaLabel = new JLabel("Nama Penumpang:");
        namaLabel.setBounds(20, 20, 150, 25);
        add(namaLabel);

        namaField = new JTextField();
        namaField.setBounds(150, 20, 230, 25);
        add(namaField);

        JLabel seatLabel = new JLabel("Pilih Kursi:");
        seatLabel.setBounds(20, 60, 100, 25);
        add(seatLabel);

        loadSeats();

        JButton pesanBtn = new JButton("PESAN");
        pesanBtn.setBounds(150, 400, 120, 40);
        pesanBtn.addActionListener(e -> confirmBooking());
        add(pesanBtn);
    }

    private void loadSeats() {
        java.util.List<Map<String, String>> seats = seatService.getAllSeats();

        int x = 20, y = 100;
        for (Map<String, String> seat : seats) {
            String seatNo = seat.get("seat_no");
            String status = seat.get("status");

            JButton btn = new JButton(seatNo);
            btn.setBounds(x, y, 80, 40);

            if ("BOOKED".equals(status)) {
                btn.setBackground(Color.RED);
                btn.setEnabled(false);
            } else {
                btn.setBackground(Color.GREEN);
                btn.addActionListener(e -> toggleSeat(btn));
            }

            seatButtons.put(seatNo, btn);
            add(btn);

            x += 90;
            if (x > 300) {
                x = 20;
                y += 60;
            }
        }
    }

    private void toggleSeat(JButton btn) {
        String seatNo = btn.getText();

        if (selectedSeats.contains(seatNo)) {
            selectedSeats.remove(seatNo);
            btn.setBackground(Color.GREEN);
        } else {
            selectedSeats.add(seatNo);
            btn.setBackground(Color.ORANGE);
        }
    }

    private void confirmBooking() {
        if (namaField.getText().isEmpty() || selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan kursi harus diisi!");
            return;
        }

        int harga = 30000;
        int total = harga * selectedSeats.size();

        boolean allOk = true;

        for (String seat : selectedSeats) {
            boolean success = seatService.bookSeat(seat);

            if (!success) {
                allOk = false;
            } else {
                seatService.saveTicket(namaField.getText(), seat, harga);
            }
        }

        if (allOk) {
            JOptionPane.showMessageDialog(this,
                "Pemesanan berhasil!\n" +
                "Nama: " + namaField.getText() + "\n" +
                "Kursi: " + selectedSeats + "\n" +
                "Total: Rp " + total
            );

            // Refresh halaman
            this.dispose();
            new SeatSelectionFrame(currentUser).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                "Beberapa kursi sudah terambil,\n" +
                "silakan pilih ulang kursi yang tersedia.");
        }
    }
}
