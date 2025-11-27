<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />

  <v-expansion-panels multiple>
    <v-expansion-panel v-for="(item, index) in PayContents" :key="index" class="custom-panel">
      <v-expansion-panel-title class="custom-panel-title">
        <th style="padding: 6px">날짜: {{ item.WORK_DATE }}</th>
        <th style="padding: 6px">건수: {{ comma(item.dateTotalCount) }} 건</th>
        <th style="padding: 6px">금액: {{ comma(item.dateTotalFare) }} 원</th>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-expansion-panels multiple>
          <v-expansion-panel bg-color="gray" class="custom-subpanel">
            <v-expansion-panel-title class="custom-subpanel-title">
              <th style="padding: 6px">선불전자카드</th>
              <th style="padding: 6px">건수: {{ comma(item.prepayTotalCount) }} 건</th>
              <th style="padding: 6px">금액: {{ comma(item.prepayTotalFare) }} 원</th>
            </v-expansion-panel-title>
            <v-expansion-panel-text class="table-container">
              <div>
                <TableComponent
                  :heightOffset="600"
                  rowType="1"
                  scrollKey="item.details.prepaid"
                  :headers="headers"
                  :contents="item.details.prepaid"
                />
                <LoadingComponent v-if="isLoading" />
              </div>
            </v-expansion-panel-text>
          </v-expansion-panel>

          <v-expansion-panel class="custom-subpanel">
            <v-expansion-panel-title class="custom-subpanel-title">
              <th style="padding: 6px">후불전자카드</th>
              <th style="padding: 6px">건수: {{ comma(item.postpayTotalCount) }} 건</th>
              <th style="padding: 6px">금액: {{ comma(item.postpayTotalFare) }} 원</th>
            </v-expansion-panel-title>
            <v-expansion-panel-text class="table-container">
              <div>
                <TableComponent
                  :heightOffset="600"
                  rowType="1"
                  scrollKey="item.details.postpaid"
                  :headers="headers"
                  :contents="item.details.postpaid"
                />
                <LoadingComponent v-if="isLoading" />
              </div>
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
  <div class="work-count-container">
    <span class="total-work">선불건수</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalPreCount)" disabled /> 건</span>
    <span class="total-work">선불금액</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalPreFare)" disabled /> 원</span>
    <span class="total-work">후불건수</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalPostCount)" disabled /> 건</span>
    <span class="total-work">후불금액</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalPostFare)" disabled /> 원</span>
    <span class="total-work">총 건수</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalCount)" disabled /> 건</span>
    <span class="total-work">총 금액</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalFare)" disabled /> 원</span>
  </div>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />
</template>

