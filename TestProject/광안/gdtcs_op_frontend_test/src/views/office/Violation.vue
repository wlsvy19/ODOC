<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent :message="loadingState.msg" v-if="loadingState.isLoading" />
  <v-row>
    <v-row class="search-container">
      <v-card variant="text">
        <div @keyup.enter="handleSearch">
          <SearchDataComponent :headers="searchHeader" v-model="searchData">
            <template v-slot:header_btn>
              <v-row>
                <v-spacer />
                <v-radio-group inline hide-details max-width="730px" v-model="optionBatch['option']">
                  <v-radio v-for="item in radioBatch" :label="item.title" :value="item.value" :key="item" />&ensp;
                  <v-btn size="small" @click="btnBatch">일괄처리</v-btn>
                  <v-btn size="small" @click="btnPlAuto">경차PL자동처리</v-btn>
                  <v-btn size="small" @click="btnExmtPlAuto">면제PL자동처리</v-btn>
                  <v-btn size="small" @click="btnRegAuto">사전등록자동처리</v-btn>
                </v-radio-group>
              </v-row>
            </template>
          </SearchDataComponent>
        </div>
        <TableComponent
          ref="refViolationList"
          :itemSize="24"
          :headers="headers"
          :contents="contents"
          @update:selectedItems="updateSelectedItems"
          @grid-click-event="onGridClickEvent"
          @grid-dbl-click-event="onGridDblClickEvent"
          scrollKey="Violation"
          :heightOffset="heightOffset"
          :heightPercent="mainGridHeightPercent"
          :footerContents="footerContents"
          rowType="1"
          :customBodyRowStyle="getCustomBodyRowStyle"
        />
      </v-card>
    </v-row>
    <v-row>
      <v-col cols="9">
        <TableComponent
          :itemSize="24"
          :headers="detailHeaders"
          :contents="detailContents"
          scrollKey="Violation-sub"
          :heightOffset="heightOffset"
          :heightPercent="100 - mainGridHeightPercent"
          rowType="1"
        />
      </v-col>
      <v-col cols="3" class="vertical-panel">
        <v-img
          class="detail-carImg"
          :src="imgSrcCar"
          :max-height="`calc(((100vh - ${heightOffset}px) * ${100 - mainGridHeightPercent} / 100) + 29px)`"
          :height="`calc(((100vh - ${heightOffset}px) * ${100 - mainGridHeightPercent} / 100) + 29px)`"
          cover
        />
      </v-col>
    </v-row>
  </v-row>
  <v-dialog v-model="dialog" max-width="1300px" style="z-index: 2000" @keydown.enter.prevent="if (dialogloadingState.isLoading === false) btnSave();">
    <v-card ref="card">
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title @mousedown="onMouseDown" style="cursor: move">하이패스 위반처리</v-toolbar-title>
      </v-toolbar>
      <LoadingComponent :message="dialogloadingState.msg" v-if="dialogloadingState.isLoading" />
      <v-card-text>
        <v-row>
          <v-col cols="7">
            <div class="dialog-subtitle">
              <h3>기본정보</h3>
            </div>
            <GridSystemComponent
              class="v-dialog-content-grid"
              :cols-per-row="3"
              :headers="dialogFirstHeaders"
              :contents="dialogContents"
              :table-th-width="'12%'"
              :table-height="'215px'"
            />
            <div class="dialog-subtitle">
              <h3>기본정보-전영업소</h3>
            </div>
            <GridSystemComponent
              class="v-dialog-content-grid"
              :cols-per-row="3"
              :headers="dialogSecondHeaders"
              :contents="dialogContents"
              :table-th-width="'12%'"
              :table-height="'50px'"
            />
            <div class="dialog-subtitle">
              <h3 style="margin: 0; padding-right: 10px">입력정보</h3>
            </div>
            <div class="condition-container">
              <th class="condition-content">
                <span v-if="dialogContents.BS_EXM_TYPE === '2'">[부산시면제대상]&ensp;</span>
                <span v-if="dialogContents.BFHD_REG_YN === 'Y'">[사전등록대상]&ensp;</span>
                <span v-if="dialogContents.OBU_ATT === 'A1' || dialogContents.OBU_ATT === 'a1'">[전기차(부산)]&ensp;</span>
                <span v-if="dialogContents.LOC_CO_DIV === '0' || dialogContents.LOC_CO_DIV === '1'">
                  [위치조회기관 : {{ dialogContents.LOC_CO_DIV_DP }}]&ensp;
                </span>
                <span
                  v-if="
                    dialogContents.ECARD_TYPE === '01' &&
                    dialogContents.VLTN_CODE !== '37' &&
                    dialogContents.VLTN_CODE !== '38' &&
                    dialogContents.BS_EXM_TYPE_DTL !== '08'
                  "
                >
                  [후불보정대상]&ensp;
                </span>
                <span v-if="dialogContents.VLTN_HAND_DIV === '1' && dialogContents.LPAY_CRCT_YN === 'Y'">
                  [후불보정처리]&ensp;[후불보정금액 : {{ dialogContents.CRCT_PAY_FARE }}]&ensp;
                </span>
              </th>
            </div>
            <InputFormGrid
              class="v-dialog-content-grid"
              :cols-per-row="2"
              :headers="dialogThirdHeaders"
              v-model="dialogContents"
              :table-header-width="'12%'"
              :selectChanged="resetInputData"
              v-model:is-valid="isValidInputForm"
              v-model:input-form="inputForm"
            />
            <div class="condition-container">
              <th class="condition-content">
                <span v-if="dialogContents.BS_EXM_TYPE === '2'">[면제상세 : {{ exemptStatusDp }}]&ensp;</span>
              </th>
            </div>
          </v-col>
          <v-col cols="5" class="vertical-dialog">
            <v-row>
              <div class="dialog-subtitle">
                <h3>차량사진</h3>
              </div>
              <v-img class="dialog-img-car" :src="imgSrcCarPop" />
            </v-row>
            <v-row>
              <div class="dialog-subtitle">
                <h3>차량번호사진</h3>
              </div>
              <v-img class="dialog-img-plate" :src="imgSrcPlate" />
            </v-row>
            <v-row>
              <div class="dialog-subtitle">
                <h3>인식한 차량번호</h3>
              </div>
              <v-text-field v-model="rpaCarNo" width="100%" variant="outlined" :readonly="true" density="compact" hide-details />
            </v-row>
            <v-row v-if="dialogContents.LOC_CO_DIV === '0' || dialogContents.LOC_CO_DIV === '1'">
              <div>
                <div class="v-dialog-content">
                  <h3>위치정보</h3>
                  <GridSystemComponent
                    :table-th-width="'20%'"
                    :cols-per-row="2"
                    :headers="welfareDialogDataHeadersLoc"
                    :contents="welfareDialogDataContents"
                    :row-height="'31px'"
                  />
                </div>
              </div>
            </v-row>
            <v-row v-if="dialogContents.LOC_CO_DIV === '0'">
              <div>
                <div class="v-dialog-content">
                  <InputFormGrid
                    :headers="welfareDialogInputHeaders"
                    v-model="welfareDialogInputContents"
                    v-model:is-valid="isValidWelfare"
                    v-model:input-form="inputFormWelfare"
                    :table-header-width="'30%'"
                    :cols-per-row="1"
                    row-height="31px"
                  />
                </div>
              </div>
            </v-row>
            <DialogButtonNavi @btn-click-event-before="btnBefore" @btn-click-event-after="btnAfter" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="btnChangeCarType">차종변경(대형[3])</v-btn>
        <v-btn variant="flat" color="blue darken-1" @click="exemRequestSearch">주소확인</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="btnCheckMember">고객정보확인</v-btn>
        <v-btn variant="flat" color="blue darken-1" @click="btnCheckPl" :disabled="isCheckPlDisabled">면제PL확인</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="btnSave" :disabled="isSaveDisabled">저장</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="btnClose">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <OZReportDialog v-model:isActive="isActiveViewer" :jsonData="jsonData" />
</template>

<script setup>
import { ref, onActivated, nextTick, watch, toRaw } from 'vue';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import dayjs from 'dayjs';
import { InputRules } from '@/utils/rules';
import {
  request,
  btnHandler,
  createOzDataset,
  comma,
  getWorkNo,
  getFareInfo,
  getFareByCarInfo,
  getCpdFareByCarInfo,
  getDiffFareByCarInfo,
  ImageCategory,
  getImage,
  showMessage,
  getSystemSmallCode,
  getCondition,
} from '@/utils/common.js';

const authStore = useAuthStore();

const loadingState = ref({
  isLoading: false,
  msg: '',
});
const dialogloadingState = ref({
  isLoading: false,
  msg: '',
});

//패딩 189 그리드헤더2개 29*2 조회조건2줄 64+3+3 소계 30 보정 0.99
const heightOffset = 189 + 29 * 2 + 70 + 30 + 0.99;
const mainGridHeightPercent = 70;

const workNoOption = ref([]);
const handTypeOption = ref([]);
const handCarTypeOption = ref([]);
const handDivOption = ref([]);
const vltnCodeOption = ref([]);
const payTypeOption = ref([]);
const exmTypeDtlOption = ref([]);
const bsExmTypeDtlOption = ref([]);
const locDivOption = ref([]);

const originalSearchData = ref(null);
const currentSearchData = ref(null);

const contents = ref([]);
const detailContents = ref([]);

const imgSrcCar = ref();
const imgSrcCarPop = ref();
const imgSrcPlate = ref();
const rpaCarNo = ref('');

const dialog = ref(false);
const dialogContents = ref({});
const dialogHandCarType = ref([]);
const dialogHandType = ref([]);
const dialogPayType = ref([]);
const dialogExmTypeDtl = ref([]);

const currentIndex = ref(0);

const bytelength = ref(0);

const card = ref(null);

const isValidInputForm = ref(true);
const inputForm = ref();

const exemptStatus = ref('');
const exemptStatusDp = ref('');

const isCheckPlDisabled = ref(true);
const isSaveDisabled = ref(false);

const isCpd = ref();

const selectedItems = ref([]);

const isActiveViewer = ref(false);
const jsonData = ref('');

