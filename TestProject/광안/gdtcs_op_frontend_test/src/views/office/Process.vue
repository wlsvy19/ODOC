<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="onProcessing.loading.state" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current">
      <template v-slot:header_btn>
        <v-row>
          <v-spacer />
          <v-checkbox v-model="optionSplitImage['isUsed']" label="선택 활성화" variant="outlined" density="compact" hide-details />
          <v-btn
            style="margin-left: 10px"
            color="blue"
            size="small"
            :disabled="!optionSplitImage['isUsed']"
            text="통행내역서"
            @click="onClickButtonPrintCertificate"
          />
          <v-radio-group
            style="margin-left: 10px; background-color: #ebf7ff; border-radius: 5px"
            inline
            hide-details
            max-width="390px"
            v-model="optionSplitImage['option']"
            :disabled="!optionSplitImage['isUsed']"
          >
            <v-label style="margin-left: 10px">출력옵션</v-label>
            <v-radio v-for="item in radioOptImageSplit" :label="item.title" :value="item.value" :key="item" />
            <v-spacer />
            <v-btn size="small" color="blue" :disabled="!optionSplitImage['isUsed']" text="선택영상출력" @click="onClickButtonPrintSelectedImages" />
          </v-radio-group>
        </v-row>
      </template>
    </SearchDataComponent>
  </div>
  <v-row justify="end">
    <div style="width: 1000px; flex-grow: 1">
      <TableComponent
        scroll-key="Process"
        :headers="!optionSplitImage['isUsed'] ? mainHeaders : mainHeadersCheckBox"
        :contents="mainContents"
        :footer-contents="footerContents"
        :height-offset="heightOffset"
        @update:selected-items="updateSelectedItems"
        @grid-click-event="onClickGridRow"
      />
    </div>
    <div style="background-color: #f5f5f5">
      <v-card elevation="4" style="width: 360px; min-height: calc(100vh - 269px); margin: 7px 4px">
        <LoadingComponent v-if="onProcessing.loadingDetail.state" :message="onProcessing.loadingDetail.message" />
        <v-col>
          <div :style="`min-height: 220px; background-color: lightgray;`">
            <v-img v-if="imageSrc" :src="imageSrc" :height="`220px`" cover @click="isDialogOpen = true">
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular color="grey-lighten-4" indeterminate />
                </div>
              </template>
            </v-img>
          </div>
          <div class="detail-container" style="width: 98%">
            <h5>기본정보</h5>
            <GridSystemComponent
              :style="`margin-bottom: 0px`"
              :row-height="'24px'"
              :table-th-width="'24%'"
              :cols-per-row="2"
              :headers="detailHeaders.baseInfo"
              :contents="detailContents"
            />
            <GridSystemComponent
              :style="`margin-top: 0px`"
              :row-height="'24px'"
              :table-th-width="'38%'"
              :cols-per-row="1"
              :headers="detailHeaders.detailInfo"
              :contents="detailContents"
            />
            <h5>감면심사정보</h5>
            <GridSystemComponent
              :style="`margin-bottom: 0px`"
              :row-height="'24px'"
              :table-th-width="'24%'"
              :cols-per-row="2"
              :headers="detailHeaders.exemptInfo"
              :contents="detailContents"
            />
            <GridSystemComponent
              :style="`margin-top: 0px`"
              :row-height="'24px'"
              :table-th-width="'38%'"
              :cols-per-row="1"
              :headers="detailHeaders.exemptInfoExt"
              :contents="detailContents"
            />
            <div v-if="detailContents.LOC_CO_DIV">
              <h5>위치조회기관: {{ detailContentsLoc.LOC_CO_DIV_DP }}</h5>
              <GridSystemComponent
                :style="`margin-bottom: 0px`"
                :row-height="'24px'"
                :table-th-width="'24%'"
                :cols-per-row="2"
                :headers="detailHeaders.locationInfo"
                :contents="detailContentsLoc"
              />
              <GridSystemComponent
                :style="`margin-top: 0px`"
                :row-height="'24px'"
                :table-th-width="'38%'"
                :cols-per-row="1"
                :headers="detailHeaders.locationInfoExtra"
                :contents="detailContentsLoc"
              />
            </div>
          </div>
        </v-col>
      </v-card>
    </div>
  </v-row>
  <OZReportDialog v-model:isActive="isActiveViewerList" :jsonData="reportRequestDataProcessList" />
  <OZReportDialog v-model:isActive="isActiveViewerVideo" :jsonData="reportRequestDataVideo" />
  <v-dialog v-model="isDialogOpen" max-width="1200px" persistent z-index="1100">
    <v-toolbar class="dialog-toolbar">
      <v-spacer />
      <v-btn style="margin-right: 20px" variant="elevated" color="#ffffff" @click="isDialogOpen = false">닫기</v-btn>
    </v-toolbar>
    <div :style="`background-color: lightgray;`">
      <v-img v-if="imageSrc" :src="imageSrc" cover @click="isDialogOpen = false">
        <template v-slot:placeholder>
          <div class="d-flex align-center justify-center fill-height">
            <v-progress-circular color="grey-lighten-4" indeterminate />
          </div>
        </template>
      </v-img>
    </div>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, nextTick, watch, computed, reactive } from 'vue';
