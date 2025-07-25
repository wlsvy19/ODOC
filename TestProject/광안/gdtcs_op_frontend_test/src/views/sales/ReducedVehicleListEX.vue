<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    scroll-key="ReducedVehicleListEx"
    :height-offset="284"
    :headers="mainHeaders"
    :contents="mainContents"
    :footer-contents="footerContents"
    row-type="1"
  />
  <OZReportDialog v-model:is-active="isActiveViewer" v-model:json-data="reportRequestData" />
</template>

<script setup>
import { ref, onActivated, nextTick, watch, computed } from 'vue';
import { request, btnHandler, comma, createOzDataset, getWorkNo, getSystemSmallCode, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const isLoading = ref(false);
const isActiveViewer = ref(false);

const authStore = useAuthStore();
const route = useRoute();
const appTitle = `감면차량 이용내역(도로공사)`;
const appCode = computed(() => route.path).value.replace('/', '');

const reportConfirmInfo = ref({});
const reportRequestData = ref('');

const carTypeOption = ref([]);
const ecardAttOption = ref([]);
const selectOptWorkNo = ref([]);
const sourceDivOption = ref([
  { title: '전체', value: '' },
  { title: '긴급면제카드', value: '긴급면제카드' },
  { title: '할인단말기', value: '할인단말기' },
  { title: '통합복지', value: '통합복지' },
]);

onActivated(async () => {
  carTypeOption.value = Object.values(
    getSystemSmallCode('171').reduce((acc, item) => {
      if (!acc[item.title]) {
        acc[item.title] = { ...item, value: '' };
      }
      acc[item.title].value = item.title;
      return acc;
    }, {}),
  );
  carTypeOption.value.unshift({
    title: '전체',
    value: '',
  });
  ecardAttOption.value = getSystemSmallCode('290', true);
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
  { label: '발생구분', key: 'SOURCE_DIV', type: 'select', option: sourceDivOption, width: '110px' },
  { label: '차종', key: 'CAR_TYPE', type: 'select', option: carTypeOption },
  { label: '카드번호', key: 'CARD_NO', type: 'input', valid: 'digit', maxLength: '19' },
  { label: '처리차량번호', key: 'CAR_NO', type: 'input' },
  { label: '위반데이터', key: 'VLTN_CODE', type: 'checkbox' },
  { label: '정상데이터', key: 'NOML_CODE', type: 'checkbox' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NO: '',
  CARD_NO: '',
  CAR_TYPE: '',
  OBU_ATT: '',
  SOURCE_DIV: '',
  ECARD_ATT: '',
  CAR_NO: '',
  WTHD_FARE: false,
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
  { title: '순번', key: 'ROWSEQ', width: '80' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '80' },
  { title: '일련번호', key: 'HAND_SNO', width: '80' },
  { title: '처리시간', key: 'HAND_DT_DP', width: '160' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: '처리차종', key: 'CAR_TYPE_DP', width: '70' },
  { title: '발생구분', key: 'SOURCE_DIV', width: '90' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '120' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금전잔액', key: 'WTHD_BEF_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금액', key: 'WTHD_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납후잔액', key: 'WTHD_AFT_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납구분', key: 'ECARD_PAY_DIV_DP', width: '120' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '120' },
  { title: '전자카드속성', key: 'ECARD_ATT_DP', width: '150' },
  { title: '단말기속성', key: 'OBU_ATT_DP', width: '160' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '160' },
  { title: '지불처리구분', key: 'PAY_HAND_DIV_DP', width: '120' },
]);

const mainContents = ref([]);

const allTotalCount = ref('0');
const allTotalOriginFare = ref('0');
const allTotalWithdrawalFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: allTotalCount, unit: '건' },
  { title: '원통행요금', value: allTotalOriginFare, unit: '원' },
  { title: '출금액(환급)', value: allTotalWithdrawalFare, unit: '원' },
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
    const data = await request('post', 'api/sales/getReducedVehicleListEx', {
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

const handleExcel = async () => {
  const row = 1;
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = mainHeaders.value;
    excelDownload(row, searchHeader.value, prevSearchData.value, excelHeaders, mainContents.value, appTitle, appTitle, [
      footerContents.value
        .map((item) => {
          return `${item.title}: ${item.value}`;
        })
        .join(', '),
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

const handlePrint = () => {
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  reportRequestData.value = createOzDataset('/SALES/ReducedVehicleListEx.ozr', {
    CSV_DATA: mainContents.value,
    IC_NAME: authStore.getIcNm,
    TITLE_NM: appTitle,
    IC_CODE: authStore.getIcCode,
    APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
    ...getCondition(searchHeader.value, prevSearchData.value),
    TOTAL_COUNT: allTotalCount.value + '건',
    TOTAL_ORIGIN_FARE: allTotalOriginFare.value + '원',
    TOTAL_WHTD_FARE: allTotalWithdrawalFare.value + '원',
  });
  isActiveViewer.value = true;
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped></style>
