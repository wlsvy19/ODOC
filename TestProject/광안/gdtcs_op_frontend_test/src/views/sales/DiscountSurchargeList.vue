<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="DiscountSurchargeList"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    :footer-contents="footerContents"
  />
  <OZReportDialog v-model:is-active="isActiveViewer" :json-data="reportRequestData" />
</template>

<script setup>
import { computed, nextTick, onActivated, reactive, ref } from 'vue';
import { request, btnHandler, showMessage, createOzDataset, getSystemSmallCode, getCondition, getWorkNo, getLaneNo } from '@/utils/common';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';
import { useRoute } from 'vue-router';

const appTitle = `할인 할증 처리내역`;
const isLoading = ref(false);
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;

const selectOptDcDiv = ref([]);
const selectOptLaneNo = ref([]);

onActivated(async () => {
  selectOptDcDiv.value = getSystemSmallCode('059', true);
  selectOptLaneNo.value = await getLaneNo();
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});

const searchHeader = ref([
  { label: '근무기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '130px' },
  { label: '처리구분', key: 'DC_DIV', type: 'select', option: selectOptDcDiv, width: '125px' },
  { label: '차등요금제 제외', key: 'EXCLUDE_DIFF_DIV', type: 'checkbox' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    DC_DIV: '',
    EXCLUDE_DIFF_DIV: true,
  },
  prev: {},
});

const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '80' },
  { title: '처리시간', key: 'HAND_DT_DP', width: '150' },
  { title: '처리구분', key: 'DC_DIV_DP', width: '100' },
  { title: '지불처리구분', key: 'ECARD_PAY_DIV_DP', width: '110' },
  { title: '차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '80' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '80' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '80' },
  { title: '할인금액', key: 'DC_FARE', width: '80' },
  { title: '할인율', key: 'DISCOUNT_RATE', width: '70' },
  { title: '전자카드번호', key: 'CARD_NO_DP', width: '160' },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const subTotalSumOriginFare = ref('0');
const subTotalSumPayFare = ref('0');
const subTotalDCFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '원통행요금', value: subTotalSumOriginFare, unit: '원' },
  { title: '수납요금', value: subTotalSumPayFare, unit: '원' },
  { title: '할인요금', value: subTotalDCFare, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumOriginFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumPayFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.PAY_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalDCFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.DC_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getDiscountSurchargeList', {
      ...searchData.current,
      ...{
        START_DATE: searchData.current['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.current['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    console.log(`output->response`,response)
    searchData.prev = { ...searchData.current };
    mainContents.value = response;
    if (mainContents.value.length === 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'success');
      return;
    }
    setFooterContents();
  } catch (error) {
    showMessage(error, 'error');
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
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle, [
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

//#region Report
const isActiveViewer = ref(false);
const reportRequestData = ref('');

const handlePrint = () => {
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
  } else {
    reportRequestData.value = createOzDataset('/SALES/discountSurchargeList.ozr', {
      CSV_DATA: mainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, searchData.prev),
      TOTAL_COUNT: subTotalCount.value,
      TOTAL_ORIGIN_FARE: subTotalSumOriginFare.value,
      TOTAL_PAY_FARE: subTotalSumPayFare.value,
    });
    isActiveViewer.value = true;
  }
};
//#endregion

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>