import {
  request,
  btnHandler,
  getWorkNo,
  getImage,
  ImageCategory,
  createOzDataset,
  showMessage,
  getSystemSmallCode,
  getCondition,
} from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import OZReportDialog from '@/components/dialog/OZReportDialog.vue';
import { useRoute } from 'vue-router';

//#region Common
const appTitle = `차량처리내역`;
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});
const isDialogOpen = ref(false);
const onProcessing = reactive({
  loading: { state: false },
  loadingDetail: { state: false, message: ' ' },
});

/* Base: 189
 * Search Header(2 row): 70
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 66 + 29 + 30;
//#endregion

//#region Select Box Option
const selectOptVltnCode = ref([]);
const selectOptWorkNo = ref([]);
const selectOptMainPayDiv = ref([]);
const selectOptPrevExmDiv = ref([]);
const radioOptImageSplit = [
  { title: '2분할', value: '2' },
  { title: '4분할', value: '4' },
  { title: '6분할', value: '6' },
  { title: '8분할', value: '8' },
];

onActivated(async () => {
  selectOptVltnCode.value = getSystemSmallCode('057', true, true);
  selectOptMainPayDiv.value = getSystemSmallCode('056', true, true);
  selectOptPrevExmDiv.value = getSystemSmallCode('808', true, false);
  selectOptWorkNo.value = await getWorkNo(searchData.current.START_DATE, searchData.current.END_DATE);
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});
//#endregion

//#region Search
const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '근무번호', key: 'WORK_NO', type: 'select', option: selectOptWorkNo, width: '90px' },
  { label: '위반코드', key: 'VLTN_CODE', type: 'select', option: selectOptVltnCode, width: '130px' },
  { label: '일련번호', key: 'HAND_SNO_S', type: 'inputButton', width: '110px', min: 1, max: 199998, step: 1000 },
  { label: '~', key: 'HAND_SNO_E', type: 'inputButton', width: '110px', min: 1, max: 199999, step: 1000 },
  { label: '카드번호', key: 'CARD_NO', type: 'input', width: '150px', valid: 'digit' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', width: '110px' },
  { label: '대표수납구분', key: 'MAIN_PAY_DIV', type: 'select', option: selectOptMainPayDiv, width: '150px' },
  { label: '면제구분', key: 'PREV_EXM_DIV', type: 'select', option: selectOptPrevExmDiv, width: '130px' },
]);

const searchData = reactive({
  current: {
    IC_CODE: authStore.getIcCode,
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    WORK_NO: '',
    VLTN_CODE: '',
    HAND_SNO_S: 1,
    HAND_SNO_E: 199999,
    CARD_NO: '',
    OBU_NO: '',
    CAR_NO: '',
    OBU_STAT: '',
    MAIN_PAY_DIV: '',
    PREV_EXM_DIV: '',
  },
  prev: {},
});

const optionSplitImage = ref({
  isUsed: false,
  option: '2',
});

/* 근무번호 조회 처리 */
watch([() => searchData.current.START_DATE, () => searchData.current.END_DATE], ([newWorkDateS, newWorkDateE], [oldWorkDateS, oldWorkDateE]) => {
  if (newWorkDateS !== oldWorkDateS || newWorkDateE !== oldWorkDateE) {
    nextTick(async () => {
      selectOptWorkNo.value = await getWorkNo(searchData.current.START_DATE, searchData.current.END_DATE);
    });
  }
});
//#endregion

