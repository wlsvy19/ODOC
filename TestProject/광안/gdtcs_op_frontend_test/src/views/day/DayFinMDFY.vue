<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeaders" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent :headers="headers" :contents="contents" scrollKey="RuleScroll" rowType="1" />
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/index';
import { request, btnHandler, makeExcel, isNullOrEmpty } from '@/utils/common';
import dayjs from 'dayjs';

const searchHeaders = [
  { label: '근무기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
];
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const headers = [
  { title: '순번', key: 'ROW_NUM', width: '158' },
  { title: '근무일자', key: 'WORK_DATE', width: '110' },
  { title: '관리자명', key: 'WORKER_NM', width: '120' },
  { title: '수정일시', key: 'HAND_DT', width: '200' },
  { title: '수정사유', key: 'MDFY_RES', width: '390' },
  { title: '수정번호', key: 'MDFY_NO', width: '100' },
];
const contents = ref([]);

const isLoading = ref(false);
const authStore = useAuthStore();

const handleSearch = async () => {
  isLoading.value = true;
  const result = await request('post', '/api/day/getDayFinMdfyList', {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
    END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
  });

  if (isNullOrEmpty(result)) contents.value = [];
  else contents.value = result;

  isLoading.value = false;
};

const handleExcel = () => makeExcel(1, searchHeaders, searchData.value, headers, contents.value, '일마감수정내역', '일마감수정내역');

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
