package gdtcs.inter.service;

import java.util.List;
import java.util.Map;

public interface InterService {

    Map<String, Object> getInter(Map<String, Object> param) throws Exception;

    Map<String, Object> getTaxi(Map<String, Object> param) throws Exception;

    Map<String, Object> getPreRegi(Map<String, Object> param) throws Exception;

    Map<String, Object> getCenterLockYn(Map<String, Object> param) throws Exception;

    Map<String, Object> getIsDayFin(Map<String, Object> param) throws Exception;

    Map<String, Object> requestInter(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getCenterInter(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getFileDivNm(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> transTaxiPre(Map<String, Object> param) throws Exception;

    void transTaxiPreTraffic(Map<String, Object> param) throws Exception;

    boolean checkIsHoliday(Map<String, Object> param) throws Exception;
}
