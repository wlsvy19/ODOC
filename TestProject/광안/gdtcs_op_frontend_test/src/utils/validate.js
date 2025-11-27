import dayjs from 'dayjs';

/**
 * 입력값이 숫자로만 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 숫자로만 구성되어 있으면 true, 아니면 false
 */
export const isNumeric = (input) => {
  return /^[0-9]+$/.test(input);
};

/**
 * 입력값이 한글로만 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 한글로만 구성되어 있으면 true, 아니면 false
 */
export const isKor = (input) => {
  return /^[ㄱ-힣]+$/.test(input);
};

/**
 * 입력값이 한글과 숫자로만 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 한글과 숫자로만 구성되어 있으면 true, 아니면 false
 */
export const isKorAndNumeric = (input) => {
  return /^[가-힣0-9]+$/.test(input);
};

/**
 * 입력값이 올바른 시간 형식(00:00 ~ 23:59)인지 확인
 * @param {string} input 검사할 시간 문자열
 * @returns {boolean} 올바른 시간 형식이면 true, 아니면 false
 */
export const isTimeFormat = (input) => {
  return /^([01]\d|2[0-3]):([0-5]\d)$/.test(input);
};

/**
 * 시작 일시가 종료 일시보다 같거나 이전인지 확인
 * @param {string} startDate 시작 일자
 * @param {string} endDate 종료 일자
 * @returns {boolean} 시작 일시가 종료 일시보다 이전 이거나 같으면 true, 아니면 false
 */
export const isValidDate = (startDate, endDate) => {
  return dayjs(startDate).isSameOrBefore(endDate);
};

/**
 * 입력값이 영문과 숫자로만 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 영문과 숫자로만 구성되어 있으면 true, 아니면 false
 */
export const isEngAndNumeric = (input) => {
  return /^[a-zA-Z0-9]+$/.test(input);
};

/**
 * 입력값이 한글, 숫자, 하이픈(-) 으로 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 한글, 숫자, 하이픈(-) 으로만 구성되어 있으면 true, 아니면 false
 */
export const isValidCarNumber = (input) => {
  return /^[가-힣0-9-]+$/.test(input.trim());
};

/**
 * 입력값이 숫자, 하이픈(-) 으로 구성되어 있는지 확인
 * @param {string} input 검사할 문자열
 * @returns {boolean} 한글, 숫자, 하이픈(-) 으로만 구성되어 있으면 true, 아니면 false
 */
export const isValidCarDNumber = (input) => {
  return /^[0-9-]+$/.test(input.trim());
};
