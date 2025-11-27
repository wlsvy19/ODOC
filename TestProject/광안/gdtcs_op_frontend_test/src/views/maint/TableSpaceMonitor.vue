<template>
  <div>
    <Bar id="my-chart-id" :options="chartOptions" :data="chartData" style="height: 400px; width: 100%" />
  </div>
  <TableComponent :heightOffset="650" :headers="headers" :contents="tableSpaceMonitorData" rowType="1" scrollKey="tableSpaceMonitorData" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { request, btnHandler } from '@/utils/common';
import { useAuthStore } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, LineElement, PointElement, CategoryScale, LinearScale, BarElement } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, LineElement, PointElement, CategoryScale, LinearScale, BarElement);
const isLoading = ref(false);
onMounted(() => {
  handleSearch();
});
const authStore = useAuthStore();
const headers = ref([
  { title: 'TABLE SPACE', key: 'TABL_SPC_NM', width: '140' },
  { title: 'FILE_NAME', key: 'FILE_NAME', width: '350' },
  { title: 'TOTAL_BYTES(MB)', key: 'TOTAL_BYTES', width: '150' },
  { title: 'FREE_BYTES(MB)', key: 'FREE_BYTES', width: '150' },
  { title: 'USED_BYTES(MB)', key: 'USED_BYTES', width: '150' },
  { title: 'FREE %', key: 'FREE_PERC', width: '100' },
  { title: 'USED %', key: 'USED_PERC', width: '100' },
]);

const tableSpaceMonitorData = ref([]);
const chartData = ref({
  labels: [],
  datasets: [
    {
      label: 'TOTAL_BYTES(MB)',
      backgroundColor: '#7acbf9',
      data: [],
    },
    {
      label: 'FREE_BYTES(MB)',
      backgroundColor: '#f87979',
      data: [],
    },
    {
      label: 'USED_BYTES(MB)',
      backgroundColor: '#ffd400',
      data: [],
    },
  ],
});
const chartOptions = ref({
  responsive: true,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: true,
      position: 'top',
      align: 'start',
      text: ' \u00a0 ' + 'TABLESPACE 사용 현황',
    },
  },
});

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/maint/getTableSpaceMonitor', {
      IC_CODE: authStore.getIcCode,
    });

    tableSpaceMonitorData.value = data;
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
    updateChartData(data);
  } catch (error) {
    alert(`테이블 스페이스 모니터링 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};
const updateChartData = (data) => {
  const labels = data.map((item) => item.TABL_SPC_NM);
  const totalBytes = data.map((obj) => (obj.TOTAL_BYTES == null ? 0 : obj.TOTAL_BYTES));
  const usedBytes = data.map((obj) => (obj.USED_BYTES == null ? 0 : obj.USED_BYTES));
  const freeBytes = data.map((obj) => (obj.FREE_BYTES == null ? 0 : obj.FREE_BYTES));

  chartData.value = {
    labels: labels,
    datasets: [
      {
        label: 'TOTAL_BYTES(MB)',
        backgroundColor: '#7acbf9',
        data: totalBytes,
      },
      {
        label: 'FREE_BYTES(MB)',
        backgroundColor: '#f87979',
        data: freeBytes,
      },
      {
        label: 'USED_BYTES(MB)',
        backgroundColor: '#ffd400',
        data: usedBytes,
      },
    ],
  };
};

const handleExcel = () => {
  const row = 1;
  if (tableSpaceMonitorData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = [
      { title: 'TABLE SPACE', key: 'TABL_SPC_NM', width: '30' },
      { title: 'FILE_NAME', key: 'FILE_NAME', width: '60', excelWidth: 60 },
      { title: 'TOTAL_BYTES(MB)', key: 'TOTAL_BYTES', width: '30' },
      { title: 'FREE_BYTES(MB)', key: 'FREE_BYTES', width: '30' },
      { title: 'USED_BYTES(MB)', key: 'USED_BYTES', width: '30' },
      { title: 'FREE %', key: 'FREE_PERC', width: '30' },
      { title: 'USED %', key: 'USED_PERC', width: '30' },
    ];
    excelDownload(row, [], {}, excelHeaders, tableSpaceMonitorData.value, '테이블 스페이스 모니터링', '테이블 스페이스 모니터링');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