onActivated(async () => {
  //searchHeader selectBox
  handCarTypeOption.value = getSystemSmallCode('171', true, true);
  vltnCodeOption.value = getSystemSmallCode('057', true, true);
  handDivOption.value = getSystemSmallCode('800', true, true);
  handTypeOption.value = getSystemSmallCode('304', true, true);
  payTypeOption.value = getSystemSmallCode('132', true, true);
  bsExmTypeDtlOption.value = getSystemSmallCode('292', true, true);
  exmTypeDtlOption.value = getSystemSmallCode('805', true, true);
  locDivOption.value = getSystemSmallCode('806', true, true);

  //dialog selectBox
  dialogHandType.value = getSystemSmallCode('304', false, true);
  dialogPayType.value = getSystemSmallCode('132', false, true);
  dialogExmTypeDtl.value = getSystemSmallCode('805', false, true);
  dialogHandCarType.value = getSystemSmallCode('171', false, true);

  workNoOption.value = await getWorkNo(searchData.value.START_DATE, searchData.value.END_DATE);

  //fetch fare total table
  await getFareInfo(authStore.getIcCode);

  await nextTick();
});
const searchHeader = ref([
  { label: '조회기간:', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차로번호', key: 'WORK_NO', type: 'select', option: workNoOption },
  { label: '차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px', maxLength: '9' },
  { label: '처리유형', key: 'HAND_TYPE', type: 'select', option: handTypeOption, width: '150px' },
  { label: '지불구분', key: 'VLTN_PAY_TYPE', type: 'select', option: payTypeOption, width: '130px' },
  { label: '처리차종', key: 'HAND_CAR_TYPE', type: 'select', option: handCarTypeOption },
  { label: '처리구분', key: 'VLTN_HAND_DIV', type: 'select', option: handDivOption, width: '100px' },
  { label: '위반코드', key: 'VLTN_CODE', type: 'select', option: vltnCodeOption, width: '170px' },
  { label: '부산시면제상세', key: 'BS_EXM_TYPE_DTL', type: 'select', option: bsExmTypeDtlOption, width: '170px' },
  { label: '위치조회기관', key: 'LOC_CO_DIV', type: 'select', option: locDivOption, width: '170px' },
  { label: '처리면제상세', key: 'EXMT_DTL_TYPE', type: 'select', option: exmTypeDtlOption, width: '170px' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NO: '',
  HAND_CAR_NO: '',
  HAND_TYPE: '',
  HAND_CAR_TYPE: '',
  VLTN_HAND_DIV: '',
  VLTN_CODE: '',
  VLTN_PAY_TYPE: '',
  BS_EXM_TYPE_DTL: '',
  LOC_CO_DIV: '',
  EXMT_DTL_TYPE: '',
});

const radioBatch = [
  { title: '정상처리', value: '0' },
  { title: '미납처리', value: '1' },
  { title: '면제처리', value: '2' },
  { title: '부적격처리', value: '3' },
];
const optionBatch = ref({
  option: '',
});

const getCustomBodyRowStyle = (item) => {
  if (item.VLTN_HAND_DIV === '1') {
    return 'body-row-style-processed';
  }
  return '';
};

const updateSelectedItems = (item) => {
  selectedItems.value = item;
};

const headers = ref([
  { key: 'checkBox', title: '', width: '40' },
  { title: '순번', key: 'ROW_NUMBER', width: '50', customBodyClass: 'violation-css' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '100' },
  { title: '차로번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '발생시각', key: 'OCC_DT_DP', width: '150' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '150' },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '100' },
  { title: '발생카드번호', key: 'CARD_NO_DP', width: '150' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '150' },

  { title: '수납구분', key: 'MAIN_PAY_DIV_DP', width: '150' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_DP', width: '100' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '출금액', key: 'WTHD_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },

  { title: '처리유형', key: 'HAND_TYPE_DP', width: '150' },
  { title: '전자카드종류', key: 'ECARD_TYPE_DP', width: '150' },

  { title: '전자카드속성', key: 'ECARD_ATT_DP', width: '150' },

  { title: '사전등록여부', key: 'BFHD_REG_YN', width: '100' },
  { title: '부산시면제대상', key: 'BS_EXM_TYPE_DP', width: '150' },
  { title: '부산시면제상세', key: 'BS_EXM_TYPE_DTL_DP', width: '150' },
  { title: 'OBU속성', key: 'OBU_ATT_DP', width: '150' },
  { title: '위치조회기관', key: 'LOC_CO_DIV_DP', width: '100' },
  { title: '처리구분', key: 'VLTN_HAND_DIV_DP', width: '100' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '150' },
  { title: '지불구분', key: 'VLTN_PAY_TYPE_DP', width: '150' },
  { title: '처리면제상세', key: 'EXMT_DTL_TYPE_DP', width: '150' },

  { title: '심사통행요금', key: 'OFC_PASS_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '연속통행할인적용', key: 'CPD_DIV_DP', width: '120' },
  { title: '연속통행할인액', key: 'CPD_FARE_DP', width: '120', customBodyCellStyle: 'body-row-style-fare' },
  { title: '차등요금제적용', key: 'DIFF_DIV_DP', width: '120' },
  { title: '차등요금제할인액', key: 'DIFF_FARE_DP', width: '120', customBodyCellStyle: 'body-row-style-fare' },
  { title: '수납할통행요금', key: 'OFC_EXPT_PAY_FARE_DP', width: '120', customBodyCellStyle: 'body-row-style-fare' },
  { title: '총수납한통행요금', key: 'SUM_OFC_PAY_FARE_DP', width: '120', customBodyCellStyle: 'body-row-style-fare' },
  { title: '수납전카드잔액', key: 'PAY_BEF_BALC_DP', width: '120', customBodyCellStyle: 'body-row-style-fare' },
  { title: '후불보정여부', key: 'LPAY_CRCT_YN', width: '100' },
  { title: '후불보정금액', key: 'CRCT_PAY_FARE_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '전자카드차량번호', key: 'ECARD_CAR_NO', width: '150' },
  { title: 'OBU유형', key: 'OBU_TYPE_DP', width: '100' },
  { title: 'OBU차량번호', key: 'OBU_CAR_NO', width: '150' },
  //{ title: '위치조회대상분류여부', key: 'LOC_SCAN_YN', width: '150' },
  { title: '근무구분', key: 'WORK_DIV_DP', width: '150' },
  { title: '수정자', key: 'ADMIN_NM', width: '100' },
  { title: '비고', key: 'NOTE', width: '200' },
]);

const footerContents = ref([
  { title: '총 건수', value: '0', unit: '건', width: '120' },
  { title: '원통행요금', value: '0', unit: '원' },
  { title: '통행요금', value: '0', unit: '원' },
  { title: '출금액', value: '0', unit: '원' },
  { title: '심사통행요금', value: '0', unit: '원' },
  { title: '수납할통행요금', value: '0', unit: '원' },
  { title: '수납한통행요금', value: '0', unit: '원' },
]);

const detailHeaders = ref([
  { title: '수정처리번호', key: 'MDFY_SNO', width: '120' },
  { title: '처리일시', key: 'HAND_DT_DP', width: '150' },
  { title: '비고', key: 'NOTE', width: '200' },
  { title: '처리유형', key: 'HAND_TYPE_DP', width: '150' },
  { title: '지불구분', key: 'VLTN_PAY_TYPE_DP', width: '150' },
  { title: '수납할통행요금', key: 'OFC_EXPT_PAY_FARE_DP', width: '150' },
  { title: '총수납한통행요금', key: 'SUM_OFC_PAY_FARE_DP', width: '150' },
  { title: '연속통행할인적용', key: 'CPD_DIV_DP', width: '150' },
  { title: '차등요금제적용', key: 'DIFF_DIV_DP', width: '150' },
  { title: '위반발생일자', key: 'WORK_DATE_DP', width: '150' },
  { title: '수정자', key: 'ADMIN_NM', width: '150' },
]);

const dialogFirstHeaders = ref([
  { title: '차로번호', key: 'WORK_NO' },
  { title: '일련번호', key: 'HAND_SNO' },
  { title: '처리구분', key: 'VLTN_HAND_DIV_DP' },
  { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: '발생일시', key: 'OCC_DT_DP' },
  { title: '처리일시', key: 'HAND_DT_DP' },
  { title: '위치조회기관', key: 'LOC_CO_DIV_DP' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_DP' },
  { title: '출금전잔액', key: 'PAY_BEF_BALC' },
  { title: '발생카드번호', key: 'CARD_NO_DP' },
  { title: 'OBU번호', key: 'OBU_NO_DP' },
  { title: '통행요금', key: 'PASS_FARE' },
  { title: '차등요금제적용', key: 'ORIGIN_DIFF_DIV_DP' },
  { title: 'OBU상태', key: 'OBU_STAT_DP' },
  { title: '출금액', key: 'WTHD_FARE' },
  { title: '사전등록여부', key: 'BFHD_REG_YN' },
  { title: 'OBU속성', key: 'OBU_ATT_DP' },
  { title: '후불보정금액', key: 'CRCT_PAY_FARE' },
  { title: '부산시면제대상', key: 'BS_EXM_TYPE_DP' },
  { title: 'OBU유형', key: 'OBU_TYPE_DP' },
  { title: '전자카드속성', key: 'ECARD_ATT_DP' },
  { title: '부산시면제상세', key: 'BS_EXM_TYPE_DTL_DP' },
  { title: '수납구분', key: 'MAIN_PAY_DIV_DP' },
  { title: '전자카드종류', key: 'ECARD_TYPE_DP' },
]);

const dialogSecondHeaders = ref([
  { title: '처리시각', key: 'BEIC_HAND_DT_DP' },
  { title: '영업소번호', key: 'BEIC_IC_CODE' },
  { title: '차로번호', key: 'BEIC_WORK_NO' },
  { title: '처리일련번호', key: 'BEIC_HAND_SNO' },
  { title: '처리유형', key: 'BEIC_HAND_TYPE_DP' },
  { title: '차종', key: 'BEIC_CAR_TYPE_DP' },
]);

const dialogThirdHeaders = ref([
  {
    title: '처리차종',
    key: 'HAND_CAR_TYPE',
    option: 'select',
    selectItem: dialogHandCarType,
  },
  {
    title: '처리유형',
    key: 'HAND_TYPE',
    option: 'select',
    selectItem: dialogHandType,
  },
  { title: '처리차량번호', key: 'HAND_CAR_NO' },
  {
    title: '지불구분',
    key: 'VLTN_PAY_TYPE',
    option: 'select',
    selectItem: dialogPayType,
  },
  { title: '수정자', key: 'ADMIN_NM' },
  {
    title: '면제상세',
    key: 'EXMT_DTL_TYPE',
    option: 'select',
    selectItem: dialogExmTypeDtl,
  },
  { title: '고객명', key: 'CUST_NM', rules: InputRules.custNmRule },
  { title: '통행요금', key: 'OFC_PASS_FARE', disabled: true },
  { title: '고객전화번호', key: 'CUST_TEL', rules: InputRules.telNoRule },
  {
    title: '할인적용',
    key: 'CHK_DC',
    option: 'checkbox',
    item: [
      { label: '연속통행할인', key: 'CPD_DIV' },
      { label: '차등요금제', key: 'DIFF_DIV' },
    ],
  },
  { title: '고객주소', key: 'CUST_ADDR', rules: InputRules.noteRule },
  { title: '연속통행할인액', key: 'CPD_FARE', disabled: true },
  { title: '우편번호', key: 'CUST_ZIP_CODE', rules: InputRules.zipCodeRule },
  { title: '차등요금할인액', key: 'DIFF_FARE', disabled: true },
  { title: '승인번호', key: 'APP_NO' },
  { title: '수납할통행요금', key: 'OFC_EXPT_PAY_FARE', option: 'numberInput' },
  { title: '비고', key: 'NOTE', rules: InputRules.noteRule },
  { title: '수납한통행요금', key: 'SUM_OFC_PAY_FARE', option: 'numberInput' },
]);

const welfareDialogDataHeadersLoc = ref([
  { title: '위치조회결과', key: 'LC_INFO_RSLT_DP' },
  { title: 'OBU일치', key: 'MATCH_OBU_NO' },
  { title: '본인인증', key: 'ARS_CRTF_TYPE_DP' },
  { title: '전자카드일치', key: 'MATCH_CARD_NO' },
  { title: '처리일시', key: 'LC_HAND_DT_DP' },
  { title: '등록차량번호', key: 'INST_CAR_NO' },
]);

const welfareDialogDataContents = ref({});

const selectOptReason = ref([
  { value: '00', title: '정상' },
  { value: '01', title: '위치정보불일치' }, // 감면불가
  { value: '02', title: '명의인증불가' }, // 감면불가
  { value: '03', title: '차량번호상이' }, // 감면불가
  { value: '04', title: '단말기번호상이' }, // 감면가능
  { value: '05', title: '복지카드번호상이' }, // 감면불가
  // { value: '06', title: '요금재계산 미처리' }, // 발생하지 않음
  { value: '99', title: '기타' }, // 감면불가
]);

const changeWelfareNewExmtNotRes = (val) => {
  welfareDialogInputContents.value.NEW_EXMT_NOT_RES = val;
};

const isPassedWelfareCorrection = {
  LC_INFO_RSLT: () => {
    if (welfareDialogDataContents.value.LC_INFO_RSLT_NM !== '일치') {
      welfareDialogInputContents.value.NEW_EXMT_NOT_RES = '01';
      return false;
    }
    return true;
  },
  ARS_CRTF_TYPE: () => {
    if (!(welfareDialogDataContents.value.ARS_CRTF_TYPE_DP === '패스' || welfareDialogDataContents.value.ARS_CRTF_TYPE_DP === '성공')) {
      changeWelfareNewExmtNotRes('02');
      return false;
    }
    return true;
  },
  HAND_CAR_NO: () => {
    if (welfareDialogDataContents.value.INST_CAR_NO !== dialogContents.value.HAND_CAR_NO) {
      changeWelfareNewExmtNotRes('03');
      return false;
    }
    return true;
  },
  OBU_NO: () => {
    if (welfareDialogDataContents.value.MATCH_OBU_NO !== '일치') {
      changeWelfareNewExmtNotRes('04');
      return false;
    }
    return true;
  },
  CARD_NO: () => {
    if (welfareDialogDataContents.value.MATCH_CARD_NO !== '일치') {
      changeWelfareNewExmtNotRes('05');
      return false;
    }
    return true;
  },
};

const onChangedDialogWelfareCarNo = () => {
  if (welfareDialogDataContents.value.LC_INFO_RSLT_NM === '강제인증') {
    // 강제인증 심사
    if (!isPassedWelfareCorrection.CARD_NO()) return;
    if (!isPassedWelfareCorrection.HAND_CAR_NO()) return;
    if (!isPassedWelfareCorrection.OBU_NO()) return;
    changeWelfareNewExmtNotRes('00');
  } else {
    // 일반심사
    if (!isPassedWelfareCorrection.ARS_CRTF_TYPE()) return;
    if (!isPassedWelfareCorrection.LC_INFO_RSLT()) return;
    if (!isPassedWelfareCorrection.HAND_CAR_NO()) return;
    if (!isPassedWelfareCorrection.OBU_NO()) return;
    if (!isPassedWelfareCorrection.CARD_NO()) return;
    changeWelfareNewExmtNotRes('00');
  }
};

const welfareDialogInputHeaders = ref([{ title: '감면심사사유', key: 'NEW_EXMT_NOT_RES', option: 'select', selectItem: selectOptReason }]);

const welfareDialogInputContents = ref({});

const isValidWelfare = ref(true);
const inputFormWelfare = ref();

watch([() => searchData.value.START_DATE, () => searchData.value.END_DATE], ([newWorkDateS, newWorkDateE], [oldWorkDateS, oldWorkDateE]) => {
  if (newWorkDateS > newWorkDateE) return;
  if (newWorkDateS !== oldWorkDateS || newWorkDateE !== oldWorkDateE) {
    getWorkNo(newWorkDateS, newWorkDateE).then((workList) => {
      workNoOption.value = workList;
    });
  }
});

const handleSearch = async () => {
  console.log(`output->searchData.value`, searchData.value);

  try {
    loadingState.value.isLoading = true;
    const response = await request('post', 'api/office/getViolationList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    if (response.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'error');
    }

    contents.value = response;
    //이력부분 초기화
    detailContents.value = [];
    imgSrcCar.value = '';

    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));

    selectedItems.value = [];

    //소계
    const totalRows = contents.value.length;
    let totalOriginPassFare = 0;
    let totalPassFare = 0;
    let totalWthdFare = 0;
    let totalOfcPassFare = 0;
    let totalOfcExptPayFare = 0;
    let totalSumOfcPayFare = 0;

    contents.value.forEach((item) => {
      totalOriginPassFare += Number(item.ORIGIN_PASS_FARE) || 0;
      totalPassFare += Number(item.PASS_FARE) || 0;
      totalWthdFare += Number(item.WTHD_FARE) || 0;
      totalOfcPassFare += Number(item.OFC_PASS_FARE) || 0;
      totalOfcExptPayFare += Number(item.OFC_EXPT_PAY_FARE) || 0;
      totalSumOfcPayFare += Number(item.SUM_OFC_PAY_FARE) || 0;
    });

    footerContents.value[0].value = comma(totalRows);
    footerContents.value[1].value = comma(totalOriginPassFare);
    footerContents.value[2].value = comma(totalPassFare);
    footerContents.value[3].value = comma(totalWthdFare);
    footerContents.value[4].value = comma(totalOfcPassFare);
    footerContents.value[5].value = comma(totalOfcExptPayFare);
    footerContents.value[6].value = comma(totalSumOfcPayFare);
  } catch (error) {
    alert(`위반심사 데이터 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value.isLoading = false;
  }
};

const onGridClickEvent = async (item) => {
  try {
    const response = await request('post', 'api/office/getViolationHistList', {
      WORK_DATE: item['WORK_DATE'].replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
      WORK_NO: item['WORK_NO'],
      HAND_SNO: item['HAND_SNO'],
    });
    detailContents.value = response;
    imgSrcCar.value = getImage(ImageCategory.CAR, item);
  } catch (error) {
    alert(`위반심사이력 데이터 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};
const refViolationList = ref();
const onGridDblClickEvent = async (index, item) => {
  console.log('output->onGridDblClickEvent : index', index);

  currentIndex.value = index;

  dialog.value = true;
  dialogloadingState.value = { msg: '단건 심사 조회 중 입니다.', isLoading: true };
  try {
    const response = await request('post', 'api/office/getViolationList', {
      START_DATE: refViolationList.value.getItem(index).WORK_DATE,
      END_DATE: refViolationList.value.getItem(index).WORK_DATE,
      WORK_NO: refViolationList.value.getItem(index).WORK_NO,
      HAND_SNO: refViolationList.value.getItem(index).HAND_SNO,
      IC_CODE: authStore.getIcCode,
    });
    console.log('Single response', response);

    dialogContents.value = {
      ...response[0],
      ...{ CPD_DIV: response[0].CPD_DIV === '1', DIFF_DIV: response[0].DIFF_DIV === '1' },
    };

    imgSrcCarPop.value = getImage(ImageCategory.CAR, refViolationList.value.getItem(index));
    imgSrcPlate.value = getImage(ImageCategory.PLATE, refViolationList.value.getItem(index));

    rpaCarNo.value = response[0].HAND_CAR_NO;

    //면제pl확인 데이터 초기화
    exemptStatus.value = '';
    exemptStatusDp.value = '';
    //연속통행할인 영업소체크 초기화
    isCpd.value = '';
  } catch (error) {
    alert(`단일 위반심사 데이터 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }

  if (refViolationList.value.getItem(index).LOC_CO_DIV === '0' || refViolationList.value.getItem(index).LOC_CO_DIV === '1') {
    try {
      const response = await request('post', 'api/office/getLocationSearchResultDetail', {
        // WORK_DATE: item.WORK_DATE,
        // WORK_NO: item.WORK_NO,
        // HAND_SNO: item.HAND_SNO,
        // LOC_CO_DIV: item.LOC_CO_DIV,
        IC_CODE: authStore.getIcCode,
        LOC_CO_DIV: refViolationList.value.getItem(index).LOC_CO_DIV,
        WORK_DATE: refViolationList.value.getItem(index).WORK_DATE,
        WORK_NO: refViolationList.value.getItem(index).WORK_NO,
        HAND_SNO: refViolationList.value.getItem(index).HAND_SNO,
      });
      welfareDialogDataContents.value = response;
      if (dialogContents.value.VLTN_HAND_DIV === '1') welfareDialogInputContents.value.NEW_EXMT_NOT_RES = response.EXMT_NOT_RES || '00';
      else onChangedDialogWelfareCarNo();
    } catch (error) {
      alert(`단일 위반심사 위치기반데이터 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
    }
  }

  await resetInputData(dialogContents.value.VLTN_HAND_DIV, 'INIT');

  dialogloadingState.value = { msg: '', isLoading: false };
};

const setDisabled = (key, value) => {
  dialogThirdHeaders.value.forEach((header) => {
    if (header.option === 'checkbox') {
      header.item.forEach((item) => {
        if (item.key === key) {
          item.disabled = value;
        }
      });
    } else if (header.key === key) {
      header.disabled = value;
    }
  });
};

const resetInputData = async (value, key) => {
  console.log(`Select box with key: ${key} changed to ${value}`);

  const totalFare = ref(0);

  //미처리데이터 첫 팝업시 기본세팅 key:INIT, value:VLTN_HAND_DIV:0
  if (key === 'INIT' && value === '0') {
    console.warn('미처리 데이터 첫 심사시, 출금액>0 정상, 나머지 미납-없음 세팅');

    // 기본값 세팅
    dialogContents.value.HAND_TYPE = dialogContents.value.WTHD_FARE > 0 ? '90' : '10';
    dialogContents.value.VLTN_PAY_TYPE = '0';
    dialogContents.value.EXMT_DTL_TYPE = '01';
    dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
    dialogContents.value.DIFF_DIV = dialogContents.value.ORIGIN_DIFF_DIV === '1';

    // 사전등록 완납세팅
    if (dialogContents.value.BFHD_REG_YN === 'Y') {
      dialogContents.value.HAND_TYPE = '30';
      dialogContents.value.VLTN_PAY_TYPE = '30';
      dialogContents.value.EXMT_DTL_TYPE = '01';
      dialogContents.value.DIFF_DIV = dialogContents.value.BFHD_REG_YN === 'Y';
    }

    // 면제 세팅
    if (
      dialogContents.value.BS_EXM_TYPE === '2' ||
      ['5', '9'].includes(dialogContents.value.MAIN_PAY_DIV) ||
      ['0', '1'].includes(dialogContents.value.LOC_CO_DIV)
    ) {
      dialogContents.value.HAND_TYPE = '40';
      if (dialogContents.value.BS_EXM_TYPE_DTL === '08') {
        if (dialogContents.value.BFHD_REG_YN === 'Y') {
          dialogContents.value.VLTN_PAY_TYPE = '30';
        } else {
          dialogContents.value.VLTN_PAY_TYPE = '0';
        }
      } else {
        if (dialogContents.value.ECARD_TYPE === '00') dialogContents.value.VLTN_PAY_TYPE = dialogContents.value.WTHD_FARE > 0 ? '50' : '0';
        else if (dialogContents.value.ECARD_TYPE === '01') dialogContents.value.VLTN_PAY_TYPE = '0';
      }

      console.log('!!! ', dialogContents.value.VLTN_PAY_TYPE);
      dialogContents.value.EXMT_DTL_TYPE = '02';
      dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
      dialogContents.value.DIFF_DIV = dialogContents.value.BFHD_REG_YN === 'Y';
    }
  }

  //처리유형 별 하위 로직 세팅
  switch (dialogContents.value.HAND_TYPE) {
    case '10':
      //처리유형-미납
      setDisabled('CPD_DIV', dialogContents.value.MAIN_PAY_DIV === '7');
      setDisabled('DIFF_DIV', dialogContents.value.ORIGIN_DIFF_DIV === '1');
      dialogPayType.value = payTypeOption.value.filter((val) => ['0'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      break;

    case '30':
      //처리유형-완납
      setDisabled('CPD_DIV', dialogContents.value.MAIN_PAY_DIV === '7');
      setDisabled('DIFF_DIV', dialogContents.value.ORIGIN_DIFF_DIV === '1');
      dialogPayType.value = payTypeOption.value.filter((val) => (dialogContents.value.BFHD_REG_YN === 'Y' ? ['30'] : ['0']).includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      break;

    case '40':
      // 처리유형:면제
      setDisabled('CPD_DIV', true);
      setDisabled('DIFF_DIV', true);
      dialogPayType.value = payTypeOption.value.filter((val) => {
        if (dialogContents.value.BS_EXM_TYPE_DTL === '08' && dialogContents.value.BFHD_REG_YN === 'Y') return ['0', '30'].includes(val.value);
        if (dialogContents.value.WTHD_FARE === 0) return val.value === '0';
        if (dialogContents.value.ECARD_TYPE === '00') return ['0', '50'].includes(val.value);
        if (dialogContents.value.ECARD_TYPE === '01') return ['0', '19'].includes(val.value);
      });
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ![''].includes(val.value));
      break;

    case '80':
      //처리유형-부적격
      setDisabled('CPD_DIV', true);
      setDisabled('DIFF_DIV', true);
      dialogPayType.value = payTypeOption.value.filter((val) => ['60', '61', '62'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      break;

    case '90':
      //처리유형-정상
      setDisabled('CPD_DIV', dialogContents.value.MAIN_PAY_DIV === '7');
      setDisabled('DIFF_DIV', dialogContents.value.ORIGIN_DIFF_DIV === '1');
      dialogPayType.value = payTypeOption.value.filter((val) => {
        if (dialogContents.value.ECARD_TYPE === '00') return ['0', '18'].includes(val.value);
        else return ['0', '18', '19'].includes(val.value);
      });
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      break;

    default:
      return;
  }

  //처리유형 변경시 지불구분 대표값 세팅, 할인적용 세팅
  if (key === 'HAND_TYPE') {
    console.log('처리유형 변경 - 대표값[0]세팅');
    dialogContents.value.VLTN_PAY_TYPE = dialogPayType.value[0].value;
    dialogContents.value.EXMT_DTL_TYPE = dialogExmTypeDtl.value[0].value;

    dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
    dialogContents.value.DIFF_DIV = dialogContents.value.ORIGIN_DIFF_DIV === '1' || dialogContents.value.BFHD_REG_YN === 'Y';
    if (value === '80') {
      dialogContents.value.CPD_DIV = false;
      dialogContents.value.DIFF_DIV = false;
    }
  }

  //지불구분 변경시 할인적용 세팅, 0원정상시 할인해제및불가, 사전등록시 차등적용
  if (key === 'VLTN_PAY_TYPE') {
    dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
    dialogContents.value.DIFF_DIV = dialogContents.value.ORIGIN_DIFF_DIV === '1';
    if (value === '18') {
      dialogContents.value.CPD_DIV = false;
      dialogContents.value.DIFF_DIV = false;
      setDisabled('CPD_DIV', true);
      setDisabled('DIFF_DIV', true);
    } else if (value === '30') {
      dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
      dialogContents.value.DIFF_DIV = dialogContents.value.BFHD_REG_YN === 'Y';
    }
  }

  if (!['VLTN_PAY_TYPE', 'EXMT_DTL_TYPE', 'HAND_CAR_TYPE'].includes(key)) {
    //처리유형 면제 시 면제PL확인 후 저장버튼 활성화, 지불구분,면제상세,처리차종 변경시 유지
    isCheckPlDisabled.value = dialogContents.value.HAND_TYPE !== '40';
    isSaveDisabled.value = dialogContents.value.HAND_TYPE === '40';
  }

  //처리차종별 통행요금 세팅
  dialogContents.value.OFC_PASS_FARE = getFareByCarInfo(
    dialogContents.value.HAND_CAR_TYPE,
    dialogContents.value.OCC_DT,
    dialogContents.value.MAIN_PAY_DIV,
  );

  //경차는 연속통행할인 강제 제거
  if (dialogContents.value.HAND_CAR_TYPE === '6') {
    dialogContents.value.CPD_DIV = false;
    console.log('경차 연속통행제거 확인', dialogContents.value.CPD_DIV);
  } else if (dialogContents.value.VLTN_PAY_TYPE === '18' || dialogContents.value.HAND_TYPE === '80') {
    dialogContents.value.CPD_DIV = false;
    console.log('나머지 중 0원정상,부적격 연속통행제거 확인', dialogContents.value.CPD_DIV);
  } else {
    dialogContents.value.CPD_DIV = dialogContents.value.MAIN_PAY_DIV === '7';
    console.log('나머지 연속통행 있으면 타는지 확인', dialogContents.value.CPD_DIV);
  }

  //연속통행 할인액 세팅
  const { cpdFare, isCpdOffice } = getCpdFareByCarInfo(
    dialogContents.value.HAND_CAR_TYPE,
    dialogContents.value.OCC_DT,
    dialogContents.value.BEIC_IC_CODE,
  );
  dialogContents.value.CPD_FARE = dialogContents.value.CPD_DIV === true ? cpdFare : 0;
  isCpd.value = isCpdOffice;
  //차등요금 할인액 세팅
  const diffFare = getDiffFareByCarInfo(dialogContents.value.HAND_CAR_TYPE, dialogContents.value.OCC_DT);
  dialogContents.value.DIFF_FARE = dialogContents.value.DIFF_DIV === true ? diffFare : 0;
  console.log('면제사전등록 DIFF DIV DIFF FARE', dialogContents.value.DIFF_DIV, dialogContents.value.DIFF_FARE);
  //재심사 시 이전처리값 가져오고, 이후 모든상황에서 금액 다시계산
  if (!(key === 'INIT' && value === '1')) {
    //최종통행요금 세팅
    totalFare.value = dialogContents.value.OFC_PASS_FARE - dialogContents.value.CPD_FARE - dialogContents.value.DIFF_FARE;

    const handType = dialogContents.value.HAND_TYPE;
    const payType = dialogContents.value.VLTN_PAY_TYPE;
    const wthdFare = dialogContents.value.WTHD_FARE;
    const vltnCode = dialogContents.value.VLTN_CODE;
    const ecardType = dialogContents.value.ECARD_TYPE;

    //처리유형-지불구분-출금액,선후불에 따른 금액 세팅 초기화
    switch (handType) {
      case '10':
        if (payType === '0') {
          console.warn('미납-없음 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value - wthdFare;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        break;
      case '30':
        if (payType === '1' || payType === '2') {
          console.warn('완납-현금/계좌 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value - wthdFare;
          dialogContents.value.SUM_OFC_PAY_FARE = totalFare.value - wthdFare;
        }
        if (payType === '30') {
          console.warn('완납-사전등록 통행요금');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value - wthdFare;
          dialogContents.value.SUM_OFC_PAY_FARE = totalFare.value - wthdFare;
        }
        break;
      case '40':
        if (payType === '0') {
          console.warn('면제-없음 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        if (payType === '30') {
          console.warn('면제-사전등록 통행요금');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value - wthdFare;
          dialogContents.value.SUM_OFC_PAY_FARE = totalFare.value - wthdFare;
        }
        if (payType === '50') {
          console.warn('면제-환불 처리 : 선불전자-환불');
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        if (payType === '19' && wthdFare > 0) {
          if (key === 'VLTN_PAY_TYPE') {
            if (!confirm('후불전자카드로 과금된 면제차량입니다. 0원 후불보정처리 하시겠습니까?')) {
              onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
              return;
            }
          }
          console.warn('0원으로 후불보정처리');
          dialogContents.value.LPAY_CRCT_YN = 'Y';
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        break;
      case '80':
        if (payType === '60' || payType === '61' || payType === '62') {
          console.warn('부적격 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        if (ecardType === '01' && wthdFare > 0) {
          console.warn('후불전자 과금된 부적격 0원 처리');
          if (key === 'HAND_TYPE') {
            if (!confirm('후불전자카드로 과금된 부적격차량입니다. 0원으로 후불보정처리 하시겠습니까?')) {
              onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
              return;
            }
          }
          dialogContents.value.LPAY_CRCT_YN = 'Y';
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        break;
      case '90':
        if (payType === '0') {
          console.warn('정상-없음 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value - wthdFare;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        if (payType === '18') {
          console.warn('정상-0원정상 처리');
          dialogContents.value.LPAY_CRCT_YN = 'N';
          dialogContents.value.OFC_EXPT_PAY_FARE = 0;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        if (payType === '19') {
          console.warn('정상-후불보정 처리');
          if (key === 'VLTN_PAY_TYPE') {
            if (!confirm('후불보정 대상 차량입니다. 후불보정 처리하시겠습니까?')) {
              onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
              return;
            }
          }
          if (vltnCode === '37' || vltnCode === '38') {
            alert('카드 출금 불가');
            onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
            return;
          }
          dialogContents.value.LPAY_CRCT_YN = 'Y';
          dialogContents.value.OFC_EXPT_PAY_FARE = totalFare.value;
          dialogContents.value.SUM_OFC_PAY_FARE = 0;
        }
        break;
      default:
        break;
    }
  }
};

//저장버튼 심사
const btnSave = async () => {
  if (isSaveDisabled.value === true) {
    alert('면제PL확인 버튼을 먼저 누르세요');
    return;
  }
  const {
    PASS_FARE: passFare,
    WTHD_FARE: wthdFare,
    ECARD_TYPE: ecardType,
    HAND_TYPE: handType,
    VLTN_PAY_TYPE: payType,
    OBU_CAR_TYPE: obuCarType,
    HAND_CAR_TYPE: handCarType,
    OFC_PASS_FARE: ofcPassFare,
    OFC_EXPT_PAY_FARE: ofcExptPayFare,
    SUM_OFC_PAY_FARE: sumOfcPayFare,
    EXMT_DTL_TYPE: exmtDtlType,
  } = dialogContents.value;

  const obuTypeIndex = handCarTypeOption.value.findIndex((type) => type.value === obuCarType);
  const handTypeIndex = handCarTypeOption.value.findIndex((type) => type.value === handCarType);

  if (!isValidInputForm.value) {
    alert(`${inputForm.value.errors[0].errorMessages[0]}`);
    return;
  }

  if (handType === undefined || handType === null || handType === '') {
    alert('처리유형이 비어 있습니다. 새창을 띄워 재심사하세요');
    dialog.value = false;
    return;
  }

  if (dialogContents.value.CPD_DIV === true && isCpd.value === undefined) {
    if (!confirm('이전영업소가 연속통행할인이 적용된 영업소가 아닙니다. 연속통행할인을 적용하시겠습니까?')) {
      return;
    }
  }
  //수납할금액,수납한금액 유효성검사
  if (ofcExptPayFare === null || sumOfcPayFare === null) {
    alert('통행요금을 입력하세요.');
    return;
  }
  switch (handType) {
    case '10':
      if (ofcExptPayFare === 0 || sumOfcPayFare !== 0) {
        alert('통행요금이 올바르지 않습니다.');
        return;
      }
      if (wthdFare > 0 && obuTypeIndex > handTypeIndex) {
        alert('미납 : OBU차종보다 낮은 차종으로 처리할 수 없습니다.');
        return;
      }
      break;

    case '30':
      if (ofcExptPayFare === 0 || sumOfcPayFare === 0) {
        alert('통행요금이 올바르지 않습니다.');
        return;
      } else if (ofcExptPayFare !== sumOfcPayFare) {
        alert('완납처리 시 통행요금과 수납한금액이 동일해야 합니다.');
        return;
      }
      if (wthdFare > 0 && passFare > ofcPassFare) {
        alert('완납 : 입력된 통행요금이 기존 통행요금보다 작을 수 없습니다.');
        return;
      }
      if (payType === '0') {
        alert('완납-없음 처리할 수 없습니다.');
        return;
      }

      break;

    case '40':
      //면제-사전등록의 경우
      if (payType === '30') {
        if (ofcExptPayFare === 0 || sumOfcPayFare === 0) {
          alert('면제-사전등록 : 통행요금이 올바르지 않습니다.');
          return;
        } else if (ofcExptPayFare !== sumOfcPayFare) {
          alert('면제-사전등록처리 : 통행요금과 수납한금액이 동일해야 합니다.');
          return;
        }
        if (wthdFare > 0 && passFare > ofcPassFare) {
          alert('면제-사전등록 : 입력된 통행요금이 기존 통행요금보다 작을 수 없습니다.');
          return;
        }
        if (!['08'].includes(exmtDtlType)) {
          alert('면제-사전등록 처리의 경우 공차택시 만 처리할 수 있습니다.');
          return;
        }
      }
      // 면제 나머지
      else {
        if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
          alert('통행요금이 0원이 아닙니다.');
          return;
        }
      }

      if (exmtDtlType === '08' && (payType === '50' || payType === '19')) {
        alert('공차택시 데이터는 면제-환불,후불보정 처리할 수 없습니다.');
        return;
      }

      if (wthdFare === 0 && (payType === '50' || payType === '19')) {
        alert('출금액이 없는 데이터는 면제-환불,후불보정 처리할 수 없습니다.');
        return;
      }

      if (['01', '02'].includes(exmtDtlType)) {
        alert('면제아님, 조회불능 차량은 면제처리할 수 없습니다.');
        return;
      }

      if (!exemptStatus.value.includes(exmtDtlType)) {
        if (dialogContents.value.BS_EXM_TYPE === '2') {
          if (!confirm('등록된 부산시면제상세와 다릅니다. 저장하시겠습니까?')) return;
        } else {
          if (!confirm('확인된 면제상세와 다릅니다. 저장하시겠습니까?')) return;
        }
      }

      /*
      // 확인된 면제pl외 다른 면제 선택가능하도록 해제
      if (exemptStatus.value.includes('01') && !['06', '07', '98'].includes(exmtDtlType)) {
        alert('부산시면제차량이 아닌경우는 부산(전기), 부산(수소), 기타면제 만 처리할 수 있습니다.');
        return;
      }

      if (exemptStatus.value.includes('98') && !['98'].includes(exmtDtlType)) {
        alert('기타면제 차량의 경우는 기타면제 만 처리할 수 있습니다.');
        return;
      }
*/
      if (exemptStatus.value.includes('17') && ['17'].includes(exmtDtlType)) {
        alert('저공해 처리할 수 없습니다.');
        return;
      }

      if (
        ['04', '05'].some((status) => exemptStatus.value.includes(status)) &&
        !['0', '1'].includes(dialogContents.value.LOC_CO_DIV) &&
        ['04', '05'].includes(exmtDtlType)
      ) {
        alert('위치조회결과가 없어 면제(장애인/유공자)처리할 수 없습니다.');
        return;
      }

      if (dialogContents.value.LOC_CO_DIV === '0') {
        if (!(welfareDialogInputContents.value.NEW_EXMT_NOT_RES === '00' || welfareDialogInputContents.value.NEW_EXMT_NOT_RES === '04')) {
          alert('감면심사사유가 정상, 단말기상이인 경우에만 정상처리할 수 있습니다.');
          return;
        }
      }

      break;

    case '80':
      if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
        alert('통행요금이 0원이 아닙니다.');
        return;
      }

      if (wthdFare > 0 && ecardType === '00') {
        alert('출금액이 있는 선불전자카드 데이터는 부적격 처리할 수 없습니다.');
        return;
      }
      break;

    case '90':
      if (payType === '0' || payType === '18') {
        if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
          alert('통행요금이 0원이 아닙니다.');
          return;
        }
      }
      if (payType === '0' && obuTypeIndex != -1 && obuTypeIndex < handTypeIndex) {
        alert('정상-없음 : OBU차종보다 높은 차종으로 처리할 수 없습니다.');
        return;
      }

      if (wthdFare === 0 && payType === '0') {
        alert('출금액이 없는 데이터는 정상-없음 처리할 수 없습니다.');
        return;
      }
      if (wthdFare > 0 && payType === '18') {
        alert('출금액이 있는 데이터는 정상-0원정상 처리할 수 없습니다.');
        return;
      }

      if (sumOfcPayFare !== 0) {
        alert('통행요금이 0원이 아닙니다.');
        return;
      }

      break;

    default:
      alert('처리유형 이상');
      break;
  }

  console.log('output->btnSave dialogContents.value : ', dialogContents.value);

  const chkDayFin = await request('post', 'api/office/chkDayFin', [
    {
      ...dialogContents.value,
    },
  ]);
  if (!chkDayFin || chkDayFin.CNT !== 0) {
    alert('이미 일마감이 된 날짜이므로 처리가 불가능합니다.');
    return;
  }

  let isSuccess = false;
  dialogloadingState.value = { msg: '단건 심사 처리 중 입니다.', isLoading: true };
  try {
    const response = await request('post', 'api/office/setViolationAudit', {
      ...dialogContents.value,
      ...{
        CRCT_PAY_FARE: dialogContents.value.LPAY_CRCT_YN === 'Y' ? dialogContents.value.OFC_EXPT_PAY_FARE : 0,
        CPD_DIV: dialogContents.value['CPD_DIV'] === true ? '1' : '0',
        DIFF_DIV: dialogContents.value['DIFF_DIV'] === true ? '1' : '0',
        ADMIN_ID: authStore.getWorkerNo,
        REQUEST_ID: (() => {
          if (dialogContents.value.LOC_CO_DIV === '0') {
            return dialogContents.value.EXMT_DTL_TYPE === '98' ? '2' : '3';
          } else if (dialogContents.value.LOC_CO_DIV === '1') {
            const validTypes = ['03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17'];
            return validTypes.includes(dialogContents.value.EXMT_DTL_TYPE) ? '2' : '3';
          }
          return '3'; // 기본값
        })(),
        item: {
          IC_CODE: dialogContents.value.IC_CODE,
          WORK_DATE: dialogContents.value.WORK_DATE,
          WORK_NO: dialogContents.value.WORK_NO,
          HAND_SNO: dialogContents.value.HAND_SNO,
          HAND_CAR_NO: dialogContents.value.HAND_CAR_NO,
          ECARD_TYPE: dialogContents.value.ECARD_TYPE,
          PASS_FARE: dialogContents.value.PASS_FARE,
          PAY_FARE: dialogContents.value.PAY_FARE,
          EXM_NOTE: dialogContents.value.NOTE,
          NEW_EXMT_NOT_RES: welfareDialogInputContents.value.NEW_EXMT_NOT_RES,
          NEW_CAR_NO: dialogContents.value.HAND_CAR_NO,
        },
      },
    });
    console.log('Save response', response);

    //alert(`${response.CODE} : ${response.MESSAGE}`);
    showMessage(`${response.CODE} : ${response.MESSAGE}`);
    if (response.CODE === 'SUCCESS') isSuccess = true;
  } catch (error) {
    alert(`단일 위반심사 처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    dialogloadingState.value = { msg: '', isLoading: false };
    if (isSuccess) btnAfter();
  }
};

const btnChangeCarType = async () => {
  dialogContents.value.HAND_CAR_TYPE = '3';
  resetInputData(dialogContents.value.HAND_CAR_TYPE, 'HAND_CAR_TYPE');
};

//차량번호로 주소확인
const searchExemRequest = ref({
  PACKET_CAR_NO: '',
});

function getBytes(str) {
  let character;
  bytelength.value = 0;

  for (let i = 0; i < str.length; i += 1) {
    character = str.charAt(i);

    if (escape(character).length > 4) bytelength.value += 2;
    else bytelength.value += 1;
  }
  return bytelength.value;
}

const exemPLFocusData = ref([]);

const exemRequestSearch = async () => {
  try {
    searchExemRequest.value.PACKET_CAR_NO = dialogContents.value.HAND_CAR_NO;

    if (searchExemRequest.value.PACKET_CAR_NO === '') {
      alert(`요청할 차량번호를 입력해주세요.`);

      return false;
    }
    // if (getBytes(searchExemRequest.value.PACKET_CAR_NO) > 10) {
    //   alert(`10 Byte까지만 입력해주세요.`);
    //   return false;
    // }
    if (getBytes(searchExemRequest.value.PACKET_CAR_NO) < 8) {
      alert(`차량번호 양식을 맞춰주세요`);
      return false;
    }
    dialogloadingState.value = { msg: '주소확인 중 입니다.', isLoading: true };
    const data = await request('post', 'api/card/requestExemPL', {
      ...searchExemRequest.value,
      //요청전문 'focus,123수1234',
      PACKET_AUTH_CHAR: 'focus',
      //PACKET_SORT: ',',
      PACKET_SORT: String.fromCharCode(0x02),
      PACKET_END_CHAR: '\r\n',
      IC_CODE: authStore.getIcCode,
      DATA: 'focus' + String.fromCharCode(0x02) + Object.values(searchExemRequest.value).join('') + '\r\n',
    });
    exemPLFocusData.value = data;
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    //selectValue.value = {};
    if (typeof data.ERROR_MSG != 'object') {
      alert(data.ERROR_MSG);
    } else {
      const alertmessage = ref('');
      alertmessage.value = '차량번호 = ' + data.ERROR_MSG['차량번호'] + '\r\n' + '주소 = ' + data.ERROR_MSG['주소'];

      alert(alertmessage.value);
    }
  } catch (error) {
    alert(`주소확인 요청 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    dialogloadingState.value = { msg: '', isLoading: false };
  }
};

//면제PL확인
const btnCheckPl = async () => {
  const { OBU_ATT, ECARD_ATT, CARD_NO, OCC_DT, HAND_CAR_NO } = dialogContents.value;

  const validateInputs = () => {
    if (ECARD_ATT >= '20' && ECARD_ATT <= '29') {
      if (!CARD_NO || CARD_NO.length < 12) {
        alert('발생카드번호가 올바르지 않습니다');
        return false;
      }
    } else {
      if (!HAND_CAR_NO || HAND_CAR_NO === '영상없음' || HAND_CAR_NO === '판독불가') {
        alert('처리차량번호가 없습니다');
        return true;
      }

      if (HAND_CAR_NO.length > 0 && HAND_CAR_NO.length < 7) {
        alert('처리차량번호가 올바르지 않습니다');
        return false;
      }
    }
    return true;
  };

  if (!validateInputs()) return;

  const handleExemptStatus = (status, location, showAlert = 'Y', showExmptList = 'N') => {
    const isExempt = status === '98' || (status >= '03' && status <= '18');
    const message = `${location} 면제차량${isExempt ? '입니다.' : '이 아닙니다.'}`;
    if (showAlert === 'Y') alert(message);

    exemptStatus.value = status;
    if (showExmptList === 'N') {
      exemptStatusDp.value = exmTypeDtlOption.value.find((item) => item.value === exemptStatus.value).title;
    }

    dialogContents.value.EXMT_DTL_TYPE = status;
    isSaveDisabled.value = false;
  };

  dialogloadingState.value = { msg: '면제PL확인 중 입니다.', isLoading: true };
  try {
    if (!HAND_CAR_NO || HAND_CAR_NO === '영상없음' || HAND_CAR_NO === '판독불가') {
      handleExemptStatus('98', '기타', 'N');
    } else if (OBU_ATT >= '10' && OBU_ATT <= '70') {
      handleExemptStatus('98', '도로공사');
    } else if (ECARD_ATT >= '20' && ECARD_ATT <= '29') {
      // 전자카드BL체크 로직 숨김
      // const response = await request('post', 'api/office/getEcardBl', { list: [{ CARD_NO }] });
      // response.length === 0 ? handleExemptStatus('98', '도로공사') : handleExemptStatus('01', '도로공사');
      handleExemptStatus('98', '도로공사');
    } else if (OBU_ATT === 'a1' || OBU_ATT === 'A1') {
      handleExemptStatus('06', '부산시');
    } else if (OBU_ATT === 'b1' || OBU_ATT === 'B1') {
      handleExemptStatus('07', '부산시');
    } else if (dialogContents.value.BS_EXM_TYPE === '2' && dialogContents.value.BS_EXM_TYPE_DTL === '08') {
      const response = await request('post', 'api/office/getExemptPl', { list: [{ HAND_CAR_NO }] });
      if (response && Array.isArray(response)) {
        exemptStatusDp.value = response
          .filter((item) => item.EXP_DT > OCC_DT) // 조건을 만족하는 항목 필터링
          .map((item) => item.EXEMPT_STATUS_DP) // EXEMPT_STATUS_DP 값만 추출
          .join(', '); // 문자열로 합치기
      }

      const exemptStatus = response?.[0]?.EXEMPT_STATUS;

      if (exemptStatus) {
        if (exemptStatus === '17') {
          handleExemptStatus('17', '부산시', 'N', 'Y');
          alert('전기 택시의 경우 주소확인 후 면제처리 할 수 있습니다.');
        } else if (!['01', '02'].includes(exemptStatus)) {
          handleExemptStatus(exemptStatus, '부산시', 'Y', 'Y');
        } else {
          handleExemptStatus('01', '부산시', 'Y', 'Y');
        }
      } else {
        const isTaxiPlate =
          /^부산3[1-7]바\d{4}$/.test(HAND_CAR_NO) ||
          /^부산33아\d{4}$/.test(HAND_CAR_NO) ||
          /^부산75바\d{4}$/.test(HAND_CAR_NO) ||
          /^부산34아\d{4}$/.test(HAND_CAR_NO) ||
          /^경남((11)|(1[3-9])|(2[0-9])|(3[01]))바\d{4}$/.test(HAND_CAR_NO);

        handleExemptStatus(isTaxiPlate ? '08' : '01', '부산시', 'Y', 'Y');
      }
    } else {
      const response = await request('post', 'api/office/getExemptPl', { list: [{ HAND_CAR_NO }] });
      if (response && Array.isArray(response)) {
        exemptStatusDp.value = response
          .filter((item) => item.EXP_DT > OCC_DT) // 조건을 만족하는 항목 필터링
          .map((item) => item.EXEMPT_STATUS_DP) // EXEMPT_STATUS_DP 값만 추출
          .join(', '); // 문자열로 합치기
      }

      if (response[0]?.EXEMPT_STATUS) {
        if (response[0].EXP_DT > dialogContents.value.OCC_DT) {
          handleExemptStatus(response[0].EXEMPT_STATUS, '부산시', 'Y', 'Y');
        } else {
          handleExemptStatus('01', '부산시', 'Y', 'Y');
        }
      } else if (
        /^부산3[1-7]바\d{4}$/.test(HAND_CAR_NO) ||
        /^부산33아\d{4}$/.test(HAND_CAR_NO) ||
        /^부산75바\d{4}$/.test(HAND_CAR_NO) ||
        /^부산34아\d{4}$/.test(HAND_CAR_NO) ||
        /^경남((11)|(1[3-9])|(2[0-9])|(3[01]))바\d{4}$/.test(HAND_CAR_NO)
      ) {
        handleExemptStatus('08', '부산시', 'Y', 'Y');
      } else {
        handleExemptStatus('01', '부산시', 'Y', 'Y');
      }
    }
  } catch (error) {
    alert(`면제PL확인 처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    dialogloadingState.value = { msg: '', isLoading: false };
  }
};

const btnCheckMember = async () => {
  const { HAND_CAR_NO } = dialogContents.value;

  let response;
  try {
    response = await request('post', 'api/office/getMemberInfo', { list: [{ HAND_CAR_NO }] });
    console.log('##### ', response);
  } catch (error) {
    alert(`고객 정보 확인 중 에러가 발생했습니다.\n문제가 지속된다면 관리자에게 문의하세요.`);
    return;
  }

  if (!response || response.length === 0) {
    alert('등록된 고객정보가 없습니다.');
    return;
  }

  const message = response.map((item) => `차량번호 : ${item.VHCL_NO}, 면제상세 : ${item.EXMPTN_TYPE_DP}`).join('\n');
  alert(message);
};

const btnClose = async () => {
  dialog.value = false;
  handleSearch();
};

//정상,미납 일괄처리
const btnBatch = async () => {
  if (!['0', '1', '2', '3'].includes(optionBatch.value['option'])) {
    alert('정상처리, 미납처리, 면제처리 중 선택하세요');
    return;
  }
  if (!selectedItems.value || selectedItems.value.length === 0) {
    alert('일괄처리할 데이터를 선택하세요');
    return;
  }

  const uniqueItems = [];

  const uniqueDates = new Set();

  selectedItems.value.forEach((item) => {
    if (!uniqueDates.has(item.WORK_DATE)) {
      uniqueDates.add(item.WORK_DATE);
      uniqueItems.push(item);
    }
  });

  const chkDayFin = await request('post', 'api/office/chkDayFin', uniqueItems);

  if (!chkDayFin || chkDayFin.CNT !== 0) {
    alert('이미 일마감이 된 날짜가 포함되어 처리가 불가능합니다.');
    return;
  }

  const result = [];
  // 정상일괄처리
  if (optionBatch.value['option'] === '0') {
    selectedItems.value
      .filter(
        (item) =>
          item.BS_EXM_TYPE !== '2' &&
          !['0', '1'].includes(item.LOC_CO_DIV) &&
          item.VLTN_CODE !== '02' &&
          item.ORIGIN_CAR_TYPE === item.HAND_CAR_TYPE &&
          item.PASS_FARE === item.WTHD_FARE &&
          item.PASS_FARE > 0,
      )
      .forEach((item) => {
        if (item.HAND_CAR_NO === null) return;
        const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

        result.push({
          ...item,
          HAND_TYPE: '90',
          VLTN_PAY_TYPE: '0',
          EXMT_DTL_TYPE: '01',
          NOTE: '정상일괄처리',
          CPD_DIV: item.MAIN_PAY_DIV === '7' ? '1' : '0',
          CPD_FARE: item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0,
          DIFF_DIV: item.ORIGIN_DIFF_DIV === '1' ? '1' : '0',
          DIFF_FARE: item.ORIGIN_DIFF_DIV === '1' ? item.ORIGIN_DIFF_FARE : 0,
          LPAY_CRCT_YN: 'N',
          CRCT_PAY_FARE: 0,
          OFC_EXPT_PAY_FARE: 0,
          SUM_OFC_PAY_FARE: 0,
          ADMIN_ID: authStore.getWorkerNo,
          OFC_PASS_FARE,
        });
      });
  }

  // 미납일괄처리
  if (optionBatch.value['option'] === '1') {
    selectedItems.value
      .filter(
        (item) =>
          item.BS_EXM_TYPE !== '2' &&
          !['0', '1'].includes(item.LOC_CO_DIV) &&
          item.VLTN_CODE !== '02' &&
          item.WTHD_FARE === 0 &&
          item.BFHD_REG_YN === 'N' &&
          item.PASS_FARE > 0,
      )
      .forEach((item) => {
        if (item.HAND_CAR_NO === null) return;
        const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

        result.push({
          ...item,
          HAND_TYPE: '10',
          VLTN_PAY_TYPE: '0',
          EXMT_DTL_TYPE: '01',
          NOTE: 'VDS1 미납일괄처리',
          CPD_DIV: item.MAIN_PAY_DIV === '7' ? '1' : '0',
          CPD_FARE: item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0,
          DIFF_DIV: item.ORIGIN_DIFF_DIV === '1' ? '1' : '0',
          DIFF_FARE: item.ORIGIN_DIFF_DIV === '1' ? item.ORIGIN_DIFF_FARE : 0,
          LPAY_CRCT_YN: 'N',
          CRCT_PAY_FARE: 0,
          OFC_EXPT_PAY_FARE:
            OFC_PASS_FARE - (item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0) - (item.ORIGIN_DIFF_DIV === '1' ? item.ORIGIN_DIFF_FARE : 0),
          SUM_OFC_PAY_FARE: 0,
          ADMIN_ID: authStore.getWorkerNo,
          OFC_PASS_FARE,
        });
      });
  }

  // 면제일괄처리
  if (optionBatch.value['option'] === '2') {
    selectedItems.value
      .filter(
        (item) =>
          !['04', '05'].includes(item.BS_EXM_TYPE_DTL) &&
          !['0', '1'].includes(item.LOC_CO_DIV) &&
          item.VLTN_CODE !== '02' &&
          item.WTHD_FARE === 0 &&
          item.BFHD_REG_YN === 'N',
      )
      .forEach((item) => {
        const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

        result.push({
          ...item,
          HAND_TYPE: '40',
          VLTN_PAY_TYPE: '0',
          EXMT_DTL_TYPE: item.BS_EXM_TYPE_DTL === '18' ? '18' : '97',
          NOTE: '면제일괄처리',
          CPD_DIV: '0',
          CPD_FARE: 0,
          DIFF_DIV: '0',
          DIFF_FARE: 0,
          LPAY_CRCT_YN: 'N',
          CRCT_PAY_FARE: 0,
          OFC_EXPT_PAY_FARE: 0,
          SUM_OFC_PAY_FARE: 0,
          ADMIN_ID: authStore.getWorkerNo,
          OFC_PASS_FARE,
        });
      });
  }

  // 부적격일괄처리
  if (optionBatch.value['option'] === '3') {
    selectedItems.value.forEach((item) => {
      const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

      result.push({
        ...item,
        HAND_TYPE: '80',
        VLTN_PAY_TYPE: '62',
        EXMT_DTL_TYPE: '01',
        NOTE: '부적격일괄처리',
        CPD_DIV: '0',
        CPD_FARE: 0,
        DIFF_DIV: '0',
        DIFF_FARE: 0,
        LPAY_CRCT_YN: 'N',
        CRCT_PAY_FARE: 0,
        OFC_EXPT_PAY_FARE: 0,
        SUM_OFC_PAY_FARE: 0,
        ADMIN_ID: authStore.getWorkerNo,
        OFC_PASS_FARE,
      });
    });
  }

  console.log('일괄처리 할 result', result);
  if (!result || result.length === 0) {
    alert('일괄처리 조건에 해당하는 데이터가 없습니다.');
    return;
  }

  if (!confirm(`총 ${result.length}건 일괄처리 하시겠습니까?`)) return;

  let response;
  loadingState.value = { msg: '일괄처리 중 입니다.', isLoading: true };
  try {
    response = await request('post', 'api/office/setViolationAuditBatch', result);
  } catch (error) {
    alert(`일괄처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
    alert(`${response.ERROR_MSG}`);
    if (response.ERROR_CODE === '1') await handleSearch();
  }
};

//경차PL자동처리
const btnPlAuto = async () => {
  if (!selectedItems.value || selectedItems.value.length === 0) {
    alert('일괄처리할 데이터를 선택하세요');
    return;
  }

  const uniqueItems = [];

  const uniqueDates = new Set();

  selectedItems.value.forEach((item) => {
    if (!uniqueDates.has(item.WORK_DATE)) {
      uniqueDates.add(item.WORK_DATE);
      uniqueItems.push(item);
    }
  });

  const chkDayFin = await request('post', 'api/office/chkDayFin', uniqueItems);

  if (!chkDayFin || chkDayFin.CNT !== 0) {
    alert('이미 일마감이 된 날짜가 포함되어 처리가 불가능합니다.');
    return;
  }

  const carNoList = selectedItems.value
    .filter(
      (item) =>
        item.BS_EXM_TYPE !== '2' &&
        !['0', '1'].includes(item.LOC_CO_DIV) &&
        item.VLTN_CODE !== '02' &&
        item.OBU_CAR_TYPE === '6' &&
        item.WTHD_FARE !== 0 &&
        item.BFHD_REG_YN === 'N',
    )
    .map((item) => ({ HAND_CAR_NO: item.HAND_CAR_NO }));

  if (!carNoList.length) {
    alert('경차PL자동처리 조건에 해당하는 데이터가 없습니다.');
    return;
  }

  const result = [];

  const chkLcarPl = await request('post', 'api/office/getLcarPl', carNoList);

  if (!chkLcarPl || chkLcarPl.length === 0) {
    alert('경차PL에 등록된 차량번호가 없습니다.');
    return;
  }

  const filteredItems = selectedItems.value.filter((item) => chkLcarPl.some((record) => record.LCAR_NO === item.HAND_CAR_NO));

  filteredItems.forEach((item) => {
    if (item.HAND_CAR_NO === null) return;
    const OFC_PASS_FARE = getFareByCarInfo(item.OBU_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

    result.push({
      ...item,
      HAND_TYPE: '90',
      VLTN_PAY_TYPE: '0',
      EXMT_DTL_TYPE: '01',
      NOTE: '경차PL자동처리',
      HAND_CAR_TYPE: '6',
      CPD_DIV: '0',
      CPD_FARE: 0,
      DIFF_DIV: item.ORIGIN_DIFF_DIV === '1' ? '1' : '0',
      DIFF_FARE: item.ORIGIN_DIFF_DIV === '1' ? item.ORIGIN_DIFF_FARE : 0,
      LPAY_CRCT_YN: 'N',
      CRCT_PAY_FARE: 0,
      OFC_EXPT_PAY_FARE: 0,
      SUM_OFC_PAY_FARE: 0,
      ADMIN_ID: authStore.getWorkerNo,
      OFC_PASS_FARE,
    });
  });
  console.log('pl자동처리할 result', result);

  if (!confirm(`총 ${result.length}건 경차PL자동처리 하시겠습니까?`)) return;

  let response;
  loadingState.value = { msg: '경차PL자동처리 중 입니다.', isLoading: true };
  try {
    response = await request('post', 'api/office/setViolationAuditBatch', result);
  } catch (error) {
    alert(`경차PL자동처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
    alert(`${response.ERROR_MSG}`);
    if (response.ERROR_CODE === '1') await handleSearch();
  }
};

//면제PL자동처리
const btnExmtPlAuto = async () => {
  if (!selectedItems.value || selectedItems.value.length === 0) {
    alert('일괄처리할 데이터를 선택하세요');
    return;
  }

  const uniqueItems = [];

  const uniqueDates = new Set();

  selectedItems.value.forEach((item) => {
    if (!uniqueDates.has(item.WORK_DATE)) {
      uniqueDates.add(item.WORK_DATE);
      uniqueItems.push(item);
    }
  });

  const chkDayFin = await request('post', 'api/office/chkDayFin', uniqueItems);

  if (!chkDayFin || chkDayFin.CNT !== 0) {
    alert('이미 일마감이 된 날짜가 포함되어 처리가 불가능합니다.');
    return;
  }

  // 조건에 따른 데이터 분류
  const obuItems = [];
  const ecardItems = [];
  const ecoItems = [];
  const elecItems = [];
  const hydroItems = [];
  const taxiItems = [];
  const bsExmtItems = [];

  selectedItems.value
    .filter((item) => item.BS_EXM_TYPE === '2' || ['4', '5', '9'].includes(item.MAIN_PAY_DIV))
    .forEach((item) => {
      if (item.OBU_ATT >= '10' && item.OBU_ATT <= '70') {
        obuItems.push(item);
      } else if (item.ECARD_ATT >= '20' && item.ECARD_ATT <= '29') {
        ecardItems.push(item);
      } else if (/^(a|A)1$/.test(item.OBU_ATT)) {
        elecItems.push(item);
      } else if (/^(b|B)1$/.test(item.OBU_ATT)) {
        hydroItems.push(item);
      } else if (item.BS_EXM_TYPE_DTL === '17') {
        ecoItems.push(item);
      } else if (
        /^부산3[1-7]바\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산33아\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산75바\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산34아\d{4}$/.test(item.HAND_CAR_NO) ||
        /^경남((11)|(1[3-9])|(2[0-9])|(3[01]))바\d{4}$/.test(item.HAND_CAR_NO)
      ) {
        taxiItems.push(item);
      } else {
        bsExmtItems.push(item);
      }
    });

  console.log('#### obuItems , ', obuItems);
  console.log('#### ecardItems, ', ecardItems);
  console.log('#### elecItems, ', elecItems);
  console.log('#### hydroItems, ', hydroItems);
  console.log('#### ecoItems, ', ecoItems);
  console.log('#### taxiItems, ', taxiItems);
  console.log('#### bsExmtItems, ', bsExmtItems);

  /*
  //전자카드BL 체크 로직 숨김
  const ecardList = ecardItems.map((item) => ({
    CARD_NO: item.CARD_NO,
  }));

  loadingState.value = { msg: '전자카드BL확인 중 입니다.', isLoading: true };
  let blResponses;
  try {
    if (ecardList.length > 0) {
      blResponses = await request('post', 'api/office/getEcardBl', { list: ecardList });
    }
  } catch (error) {
    alert(`전자카드BL확인 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
  }
  */

  //속도 이슈시 비활성화
  const exemptList = [...bsExmtItems].map((item) => ({
    OCC_DT: item.OCC_DT,
    HAND_CAR_NO: item.HAND_CAR_NO,
  }));

  //loadingState.value = { msg: '면제PL확인 중 입니다.', isLoading: true };
  let exemptResponses;
  try {
    if (exemptList.length > 0) {
      exemptResponses = await request('post', 'api/office/getExemptPl', { list: exemptList });
    }
  } catch (error) {
    alert(`면제PL확인 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    //loadingState.value = { msg: '', isLoading: false };
  }

  // 결과 병합
  const exmtList = selectedItems.value
    .filter(
      (item) =>
        (item.BS_EXM_TYPE === '2' ||
          ['4', '5', '9'].includes(item.MAIN_PAY_DIV) ||
          (item.BS_EXM_TYPE === '2' && /^(a|A|b|B)1$/.test(item.OBU_ATT))) &&
        !taxiItems.includes(item), // taxiItems에 포함된 항목 제외
    )
    .map((item) => {
      const result = {
        OBU_ATT: item.OBU_ATT,
        ECARD_ATT: item.ECARD_ATT,
        CARD_NO: item.CARD_NO,
        OCC_DT: item.OCC_DT,
        HAND_CAR_NO: item.HAND_CAR_NO,
        IC_CODE: item.IC_CODE,
        WORK_DATE: item.WORK_DATE,
        WORK_NO: item.WORK_NO,
        HAND_SNO: item.HAND_SNO,
        BS_EXM_TYPE: item.BS_EXM_TYPE,
        BS_EXM_TYPE_DTL: item.BS_EXM_TYPE_DTL,
        MAIN_PAY_DIV: item.MAIN_PAY_DIV,
        ECARD_TYPE: item.ECARD_TYPE,
        WTHD_FARE: item.WTHD_FARE,
        HAND_CAR_TYPE: item.HAND_CAR_TYPE,
        LOC_SCAN_YN: item.LOC_SCAN_YN,
        LOC_CO_DIV: item.LOC_CO_DIV,
        BFHD_REG_YN: item.BFHD_REG_YN,
        VLTN_CODE: item.VLTN_CODE,
      };

      if (obuItems.includes(item)) {
        result.EXMT_DTL_TYPE = '98';
      } else if (ecardItems.includes(item)) {
        // 전자카드BL체크 로직 숨김
        // const isBlacklisted = blResponses.some((resp) => resp.ECARD_NO === item.CARD_NO);
        // result.EXMT_DTL_TYPE = isBlacklisted ? '01' : '98';
        result.EXMT_DTL_TYPE = '98';
      } else if (elecItems.includes(item)) {
        result.EXMT_DTL_TYPE = '06';
      } else if (hydroItems.includes(item)) {
        result.EXMT_DTL_TYPE = '07';
      } else if (ecoItems.includes(item)) {
        result.EXMT_DTL_TYPE = '17';
      } else if (bsExmtItems.includes(item)) {
        const exemptResponse = exemptResponses.find((resp) => resp.HAND_CAR_NO === item.HAND_CAR_NO && resp.EXP_DT > item.OCC_DT);
        result.EXMT_DTL_TYPE = exemptResponse?.EXEMPT_STATUS || '01';
      }

      return result;
    });

  if (!exmtList.length) {
    alert('면제PL자동처리 조건에 해당하는 데이터가 없습니다.');
    return;
  }
  console.log('#exmtList#', exmtList);

  const result = [];
  exmtList.forEach((item) => {
    if (
      item.HAND_CAR_NO === null ||
      item.HAND_CAR_NO === '영상없음' ||
      item.HAND_CAR_NO === '판독불가' ||
      ['01', '02', '04', '05', '08', '17'].includes(item.EXMT_DTL_TYPE) ||
      ['0', '1'].includes(item.LOC_CO_DIV) ||
      item.VLTN_CODE === '02' ||
      ['04', '05'].includes(item.BS_EXM_TYPE_DTL) ||
      item.BFHD_REG_YN === 'Y'
    )
      return;

    const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);

    let VLTN_PAY_TYPE;
    if (item.EXMT_DTL_TYPE === '08') {
      VLTN_PAY_TYPE = '0';
    } else if (item.WTHD_FARE === 0) {
      VLTN_PAY_TYPE = '0';
    } else if (item.ECARD_TYPE === '00' && item.WTHD_FARE > 0) {
      VLTN_PAY_TYPE = '50';
    } else if (item.ECARD_TYPE === '01' && item.WTHD_FARE > 0) {
      VLTN_PAY_TYPE = '19';
    }

    result.push({
      ...item,
      HAND_TYPE: '40',
      VLTN_PAY_TYPE,
      EXMT_DTL_TYPE: item.EXMT_DTL_TYPE,
      NOTE: '면제PL자동처리',
      HAND_CAR_TYPE: item.HAND_CAR_TYPE,
      CPD_DIV: '0',
      CPD_FARE: 0,
      DIFF_DIV: '0',
      DIFF_FARE: 0,
      LPAY_CRCT_YN: VLTN_PAY_TYPE === '19' ? 'Y' : 'N',
      CRCT_PAY_FARE: 0,
      OFC_EXPT_PAY_FARE: 0,
      SUM_OFC_PAY_FARE: 0,
      ADMIN_ID: authStore.getWorkerNo,
      OFC_PASS_FARE,
    });
  });
  console.log('면제pl자동처리할 result', result);
  if (!result || result.length === 0) {
    alert('면제pl자동처리 조건에 해당하는 데이터가 없습니다.');
    return;
  }

  if (!confirm(`총 ${result.length}건 면제PL자동처리 하시겠습니까?`)) return;

  let response;
  loadingState.value = { msg: '면제PL자동처리 중 입니다.', isLoading: true };
  try {
    response = await request('post', 'api/office/setViolationAuditBatch', result);
  } catch (error) {
    alert(`면제PL자동처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
    alert(`${response.ERROR_MSG}`);
    if (response.ERROR_CODE === '1') await handleSearch();
  }
};

//사전등록자동처리
const btnRegAuto = async () => {
  if (!selectedItems.value || selectedItems.value.length === 0) {
    alert('일괄처리할 데이터를 선택하세요');
    return;
  }

  const uniqueItems = [];

  const uniqueDates = new Set();

  selectedItems.value.forEach((item) => {
    if (!uniqueDates.has(item.WORK_DATE)) {
      uniqueDates.add(item.WORK_DATE);
      uniqueItems.push(item);
    }
  });

  const chkDayFin = await request('post', 'api/office/chkDayFin', uniqueItems);

  if (!chkDayFin || chkDayFin.CNT !== 0) {
    alert('이미 일마감이 된 날짜가 포함되어 처리가 불가능합니다.');
    return;
  }

  const result = [];
  selectedItems.value
    .filter(
      (item) =>
        item.BS_EXM_TYPE !== '2' &&
        !['0', '1'].includes(item.LOC_CO_DIV) &&
        item.VLTN_CODE !== '02' &&
        item.BFHD_REG_YN === 'Y' &&
        item.ORIGIN_CAR_TYPE === item.HAND_CAR_TYPE &&
        item.WTHD_FARE === 0,
    )
    .forEach((item) => {
      if (item.HAND_CAR_NO === null) return;
      const OFC_PASS_FARE = getFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT, item.MAIN_PAY_DIV);
      const DIFF_DIV = '1';
      const DIFF_FARE = getDiffFareByCarInfo(item.HAND_CAR_TYPE, item.OCC_DT);

      const isTaxiPlate =
        /^부산3[1-7]바\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산33아\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산75바\d{4}$/.test(item.HAND_CAR_NO) ||
        /^부산34아\d{4}$/.test(item.HAND_CAR_NO) ||
        /^경남((11)|(1[3-9])|(2[0-9])|(3[01]))바\d{4}$/.test(item.HAND_CAR_NO);
      if (isTaxiPlate) return;

      result.push({
        ...item,
        HAND_TYPE: '30',
        VLTN_PAY_TYPE: '30',
        EXMT_DTL_TYPE: '01',
        NOTE: '사전등록자동처리',
        CPD_DIV: item.MAIN_PAY_DIV === '7' ? '1' : '0',
        CPD_FARE: item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0,
        DIFF_DIV,
        DIFF_FARE,
        LPAY_CRCT_YN: 'N',
        CRCT_PAY_FARE: 0,
        OFC_EXPT_PAY_FARE: OFC_PASS_FARE - (item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0) - DIFF_FARE,
        SUM_OFC_PAY_FARE: OFC_PASS_FARE - (item.MAIN_PAY_DIV === '7' ? item.ORIGIN_CPD_FARE : 0) - DIFF_FARE,
        ADMIN_ID: authStore.getWorkerNo,
        OFC_PASS_FARE,
      });
    });

  console.log('사전등록자동처리 할 result', result);
  if (!result || result.length === 0) {
    alert('사전등록자동처리 조건에 해당하는 데이터가 없습니다.');
    return;
  }

  if (!confirm(`총 ${result.length}건 사전등록자동처리 하시겠습니까?`)) return;

  let response;
  loadingState.value = { msg: '사전등록 자동처리 중 입니다.', isLoading: true };
  try {
    response = await request('post', 'api/office/setViolationAuditBatch', result);
  } catch (error) {
    alert(`사전등록자동처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: true };
    alert(`${response.ERROR_MSG}`);
    if (response.ERROR_CODE === '1') await handleSearch();
  }
};

const handleExcel = () => {
  const headerRow = 1;
  const excelTitle = `위반심사 내역조회`;
  if (contents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }

  currentSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));
  if (JSON.stringify(toRaw(currentSearchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert('조회조건이 변경됐습니다. 조회 후 시도해주세요.');
    return;
  }

  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value
      .filter((header) => header.key !== 'checkBox')
      .map((header) => ({
        title: header.title,
        key: header.key,
        excelWidth: Number(header.width) / 7,
      }));

    const excelContents = selectedItems.value.length === 0 ? contents.value : selectedItems.value;

    excelDownload(headerRow, searchHeader.value, searchData.value, excelHeaders, excelContents, excelTitle, excelTitle, [
      `총 근무건수: ${footerContents.value[0].value} 건  원통행요금: ${footerContents.value[1].value} 원  통행요금: ${footerContents.value[2].value} 원  출금액: ${footerContents.value[3].value} 원 심사통행요금: ${footerContents.value[4].value} 원 수납할통행요금: ${footerContents.value[5].value} 원  수납한통행요금: ${footerContents.value[6].value} 원`,
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

const handlePrint = async () => {
  if (selectedItems.value.length === 0) {
    alert('선택된 데이터가 없습니다.');
    return;
  }

  currentSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));
  if (JSON.stringify(toRaw(currentSearchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert('조회조건이 변경됐습니다. 조회 후 시도해주세요.');
    return;
  }

  const reportParam = ref(getCondition(searchHeader.value, searchData.value));

  loadingState.value = { msg: '보고서 출력 중 입니다.', isLoading: true };
  try {
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: '0202',
    });
    jsonData.value = createOzDataset('/OFFICE/violation.ozr', {
      CSV_DATA: selectedItems.value,
      START_DATE: reportParam.value['START_DATE'],
      END_DATE: reportParam.value['END_DATE'],
      WORK_NO: reportParam.value['WORK_NO'],
      HAND_CAR_NO: reportParam.value['HAND_CAR_NO'],
      HAND_CAR_TYPE: reportParam.value['HAND_CAR_TYPE'],
      HAND_TYPE: reportParam.value['HAND_TYPE'],
      VLTN_CODE: reportParam.value['VLTN_CODE'],
      VLTN_PAY_TYPE: reportParam.value['VLTN_PAY_TYPE'],
      REPORT_NM: '위반심사 내역 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      APPROVAL_IMG_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
    });
    isActiveViewer.value = true;
  } catch (error) {
    alert(`보고서출력 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
  }
};

//이전,다음 버튼
const btnBefore = () => {
  if (currentIndex.value <= 0) {
    alert('첫 데이터 입니다.');
    return;
  }
  currentIndex.value--;
  onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
};
const btnAfter = () => {
  if (currentIndex.value >= contents.value.length - 1) {
    alert('마지막 데이터 입니다.');
    return;
  }
  currentIndex.value++;
  onGridDblClickEvent(currentIndex.value, contents.value[currentIndex.value]);
};

//다이얼로그 이동
const onMouseDown = (event) => {
  const dialogEl = card.value.$el.parentElement; // v-dialog의 부모 요소 가져오기
  const offsetX = event.clientX - dialogEl.getBoundingClientRect().left;
  const offsetY = event.clientY - dialogEl.getBoundingClientRect().top;

  const onMouseMove = (moveEvent) => {
    dialogEl.style.left = `${moveEvent.clientX - offsetX}px`;
    dialogEl.style.top = `${moveEvent.clientY - offsetY}px`;
    dialogEl.style.position = 'absolute'; // 드래그 중일 때 absolute로 고정
  };

  const onMouseUp = () => {
    document.removeEventListener('mousemove', onMouseMove);
    document.removeEventListener('mouseup', onMouseUp);
  };

  document.addEventListener('mousemove', onMouseMove);
  document.addEventListener('mouseup', onMouseUp);
};

const handlePowerOff = () => {
  alert('위반심사 조회 - 종료버튼 클릭');
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
  PowerOff: handlePowerOff,
});
</script>

<style scoped>
/* UI */
.v-card {
  width: 100%;
}

.cardCardClass {
  padding-left: 5px;
  margin-left: 5px;
  border-radius: 0px;
  border-left: 1px solid black;
}

.cardDetailClass {
  padding: 5px;
  margin-top: 5px;
  border-radius: 0px;
  border-top: 1px solid black;
}

.search-container {
  width: 100%;
}

.vertical-panel {
  border-left: 1px solid #a9a9a9;
  border-top: 1px solid #a9a9a9;
}

.vertical-dialog {
}

.plate-input {
  font-size: 20px;
  font-weight: bold;
  text-align: right;
}

.v-text-field:deep(input) {
  height: 70px !important;
  color: black !important;
  font-size: 20px !important;
  font-weight: bold !important;
  min-height: auto;
  text-align-last: center !important;
  padding: 2px;
}

.v-text-field {
  padding: 3px;
}

.dialog-toolbar {
  background-color: #0095ff;
  text-align-last: center;
  color: white;
}

.dialog-subtitle {
  display: flex;
  align-items: center;
  margin-bottom: 1px;
  margin-top: 5px;
  margin-left: 2px;
}

.v-dialog-content-grid {
  width: 98%;
}

.condition-container {
  min-height: 25px;
}
.condition-content {
  color: #0095ff;
  font-size: 15px;
  white-space: nowrap;
  padding: 0;
}

.detail-carImg {
  background-color: lightgray;
  width: 100%;
}
.dialog-img-car {
  background-color: lightgray;
  width: 98%;
  height: 215px;
}
.dialog-img-plate {
  background-color: darkgray;
  width: 98%;
  height: 78px;
}
.v-dialog-content {
  margin: 0px 7px 10px 7px;
}
</style>
