package gdtcs.traffic.controller;

import gdtcs.traffic.service.TrafficService;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/traffic")
public class TrafficController {

    @Resource(name="trafficService")
    private TrafficService trafficService;
    
    @PostMapping("/getCarLineTraffic")
    public ResponseEntity<List<Map<String, Object>>> getCarLineTraffic(@RequestBody Map<String, Object> param) throws Exception {
   
        List<Map<String, Object>> list = trafficService.getCarLineTraffic(param);
      
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getCarTypeTraffic")
    public ResponseEntity<List<Map<String, Object>>> getCarTypeTraffic(@RequestBody Map<String, Object> param) throws Exception {
   
        List<Map<String, Object>> list = trafficService.getCarTypeTraffic(param);
      
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getDataReceive")
    public ResponseEntity<List<Map<String, Object>>> getDataReceive(@RequestBody Map<String, Object> param) throws Exception {
   
        List<Map<String, Object>> list = trafficService.getDataReceive(param);
      
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficDaily")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDaily(@RequestBody Map<String, Object> param) throws Exception {
   
        List<Map<String, Object>> list = trafficService.getTrafficDaily(param);
      
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficDirectionPeriod")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDirectionPeriod(@RequestBody Map<String, Object> param) throws Exception {
    	
    	List<Map<String, Object>> list = trafficService.getTrafficDirectionPeriod(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficDirectionDay")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDirectionDay(@RequestBody Map<String, Object> param) throws Exception {

        List<Map<String, Object>> list = trafficService.getTrafficDirectionDay(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficHour")
    public ResponseEntity<List<Map<String, Object>>> getTrafficHour(@RequestBody Map<String, Object> param)  throws Exception{
    	
        List<Map<String, Object>> list = trafficService.getTrafficHour(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficDailyDirection")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDailyDirection(@RequestBody Map<String, Object> param) throws Exception {
    	 
    	List<Map<String, Object>> list = trafficService.getTrafficDailyDirection(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficDailyPayDiv")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDailyPayDiv(@RequestBody Map<String, Object> param) throws Exception{

        List<Map<String, Object>> list = trafficService.getTrafficDailyPayDiv(param);
   
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficLine")
    public ResponseEntity<List<Map<String, Object>>> getTrafficLine(@RequestBody Map<String, Object> param) throws Exception {
    	
        List<Map<String, Object>> list = trafficService.getTrafficLine(param);
    
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTrafficLineHour")
    public ResponseEntity<List<Map<String, Object>>> getTrafficLineHour(@RequestBody Map<String, Object> param) throws Exception {
    	
    	List<Map<String, Object>> list = trafficService.getTrafficLineHour(param);
       
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficPreMonth")
    public ResponseEntity<List<Map<String, Object>>> getTrafficPreMonth(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficPreMonth(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficMonth")
    public ResponseEntity<List<Map<String, Object>>> getTrafficMonth(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficMonth(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficYear")
    public ResponseEntity<List<Map<String, Object>>> getTrafficYear(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficYear(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficDayOfWeek")
    public ResponseEntity<List<Map<String, Object>>> getTrafficDayOfWeek(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficDayOfWeek(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficQuarter")
    public ResponseEntity<List<Map<String, Object>>> getTrafficQuarter(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficQuarter(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficYearDayOfWeek")
    public ResponseEntity<List<Map<String, Object>>> getTrafficYearDayOfWeek(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficYearDayOfWeek(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getTrafficHourHoliday")
    public ResponseEntity<List<Map<String, Object>>> getTrafficHourHoliday(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getTrafficHourHoliday(param);
    	
    	return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getDailyHipassUsageStatus")
    public ResponseEntity<List<Map<String, Object>>> getDailyHipassUsageStatus(@RequestBody Map<String, Object> param) throws Exception {
    	List<Map<String, Object>> list = trafficService.getDailyHipassUsageStatus(param);
    	
    	return ResponseEntity.ok(list);
    }
}
