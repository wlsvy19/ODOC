<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent
    v-if="isLoading"
    :message="`정산요청 중입니다.<br />약 5분에서 15분정도 소요됩니다.<br/>절대로 창을 닫거나 새로고침 하시면 안됩니다.`"
  />
  <LoadingComponent v-if="centerConLoading" :message="`센터 DB에 연결중입니다...`" />

  <LoadingComponent v-if="taxiPreLoading" :message="`공차택시/사전등록 일마감 재집계 처리중입니다...`" />

  <LoadingComponent v-if="holidayPreLoading" :message="`특정일면제 일마감 재집계 처리중입니다...`" />

  <v-row @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  </v-row>

  <v-row>
    <div class="blue-chk">
      <span>영업통합정보체계</span>
      <span class="day-process-btn">
        <v-tooltip location="right">
          <template v-slot:activator="{ props }">
            <v-btn @click="transDayFin" v-bind="props" :color="dayFinbuttonColor" text="일마감전송" density="comfortable" width="110" />
          </template>
          <span>정산조건을 만족해야 전송할 수 있습니다.</span>
        </v-tooltip>
      </span>
    </div>

    <div class="status-container">
      <div class="status-box">
        <v-label class="status-label">센터 연결 상태: </v-label>
        <v-label class="status-value" :style="{ color: centerConYn === 'Y' ? 'blue' : 'red' }">
          {{ centerConYnText }}
        </v-label>
        <span class="divider">|</span>

        <v-label class="status-label">센터 LOCK 여부: </v-label>
        <v-label class="status-value" :style="{ color: centerLockYnMsg === '전송가능' ? 'blue' : 'red' }">
          {{ centerLockYnText }}
        </v-label>

        <span class="divider">|</span>
        <v-label class="status-label">일마감 여부: </v-label>
        <v-label class="status-value" :style="{ color: isDayFin === '일마감미완료' ? 'red' : 'blue' }">
          {{ isDayFinText }}
        </v-label>
      </div>
    </div>

    <br />

    <div class="content-container">
      <InformationComponent
        style="margin-bottom: 10px"
        message="센터 LOCK 여부와 일마감 여부가 모두 [전송가능] 상태여야 정산요청을 할 수 있습니다."
      />
      <v-table class="data-table">
        <thead>
          <tr>
            <th class="font-bold-ac header-cell">정산일자</th>
            <th class="font-bold-ac header-cell">정산요청일시</th>
            <th class="font-bold-ac header-cell">전송구분</th>
            <th class="font-bold-ac header-cell">전송결과</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td style="text-align: center">
              <v-chip color="success" class="chip-cell">{{ InterSearchData.CALC_DATE }} </v-chip>
            </td>
            <td style="text-align: center">
              <v-chip color="success" class="chip-cell">{{ InterSearchData.TRNRCP_DT }} </v-chip>
            </td>
            <td style="text-align: center">
              <v-chip :color="InterSearchData.TRNRCP_DIV === '전송' ? 'primary' : 'success'" class="chip-cell"
                >{{ InterSearchData.TRNRCP_DIV }}
              </v-chip>
            </td>
            <td style="text-align: center">
              <v-chip
                :color="InterSearchData.TRNRCP_RSLT === '성공' ? 'primary' : InterSearchData.TRNRCP_RSLT === '실패' ? 'error' : 'success'"
                class="chip-cell"
              >
                {{ InterSearchData.TRNRCP_RSLT }}
              </v-chip>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </v-row>

  <v-row>
    <div class="blue-chk" style="margin-top: 50px">
      <span>공차택시/사전등록 집계처리</span>
      <span v-if="!isHoliday" class="day-fin-after-process-btn">
        <v-tooltip location="right">
          <template v-slot:activator="{ props }">
            <v-btn @click="transTaxiPre" v-bind="props" :color="taxiPrebuttonColor" text="일마감 재집계" density="comfortable" width="130" />
          </template>
          <span>공차택시와 사전등록이 모두 수신 상태여야 일마감 재집계를 진행할 수 있습니다.</span>
        </v-tooltip>
      </span>
      <span v-else class="day-fin-after-process-btn">
        <v-tooltip location="right">
          <template v-slot:activator="{ props }">
            <v-btn @click="transHoliday" v-bind="props" color="success" text="특정일면제 재집계" density="comfortable" width="150" />
          </template>
          <span>특정일면제는 공차택시와 사전등록 수신여부에 상관없이 일마감 재집계를 진행할 수 있습니다.</span>
        </v-tooltip>
      </span>
    </div>

    <div class="content-container">
      <InformationComponent
        style="margin-top: 5px; margin-bottom: 10px"
        message="공차택시와 사전등록 정보를 정산일자 기준으로 수신하여 일마감 재집계 처리를 합니다."
      />
      <InformationComponent
        style="margin-top: 5px; margin-bottom: 10px"
        message="특정일면제는 공차택시와 사전등록 수신에 상관없이 일마감 재집계 처리를 합니다."
      />

      <v-table class="data-table">
        <thead>
          <tr>
            <th class="font-bold-ac header-cell">수신목록</th>
            <th class="font-bold-ac header-cell">수신여부</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td style="text-align: center">
              <h4>공차택시</h4>
            </td>
            <td style="text-align: center">
              <v-chip :color="TaxiSearchData === '미수신' ? 'error' : 'primary'" class="chip-cell">{{ TaxiSearchData }}</v-chip>
            </td>
          </tr>
          <tr>
            <td style="text-align: center">
              <h4>사전등록</h4>
            </td>
            <td style="text-align: center">
              <v-chip :color="PreRegiSearchData === '미수신' ? 'error' : 'primary'" class="chip-cell">{{ PreRegiSearchData }}</v-chip>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </v-row>
