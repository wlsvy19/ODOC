<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :headers="headers"
    :contents="equipErrorCumData"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
    rowType="1"
    scrollKey="equipErrorCumData"
  />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, getLaneNo, getSystemSmallCode, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const authStore = useAuthStore();
const equipTypeOption = ref([]);
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');
const selectOptLaneNo = ref([]);
onActivated(async () => {
  selectOptLaneNo.value = await getLaneNo();
  await nextTick();
});
equipTypeOption.value = getSystemSmallCode('095', true);

const headers = ref([
  { title: '차로', key: 'LANE_NM', width: '100' },
  { title: '기종', key: 'KIND', width: '150' },
  { title: '세부구분', key: 'DETAIL', width: '200' },
  { title: '발생 건수', key: 'PCNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '복구 건수', key: 'BCNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '복구 비율', key: 'RECVR_YN', width: '100' },
]);

const searchHeader = ref([
  { label: '발생기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차로명', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '100px' },
  { label: '기종', key: 'MDL', type: 'select', option: equipTypeOption, width: '100px' },
]);
const setCustomBodyRowStyleSummary = (item) => {
  if (item.LANE_NM.substr(-2) === '합계') {
    return 'table-body-style-summary-2';
  }
  return '';
};
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  MDL: '',
  LANE_NO: '',
});

const equipErrorCumData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/maint/getEquipErrorCum', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    equipErrorCumData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`장비이상누계조회 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (equipErrorCumData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, equipErrorCumData.value, '장비이상누계조회', '장비이상누계조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (equipErrorCumData.value.length === 0) {
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
    jsonData.value = createOzDataset('/MAINT/equipErrorCum.ozr', {
      CSV_DATA: equipErrorCumData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '장비이상누계조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
    });
    isActive.value = true;
  } catch (error) {
    alert(`장비이상누계조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
