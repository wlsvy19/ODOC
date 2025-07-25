<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="ReductionCheatListEX"
    :headers="mainHeaders"
    :contents="mainContents"
    :footer-contents="footerContents"
    :height-offset="heightOffset"
  />
</template>

<script setup>
import { ref, onActivated, reactive } from 'vue';
import { request, btnHandler, getLaneNo, showMessage, comma, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

//#region Common
const appTitle = `감면 부정사용내역(도로공사)`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;
//#endregion

//#region Select Box Option
/* Select Box Option - search */
const selectOptLaneNo = ref([]);
const selectOptCheatDiv = [
  { title: '전체', value: '' },
  { title: '긴급면제카드', value: '긴급면제카드' },
  { title: '할인단말기', value: '할인단말기' },
  { title: '통합복지', value: '통합복지' },
];

onActivated(async () => {
  selectOptLaneNo.value = await getLaneNo();
});
//#endregion

//#region Search
const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '130px' },
  { label: '처리차량번호', key: 'CAR_NO', type: 'input', width: '110px' },
  { label: '카드번호', key: 'CARD_NO', type: 'input', width: '140px', valid: 'digit' },
  { label: '부정발생구분', key: 'CHEAT_DIV', type: 'select', option: selectOptCheatDiv, width: '110px' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    CAR_NO: '',
    CARD_NO: '',
    CHEAT_DIV: '',
  },
  prev: {},
});
//#endregion

//#region Main Grid
const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '심사구분', key: 'EXM_DIV_DP', width: '80' },
  { title: '원통행료', key: 'ORIGIN_PASS_FARE_DP', width: '70' },
  { title: '통행료', key: 'PASS_FARE_DP', width: '70' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '70' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '120' },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '160' },
  { title: '카드속성', key: 'ECARD_ATT_DP', width: '90' },
  { title: '단말기속성', key: 'OBU_ATT_DP', width: '140' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '카드차량번호', key: 'ECARD_CAR_NO', width: '110' },
  { title: '발생구분', key: 'CHEAT_DIV', width: '90' },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const subTotalSumOriginFare = ref('0');
const subTotalSumPayFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '원통행요금', value: subTotalSumOriginFare, unit: '원' },
  { title: '수납요금', value: subTotalSumPayFare, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = comma(mainContents.value.length);
  subTotalSumOriginFare.value = comma(
    mainContents.value.reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0),
  );
  subTotalSumPayFare.value = comma(
    mainContents.value.reduce((acc, item) => {
      return acc + item.PAY_FARE;
    }, 0),
  );
};
//#endregion

//#region Main Button Action
const handleSearch = async () => {
  isLoading.value = true;
  try {
    const response = await request('post', 'api/office/getCheatReductionExList', {
      ...searchData.current,
      ...{
        START_DATE: searchData.current['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.current['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
        CARD_NO: searchData.current['CARD_NO'].replaceAll('-', ''),
      },
    });
    searchData.prev = getCondition(searchHeader.value, searchData.current);
    mainContents.value = response;
    setFooterContents();
  } catch (error) {
    showMessage(error, 'warning');
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = async () => {
  const headerRow = 1;
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
//#endregion
</script>

<style scoped></style>
