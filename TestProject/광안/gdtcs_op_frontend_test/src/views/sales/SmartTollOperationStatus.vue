<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <v-window>
    <v-window-item style="margin: 0 auto; margin-left: 10px; width: 100%">
      <v-row justify="center">
        <v-col cols="5">
          <h3>공차택시 적용 전 {{ beforeMessage }}</h3>
          <v-card elevation="4" style="margin-top: 10px; margin-bottom: 5px" height="750px">
            <div style="margin: 20px">
              <h4>1) 하이패스, 비하이패스(사전등록), 면제차량, 미수납 건수, 금액</h4>
              <TableComponent :headers="beforeTaxiSumHeaders" :contents="beforeTaxiSumData" scrollKey="RuleScroll" rowType="1" :heightOffset="790" />
              <div style="display: flex; vertical-align: center; margin-top: 2px">
                <v-label style="margin: 0px 10px 0px 10px">
                  <v-icon icon="mdi-information-variant-circle-outline" style="margin-right: 5px" />
                  <span style="font-weight: bold; color: blue">면제 차량</span>
                  <span>에 택시(공차포함)가 포함되어 있음(한국교통안전공단 공차택시 자료 수신 30시간 소요)</span>
                </v-label>
              </div>
              <div style="display: flex; vertical-align: center; margin-top: 2px">
                <v-label style="margin: 0px 10px 0px 10px">
                  <v-icon icon="mdi-alert-rhombus-outline" style="margin-right: 5px" />
                  <span>OBU 장착 택시의 통행량이 면제로 합산 됨</span>
                </v-label>
              </div>
              <br />
              <h4>2) 면제차량</h4>
              <TableComponent
                :headers="beforeTaxiExmptHeaders"
                :contents="beforeTaxiExmptData"
                scrollKey="RuleScroll"
                rowType="1"
                :heightOffset="715"
              />
              <h4>※ 사전등록 회원현황</h4>
              <TableComponent :headers="preRegistHeaders" :contents="preRegistData" scrollKey="RuleScroll" rowType="1" :heightOffset="740" />
            </div>
          </v-card>
        </v-col>
        <v-col cols="5" style="margin-left: 50px">
          <h3>공차택시 적용 후 {{ afterMessage }}</h3>

          <v-card elevation="4" style="margin-top: 10px; margin-bottom: 5px" height="750px">
            <div style="margin: 20px">
              <h4>1) 하이패스, 비하이패스(사전등록), 면제차량, 미수납 건수, 금액</h4>
              <TableComponent :headers="afterTaxiSumHeaders" :contents="afterTaxiSumData" scrollKey="RuleScroll" rowType="1" :heightOffset="730" />
              <h4>2) 공차택시 결과</h4>
              <TableComponent
                :headers="afterTaxiResultHeaders"
                :contents="afterTaxiResultData"
                scrollKey="RuleScroll"
                rowType="1"
                :heightOffset="745"
              />
              <h4>3) 면제차량</h4>
              <TableComponent
                :headers="afterTaxiExmptHeaders"
                :contents="afterTaxiExmptData"
                scrollKey="RuleScroll"
                rowType="1"
                :heightOffset="700"
              />
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-window-item>
  </v-window>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />
</template>

<script setup>
import { ref, computed } from 'vue';
import { btnHandler, request, comma, createOzDataset } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const isLoading = ref(false);
const route = useRoute();
const authStore = useAuthStore();
const beforeTaxiSumData = ref([]);
const beforeTaxiExmptData = ref([]);
const preRegistData = ref([]);
const preRegistNormalData = ref([]);
const afterTaxiSumData = ref([]);
const afterTaxiResultData = ref([]);
const afterTaxiExmptData = ref([]);
const beforeMessage = ref('');
const afterMessage = ref('');

const searchHeader = ref([{ label: '근무일자', key: 'WORK_DATE', type: 'date', dateType: 'day' }]);
const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
});
const isActive = ref(false);
const jsonData = ref('');

const appCode = computed(() => route.path).value.replace('/', '');

const beforeTaxiSumHeaders = [
  { title: '구 분', key: 'BEF_DIV', width: '140' },
  { title: '건수(대)', key: 'BEF_CNT', width: '120' },
  { title: '비 율', key: 'BEF_PER', width: '100' },
  { title: '통행요금(원)', key: 'BEF_FARE', width: '180' },
];
const beforeTaxiExmptHeaders = [
  { title: '구 분', key: 'BEF_EXEM_DIV', width: '180' },
  { title: '건수(대)', key: 'BEF_EXEM_CNT', width: '130' },
  { title: '비 율', key: 'BEF_EXEM_PER', width: '120' },
];
const preRegistHeaders = [
  { title: '구 분', key: 'PRE_REG_NAME', width: '180' },
  { title: '건수(대)', key: 'PRE_REG_CNT', width: '130' },
  { title: '비 율', key: 'PRE_REG_PER', width: '120' },
];
const afterTaxiSumHeaders = [
  { title: '구 분', key: 'AFT_DIV', width: '140' },
  { title: '건수(대)', key: 'AFT_CNT', width: '120' },
  { title: '비 율', key: 'AFT_PER', width: '100' },
  { title: '통행요금(원)', key: 'AFT_FARE', width: '180' },
];
const afterTaxiResultHeaders = [
  { title: '공차택시 대상 여부', key: 'AFT_TAXI_TYPE', width: '180' },
  { title: '구 분', key: 'AFT_TAXI_DIV', width: '120' },
  { title: '건수(대)', key: 'AFT_TAXI_CNT', width: '100' },
  { title: '비 율', key: 'AFT_TAXI_PER', width: '100' },
  { title: '금액', key: 'AFT_TAXI_FARE', width: '100' },
];
const afterTaxiExmptHeaders = [
  { title: '구 분', key: 'AFT_EXEM_DIV', width: '180' },
  { title: '건수(대)', key: 'AFT_EXEM_CNT', width: '130' },
  { title: '비 율', key: 'AFT_EXEM_PER', width: '120' },
];

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/sales/getSmartTollOperationStatus', {
      ...searchData.value,
      ...{
        WORK_DATE: searchData.value['WORK_DATE'].replaceAll('-', ''),
      },
    });
    if (data.length == 0) alert(`데이터가 없습니다.`);

    beforeTaxiSumData.value = [];
    afterTaxiSumData.value = [];

    beforeTaxiSumData.value.push({
      BEF_DIV: '선불하이패스',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['선불하이패스']),
      BEF_PER:
        (
          (data['BEFORE_AFTER_ALL'][2]['선불하이패스'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) *
          100
        ).toFixed(2) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['선불하이패스']),
    });

    beforeTaxiSumData.value.push({
      BEF_DIV: '후불하이패스',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['후불하이패스']),
      BEF_PER:
        (
          (data['BEFORE_AFTER_ALL'][2]['후불하이패스'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) *
          100
        ).toFixed(2) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['후불하이패스']),
    });

    beforeTaxiSumData.value.push({
      BEF_DIV: '사전등록',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['사전등록']),
      BEF_PER:
        (
          (data['BEFORE_AFTER_ALL'][2]['사전등록'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) *
          100
        ).toFixed(2) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['사전등록']),
    });

    beforeTaxiSumData.value.push({
      BEF_DIV: '면제',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['면제']),
      BEF_PER:
        ((data['BEFORE_AFTER_ALL'][2]['면제'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['면제']),
    });

    beforeTaxiSumData.value.push({
      BEF_DIV: '미납',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['미납']),
      BEF_PER:
        ((data['BEFORE_AFTER_ALL'][2]['미납'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['미납']),
    });

    beforeTaxiSumData.value.push({
      BEF_DIV: '합계',
      BEF_CNT: comma(data['BEFORE_AFTER_ALL'][2]['합계']),
      BEF_PER:
        ((data['BEFORE_AFTER_ALL'][2]['합계'] / (data['BEFORE_AFTER_ALL'][2]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][2]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      BEF_FARE: comma(data['BEFORE_AFTER_ALL'][3]['합계']),
    });

    beforeTaxiExmptData.value = data['BEFORE_EXEM'];

    const preData = ref({});

    data['PRE_REG'].forEach((header) => {
      const key = header['PRE_REG_NAME'];

      preData.value[key] = {
        NAME: header['PRE_REG_NAME'],
        CNT: header['PRE_REG_O_CNT'],
        PER: header['PRE_REG_PER'],
      };
    });

    preRegistNormalData.value = data['PRE_REG'].reduce((arr, header) => {
      if (header['PRE_REG_NAME'] == '일반') {
        arr.push({
          PRE_REG_CNT: comma(data['PRE_REG_NORMAL']['0']['CNT']),
          PRE_REG_O_CNT: data['PRE_REG_NORMAL']['0']['CNT'],
          PRE_REG_PER: ((data['PRE_REG_NORMAL']['0']['CNT'] / preData.value['합계']['CNT']) * 100).toFixed(2) + '%',
          PRE_REG_NAME: header['PRE_REG_NAME'],
        });
      } else if (header['PRE_REG_NAME'] == '기타') {
        arr.push({
          PRE_REG_CNT: comma(header['PRE_REG_O_CNT'] + preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT']),
          PRE_REG_O_CNT: header['PRE_REG_O_CNT'] + preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT'],
          PRE_REG_PER:
            (
              ((header['PRE_REG_O_CNT'] + preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT']) / preData.value['합계']['CNT']) *
              100
            ).toFixed(2) + '%',
          PRE_REG_NAME: header['PRE_REG_NAME'],
        });
      } else {
        arr.push(header); // '일반'과 '기타'가 아닌 나머지 항목들은 그대로 추가
      }
      return arr;
    }, []);

    if (!preRegistNormalData.value.some((item) => item.PRE_REG_NAME === '기타')) {
      const etcData = {
        PRE_REG_CNT: comma(preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT']),
        PRE_REG_O_CNT: preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT'],
        PRE_REG_PER: (((preData.value['일반']['CNT'] - data['PRE_REG_NORMAL']['0']['CNT']) / preData.value['합계']['CNT']) * 100).toFixed(2) + '%',
        PRE_REG_NAME: '기타',
      };

      const insertIndex = Math.max(preRegistNormalData.value.length - 1, 0);
      preRegistNormalData.value.splice(insertIndex, 0, etcData);
    }

    preRegistData.value = preRegistNormalData.value;
    preRegistNormalData.value = data['PRE_REG_NORMAL'];
    afterTaxiResultData.value = data['AFTER_RESULT'];

    afterTaxiSumData.value.push({
      AFT_DIV: '선불하이패스',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['선불하이패스']),
      AFT_PER:
        (
          (data['BEFORE_AFTER_ALL'][0]['선불하이패스'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) *
          100
        ).toFixed(2) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['선불하이패스']),
    });

    afterTaxiSumData.value.push({
      AFT_DIV: '후불하이패스',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['후불하이패스']),
      AFT_PER:
        (
          (data['BEFORE_AFTER_ALL'][0]['후불하이패스'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) *
          100
        ).toFixed(2) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['후불하이패스']),
    });

    afterTaxiSumData.value.push({
      AFT_DIV: '사전등록',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['사전등록']),
      AFT_PER:
        (
          (data['BEFORE_AFTER_ALL'][0]['사전등록'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) *
          100
        ).toFixed(2) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['사전등록']),
    });

    afterTaxiSumData.value.push({
      AFT_DIV: '면제',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['면제']),
      AFT_PER:
        ((data['BEFORE_AFTER_ALL'][0]['면제'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['면제']),
    });

    afterTaxiSumData.value.push({
      AFT_DIV: '미납',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['미납']),
      AFT_PER:
        ((data['BEFORE_AFTER_ALL'][0]['미납'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['미납']),
    });

    afterTaxiSumData.value.push({
      AFT_DIV: '합계',
      AFT_CNT: comma(data['BEFORE_AFTER_ALL'][0]['합계']),
      AFT_PER:
        ((data['BEFORE_AFTER_ALL'][0]['합계'] / (data['BEFORE_AFTER_ALL'][0]['합계'] === 0 ? 1 : data['BEFORE_AFTER_ALL'][0]['합계'])) * 100).toFixed(
          2,
        ) + '%',
      AFT_FARE: comma(data['BEFORE_AFTER_ALL'][1]['합계']),
    });

    afterTaxiExmptData.value = data['AFTER_EXEM'];

    if (afterTaxiSumData.value[5]['AFT_FARE'] == 0) {
      afterMessage.value = '[' + searchData.value['WORK_DATE'] + ' 사전등록/택시결과 미수신 상태입니다.]';
    } else {
      afterMessage.value = '';
    }

    if (beforeTaxiSumData.value[5]['BEF_FARE'] == 0) {
      beforeMessage.value = '[' + searchData.value['WORK_DATE'] + ' 일마감 미처리 상태입니다.]';
    } else {
      beforeMessage.value = '';
    }
  } catch (error) {
    alert(`조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (beforeTaxiSumData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
    jsonData.value = createOzDataset('/SALES/smartTollingOperation.ozr', {
      CSV_DATA_B1: beforeTaxiSumData.value,
      CSV_DATA_B2: beforeTaxiExmptData.value,
      CSV_DATA_B3: preRegistData.value,
      CSV_DATA_A1: afterTaxiSumData.value,
      CSV_DATA_A2: afterTaxiResultData.value,
      CSV_DATA_A3: afterTaxiExmptData.value,
      START_DATE: searchData.value['WORK_DATE'],
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      APPROVAL_IMG_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
    });
    isActive.value = true;
  } catch (error) {
    alert(`Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

btnHandler({
  Search: handleSearch,
  Print: handlePrint,
});
</script>

<style scoped>
h3 {
  margin-top: 7px;
}
.tab-container {
  margin: 0 auto;
  margin-left: 30px;
  padding-right: 30px;
}
.tab-container .v-btn {
  font-weight: bold;
}
.tab-window-container {
  /* background-color: #0086e510; */
  margin: 20px 20px 10px 20px;
}
.tab-window-contents-container {
  /* background-color: #0086e510; */
  margin-bottom: 17px;
}
</style>
