<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-text-field
          v-model="timeFormat"
          @click="canActivate()"
          :readonly="readonly"
          :disabled="disabled"
          append-icon="mdi-clock-time-four-outline"
          variant="outlined"
          density="compact"
          hide-details
        />
      </v-col>
    </v-row>
  </v-container>
  <v-dialog v-model="timepickerModal" width="auto">
    <v-time-picker format="24hr" v-model="timeFormat" scrollable></v-time-picker>
    <v-btn @click="close()">확인</v-btn>
  </v-dialog>
</template>
<script setup>
import { ref, watch } from 'vue';

const timepickerModal = ref(false);

const timeFormat = ref('00:00');

const value_time = defineModel({
  type: String,
  required: true,
  default: '0000',
});

const props = defineProps({
  format: {
    type: String,
    required: false,
    default: 'HHmm',
  },
  readonly: {
    type: Boolean,
    required: false,
    default: false,
  },
  disabled: {
    type: Boolean,
    required: false,
    default: false,
  },
});

watch(
  value_time,
  () => {
    try {
      if (props.format == 'HHmm') {
        const HH = value_time.value.slice(0, 2);
        const mm = value_time.value.slice(2, 4);
        timeFormat.value = `${HH}:${mm}`;
      }
    } catch (err) {
      console.log('시간형식 변환 중에 오류 발생 ' + err);
      timeFormat.value = `00:00`;
    }
  },
  { immediate: true },
);

watch(
  timeFormat,
  () => {
    try {
      if (props.format == 'HHmm') {
        const HH = timeFormat.value.slice(0, 2);
        const mm = timeFormat.value.slice(3, 5);
        value_time.value = `${HH}${mm}`;
      }
    } catch (err) {
      console.log('시간형식 변환 중에 오류 발생 ' + err);
      value_time.value = `0000`;
    }
  },
  { immediate: true },
);

const canActivate = () => {
  //console.log("readonly: " + props.readonly);
  //console.log("disabled: " + props.disabled);
  if (props.readonly == true) {
    return;
  }
  if (props.disabled == true) {
    return;
  }
  timepickerModal.value = true;
};

const close = () => {
  timepickerModal.value = false;
  //console.log(value_time.value);
  //console.log(timeFormat.value);
};
</script>

<style scoped>
.v-container {
  padding: 0;
}
.v-input {
  padding: 3px;
  font-size: 12px;
}
.v-input--horizontal :deep(.v-input__append) {
  margin-inline-end: 4px;
}
.v-text-field:deep(input:read-only) {
  background-color: #eeeeee;
  opacity: 0.8 !important;
}
</style>
