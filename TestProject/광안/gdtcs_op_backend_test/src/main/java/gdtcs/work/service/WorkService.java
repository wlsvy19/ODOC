package gdtcs.work.service;

import java.util.List;
import java.util.Map;

/**
 * 근무관리에 관한 서비스 클래스
 *
 * @author 이진표
 * @since 2024.05.27
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  == 개정이력(Modification Information) ==
 *
 *       수정일               수정자                     수정내용
 *  ----------------    ------------    ---------------------------
 *    2025.05.27            이진표                   최초 생성
 *
 *      </pre>
 */

public interface WorkService {

    List<Map<String, Object>> getWorkStat(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getWorkFin(Map<String, Object> workFinParam) throws Exception;

    List<Map<String, Object>> setWorkFinForce(Map<String, Object> param) throws Exception;

    int setWorkFinForceTCS(Map<String, Object> param);

    List<Map<String, Object>> getPassPrim(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getPassPrimDtl(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getWorkNm(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getWorkDocWorkNm(Map<String, Object> param) throws Exception;

    Map<String, Object> getPassPrimDate(Map<String, Object> param) throws Exception;

    Map<String, Object> getWorkDocInfo(Map<String, Object> param) throws Exception;

    Map<String, Object> getWorkDoc(Map<String, Object> param) throws Exception;

    Map<String, Object> getWorkDocCar(Map<String, Object> param) throws Exception;

}
