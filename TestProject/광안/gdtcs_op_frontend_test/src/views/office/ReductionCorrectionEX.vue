<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData">
      <template v-slot:header_btn>
        <v-btn size="small" @click="onClickButtonNormalAuto">정상자동처리</v-btn>
      </template>
    </SearchDataComponent>
  </div>
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab>긴급면제카드</v-tab>
    <v-tab>감면단말기</v-tab>
    <v-tab>통합복지(위치)</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        scroll-key="RedectionCorrectionExExmCard"
        :height-offset="heightOffset"
        :headers="exmCardHeaders"
        :contents="exmCardContents"
        :footer-contents="exmCardFooterContents"
        :custom-body-row-style="setCustomBodyRowStyle"
        @grid-dbl-click-event="onDoubleClickGridRowExmCard"
      >
      </TableComponent>
    </v-window-item>
    <v-window-item>
      <TableComponent
        scroll-key="RedectionCorrectionExExmObu"
        :height-offset="heightOffset"
        :headers="exmObuHeaders"
        :contents="exmObuContents"
        :footer-contents="exmObuFooterContents"
        :custom-body-row-style="setCustomBodyRowStyle"
        @grid-dbl-click-event="onDoubleClickGridRowExmOBU"
      />
    </v-window-item>
    <v-window-item>
      <TableComponent
        scroll-key="RedectionCorrectionExWelfare"
        :height-offset="heightOffset"
        :headers="welfareHeaders"
        :contents="welfareContents"
        :footer-contents="welfareFooterContents"
        :custom-body-row-style="setCustomBodyRowStyleWelfare"
        @grid-dbl-click-event="onDoubleClickGridRowWelfare"
      />
    </v-window-item>
  </v-window>

  <v-dialog class="dialog-form" v-model="exmCardDialog" max-width="940px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>긴급면제카드 감면심사</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <div style="width: 500px">
            <div class="v-dialog-content">
              <h3>기본정보</h3>
              <GridSystemComponent
                :table-th-width="'18%'"
                :cols-per-row="2"
                :headers="exmCardDialogDataHeaders"
                :contents="exmCardDialogDataContents"
                :row-height="'31px'"
              />
            </div>
            <div class="v-dialog-content">
              <h3>심사정보</h3>
              <InputFormGrid
                :headers="exmCardDialogInputHeaders"
                v-model="exmCardDialogInputContents"
                v-model:is-valid="isValidExmEcard"
                v-model:input-form="inputFormExmEcard"
                :table-header-width="'18%'"
                :cols-per-row="2"
                row-height="31px"
              />
            </div>
            <div class="v-dialog-content">
              <v-row>
                <v-spacer></v-spacer>
              </v-row>
            </div>
          </div>
          <div>
            <div class="v-dialog-content">
              <h3>차량사진</h3>
              <v-img v-if="exmCardDialogImageSrc" class="dialog-img-car" :src="exmCardDialogImageSrc" cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
              <v-img v-if="exmCardDialogImageSrcPlate" class="dialog-img-plate" :src="exmCardDialogImageSrcPlate">
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
            </div>
          </div>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('1')">정상(감면)</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('2')">심사(감면)</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('3')">부정(미감면)</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog class="dialog-form" v-model="exmObuDialog" max-width="940px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>감면단말기 감면심사</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <div style="width: 500px">
            <div class="v-dialog-content">
              <h3>기본정보</h3>
              <GridSystemComponent
                :table-th-width="'18%'"
                :cols-per-row="2"
                :headers="exmObuDialogDataHeaders"
                :contents="exmObuDialogDataContents"
                :row-height="'31px'"
              />
            </div>
            <div class="v-dialog-content">
              <h3>심사정보</h3>
              <InputFormGrid
                :headers="exmObuDialogInputHeaders"
                v-model="exmObuDialogInputContents"
                v-model:is-valid="isValidExmObu"
                v-model:input-form="inputFormExmObu"
                :table-header-width="'18%'"
                :cols-per-row="2"
                row-height="31px"
              />
            </div>
            <div class="v-dialog-content">
              <v-row>
                <v-spacer></v-spacer>
              </v-row>
            </div>
          </div>
          <div>
            <div class="v-dialog-content">
              <h3>차량사진</h3>
              <v-img v-if="exmObuDialogImageSrc" class="dialog-img-car" :src="exmObuDialogImageSrc" cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
              <v-img v-if="exmObuDialogImageSrcPlate" class="dialog-img-plate" :src="exmObuDialogImageSrcPlate">
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
            </div>
          </div>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('1')">정상(감면)</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('2')">심사(감면)</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('3')">부정(미감면)</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog class="dialog-form" v-model="welfareDialog" max-width="940px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>통합복지 감면심사</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <div style="width: 500px">
            <div class="v-dialog-content">
              <h3>기본정보</h3>
              <GridSystemComponent
                :table-th-width="'18%'"
                :cols-per-row="2"
                :headers="welfareDialogDataHeaders"
                :contents="welfareDialogDataContents"
                :table-height="'186px'"
                :row-height="'36px'"
              />
            </div>
          </div>
          <div>
            <div class="v-dialog-content">
              <h3>차량사진</h3>
              <v-img v-if="welfareDialogImageSrc" class="dialog-img-car" :src="welfareDialogImageSrc" cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
              <v-img v-if="welfareDialogImageSrcPlate" class="dialog-img-plate" :src="welfareDialogImageSrcPlate">
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular color="grey-lighten-4" indeterminate />
                  </div>
                </template>
              </v-img>
            </div>
          </div>
        </v-row>
        <v-row>
          <div style="width: 500px">
            <div class="v-dialog-content">
              <h3>위치정보</h3>
              <GridSystemComponent
                :table-th-width="'18%'"
                :cols-per-row="2"
                :headers="welfareDialogDataHeadersLoc"
                :contents="welfareDialogDataContents"
                :row-height="'31px'"
              />
            </div>
          </div>
          <div style="width: 391px">
            <div class="v-dialog-content">
              <h3>심사정보</h3>
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
        <v-row>
          <div class="v-dialog-content">
            <v-row>
              <v-spacer></v-spacer>
            </v-row>
          </div>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('2')">정상(감면)</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="onClickDialogButtonCorrection('3')">부정(미감면)</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="onClickCloseDialog">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <OZReportDialog v-model:isActive="isActiveViewer" :jsonData="reportRequestData" />
