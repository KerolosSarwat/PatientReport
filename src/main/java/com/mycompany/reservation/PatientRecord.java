/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.reservation;

/**
 *
 * @author kirollos
 */
public class PatientRecord {
    private String name;
    private String phone;
    private String operation;
    private double cost;
    private double paid;
    private double remaining;
    private String doctor;
    private String date;
    private String counter;
    private String notes;

    public PatientRecord(String name, String phone, String operation, double cost, double paid, double remaining, String doctor, String date, String counter, String notes) {
        this.name = name;
        this.phone = phone;
        this.operation = operation;
        this.cost = cost;
        this.paid = paid;
        this.remaining = remaining;
        this.doctor = doctor;
        this.date = date;
        this.counter = counter;
        this.notes = notes;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
