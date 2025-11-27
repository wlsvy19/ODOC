package gdtcs.inter.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("interMapper")
public interface InterMapper {

    Map<String, Object> selectInter(Map<String, Object> param) throws Exception;

    Map<String, Object> selectTaxi(Map<String, Object> param) throws Exception;

    Map<String, Object> selectPreRegi(Map<String, Object> param) throws Exception;

    Map<String, Object> selectIsDayFin(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectCenterInter(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectFileDivNm(Map<String, Object> param) throws Exception;

    void insertInter(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> transTaxiPre(Map<String, Object> param) throws Exception;

    void transTaxiPreTraffic(Map<String, Object> param) throws Exception;

    int checkIsHoliday(Map<String, Object> param) throws Exception;
}
