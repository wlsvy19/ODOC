<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="heightOffset"
    rowType="mix"
    :headers="headers"
    :contents="contents"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
  />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref } from 'vue';
import dayjs from 'dayjs';
import { btnHandler, request } from '@/utils/common.js';
import { useAuthStore } from '@/stores/index.js';
import { excelDownload } from '@/utils/excel.js';

const contents = ref([]);
const ozSearchData = ref([]);
const heightOffset = 189 + 36 + 29 + 30;

const authStore = useAuthStore();
const isLoading = ref(false);

const searchHeader = ref([
  { label: '일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const headers = ref([
  {
    title: '일자', key: 'WORK_DATE', width: '100',
  },
  {
    title: '영상심사',
    children: [
      { title: '자동', key: 'AUTO_IMG', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수동', key: 'MANUAL_IMG', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'TOTAL_IMG', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '위반심사',
    children: [
      { title: '자동', key: 'AUTO_VLTN', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수동', key: 'MANUAL_VLTN', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'TOTAL_VLTN', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '감면내역 도로공사',
    children: [
      { title: '자동', key: 'AUTO_EX', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수동', key: 'MANUAL_EX', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'TOTAL_EX', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '감면내역 부산시',
    children: [
      { title: '자동', key: 'AUTO_BS', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수동', key: 'MANUAL_BS', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'TOTAL_BS', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '합계', key: 'TOTAL_ALL', width: '100', customBodyCellStyle: 'table-body-style-right'
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.WORK_DATE === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/office/getCorrectionCount', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    contents.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`심사건수 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};
const handleExcel = () => {
  const row = 'mix';
  if (contents.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;

    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, contents.value, '심사건수 조회', '심사건수 조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped>

</style>