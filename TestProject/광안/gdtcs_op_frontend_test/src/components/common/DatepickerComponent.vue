<template>
  <v-locale-provider locale="ko">
    <v-date-picker v-if="dateType == 'day'" v-model="date" show-adjacent-year :allowed-dates="allowedDates" @update:model-value="updateDate">
      <template v-slot:header></template>
    </v-date-picker>
    <template v-else-if="dateType == 'month'">
      <v-date-picker-controls
        :text="year + `년`"
        view-mode="month"
        class="v-sheet"
        @click:month="clickYear"
        @click:next="clickNext"
        @click:prev="clickPrev"
      />
      <v-date-picker-months
        v-if="!activeYear"
        v-model="month"
        class="v-sheet v-theme--light v-picker v-date-picker"
        @update:modelValue="updateMonth"
      />
      <v-date-picker-years v-else v-model="year" class="v-sheet v-theme--light v-picker v-date-picker" @update:modelValue="updateMonthYear" />
    </template>
    <template v-else-if="dateType == 'year'">
      <v-date-picker-controls :text="year + `년`" class="v-sheet" @click:next="clickNext" @click:prev="clickPrev" />
      <v-date-picker-years v-model="year" class="v-sheet v-theme--light v-picker v-date-picker" @update:modelValue="updateYear" />
    </template>
  </v-locale-provider>
</template>

<script setup>
import { ref } from 'vue';
import dayjs from 'dayjs';

const selectedDate = defineModel({
  type: String,
  required: true,
});

const props = defineProps({
  dateType: {
    type: String,
    required: true,
  },
  validation: {
    type: Boolean,
    required: false,
    default: false,
  },
  validDate: {
    type: Object,
    required: false,
  },
});

const activeYear = ref(false);

const date = ref(new Date(selectedDate.value));
const month = ref(Number(selectedDate.value.slice(5)) - 1);
const year = ref(Number(selectedDate.value.slice(0, 4)));

const allowedDates = (date) => {
  if (props.validation == true) {
    if (Object.keys(props.validDate).length === 0) return true;

    const key = Object.keys(props.validDate)[0];
    if (key === 'END_DATE') return dayjs(date).format('YY-MM-DD') <= dayjs(props.validDate[key]).format('YY-MM-DD');
    else if (key === 'START_DATE') return dayjs(date).format('YY-MM-DD') >= dayjs(props.validDate[key]).format('YY-MM-DD');
  }
  return true;
};

const updateDate = (newDate) => (selectedDate.value = dayjs(newDate).format('YYYY-MM-DD'));
const updateMonth = (newMonth) => (selectedDate.value = dayjs(year.value + '-' + (newMonth + 1)).format('YYYY-MM'));
const updateMonthYear = (newYear) => {
  year.value = newYear;
  activeYear.value = false;
};
const updateYear = (newYear) => (selectedDate.value = dayjs(newYear.toString()).format('YYYY'));

const clickNext = () => (year.value += 1);
const clickPrev = () => (year.value -= 1);
const clickYear = () => (activeYear.value = !activeYear.value);
</script>

<style scoped>
.v-date-picker:deep(.v-picker-title) {
  text-align: center;
  padding-inline: 12px 12px;
  font-weight: 1000;
}
/* color */
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__weekday) {
  font-weight: 1000;
}
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__weekday:nth-child(1)) {
  color: red;
}
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__weekday:nth-child(7)) {
  color: blue;
}
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__day--week-start > .v-btn) {
  color: red;
}
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__day--week-end > .v-btn) {
  color: blue;
}
.v-date-picker:deep(.v-date-picker-month__days > .v-date-picker-month__day--selected > .v-btn) {
  background-color: antiquewhite;
  color: green;
}
.v-date-picker:deep(.v-btn--variant-outlined) {
  border: 0;
}
.v-date-picker-years {
  overflow-y: scroll;
}
.v-date-picker-controls:deep(.v-date-picker-controls__mode-btn) {
  display: none;
}
</style>
