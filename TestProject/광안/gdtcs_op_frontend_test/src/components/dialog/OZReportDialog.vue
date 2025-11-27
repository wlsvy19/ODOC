<template>
  <v-dialog width="70vw" min-height="100vh" v-model:modelValue="isActive" @update:model-value="btnCancel">
    <v-card>
      <OZReportComponent v-model:jsonData="jsonData" :iframe-height="'calc(100vh - 53px)'" />
      <v-card-actions :style="{ borderTop: '1px inset #ECECEC', borderLeft: '2px inset #ECECEC' }">
        <v-btn variant="flat" color="black darken-1" text="닫기" @click="btnCancel"></v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
const isActive = defineModel('isActive', {
  type: Boolean,
});
const jsonData = defineModel('jsonData', {
  type: String,
  default: () => '', // 빈 객체로 초기화
});

const btnCancel = () => (isActive.value = false);
</script>

<style scoped></style>
