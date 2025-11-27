<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    scroll-key="ReducedVehicleListBs"
    :heightOffset="284"
    row-type="1"
    :headers="mainHeaders"
    :contents="mainContents"
    :footer-contents="footerContents"
  />
  <OZReportDialog v-model:is-active="isActive" v-model:json-data="reportRequestData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick, watch, computed } from 'vue';
import { request, btnHandler, comma, createOzDataset, getWorkNo, getSystemSmallCode, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const isLoading = ref(false);
const isActive = ref(false);

const authStore = useAuthStore();
const route = useRoute();
const appTitle = `감면차량 이용내역(부산시)`;
const appCode = computed(() => route.path).value.replace('/', '');

const reportConfirmInfo = ref({});
const reportRequestData = ref('');

const carTypeOption = ref([]);
const selectOptBsExmTypeDtl = ref([]);
const selectOptWorkNo = ref([]);

onActivated(async () => {
  carTypeOption.value = getSystemSmallCode('170', true);
  selectOptBsExmTypeDtl.value = getSystemSmallCode('292', true);
  selectOptWorkNo.value = await getWorkNo(searchData.value.START_DATE, searchData.value.END_DATE);
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '근무번호', key: 'WORK_NO', type: 'select', option: selectOptWorkNo, width: '100px' },
  { label: '면제구분', key: 'BS_EXM_TYPE_DTL', type: 'select', option: selectOptBsExmTypeDtl, width: '130px' },
  { label: '차종', key: 'CAR_TYPE', type: 'select', option: carTypeOption },
  { label: '카드번호', key: 'CARD_NO', type: 'input', valid: 'digit', maxLength: '19' },
  { label: '처리차량번호', key: 'CAR_NO', type: 'input', maxLength: '16' },
  { label: '위반데이터', key: 'VLTN_CODE', type: 'checkbox' },
  { label: '정상데이터', key: 'NOML_CODE', type: 'checkbox' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NO: '',
  CARD_NO: '',
  CAR_NO: '',
  CAR_TYPE: '',
  BS_EXM_TYPE_DTL: '',
  VLTN_CODE: false,
  NOML_CODE: false,
});

const prevSearchData = ref({});

watch([() => searchData.value.START_DATE, () => searchData.value.END_DATE], ([newWorkDateS, newWorkDateE], [oldWorkDateS, oldWorkDateE]) => {
  if (newWorkDateS !== oldWorkDateS || newWorkDateE !== oldWorkDateE) {
    getWorkNo(newWorkDateS, newWorkDateE).then((workList) => {
      selectOptWorkNo.value = workList;
    });
  }
});

const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '80' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '80' },
  { title: '일련번호', key: 'HAND_SNO', width: '80' },
  { title: '처리시간', key: 'HAND_DT_DP', width: '150' },
  { title: '차종', key: 'HAND_CAR_TYPE_DP', width: '70' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '150' },
  { title: '면제구분', key: 'BS_EXM_TYPE_DTL_DP', width: '90' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '120' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금액', key: 'WTHD_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '대표수납구분', key: 'MAIN_PAY_DIV_DP', width: '120' },
  { title: 'OBU속성', key: 'OBU_ATT_DP', width: '140' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '120' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '150' },
  { title: '카드수납구분', key: 'ECARD_PAY_DIV_DP', width: '120' },
  { title: '공차조회결과', key: 'TAXI_EXM_TYPE_DP', width: '95' },
  { title: '공차조회시각', key: 'TRNRCP_DT_DP', width: '150' },
  { title: '비고', key: 'NOTE', width: '200', customBodyCellStyle: 'table-body-style-left' },
]);

const mainContents = ref([]);

const allTotalCount = ref('0');
const allTotalOriginFare = ref('0');
const allTotalWithdrawalFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: allTotalCount, unit: '건' },
  { title: '원통행요금', value: allTotalOriginFare, unit: '원' },
  { title: '출금액', value: allTotalWithdrawalFare, unit: '원' },
]);

const setFooterContents = () => {
  allTotalCount.value = comma(mainContents.value.length);
  allTotalOriginFare.value = comma(
    mainContents.value.reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0),
  );
  allTotalWithdrawalFare.value = comma(
    mainContents.value.reduce((acc, item) => {
      return acc + item.PASS_FARE;
    }, 0),
  );
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/sales/getReducedVehicleListBs', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    prevSearchData.value = { ...searchData.value };
    mainContents.value = data;
    if (mainContents.value.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'success');
    }
    setFooterContents();
  } catch (error) {
    showMessage(`${appTitle} 조회 중 에러가 발생했습니다.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (mainContents.value.length === 0) {
    showMessage('조회된 데이터가 없습니다.', 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = mainHeaders.value;
    excelDownload(row, searchHeader.value, prevSearchData.value, excelHeaders, mainContents.value, appTitle, appTitle, [
      footerContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

const handlePrint = async () => {
  if (mainContents.value.length === 0) {
    showMessage('조회된 데이터가 없습니다.', 'warning');
    return;
  }
  reportRequestData.value = createOzDataset('/SALES/ReducedVehicleListBs.ozr', {
    CSV_DATA: mainContents.value,
    IC_NAME: authStore.getIcNm,
    IC_CODE: authStore.getIcCode,
    TITLE_NM: appTitle,
    APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
    ...getCondition(searchHeader.value, prevSearchData.value),
    TOTAL_COUNT: allTotalCount.value + '건',
    TOTAL_ORIGIN_FARE: allTotalOriginFare.value + '원',
    TOTAL_WHTD_FARE: allTotalWithdrawalFare.value + '원',
  });
  isActive.value = true;
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped />
