import axios from 'axios';
import { onActivated, onDeactivated } from 'vue';
import { useMenuStore, useAuthStore, useFareStore, ozAppImageUrl, loadSystemSmallCodeAll } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { ElMessage, ElMessageBox } from 'element-plus';
import Cookies from 'js-cookie';
import dayjs from 'dayjs';

/**
 * Backend API 호출 공통 함수
 * @param {*} method: HTTP 메소드
 * @param {*} url: 백엔드 url
 * @param {*} param: 백엔드에 보낼 파라미터
 * @param {*} timeout: 백엔드 요청시 timeout 기본:2000초
 * @returns
 */
export const request = async (method, url, param = {}, timeout = 2000000) => {
  const menuStore = useMenuStore();
  const token = Cookies.get('gdtcs-auth-token');
  const headers = {};
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  const authStore = useAuthStore();
  const inputMethod = method.toUpperCase() === 'GET';
  const config = {
    method,
    url,
    headers,
    timeout: timeout,
    ...(inputMethod ? { params: param } : { data: param }),
  };

  try {
    const { data } = await axios(config);
    return data;
  } catch (error) {
    const errorCode = {
      400: () => showMessage(`${error.response.data.ERROR_CODE}: ${error.response.data.ERROR_MSG}`, 'error'),
      401: () => {
        authStore.accessToken = null;
        Cookies.remove('gdtcs-auth-token');
        menuStore.openTabs = [];
        alert(`사용자 계정 인증에 문제가 생겼습니다.\n로그인 페이지로 이동합니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
      },
      403: () => alert(`접근 권한이 없습니다.(403 Forbidden)`),
      404: () => alert(`잘못된 요청입니다.(404 Not Found)`),
      500: () => {
        alert(`서버에서 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
        location.reload();
      },
      ECONNABORTED: () => alert(`서버 응답시간이 초과되었습니다.\n문제가 지속된다면 관리자한테 문의하세요.`),
      ERR_BAD_REQUEST: () => alert(`잘못된 요청입니다.`),
      ERR_BAD_RESPONSE: () => alert(`서버에서 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`),
    };
    if (error.response) {
      Object.hasOwn(errorCode, error.response.status) ? errorCode[error.response.status]() : errorCode[error.code]();
    } else if (error.request) {
      errorCode[error.code]();
    } else {
      alert(`처리 실패: ${error.message}`);
    }
  }
};

/**
 * ImageCategory Enum
 */
export const ImageCategory = Object.freeze({
  CAR: 'car',
  PLATE: 'plate',
});

/**
 * 차량 영상 URL을 가져오는 함수
 * @param { string } imageCategory: ImageCategory
 * @param { Object } param: { IC_CODE, WORK_DATE, WORK_NO, HAND_SNO }
 * @returns
 */
export const getImage = (imageCategory, param = {}) => {
  let baseUrl = `${window.location.origin}/api/video/etcs`;
  let url = imageCategory === ImageCategory.CAR ? `${baseUrl}/getImage` : `${baseUrl}/getImagePlate`;
  let params = new URLSearchParams({
    IC_CODE: param.IC_CODE,
    tolling_time: param.WORK_DATE,
    work_number: param.WORK_NO,
    processing_number: param.HAND_SNO,
    filepathStartOffset: 8,
  });
  return `${url}?${params.toString()}`;
};

/**
 * 6개의 버튼 공통 함수
 * @param {} actions: Search, Add, Delete, Save, Print, PowerOff
 */
export const btnHandler = (actions) => {
  const menuStore = useMenuStore();

  onActivated(() => {
    Object.entries(actions).forEach(([action, handler]) => {
      const methodName = `setCurrent${action}Handler`;
      if (typeof menuStore[methodName] === 'function') {
        menuStore[methodName](handler);
      }
    });
  });

  onDeactivated(() => {
    Object.keys(actions).forEach((action) => {
      const methodName = `setCurrent${action}Handler`;
      if (typeof menuStore[methodName] === 'function') {
        menuStore[methodName](null);
      }
    });
  });
};

/**
 *
 * @param { string } data 입력 데이터
 * @param { number } type 1 -> 'yyyy-mm-dd', 2-> 000-00- (사업자번호), 3->
 */
export const formatting = (data, type) => {
  switch (type) {
    case 1: // date yyyy-mm-dd
      return dayjs(data).format('YYYY-MM-DD');
    case 2: // co_no 000-00-
      return data.slice(0, 3) + '-' + data.slice(3, 5) + '-' + data.slice(5);
  }
};

/**
 * 3자리 숫자마다 , 표시 하는 함수
 * @param { 3} num
 * @returns
 */
export const comma = (num) => {
  if (!num && num !== 0) {
    return num;
  }
  const parts = num.toString().split('.');
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');

  if (parts[1]) {
    parts[1] = parts[1].length > 6 ? parts[1].substring(0, 6) : parts[1];
  }

  return parts.join('.');
};

/**
 * DialoginputFormGridComponent data 초기화 함수
 * @param {Array} 헤더 배열({title, key})
 * @returns {Array} 데이터 배열({key, value})
 */
export const initDialogData = (headers) => {
  return headers.reduce((acc, curr) => {
    switch (curr.option) {
      case 'select':
        acc[curr.key] = curr.selectItem[0].value;
        break;
      case 'date':
        acc[curr.key] = dayjs().format('YYYY-MM-DD');
        break;
      case 'numberInput':
        acc[curr.key] = 0;
        break;
      default:
        acc[curr.key] = '';
    }
    return acc;
  }, {});
};

/**
 * BASE_FAREINFO에서 해당 영업소의 통행요금 정보를 가져오는 함수
 * @param {string} IC_CODE 영업소코드
 * @returns {(string|Array)} 통행요금 정보
 */
export const getFareInfo = async (IC_CODE) => {
  const response = await request('post', 'api/common/getFare', {
    IC_CODE: IC_CODE,
  });
  const fareStore = useFareStore();
  fareStore.setFareInfo(response);
};

/**
 * 자동차 정보로 통행료를 계산하는 함수
 * @param {string} carType 차종
 * @param {string} occDt 발생일자
 * @returns {(int)} 통행료
 */
export const getFareByCarInfo = (carType, occDt, mainPayDiv) => {
  const fareStore = useFareStore();
  const fareInfo = fareStore.getFareInfo;

  const fareApplyDt = fareInfo[0].FARE_APPLY_DT;
  const fareDiv = occDt >= fareApplyDt ? 'NEW' : 'OLD';

  const fareMap = {
    NEW: {
      1: fareInfo[0].NEW_TOT_FARE1,
      2: fareInfo[0].NEW_TOT_FARE2,
      3: fareInfo[0].NEW_TOT_FARE3,
      4: fareInfo[0].NEW_TOT_FARE4,
      5: fareInfo[0].NEW_TOT_FARE5,
      6: fareInfo[0].NEW_TOT_FARE6,
    },
    OLD: {
      1: fareInfo[0].OLD_TOT_FARE1,
      2: fareInfo[0].OLD_TOT_FARE2,
      3: fareInfo[0].OLD_TOT_FARE3,
      4: fareInfo[0].OLD_TOT_FARE4,
      5: fareInfo[0].OLD_TOT_FARE5,
      6: fareInfo[0].OLD_TOT_FARE6,
    },
  };

  let fare = fareMap[fareDiv][carType] || 0;

  if (mainPayDiv === '1') {
    const dcApplyDt = fareInfo[0].HR_DC_APPLY_DT;
    const dcDiv = occDt >= dcApplyDt ? 'NEW' : 'OLD';

    const startTm = dcDiv === 'NEW' ? fareInfo[0].NEW_START_TM : fareInfo[0].OLD_START_TM;
    const endTm = dcDiv === 'NEW' ? fareInfo[0].NEW_END_TM : fareInfo[0].OLD_END_TM;

    const timeDiv = occDt.slice(8, 12) >= startTm && occDt.slice(8, 12) <= endTm ? 0 : 1;

    const dcMap = {
      NEW: {
        1: fareInfo[timeDiv].NEW_DC_RATE1,
        2: fareInfo[timeDiv].NEW_DC_RATE2,
        3: fareInfo[timeDiv].NEW_DC_RATE3,
        4: fareInfo[timeDiv].NEW_DC_RATE4,
        5: fareInfo[timeDiv].NEW_DC_RATE5,
        6: fareInfo[timeDiv].NEW_DC_RATE6,
      },
      OLD: {
        1: fareInfo[timeDiv].OLD_DC_RATE1,
        2: fareInfo[timeDiv].OLD_DC_RATE2,
        3: fareInfo[timeDiv].OLD_DC_RATE3,
        4: fareInfo[timeDiv].OLD_DC_RATE4,
        5: fareInfo[timeDiv].OLD_DC_RATE5,
        6: fareInfo[timeDiv].OLD_DC_RATE6,
      },
    };

    const baseFare = fareMap[fareDiv][carType] || 0;

    const discountRate = dcMap[dcDiv][carType] || 0;

    fare = Math.ceil((baseFare * (1 - discountRate / 100)) / 10) * 10;
  }

  return fare;
};

/**
 * 자동차 정보와 이전영업소 정보로 연속통행할인액을 계산하는 함수
 * @param {string} carType 차종
 * @param {string} occDt 발생일자
 * @param {string} IcCode 이전영업소코드
 * @returns {(int)} 통행료
 */
export const getCpdFareByCarInfo = (carType, occDt, IcCode) => {
  const fareStore = useFareStore();
  const fareInfo = fareStore.getFareInfo;
  const isCpdOffice = fareInfo.find((obj) => obj.TRG_IC_CODE === IcCode);
  const result = isCpdOffice || fareInfo[0];

  const fareApplyDt = result.CONT_DC_APPLY_DT;
  const fareDiv = occDt >= fareApplyDt ? 'NEW' : 'OLD';

  const fareMap = {
    NEW: {
      1: result.NEW_DC_FARE2,
      2: result.NEW_DC_FARE3,
      3: result.NEW_DC_FARE4,
      4: result.NEW_DC_FARE4,
      5: result.NEW_DC_FARE4,
      6: result.NEW_DC_FARE1,
    },
    OLD: {
      1: result.OLD_DC_FARE2,
      2: result.OLD_DC_FARE3,
      3: result.OLD_DC_FARE4,
      4: result.OLD_DC_FARE4,
      5: result.OLD_DC_FARE4,
      6: result.OLD_DC_FARE1,
    },
  };

  const cpdFare = fareMap[fareDiv][carType] || 0;

  return { cpdFare, isCpdOffice };
};

/**
 * 자동차 정보로 차등요금할인액을 계산하는 함수
 * @param {string} carType 차종
 * @param {string} occDt 발생일자
 * @returns {(int)} 통행료
 */
export const getDiffFareByCarInfo = (carType, occDt) => {
  const fareStore = useFareStore();
  const fareInfo = fareStore.getFareInfo;

  const fareApplyDt = fareInfo[0].DIFF_TOLL_APPLY_DT;
  const fareDiv = occDt >= fareApplyDt ? 'NEW' : 'OLD';

  const fareMap = {
    NEW: {
      1: fareInfo[0].NEW_TOT_TOLL1,
      2: fareInfo[0].NEW_TOT_TOLL2,
      3: fareInfo[0].NEW_TOT_TOLL3,
      4: fareInfo[0].NEW_TOT_TOLL4,
      5: fareInfo[0].NEW_TOT_TOLL5,
      6: fareInfo[0].NEW_TOT_TOLL6,
    },
    OLD: {
      1: fareInfo[0].OLD_TOT_TOLL1,
      2: fareInfo[0].OLD_TOT_TOLL2,
      3: fareInfo[0].OLD_TOT_TOLL3,
      4: fareInfo[0].OLD_TOT_TOLL4,
      5: fareInfo[0].OLD_TOT_TOLL5,
      6: fareInfo[0].OLD_TOT_TOLL6,
    },
  };

  return fareMap[fareDiv][carType] || 0;
};

/**
 * getSystemSmallCode 공통 함수
 * @param {String} systemSmallCode: 가져올 시스템 코드값
 * @param {Booelean} addAll: 조회조건에 전체를 추가할건지 여부
 * @param {Booelean} displayCode: CODESC_NM[CODESC] 형태로 조회조건에 추가할건지 여부
 * @returns
 */
export const getSystemSmallCode = (systemSmallCode, addAll = false, displayCode = false) => {
  const systemSmallCodeAll = loadSystemSmallCodeAll();
  const systemCodeInfo = JSON.parse(JSON.stringify(systemSmallCodeAll.getSystemSmallCodeAll));

  if (displayCode == true) {
    systemCodeInfo[systemSmallCode].map((item) => {
      item.title =
        item.title + // CODESC_NM
        '[' +
        item.value + // CODESC
        ']';
    });
  }
  if (addAll == true) {
    systemCodeInfo[systemSmallCode].unshift({
      title: '전체',
      value: '',
    });
  }

  return systemCodeInfo[systemSmallCode];
};

/**
 * @deprecated 삭제될 함수입니다.
 */
export const getSysCode = async (codelc, option, addAll = true) => {
  const response = await request('post', 'api/base/getSystemSmallCode', {
    CODELC: codelc,
    CODESC_USE_YN: 'Y',
  });
  option.value = response;
  if (response && Array.isArray(response)) {
    option.value = response.map((item) => ({
      title: item.CODESC_NM,
      value: item.CODESC,
    }));
  } else {
    option.value = [];
  }
  if (addAll == true) {
    option.value.unshift({
      title: '전체',
      value: '',
    });
  }
};
/**
 * @deprecated 삭제될 함수입니다.
 */
export const getSysCodeDp = async (codelc, option, addAll = true) => {
  const response = await request('post', 'api/base/getSystemSmallCode', {
    CODELC: codelc,
    CODESC_USE_YN: 'Y',
  });
  option.value = response;
  if (response && Array.isArray(response)) {
    option.value = response.map((item) => ({
      title: item.CODESC_NM + '[' + item.CODESC + ']',
      value: item.CODESC,
    }));
  } else {
    option.value = [];
  }
  if (addAll == true) {
    option.value.unshift({
      title: '전체',
      value: '',
    });
  }
};
/**
 * deprecated 함수입니다. : 삭제될 예정입니다.
 */
export const getCardCoCode = async (option, type) => {
  const response = await request('post', 'api/card/getCardCoCode', {});
  option.value = response;
  if (response && Array.isArray(response)) {
    option.value = response.map((item) => ({
      title: item.CARD_CO_NM,
      value: item.CARD_CO_CODE,
    }));
  } else {
    option.value = [];
  }
  if (type == null) {
    option.value.unshift({
      title: '전체',
      value: '',
    });
  }
};
/**
 * deprecated 함수입니다. : 삭제될 예정입니다.
 */
export const getListSysCode = async (codelc, option, addAll = true) => {
  const response = await request('post', 'api/sales/getListSysCode', {
    CODELC: codelc,
  });
  option.value = response;
  if (response && Array.isArray(response)) {
    option.value = response.map((item) => ({
      title: item.CODESC_NM,
      value: item.CODESC,
    }));
  } else {
    option.value = [];
  }
  if (addAll == true) {
    option.value.unshift({
      title: '전체',
      value: '',
    });
  }
};

/**
 * excelDownload함수에 messageBox 결합한 함수
 * @param {int} row
 * @param {Array} searchHeader
 * @param {Array} searchData
 * @param {Array} headers
 * @param {Array} contents
 * @param {String} fileName
 * @param {String} sheetName
 * @param {Array} addedRow
 */
export const makeExcel = (row, searchHeader, searchData, headers, contents, fileName, sheetName, addedRow) => {
  ElMessageBox.confirm('엑셀 다운로드를 진행 하시겠습니까?', 'Excel download', {
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    type: 'success',
  })
    .then(async () => excelDownload(row, searchHeader, searchData, headers, contents, fileName, sheetName, addedRow))
    .catch(() => {
      ElMessage({
        type: 'warning',
        message: '액셀다운로드 취소',
      });
    });
};

/**
 * 세금계산서 다운로드 함수 (-세금계산서 페이지 삭제로 사용 X)
 * @param {string} fileName
 * @param {string} url
 * @param {Array} param
 */
export const makeTaxbill = (fileName, url, param = {}) => {
  ElMessageBox.confirm('엑셀 다운로드를 진행 하시겠습니까?', 'Excel download', {
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    type: 'success',
  })
    .then(async () => {
      const token = Cookies.get('gdtcs-auth-token');
      const headers = {};
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
      const authStore = useAuthStore();
      const config = {
        method: 'POST',
        data: param,
        responseType: 'blob',
        url,
        headers,
      };
      try {
        const { data } = await axios(config);
        saveAs(data, fileName);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          alert('401 Unauthorized 관리자한테 문의하세요. 로그인창으로 이동합니다.');
          authStore.accessToken = null;
          Cookies.remove('gdtcs-auth-token');
        } else {
          throw error;
        }
      }
    })
    .catch(() => {
      ElMessage({
        type: 'warning',
        message: '액셀다운로드 취소',
      });
    });
};

