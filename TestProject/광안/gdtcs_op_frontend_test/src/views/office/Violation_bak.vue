<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="isLoading" />
  <v-row>
    <v-row class="search-container">
      <v-card variant="text">
        <SearchDataComponent
          :headers="searchHeader"
          v-model="searchData"
          style="width: 1160px"
          @keyup.enter="handleSearch"
          @btn-click-event="btnClickEvent"
        />
        <GridComponent
          :headers="headers"
          :contents="contents"
          :key="gridKey"
          :margin-y="153"
          :is-default-click="true"
          :is-select="true"
          :css="localCSS"
          v-model="selectedItems"
          @grid-click-event="onGridClickEvent"
          @grid-dbl-click-event="onGridDblClickEvent"
        >
          <template v-slot:table-footer>
            <th style="padding: 10px">[소계]</th>
            <th style="height: 15px" :colspan="headers.length">
              건수: {{ selectedItems.length != 0 ? selectedItems.length : contents.length }} 건&ensp;&ensp;
              <span class="left-hr-line">&ensp;&ensp;원 통행요금: {{ getTotalSumPrice[0] }} 원</span>
              <span class="left-hr-line">&ensp;&ensp;통행요금: {{ getTotalSumPrice[1] }} 원</span>
              <span class="left-hr-line">&ensp;&ensp;출금액: {{ getTotalSumPrice[2] }} 원</span>
              <span class="left-hr-line">&ensp;&ensp;수납할통행요금: {{ getTotalSumPrice[3] }} 원</span>
              <span class="left-hr-line">&ensp;&ensp;수납한통행요금: {{ getTotalSumPrice[4] }} 원</span>
            </th>
          </template>
        </GridComponent>
      </v-card>
    </v-row>
    <v-row>
      <v-col cols="9">
        <GridComponent :headers="detailHeaders" :contents="detailContents" :height="150" :isNoDataAlert="false" />
      </v-col>
      <v-col cols="3" class="vertical-panel">
        <v-img class="detail-carImg" width="100%" height="150px" :src="imgSrcCar" />
      </v-col>
    </v-row>
  </v-row>
  <v-dialog v-model="dialog" max-width="1200px" style="z-index: 2000">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>하이패스 위반처리</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="8">
            <div class="dialog-subtitle">
              <h3>기본정보</h3>
            </div>
            <GridSystemComponent
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
              :cols-per-row="3"
              :headers="dialogSecondHeaders"
              :contents="dialogContents"
              :table-th-width="'12%'"
              :table-height="'50px'"
            />
            <div class="dialog-subtitle">
              <h3 style="margin: 0; padding-right: 10px">입력정보</h3>
              <th style="color: #0095ff; font-size: 15px; white-space: nowrap; padding: 0">
                <span v-if="dialogContents.BFHD_REG_YN === 'Y'">[사전등록대상 : {{ dialogContents.BFHD_REG_YN }}]</span>
                <span v-if="dialogContents.BS_EXM_TYPE === '2'">[부산시면제대상 : {{ dialogContents.BS_EXM_TYPE_NM }}]</span>
                <span v-if="dialogContents.ECARD_TYPE === '01' && dialogContents.VLTN_CODE !== '37' && dialogContents.VLTN_CODE !== '38'">
                  [후불보정대상 : Y]
                </span>
                <span v-if="dialogContents.LPAY_CRCT_YN === 'Y'">[후불보정여부 : {{ dialogContents.LPAY_CRCT_YN }}]</span>
                <span v-if="dialogContents.LPAY_CRCT_YN === 'Y'">[후불보정금액 : {{ dialogContents.CRCT_PAY_FARE }}]</span>
              </th>
            </div>
            <InputFormGrid
              :cols-per-row="2"
              :headers="dialogThirdHeaders"
              v-model="dialogContents"
              :table-header-width="'12%'"
              :selectChanged="resetInputData"
            />
          </v-col>
          <v-col cols="4" class="vertical-dialog">
            <v-row>
              <div class="dialog-subtitle">
                <h3>차량사진</h3>
              </div>
              <v-img class="dialog-carImg" width="100%" height="215" :src="imgSrcCar" />
            </v-row>
            <v-row>
              <div class="dialog-subtitle">
                <h3>차량번호사진</h3>
              </div>
              <v-img class="dialog-plateImg" width="100%" height="120" :src="imgSrcPlate" />
            </v-row>
            <v-row>
              <div class="dialog-subtitle">
                <h3>인식한 차량번호</h3>
              </div>
              <v-text-field v-model="rpaCarNo" width="100%" variant="outlined" :readonly="true" density="compact" hide-details />
            </v-row>
            <DialogButtonNavi @btn-click-event-before="btnBefore" @btn-click-event-after="btnAfter" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="btnCheckPl">면제PL확인</v-btn>
        <v-btn variant="flat" color="blue darken-1" text @click="btnSave">저장</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <template>
    <v-container>
      <v-dialog v-model="isActive">
        <template v-slot:default="{ isActive }">
          <v-card>
            <OZReportComponent :json-data="getJsonData" />
            <v-card-actions>
              <v-btn text="닫기" @click="isActive.value = false"></v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
    </v-container>
  </template>