</template>

<script setup>
import { useColorStore } from '@/stores/index';
import { computed, onMounted, ref, watch } from 'vue';
import { btnHandler, request, showMessage } from '@/utils/common';
import dayjs from 'dayjs';
import InformationComponent from '@/components/common/InformationComponent.vue';

const colorStore = useColorStore();

const dayFinbuttonColor = computed(() => {
  return dayChecked.value ? colorStore.basicColor : 'red';
});

const taxiPrebuttonColor = computed(() => {
  return taxiPreChecked.value ? colorStore.basicColor : 'red';
});

const searchHeader = ref([{ label: '정산일자', key: 'CALC_DATE', type: 'date', dateType: 'day' }]);

const searchData = ref({
  CALC_DATE: dayjs().subtract(1, 'day').format('YYYY-MM-DD'), // 하루전 데이터
});

const calcDate = ref('');
const InterSearchData = ref({});
const TaxiSearchData = ref({});
const PreRegiSearchData = ref({});

const validateDate = (date) => {
  const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
  if (!date || !dateRegex.test(date)) return false;

  const parsedDate = dayjs(date);
  return parsedDate.isValid();
};

const handleSearch = async () => {
  await getCenterLockYn();
  await getIsDayFin();
  if (!validateDate(searchData.value.CALC_DATE)) {
    showMessage('유효한 날짜를 선택해주세요.', 'error');
    return;
  }

  calcDate.value = dayjs(searchData.value.CALC_DATE).format('YYYY-MM-DD');
  try {
    const responseInter = await request('post', '/api/inter/getInter', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
      FILE_DIV: 'DAYE',
    });
    if (responseInter.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'error');
    }
    InterSearchData.value = responseInter;

    const responseTaxi = await request('post', '/api/inter/getTaxi', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
    });
    TaxiSearchData.value = responseTaxi.TAXI_TRNRCP_DT;

    const responsePreRegi = await request('post', '/api/inter/getPreRegi', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
    });
    PreRegiSearchData.value = responsePreRegi.PRE_TRNRCP_DT;
  } catch (error) {
    showMessage('송수신관리 데이터 조회 중 에러가 발생했습니다. 문제가 지속된다면 관리자한테 문의하세요.', 'error');
  }
};

const centerConYnStyle = computed(() => ({
  color: centerConYn.value === 'Y' ? 'blue' : 'red',
}));
const centerConYnText = computed(() => (centerConYn.value === 'Y' ? '연결성공' : '연결실패'));

const centerLockYnStyle = computed(() => ({
  color: centerLockYnMsg.value === '전송가능' ? 'blue' : 'red',
}));
const centerLockYnText = computed(() => (centerConYn.value === 'N' ? '확인불가' : centerLockYnMsg.value));

const isDayFinStyle = computed(() => ({
  color: isDayFin.value === '일마감미완료' ? 'red' : 'blue',
}));
const isDayFinText = computed(() => (isDayFin.value === '일마감미완료' ? 'LOCK (전송불가)' : '전송가능'));