/**
 * OZ Report 전달할 Dataset 만드는 함수
 * @param {string} path
 * @param {Object} param
 * @returns {string}
 */
export const createOzDataset = (path, param) => {
  const pcount = Object.keys(param).length + 2;
  const ozLogoImage = ozAppImageUrl + '9';
  const paramData = {};

  Object.entries(param).map(([key, value], idx) => {
    if (key.includes('CSV_DATA')) paramData[`connection.args${idx + 2}`] = `${key}=${covertCSV(value)}`;
    else paramData[`connection.args${idx + 2}`] = `${key}=${value}`;
  });

  const jsonData = JSON.stringify({
    'connection.fetchtype': 'concurrent',
    'connection.reportname': `${path}`,
    'connection.pcount': `${pcount}`,
    'connection.args1': `LOGO_URL=${ozLogoImage}`,
    ...paramData,
  });

  return jsonData;
};
/**
 * Array 형태의 Data 를 csv 형태로 변환해주는 함수
 * @param {Array} csvData
 * @returns
 */
const covertCSV = (csvData) => {
  if (csvData.length === 0) return '';
  const ozCSVTitle = Object.keys(csvData[0]);

  let ozDataString = '';
  csvData.map((obj) => {
    Object.values(obj).map((arr) => {
      if (arr === null) {
        arr = '';
      }
      ozDataString += String.raw`${arr}`.replace(/,/g, '');
      ozDataString += ',';
    });
    ozDataString += '\\r';
  });
  return ozCSVTitle.join(',') + '\\r' + ozDataString;
};

/**
 * message 공통 함수
 * @param {*} message: 창에 띄울 메시지
 * @param {*} type: 4가지 success, error, info, warning
 * @param {*} duration: 메시지 유지 시간
 * @returns
 */
export const showMessage = (message, type = 'info', duration = 4000) => {
  ElMessage({
    message,
    type,
    duration,
  });
};

/**
 * yyyyMMddHHmmss 형식의 일시 정보가 담긴 텍스트를 Datetime-local 컴포넌트가 요구하는 형태로 가공하는 함수
 * @param {*} yyyyMMddHHmmss: 년월일시분초
 * @returns
 */
export const yyyyMMddHHmmssToDatetimeLocal = (yyyyMMddHHmmss) => {
  return (
    yyyyMMddHHmmss.slice(0, 4) +
    '-' +
    yyyyMMddHHmmss.slice(4, 6) +
    '-' +
    yyyyMMddHHmmss.slice(6, 8) +
    'T' +
    yyyyMMddHHmmss.slice(8, 10) +
    ':' +
    yyyyMMddHHmmss.slice(10, 12)
  );
};

/**
 * Datetime-local 컴포넌트의 값을 yyyyMMddHHmmss 형식으로 가공하는 함수
 * @param {*} dt: Datetime-local 컴포넌트의 값
 * @returns
 */
