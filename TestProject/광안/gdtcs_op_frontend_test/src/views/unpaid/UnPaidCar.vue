<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent :headers="headers" :contents="unPaidCarData" rowType="1" scrollKey="unPaidCarData" />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset, getSystemSmallCode, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');

const authStore = useAuthStore();

const handTypeOption = ref([]);
const handStatOption = ref([]);
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');

handTypeOption.value = getSystemSmallCode('304', true, true);
handStatOption.value = getSystemSmallCode('134', true, true);

const headers = ref([
  { title: '순번', key: 'ROW_SEQ', width: '100' },
  { title: '근무일자', key: 'WORK_DATE', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '100' },
  { title: '처리번호', key: 'NU_HAND_SNO', width: '100' },
  { title: '미납일시', key: 'OCC_DATE', width: '150' },
  { title: '위반코드', key: 'VLTN_CODE', width: '150' },
  { title: '차량번호', key: 'CAR_NO', width: '100' },
  { title: '처리차종', key: 'CAR_TYPE_NAME', width: '100' },
  { title: '통행요금', key: 'PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납요금', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },

  { title: '차로구분', key: 'CHARO', width: '100' },
  { title: '처리유형', key: 'HAND_TYPE_NAME', width: '100' },
  { title: '지불구분', key: 'VLTN_PAY_TYPE', width: '100' },
  { title: '최종입금일', key: 'LAST_PAY_DATE', width: '100' },
  { title: '처리시각', key: 'LAST_MDFY_DT', width: '150' },
  { title: '관리자ID', key: 'UNP_ADMIN_ID', width: '100' },
  { title: '후불약정여부', key: 'LPAY_YN', width: '120' },
  { title: '약정일자', key: 'AGR_DATE', width: '100' },
  { title: '보류여부', key: 'HLD_YN', width: '100' },
  { title: '미납생성구분', key: 'UNP_DIV_CODE', width: '130' },
  { title: 'OBU번호', key: 'OBU_NO', width: '150' },
  { title: '전자카드번호', key: 'ECARD_NO', width: '150' },
  { title: '미납생성일자', key: 'UNP_MAKE_DATE', width: '100' },
  { title: '최종카드번호', key: 'LAST_CARD_NO', width: '150' },
  { title: '비고', key: 'NOTE', width: '200' },
  { title: '전화번호', key: 'CUST_TEL', width: '150' },
  { title: '우편번호', key: 'CUST_ZIP_CODE', width: '100' },
  { title: '주소', key: 'CUST_ADDR', width: '450' },
  { title: '1차고지서발송일자', key: 'BILL_SEND_DATE_1', width: '150' },
  { title: '2차고지서발송일자', key: 'BILL_SEND_DATE_2', width: '150' },
  { title: '3차고지서발송일자', key: 'BILL_SEND_DATE_3', width: '150' },
  { title: '부가통행요금', key: 'PASS_VAT', width: '100' },
  { title: '수납한부가통행요금', key: 'PAY_VAT', width: '150' },
  { title: '처리상태', key: 'HAND_STAT_NAME', width: '100' },
]);
const dateTypeOption = [
  { value: '', title: '근무일자' },
  { value: 'LAST_PAY_DATE', title: '미납완료일' },
  { value: 'AGR_DATE', title: '후불약정일' },
  { value: 'BILL_SEND_DATE_1', title: '1차 고지서 발송일' },
  { value: 'BILL_SEND_DATE_2', title: '2차 고지서 발송일' },
  { value: 'BILL_SEND_DATE_3', title: '3차 고지서 발송일' },
];

const lpayYnOption = [
  { value: '', title: '전체' },
  { value: 'Y', title: '약정[Y]' },
  { value: 'N', title: '미약정[N]' },
];

const unpDivOption = [
  { value: '', title: '전체' },
  { value: '0', title: '위반심사 미납' },
  { value: '1', title: '감면심사 미납' },
  { value: '2', title: '공차택시 비대상' },
  { value: '3', title: '사전등록 실패' },
];

const searchHeader = ref([
  { label: '', key: 'DATE_TYPE', type: 'select', option: dateTypeOption, width: '130px' },
  { label: '', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', valid: 'digit|korean', width: '110px', maxLength: '10' },
  { label: '후불약정여부', key: 'LPAY_YN', type: 'select', option: lpayYnOption, width: '90px' },
  { label: '처리유형', key: 'HAND_TYPE', type: 'select', option: handTypeOption },
  { label: '처리상태', key: 'HAND_STAT', type: 'select', option: handStatOption, width: '145px' },
  { label: '미납생성구분', key: 'UNP_DIV', type: 'select', option: unpDivOption, width: '135px' },
  { label: '비고', key: 'NOTE', type: 'input', width: '130px', maxLength: '50' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),

  END_DATE: dayjs().format('YYYY-MM-DD'),
  DATE_TYPE: '',
  CAR_NO: '',
  VLTN_CODE: '',
  LPAY_YN: '',
  HLD_YN: '',
  HAND_TYPE: '',
  HAND_STAT: '',
  UNP_DIV: '',
  NOTE: '',
});

const unPaidCarData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/unpaid/getUnPaidCar', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    unPaidCarData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`미납차량 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (unPaidCarData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, unPaidCarData.value, '미납차량 조회', '미납차량 조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (unPaidCarData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
    jsonData.value = createOzDataset('/UNPAID/unPaidCar.ozr', {
      CSV_DATA: unPaidCarData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      WORK_NO: '보고서번호',
      TITLE_NM: '미납차량 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
    });
    isActive.value = true;
  } catch (error) {
    alert(`미납차량 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};
btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped></style>