<script setup>
import { ref, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, comma, getSystemSmallCode, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';
const route = useRoute();

const authStore = useAuthStore();

const cardCoOption = ref([]);
const appCode = computed(() => route.path).value.replace('/', '');

cardCoOption.value = getSystemSmallCode('316', true);

const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');
const contentsFoot = ref({});
contentsFoot.value = {
  allTotalPreCount: 0,
  allTotalPreFare: 0,
  allTotalPostCount: 0,
  allTotalPostFare: 0,
  allTotalCount: 0,
  allTotalFare: 0,
};

const PayContents = ref([]);
const excelData = ref([]);

const headers = ref([
  { title: '근무일자', key: 'WORK_DATE', width: '200', excelWidth: 30 },
  { title: '차로명', key: 'IC_NM', width: '200', excelWidth: 30 },
  { title: '카드사', key: 'CARD_CO', width: '300', excelWidth: 30 },
  { title: '지불구분', key: 'CARD_TYPE_NM', width: '300', excelWidth: 30 },
  { title: '건수', key: 'CNT', width: '200', excelWidth: 30, customBodyCellStyle: 'table-body-style-right' },
  { title: '금액', key: 'CNT_FARE', width: '200', excelWidth: 30, customBodyCellStyle: 'table-body-style-right' },
]);

const searchHeader = ref([
  { label: '근무일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '카드사', key: 'CARD_CO_CODE', type: 'select', option: cardCoOption, width: '130px' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  CARD_CO_CODE: '',
});
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getCptLpayEcardCardCoDiv', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
    excelData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    const allTotalCount = ref(0);
    const allTotalFare = ref(0);
    const allTotalPreCount = ref(0);
    const allTotalPreFare = ref(0);
    const allTotalPostCount = ref(0);
    const allTotalPostFare = ref(0);

    const groupedData = data.reduce((acc, item) => {
      const date = item.WORK_DATE;
      if (!acc[date]) {
        acc[date] = {
          WORK_DATE: dayjs(date).format('YYYY-MM-DD'),
          details: { prepaid: [], postpaid: [] },
          dateTotalCount: 0,
          dateTotalFare: 0,
          prepayTotalCount: 0,
          prepayTotalFare: 0,
          postpayTotalCount: 0,
          postpayTotalFare: 0,
        };
      }

      const detailItem = headers.value.reduce((obj, header) => {
        header.key == 'CNT' || header.key == 'CNT_FARE' ? (obj[header.key] = comma(item[header.key])) : (obj[header.key] = item[header.key]);
        return obj;
      }, {});

      // 선불카드 071
      if (item.CARD_TYPE === '00' || item.CARD_TYPE === '02' || item.CARD_TYPE === '03') {
        acc[date].details.prepaid.push(detailItem);
        acc[date].prepayTotalCount += item.CNT;
        acc[date].prepayTotalFare += item.CNT_FARE;
        //후불카드
      } else if (item.CARD_TYPE === '01' || item.CARD_TYPE === '04' || item.CARD_TYPE === '06') {
        acc[date].details.postpaid.push(detailItem);
        acc[date].postpayTotalCount += item.CNT;
        acc[date].postpayTotalFare += item.CNT_FARE;
      }

      acc[date].dateTotalCount += item.CNT;
      acc[date].dateTotalFare += item.CNT_FARE;

      return acc;
    }, {});

    PayContents.value = Object.values(groupedData);

    PayContents.value.map((obj) => {
      allTotalCount.value += obj.dateTotalCount;
      allTotalFare.value += obj.dateTotalFare;
      allTotalPreCount.value += obj.prepayTotalCount;
      allTotalPreFare.value += obj.prepayTotalFare;
      allTotalPostCount.value += obj.postpayTotalCount;
      allTotalPostFare.value += obj.postpayTotalFare;
    });

    contentsFoot.value = {
      allTotalPreCount: comma(allTotalPreCount.value),
      allTotalPreFare: comma(allTotalPreFare.value),
      allTotalPostCount: comma(allTotalPostCount.value),
      allTotalPostFare: comma(allTotalPostFare.value),
      allTotalCount: comma(allTotalCount.value),
      allTotalFare: comma(allTotalFare.value),
    };
  } catch (error) {
    alert(`호환/후불 전자카드 카드사별 내역조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (excelData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      excelData.value,
      '호환후불 전자카드 카드사별 내역조회',
      '호환후불 전자카드 카드사별 내역조회',
      [
        `선불건수: ${contentsFoot.value.allTotalPreCount} 건  선불금액: ${contentsFoot.value.allTotalPreFare} 원 후불건수: ${contentsFoot.value.allTotalPostCount} 건 후불금액: ${contentsFoot.value.allTotalPostFare} 원 총 건수: ${contentsFoot.value.allTotalCount} 원 총 금액: ${contentsFoot.value.allTotalFare} 원`,
      ],
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (excelData.value.length === 0) {
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
    jsonData.value = createOzDataset('/CARD/cptLpayEcardCardCoDiv.ozr', {
      CSV_DATA: excelData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '호환후불 전자카드 카드사 내역조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
      TOTAL_PRECOUNT: contentsFoot.value.allTotalPreCount + '건',
      TOTAL_PREFARE: contentsFoot.value.allTotalPreFare + '원',
      TOTAL_POSTCOUNT: contentsFoot.value.allTotalPostCount + '건',
      TOTAL_POSTFARE: contentsFoot.value.allTotalPostFare + '원',
      TOTAL_COUNT: contentsFoot.value.allTotalCount + '건',
      TOTAL_FARE: contentsFoot.value.allTotalFare + '원',
    });
    isActive.value = true;
  } catch (error) {
    alert(`호환/후불 전자카드 카드사별 내역조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
.custom-panel {
  border: 1px solid #d3d3d3;
  border-radius: 8px;
  margin-bottom: 10px;
  overflow: hidden;
}

.custom-panel-title {
  background-color: #e8e8e8;
  font-weight: bold;
  padding: 10px 15px;
}

.custom-subpanel {
  border-top: 1px solid #e0e0e0;
  margin-top: 10px;
}

.custom-subpanel-title {
  background-color: #e8e8e8;
  color: #555;
  padding: 8px 15px;
}

.table-container {
  overflow-y: auto;
  max-height: wrap;
}

.v-expansion-panel-title {
  font-size: 12px;
}

.v-expansion-panel-title,
.v-expansion-panel-text {
  transition: background-color 0.3s;
}

.v-expansion-panel-title:hover,
.v-expansion-panel-text:hover {
  background-color: #f0f0f0;
}
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
