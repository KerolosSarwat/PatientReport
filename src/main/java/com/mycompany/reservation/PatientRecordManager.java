/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.reservation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kirollos
 */
public class PatientRecordManager {

    private JFrame frame;
    private JTextField patientNameField, phoneField, operationNameField, costField, paidField, doctorNameField, notesField, searchField;
    private JSpinner dateSpinner;
    private JButton addButton, updateButton, searchButton;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private ArrayList<PatientRecord> records;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> counterList;
    private DAL dal = null;

    public PatientRecordManager() {
        records = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {

        try {
            dal = new DAL();
            tableModel = new DefaultTableModel(new String[]{"اسم المريض", "الموبايل", "العملية", "التكلفة", "المدفوع", "الباقى", "الطبيب", "تاريخ العملية", "الكونتر", "ملاحظات"}, 0);
            ResultSet rec = dal.getAllRecords();
            while (rec.next()) {
                tableModel.addRow(new Object[]{rec.getString(2), rec.getString(3), rec.getString(4), rec.getString(5), rec.getString(6), rec.getString(7), rec.getString(8), rec.getString(9), rec.getString(10), rec.getString(11)});
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame = new JFrame("Patient Record Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(12, 2, 5, 5), true);

        patientNameField = new JTextField();
        phoneField = new JTextField();
        operationNameField = new JTextField();
        costField = new JTextField();
        paidField = new JTextField();
        doctorNameField = new JTextField();
        String[] counter = {"", "بيشوى", "ديماس", "رضا", "عزيزة", "مايزة"};
        counterList = new JComboBox<>(counter);
        notesField = new JTextField();
        searchField = new JTextField();
        searchButton = new JButton("بحث بألأسم");
        addButton = new JButton("اضافة مريض");
        updateButton = new JButton("تحديث التكلفة");
        updateButton.setEnabled(false);
        //inputPanel.add(new JLabel("بحث باﻷسم"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(new JLabel("اسم المريض"));
        inputPanel.add(patientNameField);
        inputPanel.add(new JLabel("الموبايل"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("العملية"));
        inputPanel.add(operationNameField);
        inputPanel.add(new JLabel("التكلفة"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("المدفوع"));
        inputPanel.add(paidField);
        inputPanel.add(new JLabel("الطبيب"));
        inputPanel.add(doctorNameField);
        inputPanel.add(new JLabel("الكونتر"));
        inputPanel.add(counterList);
        inputPanel.add(new JLabel("تاريخ العملية"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        inputPanel.add(dateSpinner);
        inputPanel.add(new JLabel("ملاحظات"));
        inputPanel.add(notesField);
        // Table Panel

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);

        // Add action listener
//        searchButton.addActionListener(this);
        // Layout
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener((ActionEvent e) -> {
            addPatientRecord();
        });

        updateButton.addActionListener((ActionEvent e) -> {
            updatePaidAmount();
        });

        searchButton.addActionListener((ActionEvent e) -> {
            searchPatient(searchField.getText());
        });

        patientTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (patientTable.getSelectedRow() >= 0) {
                updateButton.setEnabled(true);
            } else {
                updateButton.setEnabled(false);
            }
        });

        frame.setVisible(true);
    }

    private void addPatientRecord() {
        String name = patientNameField.getText();
        String phone = phoneField.getText();
        String operation = operationNameField.getText();
        String costStr = costField.getText();
        String paidStr = paidField.getText();
        String doctor = doctorNameField.getText();
        Date date = (Date) dateSpinner.getModel().getValue();
        String counter = (String) counterList.getSelectedItem();
        String notes = notesField.getText();

        if (name.isEmpty() || phone.isEmpty() || operation.isEmpty() || costStr.isEmpty() || paidStr.isEmpty() || doctor.isEmpty() || counter.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "من فضلك املأ البيان كاملا");
            return;
        }

        double cost;
        double paid;
        try {
            cost = Double.parseDouble(costStr);
            paid = Double.parseDouble(paidStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "التكلفة و المدفوع يجب ان تكون أرقام");
            return;
        }

        if (cost < 0 || paid < 0 || paid > cost) {
            JOptionPane.showMessageDialog(frame, "المدفوع يجب ان يكون اقل من او يساوى التكلفة");
            return;
        }

        double remaining = cost - paid;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        PatientRecord record = new PatientRecord(name, phone, operation, cost, paid, remaining, doctor, formattedDate, counter, notes);
        records.add(record);

        try {
            DAL dal = new DAL();
            dal.insertRecord(record);
        } catch (SQLException ex) {
            Logger.getLogger(PatientRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Update table model
        tableModel.addRow(new Object[]{record.getName(), record.getPhone(), record.getOperation(), record.getCost(), record.getPaid(), record.getRemaining(), record.getDoctor(), record.getDate(), record.getCounter(), record.getNotes()});

        // Clear input fields
        patientNameField.setText("");
        phoneField.setText("");
        operationNameField.setText("");
        costField.setText("");
        paidField.setText("");
        doctorNameField.setText("");

    }

    private void searchPatient(String name) {
        try {
            // clear table before insert in it
            tableModel.setRowCount(0);

            // insert selected row
            ResultSet rec = dal.searchByName(name);
            if (rec == null) {
                JOptionPane.showMessageDialog(frame, "لا يوجد مريض بهذا اﻷسم");
                return;
            }
            while (rec.next()) {
                tableModel.addRow(new Object[]{rec.getString(2), rec.getString(3), rec.getString(4), rec.getString(5), rec.getString(6), rec.getString(7), rec.getString(8), rec.getString(9)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void updatePaidAmount() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String paidStr = paidField.getText();
            if (paidStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a new paid amount!");
                return;
            }

            double newPaid;
            try {
                newPaid = Double.parseDouble(paidStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Paid amount must be a number!");
                return;
            }

            if (newPaid < 0) {
                JOptionPane.showMessageDialog(frame, "Paid amount cannot be negative!");
                return;
            }

            PatientRecord record = records.get(selectedRow);
            record.setPaid(newPaid);
            record.setRemaining(record.getCost() - newPaid);

            // Update table model data
            tableModel.setValueAt(newPaid, selectedRow, 4);
            tableModel.setValueAt(record.getRemaining(), selectedRow, 5);

            // Clear input field
            paidField.setText("");
        }
    }
}
