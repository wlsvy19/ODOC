<template>
  <LoadingComponent :message="loadingState.msg" v-if="loadingState.isLoading" />
  <div class="content-container">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch">
      <template v-slot:header_btn>
        <v-btn size="small" @click="fileAdd">엑셀파일 등록</v-btn>
      </template>
    </SearchDataComponent>
    <v-row>
      <v-col cols="5">
        <TableComponent :heightOffset="258" :headers="jbcHeaders" :contents="jabicallPassListData" rowType="1" scrollKey="jabicallPassListData" />
      </v-col>
      <v-col cols="7" class="vertical-panel">
        <TableComponent :heightOffset="258" :headers="gdtcsHeaders" :contents="gdtcsPassListData" rowType="1" scrollKey="gdtcsPassListData" />
      </v-col>
    </v-row>
  </div>
  <v-dialog v-model="dialogJBC" max-width="900px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>자비콜 엑셀파일 등록</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="4" style="margin-top: 12px">
            <SearchDataComponent :headers="taxiHeader" v-model="taxiData" @keyup.enter="btnAddFile" />
          </v-col>
          <v-col>
            <v-file-input
              variant="solo-filled"
              prepend-icon="mdi-file-excel"
              max-width="300"
              accept=".xlsx, .xls"
              label="엑셀파일 등록"
              v-model="jabicallExcel"
            />
          </v-col>
          <TableComponent rowType="1" :heightOffset="500" scrollKey="jabicallExcelData" :headers="fileHeaders" :contents="jabicallExcelData" />
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="btnAddFile">저장</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogJBCClose">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, watch, onActivated } from 'vue';
import { request, btnHandler, getSystemSmallCode } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';
import ExcelJS from 'exceljs';
import dayjs from 'dayjs';