</template>

<script setup>
import { ref, onActivated, nextTick, watch, computed } from 'vue';
import { useAuthStore, useFareStore, ozUrl } from '@/stores/index';
import {
  request,
  btnHandler,
  createOzDataset,
  getSysCodeDp,
  comma,
  getWorkNo,
  getFareInfo,
  getFareByCarInfo,
  getCpdFareByCarInfo,
  getDiffFareByCarInfo,
  ImageCategory,
  getImage,
  confirmMessageBox,
  alertMessageBox,
  showMessage,
} from '@/utils/common.js';
import { excelDownload } from '@/utils/excel';
import { ElMessage, ElMessageBox } from 'element-plus';
import dayjs from 'dayjs';

const authStore = useAuthStore();
const fareStore = useFareStore();

const isLoading = ref(false);

const workNoOption = ref([]);
const handTypeOption = ref([]);
const handCarTypeOption = ref([]);
const handDivOption = ref([]);
const vltnCodeOption = ref([]);
const payTypeOption = ref([]);
const exmTypeDtlOption = ref([]);
const dcTypeOption = ref([]);

const gridKey = ref(0);
const contents = ref([]);
const detailContents = ref([]);

const localCSS = ref({
  class: 'bg-F7EBDB',
  condition: (row) => {
    if (row.item['VLTN_HAND_DIV_NM'] === '처리[1]') return true;
  },
});

const imgSrcCar = ref();
const imgSrcPlate = ref();
const rpaCarNo = ref('');

const dialog = ref(false);
const dialogContents = ref({});
const dialogHandCarType = ref([]);
const dialogHandType = ref([]);
const dialogPayType = ref([]);
const dialogExmTypeDtl = ref([]);
const dialogDcType = ref([]);

const selectedItems = ref([]);
const isActive = ref(false);
const jsonData = ref('');

