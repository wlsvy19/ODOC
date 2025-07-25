<template>
  <LoadingComponent v-if="onProcessing.loading.state" />
  <LoadingComponent v-if="onProcessing.processingAuto.state" :message="onProcessing.processingAuto.message" />
  <LoadingComponent v-if="onProcessing.processingBatch.state" :message="onProcessing.processingBatch.message" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData">
      <template v-slot:header_btn>
        <v-btn
          v-if="tab === 0"
          :readonly="onProcessing.processingAuto.state"
          style="margin-right: 10px"
          size="small"
          @click="onClickButtonNormalAuto"
          text="정상자동처리"
        ></v-btn>
        <v-btn
          v-if="tab === 0"
          :readonly="onProcessing.processingBatch.state"
          style="margin-right: 10px"
          size="small"
          @click="onClickButtonLowPollutionAuto"
          text="부정일괄처리"
        ></v-btn>
        <v-radio-group v-if="tab === 1" inline hide-details v-model="radioModelTaxi">
          <v-spacer />
          <v-radio v-for="item in radioOptionRequestTypeTaxi" :key="item" :label="item.title" :value="item.value" />
        </v-radio-group>
        <v-btn
          v-if="tab === 1"
          :readonly="onProcessing.processingBatch.state"
          style="margin: 0px 10px"
          size="small"
          @click="runCorrectionTaxiBatch(radioModelTaxi)"
          >일괄처리</v-btn
        >
      </template>
    </SearchDataComponent>
  </div>
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab>부산면제</v-tab>
    <v-tab>공차택시</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        scroll-key="ReductionCorrectionBSPlbase"
        :height-offset="heightOffset"
        :headers="plbaseMainHeaders"
        :contents="plbaseMainContents"
        :footer-contents="plbaseFooterContents"
        :custom-body-row-style="setCustomBodyRowStyle"
        @grid-dbl-click-event="onDoubleClickGridRowPlbase"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        scroll-key="ReductionCorrectionBSTaxi"
        :height-offset="heightOffset"
        :headers="taxiMainHeaders"
        :contents="taxiMainContents"
        :footer-contents="taxiFooterContents"
        :custom-body-row-style="setCustomBodyRowStyle"
        @update:selected-items="updateSelectedItemsTaxi"
        @grid-dbl-click-event="onDoubleClickGridRowTaxi"
      />
    </v-window-item>
  </v-window>

  <v-dialog
    class="dialog-form"
    v-model="plbaseDialog"
    max-width="920px"
    persistent
    z-index="1100"
    @keydown.enter="if (onProcessing.processingPlbase.state === false) onClickDialogButtonCorrection('1');"
  >
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>부산면제 감면심사</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <LoadingComponent v-if="onProcessing.processingPlbase.state" :message="onProcessing.processingPlbase.message" />
        <LoadingComponent v-if="onProcessing.checkingPL.state" :message="onProcessing.checkingPL.message" />
        <v-row>
          <v-col cols="7">
            <div class="v-dialog-content">
              <h3>기본정보</h3>
              <GridSystemComponent
                :headers="plbaseDialogDataHeaders"
                :contents="plbaseDialogDataContents"
                :table-th-width="'18%'"
                :cols-per-row="2"
                row-height="29px"
              />
            </div>
            <div class="v-dialog-content">
              <h3>심사정보</h3>
              <InputFormGrid
                :headers="plbaseDialogInputHeaders"
                v-model="plbaseDialogInputContents"
                :table-header-width="'18%'"
                :cols-per-row="2"
                v-model:is-valid="isValidInputFormPlbase"
                v-model:input-form="inputFormPlbase"
              />
            </div>
            <div class="v-dialog-content">
              <v-row>
                <v-spacer></v-spacer>
              </v-row>
            </div>
          </v-col>
          <v-col cols="5">
            <div class="v-dialog-content">
              <h3>차량사진</h3>
              <v-img v-if="plbaseDialogImageSrc" class="dialog-img-car" :src="plbaseDialogImageSrc" cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
              <v-img v-if="plbaseDialogImageSrcPlate" class="dialog-img-plate" :src="plbaseDialogImageSrcPlate">
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
            </div>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn :disabled="onProcessing.checkingPL.state" variant="tonal" color="blue darken-1" text="면제PL조회" @click="checkExemption()" />
        <v-btn
          :disabled="onProcessing.processingPlbase.state"
          variant="flat"
          color="blue darken-1"
          text="정상(감면)"
          @click="onClickDialogButtonCorrection('1')"
        />
        <v-btn
          :disabled="onProcessing.processingPlbase.state"
          variant="flat"
          color="blue darken-1"
          text="부정(미감면)"
          @click="onClickDialogButtonCorrection('3')"
        />
        <v-btn :disabled="onProcessing.processingPlbase.state" variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog
    class="dialog-form"
    v-model="taxiDialog"
    max-width="920px"
    persistent
    z-index="1100"
    @keydown.enter="if (onProcessing.processingTaxi.state === false) onClickDialogButtonCorrectionTaxi('2');"
  >
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>공차택시 감면심사</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <LoadingComponent v-if="onProcessing.processingTaxi.state" :message="onProcessing.processingTaxi.message" />
        <LoadingComponent v-if="onProcessing.checkingPL.state" :message="onProcessing.checkingPL.message" />
        <v-row>
          <v-col cols="7">
            <div class="v-dialog-content">
              <h3>기본정보</h3>
              <GridSystemComponent
                :headers="taxiDialogDataHeaders"
                :contents="taxiDialogDataContents"
                :table-th-width="'18%'"
                :cols-per-row="2"
                row-height="29px"
              />
            </div>
            <div class="v-dialog-content">
              <h3>심사정보</h3>
              <InputFormGrid
                :headers="taxiDialogInputHeaders"
                v-model="taxiDialogInputContents"
                :table-header-width="'18%'"
                :cols-per-row="2"
                v-model:is-valid="isValidInputFormTaxi"
                v-model:input-form="inputFormTaxi"
              />
            </div>
            <div class="v-dialog-content">
              <DialogButtonNavi
                :is-first="stateButtonNavi.isFirst"
                :is-last="stateButtonNavi.isLast"
                @btn-click-event-before="onClickDialogButtonBefore"
                @btn-click-event-after="onClickDialogButtonAfter"
              />
            </div>
          </v-col>
          <v-col cols="5">
            <div class="v-dialog-content">
              <h3>차량사진</h3>
              <v-img v-if="taxiDialogImageSrc" class="dialog-img-car" :src="taxiDialogImageSrc" cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
              <v-img v-if="taxiDialogImageSrcPlate" class="dialog-img-plate" :src="taxiDialogImageSrcPlate">
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
            </div>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn :disabled="onProcessing.checkingPL.state" variant="tonal" color="blue darken-1" text="면제PL조회" @click="checkExemption()" />
        <v-btn :disabled="onProcessing.processingTaxi.state" variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrectionTaxi('2')"
          >심사(감면)</v-btn
        >
        <v-btn :disabled="onProcessing.processingTaxi.state" variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrectionTaxi('3')"
          >부정(미감면)</v-btn
        >
        <v-btn :disabled="onProcessing.processingTaxi.state" variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <OZReportDialog v-model:isActive="isActiveViewer" :jsonData="reportRequestData" />
