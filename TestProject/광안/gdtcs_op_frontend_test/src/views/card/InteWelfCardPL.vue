<template>
  <LoadingComponent v-if="isLoading" />
  <InformationComponent style="margin: 4px 0px 0px 0px" message=" 조회조건을 입력하지 않으면 조회 개수가 제한됩니다." icon-type="information" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="276"
    :headers="headers"
    @grid-dbl-click-event="onDoubleClickGridRow"
    @grid-click-event="onGridClickEvent"
    :contents="inteWelfCardPLData"
    rowType="1"
    scrollKey="inteWelfCardPLData"
  />
  <v-dialog v-model="dialog" max-width="300px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>통합복지카드 P/L {{ save }}</v-toolbar-title>
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
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />
</template>

<script setup>
import { ref, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset, getCondition } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { InputRules } from '@/utils/rules';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const authStore = useAuthStore();
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');
const save = ref('');
const isValidInputForm = ref(true);
const inputForm = ref();

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '100' },
  { title: '구분', key: 'DIV', width: '200' },
  { title: '카드번호', key: 'XCARD_NO', width: '200' },
  { title: '차량번호', key: 'INST_CAR_NO', width: '200' },
]);

const searchHeader = ref([
  { label: '카드번호', key: 'XCARD_NO', type: 'input', valid: 'digit', maxLength: '19', width: '150px' },
  { label: '차량번호', key: 'INST_CAR_NO', type: 'input', valid: 'digit|korean', width: '110px', maxLength: '10' },
]);

const searchData = ref({
  XCARD_NO: '',
  INST_CAR_NO: '',
});

const selectValue = ref({});

const onGridClickEvent = (selectItem) => {
  selectValue.value = {
    INST_CAR_NO: selectItem.INST_CAR_NO,
    XCARD_NO: selectItem.XCARD_NO.replaceAll('-', ''),
  };
};

const inteWelfCardPLData = ref([]);

const dialog = ref(false);

const dialogInputHeaderUpdate = ref([
  { title: '카드번호', key: 'XCARD_NO', disabled: true },
  { title: '차량번호', key: 'INST_CAR_NO', required: 'Y', rules: InputRules.carNoRule },
]);
const dialogInputHeaderAdd = ref([
  { title: '카드번호', key: 'XCARD_NO', required: 'Y', rules: InputRules.cardNoRule },
  { title: '차량번호', key: 'INST_CAR_NO', required: 'Y', rules: InputRules.carNoRule },
]);

const dialogInputContents = ref({});

const resetInputData = (value) => {
  if (value === 'init') {
    dialogInputContents.value.INST_CAR_NO = '';
    dialogInputContents.value.XCARD_NO = '';
  }
};

const btnSave = async () => {
  try {
    if (dialogInputContents.value.INST_CAR_NO === '' || dialogInputContents.value.XCARD_NO === '') {
      alert(`필수항목이 비어있습니다.`);
      return false;
    }
    if (!isValidInputForm.value) {
      alert(`${inputForm.value.errors[0].errorMessages[0]}`);
      return;
    }
    const response = await request('post', 'api/card/addIntewelfCardPL', {
      INST_CAR_NO: dialogInputContents.value.INST_CAR_NO,
      XCARD_NO: dialogInputContents.value.XCARD_NO.replaceAll('-', ''),
      DIV: save.value == '추가' ? 'ADD' : 'UPDATE',
    });
    alert(response.ERROR_MSG);

    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      handleSearch();
    }
  } catch (error) {
    alert(`통합복지카드 P/L 정보 저장 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const onDoubleClickGridRow = async () => {
  save.value = '수정';
  dialog.value = true;
  dialogInputContents.value.INST_CAR_NO = selectValue.value.INST_CAR_NO;
  dialogInputContents.value.XCARD_NO = selectValue.value.XCARD_NO;
};
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getIntewelfCardPL', {
      ...searchData.value,
      ...{ IC_CODE: authStore.getIcCode },
    });
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    inteWelfCardPLData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    selectValue.value = {};
  } catch (error) {
    alert(`통합복지카드 P/L 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
      alert(`삭제할 데이터를 선택해주세요.`);
      return;
    }

    if (confirm(`통합복지카드 P/L을 삭제하시겠습니까?`)) {
      const response = await request('post', 'api/card/delIntewelfCardPL', {
        ...selectValue.value,
      });
      alert(response.ERROR_MSG);
      handleSearch();
    }
  } catch (error) {
    alert(`통합복지카드 P/L 삭제 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const handleExcel = () => {
  const row = 1;
  if (inteWelfCardPLData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, inteWelfCardPLData.value, '통합복지카드 PL조회', '통합복지카드 PL조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (inteWelfCardPLData.value.length === 0) {
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
    jsonData.value = createOzDataset('/CARD/inteWelfCardPL.ozr', {
      CSV_DATA: inteWelfCardPLData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '통합복지카드 PL 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
    });
    isActive.value = true;
  } catch (error) {
    alert(`통합복지카드 P/L 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
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

<style scoped></style>
