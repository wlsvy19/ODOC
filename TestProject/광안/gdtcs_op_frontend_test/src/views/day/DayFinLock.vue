<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeaders" v-model="searchData" @keyup.enter="handleSearch">
    <template v-slot:header_btn>
      <v-btn size="small" @click="btnDayFinUnlock">LOCK 해제</v-btn>
    </template>
  </SearchDataComponent>
  <TableComponent @grid-click-event="onGridClickEvent" :headers="headers" :contents="contents" scrollKey="RuleScroll" rowType="1" />
  <DialogInputFormGridComponent
    v-model:isActive="dialogIsActiveUnLock"
    v-model:inputedData="dialogUnLockInputedData"
    @add-event="unLock"
    :headers="dialogUnLockHeaders"
    :dialog-title="dialogUnLockTitle"
    :cols-per-row="1"
    :css-props="{ dialogWidth: '500px', headerWidth: '20%' }"
  />
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/index';
import { InputRules } from '@/utils/rules';
import { request, btnHandler, makeExcel, initDialogData, isNullOrEmpty } from '@/utils/common';
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
  { title: 'LOCK상태', key: 'LOCK_STATE', width: '120' },
  { title: 'LOCK지정일자', key: 'LOCK_OCC_DT', width: '180' },
  { title: 'LOCK 지정자', key: 'ADMIN_ID_NM', width: '120' },
  { title: 'LOCK 수정번호', key: 'REV_NO', width: '120' },
  { title: 'LOCK 해제사유', key: 'NOTE', width: '300' },
];
const contents = ref([]);

const dialogUnLockHeaders = ref([{ title: '해제사유', key: 'COMMENT', required: 'Y', rules: InputRules.noteRule }]);
const dialogUnLockInputedData = ref(initDialogData(dialogUnLockHeaders.value));
const dialogIsActiveUnLock = ref(false);
const dialogUnLockTitle = {
  icon: 'mdi-lock-off',
  title: '일마감 LOCK 해제',
};

const isLoading = ref(false);
const selectedDate = ref();

const authStore = useAuthStore();

const onGridClickEvent = (item) => (selectedDate.value = item.WORK_DATE.replaceAll('-', ''));

const btnDayFinUnlock = async () => {
  if (!selectedDate.value) alert('LOCK 해제 날짜를 선택하세요.');
  else {
    dialogIsActiveUnLock.value = true;
  }
};

const unLock = async (data) => {
  const result = await request('post', 'api/day/setDayFinUnlock', {
    IC_CODE: authStore.getIcCode,
    WORK_DATE: selectedDate.value,
    ADMIN_ID: authStore.getWorkerNo,
    COMMENT: data['COMMENT'],
  });
  if (!result) alert('LOCK 해제 중 오류가 발생했습니다.');
  else {
    alert(result.O_CURSOR[0].MESSAGE);
    if (result.O_CURSOR[0].CODE === 'SUCCESS') handleSearch();
  }
};

const handleSearch = async () => {
  isLoading.value = true;
  const result = await request('post', 'api/day/getDayFinLockList', {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
    END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
  });

  if (isNullOrEmpty(result)) contents.value = [];
  else contents.value = result;

  selectedDate.value = null;
  isLoading.value = false;
};

const handleExcel = () => makeExcel(1, searchHeaders, searchData.value, headers, contents.value, 'LOCK관리', 'LOCK관리');

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
