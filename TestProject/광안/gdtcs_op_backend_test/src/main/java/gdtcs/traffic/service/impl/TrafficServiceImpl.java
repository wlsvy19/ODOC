package gdtcs.traffic.service.impl;

import gdtcs.traffic.mapper.TrafficMapper;
import gdtcs.traffic.service.TrafficService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("trafficService")
public class TrafficServiceImpl extends EgovAbstractServiceImpl implements TrafficService {

    @Resource(name="trafficMapper")
    private TrafficMapper trafficMapper;
    
    @Override
    public List<Map<String, Object>> getCarLineTraffic(Map<String, Object> param) throws Exception {
        return trafficMapper.getCarLineTraffic(param);
        
    }
    @Override
    public List<Map<String, Object>> getCarTypeTraffic(Map<String, Object> param) throws Exception {
        return trafficMapper.getCarTypeTraffic(param);
        
    }
    @Override
    public List<Map<String, Object>> getDataReceive(Map<String, Object> param) throws Exception {
        return trafficMapper.getDataReceive(param);
        
    }
    @Override
    public List<Map<String, Object>> getTrafficDaily(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficDaily(param);
    }

    @Override
    public List<Map<String, Object>> getTrafficDirectionPeriod(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficDirectionPeriod(param);
    }

    @Override
    public List<Map<String, Object>> getTrafficDirectionDay(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficDirectionDay(param);
    }

    @Override
    public List<Map<String, Object>> getTrafficHour(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficHour(param);
    }

    @Override
    public List<Map<String, Object>> getTrafficDailyDirection(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficDailyDirection(param);
    }
    @Override
    public List<Map<String, Object>> getTrafficDailyPayDiv(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficDailyPayDiv(param);
    }

    @Override
    public List<Map<String, Object>> getTrafficLine(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficLine(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficLineHour(Map<String, Object> param) throws Exception {
        return trafficMapper.getTrafficLineHour(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficPreMonth(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficPreMonth(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficMonth(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficMonth(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficYear(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficYear(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficDayOfWeek(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficDayOfWeek(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficQuarter(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficQuarter(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficYearDayOfWeek(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficYearDayOfWeek(param);
    }
    
    @Override
    public List<Map<String, Object>> getTrafficHourHoliday(Map<String, Object> param) throws Exception {
        return trafficMapper.selectTrafficHourHoliday(param);
    }
    
    @Override
    public List<Map<String, Object>> getDailyHipassUsageStatus(Map<String, Object> param) throws Exception {
        return trafficMapper.selectDailyHipassUsageStatus(param);
    }
}
