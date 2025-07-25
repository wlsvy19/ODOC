<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="isLoading" />
  <LoadingComponent v-if="isBatchProcessing" message="일괄처리 중입니다." />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData">
      <template v-if="tab === 0" v-slot:header_btn>
        <v-row>
          <v-spacer />
          <v-radio-group inline hide-details v-model="radioModel">
            <v-spacer />
            <v-radio v-for="item in radioOptionImgTypeDtl" :key="item" :label="item.title" :value="item.value" />
            <v-btn :readonly="isBatchProcessing" style="margin: 0px 10px" size="small" text="일괄처리" @click="onClickCorrectionBatch(radioModel)" />
          </v-radio-group>
        </v-row>
      </template>
    </SearchDataComponent>
  </div>
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab>영상심사</v-tab>
    <v-tab>심사결과</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        ref="refImageCorrectionList"
        scroll-key="ImageCorrectionList"
        :headers="mainHeaders"
        :contents="mainContents"
        @update:selected-items="updateSelectedItems"
        @grid-click-event="onClickGridRow"
        @grid-dbl-click-event="onDoubleClickGridRow"
        :height-offset="heightOffset"
        :height-percent="mainGridHeightPercent"
        :custom-body-row-style="setCustomBodyRowStyle"
      />
      <v-row style="border-top: 1px solid #a9a9a9">
        <v-col cols="9">
          <TableComponent
            scroll-key="ImageCorrectionHistory"
            :headers="histHeaders"
            :contents="histContents"
            :height-offset="heightOffset + 1"
            :height-percent="100 - mainGridHeightPercent"
          />
        </v-col>
        <v-col cols="3" style="border-left: 1px solid #a9a9a9">
          <v-img
            v-if="histImgSrc"
            class="detail-carImg"
            :src="histImgSrc"
            :max-height="`calc(((100vh - ${heightOffset}px) * ${100 - mainGridHeightPercent} / 100) + 29px)`"
            :height="`calc(((100vh - ${heightOffset}px) * ${100 - mainGridHeightPercent} / 100) + 29px)`"
            cover
          >
            <template v-slot:placeholder>
              <div class="d-flex align-center justify-center fill-height">
                <v-progress-circular color="grey-lighten-4" indeterminate />
              </div>
            </template>
          </v-img>
        </v-col>
      </v-row>
    </v-window-item>
    <v-window-item>
      <TableComponent
        scroll-key="ImageCorrectionSummary"
        :headers="summaryHeaders"
        :contents="summaryContents"
        :height-offset="heightOffset"
        :custom-body-row-style="setCustomBodyRowStyleSummary"
        row-type="mix"
      />
    </v-window-item>
  </v-window>
  <v-dialog v-model="dialog" max-width="1200px" persistent z-index="1100" @keydown.enter="if (isLoadingDialog === false) onClickDialogButtonSave();">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>영상심사처리</v-toolbar-title>
      </v-toolbar>
      <v-card-text loading="isLoadingDialog">
        <LoadingComponent v-if="isLoadingDialog" message="데이터를 처리중입니다." />
        <v-row>
          <v-col class="v-dialog-content" cols="4">
            <h3>이전 차량사진</h3>
            <v-img class="dialog-img-car" :src="imageSrc.beforeCar" cover>
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular color="grey-lighten-4" indeterminate />
                </div>
              </template>
            </v-img>
            <v-img class="dialog-img-plate" :src="imageSrc.beforePlate" />
          </v-col>
          <v-col class="v-dialog-content" cols="4">
            <h3>차량사진</h3>
            <v-img class="dialog-img-car" :src="imageSrc.currentCar" cover>
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular color="grey-lighten-4" indeterminate />
                </div>
              </template>
            </v-img>
            <v-img class="dialog-img-plate" :src="imageSrc.currentPlate" />
          </v-col>
          <v-col class="v-dialog-content" cols="4">
            <h3>이후 차량사진</h3>
            <v-img class="dialog-img-car" :src="imageSrc.afterCar" cover>
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular color="grey-lighten-4" indeterminate />
                </div>
              </template>
            </v-img>
            <v-img class="dialog-img-plate" :src="imageSrc.afterPlate" />
          </v-col>
        </v-row>
        <v-row>
          <v-col class="v-dialog-content" cols="6">
            <h3>기본정보</h3>
            <v-row>
              <v-col>
                <GridSystemComponent
                  class="v-dialog-content-grid"
                  :table-th-width="'20%'"
                  :cols-per-row="2"
                  :headers="dialogDataHeaders"
                  :contents="currentDataMainContents"
                  row-height="32px"
                />
              </v-col>
            </v-row>
          </v-col>
          <v-col class="v-dialog-content">
            <h3>입력정보</h3>
            <v-row>
              <v-col cols="5">
                <v-label>차량번호(차로)</v-label>
                <v-btn :disabled="isLoadingDialog" class="dialog-button-plate" variant="outlined" @click="onClickDialogButtonPlate('1')">{{
                  currentDataMainContents.CAR_NO
                }}</v-btn>
                <v-label>차량번호(재인식)</v-label>
                <v-btn :disabled="isLoadingDialog" class="dialog-button-plate" variant="outlined" @click="onClickDialogButtonPlate('2')">{{
                  currentDataMainContents.REP_CAR_NO
                }}</v-btn>
              </v-col>
              <v-col cols="7">
                <v-row>
                  <InputFormGrid
                    class="v-dialog-content-grid"
                    v-model="dialogInputDataContents"
                    :headers="dialogInputDataHeaders"
                    :cols-per-row="1"
                    :table-header-width="'35%'"
                    :select-changed="resetInputData"
                    v-model:is-valid="isValidInputForm"
                    v-model:input-form="inputForm"
                  />
                </v-row>
                <v-row>
                  <DialogButtonNavi
                    :is-first="stateButtonNavi.isFirst"
                    :is-last="stateButtonNavi.isLast"
                    @btn-click-event-before="onClickDialogButtonBefore"
                    @btn-click-event-after="onClickDialogButtonAfter"
                  />
                </v-row>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn :disabled="isLoadingDialog" variant="flat" color="blue darken-1" text @click="onClickDialogButtonSave">저장</v-btn>
        <v-btn :disabled="isLoadingDialog" variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <OZReportDialog v-model:is-active="isActiveViewer" :json-data="reportRequestData" />
  <OZReportDialog v-model:is-active="isActiveViewerSummary" :json-data="reportRequestDataSummary" />
