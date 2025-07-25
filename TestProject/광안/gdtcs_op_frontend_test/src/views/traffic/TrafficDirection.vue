<template>
  <v-row>
    <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  </v-row>
  <v-tabs v-model="tab" bg-color="#F5F5F5" :color="colorStore.basicColor" height="25">
    <v-tab class="font-bold-ac" style="font-size: 13px">기간별</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">일별</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        :heightOffset="310"
        rowType="2"
        scrollKey="trafficDirectionPeriodData"
        :headers="periodHeaders"
        :contents="trafficDirectionPeriodData"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="310"
        rowType="2"
        scrollKey="trafficDirectionDayData"
        :headers="dayHeaders"
        :contents="trafficDirectionDayData"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
      />
    </v-window-item>
  </v-window>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset } from '@/utils/common';
import dayjs from 'dayjs';
import { useColorStore } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');

const authStore = useAuthStore();
const isActive = ref(false);
const jsonData = ref('');

const colorStore = useColorStore();

const isLoading = ref(false);

const periodHeaders = ref([
  {
    title: '구분',
    children: [
      { title: '시간대', key: 'TRF_HR', width: '100' },
      { title: '교통량', key: 'TRF_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '상행',
    children: [
      { title: '경형', key: 'UP_LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'UP_SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'UP_FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'UP_SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'UP_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '하행',
    children: [
      { title: '경형', key: 'DOWN_LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'DOWN_SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'DOWN_FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'DOWN_SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'DOWN_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const dayHeaders = ref([
  {
    title: '구분',
    children: [
      { title: '날짜', key: 'TRF_DATE', width: '100' },
      { title: '요일', key: 'TRF_DAY', width: '100' },
      { title: '시간대', key: 'TRF_HR', width: '100' },
      { title: '교통량', key: 'TRF_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '상행',
    children: [
      { title: '경형', key: 'UP_LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'UP_SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'UP_FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'UP_SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'UP_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '하행',
    children: [
      { title: '경형', key: 'DOWN_LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'DOWN_SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'DOWN_FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'DOWN_SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'DOWN_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const tab = ref(0);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.TRF_HR === '합계') {
    return 'table-body-style-summary-1';
  } else if (item.TRF_DAY === '소계') {
    return 'table-body-style-summary-2';
  } else if (item.TRF_DAY === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};
const trafficDirectionPeriodData = ref([]);
const trafficDirectionDayData = ref([]);
const ozSearchPeriodData = ref([]);
const ozSearchDayData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    if (tab.value === 0) {
      const data = await request('post', 'api/traffic/getTrafficDirectionPeriod', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      trafficDirectionPeriodData.value = data;
      ozSearchPeriodData.value = JSON.parse(JSON.stringify(searchData.value));

      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
    } else if (tab.value === 1) {
      const data = await request('post', 'api/traffic/getTrafficDirectionDay', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      trafficDirectionDayData.value = data;
      ozSearchDayData.value = JSON.parse(JSON.stringify(searchData.value));

      if (data.length == 0) {
        alert(`데이터가 없습니다.`);
      }
    }
  } catch (error) {
    alert(`방향별 교통량 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 2;
  if (tab.value === 0) {
    if (trafficDirectionPeriodData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchPeriodData.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeaders = periodHeaders.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeaders,
        trafficDirectionPeriodData.value,
        '방향별 교통량 조회-기간별',
        '방향별 교통량 조회-기간별',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 1) {
    if (trafficDirectionDayData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDayData.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeaders = dayHeaders.value;
      excelDownload(
        row,
        searchHeader.value,
        searchData.value,
        excelHeaders,
        trafficDirectionDayData.value,
        '방향별 교통량 조회-일별',
        '방향별 교통량 조회-일별',
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
    if (tab.value === 0) {
      if (trafficDirectionPeriodData.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchPeriodData.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/TRAFFIC/trafficDirectionPeriod.ozr', {
        CSV_DATA: trafficDirectionPeriodData.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        TITLE_NM: '방향별 교통량 조회-방향별',
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      });
    } else {
      if (trafficDirectionDayData.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDayData.value) !== JSON.stringify(searchData.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/TRAFFIC/trafficDirectionDay.ozr', {
        CSV_DATA: trafficDirectionDayData.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        TITLE_NM: '방향별 교통량 조회-일별',
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      });
    }
    isActive.value = true;
  } catch (error) {
    alert(`방향별 교통량 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
</style>
