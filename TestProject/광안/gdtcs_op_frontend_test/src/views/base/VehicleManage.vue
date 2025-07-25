<template>
  <LoadingComponent v-if="isLoading" />
  <InformationComponent style="margin: 4px 0px 0px 0px" message=" 조회조건을 입력하지 않으면 1000대로 제한됩니다." icon-type="information" />

  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch">
    <template v-slot:header_btn>
      <v-btn size="small" @click="openExcelUploadModal">액셀업로드</v-btn>
      <v-btn size="small" @click="openFormDownloadModal">양식다운로드</v-btn>
      <v-btn size="small" @click="handleUpdate">단건차종변경</v-btn>
    </template>
  </SearchDataComponent>
  <TableComponent :heightOffset="305" :headers="headers" :contents="VehicleManageSearchData" rowType="1" scrollKey="VehicleManageSearchData" />

  <div class="count-container">
    <span class="total-text">총 건수</span>
    <span><input class="total-count" :value="comma(VehicleManageSearchData.length)" disabled /> 건</span>
  </div>

  <!-- 엑셀 업로드 팝업 -->
  <v-dialog v-model="isExcelUploadModalOpen" max-width="550px">
    <v-card>
      <v-card-title class="dialog-title">
        <span>차적DB 엑셀 업로드</span>
      </v-card-title>
      <v-divider />
      <v-card-text>
        <div class="upload-container">
          <v-file-input
            v-model="uploadedFile"
            label="엑셀 파일 선택"
            accept=".xls,.xlsx"
            placeholder="파일을 선택해주세요"
            show-size
            outlined
            dense
          ></v-file-input>
          <v-alert v-if="uploadedFile" type="info" dismissible class="mt-1" density="compact"> 선택된 파일: {{ uploadedFile?.name }} </v-alert>
        </div>
      </v-card-text>
      <v-divider />
      <v-card-actions>
        <v-spacer />
        <v-btn color="primary" @click="uploadExcelFile" :disabled="!uploadedFile"> 업로드 </v-btn>
        <v-btn text @click="closeExcelUploadModal">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="dialogAddContent" max-width="500px">
    <v-card ref="card">
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title @mousedown="onMouseDown" style="cursor: move">차량 추가</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <div>
          <InputFormGrid
            class="input-form-style"
            row-height="30px"
            v-model="addContent"
            :headers="addVehicleInfo.headers"
            :table-header-width="'25%'"
          />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="requestAddContent">추가</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogAddContent = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="dialogDeleteContent" max-width="500px">
    <v-card ref="card">
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title @mousedown="onMouseDown" style="cursor: move">차량 삭제</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <div>
          <InputFormGrid
            class="input-form-style"
            row-height="30px"
            v-model="deleteContent"
            :headers="deleteVehicleInfo.headers"
            :table-header-width="'25%'"
          />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="red darken-1" text @click="requestDeleteContent">삭제</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogDeleteContent = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="dialogUpdateContent" max-width="500px">
    <v-card ref="card">
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title @mousedown="onMouseDown" style="cursor: move">차종 변경</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <div>
          <InputFormGrid
            class="input-form-style"
            row-height="30px"
            v-model="updateContent"
            :headers="updateVehicleInfo.headers"
            :table-header-width="'25%'"
          />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="green darken-1" text @click="requestUpdateContent">수정</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogUpdateContent = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { onActivated, reactive, ref } from 'vue';
import { request, btnHandler, comma } from '@/utils/common';
import { useAuthStore } from '@/stores/index';
import dayjs from 'dayjs';
import { useI18n } from 'vue-i18n';
import { isValidCarNumber } from '@/utils/validate.js';
import ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';

const authStore = useAuthStore();
const isLoading = ref(false);
const { t } = useI18n();

onActivated(() => {
  handleSearch();
});

