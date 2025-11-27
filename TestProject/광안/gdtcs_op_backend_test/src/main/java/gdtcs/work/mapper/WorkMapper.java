package gdtcs.work.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

/**
 * 근무관리에 관한 데이터처리 매퍼 클래스
 *
 * @author 이진표
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *       수정일               수정자                     수정내용
 *  ----------------    ------------    ---------------------------
 *    2025.05.27            이진표                   최초 생성
 *
 *      </pre>
 * @since 2024.05.27
 */

@Mapper("workMapper")
public interface WorkMapper {

    List<Map<String, Object>> selectWorkStat(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectWorkFin(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> updateWorkFinForce(Map<String, Object> param);

    int updateWorkFinForceTCS(Map<String, Object> param);

    List<Map<String, Object>> selectPassPrim(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectPassPrimDtl(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectWorkNm(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> selectWorkDocWorkNm(Map<String, Object> param) throws Exception;

    Map<String, Object> selectPassPrimDate(Map<String, Object> param) throws Exception;

    Map<String, Object> selectWorkDocInfo(Map<String, Object> param) throws Exception;

    Map<String, Object> selectWorkDoc(Map<String, Object> param) throws Exception;

    Map<String, Object> selectWorkDocCar(Map<String, Object> param) throws Exception;

}
