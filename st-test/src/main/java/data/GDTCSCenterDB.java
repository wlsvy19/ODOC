package data;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GDTCSCenterDB {
    public static void main(String[] args) {
        // processRECIEVSTA_M();
        Map<String, Object> result = selectICRCVLOCK_M();
        System.out.println(result.get("message"));
        System.out.println(result.get("status"));
    }

    private static final String DB_URL = "jdbc:oracle:thin:@192.168.0.221:1521:HPS";
    private static final String DB_USER = "hps";
    private static final String DB_PASSWORD = "hps";

    private static <T> T executeDbOperation(java.util.function.Function<Statement, T> queryExecutor) {
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement()) {
            return queryExecutor.apply(statement);
        } catch (SQLException e) {
            throw new RuntimeException("센터 DB 연결에 실패했습니다. " + e.getMessage(), e);
        }
    }

    private static Connection createConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Properties props = new Properties();
            props.put("user", DB_USER);
            props.put("password", DB_PASSWORD);
            props.put("oracle.net.CONNECT_TIMEOUT", "1000"); // 연결 타임아웃 10초 설정
            props.put("oracle.net.READ_TIMEOUT", "20000"); // 읽기 타임아웃 20초 설정
            Connection connection = DriverManager.getConnection(DB_URL, props);
            System.out.println("센터 DB 연결 성공");
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패");
            throw new RuntimeException("JDBC 드라이버 로드 실패", e);
        } catch (SQLException e) {
            System.out.println("센터 DB 연결 실패");
            throw e;
        }
    }

    public static Map<String, Object> selectICRCVLOCK_M() {
        String icCode = "094";
        String calcDate = "20250211";
        return executeDbOperation(statement -> {
            String sql = String.format(
                    "SELECT D_CLOSE_YN FROM ICRCVLOCK_M WHERE IC_CODE = '%s' AND D_CLOSE_DATE = '%s'",
                    icCode.replace("'", "''"), calcDate.replace("'", "''")
            );

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                Map<String, Object> result = new HashMap<>();
                if (resultSet.next()) {
                    String dCloseYn = resultSet.getString("D_CLOSE_YN");
                    result.put("D_CLOSE_YN", dCloseYn);
                    result.put("status", 1);
                    result.put("message", "광안센터 DB 조회에 성공했습니다.");
                    //log.info("센터 DB 조회 성공: IC_CODE={}, CALC_DATE={}, D_CLOSE_YN={}", icCode, calcDate, dCloseYn);
                    //System.out.println("광안 센터 DB 조회 성공 - 정산날짜:" + calcDate + ", 락여부: " + dCloseYn);
                } else {
                    result.put("status", 2);
                    result.put("message", "해당 날짜에 대한 LOCK 관련 데이터가 없습니다.");
                    //log.warn("센터 DB 조회 결과 없음: IC_CODE={}, CALC_DATE={}", icCode, calcDate);
                    //String dCloseYn = resultSet.getString("D_CLOSE_YN");
                    //System.out.println("광안 센터 DB 조회 결과 없음 - 정산날짜:" + calcDate + ", 락여부: " + dCloseYn);
                }
                return result;
            } catch (SQLException e) {
                //log.error("센터 DB 테이블 ICRCVLOCK_M 조회 실패: {}", e.getMessage(), e);
                throw new RuntimeException("센터 DB 테이블 ICRCVLOCK_M 조회 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }

    public static Map<String, Object> processRECIEVSTA_M() {
        String icCode = "094";
        String calcDate = "20250204"; // 일마감 날짜

        // 현재 날짜 및 시간 계산
        String currentDateTime = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String recvDate = currentDateTime.substring(0, 8);   // 오늘 날짜 기준 yyyyMMdd
        String recvHHMISS = currentDateTime.substring(9);    // HHmmss

        return executeDbOperation(statement -> {
            String selectSql = String.format(
                    "SELECT MAX(SER_NO) AS MAX_SER_NO " +
                            "FROM RECIEVSTA_M " +
                            "WHERE TRAN_IC_CODE = '%s' AND TABLE_ID = '900' AND D_CLOSE_DATE = '%s'",
                    icCode.replace("'", "''"),
                    calcDate.replace("'", "''")
            );

            System.out.println("실제 수행된 SELECT SQL 쿼리: " + selectSql);

            try (ResultSet resultSet = statement.executeQuery(selectSql)) {
                Map<String, Object> result = new HashMap<>();
                int newSerNo = 1; // 기본값 1로 설정 (SER_NO가 없을 경우)

                if (resultSet.next()) {
                    // MAX_SER_NO 확인 및 null 처리
                    String maxSerNo = resultSet.getString("MAX_SER_NO");
                    if (maxSerNo != null) { // null이 아닐 때에만 파싱
                        newSerNo = Integer.parseInt(maxSerNo) + 1;
                    }
                }

                System.out.println("새로운 SER_NO 계산 결과: " + newSerNo);

                String insertSql = String.format(
                        "INSERT INTO RECIEVSTA_M (RECV_DATE, SER_NO, TABLE_ID, TRAN_IC_CODE, RECV_HHMISS, ERR_CODE, D_CLOSE_DATE, RETRAN_DESC, ERR_MESSAGE) " +
                                "VALUES ('%s', '%d', '900', '%s', '%s', 0, '%s', NULL, NULL)",
                        recvDate, newSerNo, icCode, recvHHMISS, calcDate
                );

                // 실제 INSERT SQL 출력
                System.out.println("실제 수행된 INSERT SQL 쿼리: " + insertSql);

                // INSERT 실행
                int rowsInserted = statement.executeUpdate(insertSql);
                if (rowsInserted > 0) {
                    System.out.println("RECIEVSTA_M 데이터 삽입 성공: " + rowsInserted + " 행");
                    result.put("status", 1);
                    result.put("message", "RECIEVSTA_M 데이터를 삽입했습니다.");
                    result.put("SER_NO", newSerNo); // 삽입된 SER_NO 값
                } else {
                    System.out.println("데이터 삽입 실패");
                    result.put("status", 2);
                    result.put("message", "RECIEVSTA_M 데이터 삽입에 실패했습니다.");
                }
                return result;

            } catch (SQLException e) {
                throw new RuntimeException("RECIEVSTA_M 처리 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }
}
