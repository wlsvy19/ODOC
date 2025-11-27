<template>
  <div>
    <v-row class="image" style="width: wrap; height: 435px">
      <v-col cols="7">
        <h1 style="position: relative; left: 100px; top: 100px; color: lightblue; font-size: 45px">광안대교 영업소</h1>
        <h3 style="position: relative; left: 100px; top: 90px; color: white; font-size: 25px">교통량 현황</h3>
      </v-col>
      <v-col cols="3" class="smartTolling">
        <div v-if="isLoading" class="smartTolling-overlay">
          <div class="loading-content">
            <v-progress-circular :width="10" size="100" indeterminate color="#0086E5" />
          </div>
        </div>
        <v-icon :size="40" style="float: right; margin: 5px; position: relative; top: 3px; right: 3px; margin: 5px" @click="refresh">
          mdi-refresh
        </v-icon>
        <h3 style="position: relative; left: 40px; top: 30px; font-size: 25px; width: 150px">스마트톨링</h3>
        <h1 style="position: relative; left: 40px; top: 40px; font-size: 45px">{{ hipassTraffic }}</h1>
      </v-col>
    </v-row>
  </div>
  <div>
    <v-row class="vertical-panel" style="width: wrap; height: 330px">
      <v-col cols="3" class="vertical-panel" style="width: auto">
        <v-container style="padding: 10px">
          <v-card elevation="4" style="width: 100%" height="305" rounded="xl">
            <v-toolbar class="toolbar" :color="colorStore.basicColor">
              <v-spacer></v-spacer>
              차로별 교통량 현황
              <v-spacer></v-spacer>
            </v-toolbar>
            <Bar id="my-chart-id" :options="carLineTraffic" :data="carLineData" class="bar" />
            <div v-if="isLoading" class="loading-overlay">
              <div class="loading-content">
                <v-progress-circular :width="10" size="100" indeterminate color="#0086E5" />
              </div>
            </div>
          </v-card>
        </v-container>
      </v-col>
      <v-col cols="3" class="vertical-panel-top" style="width: auto">
        <v-container style="padding: 10px">
          <v-card elevation="4" style="width: 100%" height="305" rounded="xl">
            <v-toolbar class="toolbar" :color="colorStore.basicColor">
              <v-spacer></v-spacer>
              차종별 교통량 현황
              <v-spacer></v-spacer>
            </v-toolbar>
            <Bar id="my-chart-id" :options="carTypeTraffic" :data="carTypeData" class="bar" />
            <div v-if="isLoading" class="loading-overlay">
              <div class="loading-content">
                <v-progress-circular :width="10" size="100" indeterminate color="#0086E5" />
              </div>
            </div>
          </v-card>
        </v-container>
      </v-col>
      <v-col cols="3" class="vertical-panel">
        <div style="margin-top: 1px">
          <v-container style="padding: 10px">
            <v-card elevation="4" style="width: 100%" height="305" rounded="xl">
              <v-toolbar class="toolbar" :color="colorStore.basicColor">
                <v-spacer></v-spacer>
                자료수신현황
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-icon icon="mdi-circle-medium" :color="colorStore.basicColor" style="margin-left: 10px" />
              <v-label style="font-size: 16px !important; font-weight: bold; margin: 7px 0 3px 0" text="도로공사" />
              <table style="min-height: 195px">
                <thead>
                  <tr>
                    <th width="38%">수신 자료</th>
                    <th width="14%">상태</th>
                    <th>수신일</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>전자카드 B/L 마스터</td>
                    <td>{{ ecardReceiveState }}</td>
                    <td>{{ ecardReceiveDT }}</td>
                  </tr>
                  <tr>
                    <td>OBU B/L 마스터</td>
                    <td>{{ obuReceiveState }}</td>
                    <td>{{ obuReceiveDT }}</td>
                  </tr>
                  <tr>
                    <td>통합복지카드 P/L 마스터</td>
                    <td>{{ wcardReceiveState }}</td>
                    <td>{{ wcardReceiveDT }}</td>
                  </tr>
                  <tr>
                    <td>경차 P/L 마스터</td>
                    <td>{{ lcardReceiveState }}</td>
                    <td>{{ lcardReceiveDT }}</td>
                  </tr>
                </tbody>
              </table>
              <div v-if="isLoading" class="loading-overlay">
                <div class="loading-content">
                  <v-progress-circular :width="10" size="100" indeterminate color="#0086E5" />
                </div>
              </div>
            </v-card>
          </v-container>
        </div>
      </v-col>
      <v-col cols="3" class="vertical-panel-top">
        <div style="margin-top: 1px">
          <v-container style="padding: 10px">
            <v-card elevation="4" style="width: 100%" height="305" rounded="xl">
              <v-toolbar class="toolbar" :color="colorStore.basicColor">
                <v-spacer></v-spacer>
                일마감현황
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-icon icon="mdi-circle-medium" :color="colorStore.basicColor" style="margin-left: 10px" />
              <v-label style="font-size: 16px !important; font-weight: bold; margin: 7px 0 3px 0" text="일마감 상태" />
              <table>
                <thead>
                  <tr>
                    <th width="30%">상태</th>
                    <th>일마감 일시</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td rowspan="2">{{ dayFinState }}</td>
                  </tr>
                  <tr>
                    <td rowspan="2">{{ dayFinDT }}</td>
                  </tr>
                </tbody>
              </table>
              <v-icon icon="mdi-circle-medium" :color="colorStore.basicColor" style="margin-left: 10px" />
              <v-label style="font-size: 16px !important; font-weight: bold; margin: 7px 0 3px 0" text="송수신 상태" />
              <table>
                <thead>
                  <tr>
                    <th width="30%">구분</th>
                    <th>전송 일시</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>일마감</td>
                    <td>{{ dayFin }}</td>
                  </tr>
                </tbody>
              </table>
              <div v-if="isLoading" class="loading-overlay">
                <div class="loading-content">
                  <v-progress-circular :width="10" size="100" indeterminate color="#0086E5" />
                </div>
              </div>
            </v-card>
          </v-container>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, LineElement, PointElement, CategoryScale, LinearScale, BarElement } from 'chart.js';
