package gdtcs.unpaid.controller;

import gdtcs.unpaid.service.UnPaidService;

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
@RequestMapping("/api/unpaid")
public class UnPaidController {

    @Resource(name="unPaidService")
    private UnPaidService unPaidService;
    
    
    @PostMapping("/getUnPaidCar")
    public ResponseEntity<List<Map<String, Object>>> getUnPaidCar(@RequestBody Map<String, Object> param) throws Exception {

        List<Map<String, Object>> list = unPaidService.getUnPaidCar(param);
  
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getUnPaidCollect")
    public ResponseEntity<List<Map<String, Object>>> getUnPaidCollect(@RequestBody Map<String, Object> param) throws Exception {
        
        List<Map<String, Object>> list = unPaidService.getUnPaidCollect(param);
    
        return ResponseEntity.ok(list);
    }

   
    @PostMapping("/getLineCtrlOfficePayList")
    public ResponseEntity<List<Map<String, Object>>> getLineCtrlOfficePayList(@RequestBody Map<String, Object> param) throws Exception {

    	List<Map<String, Object>> list = unPaidService.getLineCtrlOfficePayList(param);

        return ResponseEntity.ok(list);
    }

}
