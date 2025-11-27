package gdtcs.maint.controller;

import gdtcs.maint.service.MaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api/maint")
public class MaintController {

	@Resource(name="maintService")
    private MaintService service;


    @PostMapping("/getEquipErrorList")
    public ResponseEntity<List<Map<String, Object>>> getEquipErrorList(@RequestBody Map<String, Object> param) throws Exception {
    	

        List<Map<String, Object>> list = service.getEquipErrorList(param);
      
        return ResponseEntity.ok(list);
    }
    @PostMapping("/getEquipErrorCum")
    public ResponseEntity<List<Map<String, Object>>> getEquipErrorCum(@RequestBody Map<String, Object> param) throws Exception {
    	
        List<Map<String, Object>> list = service.getEquipErrorCum(param);
       
        return ResponseEntity.ok(list);
    }
    @PostMapping("/getCarLineMonitor")
    public ResponseEntity<List<Map<String, Object>>> getCarLineMonitor(@RequestBody Map<String, Object> param) throws Exception {
    	
        List<Map<String, Object>> list = service.getCarLineMonitor(param);
        
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTableSpaceMonitor")
    public ResponseEntity<List<Map<String, Object>>> getTableSpaceMonitor(@RequestBody Map<String, Object> param) throws Exception {
    	
        List<Map<String, Object>> list = service.getTableSpaceMonitor(param);
      
        return ResponseEntity.ok(list);
    }

  
}
