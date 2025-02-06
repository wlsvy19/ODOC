package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GDTCSCount {

    private static final String DB_URL = "jdbc:oracle:thin:@10.66.1.10:1521:orcl";
    private static final String DB_USER = "gdtcs";
    private static final String DB_PASSWORD = "gdtcs2025!!";

    private static final String QUERY = """
            SELECT 
                COUNT(1)
            FROM PRIM_ETCHAND A
            JOIN PRIM_HANDCARNO B
            ON A.IC_CODE = B.IC_CODE
            AND A.WORK_DATE = B.WORK_DATE
            AND A.WORK_NO = B.WORK_NO
            AND A.HAND_SNO = B.HAND_SNO
            WHERE A.WORK_DATE = ?
            """;

    public static void main(String[] args) {

        String workDate = "20250201"; // A.WORK_DATE 값


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


        scheduler.scheduleAtFixedRate(() -> {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {


                preparedStatement.setString(1, workDate);


                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        int count = resultSet.getInt(1); // count(1) 결과 가져오기
                        System.out.println("Count: " + count);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS); // 5초마다 실행되도록 작업 등록
    }
}
