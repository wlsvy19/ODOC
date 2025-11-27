<template>
  <v-number-input
    v-if="dateType == 'year'"
    v-model="year"
    :min="0"
    :max="9999"
    variant="outlined"
    density="compact"
    hide-details
    append-inner-icon="mdi-calendar"
    @update:modelValue="updateYear"
    @click:appendInner="showDatepicker(true)"
  />
  <input v-else-if="dateType == 'datetime'" type="datetime-local" v-model="selectedDate" :readonly="readOnly" />
  <v-text-field
    v-else
    v-model="selectedDate"
    :type="dateType == 'day' ? 'date' : 'month'"
    variant="outlined"
    density="compact"
    :max="dateType == 'day' ? '9999-12-31' : '9999-12'"
    hide-details
    :readonly="readOnly"
    append-inner-icon="mdi-calendar"
    @click:appendInner="showDatepicker(true)"
  />
  <v-dialog v-model="isDatepickerVisable" width="auto">
    <DatepickerComponent
      v-model="selectedDate"
      :date-type="dateType"
      :validation="validation"
      :valid-date="validDate"
      @update:modelValue="updatePickerYear"
    />
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue';
import dayjs from 'dayjs';

const selectedDate = defineModel({
  type: String,
  required: true,
  default: '9999-12-31T00:00:00',
});

const props = defineProps({
  dateType: {
    type: String,
    required: true,
    default: 'day',
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
  readOnly: {
    type: Boolean,
    required: false,
    default: false,
  },
});

const isDatepickerVisable = ref(false);
const year = ref(Number(selectedDate.value));

const updateYear = (newYear) => {
  if (newYear) selectedDate.value = dayjs(newYear.toString()).format('YYYY');
};

const showDatepicker = () => {
  if (!props.readOnly) isDatepickerVisable.value = true;
};

const updatePickerYear = (newDate) => {
  year.value = Number(newDate);
  isDatepickerVisable.value = false;
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
.v-number-input:deep(.v-number-input__control .v-btn) {
  width: auto;
}
input[type='datetime-local'] {
  height: 100%;
  width: 100%;
  border-radius: 4px;
  border: #a9a9a9 1px solid;
  text-align: left;
  padding: 0 3px;
}
input[type='datetime-local']:hover {
  border: #000000 1px solid;
}
input[type='datetime-local']::-webkit-calendar-picker-indicator {
  width: 18px;
  height: 18px;
  opacity: 0.5;
}
input[type='datetime-local']::-webkit-calendar-picker-indicator:hover {
  opacity: 1;
}
</style>
