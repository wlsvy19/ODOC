package gdtcs.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CommonService {
	Map<String, Object> getImagePath(Map<String, Object> param) throws Exception;
	public List<Map<String, Object>> getFare(Map param) throws Exception;
	public List<Map<String, Object>> getRevNo(Map param) throws Exception;
	public File getLogoImage(File file) throws Exception;
}
