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

const appTitle = `일간 하이패스 이용 현황`;
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

const searchHeader = ref([{ label: '근무일자', key: 'WORK_DATE', type: 'date', dateType: 'day' }]);

const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
});

const reportParams = ref({
  CSV_DATA: [{}],
  IC_CODE: authStore.getIcCode,
  IC_NAME: authStore.getIcNm,
  WORK_DATE: searchData.value.WORK_DATE,
  REPORT_NM: appTitle,
  APPROVAL_IMG_URL: '',
});

const getReportRequestData = computed(() => {
  return createOzDataset('/TRAFFIC/dailyHipassUsageStatus.ozr', reportParams.value);
});

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/traffic/getDailyHipassUsageStatus', {
      IC_CODE: authStore.getIcCode,
      WORK_DATE: searchData.value.WORK_DATE.replaceAll('-', ''),
    });
    reportParams.value.WORK_DATE = searchData.value.WORK_DATE;
    reportParams.value.CSV_DATA = response;
    reportParams.value.APPROVAL_IMG_URL = ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE;
  } catch (error) {
    showMessage(`${error}`, 'error');
  } finally {
    isLoading.value = false;
  }
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped />
