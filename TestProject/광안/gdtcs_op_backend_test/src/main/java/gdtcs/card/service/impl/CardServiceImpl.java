package gdtcs.card.service.impl;

import gdtcs.card.vo.PACKET_FOCUS_REQUEST;
import gdtcs.card.vo.PACKET_FOCUS_RESPONSE;
import gdtcs.common.service.ServerInfoService;
import oracle.net.ns.Packet;
import gdtcs.card.mapper.CardMapper;
import gdtcs.card.service.CardService;

import org.apache.commons.collections.MapUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

@Service("cardService")
public class CardServiceImpl extends EgovAbstractServiceImpl implements CardService {
    protected Log log = LogFactory.getLog(this.getClass());


    @Resource(name = "cardMapper")
    private CardMapper cardMapper;

    @Autowired
    private ServerInfoService serverInfo;

    @Override
    public List<Map<String, Object>> getCardCoCode(Map<String, Object> param) throws Exception {
        return cardMapper.getCardCoCode(param);
    }

    @Override
    public List<Map<String, Object>> getCardUseList(Map<String, Object> param) throws Exception {
        return cardMapper.getCardUseList(param);
    }

    @Override
    public List<Map<String, Object>> getTcardBL(Map<String, Object> param) throws Exception {
        return cardMapper.getTcardBL(param);
    }

    @Override
    public List<Map<String, Object>> getEcardBL(Map<String, Object> param) throws Exception {
        return cardMapper.getEcardBL(param);
    }

    @Override
    public List<Map<String, Object>> getEcardBLHist(Map<String, Object> param) throws Exception {
        return cardMapper.getEcardBLHist(param);
    }

    @Override
    public List<Map<String, Object>> getLightCarPL(Map<String, Object> param) throws Exception {
        return cardMapper.getLightCarPL(param);
    }

    @Override
    public int addLightCarPL(Map<String, Object> param) throws Exception {
        return cardMapper.updateLightCarPL(param);
    }

    @Override
    public int delLightCarPL(Map<String, Object> param) throws Exception {
        return cardMapper.deleteLightCarPL(param);
    }

    @Override
    public List<Map<String, Object>> getIntewelfCardPL(Map<String, Object> param) throws Exception {
        return cardMapper.getIntewelfCardPL(param);
    }

    @Override
    public int addIntewelfCardPL(Map<String, Object> param) throws Exception {
        return cardMapper.updateIntewelfCardPL(param);
    }

    @Override
    public int delIntewelfCardPL(Map<String, Object> param) throws Exception {
        cardMapper.deleteIntewelfCardPLCHG(param);
        return cardMapper.deleteIntewelfCardPL(param);
    }

    @Override
    public List<Map<String, Object>> getTcardBIN(Map<String, Object> param) throws Exception {
        return cardMapper.getTcardBIN(param);
    }

    @Override
    public List<Map<String, Object>> getEcardBINManage(Map<String, Object> param) throws Exception {
        return cardMapper.getEcardBINManage(param);
    }

    @Override
    public int addEcardBINManage(Map<String, Object> param) throws Exception {
        return cardMapper.updateEcardBINManage(param);
    }

    @Override
    public int addBinFile(Map<String, Object> param) throws Exception {
        return cardMapper.updateBinFile(param);
    }

    @Override
    public int delEcardBINManage(Map<String, Object> param) throws Exception {
        return cardMapper.deleteEcardBINManage(param);
    }

    @Override
    public List<Map<String, Object>> getCardCoDivPayList(Map<String, Object> param) throws Exception {
        return cardMapper.getCardCoDivPayList(param);
    }

    @Override
    public List<Map<String, Object>> getCptLpayEcardCardCoDiv(Map<String, Object> param) throws Exception {
        return cardMapper.getCptLpayEcardCardCoDiv(param);
    }

    @Override
    public List<Map<String, Object>> getExemPLBusan(Map<String, Object> param) throws Exception {
        return cardMapper.getExemPLBusan(param);
    }
    @Override
    public int addExemPLADDR(String CAR_NO, String ADDRESS) throws Exception {
        return cardMapper.updateExemPLADDR(CAR_NO, ADDRESS);
    }
 
    /**
     *
     *
     * @Method Name    : requestExemPL
     * @Method 설명    : 면제 PL요청
     * @작성자        : 이지형
     * @작성일        : 2025. 01. 03.
     *
     * @param Map param
     * @return String
     * @throws IOException
     *
     */ 
    @Override
    public Map<String, Object> requestExemPL(Map param) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            // 서버 정보 설정
            String IC_CODE = param.get("IC_CODE").toString();

            if (serverInfo.getDB_FOCUS_EXEM() == null
                    || MapUtils.isEmpty(serverInfo.getDB_FOCUS_EXEM())
                    || !serverInfo.getDB_FOCUS_EXEM().containsKey(IC_CODE)) {
                serverInfo.getServerInfo(IC_CODE);
            }

            String FOCUS_SERVER_IP_ADDRESS = serverInfo.getDB_FOCUS_EXEM().get(IC_CODE).get("SVR_ADDR").toString();
            int FOCUS_SERVER_PORT = Integer.parseInt(serverInfo.getDB_FOCUS_EXEM().get(IC_CODE).get("SVR_PORT").toString());

            log.info("SERVER_IP_ADDRESS: " + FOCUS_SERVER_IP_ADDRESS);
            log.info("SERVER_PORT: " + FOCUS_SERVER_PORT);

            // 소켓 생성 및 연결
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(FOCUS_SERVER_IP_ADDRESS, FOCUS_SERVER_PORT);
            socket.setSoTimeout(10000);
            socket.connect(socketAddress, 10000);

            if (!socket.isConnected()) {
                log.info("면제PL 조회서버에 연결하지 못했습니다.");
                result.put("ERROR_CODE", "-1");
                return result;
            }

            log.info("면제 PL를 조회할 차량정보를 전송합니다.");
            String dataToSend = param.get("DATA").toString();

            // 데이터 전송 로그 추가
            log.info("PACKET_AUTH_CHAR: " + new String(param.get("PACKET_AUTH_CHAR").toString()));
            log.info("PACKET_SORT: " + new String(param.get("PACKET_SORT").toString()));
            log.info("PACKET_CAR_NO: " + new String(param.get("PACKET_CAR_NO").toString()));
            log.info("PACKET_END_CHAR: " + new String(param.get("PACKET_END_CHAR").toString()));
            log.info("차량정보를 전송하였습니다.");

            // 실제 데이터 전송
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.write(dataToSend.getBytes("euc-kr"));

            log.info("면제 PL을 수신합니다.");
            // 데이터 수신
            inputStream = socket.getInputStream();
            int max = 600;
            byte[] data = new byte[max];
            int byteRead = 0;

            while (byteRead < data.length) {
                int read = inputStream.read();
                if (read == -1) {
                    // 연결 종료
                    break;
                }
                data[byteRead++] = (byte) read;

                // 종료 문자(\r\n) 검사
                if (byteRead > 1 && data[byteRead - 2] == 13 && data[byteRead - 1] == 10) {
                    // 패킷 종료
                    break;
                }
            }

            // 데이터 처리
            byte[] receivedPacket = new byte[byteRead];
            System.arraycopy(data, 0, receivedPacket, 0, byteRead);

            log.info("면제 PL을 수신하였습니다.");

            String readdata = new String(receivedPacket, "euc-kr");
            String[] responseList = readdata.split(Character.toString((char) 0x02));

            log.info("수신받은 바이트 길이: " + receivedPacket.length);
            log.info("수신받은 바이트: " + Arrays.toString(receivedPacket));
            log.info("getData: " + readdata);

            if (responseList[2].length() > 50) {
                responseList[2] = responseList[2].substring(0, 50);
            }
            if (responseList[3].length() > 500) {
                responseList[3] = responseList[3].substring(0, 500);
            }
            if (responseList[4].length() > 1) {
                responseList[4] = responseList[4].substring(1, 2);
            }
            if (responseList[8].length() > 2) {
                responseList[8] = responseList[8].substring(0, 2);
            }

            log.info("getPACKET_CAR_NO: " + responseList[1]);
            log.info("getPACKET_NAME: " + responseList[2]);
            log.info("getPACKET_ADDRESS: " + responseList[3]);
            log.info("getPACKET_CAR_TYPE: " + responseList[4]);
            log.info("getPACKET_MULTI_CHILD: " + responseList[5]);
            log.info("getPACKET_PERSON_MERIT: " + responseList[6]);
            log.info("getPACKET_DISABLED_PERSON: " + responseList[7]);
            log.info("getPACKET_LOW_POLLUTION: " + responseList[8]);

