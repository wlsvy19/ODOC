package gdtcs.common.mapper;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commonMapper")
public interface CommonMapper {
	
	Map<String, Object> getImagePath(Map<String, Object> param);
	List<Map<String, Object>> selectFare(Map param);
	List<Map<String, Object>> selectRevNo(Map param);
	Map<String, Object> selectLogoImage();
}