const authStore = useAuthStore();
const jabicallPassListData = ref([]);
const gdtcsPassListData = ref([]);
const jabicallExcel = ref(null);
const dialog = ref(false);
const dialogJBC = ref(false);
const isLoading = ref(false);
const jabicallExcelData = ref([]);
const bsTaxiDivOption = ref([]);
const searchBSTaxiDivOption = ref([]);
const loadingState = ref({
  isLoading: false,
  msg: '',
});
const workbook = new ExcelJS.Workbook();
watch(
  () => jabicallExcel.value,
  () => {
    if (jabicallExcel.value != null) {
      jabicallExcelData.value = [];
      const reader = new FileReader();
      reader.onload = function (e) {
        const buffer = e.target.result;

        workbook.xlsx
          .load(buffer)
          .then((workbook) => {
            workbook.eachSheet((sheet, id) => {
              if (taxiData.value.BSTAXI_DIV == '선택해주세요') {
                alert(`택시구분을 선택해주세요.`);
                return false;
              }
              sheet.eachRow((row, rowIndex) => {
                if (rowIndex != 1) {
                  if (
                    row.values[1] == '' ||
                    row.values[1] == null ||
                    row.values[2] == '' ||
                    row.values[2] == null ||
                    row.values[4] == '' ||
                    row.values[4] == null
                  ) {
                    alert('엑셀파일의 ' + rowIndex + '열, 일련번호 ' + row.values[1] + '의 엑셀 데이터 빈값을 확인해주세요.');
                    dialogJBCClose();
                    return false;
                  }
                  if (
                    row.values[4].replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '').length >= 15 ||
                    row.values[5].replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '').length >= 15
                  ) {
                    alert('엑셀파일의 ' + rowIndex + '열, 일련번호 ' + row.values[1] + '의 승차시간, 하차시간 데이터를 확인해주세요.');
                    dialogJBCClose();
                    return false;
                  }
                  if (row.values[3].length >= 10) {
                    alert('엑셀파일의 ' + rowIndex + '열, 일련번호 ' + row.values[1] + '의 운전자명 데이터를 확인해주세요.');
                    dialogJBCClose();
                    return false;
                  }
                  jabicallExcelData.value = [
                    ...jabicallExcelData.value,
                    {
                      JBC_SNO: row.values[1],
                      JBC_CAR_NO: row.values[2].replaceAll(' ', ''),
                      JBC_DRIVER: row.values[3].replaceAll(' ', ''),
                      IN_DT: row.values[4].replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '').replaceAll(' ', ''),
                      OUT_DT: row.values[5].replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '').replaceAll(' ', ''),
                      IC_CODE: authStore.getIcCode.replaceAll(' ', ''),
                      WORK_DATE: dayjs(row.values[4]).format('YYYYMMDD').replaceAll(' ', ''),
                      BSTAXI_DIV: taxiData.value.BSTAXI_DIV,
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
      reader.readAsArrayBuffer(jabicallExcel.value);
    }
  },
  {
    immediate: true,
  },
);
const jbcHeaders = ref([
  { title: '처리일련번호', key: 'JBC_SNO', width: '100' },
  { title: '차량번호', key: 'JBC_CAR_NO', width: '110' },
  { title: '승차시간', key: 'IN_DT', width: '160' },
  { title: '하차시간', key: 'OUT_DT', width: '160' },
  { title: '운전자', key: 'JBC_DRIVER', width: '110' },
  { title: '부산 콜택시 구분', key: 'BSTAXI_DIV', width: '110' },
]);

const gdtcsHeaders = ref([
  { title: '순번', key: 'SEQ_NO', width: '100' },
  { title: '근무일자', key: 'WORK_DATE', width: '100' },
  { title: '근무번호', key: 'WORK_NO', width: '100' },
  { title: '처리일련번호', key: 'HAND_SNO', width: '100' },
  { title: '처리시각', key: 'HAND_DT', width: '160' },
  { title: '통행요금', key: 'PASS_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금액', key: 'PAY_FARE', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '차량번호', key: 'HAND_CAR_NO', width: '150' },
  { title: '자비콜 차량번호', key: 'JBC_CAR_NO', width: '150' },
  { title: '운전자', key: 'JBC_DRIVER', width: '110' },
  { title: '부산 콜택시 구분', key: 'BSTAXI_DIV', width: '110' },
]);
const fileHeaders = ref([
  { title: '처리일련번호', key: 'JBC_SNO', width: '100' },
  { title: '차량번호', key: 'JBC_CAR_NO', width: '150' },
  { title: '운전자', key: 'JBC_DRIVER', width: '150' },
  { title: '승차시간', key: 'IN_DT', width: '160' },
  { title: '하차시간', key: 'OUT_DT', width: '160' },
  { title: '부산 콜택시 구분', key: 'BSTAXI_DIV', width: '110' },
]);
const searchHeader = ref([
  { label: '근무일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차량번호', key: 'JBC_CAR_NO', type: 'input', valid: 'digit|korean', maxLength: '10', width: '120px' },
  { label: '부산 콜택시 구분', key: 'BSTAXI_DIV', type: 'select', option: searchBSTaxiDivOption, width: '130px' },
]);
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  JBC_CAR_NO: '',
  BSTAXI_DIV: '',
});

const taxiHeader = ref([{ label: '부산 콜택시 구분', key: 'BSTAXI_DIV', type: 'select', option: bsTaxiDivOption, width: '130px' }]);
const taxiData = ref({
  BSTAXI_DIV: '선택해주세요',
});

onActivated(async () => {
  bsTaxiDivOption.value = getSystemSmallCode('297', false);
  searchBSTaxiDivOption.value = getSystemSmallCode('297', true);
});

const selectValue = ref({});

const ozSearchData = ref([]);

const btnAddFile = async () => {
  try {
    if (jabicallExcelData.value == '') {
      alert(`추가할 데이터가 없습니다.`);
      return false;
    }
    loadingState.value = { msg: '엑셀파일을 등록중입니다.', isLoading: true };
    const response = await request('post', 'api/sales/addJabicallFile', {
      ...jabicallExcelData.value,
    });
    alert(response.ERROR_MSG);
    if (Number(response.ERROR_CODE) > 0) {
      dialog.value = false;
      jabicallExcelData.value = [];
      jabicallExcel.value = null;
      taxiData.value.BSTAXI_DIV = '선택해주세요';
    }
  } catch (error) {
    alert(` 등록 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    loadingState.value = { msg: '', isLoading: false };
  }
};

const fileAdd = () => {
  jabicallExcel.value = null;
  jabicallExcelData.value = [];
  dialogJBC.value = true;
  taxiData.value.BSTAXI_DIV = '선택해주세요';
};

const dialogJBCClose = () => {
  dialogJBC.value = false;
  jabicallExcel.value = null;
  jabicallExcelData.value = [];
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const gdtcsData = await request('post', 'api/sales/getGdtcsPassList', {
      ...searchData.value,
      ...{
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
      },
    });

    const jbcData = await request('post', 'api/sales/getJabicallPassList', {
      ...searchData.value,
      ...{
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
      },
    });

    gdtcsPassListData.value = gdtcsData;
    jabicallPassListData.value = jbcData;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (gdtcsData.length == 0 && jbcData.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    selectValue.value = {};
  } catch (error) {
    alert(`조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (jabicallPassListData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = gdtcsHeaders.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, gdtcsPassListData.value, '자비콜 통행내역 조회', '자비콜 통행내역 조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
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
.custom-file-button {
  max-width: 100px;
  height: 36px;
  padding: 0 12px;
  background-color: #a9a9a9;
  color: white;
  font-size: 14px;
  font-weight: 500;
  border-radius: 4px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease;
}

.custom-file-button:hover {
  background-color: #4cbd49;
}
</style>
