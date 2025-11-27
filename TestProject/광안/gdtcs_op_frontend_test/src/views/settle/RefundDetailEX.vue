<template>
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="getSearchHeaders" v-model="searchData" @keyup.enter="handleSearch">
      <template v-slot:header_btn>
        <InformationComponent message="통합복지카드(위치기반) 감면심사에 따른 내역입니다." icon-type="information" />
      </template>
    </SearchDataComponent>
  </div>
  <v-tabs v-model="tab" class="tab-container" bg-color="#F5F5F5" :color="colorStore.basicColor" height="25">
    <v-tab class="font-bold-ac" style="font-size: 13px">환불이체 요청내역 조회</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">환불이체 요청결과 조회</v-tab>
  </v-tabs>
  <TableComponent
    :headers="getHeaders"
    :contents="getContents"
    scrollKey="RuleScroll"
    :height-percent="92"
    rowType="1"
    :footer-contents="footerContents"
  />
</template>

<script setup>
import { ref, computed } from 'vue';
import { useColorStore, useAuthStore } from '@/stores/index';
import { request, btnHandler, makeExcel, isNullOrEmpty, comma } from '@/utils/common';
import dayjs from 'dayjs';

const getSearchHeaders = computed(() => (tab.value == 0 ? requestSearchHeaders : responseSearchHeaders));
const getHeaders = computed(() => (tab.value == 0 ? requestHeaders : responseHeaders));
const getContents = computed(() => (tab.value == 0 ? requestContents : responseContents).value);
const footerContents = computed(() => (tab.value == 0 ? footerRequestContents : footerResponseContents).value);

const requestSearchHeaders = [
  { label: '환급요청일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '통합복지 카드번호', key: 'WCARD_NO', type: 'input', valid: 'digit', width: '160px' },
  { label: '등록차량번호', key: 'INST_CAR_NO', type: 'input', valid: 'digit|korean', width: '120px' },
];
const responseSearchHeaders = [
  { label: '환급요청일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '등록차량번호', key: 'INST_CAR_NO', type: 'input', valid: 'digit|korean', width: '120px' },
];

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WCARD_NO: '',
  INST_CAR_NO: '',
});

const requestHeaders = [
  { title: '환급요청번호', key: 'RFND_RQ_NO', width: '250' },
  { title: '통합복지 카드번호', key: 'WCARD_NO', width: '200' },
  { title: '환급요청일자', key: 'RFND_RQ_DATE', width: '180' },
  { title: '환급금액', key: 'RFND_AMT', width: '130' },
  { title: '예금주(명)', key: 'ACNT_DPST', width: '110' },
  { title: '은행코드', key: 'BANK_CODE', width: '110' },
  { title: '계좌번호', key: 'ACNT_NO', width: '150' },
  { title: '통장인자내역(거래내용)', key: 'BANKBK_NOTE', width: '250' },
  { title: '등록차량번호', key: 'INST_CAR_NO', width: '130' },
];
const responseHeaders = [
  { title: '환급요청번호', key: 'RFND_RQ_NO', width: '250' },
  { title: '환급요청일자', key: 'RFND_RQ_DATE', width: '200' },
  { title: '환급요청응답일자', key: 'RFND_RS_DATE', width: '180' },
  { title: '환급금액', key: 'RFND_AMT', width: '130' },
  { title: '예금주(명)', key: 'ACNT_DPST', width: '110' },
  { title: '은행코드', key: 'BANK_CODE', width: '110' },
  { title: '계좌번호', key: 'ACNT_NO', width: '150' },
  { title: '통장인자내역(거래내용)', key: 'BANKBK_NOTE', width: '250' },
  { title: '처리상태', key: 'LOC_HAND_STAT', width: '120' },
  // { title: '오류코드', key: 'ERR_CODE', width: '' },
  { title: '환급처리일', key: 'RFND_DATE', width: '120' },
  { title: '수수료', key: 'FEE', width: '110' },
  { title: '등록차량번호', key: 'INST_CAR_NO', width: '130' },
];

const footerRequestContents = ref([
  { title: '총 건수', key: 'totalRequestCount', value: 0, unit: '건' },
  { title: '환급금액', key: 'totalRequestFare', value: 0, unit: '원' },
]);

const footerResponseContents = ref([
  { title: '총 건수', key: 'totalResponseCount', value: 0, unit: '건' },
  { title: '환급금액', key: 'totalResponseFare', value: 0, unit: '원' },
  { title: '성공 건수', key: 'successResponseCount', value: 0, unit: '건' },
  { title: '성공 금액', key: 'successResponseFare', value: 0, unit: '원' },
  { title: '실패 건수', key: 'failResponseCount', value: 0, unit: '건' },
  { title: '실패 금액', key: 'failResponseFare', value: 0, unit: '원' },
]);

const requestContents = ref([]);
const responseContents = ref([]);

const tab = ref(0);
const colorStore = useColorStore();
const authStore = useAuthStore();

const setFooterContents = () => {
  if (tab.value === 0) {
    const totals = {
      totalRequestCount: requestContents.value.length,
      totalRequestFare: 0,
    };

    requestContents.value.forEach((item) => {
      totals.totalRequestFare += Number(item.O_RFND_AMT);
    });

    footerRequestContents.value.forEach((footer) => {
      footer.value = comma(totals[footer.key]);
    });
  } else {
    const totals = {
      totalResponseCount: responseContents.value.length,
      totalResponseFare: 0,
      successResponseCount: 0,
      successResponseFare: 0,
      failResponseCount: 0,
      failResponseFare: 0,
    };

    responseContents.value.forEach((item) => {
      totals.totalResponseFare += Number(item.O_RFND_AMT);
      if (item.LOC_HAND_STAT == '성공') {
        totals.successResponseCount++;
        totals.successResponseFare += Number(item.O_RFND_AMT);
      } else if (item.LOC_HAND_STAT == '실패') {
        totals.failResponseCount++;
        totals.failResponseFare += Number(item.O_RFND_AMT);
      }
    });

    footerResponseContents.value.forEach((footer) => {
      footer.value = comma(totals[footer.key]);
    });
  }
};

const handleSearch = async () => {
  const result = await request('post', `/api/settle/${tab.value === 0 ? 'getRefundRequestList' : 'getRefundResponseList'}`, {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
    END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
    WCARD_NO: searchData.value['WCARD_NO'],
    INST_CAR_NO: searchData.value['INST_CAR_NO'],
  });

  if (isNullOrEmpty(result)) (tab.value === 0 ? requestContents : responseContents).value = [];
  else (tab.value === 0 ? requestContents : responseContents).value = result;
  setFooterContents();
};

const handleExcel = () => {
  const tabName = tab.value == 0 ? '환불이체 요청내역 조회' : '환불이체 요청결과 조회';
  console.log(`output->getHeaders`, getHeaders);
  makeExcel(1, getSearchHeaders.value, searchData.value, getHeaders.value, getContents.value, tabName, tabName, [
    footerContents.value
      .map((item) => {
        return `${item.title}: ${item.value} ${item.unit}`;
      })
      .join(', '),
  ]);
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
