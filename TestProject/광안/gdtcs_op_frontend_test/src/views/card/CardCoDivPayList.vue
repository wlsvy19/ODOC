<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="283"
    :headers="headers"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
    :contents="cardCoDivPayListData"
    rowType="mix"
    scrollKey="cardCoDivPayListData"
  />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, getSystemSmallCode, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const authStore = useAuthStore();
const route = useRoute();

const cardCoOption = ref([]);
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');
const appCode = computed(() => route.path).value.replace('/', '');

cardCoOption.value = getSystemSmallCode('316', true);

const headers = ref([
  { title: '요금소명', key: 'IC_NM', width: '150' },
  { title: '카드사', key: 'CARD_CO', width: '150' },
  { title: '건수', key: 'CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납금액', key: 'CNT_FARE', width: '150', customBodyCellStyle: 'table-body-style-right' },
  {
    title: 'HIPASS비정상지불',
    children: [
      { title: '건수', key: 'ABNOR_CNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'ABNOR_FARE', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const searchHeader = ref([
  { label: '근무일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '카드사', key: 'CARD_CO_CODE', type: 'select', option: cardCoOption, width: '130px' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  CARD_CO_CODE: '',
});

const cardCoDivPayListData = ref([]);
const ozSearchData = ref([]);
const setCustomBodyRowStyleSummary = (item) => {
  if (item.IC_NM === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};
const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getCardCoDivPayList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    cardCoDivPayListData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`카드사별 지불내역 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 'mix';
  if (cardCoDivPayListData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      cardCoDivPayListData.value,
      '카드사별 지불내역 조회',
      '카드사별 지불내역 조회',
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (cardCoDivPayListData.value.length === 0) {
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
    jsonData.value = createOzDataset('/CARD/cardCoDivPayList.ozr', {
      CSV_DATA: cardCoDivPayListData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '카드사별 지불내역 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
    });
    isActive.value = true;
  } catch (error) {
    alert(`카드사별 지불내역 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
