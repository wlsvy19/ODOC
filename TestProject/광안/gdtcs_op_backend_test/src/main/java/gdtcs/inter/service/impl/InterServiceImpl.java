package gdtcs.inter.service.impl;

import gdtcs.inter.mapper.InterMapper;
import gdtcs.inter.service.InterService;
import gdtcs.inter.util.CenterDB;
import gdtcs.inter.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("interService")
public class InterServiceImpl extends EgovAbstractServiceImpl implements InterService {

    @Resource(name = "interMapper")
    private InterMapper interMapper;

    @Value("${externalServerHost}")
    private String externalServerHost;

    @Value("${externalServerPort}")
    private int externalServerPort;

    @Override
    public Map<String, Object> getInter(Map<String, Object> param) throws Exception {
        return interMapper.selectInter(param);
    }

    @Override
    public Map<String, Object> getTaxi(Map<String, Object> param) throws Exception {
        return interMapper.selectTaxi(param);
    }

    @Override
    public Map<String, Object> getPreRegi(Map<String, Object> param) throws Exception {
        return interMapper.selectPreRegi(param);
    }

    @Override
    public Map<String, Object> getCenterLockYn(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        try {
            String icCode = (String) param.get("IC_CODE");
            String calcDate = (String) param.get("CALC_DATE");

            // 센터 DB 연결 상태 확인
            Map<String, Object> connectionStatus = CenterDB.checkConnection();

            // 센터 DB 연결 실패
            if (connectionStatus.get("status") != null && (int) connectionStatus.get("status") == -5) {
                result.put("status", -5);
                result.put("message", connectionStatus.get("message"));
                return result;
            }

            // 데이터 없음 -> 전송가능
            Map<String, Object> centerLockYn = CenterDB.selectICRCVLOCK_M(icCode, calcDate);
            if (centerLockYn.get("status") != null && (int) centerLockYn.get("status") == 2) {
                result.put("status", 2);
                result.put("message", centerLockYn.get("message"));
                result.put("D_CLOSE_YN", "NULL");
                return result;
            }
            // 데이터 있음
            result.put("status", centerLockYn.get("status"));
            result.put("message", centerLockYn.get("message"));
            result.put("D_CLOSE_YN", centerLockYn.get("D_CLOSE_YN"));
            return result;

        } catch (Exception e) {
            result.put("status", -1);
            result.put("message", e.getMessage());
            return result;
        }
    }

    @Override
    public Map<String, Object> getIsDayFin(Map<String, Object> param) throws Exception {
        return interMapper.selectIsDayFin(param);
    }

    @Override
    public Map<String, Object> requestInter(Map<String, Object> param) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            String icCode = (String) param.get("IC_CODE");
            String calcDate = (String) param.get("CALC_DATE");
            String fileNumber = "499";

            // 외부연계 서버로 전문전송
            result = Message.sendMessage(externalServerHost, externalServerPort, icCode, calcDate, fileNumber);

            char responseServerCode = result.get("response").toString().charAt(42);

            // 외부연계서버 응답코드: 성공
            if (responseServerCode == '0') {
                log.info("외부연계서버 응답코드: 성공");
                param.put("TRNRCP_RSLT", "1");
                // CALC_ECARDSTLTRNRCP 테이블 성공 이력 추가
                interMapper.insertInter(param);
                Map<String, Object> RECIEVSTA_M = CenterDB.processRECIEVSTA_M(icCode, calcDate);
                log.info("{}", RECIEVSTA_M.get("message"));

                // 센터 LOCK 여부 Insert or Update
                Map<String, Object> centerLockYn = CenterDB.selectICRCVLOCK_M(icCode, calcDate);
                if (centerLockYn.get("status") != null && (int) centerLockYn.get("status") == 2) { // 이력 미존재: Insert
                    Map<String, Object> isLockInsert = CenterDB.insertICRCVLOCK_M(icCode, calcDate);
                    log.info("센터 ICRCVLOCK_M 테이블 Insert 결과: {}", isLockInsert);
                } else if (centerLockYn.get("status") != null && (int) centerLockYn.get("status") == 1) { // 이력 존재: Update
                    Map<String, Object> isLockUpdate = CenterDB.updateICRCVLOCK_M(icCode, calcDate);
                    if (isLockUpdate.get("status") != null && (int) isLockUpdate.get("status") == 1) {
                        log.info("센터 ICRCVLOCK_M 테이블 업데이트 결과: {}", isLockUpdate);
                    }
                } else {
                    log.error("센터 DB 테이블 ICRCVLOCK_M 조회 실패: {}");
                }
            } else if (responseServerCode == '1') { // 외부연계서버 응답코드: 실패
                log.error("외부연계서버 응답코드: 실패");
                param.put("TRNRCP_RSLT", "2");
                // CALC_ECARDSTLTRNRCP 테이블 실패 이력 추가
                interMapper.insertInter(param);
            }

            return result;
        } catch (Exception e) {
            result.put("status", -1);
            result.put("message", result.get("message"));
            // 오류로 인한 실패 이력도 추가 (ex:DB에러 등)
            param.put("TRNRCP_RSLT", "2");
            interMapper.insertInter(param);
        }

        return result;
    }


    @Override
    public List<Map<String, Object>> getCenterInter(Map<String, Object> param) throws Exception {
        return interMapper.selectCenterInter(param);
    }

    @Override
    public List<Map<String, Object>> getFileDivNm(Map<String, Object> param) throws Exception {
        return interMapper.selectFileDivNm(param);
    }

    @Override
    public List<Map<String, Object>> transTaxiPre(Map<String, Object> param) throws Exception {
        interMapper.transTaxiPre(param);
        return (List<Map<String, Object>>) param.get("output");
    }

    @Override
    public void transTaxiPreTraffic(Map<String, Object> param) throws Exception {
        interMapper.transTaxiPreTraffic(param);
    }

    @Override
    public boolean checkIsHoliday(Map<String, Object> param) throws Exception {
        return interMapper.checkIsHoliday(param) > 0;

    }
}