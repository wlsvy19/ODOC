package com.fif.cf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConTest {
    public static void main(String[] args) {
        String user = "fif";
        String pw = "fif123123";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/FIFDB?characterEncoding=UTF-8&serverTimezone=UTC";

        try {
            Class.forName(driverName);

            conn = DriverManager.getConnection(url, user, pw);
            System.out.println("DB Connection Success!!!");

            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from user");

            System.out.println("--------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id"));
                System.out.println("NAME: " + rs.getString("name"));
                System.out.println("AGE: " + rs.getString("age"));
            }
            System.out.println("--------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null){ try { rs.close(); } catch (Exception e) {}
            }
            if (stmt != null) { try { stmt.close(); } catch (Exception e) {}
            }
            if (conn != null) { try { conn.close(); } catch (Exception e) {}
            }
        }
    }
}
