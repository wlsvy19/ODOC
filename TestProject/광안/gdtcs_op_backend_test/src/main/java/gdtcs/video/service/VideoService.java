package gdtcs.video.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionException;

public interface VideoService {
	public List<Map<String, Object>> selectEtcsImageList(String IC_CODE, String tolling_time, String work_number, String processing_number) throws SqlSessionException, IOException;
}
