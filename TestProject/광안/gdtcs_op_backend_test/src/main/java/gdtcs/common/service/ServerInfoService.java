package gdtcs.common.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ServerInfoService {
	public List<Map<String, Object>>  selectServerInfo(Map map) throws IOException;
	public void getServerInfo(String IC_CODE) throws IOException;
	public HashMap<String, Map> getPATH_UPLOAD_ROOT();
	public HashMap<String, Map> getSERVER_INTER_OUT();
	public HashMap<String, Map> getSERVER_INTER_LANE();
	public HashMap<String, Map> getDB_VIDEO();
	public HashMap<String, Map> getFTP_VIDEO();
	public HashMap<String, Map> getDB_FOCUS_EXEM();
	public void setPATH_UPLOAD_ROOT(List<Map<String, Object>> list);
	public void setSERVER_INTER_OUT(List<Map<String, Object>> list);
	public void setSERVER_INTER_LANE(List<Map<String, Object>> list);
	public void setDB_VIDEO(List<Map<String, Object>> list);
	public void setFTP_VIDEO(List<Map<String, Object>> list);
	public void setDB_FOCUS_EXEM(List<Map<String, Object>> list);
}
