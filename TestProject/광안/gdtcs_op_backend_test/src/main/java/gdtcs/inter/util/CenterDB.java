package gdtcs.inter.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@Slf4j
public class CenterDB {
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.0.221:1521:HPS";
    private static final String DB_USER = "hps";
    private static final String DB_PASSWORD = "hps";

    private static <T> T executeDbOperation(Function<Statement, T> queryExecutor) {
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement()) {
            return queryExecutor.apply(statement);
        } catch (SQLException e) {
            log.error("센터 DB 연결 실패: {}", e.getMessage(), e);
            throw new RuntimeException("센터 DB 연결에 실패했습니다. " + e.getMessage(), e);
        }
    }

    private static Connection createConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Properties props = new Properties();
            props.put("user", DB_USER);
            props.put("password", DB_PASSWORD);
            props.put("oracle.net.CONNECT_TIMEOUT", "1000"); // 연결 타임아웃 설정 (10초)
            props.put("oracle.net.READ_TIMEOUT", "20000"); // 읽기 타임아웃 설정 (20초)
            Connection connection = DriverManager.getConnection(DB_URL, props);
            log.info("센터 DB 연결 성공 @ {}", DB_URL);
            return connection;
        } catch (ClassNotFoundException e) {
            log.error("JDBC 드라이버 로드 실패: {}", e.getMessage());
            throw new RuntimeException("JDBC 드라이버 로드 실패", e);
        } catch (SQLException e) {
            log.error("센터 DB 연결 실패: {}", e.getMessage());
            throw e;
        }
    }

    public static Map<String, Object> checkConnection() {
        Map<String, Object> result = new HashMap<>();
        try (Connection connection = createConnection()) {
            if (connection != null && connection.isValid(5)) { // 연결 유효성 확인 (5초 타임아웃)
                result.put("status", 1);
                result.put("message", "센터 DB 연결 성공");
                log.info("센터 DB 연결 상태 확인 성공: {}", DB_URL);
            } else {
                result.put("status", -5);
                result.put("message", "센터 DB 연결 실패: 연결이 유효하지 않습니다.");
                log.warn("센터 DB 연결 상태 확인 실패: 유효하지 않은 연결");
            }
        } catch (SQLException e) {
            result.put("status", -5);
            result.put("message", "센터 DB 연결에 실패했습니다." + e.getMessage());
            log.error("센터 DB 연결에 실패했습니다. {}", e.getMessage(), e);
        }
        return result;
    }

    // Stpe1) 정산요청 시 센터DB ICRCVLOCK_M 테이블 체크
    public static Map<String, Object> selectICRCVLOCK_M(String icCode, String calcDate) {
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
                    result.put("lock", dCloseYn);
                    log.info("센터 DB 조회 성공, 락여부:Y - IC_CODE={}, CALC_DATE={}, D_CLOSE_YN={}", icCode, calcDate, dCloseYn);
                } else {
                    result.put("status", 2);
                    result.put("message", "해당 날짜에 대한 LOCK 관련 데이터가 없습니다.");
                    result.put("lock", "N");
                    log.warn("센터 DB 조회 결과 없음: IC_CODE={}, CALC_DATE={}", icCode, calcDate);
                }
                return result;
            } catch (SQLException e) {
                log.error("센터 DB 테이블 ICRCVLOCK_M 조회 실패: {}", e.getMessage(), e);
                throw new RuntimeException("센터 DB 테이블 ICRCVLOCK_M 조회 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }

    // Stpe2-1) 센터DB ICRCVLOCK_M 데이터 없으면 INSERT 처리
    public static Map<String, Object> insertICRCVLOCK_M(String icCode, String calcDate) {
        Map<String, Object> result = new HashMap<>();
        return executeDbOperation(statement -> {
            String sql = String.format(
                    "INSERT INTO ICRCVLOCK_M (IC_CODE, D_CLOSE_DATE, D_CLOSE_YN) VALUES ('%s', '%s', 'Y')",
                    icCode.replace("'", "''"), calcDate.replace("'", "''")
            );
            try {
                int insertedRows = statement.executeUpdate(sql);
                if (insertedRows > 0) {
                    result.put("status", 1);
                    result.put("message", "센터 DB 테이블 ICRCVLOCK_M Insert 성공");
                    log.info("센터 DB 테이블 ICRCVLOCK_M Insert 성공: {}행 수정됨", insertedRows);
                    log.info("수행된 SQL: {}", sql);
                    return result;
                } else {
                    result.put("status", -1);
                    result.put("message", "센터 DB 테이블 ICRCVLOCK_M Insert 실패: 조건과 일치하는 데이터 없음.");
                    log.warn("센터 DB 테이블 ICRCVLOCK_M Insert 실패: 조건과 일치하는 데이터 없음. IC_CODE={}, CALC_DATE={}", icCode, calcDate);
                    return result;
                }
            } catch (SQLException e) {
                log.error("센터 DB 테이블 ICRCVLOCK_M Insert 실패: {}", e.getMessage(), e);
                throw new RuntimeException("센터 DB 테이블 ICRCVLOCK_M 업데이트 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }

    // Stpe2-2) 센터DB ICRCVLOCK_M 데이터 있으면 UPDATE 처리(센터에 연락해서 락 풀어달라고 한 경우)
    public static Map<String, Object> updateICRCVLOCK_M(String icCode, String calcDate) {
        Map<String, Object> result = new HashMap<>();
        return executeDbOperation(statement -> {
            String sql = String.format(
                    "UPDATE ICRCVLOCK_M SET D_CLOSE_YN = 'Y' WHERE IC_CODE = '%s' AND D_CLOSE_DATE = '%s'",
                    icCode.replace("'", "''"), calcDate.replace("'", "''")
            );
            try {
                int updatedRows = statement.executeUpdate(sql);
                if (updatedRows > 0) {

                    result.put("status", 1);
                    result.put("message", "센터 DB 테이블 ICRCVLOCK_M 업데이트 성공");
                    log.info("센터 DB 테이블 ICRCVLOCK_M 업데이트 성공: {}행 수정됨", updatedRows);
                    return result;
                } else {
                    result.put("status", -1);
                    result.put("message", "센터 DB 테이블 ICRCVLOCK_M 업데이트 실패: 조건과 일치하는 데이터 없음.");
                    log.warn("센터 DB 테이블 ICRCVLOCK_M 업데이트 실패: 조건과 일치하는 데이터 없음. IC_CODE={}, CALC_DATE={}", icCode, calcDate);
                    return result;
                }
            } catch (SQLException e) {
                log.error("센터 DB 테이블 ICRCVLOCK_M 업데이트 실패: {}", e.getMessage(), e);
                throw new RuntimeException("센터 DB 테이블 ICRCVLOCK_M 업데이트 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }

    // Stpe3) 센터DB RECIEVSTA_M 이력 추가
    public static Map<String, Object> processRECIEVSTA_M(String icCode, String calcDate) {
        // 송신시간: 현재 날짜 및 시간
        String currentDateTime = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String recvDate = currentDateTime.substring(0, 8);   // 오늘날짜 기준 yyyyMMdd
        String recvHHMISS = currentDateTime.substring(9);    // HHmmss
        return executeDbOperation(statement -> {
            String selectSql = String.format(
                    "SELECT MAX(SER_NO) AS MAX_SER_NO " +
                            "FROM RECIEVSTA_M " +
                            "WHERE TRAN_IC_CODE = '%s' AND TABLE_ID = '900' AND D_CLOSE_DATE = '%s'",
                    icCode.replace("'", "''"),
                    calcDate.replace("'", "''")
            );

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
                String insertSql = String.format(
                        "INSERT INTO RECIEVSTA_M (RECV_DATE, SER_NO, TABLE_ID, TRAN_IC_CODE, RECV_HHMISS, ERR_CODE, D_CLOSE_DATE, RETRAN_DESC, ERR_MESSAGE) " +
                                "VALUES ('%s', '%d', '900', '%s', '%s', 0, '%s', NULL, NULL)",
                        recvDate, newSerNo, icCode, recvHHMISS, calcDate
                );
                int rowsInserted = statement.executeUpdate(insertSql);
                if (rowsInserted > 0) {
                    result.put("message", "RECIEVSTA_M 데이터를 삽입했습니다.");
                } else {
                    result.put("message", "RECIEVSTA_M 데이터 삽입에 실패했습니다.");
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException("RECIEVSTA_M 처리 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }
}