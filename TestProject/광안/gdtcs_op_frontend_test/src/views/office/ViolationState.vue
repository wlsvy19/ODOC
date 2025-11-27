<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  </div>
  <OZReportComponent :json-data="getReportRequestData" :iframe-height="`calc(100vh - ${heightOffset}px)`" />
</template>

<script setup>
import { btnHandler, createOzDataset, request, showMessage } from '@/utils/common';
import { computed, onMounted, ref } from 'vue';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import dayjs from 'dayjs';
import { useRoute } from 'vue-router';

const appTitle = '위반차량 수납현황';
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

const searchHeader = [
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
];

const searchData = ref({
  IC_CODE: authStore.getIcCode,
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const reportParams = ref({
  CSV_DATA: [{}],
  IC_CODE: authStore.getIcCode,
  IC_NAME: authStore.getIcNm,
  START_DATE: searchData.value.START_DATE,
  END_DATE: searchData.value.END_DATE,
  REPORT_NM: appTitle,
  APPROVAL_IMG_URL: '',
});

const getReportRequestData = computed(() => {
  return createOzDataset('/OFFICE/violationState.ozr', reportParams.value);
});

const handleSearch = async () => {
  isLoading.value = true;
  try {
    const response = await request('post', 'api/office/getViolationState', {
      IC_CODE: authStore.getIcCode,
      START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
      END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
    });
    reportParams.value.START_DATE = searchData.value.START_DATE;
    reportParams.value.END_DATE = searchData.value.END_DATE;
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
