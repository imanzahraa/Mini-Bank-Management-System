/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banksystem.banksystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/accountdb";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return a working connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ MySQL JDBC Driver not found!", e);
        } catch (SQLException e) {
            throw new SQLException("❌ Failed to connect to database: " + e.getMessage(), e);
        }
    }
}

