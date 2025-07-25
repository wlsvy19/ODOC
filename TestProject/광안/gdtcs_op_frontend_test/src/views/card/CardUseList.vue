<template>
  <LoadingComponent v-if="isLoading" />
  <div class="content-container">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" style="width: 1100px" />
    <v-row>
      <v-col cols="9">
        <TableComponent
          :heightOffset="320"
          @grid-click-event="onGridClickEvent"
          :headers="headers"
          :contents="cardUseListData"
          rowType="1"
          scrollKey="cardUseListData"
        />
      </v-col>
      <v-col cols="3" class="vertical-panel">
        <v-img class="detail-carImg" width="100%" height="189" :src="imgSrc" />
        <div style="margin-top: 1px">
          <GridSystemComponent :cols-per-row="1" :headers="detailHeader" :contents="detailContents" tableThWidth="150px" tableHeight="30px" />
        </div>
      </v-col>
    </v-row>
  </div>
  <div class="work-count-container">
    <span class="total-work">[소계] 건수</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalCount)" disabled /> 건</span>
    <span class="total-work">통행요금</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalPassFare)" disabled /> 원</span>
    <span class="total-work">출금액</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalWthdFare)" disabled /> 원</span>
    <span class="total-work">후불보정금액</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalCrctPayFare)" disabled /> 원</span>
    <span class="total-work">후불보정 출금액 합계</span>
    <span><input class="total-count" :value="comma(contentsFoot.allTotalFare)" disabled /> 원</span>
  </div>
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />
</template>

<script setup>
import { ref, onActivated, nextTick, computed } from 'vue';
import { request, btnHandler, comma, createOzDataset, getImage, ImageCategory, getSystemSmallCode, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const authStore = useAuthStore();
const cardTypeOption = ref([]);
const carTypeOption = ref([]);
const violationCodeOption = ref([]);
const cardCompanyOption = ref([]);
const contentsFoot = ref({});

const appCode = computed(() => route.path).value.replace('/', '');

contentsFoot.value = {
  allTotalCount: 0,
  allTotalPassFare: 0,
  allTotalWthdFare: 0,
  allTotalCrctPayFare: 0,
  allTotalFare: 0,
};

const isActive = ref(false);
const jsonData = ref('');

cardTypeOption.value = getSystemSmallCode('071', true, false);
carTypeOption.value = Object.values(
  getSystemSmallCode('171').reduce((acc, item) => {
    if (!acc[item.title]) {
      acc[item.title] = { ...item, value: '' };
    }
    acc[item.title].value = item.title;
    return acc;
  }, {}),
);
carTypeOption.value.unshift({
  title: '전체',
  value: '',
});
violationCodeOption.value = getSystemSmallCode('057', true, false);
cardCompanyOption.value = getSystemSmallCode('316', true, false);

const payHandDivOption = [
  { value: '', title: '전체' },
  { value: '00', title: '정상' },
  { value: '02', title: '비정상' },
];

const imgSrc = ref();

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '100' },
  { title: '근무일자', key: 'WORK_DATE', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '100' },
  { title: '일련번호', key: 'HAND_SNO', width: '100' },
  { title: '처리시간', key: 'HAND_DT', width: '150' },
  { title: '카드번호', key: 'CARD_NO', width: '150' },
  { title: '차종', key: 'CAR_TYPE', width: '100' },
  { title: '위반코드', key: 'VLTN_CODE', width: '150' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '통행요금', key: 'PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금전카드잔액', key: 'WTHD_BEF_FARE', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금액', key: 'WTHD_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '후불보정금액', key: 'CRCT_PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납후카드잔액', key: 'PAY_AFTER_FARE', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '할인율', key: 'PAY_DC_RATE', width: '100' },
  { title: '수납구분', key: 'PAY_DIV', width: '120' },
  { title: '카드종류', key: 'CARD_DIV', width: '130' },
  { title: '카드사구분', key: 'CARD_CO_DIV', width: '100' },
  { title: '차량번호', key: 'CAR_NO', width: '100' },
  { title: '지불처리구분', key: 'PAY_HAND_DIV', width: '100' },
  { title: '카드할인번호', key: 'CARD_DC_NO', width: '100' },
  { title: 'PSAM ID', key: 'PSAM_ID', width: '150' },
  { title: '전자화폐 발행기간', key: 'EMCO_ID', width: '120' },
  { title: '거래 일련번호', key: 'TRAN_SNO', width: '120' },
  { title: '거래총액', key: 'TRAN_TOT_FARE', width: '100' },
  { title: '전자카드차량번호', key: 'ECARD_CAR_NO', width: '120' },
  { title: '단말기차량번호', key: 'OBU_CAR_NO', width: '120' },
]);

const detailHeader = ref([
  { title: '영업소명', key: 'IC_CODE_NM' },
  { title: '근무일자', key: 'WORK_DATE' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '차량번호', key: 'HAND_CAR_NO' },
]);

