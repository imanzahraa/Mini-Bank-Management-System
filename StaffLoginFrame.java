/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;

import javax.swing.*;
import java.sql.*;

public class StaffLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, createAccountBtn;

    public StaffLoginFrame() {
        setTitle("Banking System - Staff Login");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Staff Login");
        titleLabel.setBounds(160, 10, 100, 25);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 180, 25);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 140, 100, 30);
        add(loginBtn);

        createAccountBtn = new JButton("Create Account");
        createAccountBtn.setBounds(200, 140, 130, 30);
        add(createAccountBtn);

        loginBtn.addActionListener(e -> login());
        createAccountBtn.addActionListener(e -> {
            new StaffCreateAccountFrame().setVisible(true);
            dispose();
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM staff WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Staff logged in successfully
                new StaffAccountFrame(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