export const datetimeLocalToyyyyMMddHHmmss = (dt) => {
  return dt.replaceAll('-', '').replaceAll('T', '').replaceAll(':', '').replaceAll(' ', '').padEnd(14, '0').slice(0, 14);
};

/**
 * Datetime-local 컴포넌트의 값을 yyyyMMdd 형식으로 가공하는 함수
 * @param {*} dt: Datetime-local 컴포넌트의 값
 * @returns
 */
export const datetimeLocalToyyyyMMdd = (dt) => {
  return dt.replaceAll('-', '').replaceAll('T', '').replaceAll(':', '').replaceAll(' ', '').padEnd(8, '0').slice(0, 8);
};

/**
 * 현재 시간을 년월일시분초(yyyyMMddHHmmss)로 가져오는 함수
 * @returns
 */
export const getCurrentDatetime = () => {
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth() + 1;
  const day = currentDate.getDate();
  const hours = currentDate.getHours();
  const minutes = currentDate.getMinutes();
  const seconds = currentDate.getSeconds();
  return `${year}${String(month).padStart(2, '0')}${String(day).padStart(2, '0')}${String(hours).padStart(2, '0')}${String(minutes).padStart(
    2,
    '0',
  )}${String(seconds).padStart(2, '0')}`;
};

/**
 * yyyyMMdd 값을 yyyy-MM-dd 로 변경하는 함수
 * @param yyyyMMdd
 * @returns yyyy-MM-dd
 */