</template>

<script setup>
import { ref, watch, onActivated, reactive, computed, nextTick } from 'vue';
import {
  request,
  btnHandler,
  getLaneNo,
  showMessage,
  getImage,
  ImageCategory,
  getSystemSmallCode,
  getCondition,
  createOzDataset,
} from '@/utils/common';
import { ozAppImageUrl, useAuthStore, useColorStore } from '@/stores';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { InputRules } from '@/utils/rules';
import { useRoute } from 'vue-router';

//#region Common
const appTitle = `감면내역심사(부산)`;

const onProcessing = reactive({
  loading: { state: false },
  processingAuto: {
    state: false,
    message: `정상자동처리 중입니다.`,
  },
  processingBatch: {
    state: false,
    message: `일괄처리 중입니다.`,
  },
  processingPlbase: {
    state: false,
    message: `처리 중입니다.`,
  },
  processingTaxi: {
    state: false,
    message: `처리 중입니다.`,
  },
  checkingPL: {
    state: false,
    message: `면제PL 조회 중입니다.`,
  },
});

const authStore = useAuthStore();
const colorStore = useColorStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * TAB Header: 28
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 28 + 29 + 30;

const setCustomBodyRowStyle = (item) => {
  if (item.BS_EXM_TYPE_DIV_DP !== '미처리') {
    return 'body-row-style-processed';
  }
  return '';
};
//#endregion

