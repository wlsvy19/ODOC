<template>
  <v-row>
    <v-col cols="12">
      <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
    </v-col>
  </v-row>
  <v-row>
    <v-col cols="12">
      <TableComponent scroll-key="gridkeyRevisionHistory" :headers="headers" :contents="contents" :height-offset="189 + 36 + 29" row-type="1" />
    </v-col>
  </v-row>
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, onActivated } from 'vue';
import { request, btnHandler, getFareInfo } from '@/utils/common';
import { useAuthStore } from '@/stores/index';
import dayjs from 'dayjs';

const isLoading = ref(false);
const authStore = useAuthStore();

onActivated(async () => {
  const authStore = useAuthStore();
  getFareInfo(authStore.getIcCode);
});

const headers = ref([
  { title: '영업소', key: 'IC_NM', align: 'center', width: '90' },
  { title: '개정유형', key: 'REV_NO_CODE_NM', align: 'center', width: '140' },
  { title: '개정번호', key: 'REV_NO', align: 'center', width: '90' },
  { title: '수정번호', key: 'MDFY_NO', align: 'center', width: '90' },
  { title: '수정시각', key: 'MDFY_DT_FORMAT', align: 'center', width: '160' },
  { title: '적용일시', key: 'APPLY_DT_FORMAT', align: 'center', width: '160' },
  { title: '개정지시', key: 'REV_DIV_NM', align: 'center', width: '90' },
  { title: '관리자', key: 'ADMIN_NM', align: 'center', width: '140' },
]);

const contents = ref([]);

const searchHeader = ref([
  { label: '수정일자', key: 'MDFY_DT_FROM', type: 'date', dateType: 'day' },
  { label: '~', key: 'MDFY_DT_TO', type: 'date', dateType: 'day' },
]);

const searchData = ref({
  MDFY_DT_FROM: dayjs().format('YYYY-MM-DD'),
  MDFY_DT_TO: dayjs().format('YYYY-MM-DD'),
});

const handleSearch = async () => {
  isLoading.value = true;
  try {
    const data = await request('post', 'api/base/getRevisionHistory', {
      IC_CODE: authStore.getIcCode,
      MDFY_DT_FROM: dayjs(searchData.value.MDFY_DT_FROM).format('YYYYMMDD'),
      MDFY_DT_TO: dayjs(searchData.value.MDFY_DT_TO).format('YYYYMMDD'),
    });

    contents.value = data;

    nextTick(() => (isLoading.value = false));
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);

    nextTick(() => (isLoading.value = false));
  }
};

const handleAdd = () => {
  //
};
const handleDelete = () => {
  //
};
const handleSave = () => {
  //
};
const handleExcel = () => {
  //
};
const handlePrint = () => {
  //
};
const handlePowerOff = () => {
  //
};

btnHandler({
  Search: handleSearch,
  Add: handleAdd,
  Delete: handleDelete,
  Save: handleSave,
  Excel: handleExcel,
  Print: handlePrint,
  PowerOff: handlePowerOff,
});
</script>