</template>

<script setup>
import { ref, watch, onActivated, nextTick, reactive } from 'vue';
import {
  request,
  btnHandler,
  getImage,
  ImageCategory,
  getLaneNo,
  showMessage,
  createOzDataset,
  getSystemSmallCode,
  getCondition,
} from '@/utils/common';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore, useColorStore } from '@/stores';
import { excelDownload } from '@/utils/excel';
import { InputRules } from '@/utils/rules';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

//#region Common
const appTitle = `감면내역심사`;
const isLoading = ref(false);
const authStore = useAuthStore();
const colorStore = useColorStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search: 36
 * TAB Header: 28
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 28 + 29 + 30;

const setCustomBodyRowStyle = (item) => {
  if (item.EXM_DIV_DP !== '미처리') {
    return 'body-row-style-processed';
  }
  return '';
};

const setCustomBodyRowStyleWelfare = (item) => {
  if (item.EXMT_HAND_TYPE_DP !== '미처리') {
    return 'body-row-style-processed';
  }
  return '';
};

//#endregion

//#region Select Box Option
/* Select Box Option - search */
const selectOptLaneNo = ref([]);
const selectOptExmDiv = ref([]);
const selectOptEcardAtt = ref([]);
const selectOptObuAtt = ref([]);
const selectOptVltnCode = ref([]);
const selectOptLcInfoRslt = ref([]);
const selectOptExmtHandType = ref([]);
const selectOptExmtHandStat = ref([]);

const selectOptMatchDiv = [
  { value: '', title: '전체' },
  { value: '일치', title: '일치' },
  { value: '불일치', title: '불일치' },
];

