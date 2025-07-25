<template>
  <LoadingComponent :message="loadingState.msg" v-if="loadingState.isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  </div>
  <TableComponent
    scrollKey="EtcIncome"
    :headers="headers"
    :contents="contents"
    :heightOffset="heightOffset"
    @update:selectedItems="updateSelectedItems"
    @grid-dbl-click-event="onGridDblClickEvent"
    :footerContents="footerContents"
  />

  <v-dialog v-model="dialog" max-width="300px" style="z-index: 2000" @keyup.enter="btnSave">
    <v-card ref="card">
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title @mousedown="onMouseDown" style="cursor: move">기타수입관리등록</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <InputFormGrid
            v-model="dialogInputContents"
            :headers="dialogInputHeaders"
            :cols-per-row="1"
            :table-header-width="'30%'"
            :selectChanged="resetInputData"
            v-model:is-valid="isValidInputForm"
            v-model:input-form="inputForm"
          />
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="btnSave">저장</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <OZReportDialog v-model:isActive="isActiveViewer" :jsonData="jsonData" />
</template>

<script setup>
import { ref, onActivated, toRaw } from 'vue';
import { request, btnHandler, createOzDataset, comma, showMessage, getSystemSmallCode, getCondition } from '@/utils/common';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { InputRules } from '@/utils/rules';
import dayjs from 'dayjs';

const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer
 */
const heightOffset = 189 + 36 + 29 + 30;

const inTypeOption = ref([]);
const depositOption = ref([]);
const withdrawalOption = ref([]);
const dwTypeOption = ref([]);

const loadingState = ref({
  isLoading: false,
  msg: '',
});

const contents = ref([]);

const originalSearchData = ref(null);
const currentSearchData = ref(null);

const card = ref(null);

const dialog = ref(false);
const dialogInputContents = ref({});
const dialogInType = ref([]);
const dialogDwType = ref([]);

const isValidInputForm = ref(true);
const inputForm = ref();

const isActiveViewer = ref(false);
const jsonData = ref('');

onActivated(async () => {
  inTypeOption.value = getSystemSmallCode('120', true, true);
  dwTypeOption.value = getSystemSmallCode('804', true, true);

  dialogDwType.value = getSystemSmallCode('804', false, true);

  depositOption.value = inTypeOption.value.filter((val) => ['01', '02', '03', '09'].includes(val.value));
  withdrawalOption.value = inTypeOption.value.filter((val) => ['11', '12', '13', '19'].includes(val.value));
});

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '입출금구분', key: 'DW_TYPE', type: 'select', option: dwTypeOption },
  { label: '계정구분', key: 'IN_TYPE', type: 'select', option: inTypeOption, width: '160px' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', width: '120px', maxLength: '9' },
  { label: '비고', key: 'NOTE', type: 'input', width: '170px', maxLength: '100' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  DW_TYPE: '',
  IN_TYPE: '',
  CAR_NO: '',
  NOTE: '',
});

