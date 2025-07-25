<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab>도공면제</v-tab>
    <v-tab>도공할인</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        scrollKey="DailyReductionSummaryExExemption"
        :headers="exemptHeaders"
        :contents="exemptContents"
        :height-offset="heightOffset"
        rowType="mix"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        scrollKey="DailyReductionSummaryExReduction"
        :headers="reductionHeaders"
        :contents="reductionContents"
        :height-offset="heightOffset"
        rowType="mix"
      />
    </v-window-item>
  </v-window>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { request, btnHandler, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore, useColorStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

const appTitle = `일별 감면차량 이용현황`;
const isLoading = ref(false);
const authStore = useAuthStore();
const colorStore = useColorStore();

const tab = ref(0);

/* Base: 189
 * Search Header: 36
 * Tab : 28
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 28 + 29 * 2;

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
  },
  prev: {},
});

const exemptHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP', width: '110' },
  {
    title: '긴급면제',
    children: [
      { title: '군 작전(국)', key: 'TYPE0_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '군 작전(미)', key: 'TYPE1_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '119 구급차량', key: 'TYPE2_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '앰뷸런스(복지)', key: 'TYPE3_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '앰뷸런스(보훈)', key: 'TYPE4_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소방차량', key: 'TYPE5_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '경찰작전차량', key: 'TYPE6_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '교통단속차량', key: 'TYPE7_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '우편사업차량', key: 'TYPE8_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '재해복구차량', key: 'TYPE9_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  { title: '감면단말기', key: 'TYPE10_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '통합복지', key: 'TYPE11_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '기타면제', key: 'TYPE98_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '소계', key: 'SMR_VAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const exemptContents = ref([]);

const reductionHeaders = ref([
  { title: '근무일자', key: '', width: '120' },
  {
    title: '장애인1~6급',
    align: 'center',
    children: [
      { title: '건수', key: '', width: '120' },
      { title: '통행요금', key: '', width: '120' },
      { title: '출금액', key: '', width: '120' },
    ],
  },
  {
    title: '국가유공상이자6~7급',
    align: 'center',
    children: [
      { title: '건수', key: '', width: '120' },
      { title: '통행요금', key: '', width: '120' },
      { title: '출금액', key: '', width: '120' },
    ],
  },
  {
    title: '고엽제후유(의)증환자',
    align: 'center',
    children: [
      { title: '건수', key: '', width: '120' },
      { title: '통행요금', key: '', width: '120' },
      { title: '출금액', key: '', width: '120' },
    ],
  },
]);

const reductionContents = ref([]);

const handleSearch = async () => {
  if (tab.value === 0) {
    try {
      isLoading.value = true;
      const response = await request('post', 'api/sales/getDailyExemptSummaryEx', {
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      });
      searchData.prev = getCondition(searchHeader.value, searchData.current);
      exemptContents.value = response;
      if (response.length === 0) {
        showMessage('조회된 데이터가 없습니다.', 'warning');
      }
    } catch (error) {
      showMessage('데이터 조회 중 오류 발생:', 'error');
    } finally {
      isLoading.value = false;
    }
  } else {
    try {
      isLoading.value = true;
      const response = await request('post', 'api/sales/getDailyReductionSummaryEx', {
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      });
      reductionContents.value = response;
      if (response.length === 0) {
        showMessage('조회된 데이터가 없습니다.', 'warning');
      }
    } catch (error) {
      showMessage('데이터 조회 중 오류 발생:', 'error');
    } finally {
      isLoading.value = false;
    }
  }
};

const handleExcel = async () => {
  const headerRow = 'mix';
  if (tab.value === 0) {
    if (exemptContents.value.length === 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'warning');
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      excelDownload(headerRow, searchHeader.value, searchData.prev, exemptHeaders.value, exemptContents.value, '면제', appTitle);
    } else {
      showMessage(`엑셀다운로드를 취소했습니다.`);
    }
  } else {
    if (reductionContents.value.length === 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'warning');
      return;
    }
    // if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    //   excelDownload(headerRow, searchHeader.value, searchData.prev, reductionHeaders.value, reductionContents.value, '할인', appTitle);
    // } else {
    //   showMessage(`엑셀다운로드를 취소했습니다.`);
    // }
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped />
