<template>
  <v-row>
    <SearchDataComponent v-if="tab == 0" :headers="searchHeader" v-model="searchDate" @keyup.enter="handleSearch" />
    <SearchDataComponent v-else-if="tab == 1" :headers="searchHeader" v-model="searchMonth" @keyup.enter="handleSearch" />
    <SearchDataComponent v-else-if="tab == 2" :headers="searchHeader" v-model="searchYear" @keyup.enter="handleSearch" />
  </v-row>
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab class="font-bold-ac" style="font-size: 13px">일자별</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">월별</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">년도별</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        :heightOffset="189 + 36 + 28 + 29 * 2"
        :headers="dayHeaders"
        :contents="dailyTrafficProcessDayData"
        rowType="mix"
        scrollKey="dailyTrafficProcessDayData"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="189 + 36 + 28 + 29 * 2"
        rowType="mix"
        scrollKey="dailyTrafficProcessMonthData"
        :headers="monthHeaders"
        :contents="dailyTrafficProcessMonthData"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        :heightOffset="189 + 36 + 28 + 29 * 2"
        rowType="mix"
        scrollKey="dailyTrafficProcessYearData"
        :headers="yearHeaders"
        :contents="dailyTrafficProcessYearData"
      />
    </v-window-item>
  </v-window>

  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, watch, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset } from '@/utils/common';
import { useColorStore } from '@/stores/index';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const authStore = useAuthStore();
const colorStore = useColorStore();

const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');

