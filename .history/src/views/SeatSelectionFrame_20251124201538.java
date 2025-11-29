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
namaLabel.setBounds(20, 20, 120, 25);
add(namaLabel);


namaField = new JTextField();
namaField.setBounds(150, 20, 230, 25);
add(namaField);


JLabel seatLabel = new JLabel("Pilih Kursi:");
seatLabel.setBounds(20, 60, 100, 25);
add(seatLabel);


loadSeats();


JButton pesanBtn = new JButton("PESAN");
pesanBtn.setBounds(160, 380, 100, 40);
pesanBtn.addActionListener(e -> confirmBooking());
add(pesanBtn);
}


private void loadSeats() {
java.util.List<Map<String,String>> seats = seatService.getAllSeats();
int x = 20, y = 100;
for (Map<String,String> s : seats) {
String seatNo = s.get("seat_no");
String status = s.get("status");
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