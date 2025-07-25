<template>
  <slot name="label">
    <v-label> 근무기간 </v-label>
  </slot>
  <template v-if="isPeriod">
    <v-col cols="2">
      <v-text-field
        v-model="startDate"
        type="date"
        :max="endDate"
        hide-details
        append-inner-icon="mdi-calendar"
        @blur="dateValidCheck"
        @click:appendInner="showDatepicker(true)"
      ></v-text-field>
    </v-col>
    <v-label>~</v-label>
    <v-col cols="2">
      <v-text-field
        v-model="endDate"
        type="date"
        :min="startDate"
        max="9999-12-31"
        hide-details
        append-inner-icon="mdi-calendar"
        @blur="dateValidCheck"
        @click:appendInner="showDatepicker(false)"
      ></v-text-field>
    </v-col>
  </template>

  <template v-else>
    <v-col cols="2">
      <v-text-field
        v-model="oneDate"
        type="date"
        :max="9999 - 12 - 31"
        hide-details
        append-inner-icon="mdi-calendar"
        @blur="dateValidCheck"
        @click:appendInner="showDatepicker()"
      ></v-text-field>
    </v-col>
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
  startFlag.value ? (startDate.value = newDateDayjs) : (endDate.value = newDateDayjs);
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
    /* endDate가 startDate 보다 작을 때 */
    if (value.target.value < value.target.min) endDate.value = value.target.min;
  } else {
    /* startDate 가 endDate 보다 클 때 */
    if (value.target.value > value.target.max) startDate.value = value.target.max;
  }
};
</script>

<style scoped>
.v-text-field:deep(input[type='date']::-webkit-calendar-picker-indicator) {
  display: none;
}
/* UI */
.v-col {
  align-self: center;
}
.v-col-2 {
  max-width: 115px;
  padding: 5px;
  align-self: center;
}
.v-label {
  font-size: 12px;
  margin-right: 0px !important;
  margin-left: 5px;
}
.v-text-field:deep(input) {
  min-height: 20px;
  font-size: 12px;
  padding: 5px;
}
.v-text-field:deep(.v-field--appended) {
  padding-inline-end: 0px;
}
</style>