onActivated(async () => {
  selectOptLaneNo.value = await getLaneNo();
  selectOptExmDiv.value = getSystemSmallCode('138', true);
  selectOptEcardAtt.value = getSystemSmallCode('289', true);
  selectOptObuAtt.value = getSystemSmallCode('281', true);
  selectOptVltnCode.value = getSystemSmallCode('057', true);
  selectOptLcInfoRslt.value = getSystemSmallCode('140', true);
  selectOptExmtHandType.value = getSystemSmallCode('141', true);
  selectOptExmtHandStat.value = getSystemSmallCode('139', true);
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});
//#endregion

//#region Search
const searchHeader = ref([]);
const exmCardSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '심사구분', key: 'EXM_DIV', type: 'select', option: selectOptExmDiv, width: '105px' },
  { label: '면제구분', key: 'ECARD_ATT', type: 'select', option: selectOptEcardAtt, width: '110px' },
  { label: '전자카드차량번호', key: 'ECARD_CAR_NO', type: 'input', width: '110px' },
  { label: '처리차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px' },
  { label: '일치여부', key: 'MATCH_DIV', type: 'select', option: selectOptMatchDiv },
];
const exmObuSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '심사구분', key: 'EXM_DIV', type: 'select', option: selectOptExmDiv, width: '105px' },
  { label: '감면구분', key: 'OBU_ATT', type: 'select', option: selectOptObuAtt, width: '170px' },
  { label: '차량번호(OBU)', key: 'OBU_CAR_NO', type: 'input', width: '110px' },
  { label: 'OBU번호', key: 'OBU_NO', type: 'input', width: '150px', valid: 'digit' },
  { label: '일치여부', key: 'MATCH_DIV', type: 'select', option: selectOptMatchDiv },
];
const welfareSearchHeader = [
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '위치정보결과', key: 'LC_INFO_RSLT', type: 'select', option: selectOptLcInfoRslt },
  { label: '처리유형', key: 'EXMT_HAND_TYPE', type: 'select', option: selectOptExmtHandType },
  { label: '심사구분', key: 'EXMT_HAND_STAT', type: 'select', option: selectOptExmtHandStat },
  { label: '통합복지카드번호', key: 'WCARD_NO', type: 'input', width: '140px', valid: 'digit' },
  { label: '차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px' },
];

const exmCardSearchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    EXM_DIV: '',
    ECARD_ATT: '',
    ECARD_CAR_NO: '',
    HAND_CAR_NO: '',
    MATCH_DIV: '',
  },
  prev: {},
});

const exmObuSearchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    EXM_DIV: '',
    OBU_ATT: '',
    HAND_CAR_NO: '',
    OBU_CAR_NO: '',
    OBU_NO: '',
    MATCH_DIV: '',
  },
  prev: {},
});

const welfareSearchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    LC_INFO_RSLT: '',
    EXMT_HAND_TYPE: '',
    EXMT_HAND_STAT: '',
    WCARD_NO: '',
    HAND_CAR_NO: '',
  },
  prev: {},
});

const searchData = ref();

//#region Tab Action
const tab = ref(0);

watch(
  tab,
  () => {
    if (tab.value === 0) {
      searchHeader.value = exmCardSearchHeader;
      searchData.value = exmCardSearchData.current;
    } else if (tab.value === 1) {
      searchHeader.value = exmObuSearchHeader;
      searchData.value = exmObuSearchData.current;
    } else if (tab.value === 2) {
      searchHeader.value = welfareSearchHeader;
      searchData.value = welfareSearchData.current;
    }
  },
  { immediate: true },
);

//#endregion

//#region Main Grid - 면제카드
const exmCardHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: '면제구분', key: 'ECARD_ATT_DP', width: '120' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: 'OBU 번호', key: 'OBU_NO_DP', width: '160' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '카드차량번호', key: 'ECARD_CAR_NO', width: '110' },
  { title: '일치여부', key: 'MATCH_DIV', width: '70' },
  { title: '심사구분', key: 'EXM_DIV_DP', width: '80' },
  { title: '비고', key: 'NOTE', width: '200', customBodyCellStyle: 'table-body-style-left' },
  { title: '근무자', key: 'ADMIN_DP', width: '110' },
]);

const exmCardContents = ref([]);

const exmCardSubTotalCount = ref('0');
const exmCardSubTotalSumOriginFare = ref('0');

