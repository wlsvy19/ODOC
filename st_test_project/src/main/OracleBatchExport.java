package main;

import java.sql.*;
import java.io.*;

public class OracleBatchExport {
    private static final String DB_URL = "jdbc:oracle:thin:@//192.168.0.220:1521/gatcs"; // DB URL
    private static final String DB_USER = "gatcs"; // 사용자명
    private static final String DB_PASSWORD = "gatcs"; // 비밀번호
    private static final String FILE_PATH = "src/sqlfile/exportledger.sql"; // 출력할 SQL 파일 경로

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();  // 시작 시간 기록
        Connection conn = null;
        PreparedStatement stmt = null;
        BufferedWriter writer = null;
        int batchSize = 1000;  // 한 번에 가져올 데이터 건수
        int offset = 0;  // 데이터 시작 위치
        boolean hasMoreRecords = true;  // 데이터가 더 있는지 체크

        try {
            // DB 연결
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection successful!");

            // 파일 준비
            writer = new BufferedWriter(new FileWriter(FILE_PATH));

            // 쿼리 준비
            String query = "SELECT EX_ID, OBU_NO, HND_CAR_NO FROM (" +
                    "  SELECT A.EX_ID, A.OBU_NO, B.HND_CAR_NO, ROW_NUMBER() OVER (ORDER BY A.EX_ID) AS rownum " +
                    "  FROM ETCL_HANDLE A " +
                    "  JOIN PUBR_CARNODATA B ON A.PLZ_ID = B.PLZ_ID AND A.WRK_DATE = B.WRK_DATE " +
                    "  AND A.WRK_NO = B.WRK_NO AND A.HND_SNO = B.HND_SNO " +
                    "  WHERE A.WRK_DATE BETWEEN '20220101' AND '20241211' " +
                    "  AND B.HND_CAR_NO IS NOT NULL " +
                    "  AND A.EX_ID <> '0000000000000000') " +
                    "WHERE rownum > ? AND rownum <= ?";

            // 쿼리 실행 준비
            PreparedStatement selectStmt = conn.prepareStatement(query);

            // 데이터 처리
            int totalRecordsProcessed = 0;
            while (hasMoreRecords) {
                selectStmt.setInt(1, offset);  // 시작 위치
                selectStmt.setInt(2, offset + batchSize);  // 끝 위치

                ResultSet rs = selectStmt.executeQuery();  // 쿼리 실행
                int recordCount = 0;

                while (rs.next()) {
                    String exId = rs.getString("EX_ID");
                    String obuNo = rs.getString("OBU_NO");
                    String handCarNo = rs.getString("HND_CAR_NO");

                    // SQL INSERT 구문을 파일에 기록
                    writer.write(String.format("Insert into BASE_MATCHINGLEDGER (EX_ID, OBU_NO, HAND_CAR_NO) values ('%s', '%s', '%s');\n",
                            exId, obuNo, handCarNo != null ? handCarNo : "null"));
                    recordCount++;
                    totalRecordsProcessed++;
                }

                // 1000건을 처리했으면 offset을 갱신
                if (recordCount == batchSize) {
                    offset += batchSize;
                } else {
                    hasMoreRecords = false;  // 더 이상 데이터가 없으면 종료
                }

                // 진행 상황 콘솔에 출력
                System.out.println("Processed " + recordCount + " records, total processed: " + totalRecordsProcessed + ", offset: " + offset);

                // 파일에 기록을 강제로 flush
                writer.flush();
            }

            System.out.println("All records processed!");
            long endTime = System.currentTimeMillis();
            long elapsedTime = (endTime - startTime) / 1000;  // 경과 시간 (초 단위)
            System.out.println("Process completed in " + elapsedTime + " seconds.");

        } catch (SQLException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (conn != null) conn.close();
                if (writer != null) writer.close();
            } catch (SQLException | IOException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }
}