const headers = ref([
  { key: 'checkBox', title: '', width: '40' },
  { title: '순번', key: 'ROW_NUMBER', width: '50' },
  { title: '처리일자', key: 'IN_DATE_DP', width: '100' },
  { title: '처리시간', key: 'MDFY_DT_DP', width: '80' },
  { title: '처리근무자명', key: 'WORKER_DP', width: '150' },
  { title: '입출금구분', key: 'DW_TYPE_DP', width: '100' },
  { title: '계정구분', key: 'IN_TYPE_DP', width: '150' },
  { title: '입금', key: 'D_MNY_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '환불', key: 'W_MNY_DP', width: '100', customBodyCellStyle: 'body-row-style-fare' },
  { title: '고객명', key: 'CUST_NM', width: '100' },
  { title: '차량번호', key: 'CAR_NO', width: '150' },
  { title: '전화번호', key: 'TEL', width: '150' },
  { title: '비고', key: 'NOTE', width: '200' },
]);

const footerContents = ref([
  { title: '총 건수', value: '0', unit: '건', width: '100' },
  { title: '입금액', value: '0', unit: '원' },
  { title: '출금액', value: '0', unit: '원' },
]);

const handleSearch = async () => {
  loadingState.value = { msg: '기타수입관리 조회 중 입니다.', isLoading: true };
  try {
    const response = await request('post', 'api/office/getEtcIncomeList', {
      START_DATE: searchData.value.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.value.END_DATE.replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
      DW_TYPE: searchData.value.DW_TYPE,
      IN_TYPE: searchData.value.IN_TYPE,
      CAR_NO: searchData.value.CAR_NO,
      NOTE: searchData.value.NOTE,
    });

    contents.value = response;

    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));

    //소계
    const totalRows = contents.value.length;
    let totalDpstMny = 0;
    let totalWthdMny = 0;

    contents.value.forEach((item) => {
      totalDpstMny += Number(item.D_MNY) || 0;
      totalWthdMny += Number(item.W_MNY) || 0;
    });
    footerContents.value[0].value = comma(totalRows);
    footerContents.value[1].value = comma(totalDpstMny);
    footerContents.value[2].value = comma(totalWthdMny);
  } catch (error) {
    alert(`기타수입 데이터 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
  }
};

const dialogInputHeaders = ref([
  { title: '처리일자', key: 'IN_DATE', option: 'date', type: 'day' },
  { title: '관리자', key: 'ADMIN_ID', disabled: true },
  {
    title: '입출금구분',
    key: 'DW_TYPE',
    option: 'select',
    selectItem: dialogDwType,
  },
  {
    title: '계정구분',
    key: 'IN_TYPE',
    option: 'select',
    selectItem: dialogInType,
  },
  { title: '고객명', key: 'CUST_NM', required: 'Y', rules: InputRules.custNmRule },
  { title: '차량번호', key: 'CAR_NO', required: 'Y', rules: InputRules.carNoRule },
  { title: '금액', key: 'MNY', option: 'numberInput', required: 'Y' },
  { title: '비고(확인용)', key: 'NOTE', rules: InputRules.noteRule },
]);

const resetInputData = (value, key) => {
  console.log(`Select box with key: ${key} changed to ${value}`);

  switch (dialogInputContents.value.DW_TYPE) {
    case '0':
      dialogInType.value = depositOption.value;
      break;
    case '1':
      dialogInType.value = withdrawalOption.value;
      break;
  }

  if (key === 'ADD') {
    dialogInputContents.value.IN_DATE = dayjs().format('YYYY-MM-DD');
    dialogInputContents.value.ADMIN_ID = authStore.getWorkerNo;
    dialogInputContents.value.DW_TYPE = dialogDwType.value[0].value;
    dialogInputContents.value.IN_TYPE = dialogInType.value[0].value;
    dialogInputContents.value.CUST_NM = '';
    dialogInputContents.value.CAR_NO = '';
    dialogInputContents.value.MNY = 0;
    dialogInputContents.value.NOTE = '';
  }

  if (key === 'DW_TYPE') {
    console.log('입출금구분이 변경되었습니다.');
    dialogInputContents.value.IN_TYPE = dialogInType.value[0].value;
  } else if (key === 'IN_TYPE') {
    console.log('계정구분 변경되었습니다.');
  }
};

const handleAdd = () => {
  dialog.value = true;
  dialogInputContents.value.DW_TYPE = '0';
  dialogInputContents.value.MDFY_DT = '';
  dialogInputContents.value.MDFY = 'N';
  resetInputData('', 'ADD');
};

const onGridDblClickEvent = async (index, item) => {
  dialog.value = true;
  dialogInputContents.value = { ...item };
  dialogInputContents.value.IN_DATE = dayjs(dialogInputContents.value.IN_DATE).format('YYYY-MM-DD');
  dialogInputContents.value.MDFY = 'Y';
  resetInputData('', 'MDFY');
};

const btnSave = async () => {
  console.log('dialogInputContents.value', dialogInputContents.value);

  if (!isValidInputForm.value) {
    alert(`${inputForm.value.errors[0].errorMessages[0]}`);
    return;
  }

  const requestURL = dialogInputContents.value.MDFY === 'N' ? 'api/office/addEtcIncome' : 'api/office/setEtcIncome';

  try {
    const response = await request('post', requestURL, {
      ...dialogInputContents.value,
      ...{
        IN_DATE: dialogInputContents.value['IN_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
        WORKER_NO: authStore.getWorkerNo,
      },
    });
    alert(response.ERROR_MSG);
    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      handleSearch();
    }
  } catch (error) {
    alert(`기타수입 데이터 처리 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const selectedItems = ref([]);

const updateSelectedItems = (item) => {
  selectedItems.value = item;
};

const handleDelete = async () => {
  if (selectedItems.value.length === 0) {
    alert('삭제할 데이터를 선택해주세요.');
    return;
  }
  console.log('selectedItems.value', selectedItems.value);

  if (confirm('사무실 수입을 삭제하시겠습니까?')) {
    const result = [];
    selectedItems.value.forEach((item) => {
      result.push({
        ...item,
        IC_CODE: authStore.getIcCode,
      });
    });
    console.log('result', result);

    try {
      const response = await request('post', 'api/office/delEtcIncome', result);
      alert(response.ERROR_MSG);

      handleSearch();
      selectedItems.value = [];
    } catch (error) {
      alert(`기타수입 데이터 삭제 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
    }
  } else {
    showMessage('삭제 취소', 'warning');
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
      `총 건수: ${footerContents.value[0].value} 건  입금액: ${footerContents.value[1].value} 원  출금액: ${footerContents.value[2].value} 원`,
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
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

  try {
    loadingState.value = { msg: '보고서 출력 중 입니다.', isLoading: true };
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: '0209',
    });
    jsonData.value = createOzDataset('/OFFICE/etcIncome.ozr', {
      CSV_DATA: selectedItems.value,
      START_DATE: reportParam.value['START_DATE'],
      END_DATE: reportParam.value['END_DATE'],
      DW_TYPE: reportParam.value['DW_TYPE'],
      IN_TYPE: reportParam.value['IN_TYPE'],
      CAR_NO: reportParam.value['CAR_NO'],
      NOTE: reportParam.value['NOTE'],
      REPORT_NM: '기타수입관리',
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

btnHandler({
  Search: handleSearch,
  Add: handleAdd,
  Delete: handleDelete,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped>
/* UI */
.v-card {
  width: 100%;
}
</style>