export const yyyyMMddToDateFormat = (yyyyMMdd) => {
  return yyyyMMdd.substr(0, 4) + '-' + yyyyMMdd.substr(4, 2) + '-' + yyyyMMdd.substr(6, 2);
};

/**
 * yyyy-MM-dd 값을 yyyyMMdd 로 변경하는 함수
 * @param yyyy-MM-dd
 * @returns yyyyMMdd
 */
export const dateFormatToyyyyMMdd = (dateFormat) => {
  return dateFormat.replaceAll('-', '');
};

/**
 * HHmm 값을 timepicker 형식으로 변경하는 함수
 * @param HHmm
 * @returns (timepickerFormat) HH:mm
 */
export const HHmmssToTimepicker = (HHmm) => {
  return HHmm.substr(0, 2) + ':' + HHmm.substr(2, 4);
};

/**
 * timepicker 값을 HHmm 형식으로 변경하는 함수
 * @param (timepickerFormat) HH:mm
 * @returns HHmm
 */
export const timepickerToHHmm = (timepickerFormat) => {
  return timepickerFormat.replaceAll(':', '');
};

/**
 * 차로번호 목록을 가져오는 함수
 * @returns
 */
export const getLaneNo = async () => {
  const authStore = useAuthStore();
  const response = await request('post', 'api/office/getLaneNo', { IC_CODE: authStore.getIcCode });
  let laneList = [];
  if (response && Array.isArray(response)) {
    laneList = response.map((item) => ({
      title: `${item.LOGC_LANE}: ${item.LANE_NM}`,
      value: item.LOGC_LANE,
    }));
  } else {
    laneList = [];
  }
  laneList.unshift({
    title: '전체',
    value: '',
  });
  return laneList;
};

