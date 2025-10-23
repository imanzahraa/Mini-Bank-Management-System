/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;

import javax.swing.*;
import java.sql.*;

public class StaffCreateAccountFrame extends JFrame {
    JTextField nameField, phoneField, emailField, usernameField, staffIdField, designationField;
    JPasswordField passField;
    JRadioButton maleBtn, femaleBtn;
    JButton submitBtn, backBtn;

    public StaffCreateAccountFrame() {
        setTitle("Banking System - Create Staff Account");
        setSize(500, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Create Staff Account");
        titleLabel.setBounds(160, 10, 200, 25);
        add(titleLabel);

        // Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 50, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 50, 250, 25);
        add(nameField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 90, 100, 25);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(150, 90, 250, 25);
        add(phoneField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 130, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(150, 130, 250, 25);
        add(emailField);

        // Gender
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

        // Staff ID
        JLabel staffIdLabel = new JLabel("Staff ID:");
        staffIdLabel.setBounds(30, 210, 100, 25);
        add(staffIdLabel);
        staffIdField = new JTextField();
        staffIdField.setBounds(150, 210, 250, 25);
        add(staffIdField);

        // Designation
        JLabel designationLabel = new JLabel("Designation:");
        designationLabel.setBounds(30, 250, 100, 25);
        add(designationLabel);
        designationField = new JTextField();
        designationField.setBounds(150, 250, 250, 25);
        add(designationField);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 290, 100, 25);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 290, 250, 25);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 330, 100, 25);
        add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(150, 330, 250, 25);
        add(passField);

        // Buttons
        submitBtn = new JButton("Submit");
        submitBtn.setBounds(120, 370, 100, 30);
        add(submitBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(250, 370, 100, 30);
        add(backBtn);

        // Button actions
        submitBtn.addActionListener(e -> createStaffAccount());
        backBtn.addActionListener(e -> {
            new StaffLoginFrame().setVisible(true);
            dispose();
        });
    }

    private void createStaffAccount() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String gender = maleBtn.isSelected() ? "Male" : (femaleBtn.isSelected() ? "Female" : "");
        String staffId = staffIdField.getText();
        String designation = designationField.getText();
        String username = usernameField.getText();
        String password = new String(passField.getPassword());

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || staffId.isEmpty() || designation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO staff (name, phone, email, gender, staff_id, designation, username, password) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, gender);
            ps.setString(5, staffId);
            ps.setString(6, designation);
            ps.setString(7, username);
            ps.setString(8, password);

            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this,
                        "✅ Staff Account Created Successfully!\nUsername: " + username + "\nPassword: " + password);
                clearFields();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Username or Staff ID already exists! Choose another.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passField.setText("");
        staffIdField.setText("");
        designationField.setText("");
        maleBtn.setSelected(false);
        femaleBtn.setSelected(false);
    }
}