const searchHeader = ref([
  { label: '조회기간:', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '근무번호', key: 'WORK_NO', type: 'select', option: workNoOption, width: '130px' },
  { label: '차량번호', key: 'HAND_CAR_NO', type: 'input' },
  { label: '처리유형', key: 'HAND_TYPE', type: 'select', option: handTypeOption, width: '130px' },
  { label: '지불구분', key: 'VLTN_PAY_TYPE', type: 'select', option: payTypeOption, width: '130px' },
  { label: '처리차종', key: 'HAND_CAR_TYPE', type: 'select', option: handCarTypeOption, width: '130px' },
  { label: '처리구분', key: 'VLTN_HAND_DIV', type: 'select', option: handDivOption, width: '130px' },
  { label: '위반코드', key: 'VLTN_CODE', type: 'select', option: vltnCodeOption, width: '130px' },
  {
    label: '',
    key: 'RDO_HAND_TYPE',
    type: 'radio',
    option: [
      { title: '정상처리', value: '0' },
      { title: '미납처리', value: '1' },
    ],
  },
  { label: '', title: '일괄처리', key: 'BTN_BATCH', type: 'button' },
  { label: '', title: '경차PL자동처리', key: 'BTN_PL_AUTO', type: 'button' },
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
  BTN_BATCH: 'btnBatch',
  BTN_PL_AUTO: 'btnPlAuto',
});

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', align: 'center' },
  { title: '영업소명', key: 'IC_CODE_NM', align: 'center' },
  { title: '근무일자', key: 'WORK_DATE_FORM', align: 'center' },
  { title: '근무번호', key: 'WORK_NO', align: 'center' },
  { title: '일련번호', key: 'HAND_SNO', align: 'center' },
  { title: '발생시각', key: 'OCC_DT_FORM', align: 'center' },
  { title: '위반코드', key: 'VLTN_CODE_NM', align: 'center' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_NM', align: 'center' },
  { title: '수납구분', key: 'MAIN_PAY_DIV_NM', align: 'center' },
  { title: '전자카드수납구분', key: 'ECARD_PAY_DIV_NM', align: 'center' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_FORM', align: 'center' },
  { title: '통행요금', key: 'PASS_FARE_FORM', align: 'center' },
  { title: '출금액', key: 'WTHD_FARE_FORM', align: 'center' },
  { title: '전자카드종류', key: 'ECARD_TYPE_NM', align: 'center' },
  { title: '발생카드번호', key: 'CARD_NO_FORM', align: 'center' },
  { title: '전자카드상태', key: 'ECARD_STAT_NM', align: 'center' },
  { title: '전자카드속성', key: 'ECARD_ATT_NM', align: 'center' },
  { title: 'OBU상태', key: 'OBU_STAT_NM', align: 'center' },
  { title: 'OBU속성', key: 'OBU_ATT_NM', align: 'center' },
  { title: '부산시면제대상', key: 'BS_EXM_TYPE_NM', align: 'center' },
  { title: '부산시면제상세', key: 'BS_EXM_TYPE_DTL_NM', align: 'center' },
  { title: '사전등록여부', key: 'BFHD_REG_YN', align: 'center' },
  { title: '처리구분', key: 'VLTN_HAND_DIV_NM', align: 'center' },
  { title: '처리시각', key: 'HAND_DT_FORM', align: 'center' },
  { title: '처리유형', key: 'HAND_TYPE_NM', align: 'center' },
  { title: '지불구분', key: 'VLTN_PAY_TYPE_NM', align: 'center' },
  { title: '처리면제상세', key: 'EXMT_DTL_TYPE_NM', align: 'center' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_NM', align: 'center' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', align: 'center' },
  { title: '처리카드번호', key: 'HAND_ECARD_NO_FORM', align: 'center' },
  { title: '연속통행할인적용', key: 'CPD_DIV_NM', align: 'center' },
  { title: '연속통행할인액', key: 'CPD_FARE_FORM', align: 'center' },
  { title: '차등요금적용', key: 'DIFF_DIV_NM', align: 'center' },
  { title: '차등요금할인액', key: 'DIFF_FARE_FORM', align: 'center' },
  { title: '수납할통행요금', key: 'OFC_EXPT_PAY_FARE_FORM', align: 'center' },
  { title: '총수납한통행요금', key: 'SUM_OFC_PAY_FARE_FORM', align: 'center' },
  { title: '수납전카드잔액', key: 'PAY_BEF_BALC_FORM', align: 'center' },
  { title: '후불보정여부', key: 'LPAY_CRCT_YN', align: 'center' },
  { title: '후불보정금액', key: 'CRCT_PAY_FARE_FORM', align: 'center' },
  { title: '전자카드차량번호', key: 'ECARD_CAR_NO', align: 'center' },
  { title: 'OBU유형', key: 'OBU_TYPE_NM', align: 'center' },
  { title: 'OBU번호', key: 'OBU_NO_FORM', align: 'center' },
  { title: 'OBU차량번호', key: 'OBU_CAR_NO', align: 'center' },
  { title: '근무구분', key: 'WORK_DIV_NM', align: 'center' },
  { title: '관리자번호', key: 'ADMIN_ID', align: 'center' },
  { title: '비고', key: 'NOTE', align: 'center' },
]);

const detailHeaders = ref([
  { title: '수정처리번호', key: 'MDFY_SNO', align: 'center' },
  { title: '처리일시', key: 'HAND_DT_FORM', align: 'center' },
  { title: '처리유형', key: 'HAND_TYPE_NM', align: 'center' },
  { title: '지불구분', key: 'VLTN_PAY_TYPE_NM', align: 'center' },
  { title: '총수납한통행요금', key: 'SUM_OFC_PAY_FARE_FORM', align: 'center' },
  { title: '연속통행할인적용', key: 'CPD_DIV_NM', align: 'center' },
  { title: '처리카드번호', key: 'HAND_ECARD_NO', align: 'center' },
  { title: '위반발생일자', key: 'WORK_DATE_FORM', align: 'center' },
  { title: '비고', key: 'NOTE', align: 'center', width: '200px' },
  { title: '수정자', key: 'ADMIN_ID', align: 'center' },
]);