/**
 * 근무 목록을 가져오는 함수
 * @param {*} START_DATE: 조회시작일
 * @param {*} END_DATE: 조회종료일
 * @returns
 */
export const getWorkNo = async (START_DATE, END_DATE) => {
  const authStore = useAuthStore();
  const response = await request('post', 'api/office/getWorkNo', {
    START_DATE: dayjs(START_DATE).format('YYYYMMDD'),
    END_DATE: dayjs(END_DATE).format('YYYYMMDD'),
    IC_CODE: authStore.getIcCode,
  });
  let workList = [];
  if (response && Array.isArray(response)) {
    workList = response.map((item) => ({
      title: item.WORK_NM,
      value: item.WORK_NO,
    }));
  } else {
    workList = [];
  }
  workList.unshift({
    title: '전체',
    value: '',
  });
  return workList;
};

/**
 * 저장 버튼의 사용여부를 설정하는 함수
 * @param {*} stat: 'Y' 는 활성화, 'N'은 비활성화이다.
 * @returns
 */
export const changeButtonStatusSave = (stat = 'Y') => {
  const menuStore = useMenuStore();
  const selectedSubMenuPath = menuStore.getSelectedSubMenuPath;

  menuStore.mainMenus.forEach((mainMenu) => {
    mainMenu.subMenus.forEach((subMenu) => {
      if (subMenu.path === selectedSubMenuPath) {
        subMenu.btnSave = stat;
        return false;
      }
    });
  });
};

