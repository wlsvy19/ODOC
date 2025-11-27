package gdtcs.card.controller;

import gdtcs.card.service.CardService;
import gdtcs.card.vo.PACKET_FOCUS_RESPONSE;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api/card")
public class CardController {

    @Resource(name="cardService")
    private CardService cardService;
    
    @PostMapping("/getCardCoCode")
    public ResponseEntity<List<Map<String, Object>>> getCardCoCode(@RequestBody Map<String, Object> param) throws Exception {
   
    	List<Map<String, Object>> list = cardService.getCardCoCode(param);
        
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getCardUseList")
    public ResponseEntity<List<Map<String, Object>>> getCardUseList(@RequestBody Map<String, Object> param) throws Exception {
   
    	List<Map<String, Object>> list = cardService.getCardUseList(param);
        
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/getEcardBL")
    public ResponseEntity<List<Map<String, Object>>> getEcardBL(@RequestBody Map<String, Object> param) throws Exception{
        
    	List<Map<String, Object>> list = cardService.getEcardBL(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getTcardBL")
    public ResponseEntity<List<Map<String, Object>>> getTcardBL(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getTcardBL(param);
  
        return ResponseEntity.ok(list);
     
    
    }

   
    @PostMapping("/getEcardBLHist")
    public ResponseEntity<List<Map<String, Object>>> getEcardBLHist(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getEcardBLHist(param);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/getLightCarPL")
    public ResponseEntity<List<Map<String, Object>>> getLightCarPL(@RequestBody Map<String, Object> param) throws Exception{
    	
    	List<Map<String, Object>> list = cardService.getLightCarPL(param);
     
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/addLightCarPL")
	public ResponseEntity<Map<String, Object>> addLightCarPL(@RequestBody Map<String, Object> param) throws Exception{
        int result = cardService.addLightCarPL(param);
        
        Map<String, Object> resultMap = new HashMap<>();
        
        switch (result) {
		case 0: 
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "추가할 수 없습니다.");
            break;
		case -1: 
			resultMap.put("ERROR_CODE", "-1");
			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
			break;
		case -2: 
			resultMap.put("ERROR_CODE", "-2");
			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
			break;
		default:
			resultMap.put("ERROR_CODE", result);
			resultMap.put("ERROR_MSG", "추가하였습니다.");
	        break;
		}

        return ResponseEntity.ok(resultMap);
    }
	
	@PostMapping("/delLightCarPL")
	public ResponseEntity<Map<String, Object>> delLightCarPL(@RequestBody Map<String, Object> param) throws Exception{
        int result = cardService.delLightCarPL(param);
        
        Map<String, Object> resultMap = new HashMap<>();
        
        switch (result) {
		case 0: 
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "삭제할 수 없습니다.");
            break;
		case -1: 
			resultMap.put("ERROR_CODE", "-1");
			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
			break;
		case -2: 
			resultMap.put("ERROR_CODE", "-2");
			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
			break;
		default:
			resultMap.put("ERROR_CODE", result);
			resultMap.put("ERROR_MSG", "삭제하였습니다.");
	        break;
		}

        return ResponseEntity.ok(resultMap);
    }
	
    
    @PostMapping("/getIntewelfCardPL")
    public ResponseEntity<List<Map<String, Object>>> getIntewelfCardPL(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getIntewelfCardPL(param);
       
        return ResponseEntity.ok(list);
    }
    
    @PostMapping("/addIntewelfCardPL")
   	public ResponseEntity<Map<String, Object>> addIntewelfCardPL(@RequestBody Map<String, Object> param) throws Exception{
           int result = cardService.addIntewelfCardPL(param);
           
           Map<String, Object> resultMap = new HashMap<>();
           
           switch (result) {
   		case 0: 
   			resultMap.put("ERROR_CODE", "0");
   			resultMap.put("ERROR_MSG", "추가할 수 없습니다.");
               break;
   		case -1: 
   			resultMap.put("ERROR_CODE", "-1");
   			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
   			break;
   		case -2: 
   			resultMap.put("ERROR_CODE", "-2");
   			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
   			break;
   		default:
   			resultMap.put("ERROR_CODE", result);
   			resultMap.put("ERROR_MSG", "추가하였습니다.");
   	        break;
   		}

           return ResponseEntity.ok(resultMap);
       }
   	
   	@PostMapping("/delIntewelfCardPL")
   	public ResponseEntity<Map<String, Object>> delIntewelfCardPL(@RequestBody Map<String, Object> param) throws Exception{
           int result = cardService.delIntewelfCardPL(param);
           
           Map<String, Object> resultMap = new HashMap<>();
           
           switch (result) {
   		case 0: 
   			resultMap.put("ERROR_CODE", "0");
   			resultMap.put("ERROR_MSG", "삭제할 수 없습니다.");
               break;
   		case -1: 
   			resultMap.put("ERROR_CODE", "-1");
   			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
   			break;
   		case -2: 
   			resultMap.put("ERROR_CODE", "-2");
   			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
   			break;
   		default:
   			resultMap.put("ERROR_CODE", result);
   			resultMap.put("ERROR_MSG", "삭제하였습니다.");
   	        break;
   		}

           return ResponseEntity.ok(resultMap);
       }
    
    @PostMapping("/getTcardBIN")
    public ResponseEntity<List<Map<String, Object>>> getTcardBIN(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getTcardBIN(param);
     
        return ResponseEntity.ok(list);
    }
    @PostMapping("/getEcardBINManage")
    public ResponseEntity<List<Map<String, Object>>> getEcardBINManage(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getEcardBINManage(param);
        
        return ResponseEntity.ok(list);
    }
    @PostMapping("/addEcardBINManage")
   	public ResponseEntity<Map<String, Object>> addEcardBINManage(@RequestBody Map<String, Object> param) throws Exception{
           int result = cardService.addEcardBINManage(param);
           
           Map<String, Object> resultMap = new HashMap<>();
           
           switch (result) {
   		case 0: 
   			resultMap.put("ERROR_CODE", "0");
   			resultMap.put("ERROR_MSG", "추가할 수 없습니다.");
               break;
   		case -1: 
   			resultMap.put("ERROR_CODE", "-1");
   			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
   			break;
   		case -2: 
   			resultMap.put("ERROR_CODE", "-2");
   			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
   			break;
   		default:
   			resultMap.put("ERROR_CODE", result);
   			resultMap.put("ERROR_MSG", "추가하였습니다.");
   	        break;
   		}

           return ResponseEntity.ok(resultMap);
       }
   	
   	@PostMapping("/delEcardBINManage")
   	public ResponseEntity<Map<String, Object>> delEcardBINManage(@RequestBody Map<String, Object> param) throws Exception{
           int result = cardService.delEcardBINManage(param);
           
           Map<String, Object> resultMap = new HashMap<>();
           
           switch (result) {
   		case 0: 
   			resultMap.put("ERROR_CODE", "0");
   			resultMap.put("ERROR_MSG", "삭제할 수 없습니다.");
               break;
   		case -1: 
   			resultMap.put("ERROR_CODE", "-1");
   			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
   			break;
   		case -2: 
   			resultMap.put("ERROR_CODE", "-2");
   			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
   			break;
   		default:
   			resultMap.put("ERROR_CODE", result);
   			resultMap.put("ERROR_MSG", "삭제하였습니다.");
   	        break;
   		}

           return ResponseEntity.ok(resultMap);
       }
    
    @PostMapping("/getCardCoDivPayList")
    public ResponseEntity<List<Map<String, Object>>> getCardCoDivPayList(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getCardCoDivPayList(param);
      
        return ResponseEntity.ok(list);
    }
    @PostMapping("/getCptLpayEcardCardCoDiv")
    public ResponseEntity<List<Map<String, Object>>> getCptLpayEcardCardCoDiv(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getCptLpayEcardCardCoDiv(param);
   
        return ResponseEntity.ok(list);
    }
    @PostMapping("/getExemPLBusan")
    public ResponseEntity<List<Map<String, Object>>> getExemPLBusan(@RequestBody Map<String, Object> param) throws Exception{
    	
        List<Map<String, Object>> list = cardService.getExemPLBusan(param);
      
        return ResponseEntity.ok(list);
    }
    @PostMapping("/addBinFile")
    public ResponseEntity<Map<String, Object>> addBinFile(@RequestBody Map<String, Object> param) throws Exception{
        Map<String, Object> params = new HashMap<>();
        params.put("params", param);
    	int result = cardService.addBinFile(params);  
           
        Map<String, Object> resultMap = new HashMap<>();
                  
           switch (result) {
   		case 0: 
   			resultMap.put("ERROR_CODE", "0");
   			resultMap.put("ERROR_MSG", "추가할 수 없습니다.");
               break;
   		case -1: 
   			resultMap.put("ERROR_CODE", "-1");
   			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
   			break;
   		case -2: 
   			resultMap.put("ERROR_CODE", "-2");
   			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
   			break;
   		default:
   			resultMap.put("ERROR_CODE", result);
   			resultMap.put("ERROR_MSG", "추가하였습니다.");
   	        break;
   		}

           return ResponseEntity.ok(resultMap);
       }
    
    
    /**
   	 *  
   	 * @Method Name	: requestExemPL
   	 * @Method 설명	: 면제PL을 요청한다.
   	 * @작성자 		: 이지형
   	 * @작성일 		: 2024. 09. 27.
   	 *
   	 * @param Map<String, Object> param
   	 * @return ResponseEntity<List<Map<String, Object>>>
   	 * @throws Exception
   	 *
   	 */
    @PostMapping("/requestExemPL")
    public synchronized  ResponseEntity<Map<String, Object>> requestExemPL(@RequestBody Map<String, Object> param) throws Exception{
	   Map<String, Object> result = new HashMap<>();
	   ObjectMapper objectMapper = new ObjectMapper();
	   
	   if(param.get("PACKET_CAR_NO").toString().getBytes("euc-kr").length < 20) {
		  
		    Map<String, Object> PLStat = cardService.requestExemPL(param);

            switch (PLStat.get("ERROR_CODE").toString()) {
				case "0": 
					result.put("ERROR_CODE", "0");
				    result.put("ERROR_MSG", "요청 할 수 없습니다.");
				    break;
				case "-1": 
					result.put("ERROR_CODE", "-1");
				    result.put("ERROR_MSG", "일시적인 국토교통부 시스템 연계 오류입니다.\n 차량번호 입력 후 조회되는 데이터로 면제PL조회를 사용해주세요.");
					break;
				case "-2": 
					result.put("ERROR_CODE", "-2");
				    result.put("ERROR_MSG", "연결에 실패하였습니다.");
					break;
				default: //성공 메세지 출력 없이 결과값 전송
					result.put("ERROR_CODE", "1");
				    result.put("ERROR_MSG", PLStat.get("ERROR_CODE"));
				    
				    Map<String, Object> map = objectMapper.convertValue(PLStat.get("ERROR_CODE"), Map.class);
				    if(map.get("주소") != "") {
				    	cardService.addExemPLADDR(map.get("차량번호").toString(), map.get("주소").toString());
				    }
				    break;
				}
           
           return ResponseEntity.ok(result);
	   } else {
		   result.put("ERROR_CODE", "-3");
           result.put("ERROR_MSG", "차량번호 Byte길이 에러");
           
		   return ResponseEntity.ok(result);
	   }
   }
       
}
