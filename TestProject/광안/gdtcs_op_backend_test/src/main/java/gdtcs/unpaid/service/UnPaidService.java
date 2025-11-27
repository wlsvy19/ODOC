package gdtcs.unpaid.service;

import java.util.List;
import java.util.Map;

public interface UnPaidService {

	List<Map<String, Object>> getUnPaidCar(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getUnPaidCollect(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getLineCtrlOfficePayList(Map<String, Object> param) throws Exception;
}