const exmCardFooterContents = ref([
  { title: '총 건수', value: exmCardSubTotalCount, unit: '건' },
  { title: '원통행요금', value: exmCardSubTotalSumOriginFare, unit: '원' },
]);

const setExmCardFooterContents = () => {
  exmCardSubTotalCount.value = exmCardContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  exmCardSubTotalSumOriginFare.value = exmCardContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const onDoubleClickGridRowExmCard = async (idx, item) => {
  exmCardDialogImageSrc.value = getImage(ImageCategory.CAR, item);
  exmCardDialogImageSrcPlate.value = getImage(ImageCategory.PLATE, item);
  exmCardDialogDataContents.value = { ...item };
  exmCardDialogInputContents.value = { ...item };
  exmCardDialog.value = true;
};
//#endregion

//#region Dialog - 면제카드
const exmCardDialog = ref(false);
const exmCardDialogImageSrc = ref('');
const exmCardDialogImageSrcPlate = ref('');

const exmCardDialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '면제카드속성', key: 'ECARD_ATT_DP' },
  { title: '처리번호', key: 'HAND_SNO' },
  { title: '발생일시', key: 'HAND_DT_DP' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP' },
  { title: '전자카드번호', key: 'CARD_NO_DP' },
  { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: 'OBU번호', key: 'OBU_NO_DP' },
]);
const exmCardDialogDataContents = ref({});

const exmCardDialogInputHeaders = ref([
  { title: '카드차량번호', key: 'ECARD_CAR_NO', option: 'label' },
  { title: '심사구분', key: 'EXM_DIV_DP', option: 'label' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', size: 1, rules: InputRules.carNoRule },
  { title: '비고', key: 'NOTE', size: 1, rules: InputRules.noteRule },
]);
const exmCardDialogInputContents = ref({});
//#endregion

//#region Main Grid - 단말기
const exmObuHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: '카드수납구분', key: 'ECARD_PAY_DIV_DP', width: '130' },
  { title: '감면구분', key: 'OBU_ATT_DP', width: '190' },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '160' },
  { title: 'OBU상태', key: 'OBU_STAT_DP', width: '125' },
  { title: 'OBU차량번호', key: 'OBU_CAR_NO', width: '110' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '일치여부', key: 'MATCH_DIV', width: '70' },
  { title: '통복카드번호', key: 'XCARD_NO_DP', width: '160' },
  { title: '심사구분', key: 'EXM_DIV_DP', width: '80' },
  { title: '비고', key: 'NOTE', width: '200', customBodyCellStyle: 'table-body-style-left' },
  { title: '근무자', key: 'ADMIN_DP', width: '110' },
]);

const exmObuContents = ref([]);

const exmObuSubTotalCount = ref('0');
const exmObuSubTotalSumOriginFare = ref('0');

const exmObuFooterContents = ref([
  { title: '총 건수', value: exmObuSubTotalCount, unit: '건' },
  { title: '원통행요금', value: exmObuSubTotalSumOriginFare, unit: '원' },
]);

const setExmObuFooterContents = () => {
  exmObuSubTotalCount.value = exmObuContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  exmObuSubTotalSumOriginFare.value = exmObuContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const onDoubleClickGridRowExmOBU = async (idx, item) => {
  exmObuDialogImageSrc.value = getImage(ImageCategory.CAR, item);
  exmObuDialogImageSrcPlate.value = getImage(ImageCategory.PLATE, item);
  exmObuDialogDataContents.value = { ...item };
  exmObuDialogInputContents.value = { ...item };
  exmObuDialog.value = true;
};
//#endregion

//#region Dialog - 단말기
const exmObuDialog = ref(false);
const exmObuDialogImageSrc = ref('');
const exmObuDialogImageSrcPlate = ref('');

const exmObuDialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: 'OBU속성', key: 'OBU_ATT_DP' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: 'OBU상태', key: 'OBU_STAT_DP' },
  { title: '처리번호', key: 'HAND_SNO' },
  { title: '발생일시', key: 'HAND_DT_DP' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP' },
  { title: '전자카드번호', key: 'CARD_NO_DP' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
  // { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: 'OBU번호', key: 'OBU_NO_DP' },
]);
const exmObuDialogDataContents = ref({});

