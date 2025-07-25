package gdtcs.video.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gdtcs.common.service.ServerInfoService;

@Component
public class DatabaseSourceVideo {
	public int TIMEOUT_VALUE = 30; // seconds
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private ServerInfoService serverInfoService;
	
	public HashMap<String, Connection> connections;
	public PreparedStatement preparedStatement;
	
	@PreDestroy
	public void destroy() {
		try {
			for(String key : connections.keySet()){
				Connection connection = connections.get(key);
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public Connection getConnection(String IC_CODE) {
		log.debug("DatabaseSourceVideo.getConnection() is started.");
		try {
			
			if(serverInfoService.getDB_VIDEO() == null 
				|| MapUtils.isEmpty(serverInfoService.getDB_VIDEO())
				|| !serverInfoService.getDB_VIDEO().containsKey(IC_CODE)){
				serverInfoService.getServerInfo(IC_CODE);
			}
			
			/*
			if(connection != null){
				connection.close();
				connection = null;
			}
			*/
			
			/*
			if(connection != null && connection.isValid(5)){
				return connection;
			}
			*/
			
			String url = serverInfoService.getDB_VIDEO().get(IC_CODE).get("SVR_ADDR").toString();
			String id = serverInfoService.getDB_VIDEO().get(IC_CODE).get("ACCNT_ID").toString();
			String pw = serverInfoService.getDB_VIDEO().get(IC_CODE).get("ACCNT_PW").toString();
			
			log.debug("url: " + url);
			//log.debug("id: " + id);
			//log.debug("pw: " + pw);
			
			Class.forName("org.mariadb.jdbc.Driver");
			DriverManager.setLoginTimeout(TIMEOUT_VALUE);
			
			if(connections == null){
				connections = new HashMap<>();
			}
			
			Connection connection = connections.get(IC_CODE);
			log.debug("connection: " + connection);
			if(connection != null && connection.isValid(TIMEOUT_VALUE)){
				return connection;
			}
			
			connection = DriverManager.getConnection(url, id, pw);
			connections.put(IC_CODE, connection);
			log.debug("getClientInfo: " + connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLNonTransientConnectionException e) {
			log.debug("Failed to connect Video DB: " + e.toString());
			//e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("DatabaseSourceVideo.getConnection() is finished.");
		return null;		
	}
	
	public List<Map<String, Object>> selectEtcsImageList(String IC_CODE, String tolling_time, String work_number, String processing_number){
		
		String sql = "SELECT tollgate_id, tolling_time, work_number, processing_number, car_number, directory_path, filename, is_confirmed FROM hipass_image_data WHERE tollgate_id = ? AND DATE(tolling_time) = LEFT(?, 8) AND work_number = ? AND processing_number = LPAD(?, 8, '0') AND is_confirmed = 1 ORDER BY tolling_time DESC";
		//log.debug(sql);
		//System.out.println(sql);
		//System.out.println("IC_CODE: " + IC_CODE);
		//System.out.println("tolling_time: " + tolling_time);
		//System.out.println("work_number: " + work_number);
		//System.out.println("processing_number: " + processing_number);
		String processingNumberHex = Integer.toHexString(Integer.parseInt(processing_number)).toUpperCase();
		try {
			Connection connection = connections.get(IC_CODE);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, IC_CODE);
			preparedStatement.setString(2, tolling_time);
			preparedStatement.setString(3, work_number);
			preparedStatement.setString(4, processingNumberHex);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			return convertResultSetToArrayList(resultSet);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> selectAuditImageList(String IC_CODE, String tolling_time){
		log.info("selectAuditImageList start");
		String sql = "select tolling_time, work_number, processing_number from hipass_image_data where DATE(tolling_time) < LEFT(?, 8) and (audit_status = 0 or audit_status is null) group by tolling_time, work_number, processing_number;";

		try {
			log.info("selectAuditImageList try");
			Connection connection = connections.get(IC_CODE);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tolling_time);
			
//			preparedStatement.setQueryTimeout(30);  // 30초로 타임아웃 설정
			
			ResultSet resultSet = preparedStatement.executeQuery();
			log.info("selectAuditImageList finish: " + convertResultSetToArrayList(resultSet).size());
			
			return convertResultSetToArrayList(resultSet);
		} catch(SQLException e) {
			log.info("selectAuditImageList catch: " + e);
			e.printStackTrace();
		}
		log.info("selectAuditImageList end");
		return null;
	}
	
	
	public int updateImageAuditStatus(String IC_CODE, List<Map<String, Object>> imageList){
		String sql = "UPDATE hipass_image_data "
				   + "SET audit_status = ? "
				   + "WHERE work_number = ? AND processing_number = LPAD(?, 8, '0') "
				   + "AND DATE(tolling_time) = LEFT(?, 8)  AND tollgate_id = ? ";

		int updateLowCnt = 0;
//		System.out.println("SELECT 종료, update 반복 시작: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		try {
			Connection connection = connections.get(IC_CODE);
			preparedStatement = connection.prepareStatement(sql);
			
			for (Map<String, Object> imageMap : imageList) {
				String tolling_time = imageMap.get("WORK_DATE").toString();
				String work_number = imageMap.get("WORK_NO").toString();
				String processing_number = imageMap.get("HAND_SNO").toString();
				String audit_status = imageMap.get("AUDIT_STATUS").toString();
				String processingNumberHex = Integer.toHexString(Integer.parseInt(processing_number)).toUpperCase();
				
				preparedStatement.setString(1, audit_status);
				preparedStatement.setString(2, work_number);
				preparedStatement.setString(3, processingNumberHex);
				preparedStatement.setString(4, tolling_time);
				preparedStatement.setString(5, IC_CODE);
				preparedStatement.addBatch();
			}
			int[] result = preparedStatement.executeBatch();
			for (int rowCnt : result) {
                if (rowCnt >= 0) { 
                	updateLowCnt += rowCnt;
                }
            }
		} catch(SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("update 반복 종료: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		return updateLowCnt;
	}
	
	public List<Map<String,Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	 
	    while(rs.next()) {
	        HashMap<String,Object> row = new HashMap<String, Object>(columns);
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i), rs.getObject(i));
	        }
	        list.add(row);
	    }
	    
	    return list;
	}
}