const searchHeader = ref([
  { label: '사용기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '근무번호', key: 'WORK_NO', type: 'input', valid: 'digit', maxLength: '4' },
  { label: '카드종류', key: 'CARD_TYPE', type: 'select', option: cardTypeOption, width: '130px' },
  { label: '카드사', key: 'CARD_CO_CODE', type: 'select', option: cardCompanyOption, width: '130px' },
  { label: '카드번호', key: 'CARD_NO', type: 'input', valid: 'digit', width: '150px', maxLength: '19' },
  { label: '차종', key: 'CAR_TYPE', type: 'select', option: carTypeOption },
  { label: '전자카드 차량번호', key: 'ECARD_CAR_NO', type: 'input', valid: 'digit|korean', width: '110px', maxLength: '10' },
  { label: '처리 차량번호', key: 'HAND_CAR_NO', type: 'input', width: '110px', maxLength: '10' },
  { label: '단말기 차량번호', key: 'OBU_CAR_NO', type: 'input', width: '110px', maxLength: '10' },
  { label: '지불처리구분', key: 'PAY_HAND_DIV', type: 'select', option: payHandDivOption },
  { label: '위반코드', key: 'VLTN_CODE', type: 'select', option: violationCodeOption, width: '150px' },
  { label: '처리금액없음', key: 'WTHD_FARE', type: 'checkbox' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NO: '',
  CARD_TYPE: '',
  CARD_CO_CODE: '',
  CARD_NO: '',
  CAR_TYPE: '',
  ECARD_CAR_NO: '',
  HAND_CAR_NO: '',
  OBU_CAR_NO: '',
  PAY_HAND_DIV: '',
  VLTN_CODE: '',
  WTHD_FARE: false,
});

const cardUseListData = ref([]);

const isLoading = ref(false);

const detailContents = ref({});
const onGridClickEvent = (selectItem) => {
  detailContents.value = {
    WORK_DATE: selectItem.WORK_DATE,
    WORK_NO: selectItem.WORK_NO,
    HAND_CAR_NO: selectItem.CAR_NO,
    IC_CODE_NM: selectItem.IC_CODE_NM,
  };
  imgSrc.value = getImage(ImageCategory.CAR, {
    ...detailContents.value,
    ...{ IC_CODE: authStore.getIcCode, WORK_DATE: selectItem.WORK_DATE.replaceAll('-', ''), HAND_SNO: selectItem.HAND_SNO },
  });
  // imgSrc.value =
  // 'http://localhost:9081/api/video/etcs/getImage?IC_CODE=094&tolling_time=20231128045539&work_number=6201&processing_number=196&filepathStartOffset=18';
};
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getCardUseList', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    const allTotalPassFare = ref(0);
    const allTotalWthdFare = ref(0);
    const allTotalCrctPayFare = ref(0);
    const allTotalCrctBeforePayFare = ref(0);
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    data.map((obj) => {
      obj.PAY_AFTER_FARE = comma(obj.PAY_AFTER_FARE);
      allTotalPassFare.value += Number(obj.IPASS_FARE);
      allTotalWthdFare.value += Number(obj.IWTHD_FARE);
      allTotalCrctPayFare.value += Number(obj.ICRCT_PAY_FARE);
      allTotalCrctBeforePayFare.value += Number(obj.ICRCT_BEFORE_FARE);
    });
    cardUseListData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    contentsFoot.value = {
      allTotalCount: comma(data.length),
      allTotalPassFare: comma(allTotalPassFare.value),
      allTotalWthdFare: comma(allTotalWthdFare.value),
      allTotalCrctPayFare: comma(allTotalCrctPayFare.value),
      allTotalFare: comma(allTotalWthdFare.value + allTotalCrctPayFare.value - allTotalCrctBeforePayFare.value),
    };
  } catch (error) {
    alert(`카드사용내역조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (cardUseListData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, cardUseListData.value, '카드사용내역조회', '카드사용내역조회', [
      `[소계] 건수: ${contentsFoot.value.allTotalCount} 건  통행요금: ${contentsFoot.value.allTotalPassFare} 원 출금액: ${contentsFoot.value.allTotalWthdFare} 원 후불보정금액: ${contentsFoot.value.allTotalCrctPayFare} 원 출금액 합계: ${contentsFoot.value.allTotalFare} 원`,
    ]);
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (cardUseListData.value.length === 0) {
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
    jsonData.value = createOzDataset('/CARD/cardUseList.ozr', {
      CSV_DATA: cardUseListData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      WORK_NO: '보고서번호',
      TITLE_NM: '카드사용내역조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
      TOTAL_COUNT: contentsFoot.value.allTotalCount + '건',
      TOTAL_PASSFARE: contentsFoot.value.allTotalPassFare + '원',
      TOTAL_WTHDFARE: contentsFoot.value.allTotalWthdFare + '원',
      TOTAL_CRCTFARE: contentsFoot.value.allTotalCrctPayFare + '원',
      TOTAL_FARE: contentsFoot.value.allTotalFare + '원',
    });
    isActive.value = true;
  } catch (error) {
    alert(`카드사용내역조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
.content-container {
  border-bottom: 1px solid #a9a9a9;
}
.vertical-panel {
  border-left: 1px solid #a9a9a9;
  border-top: 1px solid #a9a9a9;
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
