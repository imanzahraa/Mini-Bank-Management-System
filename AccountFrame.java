/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AccountFrame extends JFrame {
    private final String username;
    private JLabel balanceLabel;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public AccountFrame(String username) {
        this.username = username;

        setTitle("Banking System - Account Page");
        setSize(720, 520);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245)); // light gray background

        // Title
        JLabel titleLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(0, 15, 720, 30);
        add(titleLabel);

        // Balance
        balanceLabel = new JLabel("Balance: ₹0.00");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        balanceLabel.setBounds(50, 60, 300, 25);
        add(balanceLabel);

        // Buttons Row
        int frameWidth = 720;
        int btnWidth = 140;
        int btnHeight = 30;
        int spacing = 20; 
        int totalWidth = 4 * btnWidth + 3 * spacing;
        int startX = (frameWidth - totalWidth) / 2;
        int btnY = 100;

        JButton initialDepositBtn = new JButton("Initial Deposit");
        initialDepositBtn.setBounds(startX, btnY, btnWidth, btnHeight);
        add(initialDepositBtn);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(startX + (btnWidth + spacing), btnY, btnWidth, btnHeight);
        add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(startX + 2 * (btnWidth + spacing), btnY, btnWidth, btnHeight);
        add(withdrawBtn);

        JButton profileBtn = new JButton("Profile Details");
        profileBtn.setBounds(startX + 3 * (btnWidth + spacing), btnY, btnWidth, btnHeight);
        add(profileBtn);

        // Transactions Table
        tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Date"}, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(50, 150, 620, 260);
        add(scrollPane);

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds((frameWidth - 100) / 2, 430, 100, 30); // centered horizontally
        add(logoutBtn);

        // Actions
        initialDepositBtn.addActionListener(e -> initialDeposit());
        depositBtn.addActionListener(e -> performTransaction("Deposit"));
        withdrawBtn.addActionListener(e -> performTransaction("Withdraw"));
        profileBtn.addActionListener(e -> viewProfileFrame());
        logoutBtn.addActionListener(e -> {
            dispose();
            new BankFrame().setVisible(true);
        });

        // Load data
        loadBalance();
        loadTransactions();
    }

    // Transactions

    private void initialDeposit() {
        try {
            double currentBalance = getBalance();
            if (currentBalance > 0) {
                JOptionPane.showMessageDialog(this, "Initial deposit already made!");
                return;
            }

            String amountStr = JOptionPane.showInputDialog(this, "Enter initial deposit amount:");
            if (amountStr == null || amountStr.isEmpty()) return;
            double amount = Double.parseDouble(amountStr);

            updateBalance(amount);
            recordTransaction("Initial Deposit", amount);
            JOptionPane.showMessageDialog(this, "Initial deposit of ₹" + amount + " successful!");
            loadBalance();
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performTransaction(String type) {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to " + type.toLowerCase() + ":");
        if (amountStr == null || amountStr.isEmpty()) return;
        double amount = Double.parseDouble(amountStr);

        try {
            double currentBalance = getBalance();
            if (type.equals("Withdraw") && amount > currentBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
                return;
            }

            double newBalance = type.equals("Deposit") ? currentBalance + amount : currentBalance - amount;
            updateBalance(newBalance);
            recordTransaction(type, amount);
            JOptionPane.showMessageDialog(this, type + " of ₹" + amount + " successful!");
            loadBalance();
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordTransaction(String type, double amount) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO transactions (username, type, amount) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        }
    }

    private void loadBalance() {
        double balance = getBalance();
        balanceLabel.setText("Balance: ₹" + balance);
    }

    private double getBalance() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT balance FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateBalance(double amount) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE users SET balance=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    private void loadTransactions() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT type, amount, date FROM transactions WHERE username=? ORDER BY date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Profile Frame
    private void viewProfileFrame() {
        JFrame profileFrame = new JFrame("Profile Details");
        profileFrame.setSize(400, 400);
        profileFrame.setLayout(null);
        profileFrame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Profile Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(130, 10, 200, 30);
        profileFrame.add(titleLabel);

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JRadioButton maleBtn = new JRadioButton("Male");
        JRadioButton femaleBtn = new JRadioButton("Female");
        JTextField usernameField = new JTextField();
        JPasswordField passField = new JPasswordField();

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);

        JLabel[] labels = {
                new JLabel("Name:"), new JLabel("Phone:"), new JLabel("Email:"),
                new JLabel("Gender:"), new JLabel("Username:"), new JLabel("Password:")
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(30, 50 + i * 40, 100, 25);
            profileFrame.add(labels[i]);
        }

        nameField.setBounds(150, 50, 200, 25);
        phoneField.setBounds(150, 90, 200, 25);
        emailField.setBounds(150, 130, 200, 25);
        maleBtn.setBounds(150, 170, 80, 25);
        femaleBtn.setBounds(230, 170, 80, 25);
        usernameField.setBounds(150, 210, 200, 25);
        passField.setBounds(150, 250, 200, 25);

        profileFrame.add(nameField);
        profileFrame.add(phoneField);
        profileFrame.add(emailField);
        profileFrame.add(maleBtn);
        profileFrame.add(femaleBtn);
        profileFrame.add(usernameField);
        profileFrame.add(passField);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(50, 300, 100, 30);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(170, 300, 100, 30);
        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(290, 300, 80, 30);

        profileFrame.add(updateBtn);
        profileFrame.add(deleteBtn);
        profileFrame.add(closeBtn);

        // Load user data
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
                emailField.setText(rs.getString("email"));
                if ("Male".equals(rs.getString("gender"))) maleBtn.setSelected(true);
                else femaleBtn.setSelected(true);
                usernameField.setText(rs.getString("username"));
                passField.setText(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateBtn.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "UPDATE users SET name=?, phone=?, email=?, gender=?, password=? WHERE username=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, phoneField.getText());
                ps.setString(3, emailField.getText());
                ps.setString(4, maleBtn.isSelected() ? "Male" : "Female");
                ps.setString(5, new String(passField.getPassword()));
                ps.setString(6, username);
                int row = ps.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(profileFrame, "Account Updated Successfully!");
                    loadBalance();
                    loadTransactions();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(profileFrame,
                    "Are you sure you want to delete your account?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = DBConnection.getConnection()) {
                    String sql = "DELETE FROM users WHERE username=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, username);
                    int row = ps.executeUpdate();
                    if (row > 0) {
                        JOptionPane.showMessageDialog(profileFrame, "Account Deleted Successfully!");
                        profileFrame.dispose();
                        dispose();
                        new LoginFrame().setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        closeBtn.addActionListener(e -> profileFrame.dispose());
        profileFrame.setVisible(true);
    }
}