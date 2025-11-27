<template>
  <LoadingComponent v-if="onProcessing.loading" />
  <LoadingComponent v-if="onProcessing.processingAll" :message="`일괄처리 중입니다.`" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeaders" v-model="searchData.current">
      <template v-slot:header_btn>
        <v-row>
          <v-spacer />
          <v-radio-group inline hide-details v-model="radioModel">
            <v-spacer />
            <v-radio v-for="item in radioOptionRefundType" :key="item" :label="item.title" :value="item.value" />
            <v-btn
              :readonly="onProcessing.processingAll"
              style="margin: 0px 10px"
              size="small"
              text="일괄처리"
              @click="onClickProcessChecked(radioModel)"
            />
          </v-radio-group>
        </v-row>
      </template>
    </SearchDataComponent>
  </div>
  <div style="display: flex; justify-content: flex-end">
    <v-col :cols="detailInfo.item.REFUND_DIV_DP ? 8 : 12">
      <TableComponent
        scroll-key="RefundDetailList"
        :headers="mainHeaders"
        :contents="mainContents"
        :footer-contents="mainFooterContents"
        :custom-body-row-style="ifRefunded"
        :height-offset="heightOffset"
        @update:selected-items="updateSelectedItems"
        @grid-click-event="onClickGridRow"
      />
    </v-col>
    <v-col :cols="detailInfo.item.REFUND_DIV_DP ? 4 : 0">
      <div style="background-color: #f5f5f5">
        <v-card v-if="detailInfo.item.REFUND_DIV_DP" elevation="2" style="width: 500px; margin: 7px; margin-right: 3px">
          <LoadingComponent v-if="onProcessing.loadingDetail" />
          <LoadingComponent v-if="onProcessing.processing" :message="`처리 중입니다.`" />
          <v-card-text>
            <v-col>
              <div style="display: flex; align-items: stretch; margin: 7px">
                <v-label style="margin-top: 14px; align-self: center; font-size: 16px !important; font-weight: bold" :text="`기본 정보`" />
              </div>
              <GridSystemComponent
                :headers="detailInfo.headers.base"
                :contents="detailInfo.item"
                :table-th-width="'18%'"
                :cols-per-row="2"
                :row-height="'30px'"
              />
            </v-col>
            <v-col>
              <div style="display: flex; align-items: stretch; margin: 7px">
                <v-label style="margin-top: 14px; align-self: center; font-size: 16px !important; font-weight: bold" :text="`지불 정보`" />
              </div>
              <GridSystemComponent
                :headers="detailInfo.headers.payment"
                :contents="detailInfo.item.payment"
                :table-th-width="'18%'"
                :cols-per-row="2"
                :row-height="'30px'"
              />
            </v-col>
            <v-col>
              <div style="display: flex; align-items: stretch; margin: 7px">
                <v-label style="margin-top: 14px; align-self: center; font-size: 16px !important; font-weight: bold" :text="`심사 정보`" />
              </div>
              <GridSystemComponent
                :headers="detailInfo.headers.correction"
                :contents="detailInfo.item.correction"
                :table-th-width="'18%'"
                :cols-per-row="2"
                :row-height="'30px'"
              />
            </v-col>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn
              v-if="detailInfo.item.REFUND_TYPE === '1'"
              style="font-weight: bold"
              variant="tonal"
              color="red-lighten-2"
              text="취소처리"
              @click="onClickRequestCancel"
            />
            <v-btn
              v-if="detailInfo.item.REFUND_TYPE !== '1'"
              style="font-weight: bold"
              variant="flat"
              color="blue darken-1"
              text="환불처리"
              @click="onClickRequestRefund"
            />
          </v-card-actions>
        </v-card>
      </div>
    </v-col>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores';
import { btnHandler, getSystemSmallCode, request, showMessage } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import dayjs from 'dayjs';
import { nextTick, onActivated, reactive, ref } from 'vue';

const authStore = useAuthStore();

