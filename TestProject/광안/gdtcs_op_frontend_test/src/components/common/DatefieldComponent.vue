<template>
  <template v-if="isPeriod">
    <v-text-field
      v-model="startDate"
      type="date"
      :max="endDate"
      variant="outlined"
      density="compact"
      hide-details
      append-inner-icon="mdi-calendar"
      @blur="dateValidCheck"
      @click:appendInner="showDatepicker(true)"
    />
    <v-label style="margin: 0 5px">~</v-label>
    <v-text-field
      v-model="endDate"
      type="date"
      :min="startDate"
      max="9999-12-31"
      variant="outlined"
      density="compact"
      hide-details
      append-inner-icon="mdi-calendar"
      @blur="dateValidCheck"
      @click:appendInner="showDatepicker(false)"
    />
  </template>
  <template v-else>
    <v-text-field
      v-model="oneDate"
      type="date"
      max="9999-12-31"
      variant="outlined"
      density="compact"
      hide-details
      append-inner-icon="mdi-calendar"
      @blur="dateValidCheck"
      @click:appendInner="showDatepicker()"
    />
  </template>

  <v-dialog v-model="isDatepickerVisable" width="auto">
    <DatepickerComponent
      v-if="isPeriod"
      v-model="date"
      :start-date="new Date(startDate)"
      :end-date="new Date(endDate)"
      :is-start="startFlag"
      @update:modelValue="updateDate"
    />
    <DatepickerComponent
      v-else
      v-model="date"
      :start-date="new Date(startDate)"
      :end-date="new Date('9999-12-31')"
      :is-start="true"
      @update:modelValue="updateDate"
    />
  </v-dialog>
</template>

<script setup>
import DatepickerComponent from '@/components/common/DatepickerComponent.vue';
import dayjs from 'dayjs';
import { ref, watch } from 'vue';

const props = defineProps({
  isPeriod: {
    type: Boolean,
    default: true,
  },
});

const date = ref(new Date(dayjs()));

const oneDate = ref(dayjs().format('YYYY-MM-DD'));
const startDate = ref(dayjs().format('YYYY-MM-DD'));
const endDate = ref(dayjs().format('YYYY-MM-DD'));
const startFlag = ref(true);
const isDatepickerVisable = ref(false);

const emits = defineEmits(['updateSelectedDate']);

watch(
  [startDate, endDate, oneDate],
  () => {
    emits('updateSelectedDate', {
      startDate: startDate.value,
      endDate: endDate.value,
      oneDate: oneDate.value,
    });
  },
  { immediate: true },
);

const updateDate = (newDate) => {
  const newDateDayjs = dayjs(newDate).format('YYYY-MM-DD');
  if (!props.isPeriod) oneDate.value = newDateDayjs;
  else startFlag.value ? (startDate.value = newDateDayjs) : (endDate.value = newDateDayjs);
  isDatepickerVisable.value = false;
};

const showDatepicker = (isStart) => {
  isStart ? (startFlag.value = true) : (startFlag.value = false);
  date.value = new Date(startFlag.value ? startDate.value : endDate.value);
  if (!props.isPeriod) date.value = new Date(oneDate.value);
  isDatepickerVisable.value = true;
};

const dateValidCheck = (value) => {
  if (value.target.min) {
    if (value.target.value < value.target.min) endDate.value = value.target.min;
  } else {
    if (value.target.value > value.target.max) startDate.value = value.target.max;
  }
};
</script>

<style scoped>
.v-text-field:deep(input[type='date']::-webkit-calendar-picker-indicator) {
  display: none;
}
.v-text-field:deep(.v-input__control) {
  width: fit-content;
}
.v-text-field:deep(.v-field__input) {
  padding-right: 0px;
}
.v-text-field:deep(.v-field) {
  width: 100px;
}
</style>
