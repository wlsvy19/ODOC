<template>
  <div style="display: flex; justify-content: flex-start;">
    <div style="width: 820px; margin-left: 6px; border-right: solid 5px #f5f5f5">
      <LoadingComponent v-if="onProcessing.submit.state" :message="onProcessing.submit.message" />
      <v-card elevation="4">
        <LoadingComponent v-if="onProcessing.loading.main.state" />
        <v-col>
          <div @keyup.enter="handleSearch">
            <SearchDataComponent :headers="searchHeader" v-model="searchData.current">
              <template v-slot:header_btn>
                <v-row>
                  <v-spacer />
                  <v-radio-group
                    style="margin: 0px 5px 0px 10px; background-color: #ebf7ff; border-radius: 5px"
                    inline
                    hide-details
                    v-model="processType"
                  >
                    <v-spacer />
                    <v-radio v-for="item in radioOptionRefundType" :key="item" :label="item.title" :value="item.value" />
                    <v-btn
                      :readonly="onProcessing.submit.state"
                      style="margin-left: 10px"
                      size="small"
                      text="일괄처리"
                      @click="onClickProcessChecked(processType)"
                    />
                  </v-radio-group>
                </v-row>
              </template>
            </SearchDataComponent>
          </div>
          <div>
            <TableComponent
              scroll-key="RefundManagementList"
              :headers="headers.main"
              :contents="contents.main"
              :footer-contents="footerContents.main"
              :height-offset="288"
              :custom-body-row-style="setCustomBodyRowStyleMain"
              @grid-click-event="onClickMainItem"
              @update:selected-items="onUpdateCheckedMainItems"
              row-type="1"
            />
          </div>
        </v-col>
      </v-card>
    </div>
    <v-col>
      <div style="display: grid; border-right: solid 5px #f5f5f5">
        <v-card elevation="4">
          <LoadingComponent v-if="onProcessing.loading.detail.state" />
          <v-col>
            <v-row :style="`min-height: 36px; background-color: #666666; color: ${currentMainItem.CATEGORY_ID == 'RFNDED' ? '#00ff00' : '#ffffff'}`">
              <v-label
                style="margin-left: 10px; font-size: 16px !important; font-weight: bold; opacity: 1"
                :text="`${currentMainItem.CATEGORY_ID ? currentMainItem.CATEGORY_NM + ' - ' + currentMainItem.HAND_CAR_NO : ''}`"
              />
            </v-row>
            <div>
              <TableComponent
                scroll-key="RefundManagementDetailUnrefund"
                :headers="headers.detail"
                :contents="contents.detail"
                :footer-contents="footerContents.detail"
                :height-offset="288"
                row-type="1"
              />
            </div>
          </v-col>
        </v-card>
      </div>
    </v-col>
  </div>
</template>

<script setup>
import { ref, reactive, onActivated } from 'vue';
import { request, btnHandler, showMessage, getSystemSmallCode } from '@/utils/common';
import { useAuthStore } from '@/stores/index';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';

const authStore = useAuthStore();
const codeCategoryID = ref([]);
const appTitle = `환불 명세 관리(차량 및 대상구분별)`;
const appSubTitle = `목록`;

/** radio */
const processType = ref('1');
const radioOptionRefundType = [
  { title: '환불처리', value: '1' },
  { title: '취소처리', value: '2' },
];

onActivated(async () => {
  codeCategoryID.value = getSystemSmallCode('562', true);
});

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', width: '100px', valid: 'digit|korean|english' },
  { label: '대상구분', key: 'CATEGORY_ID', width: '85px', type: 'select', option: codeCategoryID },
]);

const onClickProcessChecked = async (processType) => {
  await requestSave(processType, checkedMainItems.value);
};

const requestSave = async (processType, items) => {
  console.log(`items`, items);
  try {
    onProcessing.submit.state = true;
    const response = await request('post', 'api/settle/setRefundManagementBatch', {
      IC_CODE: searchData.prev.IC_CODE,
      START_DATE: searchData.prev.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.prev.END_DATE.replaceAll('-', ''),
      ADMIN: authStore.getWorkerNo,
      REFUND_TYPE: processType,
      LIST: items,
    });
    showMessage(`${response.successCount}건 정상 처리되었습니다.`, 'success');
    getRefundList();
  } catch (e) {
    showMessage(`일괄처리 중 오류가 발생했습니다.`, 'warning');
    console.log(`일괄처리 중 오류 발생: `, e);
  } finally {
    onProcessing.submit.state = false;
  }
};

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    CAR_NO: '',
    CATEGORY_ID: '',
  },
  prev: {},
});

