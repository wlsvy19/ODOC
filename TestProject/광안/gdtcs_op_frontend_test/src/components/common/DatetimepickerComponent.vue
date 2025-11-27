<template>
  <v-text-field
    v-model="dateFormat"
    type="date"
    max="9999-12-31"
    variant="outlined"
    density="compact"
    hide-details
    append-inner-icon="mdi-calendar"
    @click:appendInner="showDatepicker()"
    style="float: left"
  />

  <TimepickerComponent v-model="timeFormat" format="HHmm" style="float: left" @update:modelValue="updateTime" />

  <v-dialog v-model="isDatepickerVisable" width="auto">
    <DatepickerComponent v-model="dateFormat" dateType="day" @update:modelValue="updateDate" />
  </v-dialog>
</template>

<script setup>
import dayjs from 'dayjs';
import { ref, watch } from 'vue';

const value_datetime = defineModel({
  type: String,
  required: true,
  default: '999912312359',
});

const props = defineProps({
  format: {
    type: String,
    required: false,
    default: 'YYYYMMDDHHmm',
  },
});

const dateFormat = ref(dayjs().format('YYYY-MM-DD'));
const timeFormat = ref(dayjs().format('HHmm'));
const isDatepickerVisable = ref(false);

watch(
  value_datetime,
  () => {
    try {
      if (props.format == 'YYYYMMDDHHmm') {
        const YYYY = value_datetime.value.slice(0, 4);
        const MM = value_datetime.value.slice(4, 6);
        const DD = value_datetime.value.slice(6, 8);
        const HH = value_datetime.value.slice(8, 10);
        const mm = value_datetime.value.slice(10, 12);
        dateFormat.value = `${YYYY}-${MM}-${DD}`;
        timeFormat.value = `${HH}${mm}`;
        value_datetime.value = `${YYYY}${MM}${DD}${HH}${mm}`;
      }
    } catch (err) {
      console.log('시간형식 변환 중에 오류 발생 ' + err);
      timeFormat.value = `00:00`;
    }
  },
  { immediate: true },
);

const updateDate = (newDateFormat) => {
  const YYYY = newDateFormat.slice(0, 4);
  const MM = newDateFormat.slice(5, 7);
  const DD = newDateFormat.slice(8, 10);
  const HH = value_datetime.value.slice(8, 10);
  const mm = value_datetime.value.slice(10, 12);

  value_datetime.value = `${YYYY}${MM}${DD}${HH}${mm}`;

  isDatepickerVisable.value = false;
};

const updateTime = (newTime) => {
  console.log('newTime: ' + newTime);
  const YYYY = value_datetime.value.slice(0, 4);
  const MM = value_datetime.value.slice(4, 6);
  const DD = value_datetime.value.slice(6, 8);
  const HH = newTime.slice(0, 2);
  const mm = newTime.slice(2, 4);

  value_datetime.value = `${YYYY}${MM}${DD}${HH}${mm}`;

  isDatepickerVisable.value = false;
};

const showDatepicker = () => {
  isDatepickerVisable.value = true;
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