</template>

<script setup>
import { ref, onActivated, nextTick, watch, computed } from 'vue';
import { ozAppImageUrl, useAuthStore, useColorStore } from '@/stores/index';
import {
  request,
  btnHandler,
  getWorkNo,
  getImage,
  ImageCategory,
  showMessage,
  createOzDataset,
  getSystemSmallCode,
  getCondition,
} from '@/utils/common.js';
import dayjs from 'dayjs';
import { reactive } from 'vue';
import { excelDownload } from '@/utils/excel';
import { InputRules } from '@/utils/rules';
import { useRoute } from 'vue-router';

//#region Common
const appTitle = `영상심사`;
const isLoading = ref(false);
const isLoadingDialog = ref(false);
const isBatchProcessing = ref(false);
const authStore = useAuthStore();
const colorStore = useColorStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * TAB: 28
 * Grid Header: 29
 * line
 */
const heightOffset = 189 + 36 + 28 + 29 * 2 + 1;
const mainGridHeightPercent = 70;

//#region Select Box Option
/* Select Box Option - search */
const selectOptWorkNo = ref([]);
const selectOptImgTypeDtl = ref([]);
const radioOptionImgTypeDtl = [
  { title: '차로확정', value: '1' },
  { title: '재인식확정', value: '2' },
];

const radioModel = ref('1');

/* Select Box Option - dialog */
const selectOptionDialogHandCarType = ref([]);
const selectOptionDialogImgTypeDtl = ref([]);

onActivated(async () => {
  selectOptWorkNo.value = await getWorkNo(mainSearchData.current.START_DATE, mainSearchData.current.END_DATE);
  selectOptionDialogHandCarType.value = getSystemSmallCode('171', false, true);
  selectOptImgTypeDtl.value = getSystemSmallCode('137', true);
  selectOptionDialogImgTypeDtl.value = getSystemSmallCode('137', false);
  selectOptionDialogImgTypeDtl.value.splice(0, 1);
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});
//#endregion

//#region Search
const searchHeader = ref([]);

const mainSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차로번호', key: 'WORK_NO', type: 'select', option: selectOptWorkNo, width: '130px' },
  { label: '심사구분', key: 'IMG_TYPE_DTL', type: 'select', option: selectOptImgTypeDtl, width: '130px' },
  { label: '처리차량번호 없음', key: 'NON_HAND_CAR_NO', type: 'checkbox' },
];

const summarySearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
];

const searchData = ref();

const mainSearchData = reactive({
  current: {
    IC_CODE: '',
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    WORK_NO: '',
    IMG_TYPE_DTL: '',
    NON_HAND_CAR_NO: false,
  },
  prev: {},
});

const summarySearchData = reactive({
  current: {
    IC_CODE: '',
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
  },
  prev: {},
});

//#region Tab Action
const tab = ref(0);

watch(
  tab,
  () => {
    if (tab.value === 0) {
      searchHeader.value = mainSearchHeader;
      searchData.value = mainSearchData.current;
    } else if (tab.value === 1) {
      searchHeader.value = summarySearchHeader;
      searchData.value = summarySearchData.current;
    }
  },
  { immediate: true },
);
//#endregion

watch(
  [() => mainSearchData.current.START_DATE, () => mainSearchData.current.END_DATE],
  ([newWorkDateS, newWorkDateE], [oldWorkDateS, oldWorkDateE]) => {
    if (newWorkDateS !== oldWorkDateS || newWorkDateE !== oldWorkDateE) {
      getWorkNo(newWorkDateS, newWorkDateE).then((workList) => {
        selectOptWorkNo.value = workList;
      });
    }
  },
);
//#endregion

//#region Main Grid
const mainHeaders = ref([
  { key: 'checkBox', title: '', width: '40' },
  { title: '순번', key: 'ROW_NUMBER', width: '90' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '110' },
  { title: '차로번호', key: 'WORK_NO', width: '90' },
  { title: '일련번호', key: 'HAND_SNO', width: '90' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '180' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '140' },
  { title: '차종(차로)', key: 'CAR_TYPE_DP', width: '90' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '90' },
  { title: '차량번호(차로)', key: 'CAR_NO', width: '130' },
  { title: '차량번호(재인식)', key: 'REP_CAR_NO', width: '130' },
  { title: '심사구분', key: 'IMG_TYPE_DTL_DP', width: '100' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '130' },
]);

const mainContents = ref([]);

const selectedItems = ref([]);
const updateSelectedItems = (items) => {
  selectedItems.value = items;
};

const setCustomBodyRowStyle = (item) => {
  if (item.IMG_TYPE_DTL_DP !== '미처리') {
    return 'body-row-style-processed';
  }
  return '';
};

