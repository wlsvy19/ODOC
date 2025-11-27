<template>
  <v-row>
    <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  </v-row>
  <v-tabs v-model="tab" class="tab-container" bg-color="#F5F5F5" :color="colorStore.basicColor" height="25">
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(정상집계)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(환불집계)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(정상통행일마감)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(환불통행일마감)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(실시간차량)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행(환불심사)</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">연속통행 교통량집계(시간별,차로별)</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        :heightOffset="310"
        :headers="headers"
        :contents="nomalSumContent"
        rowType="2"
        scrollKey="nomalSumContent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="310"
        :headers="headers"
        :contents="refundSumcontent"
        rowType="2"
        scrollKey="refundSumcontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="345"
        :headers="headers"
        :contents="nomalDayFincontent"
        rowType="2"
        scrollKey="nomalDayFincontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
      <div class="work-count-container">
        <span class="total-work">[소계] 건수</span>
        <span><input class="total-count" :value="comma(nomalDayFinFoot.allTotalCount)" disabled /> 건</span>
        <span class="total-work">할인금액</span>
        <span><input class="total-count" :value="comma(nomalDayFinFoot.allDCFare)" disabled /> 원</span>
      </div>
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="345"
        :headers="headers"
        :contents="refundDayFincontent"
        rowType="2"
        scrollKey="refundDayFincontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
      <div class="work-count-container">
        <span class="total-work">[소계] 건수</span>
        <span><input class="total-count" :value="comma(refundDayFinFoot.allTotalCount)" disabled /> 건</span>
        <span class="total-work">할인금액</span>
        <span><input class="total-count" :value="comma(refundDayFinFoot.allDCFare)" disabled /> 원</span>
      </div>
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="345"
        :headers="headers"
        :contents="realTimecontent"
        rowType="2"
        scrollKey="realTimecontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
      <div class="work-count-container">
        <span class="total-work">[소계] 건수</span>
        <span><input class="total-count" :value="comma(realTimeFoot.allTotalCount)" disabled /> 건</span>
        <span class="total-work">할인금액</span>
        <span><input class="total-count" :value="comma(realTimeFoot.allDCFare)" disabled /> 원</span>
      </div>
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="345"
        :headers="headers"
        :contents="refundReviewcontent"
        rowType="2"
        scrollKey="refundReviewcontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
      <div class="work-count-container">
        <span class="total-work">[소계] 건수</span>
        <span><input class="total-count" :value="comma(refundReviewFoot.allTotalCount)" disabled /> 건</span>
      </div>
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="285"
        :headers="headers"
        :contents="trafficSumcontent"
        rowType="1"
        scrollKey="trafficSumcontent"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
    </v-window-item>
  </v-window>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, watch, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, getSystemSmallCode, createOzDataset, comma, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useColorStore } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const colorStore = useColorStore();
const authStore = useAuthStore();
const preICOption = ref([]);

preICOption.value = getSystemSmallCode('370', true);

const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');
const nomalDayFinFoot = ref({});
const refundDayFinFoot = ref({});
const realTimeFoot = ref({});
const refundReviewFoot = ref({});

