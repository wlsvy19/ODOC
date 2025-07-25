<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <OZReportComponent :json-data="getJsonData" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/index';
import { request, btnHandler, createOzDataset, showMessage } from '@/utils/common';
import { DEFAULT_MONTHTOLLCONSTRUCTION_RESULT } from './constants';
import dayjs from 'dayjs';

const getJsonData = computed(() => {
  jsonData.value = createOzDataset('/SALES/monthTollConstruction.ozr', ozParam.value);
  return jsonData.value;
});

onMounted(() => handleSearch());

const searchHeader = ref([{ label: '근무 월', key: 'WORK_DATE', type: 'date', dateType: 'month' }]);
const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM'),
  IC_DIV: '',
});

const jsonData = ref();
const authStore = useAuthStore();

const ozParam = ref({
  CSV_DATA: [{}],
  CSV_DATA_LAST_YEAR: [{}],
  CSV_DATA_SUM: [{}],
  WORK_DATE: '',
  IC_CODE: '',
});


const isEmpty = (data) => {
  if (!data || data.length === 0) return true;
  else return false;
};

const handleSearch = async () => {
  const result = await request('post', 'api/sales/getMonthTollConstrunctionReport', {
    IC_CODE: authStore.getIcCode,
    WORK_DATE: searchData.value.WORK_DATE.replaceAll('-', ''),
  });

  if(isEmpty(result?.month)){
    showMessage(`${dayjs(searchData.value.WORK_DATE).endOf('month').format('YYYY년 MM월')} 일마감이 완료되지 않았습니다.`, 'error', 5000);
    ozParam.value.CSV_DATA = DEFAULT_MONTHTOLLCONSTRUCTION_RESULT;
    ozParam.value.CSV_DATA_LAST_YEAR = DEFAULT_MONTHTOLLCONSTRUCTION_RESULT;
    ozParam.value.CSV_DATA_SUM = DEFAULT_MONTHTOLLCONSTRUCTION_RESULT;  
  }
  else {
    const newObj = Object.assign({}, result.sum[0]);
    for (const key in newObj) newObj[key] = 0;
    ozParam.value.CSV_DATA = result.month;
    ozParam.value.CSV_DATA_LAST_YEAR = result.lastYear.length === 0 ? [newObj] : result.lastYear;
    ozParam.value.CSV_DATA_SUM = result.sum;
  }
  ozParam.value.WORK_DATE = dayjs(searchData.value.WORK_DATE).endOf('month').format('YYYY-MM-DD');
  ozParam.value.IC_CODE = authStore.getIcNm;
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped></style>
