/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kirollos
 */
public class DAL {

    private static final String DB_URL = "jdbc:sqlite:patient_records.db";
    private Connection conn = null;

    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL);

    }

    public DAL() throws SQLException {
        conn = getConnection();
        createTable();

    }

    public void createTable() throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS patients (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, operation TEXT, cost DOUBLE, paid DOUBLE, remaining DOUBLE, doctor TEXT, date TEXT, counter TEXT, notes TEXT)";

        try (PreparedStatement statement = conn.prepareStatement(createTableSql)) {
            statement.execute();
        }
    }

    public void insertRecord(PatientRecord record) throws SQLException {
        String insertSql = "INSERT INTO patients (name, phone, operation, cost, paid, remaining, doctor, date, counter, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(insertSql);
        statement.setString(1, record.getName());
        statement.setString(2, record.getPhone());
        statement.setString(3, record.getOperation());
        statement.setDouble(4, record.getCost());
        statement.setDouble(5, record.getPaid());
        statement.setDouble(6, record.getRemaining());
        statement.setString(7, record.getDoctor());
        statement.setString(8, record.getDate());
        statement.setString(9, record.getCounter());
        statement.setString(10, record.getNotes());

        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    public ResultSet getAllRecords() {
        try {
            String queryString = "SELECT * FROM patients";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(queryString);
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Similar methods for update and retrieve data using prepared statements
    public void updatePaidAmount(int recordId, double newPaidAmount) throws SQLException {
        String updateSql = "UPDATE patients SET paid = ?, remaining = cost - paid WHERE id = ?";
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(updateSql);
        statement.setDouble(1, newPaidAmount);
        statement.setInt(2, recordId);
        int rowsUpdated = statement.executeUpdate();
        statement.close();
        conn.close();

        if (rowsUpdated == 1) {
            System.out.println("Record updated successfully!");
        } else {
            System.out.println("Record update failed!");
        }
    }

    public ResultSet searchByName(String patientName) {
        try {
            if (!patientName.isEmpty() && !patientName.isEmpty()) {
                String sql = "SELECT * FROM patients WHERE name LIKE ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + patientName + "%");
                ResultSet result = pstmt.executeQuery();
                return result;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