import { Bar } from 'vue-chartjs';
import { btnHandler, request, comma } from '@/utils/common';
import { useMenuStore, useAuthStore, loadSystemSmallCodeAll, useColorStore } from '@/stores/index';
import dayjs from 'dayjs';
import ChartDataLabels from 'chartjs-plugin-datalabels';

const isLoading = ref(false);
const menuStore = useMenuStore();
const authStore = useAuthStore();
const colorStore = useColorStore();

onMounted(async () => {
  await carSearch();
  await fileSearch();
});

const refresh = async () => {
  isLoading.value = true;
  await carSearch();
  await fileSearch();
  isLoading.value = false;
};
loadSystemSmallCodeAll().setSystemSmallCodeAll();

ChartJS.register(Title, Tooltip, Legend, ArcElement, LineElement, PointElement, CategoryScale, LinearScale, BarElement, ChartDataLabels);

const carLineData = ref({
  labels: [],
  datasets: [
    {
      label: '건수',
      barPercentage: 0.7,
      backgroundColor: '#7acbf9',
      data: [],
      datalabels: {
        align: 'end',
        anchor: 'end',
      },
    },
  ],
});
const carTypeData = ref({
  labels: [],
  datasets: [
    {
      label: '건수',
      barPercentage: 0.7,
      backgroundColor: '#ffa05c',
      data: [],
      datalabels: {
        align: 'end',
        anchor: 'end',
      },
    },
  ],
});
const carLineTraffic = ref({
  responsive: true,
  indexAxis: 'y',
  plugins: { legend: { display: false } },
  scales: {
    x: {
      beginAtZero: true,
      scaleLineColor: 'red',
      display: false,
      max: 40000,
      grid: {
        color: 'transparent',
      },
    },
  },
});

const carTypeTraffic = ref({
  responsive: true,
  indexAxis: 'y',
  plugins: { legend: { display: false } },
  scales: {
    x: {
      beginAtZero: true,
      scaleLineColor: 'red',
      display: false,
      max: 150000,
      grid: {
        color: 'transparent',
      },
    },
  },
});

const ecardReceiveDT = ref('-');
const ecardReceiveState = ref('-');

const obuReceiveDT = ref('-');
const obuReceiveState = ref('-');

const wcardReceiveDT = ref('-');
const wcardReceiveState = ref('-');

const lcardReceiveDT = ref('-');
const lcardReceiveState = ref('-');

const dayFinDT = ref('-');
const dayFinState = ref('-');
const dayFin = ref('-');

const carSearch = async () => {
  try {
    const carLineData = await request('post', 'api/traffic/getCarLineTraffic', {
      IC_CODE: authStore.getIcCode,
      START_DATE: dayjs().format('YYYYMMDD'),
    });
    updateCarLineData(carLineData);

    const carTypeData = await request('post', 'api/traffic/getCarTypeTraffic', {
      IC_CODE: authStore.getIcCode,
      START_DATE: dayjs().format('YYYYMMDD'),
    });
    updateCarTypeData(carTypeData);
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);
  }
};

