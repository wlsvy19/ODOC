package gdtcs.card.mapper;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;
import java.util.Map;

@Mapper("cardMapper")
public interface CardMapper {
	
	List<Map<String, Object>> getCardCoCode(Map<String, Object> param);
	List<Map<String, Object>> getCardUseList(Map<String, Object> param);
    List<Map<String, Object>> getTcardBL(Map<String, Object> param);
    List<Map<String, Object>> getEcardBL(Map<String, Object> param);
    List<Map<String, Object>> getEcardBLHist(Map<String, Object> param);
    List<Map<String, Object>> getLightCarPL(Map<String, Object> param);
	int updateLightCarPL(Map param);
	int deleteLightCarPL(Map param);
    List<Map<String, Object>> getIntewelfCardPL(Map<String, Object> param);
    int updateIntewelfCardPL(Map param);
	int deleteIntewelfCardPL(Map param);
	int deleteIntewelfCardPLCHG(Map param);
    List<Map<String, Object>> getTcardBIN(Map<String, Object> param);
    List<Map<String, Object>> getEcardBINManage(Map<String, Object> param);
    int updateEcardBINManage(Map param);
    int updateBinFile(Map param);
	int deleteEcardBINManage(Map param);
    List<Map<String, Object>> getCardCoDivPayList(Map<String, Object> param);
    List<Map<String, Object>> getCptLpayEcardCardCoDiv(Map<String, Object> param);
    List<Map<String, Object>> getExemPLBusan(Map<String, Object> param);
    int updateExemPLADDR(String CAR_NO, String ADDRESS);
}
