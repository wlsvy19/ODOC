<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" style="width: 1100px" />
  <TableComponent
    :itemSize="24"
    :headers="tableHeaders"
    :contents="CenterInterSearchData"
    @update:selectedItems="updateSelectedItems"
    scrollKey="CenterInterScroll"
    :heightOffset="285"
    rowType="1"
  />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <div class="count-container">
    <span class="total-text">총 건수</span>
    <span><input class="total-count" :value="comma(CenterInterSearchData.length)" disabled /> 건</span>
  </div>
</template>

<script setup>
import { ref, toRaw, onActivated } from 'vue';
import { useI18n } from 'vue-i18n';
import { request, btnHandler, showMessage, createOzDataset, comma } from '@/utils/common';
import { ozAppImageUrl } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { isValidDate } from '@/utils/validate';
import { useAuthStore } from '@/stores/index';
import dayjs from 'dayjs';

const { t } = useI18n();

onActivated(async () => {
  await getFileDivNm();
});

const CenterInterSearchData = ref([]);

const tableHeaders = ref([
  { key: 'checkBox', title: '', width: '40', customHeaderClass: 'checkbox-margin' },
  { key: 'ROW_NUMBER', title: '순번', width: '90', excelWidth: 10 },
  { key: 'CALC_CO_CODE', title: '기관', width: '80', excelWidth: 10 },
  { key: 'CALC_DATE', title: '정산일자', width: '150', excelWidth: 20 },
  { key: 'INTER_DATE', title: '전송일시', width: '250', excelWidth: 35 },
  { key: 'FILE_NM', title: '파일명', width: '300', excelWidth: 40 },
  { key: 'FILE_DIV', title: '파일종류', width: '250', excelWidth: 20 },
  { key: 'HAND_CNT', title: '건수', width: '150', excelWidth: 20 },
  { key: 'HAND_IC_CODE', title: '처리영업소', width: '100', excelWidth: 10 },
  { key: 'WORKER_NM', title: '처리근무자', width: '150', excelWidth: 20 },
  { key: 'INTER_DIV', title: '송수신구분', width: '140', excelWidth: 20 },
  { key: 'INTER_RSLT', title: '전송결과', width: '186', excelWidth: 20 },
]);

const trnrcpRsltOption = [
  { value: '', title: '전체' },
  { value: '0', title: '대기' },
  { value: '1', title: '성공' },
  { value: '2', title: '실패' },
];

const fileDivOption = ref([{ value: '', title: '전체' }]);

const getFileDivNm = async () => {
  const response = await request('post', '/api/inter/getFileDivNm', {
    CODELC: '074', // 대분류코드: 074
    CODESC_USE_YN: 'Y',
  });

  const options = response.map((item) => ({
    value: item.CODESC,
    title: item.CODESC_NM,
  }));

  fileDivOption.value = [{ value: '', title: '전체' }, ...options];
};

const trnrcpDivOption = [
  { value: '', title: '전체' },
  { value: 'S', title: '송신' },
  { value: 'R', title: '수신' },
];

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '전송결과', key: 'TRNRCP_RSLT', type: 'select', option: trnrcpRsltOption, width: '100px' },
  { label: '파일종류', key: 'FILE_DIV', type: 'select', option: fileDivOption, width: '150px' },
  { label: '송수신구분', key: 'TRNRCP_DIV', type: 'select', option: trnrcpDivOption, width: '100px' },
]);

const originalSearchData = ref(null);
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  TRNRCP_RSLT: '',
  FILE_DIV: '',
  TRNRCP_DIV: '',
});

const isLoading = ref(false);
const handleSearch = async () => {
  if (!isValidDate(searchData.value.START_DATE, searchData.value.END_DATE)) {
    alert(t('INVALID_DATE'));
    return;
  }
  try {
    isLoading.value = true;
    const response = await request('post', '/api/inter/getCenterInter', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      START_DATE: dayjs(searchData.value.START_DATE).format('YYYYMMDD'),
      END_DATE: dayjs(searchData.value.END_DATE).format('YYYYMMDD'),
      TRNRCP_RSLT: searchData.value.TRNRCP_RSLT,
      FILE_DIV: searchData.value.FILE_DIV,
      TRNRCP_DIV: searchData.value.TRNRCP_DIV,
    });

    if (response.length === 0) {
      showMessage(t('NO_DATA'), 'error');
    }
    CenterInterSearchData.value = response.map((item, index) => ({
      ...item,
      selected: false,
      originalIndex: index,
    }));

    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));
  } catch (error) {
    alert('센터송수신내역' + t('SEARCH_ERROR'));
  } finally {
    isLoading.value = false;
  }
};

