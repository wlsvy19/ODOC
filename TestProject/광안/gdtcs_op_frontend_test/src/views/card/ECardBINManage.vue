<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch">
    <template v-slot:header_btn>
      <v-btn size="small" @click="binCodeAdd">BIN코드 등록</v-btn>
    </template>
  </SearchDataComponent>
  <TableComponent
    :heightOffset="258"
    :headers="headers"
    @grid-dbl-click-event="onDoubleClickGridRow"
    @grid-click-event="onGridClickEvent"
    :contents="eCardBINManageData"
    rowType="1"
    scrollKey="eCardBINManageData"
  />
  <v-dialog v-model="dialogBin" max-width="700px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>BIN코드 등록</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-file-input
            variant="solo-filled"
            prepend-icon="mdi-file-excel"
            max-width="300"
            accept=".xlsx"
            label="엑셀파일 등록"
            v-model="fileBinCode" />
          <TableComponent rowType="1" :heightOffset="500" scrollKey="fileBinCodeData" :headers="fileHeaders" :contents="fileBinCodeData"
        /></v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="btnAddFile">저장</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogBinClose">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="dialog" max-width="300px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>BIN코드 {{ save }}</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <InputFormGrid
            v-model="dialogInputContents"
            :headers="save === '추가' ? dialogInputHeaderAdd : dialogInputHeaderUpdate"
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
        <v-btn variant="flat" color="blue darken-1" text @click="btnSave">{{ save }}</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, onActivated, nextTick, watch } from 'vue';
import { request, btnHandler, getSystemSmallCode } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';
import ExcelJS from 'exceljs';
import { InputRules } from '@/utils/rules';

const authStore = useAuthStore();
const cardCompanyCodeOption = ref([]);
const dialogCardCoCompany = ref([]);

const fileBinCode = ref(null);
const fileBinCodeData = ref([]);
const isLoading = ref(false);
const save = ref('');
const isValidInputForm = ref(true);

cardCompanyCodeOption.value = getSystemSmallCode('316', true);
dialogCardCoCompany.value = getSystemSmallCode('316');

const workbook = new ExcelJS.Workbook();
watch(
  () => fileBinCode.value,
  () => {
    if (fileBinCode.value != null) {
      fileBinCodeData.value = [];
      const reader = new FileReader();
      reader.onload = function (e) {
        const buffer = e.target.result;

        workbook.xlsx
          .load(buffer)
          .then((workbook) => {
            workbook.eachSheet((sheet, id) => {
              sheet.eachRow((row, rowIndex) => {
                if (rowIndex != 1) {
                  fileBinCodeData.value = [
                    ...fileBinCodeData.value,
                    {
                      ROW_SEQ: rowIndex - 1,
                      CARD_CO_CODE: row.values[1],
                      CARD_CO_NM: row.values[2],
                      BIN_CODE: row.values[3],
                      NEW_OLD_DIV: row.values[4],
                      USE_DIV: row.values[5],
                    },
                  ];
                }
              });
            });
          })
          .catch((error) => {
            alert(` 엑셀파일 등록 중 에러가 발생했습니다. \n문제가 지속된다면 관리자한테 문의하세요.`);
            console.error('Error loading workbook:', error);
          });
      };

      reader.onerror = function (e) {
        alert(` 엑셀파일을 읽는 과정에서 에러가 발생했습니다. \n문제가 지속된다면 관리자한테 문의하세요.`);
        console.error('FileReader error:', e);
      };
      reader.readAsArrayBuffer(fileBinCode.value);
    }
  },
  {
    immediate: true,
  },
);
const headers = ref([
  { title: '순번', key: 'ROW_SEQ', width: '100' },
  { title: '카드사코드', key: 'CARD_CO_CODE', width: '200' },
  { title: '카드사명', key: 'CARD_CO_NM', width: '200' },
  { title: 'BIN코드', key: 'BIN_CODE', width: '200' },
  { title: 'BIN타입', key: 'NEW_OLD_DIV', width: '100' },
  { title: '사용유무', key: 'USE_DIV', width: '100' },
]);
const fileHeaders = ref([
  { title: '순번', key: 'ROW_SEQ', width: '80' },
  { title: '카드사코드', key: 'CARD_CO_CODE', width: '130' },
  { title: '카드사명', key: 'CARD_CO_NM', width: '130' },
  { title: 'BIN코드', key: 'BIN_CODE', width: '100' },
  { title: 'BIN타입', key: 'NEW_OLD_DIV', width: '100' },
  { title: '사용유무', key: 'USE_DIV', width: '90' },
]);
const newOldDivoption = [
  { value: '', title: '전체' },
  { value: '1', title: '신' },
  { value: '0', title: '구' },
];

const useDivOption = [
  { value: '', title: '전체' },
  { value: '1', title: '사용' },
  { value: '0', title: '불가' },
];

const searchHeader = ref([
  { label: '카드사', key: 'CARD_CO_CODE', type: 'select', option: cardCompanyCodeOption, width: '130px' },
  { label: 'BIN코드', key: 'BIN_CODE', type: 'input', valid: 'digit', maxLength: '8' },
  { label: 'BIN타입', key: 'NEW_OLD_DIV', type: 'select', option: newOldDivoption },
  { label: '사용유무', key: 'USE_DIV', type: 'select', option: useDivOption },
]);
const searchData = ref({
  CARD_CO_CODE: '',
  BIN_CODE: '',
  NEW_OLD_DIV: '',
  USE_DIV: '',
});

const eCardBINManageData = ref([]);

const selectValue = ref({});

const onGridClickEvent = (selectItem) => {
  selectValue.value = {
    CARD_CO_CODE: selectItem.CARD_CO_CODE,
    CARD_CO_NM: selectItem.CARD_CO_NM,
    BIN_CODE: selectItem.BIN_CODE,
    NEW_OLD_DIV: selectItem.INEW_OLD_DIV,
    USE_DIV: selectItem.IUSE_DIV,
  };
};

const onDoubleClickGridRow = async () => {
  save.value = '수정';
  dialog.value = true;
  dialogInputContents.value.CARD_CO_CODE = selectValue.value.CARD_CO_CODE;
  dialogInputContents.value.BIN_CODE = selectValue.value.BIN_CODE;
  dialogInputContents.value.CARD_CO_NM = selectValue.value.CARD_CO_NM;
  dialogInputContents.value.NEW_OLD_DIV = selectValue.value.NEW_OLD_DIV;
  dialogInputContents.value.USE_DIV = selectValue.value.USE_DIV;
};

const newOldDivDialog = ref([
  { value: '1', title: '신' },
  { value: '0', title: '구' },
]);
const useDivDialog = ref([
  { value: '1', title: '사용' },
  { value: '0', title: '불가' },
]);

const dialog = ref(false);
const dialogBin = ref(false);

const dialogInputHeaderUpdate = ref([
  { title: '카드사코드', key: 'CARD_CO_CODE', disabled: true },
  { title: '카드사명', key: 'CARD_CO_NM', disabled: true },
  { title: 'BIN코드', key: 'BIN_CODE', disabled: true },
  { title: 'BIN타입', key: 'NEW_OLD_DIV', option: 'select', selectItem: newOldDivDialog },
  { title: '사용유무', key: 'USE_DIV', option: 'select', selectItem: useDivDialog },
]);

const dialogInputHeaderAdd = ref([
  { title: '카드사코드', key: 'CARD_CO_CODE', disabled: true },
  { title: '카드사명', key: 'CARD_CO_CODE', required: 'Y', option: 'select', selectItem: dialogCardCoCompany },
  { title: 'BIN코드', key: 'BIN_CODE', required: 'Y', option: 'input', rules: InputRules.binRule },
  { title: 'BIN타입', key: 'NEW_OLD_DIV', option: 'select', selectItem: newOldDivDialog },
  { title: '사용유무', key: 'USE_DIV', option: 'select', selectItem: useDivDialog },
]);
const dialogInputContents = ref({});

const resetInputData = (value) => {
  if (value === 'init') {
    dialogInputContents.value.CARD_CO_CODE = '';
    dialogInputContents.value.CARD_CO_NM = '';
    dialogInputContents.value.BIN_CODE = '';
    dialogInputContents.value.NEW_OLD_DIV = '1';
    dialogInputContents.value.USE_DIV = '1';
  }
};

const inputForm = ref();

const btnSave = async () => {
  try {
    if (dialogInputContents.value.CARD_CO_CODE === '' || dialogInputContents.value.BIN_CODE === '') {
      alert(`필수 항목이 비어있습니다.`);
      return false;
    }
    if (!isValidInputForm.value) {
      alert(`${inputForm.value.errors[0].errorMessages[0]}`);
      return;
    }
    const response = await request('post', 'api/card/addEcardBINManage', {
      CARD_CO_CODE: dialogInputContents.value.CARD_CO_CODE,
      CARD_CO_NM: dialogInputContents.value.CARD_CO_NM,
      BIN_CODE: dialogInputContents.value.BIN_CODE,
      NEW_OLD_DIV: dialogInputContents.value.NEW_OLD_DIV,
      USE_DIV: dialogInputContents.value.USE_DIV,
      DIV: save.value == '추가' ? 'ADD' : 'UPDATE',
    });
    alert(response.ERROR_MSG);
    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      handleSearch();
    }
  } catch (error) {
    alert(`BIN 코드 저장 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const btnAddFile = async () => {
  try {
    if (fileBinCodeData.value == '') {
      alert(`추가할 데이터가 없습니다.`);
      return false;
    }

    const response = await request('post', 'api/card/addBinFile', {
      ...fileBinCodeData.value,
    });
    alert(response.ERROR_MSG);
    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      fileBinCodeData.value = [];
      fileBinCode.value = null;
    }
  } catch (error) {
    alert(` BIN코드 등록 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const binCodeAdd = () => {
  dialogBin.value = true;
};

const dialogBinClose = () => {
  dialogBin.value = false;
  fileBinCode.value = null;
  fileBinCodeData.value = [];
};
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getEcardBINManage', {
      ...searchData.value,
      ...{ IC_CODE: authStore.getIcCode },
    });

    eCardBINManageData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    selectValue.value = {};
  } catch (error) {
    alert(`전자카드 BIN 정보 관리 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleAdd = () => {
  save.value = '추가';
  dialog.value = true;
  resetInputData('init');
};

const handleDelete = async () => {
  try {
    if (Object.keys(selectValue.value).length === 0) {
      alert(`삭제할 데이터를 선택해주세요`);
      return;
    }

    if (confirm(`전자카드 BIN을 삭제하시겠습니까?`)) {
      const response = await request('post', 'api/card/delEcardBINManage', {
        ...selectValue.value,
      });
      alert(response.ERROR_MSG);
      handleSearch();
    }
  } catch (error) {
    alert(`전자카드 BIN 정보 관리 삭제 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const handleExcel = () => {
  const row = 1;
  if (eCardBINManageData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      eCardBINManageData.value,
      '전자카드 BIN 정보 관리',
      '전자카드 BIN 정보 관리',
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

btnHandler({
  Search: handleSearch,
  Add: handleAdd,
  Delete: handleDelete,
  Excel: handleExcel,
});
</script>

<style scoped></style>