const exmObuDialogInputHeaders = ref([
  { title: 'OBU차량번호', key: 'OBU_CAR_NO', option: 'label' },
  { title: '심사처리결과', key: 'EXM_DIV_DP', option: 'label' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', rules: InputRules.carNoRule },
  { title: '통복카드번호', key: 'XCARD_NO_DP', option: 'label' },
  { title: '비고', key: 'NOTE', size: 1, rules: InputRules.noteRule },
]);
const exmObuDialogInputContents = ref({});
//#endregion

// 다이얼로그 공통
const onClickCloseDialog = async () => {
  if (tab.value === 0) exmCardDialog.value = false;
  if (tab.value === 1) exmObuDialog.value = false;
  if (tab.value === 2) welfareDialog.value = false;
  await handleSearch();
};

//#region Main Grid - 통합복지
const welfareHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '발생일시', key: 'OCC_DT_DP', width: '150' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '120' },
  { title: '등록차량번호', key: 'INST_CAR_NO', width: '110' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '통합복지카드', key: 'WCARD_NO_DP', width: '160' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '100' },
  { title: '환급금액', key: 'RFND_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
  { title: '위치조회', key: 'LC_INFO_RSLT_DP', width: '70' },
  { title: '본인인증', key: 'ARS_CRTF_TYPE_DP', width: '70' },
  { title: '심사구분', key: 'EXMT_HAND_STAT_DP', width: '80' },
  { title: '처리구분', key: 'EXMT_HAND_TYPE_DP', width: '70' },
  { title: '비고', key: 'NOTE', width: '200', customBodyCellStyle: 'table-body-style-left' },
  { title: '근무자', key: 'ADMIN_DP', width: '110' },
]);

const welfareContents = ref([]);

const welfareSubTotalCount = ref('0');
const welfareSubTotalSumOriginFare = ref('0');
const welfareSubTotalSumRefundFare = ref('0');

const welfareFooterContents = ref([
  { title: '총 건수', value: welfareSubTotalCount, unit: '건' },
  { title: '원통행요금', value: welfareSubTotalSumOriginFare, unit: '원' },
  { title: '환급금액', value: welfareSubTotalSumRefundFare, unit: '원' },
]);