const dialogFirstHeaders = ref([
  { title: '근무번호', key: 'WORK_NO' },
  { title: '일련번호', key: 'HAND_SNO' },
  { title: '처리구분', key: 'VLTN_HAND_DIV_NM' },
  { title: '위반코드', key: 'VLTN_CODE_NM' },
  { title: '발생일시', key: 'OCC_DT_FORM' },
  { title: '처리일시', key: 'HAND_DT_FORM' },
  { title: '근무구분', key: 'WORK_DIV_NM' },
  { title: 'OBU차종', key: 'OBU_CAR_TYPE_NM' },
  { title: '출금전잔액', key: 'PAY_BEF_BALC' },
  { title: '발생카드번호', key: 'CARD_NO_FORM' },
  { title: 'OBU번호', key: 'OBU_NO_FORM' },
  { title: '통행요금', key: 'PASS_FARE' },
  { title: '차등요금적용', key: 'DIFF_DIV_NM' },
  { title: 'OBU상태', key: 'OBU_STAT_NM' },
  { title: '출금액', key: 'WTHD_FARE' },
  { title: '사전등록여부', key: 'BFHD_REG_YN' },
  { title: 'OBU속성', key: 'OBU_ATT_NM' },
  { title: '후불보정금액', key: 'CRCT_PAY_FARE' },
  { title: '부산시면제대상', key: 'BS_EXM_TYPE_NM' },
  { title: 'OBU유형', key: 'OBU_TYPE_NM' },
  { title: '전자카드속성', key: 'ECARD_ATT_NM' },
  { title: '부산시면제상세', key: 'BS_EXM_TYPE_DTL_NM' },
  { title: '수납구분', key: 'MAIN_PAY_DIV_NM' },
  { title: '전자카드종류', key: 'ECARD_TYPE_NM' },
]);

const dialogSecondHeaders = ref([
  { title: '처리시각', key: 'BEIC_HAND_DT_FORM' },
  { title: '영업소번호', key: 'BEIC_IC_CODE' },
  { title: '근무번호', key: 'BEIC_WORK_NO' },
  { title: '처리일련번호', key: 'BEIC_HAND_SNO' },
  { title: '처리유형', key: 'BEIC_HAND_TYPE_NM' },
  { title: '차종', key: 'BEIC_CAR_TYPE_NM' },
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
  { title: '처리카드번호', key: 'HAND_ECARD_NO' },
  {
    title: '부산시면제상세',
    key: 'BS_EXM_TYPE_DTL',
    option: 'select',
    selectItem: dialogExmTypeDtl,
  },
  { title: '고객명', key: 'CUST_NM' },
  { title: '통행요금', key: 'OFC_PASS_FARE', disabled: true },
  { title: '고객전화번호', key: 'CUST_TEL' },
  {
    title: '할인적용',
    key: 'CPD_DIV',
    option: 'select',
    selectItem: dialogDcType,
  },
  { title: '고객주소', key: 'CUST_ADDR' },
  { title: '연속통행할인액', key: 'CPD_FARE', disabled: true },
  { title: '우편번호', key: 'CUST_ZIP_CODE' },
  { title: '수납할통행요금', key: 'OFC_EXPT_PAY_FARE', option: 'numberInput' },
  { title: '승인번호', key: 'APP_NO' },
  { title: '수납한통행요금', key: 'SUM_OFC_PAY_FARE', option: 'numberInput' },
  { title: '비고', key: 'NOTE', size: 1 },
]);

const ozParam = ref({
  CSV_DATA: contents.value,
  START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
  END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
  IC_CODE: authStore.getIcCode,
  WORK_NO: '보고서번호',
  TITLE_NM: '위반심사 내역 조회',
  IMAGE_URL: ozUrl + 'images/sign_one.png',
});

onActivated(async () => {
  await getSysCodeDp('057', vltnCodeOption);
  await getSysCodeDp('800', handDivOption);

  await getSysCodeDp('304', handTypeOption);
  dialogHandType.value = handTypeOption.value.slice(1);

  await getSysCodeDp('801', dcTypeOption);
  dialogDcType.value = dcTypeOption.value.slice(1);

  await getSysCodeDp('132', payTypeOption);
  dialogPayType.value = payTypeOption.value.slice(1);

  await getSysCodeDp('292', exmTypeDtlOption);
  dialogExmTypeDtl.value = exmTypeDtlOption.value.slice(1);

  await getSysCodeDp('171', handCarTypeOption);
  dialogHandCarType.value = handCarTypeOption.value.slice(1);

  await getWorkNo(searchData.value.START_DATE, searchData.value.END_DATE);

  getFareInfo(authStore.getIcCode);

  await nextTick();
});

const getTotalSumPrice = computed(() => {
  let ORIGIN_PASS_FARE = 0;
  let PASS_FARE = 0;
  let WTHD_FARE = 0;
  let OFC_EXPT_PAY_FARE = 0;
  let SUM_OFC_PAY_FARE = 0;

  const calcTotal = (datas) =>
    datas.forEach((data) => {
      ORIGIN_PASS_FARE += Number(data.ORIGIN_PASS_FARE);
      PASS_FARE += Number(data.PASS_FARE);
      WTHD_FARE += Number(data.WTHD_FARE);
      OFC_EXPT_PAY_FARE += Number(data.OFC_EXPT_PAY_FARE);
      SUM_OFC_PAY_FARE += Number(data.SUM_OFC_PAY_FARE);
    });

  selectedItems.value.length != 0 ? calcTotal(selectedItems.value) : calcTotal(contents.value);
  return [comma(ORIGIN_PASS_FARE), comma(PASS_FARE), comma(WTHD_FARE), comma(OFC_EXPT_PAY_FARE), comma(SUM_OFC_PAY_FARE)];
});

