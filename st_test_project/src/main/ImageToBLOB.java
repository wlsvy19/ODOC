package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageToBLOB {
    public static void main(String[] args) {
        try {
            Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String url = "jdbc:oracle:thin:@10.100.10.145:1521/orcl";
        String user = "gdtcs";
        String password = "gdtcs2024!!!";
        String filePath = "C:\\eGovFrameDev-4.1.0-64bit\\workspace\\test_project\\google.png";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO BASE_REPORTIMG (IC_CODE, RPT_APP_CODE, IMG_DATA, IMG_PTH) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "094"); // IC_CODE
                pstmt.setInt(2, 8); // RPT_APP_CODE

                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                pstmt.setBinaryStream(3, fis, file.length());

                pstmt.setString(4, "google.png"); // IMG_PTH
                pstmt.executeUpdate();

                System.out.println("이미지 저장 성공");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
