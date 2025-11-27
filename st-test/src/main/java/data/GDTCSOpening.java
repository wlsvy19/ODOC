package data;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GDTCSOpening {

    private static final String DB_URL = "jdbc:oracle:thin:@10.66.1.10:1521:orcl";
    private static final String DB_USER = "gdtcs";
    private static final String DB_PASSWORD = "gdtcs2025!!";

    private static final String QUERY = """
            SELECT *
            FROM (
                SELECT 
                    CNT.TOTAL_COUNT,
                    A.WORK_DATE,
                    A.WORK_NO,
                    A.HAND_DT,
                    A.HAND_SNO,
                    B.HAND_CAR_NO,
                    ROW_NUMBER() OVER (ORDER BY A.HAND_DT DESC) AS RN,
                    A.VLTN_CODE,
                    A.PASS_FARE,
                    A.PAY_FARE,
                    A.ECARD_TYPE,
                    A.ECARD_PAY_DIV,
                    A.ECARD_ATT
                FROM PRIM_ETCHAND A
                JOIN PRIM_HANDCARNO B
                    ON A.IC_CODE = B.IC_CODE
                    AND A.WORK_DATE = B.WORK_DATE
                    AND A.WORK_NO = B.WORK_NO
                    AND A.HAND_SNO = B.HAND_SNO
                JOIN (
                    SELECT COUNT(*) AS TOTAL_COUNT
                    FROM PRIM_ETCHAND A
                    JOIN PRIM_HANDCARNO B
                        ON A.IC_CODE = B.IC_CODE
                        AND A.WORK_DATE = B.WORK_DATE
                        AND A.WORK_NO = B.WORK_NO
                        AND A.HAND_SNO = B.HAND_SNO
                    WHERE A.WORK_DATE = ?
                ) CNT ON 1 = 1
                WHERE A.WORK_DATE = ?
            ) WHERE RN <= 30
            """;

    public static void main(String[] args) {
        String workDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {

                preparedStatement.setString(1, workDate);
                preparedStatement.setString(2, workDate);

                List<String> results = new ArrayList<>();

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Executing query at: " + new Timestamp(System.currentTimeMillis()));

                    while (resultSet.next()) {
                        int totalCount = resultSet.getInt("TOTAL_COUNT");

                        String workNo = resultSet.getString("WORK_NO");
                        String handDt = resultSet.getString("HAND_DT");
                        String handSno = resultSet.getString("HAND_SNO");
                        String handCarNo = resultSet.getString("HAND_CAR_NO");
                        String vltnCode = resultSet.getString("VLTN_CODE");

                        String passFare = resultSet.getString("PASS_FARE");
                        String payFare = resultSet.getString("PAY_FARE");

                        String ecardType = resultSet.getString("ECARD_TYPE");
                        String ecardPayDiv = resultSet.getString("ECARD_PAY_DIV");
                        String ecardAtt = resultSet.getString("ECARD_ATT");

                        String formattedHandDt = formatDate(handDt);

                        results.add(String.format("TOTAL_COUNT: %d, WORK_NO: %s, HAND_DT: %s, HAND_SNO: %s, HAND_CAR_NO: %s, VLTN_CODE: %s, PASS_FARE: %s, PAY_FARE: %s ,ECARD_TYPE: %s, ECARD_PAY_DIV: %s, ECARD_ATT: %s",
                                totalCount, workNo, formattedHandDt, handSno, handCarNo, vltnCode, passFare, payFare, ecardType, ecardPayDiv, ecardAtt));
                    }
                }
                Collections.reverse(results);

                for (String result : results) {
                    System.out.println(result);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private static String formatDate(String handDt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = inputFormat.parse(handDt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return handDt;
        }
    }
}
