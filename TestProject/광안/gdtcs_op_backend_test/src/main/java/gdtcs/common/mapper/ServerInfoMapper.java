package gdtcs.common.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("serverInfoMapper")
public interface ServerInfoMapper {
	List<Map<String, Object>> selectServerInfo(Map param);
}