            Map<String, String> ResultIndex = new LinkedHashMap<>();
            ResultIndex.put("차량번호", responseList[1]);
            ResultIndex.put("이름", responseList[2]);
            ResultIndex.put("주소", responseList[3]);

            switch (responseList[4]) {
                case "0":
                case "9":
                    responseList[4] = "조회불능";
                    break;
                case "1":
                    responseList[4] = "소형";
                    break;
                case "2":
                    responseList[4] = "중형";
                    break;
                case "3":
                case "4":
                case "5":
                    responseList[4] = "대형";
                    break;
                case "6":
                    responseList[4] = "경형";
                    break;
            }

            ResultIndex.put("차종", responseList[4]);

            int[] targetIndexes = {5, 6, 7, 8};

            for (int index : targetIndexes) {
                switch (responseList[index]) {
                    case "01":
                        responseList[index] = "면제 아님";
                        break;
                    case "02":
                        responseList[index] = "조회 불능";
                        break;
                    case "03":
                        responseList[index] = "다자녀 차량";
                        break;
                    case "04":
                        responseList[index] = "유공자 차량";
                        break;
                    case "05":
                        responseList[index] = "장애인 차량";
                        break;
                    case "06":
                        responseList[index] = "전기(부산) 차량";
                        break;
                    case "07":
                        responseList[index] = "수소(부산)";
                        break;
                    case "08":
                        responseList[index] = "공차(택시)";
                        break;
                    case "09":
                        responseList[index] = "우수기업차량";
                        break;
                    case "10":
                        responseList[index] = "우수납세자 차량";
                        break;
                    case "11":
                        responseList[index] = "효행자 차량";
                        break;
                    case "12":
                        responseList[index] = "모범노동차량";
                        break;
                    case "13":
                        responseList[index] = "장애인복지사업 차량";
                        break;
                    case "14":
                        responseList[index] = "두리발 차량";
                        break;
                    case "15":
                        responseList[index] = "시내순환";
                        break;
                    case "16":
                        responseList[index] = "시내&공항리무진버스";
                        break;
                    case "17":
                        responseList[index] = "저공해차량";
                        break;
                    case "99":
                        responseList[index] = "면제 여부 조회 전/진행";
                        break;
                }
            }

            ResultIndex.put("다자녀 결과", responseList[5]);
            ResultIndex.put("유공자 결과", responseList[6]);
            ResultIndex.put("장애인 결과", responseList[7]);
            ResultIndex.put("저공해 결과", responseList[8]);

            if (!responseList[5].equals("면제 아님")) {
                ResultIndex.put("면제 결과", responseList[5]);
            } else if (!responseList[6].equals("면제 아님") && responseList[5].equals("면제 아님")) {
                ResultIndex.put("면제 결과", responseList[6]);
            } else if (!responseList[7].equals("면제 아님") && responseList[6].equals("면제 아님") && responseList[5].equals("면제 아님")) {
                ResultIndex.put("면제 결과", responseList[7]);
            } else if (!responseList[8].equals("면제 아님") && responseList[7].equals("면제 아님") && responseList[6].equals("면제 아님") && responseList[5].equals("면제 아님")) {
                ResultIndex.put("면제 결과", responseList[8]);
            } else {
                ResultIndex.put("면제 결과", "면제 아님");
            }

            log.info(ResultIndex.toString());
            result.put("ERROR_CODE", ResultIndex);
            return result;

        } catch (Exception e) {
            log.error("오류 발생: ", e);
            result.put("ERROR_CODE", "-1");
            return result;

        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                    log.info("#####면제PL조회 - DataOutputStream Close#####");
                }
                if (outputStream != null) {
                    outputStream.close();
                    log.info("#####면제PL조회 - OutputStream Close#####");
                }
                if (inputStream != null) {
                    inputStream.close();
                    log.info("#####면제PL조회 - InputStream Close#####");
                }
                if (socket != null) {
                    socket.close();
                    log.info("#####면제PL조회 - Socket Close#####");
                }
            } catch (IOException e) {
                log.error("#####면제PL조회 - 리소스를 닫는 중 오류 발생: ", e);
            }
        }

    }

}
