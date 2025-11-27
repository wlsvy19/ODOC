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
  { title: '순번', key: 'ROW_NUM', width: '80' },
  { title: '근무일자', key: 'WORK_DATE', width: '110' },
  { title: 'LOCK 해제일자', key: 'LOCK_OCC_DT', width: '180' },
  { title: 'LOCK 지정자', key: 'ADMIN_ID_NM', width: '120' },
  { title: 'LOCK 수정번호', key: 'REV_NO', width: '120' },
  { title: 'LOCK 해제사유', key: 'NOTE', width: '180' },
];
const contents = ref([]);

const isLoading = ref(false);
const authStore = useAuthStore();

const handleSearch = async () => {
  isLoading.value = true;
  const result = await request('post', 'api/day/getDayFinUnlockList', {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
    END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
  });

  if (isNullOrEmpty(result)) contents.value = [];
  else contents.value = result;

  isLoading.value = false;
};

const handleExcel = () => makeExcel(1, searchHeaders, searchData.value, headers, contents.value, 'LOCK해제내역조회', 'LOCK해제내역조회');

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