//#region Main Grid
const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_DP', width: '80' },
  { title: '통행료', key: 'PASS_FARE_DP', width: '70' },
  { title: '출금액', key: 'PAY_FARE_DP', width: '70' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '140' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '160' },
  { title: '대표수납구분', key: 'MAIN_PAY_DIV_DP', width: '110' },
  { title: '면제구분', key: 'PREV_EXM_DIV_DP', width: '110' },
  { title: '심사상태', key: 'CORR_EXM_DIV_DP', width: '110' },
  { title: 'OBU상태', key: 'OBU_STAT_DP', width: '140' },
  { title: 'OBU속성', key: 'OBU_ATT_DP', width: '140' },
  { title: '카드속성', key: 'ECARD_ATT_DP', width: '140' },
]);

const mainHeadersCheckBox = ref([
  { key: 'checkBox', title: '', width: '40' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '처리시각', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_DP', width: '80' },
  { title: '통행료', key: 'PASS_FARE_DP', width: '70' },
  { title: '출금액', key: 'PAY_FARE_DP', width: '70' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '140' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '160' },
  { title: '대표수납구분', key: 'MAIN_PAY_DIV_DP', width: '110' },
  { title: '면제구분', key: 'PREV_EXM_DIV_DP', width: '110' },
  { title: '심사상태', key: 'CORR_EXM_DIV_DP', width: '110' },
  { title: 'OBU상태', key: 'OBU_STAT_DP', width: '140' },
  { title: 'OBU속성', key: 'OBU_ATT_DP', width: '140' },
  { title: '카드속성', key: 'ECARD_ATT_DP', width: '140' },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const footerContents = ref([{ title: '총 건수', value: subTotalCount, unit: '건' }]);
const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const selectedItems = ref([]);
const updateSelectedItems = (items) => {
  selectedItems.value = items;
};
const clearSelectedItems = () => {
  selectedItems.value = [];
};
//#endregion

const detailHeaders = reactive({
  baseInfo: [
    { title: '영업소명', key: 'IC_CODE_DP' },
    { title: '근무일자', key: 'WORK_DATE_DP' },
    { title: '근무번호', key: 'WORK_NO' },
    { title: '일련번호', key: 'HAND_SNO' },
    { title: 'OBU차종', key: 'OBU_CAR_TYPE_DP' },
    { title: 'OBU타입', key: 'OBU_TYPE_DP' },
    { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
    { title: '처리차량번호', key: 'HAND_CAR_NO' },
  ],
  detailInfo: [
    { title: '전자카드번호', key: 'CARD_NO_DP' },
    { title: 'OBU번호', key: 'OBU_NO_DP' },
  ],
  exemptInfo: [
    { title: '면제구분', key: 'PREV_EXM_DIV_DP' },
    { title: '심사상태', key: 'CORR_EXM_DIV_DP' },
  ],
  exemptInfoExt: [
    { title: '심사사유', key: 'EXMT_NOT_RES_DP' },
    { title: '심사비고', key: 'EXM_NOTE' },
  ],
  locationInfo: [
    { title: '위치조회결과', key: 'LC_INFO_RSLT_NM' },
    { title: '등록차량번호', key: 'INST_CAR_NO' },
    { title: '본인인증', key: 'ARS_CRTF_TYPE_DP' },
    { title: '전자카드일치', key: 'MATCH_CARD_NO' },
  ],
  locationInfoExtra: [
    { title: '위치조회일시', key: 'LC_HAND_DT_DP' },
    { title: '위치조회결과상세', key: 'LC_RSLT_NOTE_NM' },
  ],
});
const detailContents = ref({});
const detailContentsLoc = ref({});

const clearDetailContents = () => {
  detailContents.value = {};
  detailContentsLoc.value = {};
  imageSrc.value = '';
};

const handleSearch = async () => {
  onProcessing.loading.state = true;
  try {
    const response = await request('post', 'api/office/getProcessList', {
      START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
      WORK_NO: searchData.current.WORK_NO,
      VLTN_CODE: searchData.current.VLTN_CODE,
      HAND_SNO_S: searchData.current.HAND_SNO_S,
      HAND_SNO_E: searchData.current.HAND_SNO_E,
      CARD_NO: searchData.current.CARD_NO.replaceAll('-', ''),
      OBU_NO: searchData.current.OBU_NO.replaceAll('-', ''),
      CAR_NO: searchData.current.CAR_NO,
      OBU_STAT: searchData.current.OBU_STAT,
      MAIN_PAY_DIV: searchData.current.MAIN_PAY_DIV,
      PREV_EXM_DIV: searchData.current.PREV_EXM_DIV,
    });
    mainContents.value = response;
    searchData.prev = { ...searchData.current };
    setFooterContents();
    clearSelectedItems();
    clearDetailContents();
    if (mainContents.value.length === 0) {
      showMessage('데이터가 없습니다.');
    }
  } catch (error) {
    showMessage('조회 오류가 발생했습니다.', 'warning');
  } finally {
    onProcessing.loading.state = false;
  }
};

//#region Detail Grid
const imageSrc = ref('');

const onClickGridRow = async (item) => {
  detailContents.value = { ...item };
  imageSrc.value = getImage(ImageCategory.CAR, item);
  if (item.LOC_CO_DIV !== null) {
    try {
      onProcessing.loadingDetail.state = true;
      const response = await request('post', 'api/office/getLocationSearchResultDetail', {
        WORK_DATE: item.WORK_DATE,
        WORK_NO: item.WORK_NO,
        HAND_SNO: item.HAND_SNO,
        IC_CODE: authStore.getIcCode,
        LOC_CO_DIV: item.LOC_CO_DIV,
      });
      detailContentsLoc.value = response;
      detailContents.value = { ...detailContents.value, ...response };
    } catch (error) {
      showMessage(`위치데이터 조회 중 오류가 발생했습니다.`, 'warning');
      console.log(`위치조회 중 오류 발생: `, error);
    } finally {
      onProcessing.loadingDetail.state = false;
    }
  }
};
//#endregion

//#region Report
const isActiveViewerVideo = ref(false);
const isActiveViewerList = ref(false);
const reportRequestDataProcessList = ref('');
const reportRequestDataVideo = ref('');

const getSelectedItemsAddedImagePath = () => {
  let data = [...selectedItems.value];
  data.forEach((element) => {
    element['IMG_PATH'] = getImage(ImageCategory.CAR, element);
  });
  return data;
};

const onClickButtonPrintSelectedImages = () => {
  if (selectedItems.value.length === 0) {
    showMessage('선택한 데이터가 없습니다.');
    return;
  }
  reportRequestDataVideo.value = createOzDataset(`/OFFICE/processVideo-In_${optionSplitImage.value.option}.ozr`, {
    CSV_DATA: getSelectedItemsAddedImagePath(),
    IC_NAME: authStore.getIcNm,
    REPORT_NM: '차량처리내역 영상 조회',
    APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
  });
  isActiveViewerVideo.value = true;
};

const onClickButtonPrintCertificate = () => {
  if (selectedItems.value.length === 0) {
    showMessage('선택한 데이터가 없습니다.');
    return;
  }
  reportRequestDataVideo.value = createOzDataset(`/OFFICE/processCertificate.ozr`, {
    CSV_DATA: getSelectedItemsAddedImagePath(),
    IC_NAME: authStore.getIcNm,
    REPORT_NM: '통행내역서',
    APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
  });
  isActiveViewerVideo.value = true;
};

const handlePrint = () => {
  if (mainContents.value.length === 0) {
    showMessage('조회된 데이터가 없습니다.');
  } else {
    reportRequestDataProcessList.value = createOzDataset('/OFFICE/processList.ozr', {
      CSV_DATA: mainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, searchData.prev),
    });
    isActiveViewerList.value = true;
  }
};
//#endregion

const handleExcel = async () => {
  const headerRow = 1;
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped>
.detail-container table {
  margin: 4px;
}
.detail-container h5 {
  color: #666666;
  margin: 9px 10px 0 10px;
}
</style>
