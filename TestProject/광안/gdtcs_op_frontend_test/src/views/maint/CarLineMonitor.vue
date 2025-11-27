<template>
  <v-row>
    <v-card>
      <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
      <TableComponent
        :customBodyRowStyle="getCustomBodyRowStyle"
        :headers="headers"
        :contents="carLineMonitorData"
        rowType="1"
        scrollKey="carLineMonitorData"
        :heightOffset="390"
      >
      </TableComponent>
    </v-card>
    <v-card>
      <div class="work-count-container">
        <span class="total-work" style="color: red">{{ alertMessage }}</span>
      </div>
    </v-card>
  </v-row>

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, watch, nextTick, readonly } from 'vue';
import { request, btnHandler } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';
import sound from '@/assets/sound/Alarm.wav';

const authStore = useAuthStore();
const isLoading = ref(false);

const headers = ref([
  { title: '차로번호', key: 'LANE_NO', width: '200', customBodyCellStyle: 'CarLineMonitor-COMMENTS-body-cell-style-black' },
  { title: '차로타입', key: 'LANE_TYPE', width: '100', customBodyCellStyle: 'CarLineMonitor-COMMENTS-body-cell-style-black' },
  { title: '근무상태', key: 'WORK_STATE', width: '100', customBodyCellStyle: 'CarLineMonitor-COMMENTS-body-cell-style-black' },
  { title: '통신상태', key: 'COM_STATE', width: '100' },
  { title: '최종수집시간', key: 'HND_TM', width: '200', customBodyCellStyle: 'CarLineMonitor-COMMENTS-body-cell-style-black' },
]);
const searchHeader = ref([
  { label: '자동조회', key: 'AUTO_SEARCH', type: 'checkbox' },
  { label: '', key: 'SECOND_OPTION', type: 'inputButton', max: 60, min: 5, valid: 'digit' },
  { label: '초' },
  { label: '알람실행', key: 'ALERT_RUN', type: 'checkbox' },
]);
const getCustomBodyRowStyle = (item) => {
  if (item.COM_STATE == 'DISCONNECT') {
    return 'CarLineMonitor-COMMENTS-body-cell-style-red';
  }
  return '';
};
const searchData = ref({
  AUTO_SEARCH: false,
  SECOND_OPTION: 5,
  ALERT_RUN: false,
});
const alertMessage = ref('');
const repeatExe = ref('');

watch(
  () => searchData.value.AUTO_SEARCH,
  (autoRun) => {
    if (autoRun == true) {
      if (handleSearch() == 0) {
        return;
      }
      repeatExe.value = setInterval(function () {
        handleSearch();
        console.log('자동조회 실행중');
      }, searchData.value.SECOND_OPTION * 1000);
    } else if (autoRun == false) {
      clearInterval(repeatExe.value);
      console.log('자동조회 종료');
    }
  },
  {
    immediate: true,
  },
);

const fncSoundPlay = () => {
  var audio = new Audio(sound);
  audio.play();
};

const carLineMonitorData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    if (searchData.value.SECOND_OPTION < 5 || searchData.value.SECOND_OPTION == null) {
      alert(`5초 아래로 내릴 수 없습니다.`);
      searchData.value.SECOND_OPTION = 5;
      searchData.value.AUTO_SEARCH = false;
      return 0;
    }
    isLoading.value = true;
    const data = await request('post', 'api/maint/getCarLineMonitor', {
      ...searchData.value,
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });

    carLineMonitorData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
    const disconnectLANENM = ref([]);
    if (searchData.value.ALERT_RUN == true) {
      data.map((obj) => {
        if (obj.COM_STATE == 'DISCONNECT') {
          disconnectLANENM.value += '\u00a0 ' + obj.LANE_NO;
        }
      });
      if (disconnectLANENM.value.length > 0) {
        alertMessage.value = String.raw`${disconnectLANENM.value} 수납용 차로가 DISCONNECT 상태입니다. 확인해주세요!`;
      } else {
        alertMessage.value = '';
      }
      fncSoundPlay();
      console.log('알람 실행중');
    } else {
      alertMessage.value = '';
    }
  } catch (error) {
    alert(`차로 실시간 모니터링 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (carLineMonitorData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, carLineMonitorData.value, '차로 실시간 모니터링', '차로 실시간 모니터링');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped>
.v-card {
  width: 100%;
}
.work-count-container {
  border-top: 1px solid #d3d3d3;
  font-size: 25px;
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
