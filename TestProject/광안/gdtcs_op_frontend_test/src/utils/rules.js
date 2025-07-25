import { isNumeric, isKor, isEngAndNumeric } from '@/utils/validate';
import { reactive } from 'vue';

export const InputRules = reactive({
  requiredRule: [
    (v) => {
      return !!v || '필수 항목을 입력해주세요.';
    },
  ],
  carNoRule: [
    (v) => {
      return !v || (v && v.length <= 12) || '차량번호는 12자 이하로 입력해주세요';
    },
  ],
  handSnoRule: [
    (v) => {
      return !v || isNumeric(v) || '일련번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  cardNoRule: [
    (v) => {
      return !v || (v && v.length <= 16) || '카드번호는 16자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || '카드번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  obuNoRule: [
    (v) => {
      return !v || (v && v.length <= 16) || 'OBU번호는 16자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || 'OBU번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  noteRule: [
    (v) => {
      return !v || (v && v.length <= 100) || '비고는 100자 이하로 입력해주세요';
    },
  ],
  custNmRule: [
    (v) => {
      return !v || (v && v.length <= 10) || '고객명은 10자 이하로 입력해주세요';
    },
  ],
  binRule: [
    (v) => {
      return !v || (v && v.length <= 6) || 'BIN 코드는 6자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || 'BIN 코드는 숫자만 입력할 수 있습니다.';
    },
  ],
  telNoRule: [
    (v) => {
      return !v || (v && v.length <= 11) || '전화번호는 11자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || '전화번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  zipCodeRule: [
    (v) => {
      return !v || (v && v.length <= 5) || '우편번호는 5자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || '우편번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  icNameRule: [
    (v) => {
      return !v || (v && v.length <= 10) || '영업소명은 10자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isKor(v) || '영업소명은 한글만 입력할 수 있습니다.';
    },
  ],
  icTelRule: [
    (v) => {
      return !v || (v && v.length <= 32) || '영업소 전화번호는 32자 이하로 입력해주세요';
    },
    (v) => {
      return !v || (v && /^[0-9*-]+$/.test(v)) || '영업소 전화번호는 숫자와 특수문자(-)만 입력할 수 있습니다.';
    },
  ],
  addrRule: [
    (v) => {
      return !v || (v && v.length <= 50) || '주소는 50자 이하로 입력해주세요';
    },
  ],
  workerNameRule: [
    (v) => {
      return !v || (v && v.length <= 6) || '근무자이름은 6자 이하로 입력해주세요';
    },
  ],
  workerRnkRule: [
    (v) => {
      return !v || (v && v.length <= 5) || '직급은 5자 이하로 입력해주세요';
    },
  ],
  workerTelNoRule: [
    (v) => {
      return !v || (v && v.length <= 13) || '전화번호는 13자 이하로 입력해주세요';
    },
    (v) => {
      return !v || (v && /^[0-9*-]+$/.test(v)) || '전화번호는 숫자와 특수문자(-)만 입력할 수 있습니다.';
    },
  ],
  workerCardNoRule: [
    (v) => {
      return !v || (v && v.length <= 19) || '카드번호는 숫자 16자 이하로 입력해주세요';
    },
    (v) => {
      return !v || (v && /^[0-9*-]+$/.test(v)) || '카드번호는 숫자와 특수문자(-)만 입력할 수 있습니다.';
    },
  ],
  coNoRule: [
    (v) => {
      return !v || (v && v.length <= 8) || '사번은 8자 이하로 입력해주세요';
    },
    (v) => {
      return !v || isEngAndNumeric(v) || '사번은 영문과 숫자만 입력할 수 있습니다.';
    },
  ],
  workerNoRule: [
    (v) => {
      return !v || (v && v.length === 4) || '근무자번호는 4자로 입력해주세요';
    },
    (v) => {
      return !v || isNumeric(v) || '근무자번호는 숫자만 입력할 수 있습니다.';
    },
  ],
  passwordRule: [
    (v) => {
      return !v || isEngAndNumeric(v) || '비밀번호는 영문과 숫자만 입력할 수 있습니다.';
    },
    (v) => {
      return !!v || '비밀번호는 필수입니다.';
    },
    (v) => {
      return !v || (v && v.length >= 4) || '비밀번호는 4자 이상 입력해주세요';
    },
  ],
});
