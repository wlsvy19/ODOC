<template>
  <v-dialog v-model="isActive" @update:model-value="btnCancel" :max-width="cssProps.dialogWidth">
    <v-card :prepend-icon="dialogTitle.icon" :title="dialogTitle.title">
      <v-card-text>
        <InputFormGrid
          v-model="inputedData"
          v-model:is-valid="isValid"
          v-model:input-form="inputForm"
          :headers="headers"
          :cols-per-row="colsPerRow"
          :table-header-width="cssProps.headerWidth"
          @keyup.enter="btnAdd"
        />
      </v-card-text>
      <v-divider />
      <v-card-actions>
        <v-spacer />
        <v-btn color="blue darken-1" text="등록" variant="flat" @click="btnAdd" />
        <v-btn color="black darken-1" text="닫기" variant="flat" @click="btnCancel" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { initDialogData } from '@/utils/common';

const isActive = defineModel('isActive', {
  type: Boolean,
});
const inputedData = defineModel('inputedData', {
  type: Object,
});
const props = defineProps({
  cssProps: {
    type: Object,
    default: {
      dialogWidth: '800px',
      headerWidth: '15%',
    },
  },
  headers: {
    type: Array,
    required: true,
  },
  colsPerRow: {
    type: Number,
    required: true,
  },
  dialogTitle: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['addEvent']);

const isValid = ref(true);
const inputForm = ref();

const btnAdd = () => {
  try {
    if (!isValid.value) {
      alert(inputForm.value.errors[0].errorMessages[0], 'warning');
      return;
    }
    emit('addEvent', inputedData.value);
    isActive.value = false;
    inputedData.value = initDialogData(props.headers);
  } catch (e) {
    alert(e.message);
  }
};
const btnCancel = () => {
  isActive.value = false;
  inputedData.value = initDialogData(props.headers);
};
</script>

<style scoped>
.v-card:deep(.v-card-item) {
  align-self: center;
}
.v-card-text {
  padding-bottom: 0px !important;
}
.text-caption {
  font-weight: 600;
  color: red;
}
</style>