const setWelfareFooterContents = () => {
  welfareSubTotalCount.value = welfareContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  welfareSubTotalSumOriginFare.value = welfareContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  welfareSubTotalSumRefundFare.value = welfareContents.value
    .reduce((acc, item) => {
      return acc + item.RFND_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const onDoubleClickGridRowWelfare = async (idx, item) => {
  welfareDialogImageSrc.value = getImage(ImageCategory.CAR, item);
  welfareDialogImageSrcPlate.value = getImage(ImageCategory.PLATE, item);
  welfareDialogDataContents.value = { ...item };
  welfareDialogInputContents.value = {
    NEW_EXMT_NOT_RES: item.EXMT_NOT_RES,
    EXM_NOTE: item.NOTE,
    NEW_CAR_NO: item.HAND_CAR_NO,
  };
  if (item.EXMT_HAND_TYPE === '1') {
    onChangedDialogWelfareCarNo();
  }
  welfareDialog.value = true;
};
//#endregion

//#region Dialog - 통합복지
const welfareDialog = ref(false);
const welfareDialogImageSrc = ref('');
const welfareDialogImageSrcPlate = ref('');

const welfareDialogDataHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP' },
  { title: '위반코드', key: 'VLTN_CODE_DP' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP' },
  { title: '처리일련번호', key: 'HAND_SNO' },
  { title: '발생일시', key: 'OCC_DT_DP' },
  { title: '전자카드종류', key: 'ECARD_TYPE_DP' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP' },
  { title: '전자카드', key: 'ECARD_NO_DP' },
  { title: '통행요금', key: 'PASS_FARE_DP' },
  { title: '통합복지카드', key: 'WCARD_NO_DP' },
  { title: '수납요금', key: 'PAY_FARE_DP' },
  { title: 'OBU번호', key: 'OBU_NO_DP' },
  { title: '처리차량번호', key: 'HAND_CAR_NO' },
  { title: '등록OBU번호', key: 'DC_OBU_NO_DP' },
  { title: '등록차량번호', key: 'INST_CAR_NO' },
]);

const welfareDialogDataHeadersLoc = ref([
  { title: '조회결과상세', key: 'RSLT_NOTE_DP' },
  { title: 'OBU일치', key: 'MATCH_OBU_NO' },
  { title: '본인인증', key: 'ARS_CRTF_TYPE_DP' },
  { title: '전자카드일치', key: 'MATCH_CARD_NO' },
  { title: '처리일시', key: 'LC_HAND_DT_DP' },
  { title: '심사구분', key: 'EXMT_HAND_STAT_DP' },
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
    if (welfareDialogDataContents.value.LC_INFO_RSLT_DP !== '일치') {
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
    if (welfareDialogDataContents.value.INST_CAR_NO !== welfareDialogInputContents.value.NEW_CAR_NO) {
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
  if (welfareDialogDataContents.value.LC_INFO_RSLT_DP === '강제인증') {
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

const welfareDialogInputHeaders = ref([
  { title: '감면심사사유', key: 'NEW_EXMT_NOT_RES', option: 'select', selectItem: selectOptReason },
  { title: '처리차량번호', key: 'NEW_CAR_NO', changed: onChangedDialogWelfareCarNo, rules: InputRules.carNoRule },
  { title: '비고', key: 'EXM_NOTE', rules: InputRules.noteRule },
]);

const welfareDialogInputContents = ref({});
//#endregion

//#region 심사처리
const isValidExmEcard = ref(true);
const inputFormExmEcard = ref();
const isValidExmObu = ref(true);
const inputFormExmObu = ref();
const isValidWelfare = ref(true);
const inputFormWelfare = ref();

const onClickDialogButtonCorrection = (value) => {
  let targetItem = {};
  if (tab.value === 0) {
    if (!isValidExmEcard.value) {
      showMessage(`${inputFormExmEcard.value.errors[0].errorMessages[0]}`, 'warning');
      return;
    }
    targetItem = {
      IC_CODE: exmCardDialogDataContents.value.IC_CODE,
      WORK_DATE: exmCardDialogDataContents.value.WORK_DATE,
      WORK_NO: exmCardDialogDataContents.value.WORK_NO,
      HAND_SNO: exmCardDialogDataContents.value.HAND_SNO,
      NEW_CAR_NO: exmCardDialogInputContents.value.HAND_CAR_NO,
      EXM_NOTE: exmCardDialogInputContents.value.NOTE,
    };
  } else if (tab.value === 1) {
    if (!isValidExmObu.value) {
      showMessage(`${inputFormExmObu.value.errors[0].errorMessages[0]}`, 'warning');
      return;
    }
    targetItem = {
      IC_CODE: exmObuDialogDataContents.value.IC_CODE,
      WORK_DATE: exmObuDialogDataContents.value.WORK_DATE,
      WORK_NO: exmObuDialogDataContents.value.WORK_NO,
      HAND_SNO: exmObuDialogDataContents.value.HAND_SNO,
      NEW_CAR_NO: exmObuDialogInputContents.value.HAND_CAR_NO,
      EXM_NOTE: exmObuDialogInputContents.value.NOTE,
    };
  } else if (tab.value === 2) {
    if (!isValidWelfare.value) {
      showMessage(`${inputFormWelfare.value.errors[0].errorMessages[0]}`, 'warning');
      return;
    }
    if (value === '2' && !(welfareDialogInputContents.value.NEW_EXMT_NOT_RES == '00' || welfareDialogInputContents.value.NEW_EXMT_NOT_RES == '04')) {
      showMessage(
        `${
          selectOptReason.value[selectOptReason.value.findIndex((item) => welfareDialogInputContents.value.NEW_EXMT_NOT_RES === item.value)].title
        }인 경우 정상처리할 수 없습니다.`,
        'warning',
      );
      return;
    }
    targetItem = {
      IC_CODE: welfareDialogDataContents.value.IC_CODE,
      WORK_DATE: welfareDialogDataContents.value.WORK_DATE,
      WORK_NO: welfareDialogDataContents.value.WORK_NO,
      HAND_SNO: welfareDialogDataContents.value.HAND_SNO,
      ECARD_TYPE: welfareDialogDataContents.value.ECARD_TYPE,
      PASS_FARE: welfareDialogDataContents.value.PASS_FARE,
      PAY_FARE: welfareDialogDataContents.value.PAY_FARE,
      NEW_EXMT_NOT_RES: welfareDialogInputContents.value.NEW_EXMT_NOT_RES,
      NEW_CAR_NO: welfareDialogInputContents.value.NEW_CAR_NO,
      EXM_NOTE: welfareDialogInputContents.value.EXM_NOTE,
    };
  }
  runCorrectionCommon(value, [targetItem], null, authStore.getWorkerNo);
};

const onClickButtonNormalAuto = () => {
  let requestId = '4'; //정상자동처리
  let requestNote = '';
  let normalData = [];
  if (tab.value === 0) {
    requestNote = '긴급면제 정상자동처리';
    normalData = exmCardContents.value.filter((item) => item.EXM_DIV_DP === '미처리'); // 미처리
    normalData = normalData.filter((item) => item.MATCH_DIV === '일치');
  } else if (tab.value === 1) {
    requestNote = '감면단말기 정상자동처리';
    normalData = exmObuContents.value.filter((item) => item.EXM_DIV_DP === '미처리'); // 미처리
    normalData = normalData.filter((item) => item.MATCH_DIV === '일치');
    normalData = normalData.filter((item) => item.XCARD_NO_DP != '-');
  } else if (tab.value === 2) {
    requestNote = '통합복지 정상자동처리';
    normalData = welfareContents.value.filter((item) => item.EXMT_HAND_STAT_DP === '미처리');
    normalData = normalData.filter((item) => item.LC_INFO_RSLT_DP === '일치');
    normalData = normalData.filter((item) => item.ARS_CRTF_TYPE_DP === '성공' || item.ARS_CRTF_TYPE_DP === '패스');
    normalData = normalData.filter((item) => item.INST_CAR_NO === item.HAND_CAR_NO);
    normalData = normalData.filter((item) => item.MATCH_OBU_NO === '일치');
    normalData = normalData.filter((item) => item.MATCH_CARD_NO === '일치');
  }
  if (normalData.length === 0) {
    showMessage(`자동처리할 데이터가 없습니다.`);
    return;
  }
  runCorrectionCommon(requestId, normalData, requestNote);
};

const runCorrectionCommon = async (requestId, targetList, requestNote) => {
  try {
    isLoading.value = true;
    let response = {
      successCount: 0,
      failCount: 0,
    };
    if (tab.value === 2) {
      for (let i = 0; i < Math.floor(targetList.length / 100) + 1; i++) {
        let _response = await request('post', 'api/office/setReductionExCorrectionWelfare', {
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
    } else {
      response = await request('post', 'api/office/setReductionExCorrection', {
        requestId: requestId,
        requestNote: requestNote,
        adminId: authStore.getWorkerNo,
        data: targetList,
      });
    }
    if (response.failCount === 0) {
      showMessage(`정상 처리되었습니다.(${response.successCount}건)`, 'success');
    } else {
      showMessage(`일마감된 항목이 실패 처리되었습니다.(${response.failCount}/${targetList.length}건)`, 'error');
    }
    onClickCloseDialog();
    await handleSearch();
  } catch (error) {
    showMessage('심사처리 중 오류가 발생했습니다.', 'warning');
  } finally {
    isLoading.value = false;
  }
};
//#endregion

//#region Main Button Action
const handleSearch = async () => {
  if (tab.value === 0) {
    isLoading.value = true;
    try {
      const response = await request('post', 'api/office/getReductionExEcardList', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          ECARD_CAR_NO: searchData.value['ECARD_CAR_NO'].replaceAll('-', ''),
        },
      });
      exmCardSearchData.prev = searchData.value;
      exmCardContents.value = response;
      setExmCardFooterContents();
      exmCardDialogDataContents.value = {};
      exmCardDialogInputContents.value = {};
    } catch (error) {
      showMessage('error', 'warning');
    } finally {
      isLoading.value = false;
    }
  } else if (tab.value === 1) {
    isLoading.value = true;
    try {
      const response = await request('post', 'api/office/getReductionExObuList', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
        },
      });
      exmObuSearchData.prev = { ...searchData.value };
      exmObuContents.value = response;
      setExmObuFooterContents();
      exmObuDialogDataContents.value = {};
      exmObuDialogInputContents.value = {};
    } catch (error) {
      showMessage('error', 'warning');
    } finally {
      isLoading.value = false;
    }
  } else if (tab.value === 2) {
    try {
      isLoading.value = true;
      const response = await request('post', 'api/office/getReductionExWelfareList', {
        ...searchData.value,
        ...{
          START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
          END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
          IC_CODE: authStore.getIcCode,
          WCARD_NO: searchData.value['WCARD_NO'].replaceAll('', ''),
        },
      });
      welfareSearchData.prev = { ...searchData.value };
      welfareContents.value = response;
      setWelfareFooterContents();
    } catch (error) {
      showMessage('error', 'warning');
    } finally {
      isLoading.value = false;
    }
  }
};

const handleExcel = async () => {
  let headerRow = 1;
  let subTitle = '';
  let headers = [];
  let contents = [];
  let prevSearchData = {};
  let footer = [];
  if (tab.value === 0) {
    subTitle = '긴급면제카드';
    headers = [...exmCardHeaders.value];
    contents = [...exmCardContents.value];
    prevSearchData = { ...exmCardSearchData.prev };
    footer = [
      exmCardFooterContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ];
  } else if (tab.value === 1) {
    subTitle = '감면단말기';
    headers = [...exmObuHeaders.value];
    contents = [...exmObuContents.value];
    prevSearchData = { ...exmObuSearchData.prev };
    footer = [
      exmObuFooterContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ];
  } else if (tab.value === 2) {
    subTitle = '통합복지(위치)';
    headers = [...welfareHeaders.value];
    contents = [...welfareContents.value];
    prevSearchData = { ...welfareSearchData.prev };
    footer = [
      welfareFooterContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ];
  }
  if (contents.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, prevSearchData, headers, contents, subTitle, appTitle, footer);
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
    subTitle = '긴급면제카드';
    requestData = createOzDataset('/OFFICE/reductionExEcardList.ozr', {
      CSV_DATA: exmCardContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: `${appTitle} - ${subTitle}`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, exmCardSearchData.prev),
      TOTAL_COUNT: exmCardSubTotalCount.value,
      TOTAL_ORIGIN_FARE: exmCardSubTotalSumOriginFare.value,
    });
  } else if (tab.value === 1) {
    subTitle = '감면단말기';
    requestData = createOzDataset('/OFFICE/reductionExObuList.ozr', {
      CSV_DATA: exmObuContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: `${appTitle} - ${subTitle}`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, exmObuSearchData.prev),
      TOTAL_COUNT: exmObuSubTotalCount.value,
      TOTAL_ORIGIN_FARE: exmObuSubTotalSumOriginFare.value,
    });
  } else if (tab.value === 2) {
    subTitle = '통합복지';
    requestData = createOzDataset('/OFFICE/reductionExWelfareList.ozr', {
      CSV_DATA: welfareContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: `${appTitle} - ${subTitle}`,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, welfareSearchData.prev),
      TOTAL_COUNT: welfareSubTotalCount.value,
      TOTAL_ORIGIN_FARE: welfareSubTotalSumOriginFare.value,
      TOTAL_RFND_FARE: welfareSubTotalSumRefundFare.value,
    });
  }
  return requestData;
};

const handlePrint = () => {
  let contentsLength = 0;
  if (tab.value === 0) {
    contentsLength = exmCardContents.value.length;
  } else if (tab.value === 1) {
    contentsLength = exmObuContents.value.length;
  } else if (tab.value === 2) {
    contentsLength = welfareContents.value.length;
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
//#endregion
</script>

<style scoped>
/* UI */
.v-dialog-content {
  margin: 0px 7px 10px 7px;
}
.dialog-img-car {
  background-color: lightgray;
  width: 377px;
  height: 215px;
}
.dialog-img-plate {
  background-color: darkgray;
  width: 377px;
  height: 72px;
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
