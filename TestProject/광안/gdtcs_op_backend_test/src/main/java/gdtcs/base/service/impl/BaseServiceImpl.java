package gdtcs.base.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.math.BigDecimal;

import gdtcs.base.mapper.BaseMapper;
import gdtcs.base.service.BaseService;
import gdtcs.base.util.DateUtil;
import gdtcs.base.vo.PACKET_1000_0010;
import gdtcs.base.vo.PACKET_1000_0020;
import gdtcs.common.service.ServerInfoService;
import gdtcs.util.SecurityUtil;

@Service("baseService")
public class BaseServiceImpl extends EgovAbstractServiceImpl implements BaseService{
    protected Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private ServerInfoService serverInfo;

    @Override
    public List<Map<String, Object>> getMenuInfo(Map<String, Object> param) throws Exception {
        return baseMapper.selectMenuInfo(param);
    }

    @Override
    public List<Map<String, Object>> getSystemCodeList(Map param) throws Exception{
        return baseMapper.selectSystemCodeList(param);
    }

    @Override
    public int setSystemLargeCode(Map param) throws Exception{
        return baseMapper.updateSystemLargeCode(param);
    }

    @Override
    public int addSystemLargeCode(Map param) throws Exception{
        if(isLargeCodeExist(param)){
            return -2;
        }

        return baseMapper.insertSystemLargeCode(param);
    }

    @Override
    public int delSystemLargeCode(Map param) throws Exception{
        if(hasLargeCodeChild(param)){
            return -2;
        }

        return baseMapper.deleteSystemLargeCode(param);
    }

    @Override
    public List<Map<String, Object>> getSystemSmallCode(Map param) throws Exception{
        return baseMapper.selectSystemSmallCode(param);
    }

    @Override
    public int setSystemSmallCode(Map param) throws Exception{
        return baseMapper.updateSystemSmallCode(param);
    }

    @Override
    public int addSystemSmallCode(Map param) throws Exception{
        if(isSmallCodeExist(param)){
            return -2;
        }

        return baseMapper.insertSystemSmallCode(param);
    }

    @Override
    public int delSystemSmallCode(Map param) throws Exception{
        return baseMapper.deleteSystemSmallCode(param);
    }