//#region Select Box Option
/* Select Box Option - search */
const selectOptLaneNo = ref([]);
const selectOptBsExmTypeDiv = ref([]);
const selectOptBsExmTypeDtl = ref([]);
const selectOptionDialogExmTypeDtl = ref([]);
const selectOptionDialogExmTypeDtlTaxi = ref([]);
const selectOptBsExmTypeDivForTaxi = ref([]);
const radioOptionRequestTypeTaxi = [
  { title: '심사(감면)', value: '2' },
  { title: '부정(미감면)', value: '3' },
];

const radioModelTaxi = ref('2');

onActivated(async () => {
  selectOptLaneNo.value = await getLaneNo();
  selectOptBsExmTypeDiv.value = getSystemSmallCode('294', true);
  selectOptBsExmTypeDtl.value = getSystemSmallCode('292', true);
  selectOptBsExmTypeDtl.value = selectOptBsExmTypeDtl.value.filter((item) => item.value !== '08');
  selectOptBsExmTypeDivForTaxi.value = selectOptBsExmTypeDiv.value.filter((item) => item.value !== '1' && item.value !== '4');
  selectOptionDialogExmTypeDtl.value = [...selectOptBsExmTypeDtl.value];
  // selectOptionDialogExmTypeDtl.value.splice(2, 3);
  selectOptionDialogExmTypeDtl.value.splice(0, 1);
  selectOptionDialogExmTypeDtlTaxi.value = getSystemSmallCode('292', true);
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});
//#endregion

//#region Search
const searchHeader = ref([]);
const searchData = ref();

const plbaseSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '심사구분', key: 'BS_EXM_TYPE_DIV', type: 'select', option: selectOptBsExmTypeDiv, width: '130px' },
  { label: '면제구분', key: 'BS_EXM_TYPE_DTL', type: 'select', option: selectOptBsExmTypeDtl, width: '130px' },
  { label: '처리차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px' },
  { label: '수납금액', key: 'PAY_FARE', type: 'input', width: '110px', valid: 'digit' },
];

const plbaseSearchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    BS_EXM_TYPE_DIV: '',
    BS_EXM_TYPE_DTL: '',
    HAND_CAR_NO: '',
    PAY_FARE: '',
  },
  prev: {},
});

const taxiSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '심사구분', key: 'BS_EXM_TYPE_DIV', type: 'select', option: selectOptBsExmTypeDivForTaxi, width: '130px' },
  { label: '처리차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px', valid: 'digit|korean' },
];

const taxiSearchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    BS_EXM_TYPE_DIV: '',
    BS_EXM_TYPE_DTL: '08',
    HAND_CAR_NO: '',
  },
  prev: {},
});
//#endregion

//#region Tab Action
const tab = ref(0);

watch(
  tab,
  () => {
    if (tab.value === 0) {
      searchHeader.value = plbaseSearchHeader;
      searchData.value = plbaseSearchData.current;
    } else if (tab.value === 1) {
      searchHeader.value = taxiSearchHeader;
      searchData.value = taxiSearchData.current;
    }
  },
  {
    immediate: true,
  },
);
//#endregion

//#region Main Grid - 부산면제
const plbaseMainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '90' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '90' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '100' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '면제구분', key: 'BS_EXM_TYPE_DTL_DP', width: '90' },
  { title: '위치조회', key: 'LC_INFO_RSLT_DP', width: '90' },
  { title: '심사구분', key: 'BS_EXM_TYPE_DIV_DP', width: '90' },
  { title: '비고', key: 'NOTE', width: '200' },
  { title: '근무자', key: 'ADMIN_DP', width: '110' },
]);

const plbaseMainContents = ref([]);

const plbaseSubTotalCount = ref('0');
const plbaseSubTotalSumOriginFare = ref('0');

