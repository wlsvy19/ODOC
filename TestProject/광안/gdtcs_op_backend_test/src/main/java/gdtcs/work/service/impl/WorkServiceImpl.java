package gdtcs.work.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import gdtcs.work.mapper.WorkMapper;
import gdtcs.work.service.WorkService;

/**
 * 근무관리에 관한 서비스 구현 클래스
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

@Service("workService")
public class WorkServiceImpl extends EgovAbstractServiceImpl implements WorkService {

    @Resource(name = "workMapper")
    private WorkMapper workMapper;

    /**
     * 
     * @Method Name : getWorkStat
     * @Method 설명 : 근무상태를 조회한다
     * @작성자 : 이진표
     * @작성일 : 2024. 07. 01.
     */
    @Override
    public List<Map<String, Object>> getWorkStat(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkStat(param);
    }

    /**
     * 
     * @Method Name : getWorkFin
     * @Method 설명 : 근무마감처리를 위한 데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 10. 02.
     */
    @Override
    public List<Map<String, Object>> getWorkFin(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkFin(param);
    }

    /**
     * 
     * @Method Name : setWorkFinForce
     * @Method 설명 : ETCS 강제근무종료를 한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> setWorkFinForce(Map<String, Object> param) throws Exception {
        workMapper.updateWorkFinForce(param);
        return (List<Map<String, Object>>) param.get("output");
    }

    /**
     * 
     * @Method Name : setWorkFinForceTCS
     * @Method 설명 : TCS 강제근무종료를 한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public int setWorkFinForceTCS(Map<String, Object> param) {
        return workMapper.updateWorkFinForceTCS(param);
    }

    /**
     * 
     * @Method Name : getPassPrim
     * @Method 설명 : 통행원시를 데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public List<Map<String, Object>> getPassPrim(Map<String, Object> param) throws Exception {
        return workMapper.selectPassPrim(param);
    }

    /**
     * 
     * @Method Name : getPassPrimDtl
     * @Method 설명 : 통행원시 상세데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public List<Map<String, Object>> getPassPrimDtl(Map<String, Object> param) throws Exception {
        return workMapper.selectPassPrimDtl(param);
    }

    /**
     * 
     * @Method Name : getWorkNm
     * @Method 설명 : 통행원시시(상세)에서 조회를 위해 근무번호를 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public List<Map<String, Object>> getWorkNm(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkNm(param);
    }

    /**
     * 
     * @Method Name : getWorkDocWorkNm
     * @Method 설명 : 근무확인서에서 종료된 근무를 위해 근무번호를 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public List<Map<String, Object>> getWorkDocWorkNm(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkDocWorkNm(param);
    }

    /**
     * 
     * @Method Name : getPassPrimDate
     * @Method 설명 : 통행원시시(상세)에서 근무번호 선택시 거기에 해당하는 조회시간을 자동으로 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public Map<String, Object> getPassPrimDate(Map<String, Object> param) throws Exception {
        return workMapper.selectPassPrimDate(param);
    }

    /**
     * 
     * @Method Name : getWorkDocInfo
     * @Method 설명 : 근무확인서에서 근무확인서 정보를 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public Map<String, Object> getWorkDocInfo(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkDocInfo(param);
    }

    /**
     * 
     * @Method Name : getWorkDoc
     * @Method 설명 : 근무확인서를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public Map<String, Object> getWorkDoc(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkDoc(param);
    }

    /**
     * 
     * @Method Name : getWorkDocCar
     * @Method 설명 : 근무확인서에서 차종별 내역을 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @Override
    public Map<String, Object> getWorkDocCar(Map<String, Object> param) throws Exception {
        return workMapper.selectWorkDocCar(param);
    }

}
