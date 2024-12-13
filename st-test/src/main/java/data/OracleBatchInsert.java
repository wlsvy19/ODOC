package data;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.charset.Charset;

public class OracleBatchInsert {
    // Oracle DB 연결 정보
    private static final String DB_URL = "jdbc:oracle:thin:@10.66.1.10:1521:orcl";
    private static final String DB_USER = "gdtcs";
    private static final String DB_PASSWORD = "gdtcs2025!!";
    private static final String FILE_PATH = "src/sqlfile/ledger.sql"; // SQL 파일 경로
    private static final Charset FILE_CHARSET = Charset.forName("MS949"); // KO16MSWIN949에 해당하는 인코딩

    public static void main(String[] args) {
        String insertQuery = "INSERT INTO BASE_MATCHINGLEDGER (EX_ID, OBU_NO, HAND_CAR_NO) VALUES (?, ?, ?)";
        int batchSize = 1000; // 배치 크기
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        BufferedReader reader = null;

        try {
            // 1. 파일 존재 여부 확인
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                throw new IllegalArgumentException("SQL file not found: " + FILE_PATH);
            }

            // 2. DB 연결
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false); // 자동 커밋 비활성화

            // 파일 인코딩을 명시적으로 지정
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_CHARSET));

            pstmt = conn.prepareStatement(insertQuery);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Insert into")) {
                    // 데이터 파싱
                    String[] values = line
                            .substring(line.indexOf("values") + 7) // values 이후 부분 추출
                            .replace("(", "")
                            .replace(")", "")
                            .replace("'", "")
                            .split(",");

                    // HAND_CAR_NO 설정 시 세미콜론 제거
                    String handCarNo = values[2].trim().replaceAll(";$", ""); // 끝의 세미콜론 제거

                    pstmt.setString(1, values[0].trim()); // EX_ID
                    pstmt.setString(2, values[1].trim()); // OBU_NO
                    pstmt.setString(3, handCarNo.equalsIgnoreCase("null") ? null : handCarNo); // HAND_CAR_NO
                    pstmt.addBatch();

                    count++;
                    if (count % batchSize == 0) {
                        pstmt.executeBatch(); // 배치 실행
                        conn.commit(); // 트랜잭션 커밋
                        System.out.println("Processed " + count + " records...");
                    }
                }
            }

            // 남은 데이터 처리
            pstmt.executeBatch();
            conn.commit();
            System.out.println("All records processed: " + count);

        } catch (IllegalArgumentException e) {
            // 파일 없음 오류 처리
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            // DB 연결 또는 실행 오류
            System.err.println("Database error: " + e.getMessage());
            if (e.getNextException() != null) {
                System.err.println("Next exception: " + e.getNextException().getMessage());
            }
            e.printStackTrace();

            // 롤백 처리
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (reader != null) reader.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception closeEx) {
                System.err.println("Failed to close resources: " + closeEx.getMessage());
            }
        }
    }
}