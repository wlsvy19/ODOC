package gdtcs.day.service;

import java.util.List;
import java.util.Map;

public interface DayService {
	List<Map<String, Object>> getDayFinList(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDayFinMdfyList(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDayFinLockList(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDayFinUnlockList(Map<String, Object> param) throws Exception;
	Map<String, List<Map<String, Object>>> getDayFinReport(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDayFinPeriodReport(Map<String, Object> param) throws Exception;
	
	Map<String, Object> setDayFin(Map<String, Object> param) throws Exception;
	Map<String, Object> setDayFinCancel(Map<String, Object> param) throws Exception;
	Map<String, Object> setDayFinLock(Map<String, Object> param) throws Exception;
	Map<String, Object> setDayFinUnlock(Map<String, Object> param) throws Exception;
	
	Map<String, List<Map<String, Object>>> getDayFinReportNew(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDayFinPeriodReportNew(Map<String, Object> param) throws Exception;

	int setImageAuditStatus(Map<String, Object> param) throws Exception;
}
