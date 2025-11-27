<template>
  <LoadingComponent :message="loadingState.msg" v-if="loadingState.isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  </div>
  <TableComponent scrollKey="PreRegistration" :headers="headers" :contents="contents" :heightOffset="heightOffset" :footerContents="footerContents" />
</template>

<script setup>
import { ref, toRaw, onActivated } from 'vue';
import { request, btnHandler, comma, showMessage, getSystemSmallCode } from '@/utils/common';
import { useAuthStore } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import dayjs from 'dayjs';

const authStore = useAuthStore();
const contents = ref([]);

const loadingState = ref({
  isLoading: false,
  msg: '',
});

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer
 */
const heightOffset = 189 + 36 + 29 + 30;

const originalSearchData = ref(null);
const currentSearchData = ref(null);

const trnRcpOption = ref([]);

onActivated(async () => {
  //searchHeader selectBox
  trnRcpOption.value = getSystemSmallCode('807', true, true);
});

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차량번호', key: 'HAND_CAR_NO', type: 'input', maxLength: '9' },
  { label: '고객명', key: 'MBR_NM', type: 'input', valid: 'korean', maxLength: '5' },
  { label: '전송결과', key: 'TRNRCP_RSLT', type: 'select', option: trnRcpOption, width: '130px' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  HAND_CAR_NO: '',
  MBR_NM: '',
  TRNRCP_RSLT: '',
});

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '50' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '150' },
  { title: '통행요금', key: 'OFC_EXPT_PAY_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '전송결과', key: 'TRNRCP_RSLT_DP', width: '100' },
  { title: '수신일시', key: 'TRNRCP_DT_DP', width: '150' },
  { title: '고객명', key: 'MBR_NM', width: '100' },
  { title: '고객명ID', key: 'MBR_ID', width: '100' },
  { title: '응답유형', key: 'RSPNS_TYPE', width: '100' },
  { title: '응답결과', key: 'RSPNS_RSLT_CN', width: '100' },
  { title: '응답일시', key: 'RSPNS_DT_DP', width: '150' },
]);

const footerContents = ref([
  { title: '총 건수', value: '0', unit: '건', width: '100' },
  { title: '통행요금', value: '0', unit: '원' },
]);

const handleSearch = async () => {
  console.log(`output->searchData.value`, searchData.value);
  loadingState.value = { msg: '사전등록처리 내역 조회 중 입니다.', isLoading: true };
  try {
    const response = await request('post', 'api/office/getPreRegistrationList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    contents.value = response;

    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));

    //소계
    const totalRows = contents.value.length;
    let totalOfcExptPayFare = 0;

    contents.value.forEach((item) => {
      totalOfcExptPayFare += Number(item.OFC_EXPT_PAY_FARE) || 0;
    });

    footerContents.value[0].value = comma(totalRows);
    footerContents.value[1].value = comma(totalOfcExptPayFare);
  } catch (error) {
    alert(`사전등록처리 내역 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
  }
};

const handleExcel = () => {
  const headerRow = 1;
  const excelTitle = `사전등록처리 내역조회`;
  if (contents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }

  currentSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));

  if (JSON.stringify(toRaw(currentSearchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert('조회조건이 변경됐습니다. 조회 후 시도해주세요.');
    return;
  }

  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value
      .filter((header) => header.key !== 'checkBox')
      .map((header) => ({
        title: header.title,
        key: header.key,
        excelWidth: Number(header.width) / 7,
      }));

    const excelContents = contents.value;

    excelDownload(headerRow, searchHeader.value, searchData.value, excelHeaders, excelContents, excelTitle, excelTitle, [
      `총 근무건수: ${footerContents.value[0].value} 건  통행요금: ${footerContents.value[1].value} 원 `,
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped />
