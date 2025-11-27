package gdtcs.day.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gdtcs.day.service.DayService;
import gdtcs.day.mapper.DayMapper;
import gdtcs.video.util.DatabaseSourceVideo;

@Service("DayService")
public class DayServiceImpl extends EgovAbstractServiceImpl implements DayService {

    @Resource(name = "dayMapper")
    private DayMapper dayMapper;

	@Autowired
	DatabaseSourceVideo databaseSourceVideo;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
    @Override
    public List<Map<String, Object>> getDayFinList(Map<String, Object> param){
    	return dayMapper.selectDayFinList(param);
    }
    @Override
    public List<Map<String, Object>> getDayFinMdfyList(Map<String, Object> param){
    	return dayMapper.selectDayFinMdfyList(param);
    }
    @Override
	public List<Map<String, Object>> getDayFinLockList(Map<String, Object> param){
    	return dayMapper.selectDayFinLockList(param);
    }
    @Override
    public List<Map<String, Object>> getDayFinUnlockList(Map<String, Object> param){
    	return dayMapper.selectDayFinUnlockList(param);
    }
    @Override
    public Map<String, List<Map<String, Object>>> getDayFinReport(Map<String, Object> param){
    	Map<String, List<Map<String, Object>>> result = new HashMap<>();
    	
    	String day = param.get("WORK_DATE").toString();
    	String month = param.get("WORK_DATE").toString().substring(4,6);
    	String year = param.get("WORK_DATE").toString().substring(0,4);
    	
    	result.put("day", dayMapper.selectDayFinReport(param));
    	
    	param.put("MONTH", year+month+"01");
    	result.put("month", dayMapper.selectDayFinReport(param));
    	
    	param.put("YEAR", year+"0101");
    	result.put("year", dayMapper.selectDayFinReport(param));
    	
    	return result;
    }
    @Override
    public List<Map<String, Object>> getDayFinPeriodReport(Map<String, Object> param){
    	return dayMapper.selectDayFinReport(param);
    }

    @Override
	public Map<String, Object> setDayFin(Map<String, Object> param){
    	dayMapper.insertDayFin(param);
    	return param;
    }
    @Override
	public Map<String, Object> setDayFinCancel(Map<String, Object> param){
    	dayMapper.insertDayFinCancel(param);
    	return param;
    }
    @Override
	public Map<String, Object> setDayFinLock(Map<String, Object> param){
    	dayMapper.insertDayFinLock(param);
    	return param;
    }
    @Override
    public Map<String, Object> setDayFinUnlock(Map<String, Object> param) {
    	dayMapper.insertDayFinUnlock(param);
    	return param;
    }
    @Override
    public Map<String, List<Map<String, Object>>> getDayFinReportNew(Map<String, Object> param){
    	Map<String, List<Map<String, Object>>> result = new HashMap<>();
    	
    	String day = param.get("WORK_DATE").toString();
    	String month = param.get("WORK_DATE").toString().substring(4,6);
    	String year = param.get("WORK_DATE").toString().substring(0,4);
    	
    	result.put("day", dayMapper.selectDayFinReportNew(param));
    	
    	param.put("MONTH", year+month+"01");
    	result.put("month", dayMapper.selectDayFinReportNew(param));
    	
    	param.put("YEAR", year+"0101");
    	result.put("year", dayMapper.selectDayFinReportNew(param));
    	
    	return result;
    }
    @Override
    public List<Map<String, Object>> getDayFinPeriodReportNew(Map<String, Object> param){
    	return dayMapper.selectDayFinReportNew(param);
    }
    
    @Override
	public int setImageAuditStatus(Map<String, Object> param) throws Exception{
    	String IC_CODE = param.get("IC_CODE").toString();
		databaseSourceVideo.getConnection(IC_CODE);
		int lowCount = 0;
		
		// maria에서 대상 select
		List<Map<String, Object>> auditImageList = databaseSourceVideo.selectAuditImageList(IC_CODE, param.get("WORK_DATE").toString());

		log.info("setImageAuditStatus: selectAuditImageList Fin" + auditImageList.size());
		
		// 대상 audit 값 받아오기 (oracle)
		if (auditImageList != null && auditImageList.size() > 0){
			log.info("setImageAuditStatus: Get size of ImageDB non-audit" + auditImageList.size());
			List<Map<String, Object>> tempImageList = new ArrayList<>();
			for (int i = 0; i < auditImageList.size(); i++) {
	            Map<String, Object> imageParam = new HashMap<String, Object>();
	            
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	            Date date = inputFormat.parse(auditImageList.get(i).get("tolling_time").toString());
	            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
	            String WORK_DATE = outputFormat.format(date);            
	            imageParam.put("WORK_DATE", WORK_DATE);
	            imageParam.put("WORK_NO", auditImageList.get(i).get("work_number"));
	            imageParam.put("HAND_SNO", Integer.parseInt(auditImageList.get(i).get("processing_number").toString(), 16));
	           
	            tempImageList.add(imageParam);
	            if(tempImageList.size() > 1000) {
	            	List<Map<String, Object>> imageList = dayMapper.selectImageList(tempImageList);
	            	lowCount += databaseSourceVideo.updateImageAuditStatus(IC_CODE, imageList);
	            	tempImageList.clear();
	            }
	        }
			if(tempImageList != null && tempImageList.size() > 0) {
            	List<Map<String, Object>> imageList = dayMapper.selectImageList(tempImageList);
            	lowCount += databaseSourceVideo.updateImageAuditStatus(IC_CODE, imageList);
			}
		}
		else {
			log.info("setImageAuditStatus: Get 0 size of ImageDB non-audit");
		}
		return lowCount;
	}
}