const centerLockYn = ref('');
const centerLockYnMsg = ref('');
const centerConYn = ref('');
const centerConLoading = ref(false);
const getCenterLockYn = async () => {
  if (!validateDate(searchData.value.CALC_DATE)) {
    return;
  }

  try {
    centerConLoading.value = true;
    const response = await request('post', '/api/inter/getCenterLockYn', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
    });

    if (response.D_CLOSE_YN === 'Y') {
      centerLockYnMsg.value = 'LOCK (전송불가)';
    } else if (response.D_CLOSE_YN === 'N' || response.D_CLOSE_YN === 'NULL') {
      centerLockYnMsg.value = '전송가능';
      centerLockYn.value = 'N';
    }

    if (response?.status === 1 || response?.status === 2) {
      centerConYn.value = 'Y';
    } else {
      // 센터 연결불가
      centerConYn.value = response?.status === -5 ? 'N' : 'Y';
      showMessage(response.message, 'error');
    }
  } catch (error) {
    if (error.response && error.response.data) {
      alert(`DB 에러: ${error.response.data.message}`);
    } else {
      alert(`시스템 에러: ${error.message}`);
    }
  } finally {
    centerConLoading.value = false;
  }
};

const isDayFin = ref('');
const getIsDayFin = async () => {
  if (!validateDate(searchData.value.CALC_DATE)) return;

  try {
    const response = await request('post', '/api/inter/getIsDayFin', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
    });
    isDayFin.value = response.DAY_FIN_STATE;
  } catch (error) {
    showMessage(`일 마감 상태 조회 중 에러가 발생했습니다: ${error.message}`, 'error');
  }
};

const dayChecked = computed(() => {
  return centerLockYnMsg.value === '전송가능' && isDayFin.value === '일마감완료';
});

const taxiPreChecked = computed(() => {
  return TaxiSearchData.value !== '미수신' && PreRegiSearchData.value !== '미수신';
});

const isLoading = ref(false);
const transDayFin = async () => {
  if (dayChecked.value === false) {
    alert('센터 LOCK 여부와 일마감 여부를 확인하세요.');
    return;
  }
  const today = new Date();
  if (!validateDate(searchData.value.CALC_DATE)) {
    showMessage('유효한 날짜를 선택해주세요.', 'error');
    return;
  }

  if (dayjs(today).format('YYYY-MM-DD') !== searchData.value.CALC_DATE) {
    if (!confirm(`정산일자 [${searchData.value.CALC_DATE}] 정산요청을 하시겠습니까?`)) {
      return;
    }
    try {
      isLoading.value = true;

      const centerLockCheck = await request('post', '/api/inter/centerLockCheck', {
        IC_CODE: localStorage.getItem('loginIcCode'),
        CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
      });

      if (centerLockCheck.lock === 'Y') {
        alert(`[${searchData.value.CALC_DATE}] 일자로 센터에 락이 걸려 있습니다.\n페이지 새로고침을 합니다.`);
        return;
      }

      const response = await request('post', '/api/inter/requestInter', {
        IC_CODE: localStorage.getItem('loginIcCode'),
        CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
        WORKER_NO: localStorage.getItem('loginWorkerNo'),
      });
      if (response.RESULT_CODE === 0) {
        alert(response.SUCCESS_MSG);
      } else if (response.RESULT_CODE === 1) {
        alert(response.FAIL_MSG);
      } else if (response.RESULT_CODE === -1) {
        alert(response.UNKNOWN_MSG);
      } else if (response.status === 1) {
        alert(response.message);
      } else if (response.status === -1) {
        alert(response.message);
      }
    } catch (error) {
      showMessage(`서버 요청 중 에러가 발생했습니다: ${error.message}`, 'error');
    } finally {
      isLoading.value = false;
      handleSearch();
    }
  } else {
    showMessage('당일은 정산 요청을 할 수 없습니다.', 'error');
    return -1;
  }
};

