package gdtcs.card.service;

import java.util.List;
import java.util.Map;

public interface CardService {
	
	List<Map<String, Object>> getCardCoCode(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getCardUseList(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getTcardBL(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getEcardBL(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getEcardBLHist(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getLightCarPL(Map<String, Object> param) throws Exception;

    public int addLightCarPL(Map<String, Object> param) throws Exception; /* 경차 PL 추가 */

	public int delLightCarPL(Map<String, Object> param) throws Exception; /* 경차 PL 삭제 */

    List<Map<String, Object>> getIntewelfCardPL(Map<String, Object> param) throws Exception;

    public int addIntewelfCardPL(Map<String, Object> param) throws Exception; /* 통합복지 PL 수정 */

	public int delIntewelfCardPL(Map<String, Object> param) throws Exception; /* 통합복지 PL 삭제 */

    List<Map<String, Object>> getTcardBIN(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getEcardBINManage(Map<String, Object> param) throws Exception;
    
    public int addEcardBINManage(Map<String, Object> param) throws Exception; /* 전자카드 BIN 관리 PL 추가 및 수정 */

   	public int delEcardBINManage(Map<String, Object> param) throws Exception; /* 전자카드 BIN 관리 PL 삭제 */
   	
   	public int addBinFile(Map<String, Object> param) throws Exception; /* 전자카드 BIN File 추가 */
   	
    List<Map<String, Object>> getCardCoDivPayList(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getCptLpayEcardCardCoDiv(Map<String, Object> param) throws Exception;
    
    List<Map<String, Object>> getExemPLBusan(Map<String, Object> param) throws Exception;
 	public int addExemPLADDR(String CAR_NO, String ADDRESS) throws Exception; /* 면제 PL 주소 추가 */
    public Map<String,Object> requestExemPL(Map param) throws Exception; // 포커스 면제 PL 요청

}
