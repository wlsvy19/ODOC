<template>
  <div @keyup.enter="handleSearch">
    <LoadingComponent v-if="isLoading" />
    <SearchDataComponent :headers="searchHeader" v-model="searchData" style="width: 1500px" />
    <InformationComponent style="margin: 0px 0px 5px -5px" message="감지-처리-감지 그룹이 한건의 통행원시 데이터 입니다." />
    <TableComponent
      :itemSize="24"
      :headers="tableHeaders"
      :contents="PassPrimSearchData"
      @update:selectedItems="updateSelectedItems"
      scrollKey="PassPrimScroll"
      :heightOffset="308"
      rowType="1"
    />
  </div>

  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <div class="count-container">
    <span class="total-text">총 건수</span>
    <span><input class="total-count" :value="comma(PassPrimSearchData.length)" disabled /> 건</span>
  </div>
</template>

<script setup>
import { ref, watch, onActivated, toRaw } from 'vue';
import { useI18n } from 'vue-i18n';
import { request, btnHandler, showMessage, createOzDataset, comma } from '@/utils/common';
import { ozAppImageUrl } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { isNumeric, isKorAndNumeric, isTimeFormat, isValidDate, isValidCarDNumber, isValidCarNumber } from '@/utils/validate';
import { useAuthStore } from '@/stores/index';
import dayjs from 'dayjs';

const { t } = useI18n();

const PassPrimSearchData = ref([]);

const tableHeaders = ref([
  { key: 'checkBox', title: '', width: '40', customHeaderClass: 'checkbox-margin' },
  { key: 'ROW_NUMBER', title: '순번', width: '90', excelWidth: 10 },
  { key: 'RNUM11', title: '순번', width: '90', excelWidth: 10, customClass: 'text-bold' },
  { key: 'S_TYPE', title: '종류', width: '100', excelWidth: 10 },
  { key: 'SMALLTIME1', title: '시간', width: '100', excelWidth: 15 },
  { key: 'COMMENT1', title: '내용', width: '140', excelWidth: 25 },
  { key: 'RNUM22', title: '순번', width: '90', excelWidth: 10 },
  { key: 'C_TYPE', title: '종류', width: '100', excelWidth: 10 },
  { key: 'SMALLTIME2', title: '시간', width: '100', excelWidth: 15 },
  { key: 'COMMENT2', title: '내용', width: '1035', excelWidth: 170, customBodyCellStyle: 'PassPrimDtl-COMMENTS-body-cell-style' },
]);

onActivated(() => {
  getWorkNm(searchData.value.WORK_DATE);
});

const workNmOption = ref([]);

const searchHeader = ref([
  { label: '근무일자', key: 'WORK_DATE', type: 'date', dateType: 'day' },
  { label: '근무번호', key: 'WORK_NM', type: 'select', option: workNmOption, width: '213px' },
  { label: '조회시간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '', key: 'SEARCH_START_TIME', type: 'time' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '', key: 'SEARCH_END_TIME', type: 'time' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', width: '150px' },
  { label: '카드번호', key: 'CARD_NO', type: 'input', width: '200px' },
  { label: '처리일련번호', key: 'HAND_SNO', type: 'input', width: '150px' },
]);

const originalSearchData = ref(null);
const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NM: '',
  CAR_NO: '',
  CARD_NO: '',
  HAND_SNO: '',
  START_DATE: dayjs().format('YYYY-MM-DD'),
  SEARCH_START_TIME: dayjs('00:00:00', 'HH:mm:ss').format('HH:mm'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  SEARCH_END_TIME: dayjs('23:59:00', 'HH:mm:ss').format('HH:mm'),
});

watch(
  () => searchData.value.WORK_DATE,
  (newDate, oldDate) => {
    if (newDate !== oldDate) {
      getWorkNm(newDate);
      searchData.value.WORK_NM = '';
      searchData.value.START_DATE = newDate;
      searchData.value.SEARCH_START_TIME = '00:00';
      searchData.value.END_DATE = newDate;
      searchData.value.SEARCH_END_TIME = '23:59';
    }
  },
);

const getWorkNm = async (date) => {
  try {
    const response = await request('post', '/api/work/getWorkNm', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      WORK_DATE: dayjs(date).format('YYYYMMDD'),
    });
    workNmOption.value = response;
    if (response && Array.isArray(response)) {
      workNmOption.value = response.map((item) => ({
        title: item.WORK_NM,
        value: item.WORK_NO,
      }));
    } else {
      workNmOption.value = [];
    }
    workNmOption.value.unshift({
      title: `근무: ${workNmOption.value.length}건`,
      value: '',
    });
  } catch (error) {
    alert(t('WORKNO_ERROR'));
  }
};

watch(
  () => searchData.value.WORK_NM,
  (newVal, oldVal) => {
    if (newVal !== oldVal) {
      getPassPrimDate();
    }
  },
);