const dayHeaders = ref([
  {
    title: '',
    children: [
      { title: '영업소명', key: 'IC_NM', width: '100' }, // 내가 DB에 넘겨줄 변수명
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
    ],
  },
  {
    title: '교통량 합계',
    children: [
      { title: '건수', key: 'CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '경형',
    children: [
      { title: '건수', key: 'CAR6_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR6_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '소형',
    children: [
      { title: '건수', key: 'CAR1_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR1_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '중형',
    children: [
      { title: '건수', key: 'CAR2_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR2_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '대형',
    children: [
      { title: '건수', key: 'CAR345_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR345_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(일반)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(할인)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_DC_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_DC_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  { title: '면제', key: 'LANE_EXMT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '위반', key: 'ESCP_VLTN', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '집계제외', key: 'LANE_OFCNOT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const monthHeaders = ref([
  {
    title: '',
    children: [
      { title: '영업소명', key: 'IC_NM', width: '100' },
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
    ],
  },
  {
    title: '교통량 합계',
    children: [
      { title: '건수', key: 'CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '경형',
    children: [
      { title: '건수', key: 'CAR6_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR6_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '소형',
    children: [
      { title: '건수', key: 'CAR1_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR1_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '중형',
    children: [
      { title: '건수', key: 'CAR2_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR2_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '대형',
    children: [
      { title: '건수', key: 'CAR345_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR345_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(일반)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(할인)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_DC_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_DC_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  { title: '면제', key: 'LANE_EXMT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '집계제외', key: 'LANE_OFCNOT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const yearHeaders = ref([
  {
    title: '',
    children: [
      { title: '영업소명', key: 'IC_NM', width: '100' }, // 내가 DB에 넘겨줄 변수명
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
    ],
  },
  {
    title: '교통량 합계',
    children: [
      { title: '건수', key: 'CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '경형',
    children: [
      { title: '건수', key: 'CAR6_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR6_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '소형',
    children: [
      { title: '건수', key: 'CAR1_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR1_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '중형',
    children: [
      { title: '건수', key: 'CAR2_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR2_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '대형',
    children: [
      { title: '건수', key: 'CAR345_CNT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '금액', key: 'CAR345_FARE_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(일반)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(할인)',
    children: [
      { title: '전자카드(선불)', key: 'LANE_PAY_DC_EPCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'LANE_PAY_DC_ELCARD_COUNT', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  { title: '면제', key: 'LANE_EXMT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '집계제외', key: 'LANE_OFCNOT_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const tab = ref(0);

const searchDate = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const searchMonth = ref({
  START_DATE: dayjs().format('YYYY-MM'),
  END_DATE: dayjs().format('YYYY-MM'),
});

const searchYear = ref({
  START_DATE: dayjs().format('YYYY'),
  END_DATE: dayjs().format('YYYY'),
});

const ozSearchDataDate = ref([]);
const ozSearchDataMonth = ref([]);
const ozSearchDataYear = ref([]);

const searchHeader = ref([]);
watch(
  tab,
  () => {
    searchHeader.value =
      tab.value == 0
        ? [
            { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
            { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
          ]
        : tab.value == 1
        ? [
            { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'month' },
            { label: '~', key: 'END_DATE', type: 'date', dateType: 'month' },
          ]
        : [
            { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'year' },
            { label: '~', key: 'END_DATE', type: 'date', dateType: 'year' },
          ];
  },
  {
    immediate: true,
  },
);

const dailyTrafficProcessDayData = ref([]);
const dailyTrafficProcessMonthData = ref([]);
const dailyTrafficProcessYearData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    if (tab.value === 0) {
      const data = await request('post', 'api/sales/getDailyTrafficProcessDay', {
        ...searchDate.value,
        ...{
          START_DATE: searchDate.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchDate.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      if (data.length == 0) alert(`데이터가 없습니다.`);
      dailyTrafficProcessDayData.value = data;
      ozSearchDataDate.value = JSON.parse(JSON.stringify(searchDate.value));
    } else if (tab.value === 1) {
      const data = await request('post', 'api/sales/getDailyTrafficProcessMonth', {
        ...searchMonth.value,
        ...{
          START_DATE: searchMonth.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchMonth.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      if (data.length == 0) alert(`데이터가 없습니다.`);

      dailyTrafficProcessMonthData.value = data;
      ozSearchDataMonth.value = JSON.parse(JSON.stringify(searchMonth.value));
    } else if (tab.value === 2) {
      const data = await request('post', 'api/sales/getDailyTrafficProcessYear', {
        ...searchYear.value,
        ...{
          START_DATE: searchYear.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchYear.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      if (data.length == 0) alert(`데이터가 없습니다.`);
      dailyTrafficProcessYearData.value = data;
      ozSearchDataYear.value = JSON.parse(JSON.stringify(searchYear.value));
    }
  } catch (error) {
    alert(`일별통행처리현황 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 'mix';
  if (tab.value === 0) {
    if (dailyTrafficProcessDayData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataDate.value) !== JSON.stringify(searchDate.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeaders = dayHeaders.value;
      excelDownload(
        row,
        searchHeader.value,
        searchDate.value,
        excelHeaders,
        dailyTrafficProcessDayData.value,
        '일별통행처리현황-일자별',
        '일별통행처리현황-일자별',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 1) {
    if (dailyTrafficProcessMonthData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataMonth.value) !== JSON.stringify(searchMonth.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeaders = monthHeaders.value;

      excelDownload(
        row,
        searchHeader.value,
        searchMonth.value,
        excelHeaders,
        dailyTrafficProcessMonthData.value,
        '일별통행처리현황-월별',
        '일별통행처리현황-월별',
      );
    } else {
      alert(`엑셀다운로드 취소`);
    }
  } else if (tab.value === 2) {
    if (dailyTrafficProcessYearData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchDataYear.value) !== JSON.stringify(searchYear.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
      const excelHeaders = yearHeaders.value;

      excelDownload(
        row,
        searchHeader.value,
        searchYear.value,
        excelHeaders,
        dailyTrafficProcessYearData.value,
        '일별통행처리현황-년도별',
        '일별통행처리현황-년도별',
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
      if (dailyTrafficProcessDayData.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataDate.value) !== JSON.stringify(searchDate.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/dailyTrafficProcess.ozr', {
        CSV_DATA: dailyTrafficProcessDayData.value,
        START_DATE: dayjs(searchDate.value['START_DATE']).format('YYYY년MM월DD일'),
        END_DATE: dayjs(searchDate.value['END_DATE']).format('YYYY년MM월DD일'),
        TITLE_NM: '일별통행처리현황-일자별',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      });
    } else if (tab.value == 1) {
      if (dailyTrafficProcessMonthData.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataMonth.value) !== JSON.stringify(searchMonth.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/dailyTrafficProcess.ozr', {
        CSV_DATA: dailyTrafficProcessMonthData.value,
        START_DATE: dayjs(searchMonth.value['START_DATE']).format('YYYY년MM월'),
        END_DATE: dayjs(searchMonth.value['END_DATE']).format('YYYY년MM월'),
        TITLE_NM: '일별통행처리현황-월별',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      });
    } else if (tab.value == 2) {
      if (dailyTrafficProcessYearData.value.length === 0) {
        alert(`조회된 데이터가 없습니다.`);
        return;
      }
      if (JSON.stringify(ozSearchDataYear.value) !== JSON.stringify(searchYear.value)) {
        alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
        return;
      }
      jsonData.value = createOzDataset('/SALES/dailyTrafficProcess.ozr', {
        CSV_DATA: dailyTrafficProcessYearData.value,
        START_DATE: dayjs(searchYear.value['START_DATE']).format('YYYY년'),
        END_DATE: dayjs(searchYear.value['END_DATE']).format('YYYY년'),
        TITLE_NM: '일별통행처리현황-년도별',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      });
    }
    isActive.value = true;
  } catch (error) {
    alert(`일별통행처리현황 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
.tab-container {
  margin: 10px 0 0 -10px;
}
.v-tab {
  color: #a9a9a9;
}
</style>
