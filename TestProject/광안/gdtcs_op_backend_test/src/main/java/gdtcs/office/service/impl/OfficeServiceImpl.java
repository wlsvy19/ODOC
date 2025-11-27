package gdtcs.office.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import gdtcs.util.RedisCacheUtil;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdtcs.office.mapper.OfficeMapper;
import gdtcs.office.service.OfficeService;
import gdtcs.util.ResponseCode;
import gdtcs.util.TCSException;

@Service("officeService")
public class OfficeServiceImpl  extends EgovAbstractServiceImpl implements OfficeService{

	/*@Autowired
    private OfficeMapper officeMapper;*/

	@Resource(name = "officeMapper")
	private OfficeMapper officeMapper;

	@Autowired
	private RedisCacheUtil redisCacheUtil;


	@Override
	public List<Map<String, Object>> getViolationList(Map<String, Object> param) throws Exception {
		System.out.println("##### getViolationList param = " + param);

		return redisCacheUtil.getOrSet(
				"getViolationList",
				param,
				1, TimeUnit.DAYS,
				() -> officeMapper.selectViolationList(param)
		);
	}

	@Override
	public List<Map<String, Object>> getViolationHistList(Map<String, Object> param) throws Exception {
		return officeMapper.selectViolationHistList(param);
	}
	@Override
	public Map<String, Object> chkDayFin(List<Map<String, Object>> param) throws Exception {
		return officeMapper.checkDayFin(param);
	}
	@Override
	public List<Map<String, Object>> getLcarPl(List<Map<String, Object>> param) throws Exception {
		return officeMapper.selectLcarPl(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> setViolationAudit(Map<String, Object> param) throws Exception {

		String locCoDiv = (String) param.get("LOC_CO_DIV");
		String requestId = (String) param.get("REQUEST_ID");
		Map<String, Object> item = (Map<String, Object>) param.get("item");

		int successCount = 0;

		if ("0".equals(locCoDiv) || "1".equals(locCoDiv)) {
			successCount = setLocationCorrection(locCoDiv, requestId, item);
			param.put("successCount", successCount);

			if (successCount != 1) {
				throw new TCSException(ResponseCode.INSERT_FAIL);
			}
		}

		officeMapper.updateViolationAudit(param);
		return ((List<Map<String, Object>>) param.get("output")).get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> setViolationAuditBatch(List<Map<String, Object>> param) throws Exception {

		int successCount = 0;
		int failureCount = 0;

		try {
			for (Map<String, Object> auditData : param) {
				// 프로시저 호출
				officeMapper.updateViolationAudit(auditData);

				// 프로시저에서 반환된 결과 확인
				List<Map<String, Object>> outputList = (List<Map<String, Object>>) auditData.get("output");

				if (outputList != null && !outputList.isEmpty()) {
					for (Map<String, Object> output : outputList) {
						String code = (String) output.get("CODE");
						if ("SUCCESS".equals(code)) {
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
			}

			// 처리 결과 반환 (성공, 실패 건수)
			return Map.of("SUCCESS_COUNT", successCount, "FAILURE_COUNT", failureCount);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("프로시저 실행 중 문제가 발생했습니다: " + e.getMessage());
		}

	}
	@Override
	public List<Map<String, Object>> getEcardBl(Map<String, Object> param) throws Exception {
		return officeMapper.selectEcardBl(param);
	}
	@Override
	public List<Map<String, Object>> getExemptPl(Map<String, Object> param) throws Exception {
		return officeMapper.selectExemptPl(param);
	}
	@Override
	public List<Map<String, Object>> getMemberInfo(Map<String, Object> param) throws Exception {
		return officeMapper.selectMemberInfo(param);
	}
	@Override
	public Map<String, Object> getLocationSearchResultDetail(Map<String, Object> param) throws Exception {
		return officeMapper.selectLocationSearchResultDetail(param);
	}
	/* 위반심사 - 위치정보 심사처리 */
	@SuppressWarnings("serial")
	public int setLocationCorrection(String lcCoDiv, String requestId, Map<String, Object> item) throws Exception {
		int updatedCount = 0;
		if (lcCoDiv.equals("1")) {
			// lcCoDiv: 1(광안)
			// requestId: 2(감면), 3(부정)
			// item: IC_CODE, WORK_DATE, WORK_NO, HAND_SNO, HAND_CAR_NO
			Map<String, Object> params = new HashMap<>() {{
				put("requestId", requestId);
				put("item", item);
			}};
			updatedCount = officeMapper.updateLocationCorrectionBs(params);
		} else if (lcCoDiv.equals("0")) {
			// lcCoDiv: 0(도로공사)
			// requestId: 2(감면), 3(부정)
			// item: IC_CODE, WORK_DATE, WORK_NO, HAND_SNO
			//		 ECARD_TYPE, PASS_FARE, PAY_FARE, EXM_NOTE, NEW_EXMT_NOT_RES, NEW_CAR_NO
			Map<String, Object> params = new HashMap<>() {{
				put("requestId", requestId);
				put("data", List.of(item));
			}};
			Map<String, Object> res = setReductionExCorrectionWelfare(params);
			updatedCount = (int) res.get("successCount");
		}
		return updatedCount;
	}

	@Override
	public List<Map<String, Object>> getImageList(Map<String, Object> param) throws Exception {
		return officeMapper.selectImageList(param);
	}
	@Override
	public Map<String, Object> getImageDetail(Map<String, Object> param) throws Exception {
		return officeMapper.selectImageDetail(param);
	}
	@Override
	public List<Map<String, Object>> getImageHistList(Map<String, Object> param) throws Exception {
		return officeMapper.selectImageHistList(param);
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	@SuppressWarnings({ "unchecked", "serial" })
	public Map<String, Object> setImageAudit(Map<String, Object> param) throws Exception {
		Map<String, Object> checkParam = new HashMap<String, Object>() {{
			put("item", param);
		}};
		if (officeMapper.selectTableProcDayfininfo(checkParam) != 0) {
			throw new TCSException(ResponseCode.CLOSED, "officeMapper.selectTableProcDayfininfo");
		}
		officeMapper.updateImageAudit(param);
		List<Map<String, Object>> output = (List<Map<String, Object>>) param.get("output");
		Map<String, Object> res = output.get(0);
		if (res.get("CODE").equals("00")) {
			int insertCount = officeMapper.insertImageHist(param);
			if (insertCount != 1) {
				throw new TCSException(ResponseCode.INSERT_FAIL, "officeMapper.insertImageHist");
			}
		} else if (res.get("CODE").equals("91")) {
			throw new TCSException(ResponseCode.IMG_CORR_RECOMMENDATION_VLTN, "officeMapper.insertImageHist");
		} else if (res.get("CODE").equals("92")) {
			throw new TCSException(ResponseCode.IMG_CORR_HAS_HISTORY_VLTN, "officeMapper.insertImageHist");
		} else if (res.get("CODE").equals("93")) {
			throw new TCSException(ResponseCode.IMG_CORR_HAS_HISTORY, "officeMapper.insertImageHist");
		}
		return res;
	}
	@Override
	public List<Map<String, Object>> getImageCorrectionResultList(Map<String, Object> param) throws Exception {
		return officeMapper.selectImageCorrectionResultList(param);
	}
	@Override
	public List<Map<String, Object>> getProcessList(Map<String, Object> param) throws Exception {
		return officeMapper.selectProcessList(param);
	}

//	@Override
//	public List<Map<String, Object>> getProcessList(Map<String, Object> param) throws Exception {
//		System.out.println("##### 차량처리내역조회 param = " + param);
//		// Redis 키 생성 (파라미터를 이용한 고유 키 생성)
//		String redisKey = generateRedisKey(param);
//		// Redis에서 캐시된 데이터 조회
//		List<Map<String, Object>> cachedData = redisTemplate.opsForValue().get(redisKey);
//		if (cachedData != null) {
//			// 캐시된 데이터가 있으면 바로 반환
//			System.out.println("Returning data from Redis cache for key: " + redisKey);
//
//			return cachedData;
//		}
//
//		// 캐시된 데이터가 없으면 DB에서 조회
//		List<Map<String, Object>> result = officeMapper.selectProcessList(param);
//		redisTemplate.opsForValue().set(redisKey, result, 1, TimeUnit.DAYS); // TTL 1일 <- 우선순위 1
//		return result;
//	}

	@Override
	public List<Map<String, Object>> getReductionExEcardList(Map<String, Object> param) throws Exception {
		return officeMapper.selectReductionExEcardList(param);
	}
	@Override
	public List<Map<String, Object>> getReductionExObuList(Map<String, Object> param) throws Exception {
		return officeMapper.selectReductionExObuList(param);
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> setReductionExCorrection(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		officeMapper.updateReductionExCorrection(param);
		int successCount = (int) param.get("successCount");
		int failCount = (int)param.get("failCount");
		if(failCount == 0) {
			officeMapper.updateTablePrimHandcarno(param);
			int updateCount = (int)param.get("updateRows");
			if (updateCount == 0) {
				throw new TCSException(ResponseCode.NO_DATA_CANCEL, ResponseCode.NO_DATA_CANCEL.getMessage());
			} else if (updateCount > 1 && !param.get("requestId").equals("4")) {
				throw new TCSException(ResponseCode.DUPLICATED_FAIL, ResponseCode.DUPLICATED_FAIL.getMessage());
			} else if (updateCount < 0) {
				throw new TCSException(ResponseCode.UNKNOWN_ERROR, ResponseCode.UNKNOWN_ERROR.getMessage());
			}
		}
		res.put("successCount", successCount);
		res.put("failCount", failCount);
		return res;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> setReductionExCorrectionWelfare(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		officeMapper.updateReductionExCorrectionWelfare(param);
		int successCount = (int) param.get("successCount");
		int failCount = (int)param.get("failCount");

		if(failCount == 0) {
			officeMapper.updateTablePrimHandcarno(param);
			int updateCount = (int)param.get("updateRows");
			if (updateCount == 0) {
				throw new TCSException(ResponseCode.NO_DATA_CANCEL, ResponseCode.NO_DATA_CANCEL.getMessage());
			} else if (updateCount > 1 && !param.get("requestId").equals("4")) {
				throw new TCSException(ResponseCode.DUPLICATED_FAIL, ResponseCode.DUPLICATED_FAIL.getMessage());
			} else if (updateCount < 0) {
				throw new TCSException(ResponseCode.UNKNOWN_ERROR, ResponseCode.UNKNOWN_ERROR.getMessage());
			}
		}
		res.put("successCount", successCount);
		res.put("failCount", failCount);
		return res;
	}
	@Override
	public List<Map<String, Object>> getReductionExWelfareList(Map<String, Object> param) throws Exception {
		return officeMapper.selectReductionExWelfareList(param);
	}
	@Override
	public List<Map<String, Object>> getCheatReductionExList(Map<String, Object> param) throws Exception {
		return officeMapper.selectCheatReductionExList(param);
	}
	@Override
	public List<Map<String, Object>> getReductionBsList(Map<String, Object> param) throws Exception {
		return officeMapper.selectReductionBsList(param);
	}
	@Override
	public Map<String, Object> getReductionBsDetail(Map<String, Object> param) throws Exception {
		return officeMapper.selectReductionBsDetail(param);
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> setReductionBsCorrection(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> correctionParam = new HashMap<String, Object>();
		// 일괄부정처리: rquestId = 5
		String requestId = (String) param.get("requestId");
		if (requestId.equals("5")) {
			correctionParam.put("requestId", "3");
		} else {
			correctionParam.put("requestId", requestId);
		}
		correctionParam.put("data", param.get("data"));
		officeMapper.updateReductionBsCorrection(correctionParam);
		int successCount = (int) correctionParam.get("successCount");
		int failCount = (int)correctionParam.get("failCount");
		if (!requestId.equals("5")) {
			if(failCount == 0) {
				officeMapper.updateTablePrimHandcarno(param);
				int updateCount = (int)param.get("updateRows");
				if (updateCount == 0) {
					throw new TCSException(ResponseCode.NO_DATA_CANCEL, "officeMapper.updateTablePrimHandcarno");
				} else if (updateCount > 1 && (!requestId.equals("4"))) {
					throw new TCSException(ResponseCode.DUPLICATED_FAIL, "officeMapper.updateTablePrimHandcarno");
				} else if (updateCount < 0) {
					throw new TCSException(ResponseCode.UNKNOWN_ERROR, "officeMapper.updateTablePrimHandcarno");
				}
			}
		} else {
			officeMapper.updateTablePrimHandcarno(param);
		}
		res.put("successCount", successCount);
		res.put("failCount", failCount);
		return res;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> setReductionBsCorrectionTaxi(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		Object successCount = 0;
		Object failCount = 0;
		param.put("data", List.of(param.get("item")));
		if (officeMapper.selectTableProcDayfininfo(param) == 0) {
			officeMapper.updateReductionBsCorrection(param);
			if (param.get("requestId").equals("2")) {
				boolean isChanged = param.get("isChanged") == null ? false : (boolean) param.get("isChanged");
				if (isChanged) {
					officeMapper.deleteReductionBsAdditionalProcessTaxi(param);
				} else {
					officeMapper.insertReductionBsAdditionalProcessTaxi(param);
				}
			} else {
				officeMapper.deleteReductionBsAdditionalProcessTaxi(param);
			}
			officeMapper.updateTablePrimHandcarno(param);
			int updateCount = (int)param.get("updateRows");
			if (updateCount == 0) {
				throw new TCSException(ResponseCode.NO_DATA_CANCEL, "officeMapper.updateTablePrimHandcarno");
			} else if (updateCount > 1) {
				throw new TCSException(ResponseCode.DUPLICATED_FAIL, "officeMapper.updateTablePrimHandcarno");
			} else if (updateCount < 0) {
				throw new TCSException(ResponseCode.UNKNOWN_ERROR, "officeMapper.updateTablePrimHandcarno");
			}
			successCount = (Object) param.get("successCount");
		} else {
			failCount = 1;
		}
		res.put("successCount", successCount);
		res.put("failCount", failCount);
		return res;
	}
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> setReductionBsCorrectionTaxiBatch(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("successCount", 0);
		res.put("failCount", 0);
		List<Map<String, Object>> data = (List<Map<String, Object>>) param.get("data");
		for (Map<String, Object> item : data) {
			item.put("NEW_CAR_NO", item.get("HAND_CAR_NO"));
			Map<String, Object> correctionParam = new HashMap<String, Object>();
			correctionParam.put("requestId", param.get("requestId"));
			correctionParam.put("adminId", param.get("adminId"));
			correctionParam.put("item", item);
			Map<String, Object> resVal = setReductionBsCorrectionTaxi(correctionParam);
			res.put("successCount", (int) res.get("successCount") + (int) resVal.get("successCount")) ;
			res.put("failCount", (int) res.get("failCount") + (int) resVal.get("failCount")) ;
		}
		return res;
	}
	@Override
	public List<Map<String, Object>> getCheatReductionBsList(Map<String, Object> param) throws Exception {
		return officeMapper.selectCheatReductionBsList(param);
	}
	@Override
	public List<Map<String, Object>> getPayCorrectionList(Map<String, Object> param) throws Exception {
		return officeMapper.selectPayCorrectionList(param);
	}
	@Override
	public List<Map<String, Object>> getPreRegistrationList(Map<String, Object> param) throws Exception {
		return officeMapper.selectPreRegistrationList(param);
	}
	@Override
	public List<Map<String, Object>> getEtcIncomeList(Map<String, Object> param) throws Exception {
		return officeMapper.selectEtcIncomeList(param);
	}
	@Override
	public List<Map<String, Object>> getWorkerList(Map<String, Object> param) throws Exception {
		return officeMapper.selectWorkerList(param);
	}
	@Override
	public int chkEtcIncome(Map<String, Object> param) throws Exception {
		return officeMapper.checkEtcIncome(param);
	}
	@Override
	public int addEtcIncome(Map<String, Object> param) throws Exception {
		return officeMapper.insertEtcIncome(param);
	}
	@Override
	public int delEtcIncome(List<Map<String, Object>> param) throws Exception {
		return officeMapper.deleteEtcIncome(param);
	}
	@Override
	public int setEtcIncome(Map<String, Object> param) throws Exception {
		return officeMapper.updateEtcIncome(param);
	}
	@Override
	public List<Map<String, Object>> getWorkNo(Map<String, Object> param) throws Exception {
		return officeMapper.selectWorkNo(param);
	}
	@Override
	public List<Map<String, Object>> getLaneNo(Map<String, Object> param) throws Exception {
		return officeMapper.selectLaneNo(param);
	}
	@Override
	public List<Map<String, Object>> getViolationState(Map<String, Object> param) throws Exception {
		return officeMapper.selectViolationState(param);
	}

	@Override
	public List<Map<String, Object>> getCorrectionCount(Map<String, Object> param) {
		return officeMapper.selectCorrectionCount(param);
	}
}