/**
 * 조회 버튼의 사용여부를 설정하는 함수
 * @param {*} stat: 'Y' 는 활성화, 'N'은 비활성화이다.
 * @returns
 */
export const changeButtonStatusSearch = (stat = 'Y') => {
  const menuStore = useMenuStore();
  const selectedSubMenuPath = menuStore.getSelectedSubMenuPath;

  menuStore.mainMenus.forEach((mainMenu) => {
    mainMenu.subMenus.forEach((subMenu) => {
      if (subMenu.path === selectedSubMenuPath) {
        subMenu.btnSearch = stat;
        return false;
      }
    });
  });
};
/**
 * parameter 값에따라 input 값 허용하는 regex 뱉어내는 함수
 * @param {string} type
 * @returns
 */
export const getRegex = (type) => {
  switch (type) {
    case 'digit':
      return '0-9';
    case 'korean':
      return 'ㄱ-힣';
    case 'english':
      return 'a-zA-Z';
    case 'space':
      return '\\s';
    case 'symbol':
      return '\\W';
    case 'dot':
      return '.';
    case 'comma':
      return ',';
    case 'sign':
      return '+-';
    default:
      return '';
  }
};

/**
 * 조회조건을 Object로 가져오는 함수
 * Report 조회조건에 활용
 * @param {Array} searchHeader
 * @param {Object} searchData
 * @returns {Object} {START_DATE: '2024-10-10', END_DATE: '2024-10-10', WORK_NO: '전체', IMG_TYPE_DTL: '재인식확정'}
 */
export const getCondition = (searchHeaders, searchData) => {
  let condition = {};
  searchHeaders.forEach((header) => {
    if (!!header.option && (header.type === 'select' || header.type === 'radio')) {
      condition[`${header.key}`] = header.option.find((opt) => opt.value === searchData[header.key]).title;
    } else if (header.type === 'checkbox') {
      if (searchData[header.key] === true) condition[`${header.key}`] = header.label;
    } else {
      condition[`${header.key}`] = searchData[header.key] === '' ? '전체' : searchData[header.key];
    }
  });
  return condition;
};
/**
 * 쿼리 결과 null이나 empty 인 경우 확인하는 함수
 * @param {*} data
 * @returns {Boolean} true or false
 */
export const isNullOrEmpty = (data) => {
  if (!data) alert('조회 중 문제가 발생했습니다.');
  else if (data.length === 0) showMessage(`조회된 데이터가 없습니다.`, 'success');
  else return false;
  return true;
};
