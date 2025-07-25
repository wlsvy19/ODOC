<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent :headers="headers" :contents="lineCtrlOfficePayListData" rowType="1" scrollKey="lineCtrlOfficePayListData" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick } from 'vue';
import { request, btnHandler, getSystemSmallCode } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';

const authStore = useAuthStore();
const isLoading = ref(false);
const cardType = ref([]);

cardType.value = getSystemSmallCode('071', true);

const headers = ref([
  { title: '순번', key: 'ROW_SEQ', width: '100' }, // 내가 DB에 넘겨줄 변수명

  { title: '근무일자', key: 'WORK_DATE', width: '100' },
  { title: '영업소명', key: 'IC_CODE_NM', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '100' },
  { title: '근무자번호', key: 'WORKER_NO', width: '100' },
  { title: '처리시각', key: 'HAND_DT', width: '150' },
  { title: '차로제어기 수납금', key: 'LANE_PAY_FARE', width: '160', customBodyCellStyle: 'table-body-style-right' },
  { title: '미납처리구분', key: 'UNPAY_HAND_DIV', width: '150' },
  { title: '미납발생 근무일자', key: 'UNP_OCC_WORKDATE', width: '150' },
  { title: '미납발생 근무번호', key: 'UNP_OCC_WORKNO', width: '150' },
  { title: '미납발생 일련번호', key: 'UNP_OCC_SNO', width: '150' },
  { title: '미납수납금', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '카드수납전 잔액', key: 'PAY_BEF_BALC', width: '150', customBodyCellStyle: 'table-body-style-right' },
  { title: '수입구분', key: 'REVEN_DIV', width: '100' },
  { title: '카드종류', key: 'UNP_CARD_TYPE', width: '100' },
  { title: '카드번호', key: 'CARD_NO', width: '150' },
  { title: '차로구분', key: 'LANE_DIV', width: '100' },
  { title: '차로명', key: 'LANE_NM', width: '100' },
]);
const unPayHandDiv = [
  { value: '', title: '전체' },
  { value: '0', title: '미처리' },
  { value: '1', title: '처리' },
];

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '미납처리구분', key: 'UNPAY_HAND_DIV', type: 'select', option: unPayHandDiv },
  { label: '카드종류', key: 'UNP_CARD_TYPE', type: 'select', option: cardType, width: '140px' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  UNPAY_HAND_DIV: '',
  UNP_CARD_TYPE: '',
});

const lineCtrlOfficePayListData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/unpaid/getLineCtrlOfficePayList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    lineCtrlOfficePayListData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`차로제어기 사무실수납 내역 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (lineCtrlOfficePayListData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      lineCtrlOfficePayListData.value,
      '차로제어기 사무실수납 내역 조회',
      '차로제어기 사무실수납 내역 조회',
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
