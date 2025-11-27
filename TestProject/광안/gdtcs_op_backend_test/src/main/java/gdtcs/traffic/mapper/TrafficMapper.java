package gdtcs.traffic.mapper;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;
import java.util.Map;

@Mapper("trafficMapper")
public interface TrafficMapper {
	List<Map<String, Object>> getCarLineTraffic(Map<String, Object> param);
	List<Map<String, Object>> getCarTypeTraffic(Map<String, Object> param);
	List<Map<String, Object>> getDataReceive(Map<String, Object> param);
    List<Map<String, Object>> getTrafficDaily(Map<String, Object> param);
    List<Map<String, Object>> getTrafficDirectionPeriod(Map<String, Object> param);
    List<Map<String, Object>> getTrafficDirectionDay(Map<String, Object> param);
    List<Map<String, Object>> getTrafficHour(Map<String, Object> param);
    List<Map<String, Object>> getTrafficDailyDirection(Map<String, Object> param);
    List<Map<String, Object>> getTrafficDailyPayDiv(Map<String, Object> param);
    List<Map<String, Object>> getTrafficLine(Map<String, Object> param);
    List<Map<String, Object>> getTrafficLineHour(Map<String, Object> param);
    
    List<Map<String, Object>> selectTrafficPreMonth(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficMonth(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficYear(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficDayOfWeek(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficQuarter(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficYearDayOfWeek(Map<String, Object> param);
    List<Map<String, Object>> selectTrafficHourHoliday(Map<String, Object> param);
    List<Map<String, Object>> selectDailyHipassUsageStatus(Map<String, Object> param);
}