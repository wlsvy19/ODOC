<template>
  <div @keyup.enter="handleSearch">
    <LoadingComponent :message="loadingState.msg" v-if="loadingState.isLoading" />
    <SearchDataComponent :headers="searchHeaders" v-model="searchData">
      <template v-slot:header_btn>
        <v-btn size="small" @click="[setAuditStatus(), commonBtnHandle(`api/day/setDayFin`, computedParam, `일마감`)]">일마감</v-btn>
        <v-btn size="small" @click="btnDayFinCancel">일마감 취소</v-btn>
        <v-btn size="small" @click="commonBtnHandle(`api/day/setDayFinLock`, computedParam, `일마감 확정(LOCK)`)">일마감 확정(LOCK)</v-btn>
      </template>
    </SearchDataComponent>
    <TableComponent @grid-click-event="onGridClickEvent" :headers="headers" :contents="contents" scrollKey="RuleScroll" rowType="1" />
  </div>
  <DialogInputFormGridComponent
    v-model:isActive="dialogIsActiveDayFinCancel"
    v-model:inputedData="dialogDayFinCancelInputedData"
    @add-event="commonBtnHandle(`api/day/setDayFinCancel`, computedParam, `일마감 취소`)"
    :headers="dialogDayFinCancelHeaders"
    :cols-per-row="1"
    :dialog-title="dialogDayFinCancelTitle"
    :css-props="{ dialogWidth: '500px', headerWidth: '20%' }"
  />
</template>

<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/index';
import { InputRules } from '@/utils/rules';
import { request, btnHandler, makeExcel, initDialogData, isNullOrEmpty } from '@/utils/common';
import dayjs from 'dayjs';

const searchHeaders = [
  { label: '근무기간', key: 'START_DATE', type: 'date', dateType: 'day', pair: { name: 'WORK_DATE', state: 'START_DATE' } },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day', pair: { name: 'WORK_DATE', state: 'END_DATE' } },
];
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const headers = [
  { title: '순번', key: 'ROW_NUM', width: '80' },
  { title: '관리자', key: 'WORKER_NM', width: '120' },
  { title: '근무일자', key: 'WORK_DATE', width: '110' },
  { title: '근무번호', key: 'WORK_NO', width: '110' },
  { title: '근무개시구분', key: 'WORK_DIV', width: '130' },
  { title: '근무상태', key: 'WORK_STATE', width: '110' },
  { title: '근무종료상태', key: 'END_STATE', width: '130' },
  { title: '일마감상태', key: 'DAY_FIN_STATE', width: '120' },
  { title: 'LOCK상태', key: 'LOCK_STATE', width: '120' },
  { title: '일마감처리일시', key: 'DAY_FIN_PROC_DT', width: '180' },
  { title: '근무개시일시', key: 'START_DT', width: '180' },
  { title: '근무종료일시', key: 'END_DT', width: '180' },
];
const contents = ref([]);

const dialogDayFinCancelHeaders = [{ title: '취소사유', key: 'COMMENTS', required: 'Y', rules: InputRules.noteRule }];
const dialogDayFinCancelInputedData = ref(initDialogData(dialogDayFinCancelHeaders));
const dialogDayFinCancelTitle = {
  icon: 'mdi-pencil-off',
  title: '일마감 취소',
};
const dialogIsActiveDayFinCancel = ref(false);

const loadingState = ref({
  isLoading: false,
  msg: '',
});
const selectedDate = ref();
const authStore = useAuthStore();

const computedParam = computed(() => {
  return {
    IC_CODE: authStore.getIcCode,
    WORK_DATE: selectedDate.value,
    WORKER_NO: authStore.getWorkerNo,
    ADMIN_ID: authStore.getWorkerNo,
    COMMENT: dialogDayFinCancelInputedData.value['COMMENTS'],
  };
});

const onGridClickEvent = (item) => (selectedDate.value = item.isSelected ? item.WORK_DATE.replaceAll('-', '') : '');

const setAuditStatus = async () => {
  const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));
  await delay(1000);
  
  const result = await request('post', 'api/day/setAuditStatus', {IC_CODE: authStore.getIcCode, WORK_DATE: dayjs(selectedDate.value).subtract(14, 'day').format('YYYYMMDD')});
}

const commonBtnHandle = async (url, param, msg) => {
  if (!selectedDate.value) alert(`${msg} 날짜를 선택하세요.`);
  else {
    loadingState.value = { msg: `${msg} 처리 중 입니다.`, isLoading: true };

    const result = await request('post', url, param);
    if (!result) alert(`${msg} 처리 중 오류가 발생했습니다.`);
    else {
      alert(result.O_CURSOR[0].MESSAGE);
      if (result.O_CURSOR[0].CODE === 'SUCCESS') handleSearch();
    }

    loadingState.value = { msg: '', isLoading: false };
  }
};

const btnDayFinCancel = () => {
  if (!selectedDate.value) alert(`일마감 취소 날짜를 선택하세요.`);
  else dialogIsActiveDayFinCancel.value = true;
};

const handleSearch = async () => {
  loadingState.value.isLoading = true;
  const result = await request('post', 'api/day/getDayFinList', {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
    END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
  });

  if (isNullOrEmpty(result)) contents.value = [];
  else contents.value = result;

  selectedDate.value = null;
  loadingState.value.isLoading = false;
};

const handleExcel = () => makeExcel(1, searchHeaders, searchData.value, headers, contents.value, '일마감관리', '일마감관리');

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped></style>
