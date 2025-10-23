/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffAccountFrame extends JFrame {
    private String staffUsername;
    private JPanel rightPanel;

    public StaffAccountFrame(String staffUsername) {
        this.staffUsername = staffUsername;

        setTitle("Banking System - Staff Panel");
        setSize(900, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, 200, 500);
        leftPanel.setBackground(new Color(90, 113, 161));

        JButton profileBtn = new JButton("View Profile");
        profileBtn.setBounds(20, 50, 160, 40);
        leftPanel.add(profileBtn);

        JButton userDetailsBtn = new JButton("View User Details");
        userDetailsBtn.setBounds(20, 120, 160, 40);
        leftPanel.add(userDetailsBtn);

        JButton userTransactionBtn = new JButton("View Transactions");
        userTransactionBtn.setBounds(20, 190, 160, 40);
        leftPanel.add(userTransactionBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(20, 260, 160, 40);
        leftPanel.add(logoutBtn);

        add(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBounds(200, 0, 700, 500);
        add(rightPanel);

        // Button actions
        profileBtn.addActionListener(e -> showProfile());
        userDetailsBtn.addActionListener(e -> showAllUsers());
        userTransactionBtn.addActionListener(e -> showUserTransactions());
        logoutBtn.addActionListener(e -> logout());
    }

    // VIEW STAFF PROFILE
    private void showProfile() {
        rightPanel.removeAll();
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Staff Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(250, 10, 200, 30);
        rightPanel.add(titleLabel);

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField staffIdField = new JTextField();
        JTextField designationField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JLabel[] labels = {
                new JLabel("Name:"), new JLabel("Phone:"), new JLabel("Email:"),
                new JLabel("Staff ID:"), new JLabel("Designation:"), new JLabel("Username:"), new JLabel("Password:")
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(50, 50 + i * 40, 100, 25);
            rightPanel.add(labels[i]);
        }

        nameField.setBounds(160, 50, 200, 25);
        phoneField.setBounds(160, 90, 200, 25);
        emailField.setBounds(160, 130, 200, 25);
        staffIdField.setBounds(160, 170, 200, 25);
        designationField.setBounds(160, 210, 200, 25);
        usernameField.setBounds(160, 250, 200, 25);
        passField.setBounds(160, 290, 200, 25);

        rightPanel.add(nameField);
        rightPanel.add(phoneField);
        rightPanel.add(emailField);
        rightPanel.add(staffIdField);
        rightPanel.add(designationField);
        rightPanel.add(usernameField);
        rightPanel.add(passField);

        // Load staff data
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM staff WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, staffUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
                emailField.setText(rs.getString("email"));
                staffIdField.setText(rs.getString("staff_id"));
                designationField.setText(rs.getString("designation"));
                usernameField.setText(rs.getString("username"));
                passField.setText(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update & Delete Button
        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(160, 340, 100, 30);
        rightPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(280, 340, 100, 30);
        rightPanel.add(deleteBtn);

        // Update Button Action
        updateBtn.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "UPDATE staff SET name=?, phone=?, email=?, staff_id=?, designation=?, password=? WHERE username=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, phoneField.getText());
                ps.setString(3, emailField.getText());
                ps.setString(4, staffIdField.getText());
                ps.setString(5, designationField.getText());
                ps.setString(6, new String(passField.getPassword()));
                ps.setString(7, staffUsername);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Delete Button Action
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this staff account?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = DBConnection.getConnection()) {
                    String sql = "DELETE FROM staff WHERE username=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, staffUsername);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                        dispose();
                        new BankFrame().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Deletion failed!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // VIEW ALL USERS 
    private void showAllUsers() {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        tableModel.addColumn("Name");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Email");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Balance");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getDouble("balance")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // VIEW USER TRANSACTIONS 
    private void showUserTransactions() {
        String username = JOptionPane.showInputDialog(this, "Enter username to view transactions:");
        if (username == null || username.isEmpty()) return;

        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        tableModel.addColumn("Type");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE username=? ORDER BY date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                tableModel.addRow(new Object[]{
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("date")
                });
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No transactions found for user: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // LOGOUT
    private void logout() {
        dispose(); // Close current window
        new BankFrame().setVisible(true); // Open main frame
    }

    public static void main(String[] args) {
        new StaffAccountFrame("staffusername").setVisible(true);
    }
}