const onProcessing = reactive({
  loading: {
    main: {
      state: false,
    },
    detail: {
      state: false,
    },
  },
  submit: {
    state: false,
    message: `일괄처리 중 입니다.`,
  },
});

const headers = reactive({
  main: [
    { key: 'checkBox', title: '', width: '40' },
    { title: '순번', key: 'ROW_NUMBER', width: '53' },
    { title: '차량번호', key: 'HAND_CAR_NO', width: '120' },
    { title: '대상구분', key: 'CATEGORY_NM', width: '80' },
    { title: '건수', key: 'TOTAL_CNT', width: '60', customBodyCellStyle: 'table-body-style-right' },
    { title: '환불요금', key: 'REFUND_FARE_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
    { title: '고객명', key: 'MBR_NM', width: '137' },
    { title: '은행', key: 'BANK_TYPE', width: '80' },
    { title: '계좌번호', key: 'BACNT_NO', width: '140' },
    { title: '계좌유무', key: 'BACNT_YN', width: '100' },
  ],
  detail: [
    { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
    { title: '근무번호', key: 'WORK_NO', width: '70' },
    { title: '일련번호', key: 'HAND_SNO', width: '70' },
    { title: '처리시각', key: 'HAND_DT', width: '160' },
    { title: '생성일자', key: 'MAKE_DATE_DP', width: '90' },
    { title: '차량번호', key: 'HAND_CAR_NO', width: '100' },
    { title: '환불요금', key: 'REFUND_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
    { title: '발생구분', key: 'REFUND_DIV_DP', width: '90' },
    { title: '처리구분', key: 'REFUND_TYPE_DP', width: '70' },
    { title: '담당자', key: 'REFUND_ADMIN_DP', width: '100' },
    { title: '환불처리시각', key: 'REFUND_DT_DP', width: '140' },
  ],
});

const setCustomBodyRowStyleMain = (item) => {
  if (item.CATEGORY_ID == 'RFNDED') {
    return 'body-row-style-processed-green';
  }
};

const contents = reactive({
  main: [],
  detail: [],
});

const checkedMainItems = ref([]);

const mainSubTotalCount = ref('0');
const detailSubTotalCount = ref('0');
const detailSubTotalRefundFare = ref('0');

const footerContents = reactive({
  main: [{ title: '총 건수', value: mainSubTotalCount, unit: '건' }],
  detail: [
    { title: '총 건수', value: detailSubTotalCount, unit: '건' },
    { title: '총 환불요금', value: detailSubTotalRefundFare, unit: '원' },
  ],
});

const setFooterContentsMain = () => {
  mainSubTotalCount.value = contents.main.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const setFooterContentsDetail = () => {
  detailSubTotalCount.value = contents.detail.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  detailSubTotalRefundFare.value = contents.detail
    .reduce((acc, item) => {
      return acc + item.REFUND_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const onUpdateCheckedMainItems = (items) => {
  checkedMainItems.value = items;
};

const currentMainItem = ref({});

const onClickMainItem = async (item) => {
  await getRefundDetailList(item);
};

const clearRefundManagementDetail = () => {
  currentMainItem.value = {};
  contents.detail = [];
};

const getRefundDetailList = async (item) => {
  try {
    onProcessing.loading.detail.state = true;
    clearRefundManagementDetail();
    const response = await request('post', 'api/settle/getRefundManagementDetailList', {
      IC_CODE: searchData.prev.IC_CODE,
      START_DATE: searchData.prev.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.prev.END_DATE.replaceAll('-', ''),
      HAND_CAR_NO: item.HAND_CAR_NO,
      CATEGORY_ID: item.CATEGORY_ID,
    });
    contents.detail = response;
    currentMainItem.value = { ...item };
    setFooterContentsDetail();
  } catch (e) {
    showMessage(`상세내역 조회 중 오류가 발생했습니다.`, 'warning');
    console.log(`상세조회 중 오류 발생: `, e);
  } finally {
    onProcessing.loading.detail.state = false;
  }
};

const getRefundList = async () => {
  try {
    onProcessing.loading.main.state = true;
    clearRefundManagementDetail();
    let params = {
      IC_CODE: authStore.getIcCode,
      START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      CAR_NO: searchData.current.CAR_NO,
      CATEGORY_ID: searchData.current.CATEGORY_ID,
    };
    const response = await request('post', 'api/settle/getRefundManagementList', params);
    contents.main = response;
    searchData.prev = { ...params };
    setFooterContentsMain();
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    onProcessing.loading.main.state = false;
  }
};

const handleSearch = async () => {
  await getRefundList();
};

const handleExcel = async () => {
  const headerRow = 1;
  if (contents.main.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, searchData.prev, headers.main, contents.main, appSubTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style>
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
</style>