const onClickCorrectionBatch = async (option) => {
  try {
    isBatchProcessing.value = true;
    // if (selectedItems.value.length > 1000) {
    //   if (!confirm(`1,000건만 처리됩니다. 계속 진행하시겠습니까?`, 'warning')) {
    //     return;
    //   }
    // }
    const response = await request('post', 'api/office/setImageAuditBatch', {
      option: option,
      data: selectedItems.value, //.slice(0, 1000),
    });
    showMessage(`정상 처리되었습니다.`, 'success');
    // showMessage(`일괄처리 되었습니다.(${response.successCount}건)`, 'success');
    await handleSearch();
  } catch (e) {
    showMessage('처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    isBatchProcessing.value = false;
  }
};

const onClickGridRow = (item) => {
  // let idx = mainContents.value.indexOf(item);
  nextTick(async () => {
    try {
      histIsLoading.value = true;
      histImgSrc.value = getImage(ImageCategory.CAR, item);
      histContents.value = await request('post', 'api/office/getImageHistList', {
        IC_CODE: item.IC_CODE,
        WORK_DATE: item.WORK_DATE,
        WORK_NO: item.WORK_NO,
        HAND_SNO: item.HAND_SNO,
      });
    } catch {
      showMessage('error', 'warning');
    } finally {
      histIsLoading.value = false;
    }
  });
};

const onDoubleClickGridRow = (idx) => {
  setDialogItem(idx);
  dialog.value = true;
};
//#endregion

//#region History Grid
const histHeaders = [
  // { title: '근무일자', key: 'WORK_DATE_DP', width: '120' },
  // { title: '근무번호', key: 'WORK_NO', width: '90' },
  // { title: '일련번호', key: 'HAND_SNO', width: '90' },
  { title: '수정번호', key: 'MDFY_SNO', width: '90' },
  { title: '심사구분', key: 'IMG_TYPE_DTL_DP', width: '90' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '90' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '130' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '180' },
  { title: '수정자', key: 'ADMIN_NM', width: '120' },
];

const histContents = ref([]);
const histIsLoading = ref(false);
const histImgSrc = ref('');
//#endregion

//#region Summary Grid
const summaryHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '영업소구분', key: 'IC_DIV_DP', width: '140', customBodyCellStyle: 'table-body-style-left' },
  { title: '소계', key: 'TOT_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: 'RPA 미처리', key: 'READY_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  {
    title: '일치',
    children: [
      { title: '건 수', key: 'MATCH_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'MATCH_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '불일치 소계(심사)',
    children: [
      { title: '건 수', key: 'NOT_MATCH_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'NOT_MATCH_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '차로확정(심사)',
    children: [
      { title: '건 수', key: 'CONF_LANE_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'CONF_LANE_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '재인식확정(심사)',
    children: [
      { title: '건 수', key: 'CONF_REP_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'CONF_REP_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수기확정(심사)',
    children: [
      { title: '건 수', key: 'CONF_MANUAL_CNT', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'CONF_MANUAL_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '영상없음(심사)',
    children: [
      { title: '건 수', key: 'CONF_NO_IMAGE_CNT', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'CONF_NO_IMAGE_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '판독불가(심사)',
    children: [
      { title: '건 수', key: 'NOT_READ_IMAGE_CNT', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'NOT_READ_IMAGE_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '인식실패(심사)',
    children: [
      { title: '건 수', key: 'FAIL_READ_IMAGE_CNT', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'FAIL_READ_IMAGE_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '심사 미처리',
    children: [
      { title: '건 수', key: 'CONF_NOT_CNT_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
      { title: '비율', key: 'CONF_NOT_RATE', width: '80', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const summaryContents = ref([]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.IC_DIV_DP === '합계') {
    return 'table-body-style-summary-1';
  } else if (item.IC_DIV_DP === '소계') {
    return 'table-body-style-summary-2';
  }
  return '';
};
//#endregion

//#region Dialog
const dialog = ref(false);
const isDisabledInputCarNo = ref(true);

const currentDataMainContents = ref({});
const imageSrc = reactive({
  beforeCar: '',
  beforePlate: '',
  currentCar: '',
  currentPlate: '',
  afterCar: '',
  afterPlate: '',
});

const refImageCorrectionList = ref();

const setDialogItem = async (idx) => {
  try {
    const response = await request('post', 'api/office/getImageDetail', {
      ...refImageCorrectionList.value.getItem(idx),
      // ...mainContents.value[idx],
    });
    currentDataMainContents.value = response;
    dialogInputDataContents.value = {
      NEW_IMG_TYPE_DTL:
        currentDataMainContents.value.IMG_TYPE_DTL !== '0'
          ? currentDataMainContents.value.IMG_TYPE_DTL
          : !currentDataMainContents.value.HAND_CAR_NO
          ? '4'
          : '1',
      NEW_CAR_TYPE: currentDataMainContents.value.HAND_CAR_TYPE,
      NEW_CAR_NO:
        currentDataMainContents.value.IMG_TYPE_DTL === '0' ? currentDataMainContents.value.REP_CAR_NO : currentDataMainContents.value.HAND_CAR_NO,
    };
    imageSrc.beforeCar = getImage(ImageCategory.CAR, {
      ...currentDataMainContents.value,
      ...{ HAND_SNO: currentDataMainContents.value.HAND_SNO - 1 },
    });
    imageSrc.beforePlate = getImage(ImageCategory.PLATE, {
      ...currentDataMainContents.value,
      ...{ HAND_SNO: currentDataMainContents.value.HAND_SNO - 1 },
    });
    imageSrc.currentCar = getImage(ImageCategory.CAR, currentDataMainContents.value);
    imageSrc.currentPlate = getImage(ImageCategory.PLATE, currentDataMainContents.value);
    imageSrc.afterCar = getImage(ImageCategory.CAR, {
      ...currentDataMainContents.value,
      ...{ HAND_SNO: currentDataMainContents.value.HAND_SNO + 1 },
    });
    imageSrc.afterPlate = getImage(ImageCategory.PLATE, {
      ...currentDataMainContents.value,
      ...{ HAND_SNO: currentDataMainContents.value.HAND_SNO + 1 },
    });
    updateStateButtonNavi(idx);
    resetInputData();
  } catch {
    showMessage('error', 'warning');
  }
};

const onClickCloseDialog = () => {
  handleSearch();
  dialog.value = false;
};

/* Dialog - Basic */
const dialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: '위반코드(원시)', key: 'ORIGIN_VLTN_CODE_DP' },
  { title: '차로번호', key: 'WORK_NO' },
  { title: '처리차종(차로)', key: 'CAR_TYPE_DP' },
  { title: '일련번호', key: 'HAND_SNO' },
  { title: 'OBU차량번호', key: 'OBU_CAR_NO' },
  { title: '처리일시', key: 'HAND_DT_DP' },
  { title: '카드차량번호', key: 'ECARD_CAR_NO' },
]);

const dialogInputDataHeaders = ref([
  {
    title: '처리차종',
    key: 'NEW_CAR_TYPE',
    option: 'select',
    selectItem: selectOptionDialogHandCarType,
  },
  {
    title: '영상심사구분',
    key: 'NEW_IMG_TYPE_DTL',
    option: 'select',
    selectItem: selectOptionDialogImgTypeDtl,
  },
  {
    title: '차량번호(확정)',
    key: 'NEW_CAR_NO',
    disabled: isDisabledInputCarNo,
    rules: InputRules.carNoRule,
  },
]);

const dialogInputDataContents = ref();

const resetInputData = () => {
  isDisabledInputCarNo.value = true;
  if (dialogInputDataContents.value.NEW_IMG_TYPE_DTL === '1') {
    dialogInputDataContents.value.NEW_CAR_NO = currentDataMainContents.value.CAR_NO;
  } else if (dialogInputDataContents.value.NEW_IMG_TYPE_DTL === '2') {
    dialogInputDataContents.value.NEW_CAR_NO = currentDataMainContents.value.REP_CAR_NO;
  } else {
    isDisabledInputCarNo.value = false;
    if (dialogInputDataContents.value.NEW_IMG_TYPE_DTL === '4') dialogInputDataContents.value.NEW_CAR_NO = '영상없음';
    if (dialogInputDataContents.value.NEW_IMG_TYPE_DTL === '5') dialogInputDataContents.value.NEW_CAR_NO = '판독불가';
    if (dialogInputDataContents.value.NEW_IMG_TYPE_DTL === '6') dialogInputDataContents.value.NEW_CAR_NO = '인식실패';
  }
};

/* Dialog Button Action */
const onClickDialogButtonPlate = (value) => {
  dialogInputDataContents.value.NEW_IMG_TYPE_DTL = value;
  resetInputData();
};

const isValidInputForm = ref(true);
const inputForm = ref();

const onClickDialogButtonSave = async () => {
  try {
    isLoadingDialog.value = true;
    if (
      dialogInputDataContents.value.NEW_CAR_NO === undefined ||
      dialogInputDataContents.value.NEW_CAR_NO === null ||
      dialogInputDataContents.value.NEW_CAR_NO?.replace(/\s/g, '').length === 0
    ) {
      if (!confirm(`확정차량번호가 공란입니다. 처리하시겠습니까?`)) return;
    }
    if (!isValidInputForm.value) {
      showMessage(`${inputForm.value.errors[0].errorMessages[0]}`, 'warning');
      return;
    }
    const response = await request('post', 'api/office/setImageAudit', {
      /* IC_CODE, WORK_DATE, WORK_NO, HAND_SNO, TRNRCP_SNO
       * HAND_CAR_TYPE, IMG_CAR_TYPE, IMG_VLTN_MAKE_YN
       * NEW_IMG_TYPE_DTL, NEW_CAR_TYPE, NEW_CAR_NO
       */
      IC_CODE: currentDataMainContents.value.IC_CODE,
      WORK_DATE: currentDataMainContents.value.WORK_DATE,
      WORK_NO: currentDataMainContents.value.WORK_NO,
      HAND_SNO: currentDataMainContents.value.HAND_SNO,
      TRNRCP_SNO: currentDataMainContents.value.TRNRCP_SNO,
      CAR_TYPE: currentDataMainContents.value.CAR_TYPE,
      HAND_CAR_TYPE: currentDataMainContents.value.HAND_CAR_TYPE,
      ORIGIN_VLTN_CODE: currentDataMainContents.value.ORIGIN_VLTN_CODE,
      IMG_VLTN_MAKE_YN: currentDataMainContents.value.IMG_VLTN_MAKE_YN,
      NEW_IMG_TYPE_DTL: dialogInputDataContents.value.NEW_IMG_TYPE_DTL,
      NEW_CAR_TYPE: dialogInputDataContents.value.NEW_CAR_TYPE,
      NEW_CAR_NO: dialogInputDataContents.value.NEW_CAR_NO?.replace(/\s/g, ''),
      ADMIN_ID: authStore.getWorkerNo,
    });
    if (!!response && response.CODE === '00') {
      showMessage(`정상 처리되었습니다.`, 'success');
      stateButtonNavi.value.isLast === true ? onClickCloseDialog() : onClickDialogButtonAfter();
    }
  } catch (error) {
    console.log(error);
    showMessage('처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    isLoadingDialog.value = false;
  }
};

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
  stateButtonNavi.value.isLast = idx >= mainContents.value.length - 1 ? true : false;
};

const onClickDialogButtonBefore = () => {
  setDialogItem(beforeIndex.value);
};

const onClickDialogButtonAfter = () => {
  setDialogItem(afterIndex.value);
};
//#endregion

//#region Main Button
const handleSearch = async () => {
  try {
    isLoading.value = true;
    if (tab.value === 0) {
      const response = await request('post', 'api/office/getImageList', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value.START_DATE.replaceAll('-', ''),
          END_DATE: searchData.value.END_DATE.replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      mainSearchData.prev = { ...searchData.value };
      mainContents.value = response;
      histContents.value = [];
      currentDataMainContents.value = {};
      dialogInputDataContents.value = {};
    } else if (tab.value === 1) {
      const response = await request('post', 'api/office/getImageCorrectionResultList', {
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.value.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.value.END_DATE.replaceAll('-', ''),
      });
      summarySearchData.prev = { ...searchData.value };
      summaryContents.value = response;
    }
  } catch (error) {
    showMessage(error, 'error');
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = async () => {
  let headerRow = 1;
  let subTitle = '';
  let headers = [];
  let contents = [];
  let prevSearchData = {};
  if (tab.value === 0) {
    subTitle = '심사목록';
    headers = [...mainHeaders.value];
    contents = [...mainContents.value];
    prevSearchData = { ...mainSearchData.prev };
  } else if (tab.value === 1) {
    headerRow = 'mix';
    subTitle = '심사결과';
    headers = [...summaryHeaders.value];
    contents = [...summaryContents.value];
    prevSearchData = { ...summarySearchData.prev };
  }
  if (contents.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, prevSearchData, headers, contents, subTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

//#region Report
const isActiveViewer = ref(false);
const isActiveViewerSummary = ref(false);
const reportRequestData = ref('');
const reportRequestDataSummary = ref('');

const getReportRequestData = () => {
  if (tab.value === 0) {
    reportRequestData.value = createOzDataset('/OFFICE/imageCorrectionList.ozr', {
      CSV_DATA: mainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle + ` 내역`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, mainSearchData.prev),
    });
    isActiveViewer.value = true;
  } else if (tab.value === 1) {
    reportRequestDataSummary.value = createOzDataset('/OFFICE/imageCorrectionResult.ozr', {
      CSV_DATA: summaryContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle + ` - 심사결과`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, summarySearchData.prev),
    });
    isActiveViewerSummary.value = true;
  }
};

const handlePrint = () => {
  let contentsLength = 0;
  if (tab.value === 0) {
    contentsLength = mainContents.value.length;
  } else if (tab.value === 1) {
    contentsLength = summaryContents.value.length;
  }
  if (contentsLength === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  getReportRequestData();
};
//#endregion

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
//#endregion
</script>

<style scoped>
/* UI */
.dialog-button-plate {
  width: 90%;
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin: 2px;
  padding: 5px;
}
.v-dialog-content {
  width: 98%;
  margin-bottom: 10px;
}
.v-dialog-content-grid {
  height: 100%;
  width: 98%;
}
.detail-carImg {
  background-color: lightgray;
  width: 100%;
  border: none;
}
.dialog-img-car {
  background-color: lightgray;
  width: 98%;
  height: 283px;
}
.dialog-img-plate {
  background-color: darkgray;
  width: 98%;
  height: 78px;
}
</style>