    @Override
    public boolean isLargeCodeExist(Map param) throws Exception{
        int row_count = baseMapper.countLargeCode(param);
        if(row_count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isSmallCodeExist(Map param){
        int row_count = baseMapper.countSmallCode(param);
        if(row_count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasLargeCodeChild(Map param){
        int row_count = baseMapper.countLargeCodeChild(param);
        if(row_count > 0){
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @Method Name	: requestRevision
     * @Method 설명	: 개정 지시
     * @작성자 		: 박형철
     * @작성일 		: 2024. 7. 28.
     *
     * @param Map param
     * @return int
     * @throws IOException
     *
     */
    @Override
    public int requestRevision(Map param) throws IOException {
        String IC_CODE = param.get("IC_CODE").toString();

        if(serverInfo.getSERVER_INTER_LANE() == null
                || MapUtils.isEmpty(serverInfo.getSERVER_INTER_LANE())
                || !serverInfo.getSERVER_INTER_LANE().containsKey(IC_CODE)){
            serverInfo.getServerInfo(IC_CODE);
        }

        String LANE_SERVER_IP_ADDRESS = serverInfo.getSERVER_INTER_LANE().get(IC_CODE).get("SVR_ADDR").toString();
        int LANE_SERVER_PORT = Integer.parseInt(serverInfo.getSERVER_INTER_LANE().get(IC_CODE).get("SVR_PORT").toString());

        log.debug("LANE_SERVER_IP_ADDRESS:"+LANE_SERVER_IP_ADDRESS);
        log.debug("LANE_SERVER_PORT:"+LANE_SERVER_PORT);

        PACKET_1000_0010 packetSend = new PACKET_1000_0010(
                param.get("PACKET_SYSTEM_CODE_SEND").toString(),
                param.get("PACKET_SYSTEM_CODE_RECV").toString(),
                param.get("PACKET_OP_DIV").toString(),
                param.get("PACKET_WORK_DIV").toString(),
                param.get("PACKET_SEND_DT").toString(),
                param.get("PACKET_REPLY_CODE").toString(),
                param.get("PACKET_REVISION_DATA").toString()
        );

        log.info("PACKET_SYSTEM_CODE_SEND: " + param.get("PACKET_SYSTEM_CODE_SEND").toString());
        log.info("PACKET_SYSTEM_CODE_RECV: " + param.get("PACKET_SYSTEM_CODE_RECV").toString());
        log.info("PACKET_OP_DIV: " + param.get("PACKET_OP_DIV").toString());
        log.info("PACKET_SEND_DT: " + param.get("PACKET_SEND_DT").toString());
        log.info("PACKET_REVISION_DATA: " + param.get("PACKET_REVISION_DATA").toString());

        Socket socket = null;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;
        BufferedReader bufferedReader = null;

        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(LANE_SERVER_IP_ADDRESS , LANE_SERVER_PORT);
            socket.connect(socketAddress);

            if(!socket.isConnected()) {
                return -2;
            }

            // 데이터 전송
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.write(packetSend.getData());
            //dataOutputStream.flush();

            // 데이터 수신
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PACKET_1000_0020 packetRecv = new PACKET_1000_0020();
            for(int i=0; i<packetRecv.getData().length; i++) {
                packetRecv.getData()[i] = (byte) bufferedReader.read();
                //System.out.printf("%d ", packetRecv.getData()[i]);
            }

            // 결과처리
            StringBuffer stringBuffer = new StringBuffer();
            byte[] tmp = packetRecv.getREPLY_CODE();
            for(byte c : tmp){
                stringBuffer.append((char)c);
            }
            String replyCode = stringBuffer.toString();
            log.info("replyCode: " + replyCode);
            if(replyCode.equals("0000")){
                log.info("개정이 정상적으로 수행되었습니다.");
            } else if(replyCode.equals("0001")){
                log.info("개정 중에 오류가 발생하였습니다. (차로통신<->통신서버)");
                return -1;
            } else {
                log.info("정의되지 않은 응답 코드가 수신되었습니다.");
                return -1;
            }

            // 개정 결과 반영
            byte[] data = packetSend.getRevisionData();
            Map mapHist = new HashMap<>();
            mapHist.put("REV_DIV", "1");
            mapHist.put("IC_CODE", param.get("IC_CODE").toString());
            if(data[0] == '1'){
                mapHist.put("REV_NO_CODE", "01");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[1] == '1'){
                mapHist.put("REV_NO_CODE", "02");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[2] == '1'){
                mapHist.put("REV_NO_CODE", "03");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[3] == '1'){
                mapHist.put("REV_NO_CODE", "04");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[4] == '1'){
                mapHist.put("REV_NO_CODE", "05");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[5] == '1'){
                mapHist.put("REV_NO_CODE", "06");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[6] == '1'){
                mapHist.put("REV_NO_CODE", "07");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[7] == '1'){
                mapHist.put("REV_NO_CODE", "08");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[8] == '1'){
                mapHist.put("REV_NO_CODE", "09");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[9] == '1'){
                mapHist.put("REV_NO_CODE", "10");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[10] == '1'){
                mapHist.put("REV_NO_CODE", "11");
                setIcRevisionHistoryResult(mapHist);
            }
            if(data[11] == '1'){
                mapHist.put("REV_NO_CODE", "12");
                setIcRevisionHistoryResult(mapHist);
            }

            //socket.shutdownOutput();
            return 1;
        } catch (Exception e) {
            if(e.getMessage().contains("Socket is closed")) {
                return -2;
            } else if(e.getMessage().contains("Connection refused")) {
                return -2;
            } else if(e.getMessage().contains("Connection timed out")) {
                return -2;
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                    log.info("#####개정지시 - BufferedReader Close#####");
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                    log.info("#####개정지시 - DataOutputStream Close#####");
                }
                if (outputStream != null) {
                    outputStream.close();
                    log.info("#####개정지시 - OutputStream Close#####");
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    log.info("#####개정지시 - Socket Close#####");
                }
            } catch (IOException e) {
                log.error("#####개정지시 - 리소스를 닫는 중 오류 발생: ", e);
            }
        }
    }

    /**
     *
     * @Method Name	: setIcRevisionHistoryResult
     * @Method 설명	: 영업소 개정 기록 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 7. 28.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionHistoryResult(Map map)
    {
        return baseMapper.updateIcRevisionHistoryResult(map);
    }

    /**
     *
     * @Method Name	: getIcInformation
     * @Method 설명	: 영업소 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 8. 1.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getIcInformation(Map param) throws Exception{
        return baseMapper.selectIcInformation(param);
    }

    /**
     *
     * @Method Name	: getRevisionHistory
     * @Method 설명	: 개정 기록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 8. 1.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getRevisionHistory(Map param) throws Exception{
        return baseMapper.selectRevisionHistory(param);
    }

    /**
     *
     * @Method Name	: getLaneRevisionStatus
     * @Method 설명	: 차로별 개정 현황을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 8. 1.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getLaneRevisionStatus(Map param) throws Exception{
        return baseMapper.selectLaneRevisionStatus(param);
    }

    /**
     *
     * @Method Name	: getIcRevision
     * @Method 설명	: 영업소 개정 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getIcRevision(Map param) throws Exception
    {
        return baseMapper.selectIcRevision(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionBase
     * @Method 설명	: 기초정보 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionBase(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionBase(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionWorker
     * @Method 설명	: 근무자 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionWorker(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionWorker(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionFare
     * @Method 설명	: 요금 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionFare(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionFare(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlDiscount
     * @Method 설명	: 무효할인면제 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionBlDiscount(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionBlDiscount(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionHoliday
     * @Method 설명	: 휴일일자 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionHoliday(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionHoliday(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionCommute
     * @Method 설명	: 출퇴근할인 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionCommute(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionCommute(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlEcard
     * @Method 설명	: 무효전자카드 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionBlEcard(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionBlEcard(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionContinuousDiscount
     * @Method 설명	: 연속통행할인 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionContinuousDiscount(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionContinuousDiscount(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlTcard
     * @Method 설명	: 무효교통카드 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionBlTcard(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionBlTcard(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionHourDiscount
     * @Method 설명	: 시간별할인 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionHourDiscount(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionHourDiscount(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlObu
     * @Method 설명	: 무효 OBU 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionBlObu(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionBlObu(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionHolidayException
     * @Method 설명	: 명절(특정일) 면제 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionHolidayException(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionHolidayException(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionPlEcard
     * @Method 설명	: 전자카드면제 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionPlEcard(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionPlEcard(param);
    }

    /**
     *
     * @Method Name	: setIcRevisionDiffFare
     * @Method 설명	: 차등요금제 개정 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcRevisionDiffFare(Map param) throws Exception
    {
        return baseMapper.updateIcRevisionDiffFare(param);
    }

    /**
     *
     * @Method Name	: insertIcRevisionHistory
     * @Method 설명	: 영업소 개정 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addIcRevisionHistory(Map param) throws Exception
    {
        return baseMapper.insertIcRevisionHistory(param);
    }

    /**
     *
     * @Method Name	: getLaneList
     * @Method 설명	: 차로 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 06.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getLaneList(Map param) throws Exception{
        return baseMapper.selectLaneList(param);
    }

    /**
     *
     * @Method Name	: setLane
     * @Method 설명	: 차로 정보를 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 06.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setLane(Map param) throws Exception
    {
        return baseMapper.updateLane(param);
    }

    /**
     *
     * @Method Name	: getFareInfo
     * @Method 설명	: 요금 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 07.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getFareInfo(Map param) throws Exception{
        return baseMapper.selectFareInfo(param);
    }

    /**
     *
     * @Method Name	: setFareInfo
     * @Method 설명	: 요금 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 07.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setFareInfo(Map param) throws Exception
    {
        return baseMapper.updateFareInfo(param);
    }

    /**
     *
     * @Method Name	: addFareInfoHistory
     * @Method 설명	: 요금 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addFareInfoHistory(Map param) throws Exception
    {
        return baseMapper.insertFareInfoHistory(param);
    }

    /**
     *
     * @Method Name	: getDiffFare
     * @Method 설명	: 차등 요금 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getDiffFare(Map param) throws Exception{
        return baseMapper.selectDiffFare(param);
    }

    /**
     *
     * @Method Name	: setDiffFare
     * @Method 설명	: 차등 요금 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setDiffFare(Map param) throws Exception
    {
        return baseMapper.updateDiffFare(param);
    }

    /**
     *
     * @Method Name	: addDiffFareHistory
     * @Method 설명	: 차등 요금 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addDiffFareHistory(Map param) throws Exception
    {
        return baseMapper.insertDiffFareHistory(param);
    }

    /**
     *
     * @Method Name	: getWorkerList
     * @Method 설명	: 근무자 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 11.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getWorkerList(Map param) throws Exception{
        return baseMapper.selectWorkerList(param);
    }

    /**
     *
     * @Method Name	: getAvailableWorkerNo
     * @Method 설명	: 사용가능한 WORKER_NO 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getAvailableWorkerNo(Map param) throws Exception{
        return baseMapper.selectAvailableWorkerNo(param);
    }

    /**
     *
     * @Method Name	: addWorkerInfo
     * @Method 설명	: 근무자 정보를 추가한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addWorkerInfo(Map param) throws Exception
    {
        return baseMapper.insertWorkerInfo(param);
    }

    /**
     *
     * @Method Name	: setWorkerPassword
     * @Method 설명	: 근무자 비밀번호를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setWorkerPassword(Map param) throws Exception
    {
        param.put("OLD_PWD_ENC", SecurityUtil.encryptSHA256(param.get("OLD_PWD").toString()));
        param.put("NEW_PWD_ENC", SecurityUtil.encryptSHA256(param.get("NEW_PWD").toString()));

        if(!param.get("OLD_PWD").toString().equals("LbdLCNYQHfSHgfw9281vKYAJek865MMnBzZ12NitV216OdWSq3nhskbSE0JDpa1u")){
            // 회원 관리자가 아니면 비밀번호 유효성 검증을 수행한다.
            int cnt = baseMapper.isMatchPassword(param);
            log.debug("cnt: "+cnt);
            if(cnt != 1){
                return -2;
            }
        }
        int result = baseMapper.updateWorkerPassword(param);
        if(result != 1){
            return -3;
        }
        return result;
    }

    /**
     *
     * @Method Name	: setWorkerInfo
     * @Method 설명	: 근무자 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setWorkerInfo(Map param) throws Exception
    {
        return baseMapper.updateWorkerInfo(param);
    }

    /**
     *
     * @Method Name	: setWorkerResign
     * @Method 설명	: 근무자 퇴사 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setWorkerResign(Map param) throws Exception
    {
        return baseMapper.updateWorkerResign(param);
    }

    /**
     *
     * @Method Name	: setWorkerProfileImage
     * @Method 설명	: 근무자 프로필 이미지를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setWorkerProfileImage(Map param) throws Exception
    {
        return baseMapper.updateWorkerProfileImage(param);
    }

    /**
     *
     * @Method Name	: removeWorker
     * @Method 설명	: 근무자 정보를 삭제한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int removeWorker(Map param) throws Exception
    {
        return baseMapper.deleteWorker(param);
    }

    /**
     *
     * @Method Name	: addIcInformationHistory
     * @Method 설명	: 요금 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 11.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addIcInformationHistory(Map param) throws Exception
    {
        return baseMapper.insertIcInformationHistory(param);
    }

    /**
     *
     * @Method Name	: setIcInformation
     * @Method 설명	: 영업소 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 11.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setIcInformation(Map param) throws Exception
    {
        return baseMapper.updateIcInformation(param);
    }

    /**
     *
     * @Method Name	: getProgramList
     * @Method 설명	: 프로그램 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getProgramList(Map param) throws Exception{
        return baseMapper.selectProgramList(param);
    }

    /**
     *
     * @Method Name	: setProgram
     * @Method 설명	: 프로그램 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setProgram(Map param) throws Exception
    {
        return baseMapper.updateProgram(param);
    }

    /**
     *
     * @Method Name	: getReportImageInformation
     * @Method 설명	: 보고서 이미지 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getReportImageInformation(Map param) throws Exception{
        return baseMapper.selectReportImageInformation(param);
    }

    /**
     *
     * @Method Name	: setReportImageInformation
     * @Method 설명	: 보고서 이미지 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setReportImageInformation(Map param) throws Exception
    {
        return baseMapper.mergeReportImageInformation(param);
    }

    /**
     *
     * @Method Name	: getContinuousPassIcList
     * @Method 설명	: 연속통행할인 영업소 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getContinuousPassIcList(Map param) throws Exception{
        return baseMapper.selectContinuousPassIcList(param);
    }

    /**
     *
     * @Method Name	: getEcardDiscountCarType
     * @Method 설명	: 전자카드 차종별 할인율을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getEcardDiscountCarType(Map param) throws Exception{
        return baseMapper.selectEcardDiscountCarType(param);
    }

    /**
     *
     * @Method Name	: addEcardDiscountCarTypeHistory
     * @Method 설명	: 전자카드 차종별 할인율 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addEcardDiscountCarTypeHistory(Map param) throws Exception
    {
        return baseMapper.insertEcardDiscountCarTypeHistory(param);
    }

    /**
     *
     * @Method Name	: setEcardDiscountCarType
     * @Method 설명	: 전자카드 차종별 할인율을 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setEcardDiscountCarType(Map param) throws Exception
    {
        return baseMapper.updateEcardDiscountCarType(param);
    }

    /**
     *
     * @Method Name	: getEcardDiscount
     * @Method 설명	: 전자카드 선/후불별 할인율을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getEcardDiscount(Map param) throws Exception{
        return baseMapper.selectEcardDiscount(param);
    }

    /**
     *
     * @Method Name	: setEcardDiscount
     * @Method 설명	: 전자카드 선/후불별 할인율을 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setEcardDiscount(Map param) throws Exception
    {
        return baseMapper.updateEcardDiscount(param);
    }

    /**
     *
     * @Method Name	: getHourDiscount
     * @Method 설명	: 시간대별 할인율을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getHourDiscount(Map param) throws Exception{
        return baseMapper.selectHourDiscount(param);
    }

    /**
     *
     * @Method Name	: addHourDiscountHistory
     * @Method 설명	: 시간대별 할인율 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addHourDiscountHistory(Map param) throws Exception
    {
        return baseMapper.insertHourDiscountHistory(param);
    }

    /**
     *
     * @Method Name	: setHourDiscount
     * @Method 설명	: 시간대별 할인율을 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setHourDiscount(Map param) throws Exception
    {
        return baseMapper.updateHourDiscount(param);
    }

    /**
     *
     * @Method Name	: getDiscountExtra
     * @Method 설명	: 할인/할증 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getDiscountExtra(Map param) throws Exception{
        return baseMapper.selectDiscountExtra(param);
    }

    /**
     *
     * @Method Name	: addDiscountExtraHistory
     * @Method 설명	: 할인/할증 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addDiscountExtraHistory(Map param) throws Exception
    {
        return baseMapper.insertDiscountExtraHistory(param);
    }

    /**
     *
     * @Method Name	: setDiscountExtra
     * @Method 설명	: 할인/할증 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setDiscountExtra(Map param) throws Exception
    {
        return baseMapper.updateDiscountExtra(param);
    }

    /**
     *
     * @Method Name	: getChargeExtra
     * @Method 설명	: 충전 할증률 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getChargeExtra(Map param) throws Exception{
        return baseMapper.selectChargeExtra(param);
    }

    /**
     *
     * @Method Name	: addChargeExtraHistory
     * @Method 설명	: 충전 할증률 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addChargeExtraHistory(Map param) throws Exception
    {
        return baseMapper.insertChargeExtraHistory(param);
    }

    /**
     *
     * @Method Name	: setChargeExtra
     * @Method 설명	: 충전 할증률 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setChargeExtra(Map param) throws Exception
    {
        return baseMapper.updateChargeExtra(param);
    }

    /**
     *
     * @Method Name	: getEcardChargeLimit
     * @Method 설명	: 전자카드 최대 충전 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getEcardChargeLimit(Map param) throws Exception{
        return baseMapper.selectEcardChargeLimit(param);
    }

    /**
     *
     * @Method Name	: setEcardChargeLimit
     * @Method 설명	: 전자카드 최대 충전 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setEcardChargeLimit(Map param) throws Exception
    {
        return baseMapper.updateEcardChargeLimit(param);
    }

    /**
     *
     * @Method Name	: getObuDiscount
     * @Method 설명	: 충전 할증률 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getObuDiscount(Map param) throws Exception{
        return baseMapper.selectObuDiscount(param);
    }

    /**
     *
     * @Method Name	: addObuDiscountHistory
     * @Method 설명	: 충전 할증률 정보 수정 내역을 기록한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int addObuDiscountHistory(Map param) throws Exception
    {
        return baseMapper.insertObuDiscountHistory(param);
    }

    /**
     *
     * @Method Name	: setObuDiscount
     * @Method 설명	: 충전 할증률 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setObuDiscount(Map param) throws Exception
    {
        return baseMapper.updateObuDiscount(param);
    }

    /**
     *
     * @Method Name	: getContinuousDiscount
     * @Method 설명	: 연속통행 할인율 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 20.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getContinuousDiscount(Map param) throws Exception{
        return baseMapper.selectContinuousDiscount(param);
    }

    /**
     *
     * @Method Name	: getBaseDateForCalendar
     * @Method 설명	: 캘린더 컴포넌트용으로 일자 정보를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getBaseDateForCalendar(Map param) throws Exception{
        return baseMapper.selectBaseDateForCalendar(param);
    }

    /**
     *
     * @Method Name	: getSaveTermDate
     * @Method 설명	:
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getSaveTermDate(Map param) throws Exception{
        return baseMapper.selectSaveTermDate(param);
    }

    /**
     *
     * @Method Name	: getBaseDateInfo
     * @Method 설명	:
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getBaseDateInfo(Map param) throws Exception{
        return baseMapper.selectBaseDateInfo(param);
    }

    /**
     *
     * @Method Name	: getYearToDate
     * @Method 설명	:
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getYearToDate(Map param) throws Exception{
        return baseMapper.selectYearToDate(param);
    }

    /**
     *
     * @Method Name	: saveBaseDate
     * @Method 설명	:
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int saveBaseDate(Map param) throws Exception
    {
        int result = baseMapper.updateBaseDate(param);

        if(param.get("workDateDiv").toString().equals("4")){
            result = baseMapper.updateBaseHolyDay(param);
        }else{
            result = baseMapper.deleteBaseHolyDay(param);
        }

        return result;
    }

    /**
     *
     * @Method Name	: removeBaseDate
     * @Method 설명	:
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int removeBaseDate(Map param) throws Exception
    {
        int result = -1;

        if(param.get("workDateDiv").toString().equals("4")){
            result = baseMapper.deleteBaseHolyDay(param);
        }
        result = baseMapper.deleteBaseDate(param);

        return result;
    }

    /**
     *
     * @Method Name	: createDateList
     * @Method 설명	: 특정 연도의 일자 목록을 생성한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int createDateList(Map param) throws Exception
    {
        List<Map<String,Object>> dateList = getYearToDate(param);

        for(Map<String,Object> date : dateList){
            date.put("luna_date", DateUtil.toLunar(date.get("dayDate").toString()).get("day").toString());
            date.put("ic_code", param.get("IC_CODE"));
            saveBaseDate(date);
        }

        return 1;
    }

    /**
     *
     * @Method Name	: getDateList
     * @Method 설명	: 일자 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 23.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getDateList(Map param) throws Exception{
        return baseMapper.selectDateList(param);
    }

    /**
     *
     * @Method Name	: setDate
     * @Method 설명	: 일자 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 23.
     *
     * @param Map param
     * @return int
     * @throws Exception
     *
     */
    @Override
    public int setDate(Map param) throws Exception
    {
        log.info("IC_CODE: " + param.get("IC_CODE").toString());
        log.info("DAY_DATE: " + param.get("DAY_DATE").toString());
        log.info("WORK_DATE_DIV: " + param.get("WORK_DATE_DIV").toString());
        switch(param.get("WORK_DATE_DIV").toString()) {
            case "1":
                log.info("특정일 면제 정보 삭제");
                baseMapper.deleteHolyday(param);
                break;
            case "2":
                log.info("특정일 면제 정보 삭제");
                baseMapper.deleteHolyday(param);
                log.info(param.toString());
                break;
            case "4":
                log.info("특정일 면제 정보 생성");
                baseMapper.mergeHolyday(param);
                break;
            default:
                log.info("잘못된 일자 구분 값이 들어왔습니다.");
                break;
        }

        return baseMapper.updateDate(param);
    }
    /**
     *
     * @Method Name : getVehicleInfo
     * @Method 설명   : 차적 정보를 조회한다.
     * @작성자         : 전현진
     * @작성일         : 2025. 02. 20.
     *
     * @param Map param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @Override
    public List<Map<String, Object>> getVehicleManage(Map param) throws Exception {
        return baseMapper.selectVehicleManage(param);
    }

    @Override
    public String processVehicleData(List<Map<String, String>> vehicleDataList) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimestamp = dateFormat.format(new Date());

        for (Map<String, String> vehicle : vehicleDataList) {
            String carNo = vehicle.get("vehicleNumber"); // 차량번호
            String carType = vehicle.get("vehicleType"); // 차종

            // 차량번호 존재 여부 확인
            int exists = baseMapper.checkVehicleExists(carNo);

            if (exists == 0) {
                // 차량번호가 테이블에 없으면 INSERT
                baseMapper.insertVehicleInfo(Map.of(
                        "carNo", carNo,
                        "carType", carType,
                        "mdfyDt", currentTimestamp
                ));
            } else {
                // 차량번호가 테이블에 있으면 UPDATE
                baseMapper.updateVehicleInfo(Map.of(
                        "carNo", carNo,
                        "carType", carType,
                        "mdfyDt", currentTimestamp
                ));
            }
        }
        return "차량 데이터 처리 완료: 총 " + vehicleDataList.size() + "건";
    }

    @Override
    public String addVehicleInfo(Map<String, String> param) {
        try {
            int insertCount = baseMapper.addVehicleInfo(param); // INSERT된 행 수 반환
            if (insertCount > 0) {
                return param.get("VCAR_NO") + " 차량을 추가했습니다.";
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                return "차량번호가 이미 있습니다. 확인해주세요.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "차량번호는 한글과 숫자만 가능하며 차종은 숫자만 입력가능 합니다.";
        }
        return "해당 차량번호가 이미 존재합니다. 차량번호를 확인해주세요.";
    }

    @Override
    public String deleteVehicleInfo(Map<String, String> param) {
        try {
            int deleteCount = baseMapper.deleteVehicleInfo(param);
            if (deleteCount > 0) {
                return param.get("VCAR_NO") + " 차량을 삭제했습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "차량번호는 한글과 숫자만 가능합니다.";
        }
        return "해당 차량번호가 존재하지 않습니다. 차량번호를 확인해주세요.";
    }

    @Override
    public String oneUpdateVehicleInfo(Map<String, String> param) {
        try {
            int updateCount = baseMapper.oneUpdateVehicleInfo(param);
            if (updateCount > 0) {
                String carTypeDescription = getCarTypeDescription(param.get("CAR_TYPE"));
                return param.get("VCAR_NO") + " 차량의 차종을 [" + carTypeDescription + "] 으로 변경했습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "차량번호는 한글과 숫자만 가능하며 차종은 숫자만 입력가능 합니다.";
        }
        return "해당 차량번호가 존재하지 않습니다. 차량번호를 확인해주세요.";
    }

    private String getCarTypeDescription(String carType) {
        switch (carType) {
            case "1": return "소형";
            case "2": return "중형";
            case "3": // case 3, 4, 5 모두 대형으로 처리
            case "4":
            case "5": return "대형";
            case "6": return "경형";
            default: return "알 수 없는 차종";
        }
    }

}