const selectedItems = ref([]);
const updateSelectedItems = (items) => {
  selectedItems.value = items;
};

const handleExcel = async () => {
  const row = 1;
  if (CenterInterSearchData.value.length === 0) {
    alert(t('NO_DATA_EXCEL'));
    return;
  }
  if (JSON.stringify(toRaw(searchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert(t('SEARCH_CONDITION_CHANGED'));
    return;
  }

  const selectedData = selectedItems.value;
  if (selectedData.length === 0) {
    alert(t('CHECK_DATA_EXCEL'));
    return;
  }

  if (confirm(`[${selectedData.length}]` + t('CONFIRM_EXCEL'))) {
    try {
      const excelHeaders = tableHeaders.value
        .filter((header) => header.field !== 'checkBox')
        .map((header) => ({
          title: header.title,
          key: header.key,
          excelWidth: header.excelWidth,
        }));
      excelDownload(row, searchHeader.value, searchData.value, excelHeaders, selectedData, '센터송수신', '센터송수신내역조회');
    } catch (error) {
      alert(t('INCORRECT_PARAM_EXCEL'));
    }
  } else {
    showMessage(t('CANCEL_EXCEL'), 'error');
  }
};

const authStore = useAuthStore();
const isActive = ref(false);
const jsonData = ref('');

const handlePrint = async () => {
  let TRNRCP_RSLT = '';
  let TRNRCP_DIV = '';

  if (searchData.value.TRNRCP_RSLT === '0') {
    TRNRCP_RSLT = '대기';
  } else if (searchData.value.TRNRCP_RSLT === '1') {
    TRNRCP_RSLT = '성공';
  } else if (searchData.value.TRNRCP_RSLT === '2') {
    TRNRCP_RSLT = '실패';
  } else {
    TRNRCP_RSLT = '전체';
  }

  if (searchData.value.TRNRCP_DIV === 'S') {
    TRNRCP_DIV = '송신';
  } else if (searchData.value.TRNRCP_DIV === 'R') {
    TRNRCP_DIV = '수신';
  } else {
    TRNRCP_DIV = '전체';
  }

  try {
    if (CenterInterSearchData.value.length === 0) {
      alert(t('NO_DATA_PRINT'));
      return;
    }
    const selectedData = selectedItems.value;
    if (selectedData.length === 0) {
      alert(t('CHECK_DATA_PRINT'));
      return;
    }
    if (JSON.stringify(toRaw(searchData.value)) !== JSON.stringify(originalSearchData.value)) {
      alert(t('SEARCH_CONDITION_CHANGED'));
      return;
    }

    if (confirm(`[${selectedData.length}]` + t('CONFIRM_PRINT'))) {
      isLoading.value = true;
      const startWorkDate = dayjs(searchData.value.START_DATE).format('YYYY-MM-DD');
      const endWorkDate = dayjs(searchData.value.END_DATE).format('YYYY-MM-DD');
      const imagePath = await request('post', '/api/common/getImagePath', {
        PRG_CODE: '0103',
      });

      let IC_NAME = '';
      if (authStore.getIcCode === '094') {
        IC_NAME = '광안대교';
      }
      jsonData.value = createOzDataset('/INTER/centerInter.ozr', {
        CSV_DATA: selectedItems.value,
        TITLE_NM: '센터송수신내역 조회',
        // IC_CODE: authStore.getIcCode,
        IC_NAME: IC_NAME,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYY-MM-DD'),
        WORK_NO: searchData.value.WORK_NM,
        START_WORK_DATE: startWorkDate,
        END_WORK_DATE: endWorkDate,
        TRNRCP_RSLT: TRNRCP_RSLT,
        TRNRCP_DIV: TRNRCP_DIV,
      });
      isActive.value = true;
    } else {
      showMessage(t('CANCEL_PRINT'), 'error');
    }
  } catch (error) {
    alert('센터송수신내역' + t('REPORT_ERROR'));
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
.count-container {
  border-top: 1px solid #d3d3d3;
  font-size: 12px;
}

.total-text {
  margin-left: 10px;
}

.total-count {
  width: 80px;
  text-align: center;
  margin: 5px 0px 0px 10px;
  border-radius: 3px;
  border: 1px solid #d3d3d3;
}
</style>
