<template>
  <InformationComponent style="margin: 4px 0px 0px 0px" message=" 조회조건을 입력하지 않으면 조회 개수가 제한됩니다. " icon-type="information" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch">
    <template v-slot:header_btn>
      <v-btn size="small" @click="exemRequest">면제여부 요청</v-btn>
    </template>
  </SearchDataComponent>
  <v-dialog v-model="dialogExemPL" max-width="310px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>면제 PL 요청</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <SearchDataComponent :headers="searchFocusExem" v-model="searchExemRequest" @keyup.enter="exemRequestSearch">
          <template v-slot:header_btn>
            <v-btn size="small" ariant="flat" color="blue" @click="exemRequestSearch">조회</v-btn>
            <!-- <v-text style="font-size: 14px; margin: 0 5px; font-weight: bold; align-items: center; position: relative; left: 14px; top: 5px">
              바이트 수 : {{ bytelength }} / 10Byte
            </v-text> -->
          </template>
        </SearchDataComponent>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="black darken-1" text @click="dialogExemPL = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <TableComponent :heightOffset="277" :headers="headers" :contents="exemPLBusanData" rowType="1" scrollKey="exemPLBusanData" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, onActivated, watch } from 'vue';
import { request, btnHandler, getSystemSmallCode } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';

const authStore = useAuthStore();

const isLoading = ref(false);
const plResOption = ref([]);
const dialogExemPL = ref(false);

const bytelength = ref(0);

plResOption.value = getSystemSmallCode('292', true);

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '100' },
  { title: '차량번호', key: 'CAR_NO', width: '150' },
  { title: '면제사유', key: 'EXEM_STATUS', width: '150' },
  { title: '기준일자', key: 'BASE_DT', width: '200' },
  { title: '유효날짜', key: 'EXP_DT', width: '200' },
  { title: '요청날짜', key: 'REQUEST_DATE', width: '200' },
  { title: '주소', key: 'ADDRESS', width: '500' },
]);

const searchHeader = ref([
  { label: '차량번호', key: 'CAR_NO', type: 'input', valid: 'digit|korean', maxLength: '10', width: '120px' },
  { label: '면제사유', key: 'EXEM_STATUS', type: 'select', option: plResOption, width: '110px' },
]);

const searchFocusExem = ref([
  { label: '요청 차량번호', key: 'PACKET_CAR_NO', width: '120px', type: 'input', valid: 'digit|korean', maxLength: '10', required: 'Y' },
]);

const searchData = ref({
  CAR_NO: '',
  EXEM_STATUS: '',
});
const searchExemRequest = ref({
  PACKET_CAR_NO: '',
});
const selectValue = ref({});

const exemPLBusanData = ref([]);
const exemPLFocusData = ref([]);
const exemRequest = async () => {
  dialogExemPL.value = true;
  searchExemRequest.value.PACKET_CAR_NO = '';
};

watch(
  () => searchExemRequest.value.PACKET_CAR_NO,
  () => {
    getBytes(searchExemRequest.value.PACKET_CAR_NO);
  },
);

const exemRequestSearch = async () => {
  try {
    if (searchExemRequest.value.PACKET_CAR_NO === '') {
      alert(`요청할 차량번호를 입력해주세요.`);

      return false;
    }
    // if (getBytes(searchExemRequest.value.PACKET_CAR_NO) > 10) {
    //   alert(`10 Byte까지만 입력해주세요.`);
    //   return false;
    // }
    if (getBytes(searchExemRequest.value.PACKET_CAR_NO) < 8) {
      alert(`차량번호 양식을 맞춰주세요`);
      return false;
    }
    isLoading.value = true;
    const data = await request('post', 'api/card/requestExemPL', {
      ...searchExemRequest.value,
      //요청전문 'focus,123수1234',
      PACKET_AUTH_CHAR: 'focus',
      //PACKET_SORT: ',',
      PACKET_SORT: String.fromCharCode(0x02),
      PACKET_END_CHAR: '\r\n',
      IC_CODE: authStore.getIcCode,
      DATA: 'focus' + String.fromCharCode(0x02) + Object.values(searchExemRequest.value).join('') + '\r\n',
    });
    exemPLFocusData.value = data;
    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    selectValue.value = {};
    if (typeof data.ERROR_MSG != 'object') {
      alert(data.ERROR_MSG);
    } else {
      const alertmessage = ref('');
      alertmessage.value =
        '차량번호 = ' +
        data.ERROR_MSG['차량번호'] +
        '\n' +
        '차종 = ' +
        data.ERROR_MSG['차종'] +
        '\n' +
        '이름 = ' +
        data.ERROR_MSG['이름'] +
        '\n' +
        '주소 = ' +
        data.ERROR_MSG['주소'] +
        '\n' +
        '면제 결과 = ' +
        data.ERROR_MSG['면제 결과'];

      alert(alertmessage.value);
    }
  } catch (error) {
    alert(`면제 PL 요청 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

function getBytes(str) {
  let character;
  bytelength.value = 0;

  for (let i = 0; i < str.length; i += 1) {
    character = str.charAt(i);

    if (escape(character).length > 4) bytelength.value += 2;
    else bytelength.value += 1;
  }
  return bytelength.value;
}

const ozSearchData = ref([]);
const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getExemPLBusan', {
      ...searchData.value,
    });

    exemPLBusanData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }

    selectValue.value = {};
  } catch (error) {
    alert(`면제 PL 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (exemPLBusanData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, exemPLBusanData.value, '면제 PL 조회-부산시', '면제 PL 조회-부산시');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