const getPassPrimDate = async () => {
  const response = await request('post', '/api/work/getPassPrimDate', {
    IC_CODE: localStorage.getItem('loginIcCode'),
    WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYYMMDD'),
    WORK_NO: searchData.value.WORK_NM,
  });
  if (response) {
    searchData.value.START_DATE = response.WORK_DATE_S;
    const startDate = response.WORK_DATE_S;
    const [sDatePart, sTimePart] = startDate.split(' ');

    const endDate = response.WORK_DATE_E;
    const [eDatePart, eTimePart] = endDate.split(' ');
    searchData.value.START_DATE = sDatePart;
    searchData.value.SEARCH_START_TIME = dayjs(sTimePart, 'HH:mm:ss').format('HH:mm');
    searchData.value.END_DATE = eDatePart;
    searchData.value.SEARCH_END_TIME = dayjs(eTimePart, 'HH:mm:ss').format('HH:mm');
  }
};

const isLoading = ref(false);
const handleSearch = async () => {
  if (!searchData.value.WORK_NM) {
    alert(t('SELECT_WORKNO'));
    return;
  }
  if (searchData.value.CAR_NO && !isValidCarNumber(searchData.value.CAR_NO)) {
    alert(`차량번호는 한글과 숫자와 하이픈(-) 만 입력 할 수 있습니다.`);
    return;
  }
  if (searchData.value.CARD_NO && !isValidCarDNumber(searchData.value.CARD_NO)) {
    alert(`카드번호는 숫자와 하이픈(-) 입력 할 수 있습니다.`);
    return;
  }
  if (searchData.value.HAND_SNO && !isNumeric(searchData.value.HAND_SNO)) {
    alert(`처리일련번호는 숫자만 입력 할 수 있습니다.`);
    return;
  }
  if (!isTimeFormat(searchData.value.SEARCH_START_TIME) || !isTimeFormat(searchData.value.SEARCH_END_TIME)) {
    alert('00:00 ~ 23:59 사이의 숫자만 입력 가능합니다.');
    return;
  }

  const startDate = searchData.value.START_DATE + searchData.value.SEARCH_START_TIME;
  const endDate = searchData.value.END_DATE + searchData.value.SEARCH_END_TIME;
  if (!isValidDate(startDate, endDate)) {
    alert(t('INVALID_DATE'));
    return;
  }

  try {
    isLoading.value = true;
    const startWorkDate = dayjs(searchData.value.START_DATE).format('YYYYMMDD') + searchData.value.SEARCH_START_TIME.replace(':', '');
    const endWorkDate = dayjs(searchData.value.END_DATE).format('YYYYMMDD') + searchData.value.SEARCH_END_TIME.replace(':', '');
    const response = await request('post', '/api/work/getPassPrim', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYYMMDD'),
      WORK_NO: searchData.value.WORK_NM,
      START_WORK_DATE: startWorkDate,
      END_WORK_DATE: endWorkDate,
      CAR_NO: searchData.value.CAR_NO,
      CARD_NO: searchData.value.CARD_NO.replace(/-/g, ''),
      HAND_SNO: searchData.value.HAND_SNO,
    });

    if (response.length === 0) {
      showMessage(t('NO_DATA'), 'error');
    }
    PassPrimSearchData.value = response.map((item, index) => ({
      ...item,
      selected: false,
      originalIndex: index,
    }));
    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));
  } catch (error) {
    alert('통행원시' + t('SEARCH_ERROR'));
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
  if (PassPrimSearchData.value.length === 0) {
    alert(t('NO_DATA_EXCEL'));
    return;
  }
  const selectedData = selectedItems.value;
  if (selectedData.length === 0) {
    alert(t('CHECK_DATA_EXCEL'));
    return;
  }
  if (JSON.stringify(toRaw(searchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert(t('SEARCH_CONDITION_CHANGED'));
    return;
  }

  if (confirm(`[${selectedData.length}]` + t('CONFIRM_EXCEL'))) {
    const excelHeaders = tableHeaders.value
      .filter((header) => header.field !== 'checkBox')
      .map((header) => ({
        title: header.title,
        key: header.key,
        excelWidth: header.excelWidth,
      }));
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, selectedData, '통행원시', '통행원시조회');
  } else {
    showMessage(t('CANCEL_EXCEL'), 'error');
  }
};

const authStore = useAuthStore();
const isActive = ref(false);
const jsonData = ref('');

const handlePrint = async () => {
  try {
    if (PassPrimSearchData.value.length === 0) {
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
      const startWorkDate = dayjs(searchData.value.START_DATE).format('YYYY-MM-DD') + ' ' + searchData.value.SEARCH_START_TIME;
      const endWorkDate = dayjs(searchData.value.END_DATE).format('YYYY-MM-DD') + ' ' + searchData.value.SEARCH_END_TIME;
      const imagePath = await request('post', '/api/common/getImagePath', {
        PRG_CODE: '0103',
      });

      let IC_NAME = '';
      if (authStore.getIcCode === '094') {
        IC_NAME = '광안대교';
      }
      jsonData.value = createOzDataset('/WORK/passPrim.ozr', {
        CSV_DATA: selectedData,
        TITLE_NM: '통행원시 조회',
        IC_NAME: IC_NAME,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYY-MM-DD'),
        WORK_NO: searchData.value.WORK_NM,
        START_WORK_DATE: startWorkDate,
        END_WORK_DATE: endWorkDate,
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
