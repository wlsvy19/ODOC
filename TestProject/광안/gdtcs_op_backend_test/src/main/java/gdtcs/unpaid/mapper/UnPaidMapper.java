package gdtcs.unpaid.mapper;

import java.util.List;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import java.util.Map;

@Mapper("unPaidMapper")
public interface UnPaidMapper {

	List<Map<String, Object>> getUnPaidCollect(Map<String, Object> param);

	List<Map<String, Object>> getUnPaidCar(Map<String, Object> param);

	List<Map<String, Object>> getLineCtrlOfficePayList(Map<String, Object> param);
}
