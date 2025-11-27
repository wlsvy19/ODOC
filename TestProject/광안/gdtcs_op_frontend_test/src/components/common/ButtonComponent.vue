<template>
  <!-- 공통 버튼 영역-->
  <v-app-bar id="common-container" :color="colorStore.buttonColor" height="36">
    <v-spacer />
    <div>
      <v-btn class="font-regular-ac" :disabled="disabledSearch" @click="handleButtonClick('search')" variant="tonal"
        ><v-icon icon="mdi-magnify" />조회</v-btn
      >
      <v-btn class="font-regular-ac" :disabled="disabledAdd" @click="handleButtonClick('add')" variant="tonal" rounded="none"
        ><v-icon icon="mdi-plus-circle-outline" />추가</v-btn
      >
      <v-btn class="font-regular-ac" :disabled="disabledDelete" @click="handleButtonClick('delete')" variant="tonal"
        ><v-icon icon="mdi-close-circle-outline" />삭제</v-btn
      >
      <v-btn class="font-regular-ac" :disabled="disabledSave" @click="handleButtonClick('save')" variant="tonal"
        ><v-icon icon="mdi-download" />저장</v-btn
      >
      <v-btn class="font-regular-ac" :disabled="disabledExcel" @click="handleButtonClick('excel')" variant="tonal"
        ><v-icon icon="mdi-file-excel" />엑셀</v-btn
      >
      <v-btn class="font-regular-ac" :disabled="disabledPrint" @click="handleButtonClick('print')" variant="tonal"
        ><v-icon icon="mdi-printer" />출력</v-btn
      >
      <v-btn class="font-regular-ac" @click="handleButtonClick('power-off')" variant="tonal"><v-icon icon="mdi-power" />종료</v-btn>
    </div>
  </v-app-bar>
</template>

<script setup>
import { useColorStore } from '@/stores/index';

const colorStore = useColorStore();

defineProps({
  disabledSearch: Boolean,
  disabledAdd: Boolean,
  disabledDelete: Boolean,
  disabledSave: Boolean,
  disabledExcel: Boolean,
  disabledPrint: Boolean,
});
const emit = defineEmits(['search-click', 'add-click', 'delete-click', 'save-click', 'excel-click', 'print-click', 'power-off-click']);
const handleButtonClick = (type) => {
  emit(`${type}-click`);
};
</script>

<style scoped>
.v-btn {
  margin: 0.2rem;
}
</style>
