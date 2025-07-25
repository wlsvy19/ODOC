<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="320"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
    :headers="headers"
    :contents="unPaidCollectData"
    rowType="2"
    scrollKey="dailyTrafficProcessDayData"
  />
  <div class="work-count-container">
    <span class="total-work">미납률</span>
    <span><input class="total-count" :value="contentsFoot.unPaidPercent" disabled /> %</span>
  </div>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');

const authStore = useAuthStore();
const contentsFoot = ref([]);
contentsFoot.value = {
  unPaidPercent: 0,
};
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');

const headers = ref([
  {
    title: '일자',
    children: [
      { title: '순번', key: 'ROW_SEQ', width: '100' },
      { title: '근무일자', key: 'WORK_DATE', width: '100' },
    ],
  },
  {
    title: '전체',
    children: [
      { title: '처리대수', key: 'OCC_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'PASS_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'PAY_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '미납금', key: 'UNP_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '미납',
    children: [
      { title: '대수', key: 'MN_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '완납',
    children: [
      { title: '대수', key: 'WN_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'WN_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'WN_MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '면제',
    children: [
      { title: '대수', key: 'EXMT_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'EXMT_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'EXMT_MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '할인',
    children: [
      { title: '대수', key: 'DC_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'DC_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'DC_MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납불가',

    children: [
      { title: '대수', key: 'XX_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'XX_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'XX_MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '정상',
    children: [
      { title: '대수', key: 'OK_CNT', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수할 금액', key: 'OK_FARE', width: '130', customBodyCellStyle: 'table-body-style-right' },
      { title: '징수한 금액', key: 'OK_MN_RESULT', width: '130', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);
const setCustomBodyRowStyleSummary = (item) => {
  if (item.WORK_DATE === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),

  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const unPaidCollectData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/unpaid/getUnPaidCollect', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    unPaidCollectData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
    data.map((item) => {
      if (item.WORK_DATE == '합계') {
        if (item.IPASS_FARE != 0) {
          contentsFoot.value = {
            unPaidPercent: ((item.IUNP_FARE / item.IPASS_FARE) * 100).toFixed(2),
          };
        }
      }
    });
  } catch (error) {
    alert(`미납금 회수 현황 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 2;
  if (unPaidCollectData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, unPaidCollectData.value, '미납금 회수 현황', '미납금 회수 현황', [
      `미납률: ${contentsFoot.value.unPaidPercent} %`,
    ]);
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (unPaidCollectData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
    jsonData.value = createOzDataset('/UNPAID/unPaidCollect.ozr', {
      CSV_DATA: unPaidCollectData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '미납금회수 현황',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
      UNPAID_PERCENT: contentsFoot.value.unPaidPercent + '%',
    });
    isActive.value = true;
  } catch (error) {
    alert(`미납금 회수 현황 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