const taxiPreLoading = ref(false);
const transTaxiPre = async () => {
  if (taxiPreChecked.value === false) {
    alert('공차택시와 사전등록 데이터 수신여부를 확인하세요.');
    return;
  }

  const today = new Date();

  if (!validateDate(searchData.value.CALC_DATE)) {
    showMessage('유효한 날짜를 선택해주세요.', 'error');
    return;
  }

  if (dayjs(today).format('YYYY-MM-DD') !== searchData.value.CALC_DATE) {
    if (!confirm(`정산일자 [${searchData.value.CALC_DATE}] 공차택시와 사전등록을 반영하여 일마감 재집계를 하시겠습니까?`)) {
      return;
    }
    try {
      taxiPreLoading.value = true;
      const response = await request('post', '/api/inter/requestReDayFin', {
        IC_CODE: localStorage.getItem('loginIcCode'),
        WORK_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
        WORKER_NO: localStorage.getItem('loginWorkerNo'),
        IS_HOLIDAY: false,
      });
      alert(response.message);
    } catch (error) {
      showMessage(`공차택시/사전등록 집계처리 요청 중 에러가 발생했습니다: ${error.message}`, 'error');
    } finally {
      taxiPreLoading.value = false;
      handleSearch();
    }
  } else {
    showMessage('당일은 재집계 요청을 할 수 없습니다.', 'error');
    return -1;
  }
};

const isHoliday = ref(false); // 특정일면제 확인용

const checkIsHoliday = async () => {
  try {
    const response = await request('post', '/api/inter/checkIsHoliday', {
      CALC_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
    });
    isHoliday.value = response;
  } catch (error) {
    showMessage('공휴일 확인 요청 중 에러가 발생했습니다.', 'error');
    isHoliday.value = false;
  }
};

const holidayPreLoading = ref(false);
const transHoliday = async () => {
  const today = new Date();

  if (!validateDate(searchData.value.CALC_DATE)) {
    showMessage('유효한 날짜를 선택해주세요.', 'error');
    return;
  }

  if (dayjs(today).format('YYYY-MM-DD') !== searchData.value.CALC_DATE) {
    if (!confirm(`정산일자 [${searchData.value.CALC_DATE}] 특정일면제 일마감 재집계를 하시겠습니까?`)) {
      return;
    }
    try {
      holidayPreLoading.value = true;
      const response = await request('post', '/api/inter/requestReDayFin', {
        IC_CODE: localStorage.getItem('loginIcCode'),
        WORK_DATE: dayjs(searchData.value.CALC_DATE).format('YYYYMMDD'),
        WORKER_NO: localStorage.getItem('loginWorkerNo'),
        IS_HOLIDAY: true,
      });
      alert(response.message);
    } catch (error) {
      showMessage(`특정일면제 집계처리 요청 중 에러가 발생했습니다: ${error.message}`, 'error');
    } finally {
      holidayPreLoading.value = false;
      handleSearch();
    }
  } else {
    showMessage('당일은 재집계 요청을 할 수 없습니다.', 'error');
    return -1;
  }
};

let timer = null;
watch(
  () => searchData.value.CALC_DATE,
  async (newValue) => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(async () => {
      if (validateDate(newValue)) {
        await handleSearch();
        await checkIsHoliday(); // 특정일면제 체크
      } else {
        showMessage('유효한 날짜를 입력해주세요.', 'error');
      }
    }, 700); // ms
  },
);

onMounted(() => {
  handleSearch();
});

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped>
.content-container {
  width: 1300px;
  margin-top: 1rem;
  margin-left: 10px;
}

.data-table {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.data-table th,
.data-table td {
  border: 1px solid #ddd;
}

.data-table th {
  background-color: #f2f2f2;
  text-align: center !important;
}

.blue-chk {
  width: 100%;
  font-size: 18px;
  background-color: #e3f2fd;
  color: #0d47a1;
  text-align: left;
  padding: 5px 25px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  font-weight: bold;
}

.day-process-btn {
  margin-left: 110px;
}

.day-fin-after-process-btn {
  margin-left: 35px;
}

.status-container {
  margin-top: 15px;
  margin-left: 10px;
}

.status-box {
  width: 1300px;
  padding: 10px 15px;
  border: 2px solid #dcdcdc;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: 10px;
}

.status-label {
  font-size: 15px !important;
  font-weight: bold;
}

.status-value {
  font-size: 15px !important;
  font-weight: bold;
  color: red;
}

.divider {
  margin: 0 10px;
  color: #aaa;
  font-weight: bold;
}

.header-cell {
  font-size: 16px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2);
  letter-spacing: 2px;
}

.chip-cell {
  border-radius: 5px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2);
}
</style>
