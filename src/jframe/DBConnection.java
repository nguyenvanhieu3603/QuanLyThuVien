/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframe;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author hieu
 */
public class DBConnection {

    static Connection connect = null;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlythuvien", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connect;
    }
}
