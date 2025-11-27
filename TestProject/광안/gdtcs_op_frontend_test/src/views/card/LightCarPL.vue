<template>
  <InformationComponent style="margin: 4px 0px 0px 0px" message=" 조회조건을 입력하지 않으면 조회 개수가 제한됩니다." icon-type="information" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="276"
    :headers="headers"
    @grid-click-event="onGridClickEvent"
    :contents="lightCarPLData"
    rowType="1"
    scrollKey="lightCarPLData"
  />
  <v-dialog v-model="dialog" max-width="300px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>경차 P/L추가</v-toolbar-title>
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
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, reactive } from 'vue';
import { request, btnHandler } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';
import { InputRules } from '@/utils/rules';

const authStore = useAuthStore();
const isLoading = ref(false);
const isValidInputForm = ref(true);
const inputForm = ref();

const headers = ref([
  { title: '순번', key: 'ROW_SEQ', width: '100' },
  { title: '구분', key: 'DIV_CODE', width: '200' },
  { title: '차량번호', key: 'LCAR_NO', width: '200' },
]);

const searchHeader = ref([{ label: '차량번호', key: 'LCAR_NO', type: 'input', valid: 'digit|korean', width: '110px', maxLength: '10' }]);

const searchData = ref({
  LCAR_NO: '',
});
const selectValue = ref({});

const onGridClickEvent = (selectItem) => {
  selectValue.value = {
    LCAR_NO: selectItem.LCAR_NO,
    HAND_DIV: selectItem.IHAND_DIV,
    DIV_CODE: selectItem.IDIV_CODE,
  };
};

const dialog = ref(false);

const dialogInputHeaders = ref([
  { title: '차량번호', key: 'LCAR_NO', required: 'Y', valid: 'digit|korean', rules: InputRules.carNoRule, option: 'input' },
]);
const dialogInputContents = ref({});

const resetInputData = (value) => {
  if (value === 'init') {
    dialogInputContents.value.LCAR_NO = '';
  }
};
const lightCarPLData = ref([]);

const btnSave = async () => {
  try {
    if (dialogInputContents.value.LCAR_NO === '') {
      alert(`필수항목이 비어있습니다.`);
      return false;
    }
    if (!isValidInputForm.value) {
      alert(`${inputForm.value.errors[0].errorMessages[0]}`);
      return;
    }
    const response = await request('post', 'api/card/addLightCarPL', {
      LCAR_NO: dialogInputContents.value.LCAR_NO,
    });
    alert(response.ERROR_MSG);
    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      handleSearch();
    }
  } catch (error) {
    alert(`경차 P/L 정보 저장 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getLightCarPL', {
      ...searchData.value,
      ...{ IC_CODE: authStore.getIcCode },
    });

    lightCarPLData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    selectValue.value = {};
  } catch (error) {
    alert(`경차 P/L 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleAdd = () => {
  dialog.value = true;
  resetInputData('init');
};
const handleDelete = async () => {
  try {
    if (Object.keys(selectValue.value).length === 0) {
      alert(`삭제할 데이터를 선택해주세요.`);
      return;
    }
    if (confirm(`경차 P/L을 삭제하시겠습니까?`)) {
      const response = await request('post', 'api/card/delLightCarPL', {
        ...selectValue.value,
      });
      alert(response.ERROR_MSG);
      handleSearch();
    }
  } catch (error) {
    alert(`경차 P/L 삭제 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  }
};

const handleExcel = () => {
  const row = 1;
  if (lightCarPLData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, lightCarPLData.value, '경차 PL 조회', '경차 PL 조회');
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