const VehicleManageSearchData = ref([]);

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '111' },
  { title: '차량번호', key: 'VCAR_NO', width: '300' },
  { title: '차종', key: 'CAR_TYPE', width: '300' },
  { title: '생성시간', key: 'CRATE_DT', width: '330' },
  { title: '이전차종', key: 'OLD_CAR_TYPE', width: '300' },
  { title: '수정시간', key: 'MDFY_DT', width: '330' },
]);

const searchHeader = ref([
  { label: '차량번호', key: 'VCAR_NO', type: 'input', width: '150px' },
  // { label: '수정시간', key: 'START_DATE', type: 'date', dateType: 'day' },
  // { label: '', key: 'MODIFY_START_TIME', type: 'time' },
  // { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  // { label: '', key: 'MODIFY_END_TIME', type: 'time' },
]);

const searchData = ref({
  VCAR_NO: '',
  START_DATE: dayjs().format('YYYY-MM-DD'),
  MODIFY_START_TIME: dayjs('00:00:00', 'HH:mm:ss').format('HH:mm'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  MODIFY_END_TIME: dayjs('23:59:00', 'HH:mm:ss').format('HH:mm'),
});

const handleSearch = async () => {
  if (searchData.value.VCAR_NO && !isValidCarNumber(searchData.value.VCAR_NO)) {
    alert(`차량번호를 정확히 입력해주세요. (한글, 숫자, - 만 허용)\n또한 공백이 있는지도 확인해주세요.`);
    return;
  }
  try {
    isLoading.value = true;
    const response = await request('post', '/api/base/getVehicleManage', {
      VCAR_NO: searchData.value.VCAR_NO.trim(),
      IC_CODE: authStore.getIcCode,
    });
    if (response.length == 0) {
      alert(t('NO_DATA'));
    }
    const formattedResponse = response.map((item) => {
      return {
        ...item,
        CRATE_DT: dayjs(item.CRATE_DT, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss'),
        MDFY_DT: dayjs(item.MDFY_DT, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss'),
      };
    });
    VehicleManageSearchData.value = formattedResponse;
  } catch (error) {
    alert('차적DB관리' + t('SEARCH_ERROR'));
  } finally {
    isLoading.value = false;
  }
};

const isExcelUploadModalOpen = ref(false); // 팝업 열림 상태
const uploadedFile = ref(null); // 업로드된 파일

const openExcelUploadModal = () => {
  isExcelUploadModalOpen.value = true;
};

const closeExcelUploadModal = () => {
  isExcelUploadModalOpen.value = false;
  uploadedFile.value = null; // 선택 파일 초기화
};

const uploadExcelFile = async () => {
  if (!uploadedFile.value) {
    alert('파일을 선택해 주세요.');
    return;
  }
  const formData = new FormData();
  formData.append('file', uploadedFile.value);

  try {
    isLoading.value = true;
    const response = await request('post', '/api/base/excelVehicleManage', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    alert(response);
    closeExcelUploadModal();
  } catch (error) {
    alert('엑셀 파일 업로드 중 에러가 발생했습니다.');
  } finally {
    await handleSearch();
    isLoading.value = false;
  }
};

const openFormDownloadModal = async () => {
  if (!confirm('차적DB관리 엑셀 파일을 다운로드 하시겠습니까?')) {
    return;
  }

  const workbook = new ExcelJS.Workbook();

  const worksheet = workbook.addWorksheet('차적DB관리');

  worksheet.columns = [
    { header: '차량번호', key: 'carNumber', width: 20 },
    { header: '차종', key: 'carType', width: 10 },
    { header: '', key: 'info', width: 50 },
    { header: '', key: '', width: 15 }, // D열
    { header: '', key: '', width: 15 }, // E열
  ];

  worksheet.getColumn(1).alignment = { horizontal: 'center', vertical: 'middle' }; // A열
  worksheet.getColumn(2).alignment = { horizontal: 'center', vertical: 'middle' }; // B열

  const headerRow = worksheet.getRow(1);
  headerRow.font = {
    size: 11,
    bold: true,
  };

  worksheet.getCell('A1').alignment = { horizontal: 'center', vertical: 'middle' };
  worksheet.getCell('B1').alignment = { horizontal: 'center', vertical: 'middle' };

  worksheet.getCell('A2').value = '123가5678'; // 차량번호 샘플 값
  worksheet.getCell('B2').value = 1; // 차종 값

  const cellC2 = worksheet.getCell('C2');
  cellC2.value = '<- 샘플 데이터 삭제 후 사용하세요';
  cellC2.font = {
    color: { argb: 'FFFF0000' },
    size: 10,
    bold: true,
  };
  cellC2.alignment = { horizontal: 'left', vertical: 'middle' };

  worksheet.mergeCells('C3:C4'); // C3과 C4 병합
  worksheet.getCell('C3').value = `1. 차량번호는 한글과 숫자만 허용됩니다.\n` + `2. 차종은 1~6 사이 숫자만 허용됩니다.`;
  worksheet.getCell('C3').alignment = { horizontal: 'left', vertical: 'top', wrapText: true };
  worksheet.getCell('C3').font = { size: 10, bold: true };

  worksheet.mergeCells('D1:D7');
  worksheet.getCell('D1').value = `차종(6종)\n` + `1종: 소형\n` + `2종: 중형\n` + `3종: 대형\n` + `4종: 대형\n` + `5종: 대형\n` + `6종: 경형`; // 병합된 셀의 텍스트 추가
  worksheet.getCell('D1').alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
  worksheet.getCell('D1').font = { size: 10, bold: true };

  const buffer = await workbook.xlsx.writeBuffer();

  const blob = new Blob([buffer], {
    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  });
  saveAs(blob, '차적DB관리양식.xlsx');
};

////////////////////////////////////단건추가시작///////////////////////////////////////
const dialogAddContent = ref(false);
const handleAdd = async () => {
  dialogAddContent.value = true;
};

const carTypeOption = ref([
  { title: '경형', value: '6' },
  { title: '소형', value: '1' },
  { title: '중형', value: '2' },
  { title: '대형', value: '3' },
]);

const addVehicleInfo = reactive({
  headers: [
    { title: '차량번호', key: 'VCAR_NO', required: 'Y' },
    { title: '차종', key: 'CAR_TYPE', required: 'Y', option: 'select', selectItem: carTypeOption },
  ],
});

const addContent = ref({});

const requestAddContent = async () => {
  if (addContent.value.VCAR_NO === null || addContent.value.VCAR_NO === undefined || addContent.value.VCAR_NO === '') {
    alert('추가하실 차량번호를 입력해주세요.');
    return false;
  }
  if (addContent.value.CAR_TYPE === null || addContent.value.CAR_TYPE === undefined || addContent.value.CAR_TYPE === '') {
    alert('차종을 선택해주세요.');
    return false;
  }
  if (addContent.value.VCAR_NO && !isValidCarNumber(addContent.value.VCAR_NO)) {
    alert(`차량번호를 정확히 입력해주세요. (한글, 숫자, - 만 허용)\n또한 공백이 있는지도 확인해주세요.`);
    return;
  }
  if (!confirm(`[${addContent.value.VCAR_NO.trim()}] 차량을 정말로 추가 하시겠습니까?`)) {
    return;
  }
  try {
    const response = await request('post', '/api/base/addVehicleInfo', {
      VCAR_NO: addContent.value.VCAR_NO.trim(),
      CAR_TYPE: addContent.value.CAR_TYPE,
    });
    alert(response);
  } catch (error) {
    alert('차종 추가화면에서 에러가 발생했습니다.');
  } finally {
    await handleSearch();
    addContent.value = {};
  }
};
////////////////////////////////////단건추가끝////////////////////////////////////////
////////////////////////////////////단건삭제시작///////////////////////////////////////
const dialogDeleteContent = ref(false);
const handleDelete = async () => {
  dialogDeleteContent.value = true;
};

const deleteVehicleInfo = reactive({
  headers: [{ title: '차량번호', key: 'VCAR_NO', required: 'Y' }],
});

const deleteContent = ref({});

const requestDeleteContent = async () => {
  if (deleteContent.value.VCAR_NO === null || deleteContent.value.VCAR_NO === undefined || deleteContent.value.VCAR_NO === '') {
    alert('삭제하실 차량번호를 입력해주세요.');
    return false;
  }
  if (deleteContent.value.VCAR_NO && !isValidCarNumber(deleteContent.value.VCAR_NO)) {
    alert(`차량번호를 정확히 입력해주세요. (한글, 숫자, - 만 허용)\n또한 공백이 있는지도 확인해주세요.`);
    return;
  }
  if (!confirm(`[${deleteContent.value.VCAR_NO.trim()}] 차량을 정말로 삭제 하시겠습니까?`)) {
    return;
  }
  try {
    const response = await request('post', '/api/base/deleteVehicleInfo', {
      VCAR_NO: deleteContent.value.VCAR_NO.trim(),
    });
    alert(response);
  } catch (error) {
    alert('차종 삭제화면에서 에러가 발생했습니다.');
  } finally {
    await handleSearch();
    deleteContent.value = {};
  }
};
////////////////////////////////////단건삭제끝///////////////////////////////////////
////////////////////////////////////단건수정시작//////////////////////////////////////
const dialogUpdateContent = ref(false);
const handleUpdate = async () => {
  dialogUpdateContent.value = true;
};

const updateVehicleInfo = reactive({
  headers: [
    { title: '차량번호', key: 'VCAR_NO', required: 'Y' },
    { title: '변경할 차종', key: 'CAR_TYPE', required: 'Y', option: 'select', selectItem: carTypeOption },
  ],
});

const updateContent = ref({});

const requestUpdateContent = async () => {
  if (updateContent.value.VCAR_NO === null || updateContent.value.VCAR_NO === undefined || updateContent.value.VCAR_NO === '') {
    alert('수정하실 차량번호를 입력해주세요.');
    return false;
  }

  if (updateContent.value.VCAR_NO && !isValidCarNumber(updateContent.value.VCAR_NO)) {
    alert(`차량번호를 정확히 입력해주세요. (한글, 숫자, - 만 허용)\n또한 공백이 있는지도 확인해주세요.`);
    return;
  }
  if (updateContent.value.CAR_TYPE === null || updateContent.value.CAR_TYPE === undefined || updateContent.value.CAR_TYPE === '') {
    alert('차종을 선택해주세요.');
    return false;
  }

  const carTypeMap = {
    1: '소형',
    2: '중형',
    3: '대형',
    6: '경형',
  };
  const carTypeString = carTypeMap[updateContent.value.CAR_TYPE] || '알 수 없음';

  if (!confirm(`[${updateContent.value.VCAR_NO.trim()}] 차량의 차종을 [${carTypeString}]으로 수정 하시겠습니까?`)) {
    return;
  }
  try {
    const response = await request('post', '/api/base/oneUpdateVehicleInfo', {
      VCAR_NO: updateContent.value.VCAR_NO.trim(),
      CAR_TYPE: updateContent.value.CAR_TYPE,
      MDFY_DT: dayjs().format('YYYYMMDDHHmmss'),
    });
    alert(response);
  } catch (error) {
    alert('차종 수정화면에서 에러가 발생했습니다.');
  } finally {
    await handleSearch();
    updateContent.value = {};
  }
};
////////////////////////////////////단건수정끝//////////////////////////////////////
//다이얼로그 이동
const card = ref(null);
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

btnHandler({
  Search: handleSearch,
  Add: handleAdd,
  Delete: handleDelete,
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

.dialog-title {
  font-size: 18px;
  font-weight: bold;
  color: #333333;
}

.upload-container {
  margin-top: 20px;
}

.v-alert {
  font-size: 14px;
}
</style>