const fileSearch = async () => {
  try {
    const receiveData = await request('post', 'api/traffic/getDataReceive', {
      IC_CODE: authStore.getIcCode,
    });
    receiveData.map((item) => {
      switch (item.FILE_DIV) {
        case '472':
          ecardReceiveDT.value = item.TRNRCP_DT;
          ecardReceiveState.value = item.TRNRCP_RSLT;
          break;
        case '316':
          obuReceiveDT.value = item.TRNRCP_DT;
          obuReceiveState.value = item.TRNRCP_RSLT;
          break;
        case '285':
          wcardReceiveDT.value = item.TRNRCP_DT;
          wcardReceiveState.value = item.TRNRCP_RSLT;
          break;
        case '480':
          lcardReceiveDT.value = item.TRNRCP_DT;
          lcardReceiveState.value = item.TRNRCP_RSLT;
          break;
        case 'DAYE':
          dayFin.value = item.TRNRCP_DT;
          break;
        case '일마감':
          dayFinDT.value = item.TRNRCP_DT;
          dayFinState.value = item.TRNRCP_RSLT;
          break;
      }
    });
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);
  }
};

const hipassTraffic = ref(0);
const updateCarLineData = (data) => {
  hipassTraffic.value = 0;
  const labels = data.map((item) => item.LINE_NM);

  const totalCount = data.map((obj) => (obj.HAND_CNT == null ? 0 : obj.HAND_CNT));
  data.map((obj) => {
    hipassTraffic.value += obj.HAND_CNT;
  });
  hipassTraffic.value = comma(hipassTraffic.value);
  carLineData.value = {
    labels: labels,
    datasets: [
      {
        label: '건수',
        backgroundColor: '#7acbf9',
        data: totalCount,
      },
    ],
  };
};

const updateCarTypeData = (data) => {
  const labels = data.map((item) => item.TRF_CAR_TYPE_NM);

  const totalCount = data.map((obj) => (obj.HAND_CNT == null ? 0 : obj.HAND_CNT));

  carTypeData.value = {
    labels: labels,
    datasets: [
      {
        label: '건수',
        backgroundColor: '#ffa05c',
        data: totalCount,
      },
    ],
  };
};

const handlePowerOff = () => {
  alert('대시보드 - 종료버튼 클릭');
};

btnHandler({
  PowerOff: handlePowerOff,
});
</script>

<style scoped>
.loading-overlay {
  position: absolute;
  top: 64px;
  left: 0;
  width: 100%;
  height: calc(100% - 64px);
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.vertical-panel {
  border-top: 1px solid #a9a9a9;
}

.vertical-panel-top {
  border-top: 1px solid #a9a9a9;
}

.image {
  background-size: 100% 100%;
  image-rendering: -webkit-optimize-contrast;
  background-image: url('../assets/images/main.jpg');
}

.image::before {
  position: absolute;
  content: '';
  top: 0px;
  left: 0px;
  width: 100%;
  height: 580px;
  background-color: rgba(0, 0, 0, 0.4);
}

.smartTolling {
  background-size: 35%;
  background-position: right 50px top 60px;

  image-rendering: -webkit-optimize-contrast;
  background-image: url('../assets/images/hipass.png');
  height: 250px;
  background-color: seashell;
  position: relative;
  top: 50px;
  border-radius: 30px;
}
.smartTolling-overlay {
  position: absolute;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  border-radius: 30px;
}
.toolbar {
  font-size: 18px;
  font-weight: bold;
  color: white;
}
.title {
  margin-left: 10px;
  font-size: 18px;
  font-weight: bold;
  padding: 0.3em 0.5em;
  vertical-align: middle;
}

.bar {
  padding: 14px 10px 7px 10px;
  min-height: 240px;
  max-height: 240px;
}
body {
  padding: 1.5em;

  background: #f5f5f5;
}

table {
  border: none;
  font-size: 0.75em;
  width: 100%;
  border-collapse: collapse;
  overflow: hidden;
  text-align: center;
}

thead {
  font-weight: bold;
  color: black;
  background-color: whitesmoke;
}

th {
  vertical-align: middle;
  height: 30px;
  font-size: 13px;
}

td {
  padding: 0.3em 0.5em;
  vertical-align: middle;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  background: #fff;
}

a {
  color: #73685d;
}

@media all and (max-width: 768px) {
  table,
  thead,
  tbody,
  th,
  td,
  tr {
    display: block;
  }

  th {
    text-align: middle;
  }

  table {
    position: relative;
    padding-bottom: 0;
    border: none;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  thead {
    float: left;
    white-space: nowrap;
  }

  tbody {
    overflow-x: auto;
    overflow-y: hidden;
    position: relative;
    white-space: nowrap;
  }

  tr {
    display: inline-block;
    vertical-align: top;
  }

  th {
    border-bottom: 1px solid #84c2d7;
  }

  td {
    border-bottom: 1px solid #e5e5e5;
  }
}
</style>
