<template>
  <TableComponent scroll-key="gridKeyTollContinuousPass" :headers="headers" :contents="contents" :height-offset="189 + 29" row-type="1" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, onActivated } from 'vue';
import { useAuthStore } from '@/stores/index';
import { request, btnHandler } from '@/utils/common';
// const code_APPLY_DIV = ref([]);
// const code_EXMT_DIV = ref([]);
// const code_BASE_DIV = ref([]);
onActivated(async () => {
  // await nextTick();
  await handleSearch();
});

const headers = ref([
  { title: '영업소ID', key: 'IC_CODE', align: 'center', width: '90' },
  { title: '영업소명', key: 'IC_NM', align: 'center', width: '90' },
  { title: '연속통행할인대상구분', key: 'EXMT_DIV_NM', align: 'center', width: '160' },
  { title: '제도 적용 여부', key: 'APPLY_DIV_NM', align: 'center', width: '100' },
  { title: '제도 적용 일자', key: 'EXMT_APPLY_BGN_DT_FORMAT', align: 'center', width: '100' },
  { title: '신정보 적용일시', key: 'EXMT_APPLY_DT_FORMAT', align: 'center', width: '180' },
  { title: '구정보 폐쇄일시', key: 'EXMT_CLS_DT_FORMAT', align: 'center', width: '180' },
  { title: '기준정보 적용유무', key: 'BASE_DIV_NM', align: 'center', width: '120' },
  { title: '생성일시', key: 'MAKE_DT', align: 'center', width: '180' },
  // { title: '생성자', key: 'MAKE_ADMIN', align: 'center', width: '90' },
  { title: '수정일시', key: 'MDFY_CONT_DT', align: 'center', width: '170' },
  // { title: '수정자', key: 'MDFY_ADMIN', align: 'center', width: '90' },
]);

const contents = ref([]);

const isLoading = ref(false);

const authStore = useAuthStore();

const handleSearch = async () => {
  isLoading.value = true;

  const result = await request('post', 'api/base/getContinuousPassIcList', {
    IC_CODE: authStore.getIcCode,
  });

  if (result.length == 0) {
    alert('데이터가 없습니다.');
  }

  contents.value = result;

  nextTick(() => (isLoading.value = false));
};

btnHandler({
  Search: handleSearch,
});
</script>
