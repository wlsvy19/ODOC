/**
 * @작성자 : 이진표
 * @작성일 : 2025. 04. 16. 수
 * 이 코드는 메모리 임계치 재시작 프로세스로 인해 사용 중단 됩니다.
 * 추후 사용될 수 있어 주석 처리 합니다.
 * 지우지 마세요.
 */

//package gdtcs.util;
//
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//@Component
//@Slf4j
//public class DBPoolMonitoring {
//
//    private static final Logger DB_CONNECTION_LOGGER = LogManager.getLogger("DB_CONNECTION_MONITORING");
//    private static final String SEPARATOR = "==========================================================================";
//
//    private static final String SERVICE_NAME = "springboot_gdtcs.service";
//    private final HikariDataSource hikariDataSource;
//
//    public DBPoolMonitoring(HikariDataSource hikariDataSource) {
//        this.hikariDataSource = hikariDataSource;
//    }
//
////    @Scheduled(initialDelay = 5000, fixedRate = 3000)
//    @Scheduled(fixedRate = 3000)
//    public void logHikariPoolStatus() {
//        if (hikariDataSource == null || hikariDataSource.getHikariPoolMXBean() == null) {
//            log.error("HikariDataSource 또는 HikariPoolMXBean이 초기화되지 않았습니다.");
//            log.info("적어도 백엔드 -> DB에 쿼리 하나를 날려야 HikariDataSourc 와 HikariPoolMXBean이 설정됩니다.");
//            return;
//        }
//        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
//        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
//
//        DB_CONNECTION_LOGGER.info(SEPARATOR);
//        DB_CONNECTION_LOGGER.info("HikariCP 활성 연결 수: " + "#" + activeConnections + "#");
//        DB_CONNECTION_LOGGER.info("HikariCP 대기 연결 수: " + "#" + idleConnections + "#");
//        if (activeConnections > 5) { // 커넥션 개수 8개 넘는 순간 백엔드 자동 재시작 실행
//            restartSystemdService("springboot_gdtcs.service");
//            throw new TCSException(ResponseCode.DB_CONN_FULL, "DB 커넥션이 FULL 상태입니다. 서버를 재시작 합니다.");
//        }
//        DB_CONNECTION_LOGGER.info(SEPARATOR);
//    }
//
//    private void restartSystemdService(String serviceName) {
//        if (isWindows()) {
//            DB_CONNECTION_LOGGER.warn("Windows 환경에서는 서비스 재시작 명령이 실행되지 않습니다.");
//            return;
//        }
//        try {
//            String command = "sudo systemctl restart " + serviceName;
//            DB_CONNECTION_LOGGER.info("GDTCS DB 커넥션 풀 에러로 인해 백엔드 재시작 명령 실행 : " + command);
//            Process process = Runtime.getRuntime().exec(command);
//        } catch (Exception e) {
//            log.error("GDTCS 백엔드 '{}' 재시작 도중 오류 발생: {}", serviceName, e.getMessage());
//            DB_CONNECTION_LOGGER.info("GDTCS 백엔드 '{}' 재시작 도중 오류 발생: : " + serviceName + " - " + e.getMessage());
//        } finally {
//            DB_CONNECTION_LOGGER.info(SEPARATOR);
//        }
//    }
//
//    // 운영체제(OS) 체크: 리눅스만 명령어 실행 됨
//    private boolean isWindows() {
//        return System.getProperty("os.name").toLowerCase().contains("win");
//    }
//}
