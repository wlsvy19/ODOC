package gdtcs.day.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;


@Mapper("dayMapper")
public interface DayMapper {
	List<Map<String, Object>> selectDayFinList(Map<String, Object> param);
	List<Map<String, Object>> selectDayFinMdfyList(Map<String, Object> param);
	List<Map<String, Object>> selectDayFinLockList(Map<String, Object> param);
	List<Map<String, Object>> selectDayFinUnlockList(Map<String, Object> param);
	List<Map<String, Object>> selectDayFinReport(Map<String, Object> param);

	int insertDayFin(Map<String, Object> param);
	int insertDayFinCancel(Map<String, Object> param);
	int insertDayFinLock(Map<String, Object> param);
	int insertDayFinUnlock(Map<String, Object> param);
	
	List<Map<String, Object>> selectDayFinReportNew(Map<String, Object> param);

	List<Map<String, Object>> selectImageList(List<Map<String, Object>> param);
}
