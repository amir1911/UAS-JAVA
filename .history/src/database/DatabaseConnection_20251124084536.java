package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/login_app";
    private static final String USER = "root";  // default Laragon
    private static final String PASS = "";      // kosong

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Berhasil!");
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi GAGAL: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        getConnection(); // Untuk test koneksi
    }
}