/* Base: 189
 * Search: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;
const onProcessing = reactive({
  loading: false,
  loadingDetail: false,
  processing: false,
  processingAll: false,
});
const appTitle = `환불 명세 조회`;

const selectOptionRefundDiv = ref([]);
const selectOptionRefundType = ref([]);
const radioOptionRefundType = [
  { title: '환불처리', value: '1' },
  { title: '취소처리', value: '2' },
];
const selectOptionType = [
  { title: '근무일자', value: '0' },
  { title: '환불처리일자', value: '1' },
];

const radioModel = ref('1');

onActivated(async () => {
  selectOptionRefundDiv.value = getSystemSmallCode('560', true);
  selectOptionRefundType.value = getSystemSmallCode('561', true);
  await nextTick();
});

const searchHeaders = ref([
  { label: '', key: 'SEARCH_TYPE', type: 'select', option: selectOptionType, width: '110px' },
  { label: ':', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '발생구분', key: 'REFUND_DIV', type: 'select', option: selectOptionRefundDiv, width: '120px' },
  { label: '처리구분', key: 'REFUND_TYPE', type: 'select', option: selectOptionRefundType },
  { label: '처리차량번호', key: 'HAND_CAR_NO', type: 'input', valid: 'digit|korean|english' },
]);

const searchData = reactive({
  current: {
    SEARCH_TYPE: '0',
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    REFUND_DIV: '',
    REFUND_TYPE: '',
    HAND_CAR_NO: '',
  },
  prev: {},
});

const mainHeaders = [
  { key: 'checkBox', title: '', width: '40' },
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '처리시각', key: 'HAND_DT', width: '160' },
  { title: '생성일자', key: 'MAKE_DATE_DP', width: '90' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '수납요금', key: 'PAY_FARE_DP', width: '78', customBodyCellStyle: 'table-body-style-right' },
  { title: '환불요금', key: 'REFUND_FARE_DP', width: '78', customBodyCellStyle: 'table-body-style-right' },
  { title: '발생구분', key: 'REFUND_DIV_DP', width: '100' },
  { title: '처리구분', key: 'REFUND_TYPE_DP', width: '80' },
  { title: '담당자', key: 'REFUND_ADMIN_DP', width: '125' },
  { title: '환불처리시각', key: 'REFUND_DT_DP', width: '160' },
  { title: '고객명', key: 'MBR_NM', width: '160' },
  { title: '은행', key: 'BANK_TYPE', width: '160' },
  { title: '계좌번호', key: 'BACNT_NO', width: '160' },
  { title: '계좌유무', key: 'BACNT_YN', width: '100' },
];
const mainContents = ref([]);

const selectedItems = ref([]);
const updateSelectedItems = (items) => {
  selectedItems.value = items;
};

const subTotalCount = ref('0');
const subTotalPayFare = ref('0');
const subTotalRefundFare = ref('0');
const subTotalRefundAmount = ref('0');

const mainFooterContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '총 수납요금', value: subTotalPayFare, unit: '원' },
  { title: '총 환불요금', value: subTotalRefundFare, unit: '원' },
  { title: '총 지급금액', value: subTotalRefundAmount, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalPayFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.PAY_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalRefundFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.REFUND_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalRefundAmount.value = mainContents.value
    .reduce((acc, item) => {
      if (item.REFUND_TYPE === '1') {
        return acc + item.REFUND_FARE;
      } else {
        return acc;
      }
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const ifRefunded = (item) => {
  if (item.REFUND_TYPE === '1') {
    return 'body-row-style-processed';
  } else if (item.REFUND_TYPE === '2') {
    return 'body-row-style-canceled';
  }
  return '';
};

const detailInfo = reactive({
  headers: {
    base: [
      { title: '영업소', key: 'IC_CODE_DP' },
      { title: '근무일자', key: 'WORK_DATE_DP' },
      { title: '근무번호', key: 'WORK_NO' },
      { title: '일련번호', key: 'HAND_SNO' },
      { title: '환불발생구분', key: 'REFUND_DIV_DP' },
      { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
      { title: '발생시각', key: 'OCC_DT_DP' },
      { title: '차량번호', key: 'HAND_CAR_NO' },
    ],
    payment: [
      { title: '전자카드속성', key: 'ECARD_ATT_DP' },
      { title: 'OBU속성', key: 'OBU_ATT_DP' },
      { title: '전자카드번호', key: 'CARD_NO_DP' },
      { title: '카드지불구분', key: 'ECARD_PAY_DIV_DP' },
      { title: '대표수납구분', key: 'MAIN_PAY_DIV_DP' },
      { title: '전자카드종류', key: 'ECARD_TYPE_DP' },
      { title: '수납요금', key: 'PAY_FARE_DP' },
      { title: '환불요금', key: 'REFUND_FARE_DP' },
    ],
    correction: [
      { title: '면제심사상세', key: 'EXM_DTL_DP' },
      { title: '처리시각', key: 'HAND_DT_DP' },
    ],
  },
  item: { payment: {}, correction: {} },
});

const onClickGridRow = async (item) => {
  try {
    onProcessing.loadingDetail = true;
    const response = await request('post', 'api/settle/getRefundInfoDetail', {
      ...{
        IC_CODE: item.IC_CODE,
        WORK_DATE: item.WORK_DATE,
        WORK_NO: item.WORK_NO,
        HAND_SNO: item.HAND_SNO,
      },
    });
    detailInfo.item = response;
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'warning');
  } finally {
    onProcessing.loadingDetail = false;
  }
};

const onClickProcessChecked = async (processType) => {
  await requestSave(processType, selectedItems.value);
};

const onClickRequestRefund = async () => {
  await requestSave('1', [
    {
      IC_CODE: detailInfo.item.IC_CODE,
      WORK_DATE: detailInfo.item.WORK_DATE,
      WORK_NO: detailInfo.item.WORK_NO,
      HAND_SNO: detailInfo.item.HAND_SNO,
    },
  ]);
};

const onClickRequestCancel = async () => {
  await requestSave('2', [
    {
      IC_CODE: detailInfo.item.IC_CODE,
      WORK_DATE: detailInfo.item.WORK_DATE,
      WORK_NO: detailInfo.item.WORK_NO,
      HAND_SNO: detailInfo.item.HAND_SNO,
    },
  ]);
};

const handleSearch = async () => {
  try {
    onProcessing.loading = true;
    const response = await request('post', 'api/settle/getRefundInfoDetailList', {
      ...searchData.current,
      ...{
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      },
    });
    searchData.prev = { ...searchData.current };
    mainContents.value = response;
    if (mainContents.value.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'success');
    }
    setFooterContents();
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.loading = false;
  }
};

const requestSave = async (processType, items) => {
  try {
    onProcessing.processing = true;
    const response = await request('post', 'api/settle/setRefundInfoBatch', {
      REFUND_TYPE: processType,
      ADMIN: authStore.getWorkerNo,
      data: items,
    });
    showMessage(`${response.successCount}건 정상 처리되었습니다.`, 'success');
  } catch (error) {
    showMessage(`데이터 처리 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.processing = false;
    handleSearch();
  }
};

const handleExcel = async () => {
  const headerRow = 1;
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeaders.value, searchData.prev, mainHeaders, mainContents.value, appTitle, appTitle, [
      mainFooterContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>
