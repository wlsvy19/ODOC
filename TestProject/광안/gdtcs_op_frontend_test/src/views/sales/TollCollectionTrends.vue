<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  </div>
  <OZReportComponent :json-data="getReportRequestData" :iframe-height="`calc(100vh - ${heightOffset}px)`" />
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { request, btnHandler, createOzDataset, showMessage } from '@/utils/common';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import { useRoute } from 'vue-router';

const appTitle = `광안대로 통행료 징수관련 동향`;
const isLoading = ref(false);
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

const heightOffset = 189 + 36;

onMounted(async () => {
  reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
    PRG_CODE: appCode,
  });
  handleSearch();
});

const searchHeader = ref([{ label: '조회일자', key: 'WORK_DATE', type: 'date', dateType: 'day' }]);

const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
});

const reportParams = ref({
  CSV_DATA1: [{}],
  CSV_DATA2: [{}],
  CSV_DATA3: [{}],
  CSV_DATA4: [{}],
  IC_CODE: authStore.getIcCode,
  IC_NAME: authStore.getIcNm,
  WORK_DATE: searchData.value.WORK_DATE,
  REPORT_NM: appTitle,
  APPROVAL_IMG_URL: '',
});

const getReportRequestData = computed(() => {
  return createOzDataset('/SALES/dailyTollTrends.ozr', reportParams.value);
});

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getDailyTollTrends', {
      IC_CODE: authStore.getIcCode,
      WORK_DATE: searchData.value.WORK_DATE.replaceAll('-', ''),
    });
    reportParams.value.WORK_DATE = searchData.value.WORK_DATE;
    reportParams.value.CSV_DATA1 = response.DailyAvgContrast;
    reportParams.value.CSV_DATA2 = response.TollTraffic;
    reportParams.value.CSV_DATA3 = response.TollSummary;
    reportParams.value.CSV_DATA4 = response.DailyAvgContrastQuater;
    reportParams.value.APPROVAL_IMG_URL = ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE;
  } catch (error) {
    showMessage(`오류가 발생했습니다.`, 'error');
  } finally {
    isLoading.value = false;
  }
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped />
