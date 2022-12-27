package com.fif.app.Dmybatis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
    public static void main(String[] args) {
        int dbType = 3;

        String user = "202104student";
        String pw = "202104student!!";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String driverName = null;
        String url = null;
        String queryString = null;

        if (dbType == 1) {
            // oracle
            driverName = "oracle.jdbc.driver.OracleDrvier";
            url = "jdbc:oracle:thin:@11.211.48.17:1523:DBDEAI";
            queryString = "SELECT 1 FROM DUAL";
        } else if(dbType == 2) {
            // mysql
            driverName = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://11.211.48.17:3306/DBDEAI?serverTimeZone=UTC";
            queryString = "SELECT 1";
        } else {
            // maria db
            driverName = "org.mariadb.jdbc.Driver";
            url = "jdbc:mariadb://121.133.147.152:5001/202104_academy?serverTimeZone=UTC";
            queryString = "select * from user";
        }

        try {
            // 1. driver load
            Class.forName(driverName);

            // 2. db connection
            conn = DriverManager.getConnection(url, user, pw);
            System.out.println("connection success");

            // 3. statement connection
            stmt = conn.createStatement();

            // 4. run
            System.out.println("execute query: " + queryString);
            rs = stmt.executeQuery(queryString);

            System.out.println("--------------------------");
            while(rs.next()) {;
            System.out.println("ID: " + rs.getString("id"));
            System.out.println("PW: " + rs.getString("pw"));
            System.out.println("--------------------------");
            }
            
//            if (rs.next()) {
//                int v = rs.getInt(1);
//                System.out.println("RES:" + v);
//                if (v == 1) {
//                    System.out.println("Success");
//                } else {
//                    System.err.println("Fail - result: " + v);
//                }
//            } else {
//                System.err.println("Fail - empty result");
//            }
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
