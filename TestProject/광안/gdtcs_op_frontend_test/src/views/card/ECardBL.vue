<template>
  <InformationComponent style="margin: 4px 0px 0px 0px" message=" 조회조건을 입력하지 않으면 조회 개수가 제한됩니다." icon-type="information" />

  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent :heightOffset="276" :headers="headers" :contents="eCardBLData" rowType="1" scrollKey="eCardBLData" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { request, btnHandler } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { useAuthStore } from '@/stores/index';

const authStore = useAuthStore();
const isLoading = ref(false);

const headers = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '100' },
  { title: '카드번호', key: 'ECARD_NO', width: '200' },
  { title: '무효사유', key: 'BL_RES', width: '200' },
]);
const blResOption = [
  { value: '', title: '전체' },
  { value: '000', title: '분실[000]' },
  { value: '001', title: '하자[001]' },
];

const searchData = ref({
  ECARD_NO: '',
  BL_RES: '',
});

const searchHeader = ref([
  { label: '카드번호', key: 'ECARD_NO', type: 'input', valid: 'digit', maxLength: '19', width: '150px' },
  { label: '무효사유', key: 'BL_RES', type: 'select', option: blResOption, width: '100px' },
]);

const eCardBLData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/card/getEcardBL', {
      ...searchData.value,
      ...{ IC_CODE: authStore.getIcCode },
    });

    eCardBLData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert('데이터가 없습니다.');
    }
  } catch (error) {
    alert(`전자카드 B/L 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (eCardBLData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, eCardBLData.value, '전자카드BL조회', '전자카드BL조회');
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
