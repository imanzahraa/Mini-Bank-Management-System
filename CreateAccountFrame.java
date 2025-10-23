/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class CreateAccountFrame extends JFrame {
    JTextField nameField, phoneField, emailField, usernameField;
    JPasswordField passField;
    JRadioButton maleBtn, femaleBtn;
    JButton submitBtn, backBtn;

    public CreateAccountFrame() {
        setTitle("Banking System - Create Account");
        setSize(450, 380);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Create Account");
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
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 250, 100, 25);
        add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(150, 250, 200, 25);
        add(passField);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(100, 300, 100, 30);
        add(submitBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(230, 300, 100, 30);
        add(backBtn);

        // Button actions
        submitBtn.addActionListener(e -> createAccount());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    //  Create new account
    private void createAccount() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String gender = maleBtn.isSelected() ? "Male" : (femaleBtn.isSelected() ? "Female" : "");
        String username = usernameField.getText();
        String password = new String(passField.getPassword());

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (name, phone, email, gender, username, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, gender);
            ps.setString(5, username);
            ps.setString(6, password);

            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this,
                        "✅ Account Created Successfully!\nUsername: " + username + "\nPassword: " + password);
                clearFields();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Username already exists! Choose another.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    //  Clear input fields
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passField.setText("");
        maleBtn.setSelected(false);
        femaleBtn.setSelected(false);
    }
}