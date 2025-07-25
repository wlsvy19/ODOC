package gdtcs.traffic.service;



import java.util.List;
import java.util.Map;

public interface TrafficService {
	List<Map<String, Object>> getCarLineTraffic(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getCarTypeTraffic(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getDataReceive(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getTrafficDaily(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficDirectionPeriod(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficDirectionDay(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficHour(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficDailyDirection(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficDailyPayDiv(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficLine(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficLineHour(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getTrafficPreMonth(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficMonth(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficYear(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficDayOfWeek(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficQuarter(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficYearDayOfWeek(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTrafficHourHoliday(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getDailyHipassUsageStatus(Map<String, Object> param) throws Exception;

}