const getJsonData = computed(() => {
  console.log(`output->ozParam.value`, ozParam.value);
  jsonData.value = createOzDataset('/OFFICE/violation.ozr', ozParam.value);
  console.log(`output->jsonData.value`, jsonData.value);
  return jsonData.value;
});

watch([() => searchData.value.START_DATE, () => searchData.value.END_DATE], ([newWorkDateS, newWorkDateE], [oldWorkDateS, oldWorkDateE]) => {
  if (newWorkDateS !== oldWorkDateS || newWorkDateE !== oldWorkDateE) {
    getWorkNo(newWorkDateS, newWorkDateE).then((workList) => {
      workNoOption.value = workList;
    });
  }
});

const handleSearch = async () => {
  console.log(`output->searchData.value`, searchData.value);
  isLoading.value = true;
  try {
    const response = await request('post', 'api/office/getViolationList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    contents.value = response;
    selectedItems.value = [];
    console.log(response);

    gridKey.value++;
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

const onGridClickEvent = async (item, index) => {
  console.log(`onGridClickEvent->item`, item);
  console.log(`onGridClickEvent->item`, index);

  const response = await request('post', 'api/office/getViolationHistList', {
    ...item,
    ...{ WORK_DATE: item['WORK_DATE'].replaceAll('-', ''), IC_CODE: authStore.getIcCode },
  });
  detailContents.value = response;
  //imgSrcCar.value = getImage(ImageCategory.CAR, detailContents.value);
  imgSrcCar.value =
    'http://localhost:9081/api/video/etcs/getImage?IC_CODE=094&tolling_time=20231128045539&work_number=6201&processing_number=196&filepathStartOffset=18';
};

const currentIndex = ref(0);

const onGridDblClickEvent = async (item, index) => {
  console.log(`onGridDblClickEvent->item`, item);
  console.log(`onGridDblClickEvent->index`, index);
  currentIndex.value = index;

  dialog.value = true;

  dialogContents.value = { ...item };
  //imgSrcPlate.value = getImage(ImageCategory.PLATE, dialogContents.value);
  imgSrcPlate.value =
    'http://localhost:9081/api/video/etcs/getImagePlate?IC_CODE=094&tolling_time=20231128045539&work_number=6201&processing_number=196&filepathStartOffset=18';
  rpaCarNo.value = item.HAND_CAR_NO;

  resetInputData(item.VLTN_HAND_DIV, 'INIT');
};

const resetInputData = async (value, key) => {
  console.log(`Select box with key: ${key} changed to ${value}`);

  const totalFare = ref(0);

  const setDisable = (isDisable) => {
    if (isDisable) {
      dialogThirdHeaders.value.find((header) => header.key === 'HAND_ECARD_NO').disabled = true;
      dialogContents.value.HAND_ECARD_NO = '';
    } else {
      dialogThirdHeaders.value.find((header) => header.key === 'HAND_ECARD_NO').disabled = false;
    }
  };

  if (key === 'INIT' && value === '0') {
    console.log('미처리 데이터 첫 심사시 기본 세팅');

    if (dialogContents.value.ECARD_TYPE === '01' && dialogContents.value.VLTN_CODE !== '37' && dialogContents.value.VLTN_CODE !== '38') {
      //후불전자카드,지불가능일시 정상-후불보정 기본세팅
      dialogContents.value.HAND_TYPE = '90';
      dialogContents.value.VLTN_PAY_TYPE = '19';
      dialogContents.value.BS_EXM_TYPE_DTL = '01';
    } else {
      //이후 모든 경우 미납-없음 기본세팅
      console.warn('기본 미납-없음 세팅');
      dialogContents.value.HAND_TYPE = '10';
      dialogContents.value.VLTN_PAY_TYPE = '0';
      dialogContents.value.BS_EXM_TYPE_DTL = '01';
    }
    dialogContents.value.OFC_PASS_FARE = dialogContents.value.PASS_FARE - dialogContents.value.WTHD_FARE;
    dialogContents.value.CPD_DIV = dialogDcType.value[0].value;
    dialogContents.value.OFC_EXPT_PAY_FARE = dialogContents.value.OFC_PASS_FARE;
    dialogContents.value.SUM_OFC_PAY_FARE = 0;
  }

  switch (dialogContents.value.HAND_TYPE) {
    case '10':
      //처리유형-미납
      setDisable(false);
      dialogPayType.value = payTypeOption.value.filter((val) => ['0'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      dialogDcType.value = dcTypeOption.value.filter((val) => ['00', '01'].includes(val.value));
      break;
    case '30':
      //처리유형-완납
      setDisable(false);
      if (dialogContents.value.BFHD_REG_YN === 'Y') dialogPayType.value = payTypeOption.value.filter((val) => ['30'].includes(val.value));
      if (dialogContents.value.BFHD_REG_YN === 'N') dialogPayType.value = payTypeOption.value.filter((val) => ['1', '2'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      dialogDcType.value = dcTypeOption.value.filter((val) => ['00', '01'].includes(val.value));
      break;
    case '40':
      setDisable(true);
      // 처리유형:면제
      if (dialogContents.value.ECARD_TYPE === '00') dialogPayType.value = payTypeOption.value.filter((val) => ['0', '50'].includes(val.value));
      if (dialogContents.value.ECARD_TYPE === '01') dialogPayType.value = payTypeOption.value.filter((val) => ['0', '19'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => val.value !== '');
      dialogDcType.value = dcTypeOption.value.filter((val) => ['00'].includes(val.value));
      break;
    case '80':
      //처리유형-부적격
      setDisable(false);
      dialogPayType.value = payTypeOption.value.filter((val) => ['60', '61', '62'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      dialogDcType.value = dcTypeOption.value.filter((val) => ['00'].includes(val.value));
      break;
    case '90':
      //처리유형-정상
      setDisable(false);
      if (dialogContents.value.ECARD_TYPE === '00') dialogPayType.value = payTypeOption.value.filter((val) => ['0', '18'].includes(val.value));
      if (dialogContents.value.ECARD_TYPE === '01') dialogPayType.value = payTypeOption.value.filter((val) => ['0', '18', '19'].includes(val.value));
      dialogExmTypeDtl.value = exmTypeDtlOption.value.filter((val) => ['01'].includes(val.value));
      dialogDcType.value = dcTypeOption.value.filter((val) => ['00', '01'].includes(val.value));
      break;
    default:
      return;
  }

  if (key === 'HAND_TYPE') {
    console.log('처리유형 변경 - 대표값[0]세팅');
    dialogContents.value.VLTN_PAY_TYPE = dialogPayType.value[0].value;
    dialogContents.value.BS_EXM_TYPE_DTL = dialogExmTypeDtl.value[0].value;
    dialogContents.value.CPD_DIV = dialogDcType.value[0].value;
  }
  //처리차종별 통행요금 세팅
  dialogContents.value.OFC_PASS_FARE = await getFareByCarInfo(dialogContents.value.HAND_CAR_TYPE, dialogContents.value.OCC_DT);
  //처리차종별 연속통행할인액 세팅, 할인구분에 따라 적용
  const cpdFare = await getCpdFareByCarInfo(dialogContents.value.HAND_CAR_TYPE, dialogContents.value.OCC_DT, '080');
  dialogContents.value.CPD_FARE = dialogContents.value.CPD_DIV === '01' ? cpdFare : 0;
  const diffFare = await getDiffFareByCarInfo(dialogContents.value.HAND_CAR_TYPE, dialogContents.value.OCC_DT);
  dialogContents.value.DIFF_FARE = dialogContents.value.DIFF_DIV === '1' ? diffFare : 0;
  //최종통행요금 세팅
  dialogContents.value.OFC_PASS_FARE = dialogContents.value.OFC_PASS_FARE - dialogContents.value.DIFF_FARE;
  totalFare.value = dialogContents.value.OFC_PASS_FARE - dialogContents.value.CPD_FARE;

  const handType = dialogContents.value.HAND_TYPE;
  const payType = dialogContents.value.VLTN_PAY_TYPE;
  const wthdFare = dialogContents.value.WTHD_FARE;
  const vltnCode = dialogContents.value.VLTN_CODE;
  const ecardType = dialogContents.value.ECARD_TYPE;

  //처리유형에 따른 지불구분에 따른 금액 세팅 초기화
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
        console.warn('완납-사전등록 통행요금-100원 처리');
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
      if (payType === '50') {
        console.warn('면제-환불 처리 : 선불전자-환불');
        dialogContents.value.OFC_EXPT_PAY_FARE = 0;
        dialogContents.value.SUM_OFC_PAY_FARE = 0;
      }
      if (payType === '19' && wthdFare !== 0) {
        let isConfirmed = true;
        if (key !== 'INIT' || value !== '1') {
          isConfirmed = await confirmMessageBox('후불전자카드로 과금된 면제차량입니다. 0원 후불보정처리 하시겠습니까?', '후불보정');
        }
        if (!isConfirmed) {
          showMessage('후불보정 처리 취소', 'warning');
          onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
          return;
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
      if (ecardType === '01' && wthdFare !== 0) {
        console.warn('후불전자 과금된 부적격 0원 처리');
        let isConfirmed = true;
        if (key !== 'INIT' || value !== '1') {
          isConfirmed = await confirmMessageBox('후불전자카드로 과금된 부적격차량입니다. 0원으로 후불보정처리 하시겠습니까?', '후불보정');
        }
        if (!isConfirmed) {
          showMessage('후불보정 처리 취소', 'warning');
          onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
          return;
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
        dialogContents.value.OFC_EXPT_PAY_FARE = 0;
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
        let isConfirmed = true;
        if (key !== 'INIT' || value !== '1') {
          isConfirmed = await confirmMessageBox('후불보정 대상 차량입니다. 후불보정 처리하시겠습니까?', '후불보정');
        }
        if (!isConfirmed) {
          showMessage('후불보정 처리 취소', 'warning');
          onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
          return;
        }
        if (vltnCode === '37' || vltnCode === '38') {
          showMessage('카드 출금 불가', 'warning');
          onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
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
};

const btnSave = async () => {
  const passFare = dialogContents.value.PASS_FARE;
  const wthdFare = dialogContents.value.WTHD_FARE;
  const ecardType = dialogContents.value.ECARD_TYPE;
  const handType = dialogContents.value.HAND_TYPE;
  const payType = dialogContents.value.VLTN_PAY_TYPE;
  const ofcPassFare = dialogContents.value.OFC_PASS_FARE;
  const ofcExptPayFare = dialogContents.value.OFC_EXPT_PAY_FARE;
  const sumOfcPayFare = dialogContents.value.SUM_OFC_PAY_FARE;
  const lpayCrctYn = dialogContents.value.LPAY_CRCT_YN;
  dialogContents.value.CRCT_PAY_FARE = dialogContents.value.LPAY_CRCT_YN === 'Y' ? dialogContents.value.OFC_EXPT_PAY_FARE : 0;
  const crctPayFare = dialogContents.value.CRCT_PAY_FARE;

  console.log('passFare', passFare, typeof passFare);
  console.log('wthdFare', wthdFare, typeof wthdFare);
  console.log('ecardType', ecardType, typeof ecardType);
  console.log('handType', handType, typeof handType);
  console.log('payType', payType, typeof payType);
  console.log('ofcExptPayFare', ofcExptPayFare, typeof ofcExptPayFare);
  console.log('sumOfcPayFare', sumOfcPayFare, typeof sumOfcPayFare);
  console.log('lpayCrctYn', lpayCrctYn, typeof lpayCrctYn);
  console.log('crctPayFare', crctPayFare, typeof crctPayFare);

  //수납할금액,수납한금액 유효성검사
  if (ofcExptPayFare === undefined || sumOfcPayFare === undefined) {
    alertMessageBox('통행요금을 입력하세요.', '경고', 'warning');
    return false;
  }
  switch (handType) {
    case '10':
      if (ofcExptPayFare === 0 || sumOfcPayFare !== 0) {
        alertMessageBox('미납-통행요금이 올바르지 않습니다.', '경고', 'warning');
        return;
      }
      if (passFare > ofcPassFare) {
        alertMessageBox('미납 : 입력된 통행요금이 기존 통행요금보다 작을 수 없습니다.', '경고', 'warning');
        return;
      }
      break;

    case '30':
      if (ofcExptPayFare === 0 || sumOfcPayFare === 0) {
        alertMessageBox('완납-통행요금이 올바르지 않습니다.', '경고', 'warning');
        return;
      } else if (ofcExptPayFare !== sumOfcPayFare) {
        alertMessageBox('완납은 통행요금과 수납한금액이 동일해야 합니다.', '경고', 'warning');
        return;
      }
      if (passFare > ofcPassFare) {
        alertMessageBox('완납 : 입력된 통행요금이 기존 통행요금보다 작을 수 없습니다.', '경고', 'warning');
        return;
      }
      break;

    case '40':
      if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
        alertMessageBox('면제-통행요금이 0원이 아닙니다.', '경고', 'warning');
        return;
      }

      if (wthdFare !== 0 && payType === '0') {
        alertMessageBox('출금액이 있는 데이터는 면제-없음 처리할 수 없습니다.', '경고', 'warning');
        return;
      } else if (wthdFare === 0 && (payType === '50' || payType === '19')) {
        alertMessageBox('출금액이 없는 데이터는 면제-환불,후불보정 처리할 수 없습니다.', '경고', 'warning');
        return;
      }

      break;

    case '80':
      if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
        alertMessageBox('부적격-통행요금이 0원이 아닙니다.', '경고', 'warning');
        return;
      }

      if (wthdFare !== 0 && ecardType === '00') {
        alertMessageBox('출금액이 있는 선불전자카드 데이터는 부적격 처리할 수 없습니다.', '경고', 'warning');
        return;
      }
      break;

    case '90':
      if (sumOfcPayFare !== 0) {
        alertMessageBox('정상-통행요금이 0원이 아닙니다.', '경고', 'warning');
        return;
      }

      if (wthdFare === 0 && payType === '0') {
        alertMessageBox('출금액이 없는 데이터는 정상-없음 처리할 수 없습니다.', '경고', 'warning');
        return;
      } else if (wthdFare !== 0 && payType === '18') {
        alertMessageBox('출금액이 있는 데이터는 정상-0원정상 처리할 수 없습니다.', '경고', 'warning');
        return;
      }

      if (ecardType === '00') {
        if (ofcExptPayFare !== 0 || sumOfcPayFare !== 0) {
          alertMessageBox('정상-선불전자카드 경우 통행요금이 0원이 아닙니다.', '경고', 'warning');
          return;
        }
      }
      break;

    default:
      alertMessageBox('처리유형 이상', '경고', 'warning');
      break;
  }

  console.log('dialogContents.value : ', dialogContents.value);

  try {
    const response = await request('post', 'api/office/setViolationAudit', {
      ...dialogContents.value,
    });
    console.log('response', response);
    alertMessageBox(`${response[0].CODE}: ${response[0].MESSAGE}`, '경고', 'warning');
    if (response[0].CODE === 'SUCCESS') {
      dialog.value = false;
      handleSearch();
    }
  } catch (error) {
    console.error('처리 중 오류가 발생했습니다.', error);
  } finally {
    isLoading.value = false;
  }
};

const btnBefore = () => {
  if (currentIndex.value <= 0) {
    alertMessageBox('첫 데이터 입니다.', '경고', 'warning');
    return;
  }
  currentIndex.value--;
  onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
};
const btnAfter = () => {
  if (currentIndex.value >= contents.value.length - 1) {
    alertMessageBox('마지막 데이터 입니다.', '경고', 'warning');
    return;
  }
  currentIndex.value++;
  onGridDblClickEvent(contents.value[currentIndex.value], currentIndex.value);
};

const btnCheckPl = async () => {
  const { ECARD_ATT, CARD_NO, HAND_CAR_NO } = dialogContents.value;

  if (CARD_NO.length < 12) {
    alertMessageBox('처리카드번호가 올바르지 않습니다', '경고', 'warning');
    return;
  }

  if (HAND_CAR_NO.length < 7) {
    alertMessageBox('처리차량번호가 올바르지 않습니다', '경고', 'warning');
    return;
  }

  const isEcard = ECARD_ATT >= '20' && ECARD_ATT <= '29';
  const apiEndpoint = isEcard ? 'api/office/getEcardPl' : 'api/office/getExemptPl';
  const payload = isEcard ? { CARD_NO } : { HAND_CAR_NO };

  const response = await request('post', apiEndpoint, payload);

  const msg = response[0].PL_YN === 'Y' ? '면제차량입니다.' : '면제차량이 아닙니다.';
  alertMessageBox(msg, '경고', 'warning');
};

const btnClickEvent = (key) => {
  if (key === 'BTN_BATCH') {
    btnBatch();
  } else if (key === 'BTN_PL_AUTO') {
    btnPlAuto();
  }
};
const btnBatch = () => {
  alert('일괄처리버튼');
};
const btnPlAuto = () => {
  alert('경차PL버튼');
};

const btnProcPl = () => {
  dialog.value = true;
};

const handleExcel = () => {
  ElMessageBox.confirm('엑셀 다운로드를 진행 하시겠습니까?', 'Excel download', {
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    type: 'success',
  })
    .then(async () => {
      const excelHeader = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
      const excelContents = selectedItems.value.length === 0 ? contents.value : selectedItems.value;
      excelDownload(1, searchHeader.value, searchData.value, excelHeader, excelContents, '위반심사 내역조회', '위반심사 내역조회');
    })
    .catch(() => {
      ElMessage({
        type: 'warning',
        message: '액셀다운로드 취소',
      });
    });
};

const handlePrint = async () => {
  if (selectedItems.value.length === 0) {
    alert('선택된 데이터가 없습니다.');
    return;
  }
  isActive.value = true;
  ozParam.value.CSV_DATA = selectedItems.value;
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
  border-bottom: 1px solid #a9a9a9;
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
</style>
