package com.banksystem.banksystem;

import javax.swing.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, createAccountBtn;

    public LoginFrame() {
        setTitle("Banking System - Login");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setBounds(170, 10, 100, 25);
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
            new CreateAccountFrame().setVisible(true);
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
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new AccountFrame(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}