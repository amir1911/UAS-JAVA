package com.config;

import java.sql.Connection;
import java.sql.DriverManager;


public class cConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/login_app";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Berhasil!");
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }

    // TEST
    public static void main(String[] args) {
        getConnection();
    }
}