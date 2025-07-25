package gdtcs.common.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import gdtcs.common.mapper.ServerInfoMapper;
import gdtcs.common.service.ServerInfoService;

@Service
public class ServerInfoServiceImpl implements ServerInfoService, InitializingBean, ApplicationListener<ContextRefreshedEvent>{
	protected Log log = LogFactory.getLog(this.getClass());
	
	 @Resource(name="serverInfoMapper")
    private ServerInfoMapper serverInfoMapper;
	
	// BASE_SVRINFO 테이블 참초
	private HashMap<String, Map> PATH_UPLOAD_ROOT; // 업로드 서버 및 경로
	private HashMap<String, Map> SERVER_INTER_OUT; // 외부연계서버
	private HashMap<String, Map> SERVER_INTER_LANE; // 차로연계서버
	private HashMap<String, Map> DB_VIDEO; // 영상DB서버
	private HashMap<String, Map> FTP_VIDEO; // 영상FTP서버
	private HashMap<String, Map> DB_FOCUS_EXEM;
	
	@Autowired
	public DataSource dataSource;

	@Autowired
	private Environment environment;
	
	public ServerInfoServiceImpl() throws SQLException{
		log.debug("ServerInfo()");
	}

	@PostConstruct
	public void postConstruct() {
		log.debug("ServerInfo.postConstruct()");
		// 로컬 개발용 서버 정보를 추가한다
		// 업로드 루트 디렉토리 정보
		/*
		PATH_UPLOAD_ROOT = new ArrayList<Map>();
		Map<String, String> map = new HashMap<>();
		map.put("IC_CODE", "011");
		map.put("SVR_ADDR", "172.20.30.24");
		map.put("SVR_PORT", "");
		map.put("SVR_VAR_01", "/home/tomcat/pdmtcs/upload/");
		map.put("ACCNT_ID", "");
		map.put("ACCNT_PW", "");
		PATH_UPLOAD_ROOT.add(map);
		// 외부 연계 서버 정보
		SERVER_INTER_OUT = new ArrayList<Map>();
		map = new HashMap<>();
		map.put("IC_CODE", "011");
		map.put("SVR_ADDR", "172.20.30.24");
		map.put("SVR_PORT", "44444");
		map.put("SVR_VAR_01", "");
		map.put("ACCNT_ID", "");
		map.put("ACCNT_PW", "");
		SERVER_INTER_OUT.add(map);
		// 차로 연계 서버 정보
		SERVER_INTER_LANE = new ArrayList<Map>();
		map = new HashMap<>();
		map.put("IC_CODE", "011");
		map.put("SVR_ADDR", "172.20.30.23");
		map.put("SVR_PORT", "55555");
		map.put("SVR_VAR_01", "");
		map.put("ACCNT_ID", "");
		map.put("ACCNT_PW", "");
		SERVER_INTER_LANE.add(map);
		// 영상 DB 정보
		DB_VIDEO = new ArrayList<Map>();
		map = new HashMap<>();
		map.put("IC_CODE", "011");
		map.put("SVR_ADDR", "jdbc:mariadb://172.20.30.55:3306/tves");
		map.put("SVR_PORT", "3306");
		map.put("SVR_VAR_01", "org.mariadb.jdbc.Driver");
		map.put("ACCNT_ID", "tves");
		map.put("ACCNT_PW", "tves");
		DB_VIDEO.add(map);
		// 영상 FTP 정보
		FTP_VIDEO = new ArrayList<Map>();
		map = new HashMap<>();
		map.put("IC_CODE", "011");
		map.put("SVR_ADDR", "172.20.30.55");
		map.put("SVR_PORT", "21");
		map.put("SVR_VAR_01", "");
		map.put("ACCNT_ID", "tves");
		map.put("ACCNT_PW", "tves");
		FTP_VIDEO.add(map);
		*/
	}
	
	@Override
    public void afterPropertiesSet() throws Exception {
		log.debug("afterPropertiesSet()");
    }

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug("onApplicationEvent()");
	}
	
	public void init() {
		log.debug("init()");
    }
	
	/**
	 *  
	 * @Method Name	: selectServerInfo
	 * @Method 설명   	: 서버 정보를 가져오는 서비스
	 * @작성자 		: 박형철
	 * @작성일 		: 2022.11.22
	 *
	 * @param searchMap
	 * @return
	 * @throws SqlSessionException
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> selectServerInfo(Map param) throws SqlSessionException {	
		return serverInfoMapper.selectServerInfo(param);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getServerInfo(String IC_CODE) throws IOException{
		Map searchMap = new HashMap<>();
		searchMap.put("IC_CODE", IC_CODE);		
		searchMap.put("SVR_CTGR", "100");
		setSERVER_INTER_OUT(selectServerInfo(searchMap));		
		searchMap.put("SVR_CTGR", "101");
		setSERVER_INTER_LANE(selectServerInfo(searchMap));
		//log.debug("save SERVER_INTER_LANE():"+cmmService.selectServerInfo(searchMap).get(0).get("SVR_ADDR"));
		//log.debug("saved SERVER_INTER_LANE():"+serverInfo.getSERVER_INTER_LANE().get(0).get("SVR_ADDR"));
		searchMap.put("SVR_CTGR", "901");
		setPATH_UPLOAD_ROOT(selectServerInfo(searchMap));		
		searchMap.put("SVR_CTGR", "200");
		setDB_VIDEO(selectServerInfo(searchMap));
		searchMap.put("SVR_CTGR", "201");
		setFTP_VIDEO(selectServerInfo(searchMap));
		searchMap.put("SVR_CTGR", "102");
		setDB_FOCUS_EXEM(selectServerInfo(searchMap));
	}

	public HashMap<String, Map> getPATH_UPLOAD_ROOT() {
		return this.PATH_UPLOAD_ROOT;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setPATH_UPLOAD_ROOT(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.PATH_UPLOAD_ROOT = hashMap;
	}

	public HashMap<String, Map> getSERVER_INTER_OUT() {
		return SERVER_INTER_OUT;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setSERVER_INTER_OUT(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.SERVER_INTER_OUT = hashMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Map> getSERVER_INTER_LANE() {
		return SERVER_INTER_LANE;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setSERVER_INTER_LANE(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.SERVER_INTER_LANE = hashMap;
	}

	public HashMap<String, Map> getDB_VIDEO() {
		return DB_VIDEO;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setDB_VIDEO(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.DB_VIDEO = hashMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Map> getFTP_VIDEO() {
		return FTP_VIDEO;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setFTP_VIDEO(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.FTP_VIDEO = hashMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Map> getDB_FOCUS_EXEM() {
		return DB_FOCUS_EXEM;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setDB_FOCUS_EXEM(List<Map<String, Object>> list) {
		HashMap hashMap = new HashMap<String, Map>();
		
		for(Map item : list){
			hashMap.put(item.get("IC_CODE"), item);
		}
		
		this.DB_FOCUS_EXEM = hashMap;
	}
}