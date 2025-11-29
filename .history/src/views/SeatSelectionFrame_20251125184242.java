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
    private String busName;
    private String scheduleJam;

    private int busId;
    private int scheduleId;

    public SeatSelectionFrame(String username, String busName, String scheduleJam) {
        this.currentUser = username;
        this.busName = busName;
        this.scheduleJam = scheduleJam;

        // Ambil bus_id dari nama bus
        try {
            busId = seatService.getBusIdByName(busName);
            scheduleId = seatService.getScheduleId(busId, scheduleJam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Pilih Kursi - " + busName);
        setSize(480, 540);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel namaLabel = new JLabel("Nama Penumpang:");
        namaLabel.setBounds(20, 20, 150, 25);
        add(namaLabel);

        namaField = new JTextField();
        namaField.setBounds(170, 20, 270, 25);
        add(namaField);

        JLabel seatLabel = new JLabel("Pilih Kursi:");
        seatLabel.setBounds(20, 60, 200, 25);
        add(seatLabel);

        loadSeats(busId);

        JButton pesanBtn = new JButton("PESAN");
        pesanBtn.setBounds(180, 440, 120, 40);
        pesanBtn.addActionListener(e -> confirmBooking());
        add(pesanBtn);
    }

    private void loadSeats(int busId) {
        java.util.List<Map<String, String>> seats = seatService.getSeatsByBus(busId);

        int x = 20, y = 100;
        for (Map<String, String> seat : seats) {
            String seatNo = seat.get("seat_no");
            String status = seat.get("status");

            JButton btn = new JButton(seatNo);
            btn.setBounds(x, y, 90, 45);

            if ("BOOKED".equals(status)) {
                btn.setBackground(Color.RED);
                btn.setEnabled(false);
            } else {
                btn.setBackground(Color.GREEN);
                btn.addActionListener(e -> toggleSeat(btn));
            }

            seatButtons.put(seatNo, btn);
            add(btn);

            x += 100;
            if (x > 380) {
                x = 20;
                y += 70;
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

        int harga = seatService.getBusPrice(busId);
        int total = harga * selectedSeats.size();

        boolean allOk = true;

        for (String seat : selectedSeats) {
            boolean ok = seatService.bookSeat(busId, seat);
            if (!ok) {
                allOk = false;
            } else {
                seatService.saveTicket(namaField.getText(), busId, scheduleId, seat, harga);
            }
        }

        if (allOk) {
            JOptionPane.showMessageDialog(this,
                "Pemesanan berhasil!\n" +
                "Nama: " + namaField.getText() + "\n" +
                "Bus: " + busName + "\n" +
                "Jadwal: " + scheduleJam + "\n" +
                "Kursi: " + selectedSeats + "\n" +
                "Harga per kursi: Rp " + harga + "\n" +
                "Total: Rp " + total
            );

            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                "Beberapa kursi gagal dipesan (sudah terambil).");
        }
    }
}
