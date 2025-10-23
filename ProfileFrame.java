/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;
import javax.swing.*;
import java.sql.*;

public class ProfileFrame extends JFrame {
    JTextField nameField, phoneField, emailField, usernameField;
    JPasswordField passField;
    JRadioButton maleBtn, femaleBtn;
    JButton updateBtn, deleteBtn, backBtn;
    String currentUsername;

    public ProfileFrame(String username) {
        this.currentUsername = username;

        setTitle("Profile Details");
        setSize(450, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Profile Details");
        titleLabel.setBounds(160, 10, 150, 25);
        add(titleLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 50, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 25);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 90, 100, 25);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(150, 90, 200, 25);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 130, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(150, 130, 200, 25);
        add(emailField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(30, 170, 100, 25);
        add(genderLabel);
        maleBtn = new JRadioButton("Male");
        maleBtn.setBounds(150, 170, 80, 25);
        femaleBtn = new JRadioButton("Female");
        femaleBtn.setBounds(230, 170, 80, 25);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        add(maleBtn);
        add(femaleBtn);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 210, 100, 25);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 210, 200, 25);
        usernameField.setEditable(false);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 250, 100, 25);
        add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(150, 250, 200, 25);
        add(passField);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(50, 300, 100, 30);
        add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(180, 300, 100, 30);
        add(deleteBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(310, 300, 100, 30);
        add(backBtn);

        loadUserData();

        updateBtn.addActionListener(e -> updateAccount());
        deleteBtn.addActionListener(e -> deleteAccount());
        backBtn.addActionListener(e -> {
            new AccountFrame(currentUsername).setVisible(true);
            dispose();
        });
    }

    private void loadUserData() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currentUsername);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
                emailField.setText(rs.getString("email"));
                usernameField.setText(rs.getString("username"));
                passField.setText(rs.getString("password"));
                String gender = rs.getString("gender");
                if ("Male".equalsIgnoreCase(gender)) maleBtn.setSelected(true);
                else femaleBtn.setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAccount() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String gender = maleBtn.isSelected() ? "Male" : "Female";
        String password = new String(passField.getPassword());

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE users SET name=?, phone=?, email=?, gender=?, password=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, gender);
            ps.setString(5, password);
            ps.setString(6, currentUsername);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "DELETE FROM users WHERE username=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, currentUsername);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                new LoginFrame().setVisible(true);
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}