const headers = ref([]);
const searchHeader = ref([]);
const sumHeaders = ref([
  {
    title: '구분',
    children: [
      { title: '이전영업소', key: 'BEIC_IC_CODE_NM', width: '100' },
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
      { title: '차로구분', key: 'LANE_DIV', width: '100' },
      { title: '카드종류', key: 'ECARD_TYPE', width: '150' },
    ],
  },
  {
    title: '대수',
    children: [
      { title: '경형', key: 'LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '합계', key: 'CARTYPE_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '금액',
    children: [
      { title: '경형', key: 'LIGHT_CAR_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'SMALLSIZE_CAR_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'FULLSIZED_CAR_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'SPECIAL_CAR_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '합계', key: 'CARTYPE_TOTAL_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);
const contentPreHeaders = ref([
  {
    title: '이전영업소',
    children: [
      { title: '순번', key: 'ROW_SEQ', width: '100' },
      { title: '통과시간', key: 'BEIC_HAND_DT', width: '150' },
      { title: '영업소명', key: 'BEIC_IC_CODE_NM', width: '100' },
      { title: '근무번호', key: 'BEIC_WORK_NO', width: '100' },
      { title: '일련번호', key: 'BEIC_HAND_SNO', width: '100' },
      { title: '차종', key: 'BEIC_CAR_TYPE', width: '100' },
    ],
  },
]);

const contentGDTCSHeaders = ref([
  {
    title: '광안대로',
    children: [
      { title: '통과시간', key: 'HAND_DT', width: '150' },
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
      { title: '차로명', key: 'LANE_NM', width: '100' },
      { title: '근무번호', key: 'WORK_NO', width: '100' },
      { title: '일련번호', key: 'HAND_SNO', width: '100' },
      { title: '차종', key: 'CAR_TYPE', width: '100' },
      { title: '차량번호', key: 'CAR_NO', width: '100' },
      { title: '카드번호', key: 'CARD_NO', width: '150' },
      { title: 'OBU번호', key: 'OBU_NO', width: '150' },
      { title: '위반코드', key: 'VLTN_CODE', width: '120' },
      { title: '원통행요금', key: 'ORIGIN_PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수납금액', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '할인금액', key: 'DC_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '출금전카드잔액', key: 'WTHD_BEF_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '카드종류', key: 'CARD_DIV', width: '100' },
      { title: '수납구분', key: 'PAY_DIV', width: '100' },
    ],
  },
]);

const contentRealTimeHeaders = ref([
  {
    title: '광안대로',
    children: [
      { title: '통과시간', key: 'HAND_DT', width: '150' },
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
      { title: '차로명', key: 'LANE_NM', width: '100' },
      { title: '근무번호', key: 'WORK_NO', width: '100' },
      { title: '일련번호', key: 'HAND_SNO', width: '100' },
      { title: '차종', key: 'CAR_TYPE', width: '100' },
      { title: '차량번호', key: 'CAR_NO', width: '100' },
      { title: '카드번호', key: 'CARD_NO', width: '150' },
      { title: 'OBU번호', key: 'OBU_NO', width: '150' },
      { title: '위반코드', key: 'VLTN_CODE', width: '120' },
      { title: '원통행요금', key: 'ORIGIN_PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수납금액', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '할인금액', key: 'DC_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '카드종류', key: 'CARD_DIV', width: '100' },
      { title: '수납구분', key: 'PAY_DIV', width: '100' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.BEIC_IC_CODE_NM === '총합계' || item.LANE_NM === '총합계') {
    return 'table-body-style-summary-1';
  } else if (item.WORK_DATE === '합계' || item.ECARD_TYPE === '소계') {
    return 'table-body-style-summary-2';
  }
  return '';
};

const tab = ref(0);
watch(
  tab,
  () => {
    searchHeader.value =
      tab.value == 2 || tab.value == 4
        ? [
            { label: '근무일자', key: 'START_DATE', type: 'date' },
            { label: '근무번호', key: 'WORK_NO', type: 'input', valid: 'digit', maxLength: '4' },
          ]
        : tab.value == 3
        ? [
            { label: '환불일자', key: 'START_DATE', type: 'date' },
            { label: '근무번호', key: 'WORK_NO', type: 'input', valid: 'digit', maxLength: '4' },
          ]
        : tab.value == 5
        ? [
            { label: '조회기간', key: 'START_DATE', type: 'date' },
            { label: '~', key: 'END_DATE', type: 'date' },
            { label: '카드번호', key: 'CARD_NO', type: 'input', valid: 'digit', maxLength: '19' },
            { label: '차량번호', key: 'CAR_NO', type: 'input', valid: 'digit|korean', width: '110px', maxLength: '10' },
          ]
        : tab.value == 6
        ? [
            { label: '조회기간', key: 'START_DATE', type: 'date' },
            { label: '~', key: 'END_DATE', type: 'date' },
          ]
        : [
            { label: '조회기간', key: 'START_DATE', type: 'date' },
            { label: '~', key: 'END_DATE', type: 'date' },
            { label: '이전영업소', key: 'BEIC_IC_CODE', type: 'select', option: preICOption, width: '110px' },
          ];

    headers.value =
      tab.value == 2
        ? [...contentPreHeaders.value, ...contentGDTCSHeaders.value]
        : tab.value == 4
        ? [...contentPreHeaders.value, ...contentRealTimeHeaders.value]
        : tab.value == 3
        ? [
            {
              title: '이전영업소',
              children: [
                { title: '순번', key: 'ROW_SEQ', width: '100' },
                { title: '영업소명', key: 'BEIC_IC_CODE_NM', width: '100' },
                { title: '근무번호', key: 'BEIC_WORK_NO', width: '100' },
                { title: '일련번호', key: 'BEIC_HAND_SNO', width: '100' },
                { title: '차종', key: 'BEIC_CAR_TYPE', width: '100' },
              ],
            },
            ...contentGDTCSHeaders.value,
          ]
        : tab.value == 5
        ? [
            ...contentPreHeaders.value,
            {
              title: '광안대로',
              children: [
                { title: '처리유형', key: 'HAND_TYPE', width: '100' },
                { title: '통과시간', key: 'HAND_DT', width: '150' },
                { title: '근무일자', key: 'WORK_DATE', width: '100' },
                { title: '차로명', key: 'LANE_NM', width: '100' },
                { title: '근무번호', key: 'WORK_NO', width: '100' },
                { title: '일련번호', key: 'HAND_SNO', width: '100' },
                { title: '차종', key: 'CAR_TYPE', width: '100' },
                { title: '차량번호', key: 'CAR_NO', width: '100' },
                { title: '카드번호', key: 'CARD_NO', width: '150' },
                { title: 'OBU번호', key: 'OBU_NO', width: '150' },
                { title: '위반코드', key: 'VLTN_CODE', width: '120' },
                { title: '원통행요금', key: 'ORIGIN_PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
                { title: '수납금액', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
                { title: '할인금액', key: 'DC_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
                { title: '출금전카드잔액', key: 'PAY_BEF_BALC', width: '100', customBodyCellStyle: 'table-body-style-right' },
                { title: '카드종류', key: 'CARD_DIV', width: '100' },
                { title: '수납구분', key: 'PAY_DIV', width: '100' },
              ],
            },
          ]
        : tab.value == 6
        ? [
            { title: '차로명', key: 'LANE_NM', width: '100' },
            { title: '합계', key: 'TRF_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '00시', key: 'TIME_00', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '01시', key: 'TIME_01', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '02시', key: 'TIME_02', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '03시', key: 'TIME_03', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '04시', key: 'TIME_04', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '05시', key: 'TIME_05', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '06시', key: 'TIME_06', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '07시', key: 'TIME_07', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '08시', key: 'TIME_08', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '09시', key: 'TIME_09', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '10시', key: 'TIME_10', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '11시', key: 'TIME_11', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '12시', key: 'TIME_12', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '13시', key: 'TIME_13', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '14시', key: 'TIME_14', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '15시', key: 'TIME_15', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '16시', key: 'TIME_16', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '17시', key: 'TIME_17', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '18시', key: 'TIME_18', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '19시', key: 'TIME_19', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '20시', key: 'TIME_20', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '21시', key: 'TIME_21', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '22시', key: 'TIME_22', width: '100', customBodyCellStyle: 'table-body-style-right' },
            { title: '23시', key: 'TIME_23', width: '100', customBodyCellStyle: 'table-body-style-right' },
          ]
        : [...sumHeaders.value];
  },
  {
    immediate: true,
  },
);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NO: '',
  CARD_NO: '',
  CAR_NO: '',
  BEIC_IC_CODE: '',
});

const nomalSumContent = ref([]);
const refundSumcontent = ref([]);
const nomalDayFincontent = ref([]);
const refundDayFincontent = ref([]);
const realTimecontent = ref([]);
const refundReviewcontent = ref([]);
const trafficSumcontent = ref([]);
nomalDayFinFoot.value = {
  allTotalCount: 0,
  allDCFare: 0,
};
refundDayFinFoot.value = {
  allTotalCount: 0,
  allDCFare: 0,
};
realTimeFoot.value = {
  allTotalCount: 0,
  allDCFare: 0,
};
refundReviewFoot.value = {
  allTotalCount: 0,
};
const ozSearchDataSum = ref([]);
const ozSearchDataRefundSum = ref([]);
const ozSearchDataFin = ref([]);
const ozSearchDataRefundFin = ref([]);
const ozSearchDataRealTime = ref([]);
const ozSearchDataRefundReview = ref([]);
const ozSearchDataTraffic = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;

    if (tab.value === 0) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCSum', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          LPAY_CRCT_YN: '0',
        },
      });
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }

      nomalSumContent.value = data;
      ozSearchDataSum.value = JSON.parse(JSON.stringify(searchData.value));
    } else if (tab.value === 1) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCSum', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          LPAY_CRCT_YN: '1',
        },
      });
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      refundSumcontent.value = data;
      ozSearchDataRefundSum.value = JSON.parse(JSON.stringify(searchData.value));
    } else if (tab.value === 2) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCDayFin', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          LPAY_CRCT_YN: '0',
        },
      });
      const allTotalCount = ref(data.length);
      const allDCFare = ref(0);
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      data.map((obj) => {
        allDCFare.value += Number(obj.IDC_FARE);
      });
      nomalDayFincontent.value = data;
      ozSearchDataFin.value = JSON.parse(JSON.stringify(searchData.value));
      nomalDayFinFoot.value = {
        allTotalCount: allTotalCount.value,
        allDCFare: allDCFare.value,
      };
    } else if (tab.value === 3) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCDayFin', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          LPAY_CRCT_YN: '1',
        },
      });
      const allTotalCount = ref(data.length);
      const allDCFare = ref(0);
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      data.map((obj) => {
        allDCFare.value += Number(obj.IDC_FARE);
      });
      refundDayFincontent.value = data;
      ozSearchDataRefundFin.value = JSON.parse(JSON.stringify(searchData.value));
      refundDayFinFoot.value = {
        allTotalCount: allTotalCount.value,
        allDCFare: allDCFare.value,
      };
    } else if (tab.value === 4) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCRealTime', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      const allTotalCount = ref(data.length);
      const allDCFare = ref(0);
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      data.map((obj) => {
        allDCFare.value += Number(obj.IDC_FARE);
      });
      realTimecontent.value = data;
      ozSearchDataRealTime.value = JSON.parse(JSON.stringify(searchData.value));
      realTimeFoot.value = {
        allTotalCount: allTotalCount.value,
        allDCFare: allDCFare.value,
      };
    } else if (tab.value === 5) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCRefund', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      refundReviewcontent.value = data;
      ozSearchDataRefundReview.value = JSON.parse(JSON.stringify(searchData.value));
      const allTotalCount = ref(data.length);
      refundReviewFoot.value = {
        allTotalCount: allTotalCount.value,
      };
    } else if (tab.value === 6) {
      const data = await request('post', 'api/sales/getBusanContinueTrafficDCTrfSum', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
      trafficSumcontent.value = data;
      ozSearchDataTraffic.value = JSON.parse(JSON.stringify(searchData.value));
    }
  } catch (error) {
    alert(`부산시 연속통행할인 현황 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 2;
  if (tab.value === 0) {
    if (nomalSumContent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataSum.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        nomalSumContent.value,
        '부산시 연속통행할인 현황-연속통행(정상집계)',
        '부산시 연속통행할인 현황-연속통행(정상집계)',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 1) {
    if (refundSumcontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataRefundSum.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;

      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        refundSumcontent.value,
        '부산시 연속통행할인 현황-연속통행(환불집계)',
        '부산시 연속통행할인 현황-연속통행(환불집계)',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 2) {
    if (nomalDayFincontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataFin.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        nomalDayFincontent.value,
        '부산시 연속통행할인 현황-연속통행(정상통행일마감)',
        '부산시 연속통행할인 현황-연속통행(정상통행일마감)',
        [`[소계] 건수: ${comma(nomalDayFinFoot.value.allTotalCount)} 건  할인금액: ${comma(nomalDayFinFoot.value.allDCFare)} 원`],
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 3) {
    if (refundDayFincontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataRefundFin.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        refundDayFincontent.value,
        '부산시 연속통행할인 현황-연속통행(환불통행일마감)',
        '부산시 연속통행할인 현황-연속통행(환불통행일마감)',
        [`[소계] 건수: ${comma(refundDayFinFoot.value.allTotalCount)} 건  할인금액: ${comma(refundDayFinFoot.value.allDCFare)} 원`],
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 4) {
    if (realTimecontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataRealTime.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        realTimecontent.value,
        '부산시 연속통행할인 현황-연속통행(실시간차량)',
        '부산시 연속통행할인 현황-연속통행(실시간차량)',
        [`[소계] 건수: ${comma(realTimeFoot.value.allTotalCount)} 건  할인금액: ${comma(realTimeFoot.value.allDCFare)} 원`],
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 5) {
    if (refundReviewcontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataRefundReview.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        refundReviewcontent.value,
        '부산시 연속통행할인 현황-연속통행(환불심사)',
        '부산시 연속통행할인 현황-연속통행(환불심사)',
        [`[소계] 건수: ${comma(refundReviewFoot.value.allTotalCount)} 건 `],
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 6) {
    if (trafficSumcontent.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataTraffic.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    const row = 1;
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeader = headers.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeader,
        trafficSumcontent.value,
        '부산시 연속통행할인 현황-연속통행 교통량집계(시간별 차로별)',
        '부산시 연속통행할인 현황-연속통행 교통량집계(시간별 차로별)',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
    if (tab.value == 0) {
      if (nomalSumContent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataSum.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueNormalSumRefundSum.ozr', {
        CSV_DATA: nomalSumContent.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '연속통행(정상집계)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataSum.value),
      });
    } else if (tab.value == 1) {
      if (refundSumcontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataRefundSum.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueNormalSumRefundSum.ozr', {
        CSV_DATA: refundSumcontent.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '연속통행(환불집계)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataRefundSum.value),
      });
    } else if (tab.value == 2) {
      if (nomalDayFincontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataFin.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueDayFin.ozr', {
        CSV_DATA: nomalDayFincontent.value,
        START_DATE: searchData.value['START_DATE'],
        TITLE_NM: '연속통행(정상통행일마감)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataFin.value),
        TOTAL_COUNT: comma(nomalDayFinFoot.value.allTotalCount) + '건',
        DC_FARE: comma(nomalDayFinFoot.value.allDCFare) + '원',
      });
    } else if (tab.value == 3) {
      if (refundDayFincontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataRefundFin.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueRefundDayFin.ozr', {
        CSV_DATA: refundDayFincontent.value,
        START_DATE: searchData.value['START_DATE'],
        TITLE_NM: '연속통행(환불통행일마감)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataRefundFin.value),
        TOTAL_COUNT: comma(refundDayFinFoot.value.allTotalCount) + '건',
        DC_FARE: comma(refundDayFinFoot.value.allDCFare) + '원',
      });
    } else if (tab.value == 4) {
      if (realTimecontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataRealTime.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueRealTimeTraffic.ozr', {
        CSV_DATA: realTimecontent.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '연속통행(실시간차량)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataRealTime.value),
        TOTAL_COUNT: comma(realTimeFoot.value.allTotalCount) + '건',
        DC_FARE: comma(realTimeFoot.value.allDCFare) + '원',
      });
    } else if (tab.value == 5) {
      if (refundReviewcontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataRefundReview.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueRefund.ozr', {
        CSV_DATA: refundReviewcontent.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '연속통행(환불심사)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataRefundReview.value),
        TOTAL_COUNT: comma(refundReviewFoot.value.allTotalCount) + '건',
      });
    } else if (tab.value == 6) {
      if (trafficSumcontent.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataTraffic.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/busanContinueTraffic.ozr', {
        CSV_DATA: trafficSumcontent.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '연속통행 교통량집계(시간별,차로별)',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchDataTraffic.value),
      });
    }
    isActive.value = true;
  } catch (error) {
    alert(`부산시 연속통행할인 현황 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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

<style scoped>
.v-tab {
  color: #a9a9a9;
}

.work-count-container {
  border-top: 1px solid #d3d3d3;
  font-size: 12px;
}

.total-work {
  margin-left: 10px;
}

.total-count {
  width: 80px;
  text-align: center;
  margin: 10px 0px 0px 10px;
  border-radius: 3px;
  border: 1px solid #d3d3d3;
  font-weight: 400;
}
</style>