const plbaseFooterContents = ref([
  { title: '총 건수', value: plbaseSubTotalCount, unit: '건' },
  { title: '원통행요금', value: plbaseSubTotalSumOriginFare, unit: '원' },
]);

const setPlbaseFooterContents = () => {
  plbaseSubTotalCount.value = plbaseMainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  plbaseSubTotalSumOriginFare.value = plbaseMainContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
//#endregion

// #region Dialog - 공통
const onClickCloseDialog = () => {
  if (tab.value === 0) plbaseDialog.value = false;
  if (tab.value === 1) taxiDialog.value = false;
  handleSearch();
};
//#endregion

//#region 면제PL확인
const checkExemption = async () => {
  let carNo = tab.value === 0 ? plbaseDialogInputContents.value.NEW_CAR_NO : taxiDialogInputContents.value.NEW_CAR_NO;
  let params = {
    PACKET_CAR_NO: carNo,
  };
  try {
    onProcessing.checkingPL.state = true;
    const res = await request('post', 'api/card/requestExemPL', {
      ...params,
      //요청전문 'focus,123수1234',
      PACKET_AUTH_CHAR: 'focus',
      //PACKET_SORT: ',',
      PACKET_SORT: String.fromCharCode(0x02),
      PACKET_END_CHAR: '\r\n',
      IC_CODE: authStore.getIcCode,
      DATA: 'focus' + String.fromCharCode(0x02) + Object.values(params).join('') + '\r\n',
    });
    if (typeof res.ERROR_MSG != 'object') {
      alert(res.ERROR_MSG);
    } else {
      let alertmessage = '';
      alertmessage = `차량번호: ${res.ERROR_MSG['차량번호']}\n`;
      alertmessage = alertmessage + `차종: ${res.ERROR_MSG['차종']}\n`;
      alertmessage = alertmessage + `이름: ${res.ERROR_MSG['이름']}\n`;
      alertmessage = alertmessage + `주소: ${res.ERROR_MSG['주소']}\n`;
      alertmessage = alertmessage + `결과: ${res.ERROR_MSG['면제 결과']}`;
      alert(alertmessage);
    }
  } catch (e) {
    showMessage(`면제 PL 요청 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.checkingPL.state = false;
  }
};

//#endregion
// #region Dialog - PL기반
const plbaseDialog = ref(false);
const plbaseDialogImageSrc = ref('');
const plbaseDialogImageSrcPlate = ref('');

const plbaseDialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: '발생일시', key: 'HAND_DT_DP' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
  { title: '처리일련번호', key: 'HAND_SNO' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP' },
  { title: '전자카드번호', key: 'CARD_NO_DP' },
  { title: '통행요금', key: 'PASS_FARE_DP' },
  { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: '수납요금', key: 'PAY_FARE_DP' },
  { title: '전자카드종류', key: 'ECARD_TYPE_DP' },
  { title: '대표수납구분', key: 'MAIN_PAY_DIV_DP' },
  { title: 'OBU속성', key: 'OBU_ATT_DP' },
  { title: '심사구분', key: 'BS_EXM_TYPE_DIV_DP' },
  { title: '위치처리시각', key: 'LC_HAND_DT_DP' },
  { title: '위치조회결과', key: 'RSLT_NOTE_DP' },
]);

const plbaseDialogDataContents = ref({});

const plbaseDialogInputHeaders = ref([
  { title: '면제구분', key: 'NEW_BS_EXM_TYPE_DTL', option: 'select', selectItem: selectOptionDialogExmTypeDtl },
  { title: '처리차량번호', key: 'NEW_CAR_NO', rules: InputRules.carNoRule },
  { title: '비고', key: 'EXM_NOTE', size: 1, rules: InputRules.noteRule },
]);

const plbaseDialogInputContents = ref({});

const onDoubleClickGridRowPlbase = async (idx, item) => {
  plbaseDialog.value = true;
  plbaseDialogImageSrc.value = getImage(ImageCategory.CAR, item);
  plbaseDialogImageSrcPlate.value = getImage(ImageCategory.PLATE, item);
  plbaseDialogDataContents.value = { ...item };
  plbaseDialogInputContents.value = {
    NEW_BS_EXM_TYPE_DTL: item.BS_EXM_TYPE_DTL,
    NEW_CAR_NO: item.HAND_CAR_NO,
    EXM_NOTE: item.NOTE,
  };
};
//#endregion

//#region Main Grid - 공차택시
const taxiDialog = ref(false);
const taxiDialogImageSrc = ref('');
const taxiDialogImageSrcPlate = ref('');

const taxiMainHeaders = ref([
  { key: 'checkBox', title: '', width: '40' },
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '90' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '90' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '100' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '면제구분', key: 'BS_EXM_TYPE_DTL_DP', width: '90' },
  { title: '심사구분', key: 'BS_EXM_TYPE_DIV_DP', width: '90' },
  { title: '비고', key: 'NOTE', width: '200' },
  { title: '근무자', key: 'ADMIN_DP', width: '110' },
]);

const taxiMainContents = ref([]);

const taxiSubTotalCount = ref('0');
const taxiSubTotalSumOriginFare = ref('0');
const taxiSubTotalSumPassFare = ref('0');
const taxiSubTotalSumPayFare = ref('0');

const taxiFooterContents = ref([
  { title: '총 건수', value: taxiSubTotalCount, unit: '건' },
  { title: '원통행요금', value: taxiSubTotalSumOriginFare, unit: '원' },
  { title: '통행요금', value: taxiSubTotalSumPassFare, unit: '원' },
  { title: '수납금액', value: taxiSubTotalSumPayFare, unit: '원' },
]);

const setTaxiFooterContents = () => {
  taxiSubTotalCount.value = taxiMainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  taxiSubTotalSumOriginFare.value = taxiMainContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  taxiSubTotalSumPassFare.value = taxiMainContents.value
    .reduce((acc, item) => {
      return acc + item.PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  taxiSubTotalSumPayFare.value = taxiMainContents.value
    .reduce((acc, item) => {
      return acc + item.PAY_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
//#endregion

// #region Dialog - 공차택시
const taxiDialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: '발생일시', key: 'HAND_DT_DP' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
  { title: '처리일련번호', key: 'HAND_SNO' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP' },
  { title: '전자카드번호', key: 'CARD_NO_DP' },
  { title: '통행요금', key: 'PASS_FARE_DP' },
  { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: '수납요금', key: 'PAY_FARE_DP' },
  { title: '전자카드종류', key: 'ECARD_TYPE_DP' },
  { title: '면제구분', key: 'BS_EXM_TYPE_DTL_DP' },
]);

const taxiDialogDataContents = ref({});

const taxiDialogInputHeaders = ref([
  { title: '면제구분', key: 'NEW_BS_EXM_TYPE_DTL', option: 'select', selectItem: selectOptionDialogExmTypeDtlTaxi },
  { title: '처리차량번호', key: 'NEW_CAR_NO', rules: InputRules.carNoRule },
  { title: '비고', key: 'EXM_NOTE', size: 1, rules: InputRules.noteRule },
]);

const taxiDialogInputContents = ref({});

const selectedItemsTaxi = ref([]);
const updateSelectedItemsTaxi = (items) => {
  selectedItemsTaxi.value = items;
};

const onDoubleClickGridRowTaxi = async (idx) => {
  setDialogItemTaxi(idx);
  taxiDialog.value = true;
};
//#endregion

/* DialogButton - Navi */
const beforeIndex = ref(0);
const afterIndex = ref(0);

const stateButtonNavi = ref({
  isFirst: false,
  isLast: false,
});

const updateStateButtonNavi = (idx) => {
  beforeIndex.value = idx - 1;
  afterIndex.value = idx + 1;
  stateButtonNavi.value.isFirst = idx < 1 ? true : false;
  stateButtonNavi.value.isLast = idx >= taxiMainContents.value.length - 1 ? true : false;
};

const onClickDialogButtonBefore = () => {
  setDialogItemTaxi(beforeIndex.value);
};

const onClickDialogButtonAfter = () => {
  setDialogItemTaxi(afterIndex.value);
};

const setDialogItemTaxi = async (idx) => {
  try {
    const response = await request('post', 'api/office/getReductionBsDetail', taxiMainContents.value[idx]);
    taxiDialogDataContents.value = response;
    taxiDialogImageSrc.value = getImage(ImageCategory.CAR, taxiDialogDataContents.value);
    taxiDialogImageSrcPlate.value = getImage(ImageCategory.PLATE, taxiDialogDataContents.value);
    taxiDialogInputContents.value = {
      NEW_BS_EXM_TYPE_DTL: taxiDialogDataContents.value.BS_EXM_TYPE_DTL,
      NEW_CAR_NO: taxiDialogDataContents.value.HAND_CAR_NO,
      EXM_NOTE: taxiDialogDataContents.value.NOTE,
    };
    updateStateButtonNavi(idx);
  } catch {
    showMessage('error', 'warning');
  }
};
//#endregion

//#region 심사 - 부산면제
const isValidInputFormPlbase = ref(true);
const inputFormPlbase = ref();

const onClickDialogButtonCorrection = (value) => {
  if (!isValidInputFormPlbase.value) {
    showMessage(`${inputFormPlbase.value.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (plbaseDialogInputContents.value.NEW_BS_EXM_TYPE_DTL === '01' && value !== '3') {
    showMessage(`면제아님은 정상(감면)처리할 수 없습니다.`, 'warning');
    return;
  }
  runCorrectionPlbase(value, [
    {
      IC_CODE: plbaseDialogDataContents.value.IC_CODE,
      WORK_DATE: plbaseDialogDataContents.value.WORK_DATE,
      WORK_NO: plbaseDialogDataContents.value.WORK_NO,
      HAND_SNO: plbaseDialogDataContents.value.HAND_SNO,
      NEW_BS_EXM_TYPE_DTL: plbaseDialogInputContents.value.NEW_BS_EXM_TYPE_DTL,
      NEW_CAR_NO: plbaseDialogInputContents.value.NEW_CAR_NO,
      EXM_NOTE: plbaseDialogInputContents.value.EXM_NOTE,
    },
  ]);
};

const onClickButtonNormalAuto = async () => {
  let requestId = '4'; //정상자동처리
  let requestNote = '부산면제 정상자동처리';
  let normalData = plbaseMainContents.value.filter((item) => item.BS_EXM_TYPE_DIV_DP === '미처리');
  // 면제아님[01], 조회불능[02], 저공해[17] 차량 정상자동처리 제외
  normalData = normalData.filter((item) => {
    return item.BS_EXM_TYPE_DTL !== '01' && item.BS_EXM_TYPE_DTL !== '02' && item.BS_EXM_TYPE_DTL !== '17';
  });
  normalData = normalData.filter((item) => {
    return !((item.BS_EXM_TYPE_DTL === '04' || item.BS_EXM_TYPE_DTL === '05') && item.BS_LC_INFO_RSLT !== '01');
  });
  // 공단요청 임시적용: 친환경 차량이 대표수납구분 친환경PL로 올라오지 않을 경우 자동처리 제외.
  normalData = normalData.filter((item) => {
    return !((item.BS_EXM_TYPE_DTL === '06' || item.BS_EXM_TYPE_DTL === '07') && item.MAIN_PAY_DIV !== '4');
  });
  if (normalData.length === 0) {
    showMessage(`자동처리할 데이터가 없습니다.`);
    return;
  }
  try {
    onProcessing.processingAuto.state = true;
    await runCorrectionPlbase(requestId, normalData, requestNote);
  } catch (error) {
    showMessage(`정상자동처리 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.processingAuto.state = false;
  }
};

const onClickButtonLowPollutionAuto = async () => {
  if (!confirm(`저공해차량, 장애인,유공자(본인탑승 미확인)차량을 일괄 부정처리 하시겠습니까?`)) return;
  let requestId = '5'; //부정처리
  let lowPollutionData = plbaseMainContents.value.filter((item) => item.BS_EXM_TYPE_DIV_DP === '미처리');
  lowPollutionData = lowPollutionData.filter(
    (item) => item.BS_EXM_TYPE_DTL === '17' || item.BS_EXM_TYPE_DTL === '04' || item.BS_EXM_TYPE_DTL === '05',
  );
  lowPollutionData = lowPollutionData.filter((item) => item.BS_LC_INFO_RSLT !== '01');
  if (lowPollutionData.length === 0) {
    showMessage(`일괄 처리할 데이터가 없습니다.`);
    return;
  }
  try {
    onProcessing.processingBatch.state = true;
    await runCorrectionPlbase(requestId, lowPollutionData, '부산면제 부정일괄처리');
  } catch (error) {
    showMessage(`일괄처리 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.processingBatch.state = false;
  }
};

const runCorrectionPlbase = async (requestId, targetList, requestNote) => {
  try {
    onProcessing.processingPlbase.state = true;

    let response = {
      successCount: 0,
      failCount: 0,
    };

    for (let i = 0; i < Math.floor(targetList.length / 100) + 1; i++) {
      let _response = await request('post', 'api/office/setReductionBsCorrection', {
        requestId: requestId,
        requestNote: requestNote,
        adminId: authStore.getWorkerNo,
        data: targetList.slice(i * 100, (i + 1) * 100),
      });
      response = {
        successCount: response.successCount + _response.successCount,
        failCount: response.failCount + _response.failCount,
      };
    }

    if (response.failCount === 0) {
      showMessage(`정상 처리되었습니다.(${response.successCount}건)`, 'success');
    } else {
      showMessage(`일마감된 항목이 실패 처리되었습니다.(${response.failCount}/${targetList.length}건)`, 'error');
    }
    onClickCloseDialog();
  } catch (error) {
    showMessage('심사처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    onProcessing.processingPlbase.state = false;
  }
};
//#endregion

//#region 심사 - 공차택시
const isValidInputFormTaxi = ref(true);
const inputFormTaxi = ref();

const onClickDialogButtonCorrectionTaxi = (value) => {
  if (!isValidInputFormTaxi.value) {
    showMessage(`${inputFormTaxi.value.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (taxiDialogInputContents.value.NEW_BS_EXM_TYPE_DTL !== '08') {
    if (!confirm(`면제구분을 변경할 경우, 공차택시로 다시 변경할 수 없습니다.\n계속 진행하시겠습니까?`)) return;
  }
  if (value === '2' && taxiDialogInputContents.value.NEW_CAR_NO === null) {
    showMessage(`처리차량번호가 공란인 경우 감면처리를 할 수 없습니다.`, 'warning');
    return;
  }
  if (taxiDialogInputContents.value.NEW_BS_EXM_TYPE_DTL === '01' && value !== '3') {
    showMessage(`면제아님은 정상(감면)처리할 수 없습니다.`, 'warning');
    return;
  }
  runCorrectionTaxi(value, { ...taxiDialogDataContents.value, ...taxiDialogInputContents.value });
};

const runCorrectionTaxi = async (requestId, targetItem) => {
  try {
    onProcessing.processingTaxi.state = true;
    let response = await request('post', 'api/office/setReductionBsCorrectionTaxi', {
      requestId: requestId,
      adminId: authStore.getWorkerNo,
      item: targetItem,
      isChanged: taxiDialogInputContents.value.NEW_BS_EXM_TYPE_DTL === '08' ? false : true,
    });
    if (response.failCount === 0) {
      showMessage(`정상 처리되었습니다.(${response.successCount}건)`, 'success');
      stateButtonNavi.value.isLast === true ? onClickCloseDialog() : onClickDialogButtonAfter();
    } else {
      showMessage(`일마감된 항목이 실패 처리되었습니다.(${response.failCount}건)`, 'error');
    }
  } catch (error) {
    showMessage('심사처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    onProcessing.processingTaxi.state = false;
  }
};

const runCorrectionTaxiBatch = async (requestId) => {
  try {
    onProcessing.processingBatch.state = true;
    let response = await request('post', 'api/office/setReductionBsCorrectionTaxiBatch', {
      requestId: requestId,
      adminId: authStore.getWorkerNo,
      data: selectedItemsTaxi.value, // selected
    });
    if (response.failCount === 0) {
      showMessage(`정상 처리되었습니다.(${response.successCount}건)`, 'success');
      stateButtonNavi.value.isLast === true ? onClickCloseDialog() : onClickDialogButtonAfter();
    } else {
      showMessage(`일마감된 항목이 실패 처리되었습니다.(${response.failCount}건)`, 'error');
    }
  } catch (error) {
    showMessage('심사처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    onProcessing.processingBatch.state = false;
    handleSearch();
  }
};
//#endregion

const handleSearch = async () => {
  try {
    onProcessing.loading.state = true;
    const response = await request('post', 'api/office/getReductionBsList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    if (tab.value === 0) {
      plbaseSearchData.prev = { ...searchData.value };
      plbaseMainContents.value = response;
      setPlbaseFooterContents();
    } else if (tab.value === 1) {
      taxiSearchData.prev = { ...searchData.value };
      taxiMainContents.value = response;
      setTaxiFooterContents();
    }
  } catch (error) {
    showMessage('error', 'warning');
  } finally {
    onProcessing.loading.state = false;
  }
};

const handleExcel = async () => {
  let headerRow = 1;
  let subTitle = '';
  let headers = [];
  let contents = [];
  let prevSearchData = {};
  let footer = '';
  if (tab.value === 0) {
    subTitle = 'PL';
    headers = [...plbaseMainHeaders.value];
    contents = [...plbaseMainContents.value];
    prevSearchData = { ...plbaseSearchData.prev };
    footer = plbaseFooterContents.value
      .map((item) => {
        return `${item.title}: ${item.value} ${item.unit}`;
      })
      .join(', ');
  } else if (tab.value === 1) {
    subTitle = '택시';
    headers = [...taxiMainHeaders.value];
    contents = [...taxiMainContents.value];
    prevSearchData = { ...taxiSearchData.prev };
    footer = taxiFooterContents.value
      .map((item) => {
        return `${item.title}: ${item.value} ${item.unit}`;
      })
      .join(', ');
  }
  if (contents.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, prevSearchData, headers, contents, subTitle, appTitle, [footer]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

//#region Report
const isActiveViewer = ref(false);
const reportRequestData = ref('');

const getReportRequestData = () => {
  let requestData = {};
  let subTitle = '';
  if (tab.value === 0) {
    subTitle = '부산면제';
    requestData = createOzDataset('/OFFICE/reductionBsPlbaseList.ozr', {
      CSV_DATA: plbaseMainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: `${appTitle} - ${subTitle}`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, plbaseSearchData.prev),
      TOTAL_COUNT: plbaseSubTotalCount.value + '건',
      TOTAL_ORIGIN_FARE: plbaseSubTotalSumOriginFare.value + '원',
    });
  } else if (tab.value === 1) {
    subTitle = '공차택시';
    requestData = createOzDataset('/OFFICE/reductionBsTaxiList.ozr', {
      CSV_DATA: taxiMainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: `${appTitle} - ${subTitle}`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, taxiSearchData.prev),
      TOTAL_COUNT: taxiSubTotalCount.value + '건',
      TOTAL_ORIGIN_FARE: taxiSubTotalSumOriginFare.value + '원',
      TOTAL_PASS_FARE: taxiSubTotalSumPassFare.value + '원',
      TOTAL_PAY_FARE: taxiSubTotalSumPayFare.value + '원',
    });
  }
  return requestData;
};

const handlePrint = () => {
  let contentsLength = 0;
  if (tab.value === 0) {
    contentsLength = plbaseMainContents.value.length;
  } else if (tab.value === 1) {
    contentsLength = taxiMainContents.value.length;
  }
  if (contentsLength === 0) {
    showMessage('데이터가 없습니다.');
    return;
  }
  reportRequestData.value = getReportRequestData();
  isActiveViewer.value = true;
};
//#endregion

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped>
/* UI */
.v-dialog-content {
  margin: 0px 7px 10px 7px;
}
.dialog-img-car {
  background-color: lightgray;
  width: 100%;
  height: 253px;
}
.dialog-img-plate {
  background-color: darkgray;
  height: 80px;
}
/* v-btn, v-label correction */
.button-correction {
  margin: 0.2em;
  padding: 0 7px;
}
.dialog-form .v-card .v-card-text {
  padding: 16px 24px 0px;
}
</style>
