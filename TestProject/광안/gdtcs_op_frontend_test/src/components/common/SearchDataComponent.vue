<template>
  <v-row>
    <template v-for="(header, idx) in headers" :key="idx">
      <template v-if="idx == 0">
        {{ ((inputIdx = []), null) }}
      </template>
      <template v-if="header.type == 'input'">
        {{ (inputIdx.push(idx), null) }}
      </template>
      <v-col cols="2">
        <v-row>
          <v-label v-if="header.type != 'checkbox'" :text="`${header.label}`" />
          <DateComponent
            v-if="header.type == 'date'"
            :date-type="header.dateType"
            :validation="true"
            :valid-date="getValidDate(header.key, header.pair)"
            v-model="searchedData[header.key]"
            @update:modelValue="(event) => updateDate(event, header.key, header.pair)"
          />
          <v-text-field
            v-else-if="header.type == 'input'"
            ref="inputField"
            @input="(event) => validation(event, header.key, header.valid, inputIdx.indexOf(idx))"
            @update:focused="(event) => updateFocus(event, header.key, inputIdx.indexOf(idx))"
            @click:clear="clickClear(header.key)"
            @keydown.tab.prevent=""
            :key="header.key + idx"
            :maxlength="header.maxLength ? header.maxLength : 100"
            :style="{ width: `${header.width ? header.width : '90px'}` }"
            variant="outlined"
            density="compact"
            hide-details
            clearable
          />
          <v-number-input
            v-else-if="header.type == 'inputButton'"
            v-model="searchedData[header.key]"
            @input="
              (event) => {
                if (/[+-.ㄱ-힣]/.test(event.data)) searchedData[header.key] = '';
              }
            "
            :style="{ width: `${header.width ? header.width : '90px'}` }"
            variant="outlined"
            hide-details
            density="compact"
            clearable
            :min="header.min ? header.min : 0"
            :max="header.max ? header.max : 60"
            :step="header.step ? header.step : 1"
          />
          <v-select
            v-else-if="header.type == 'select'"
            v-model="searchedData[header.key]"
            :style="{ width: `${header.width ? header.width : '80px'}` }"
            item-title="title"
            item-value="value"
            :items="header.option"
            :menu-props="{ contentClass: 'selectItemClass' }"
            variant="outlined"
            density="compact"
            hide-details
          />
          <v-checkbox
            v-else-if="header.type == 'checkbox'"
            v-model="searchedData[header.key]"
            :label="header.label"
            variant="outlined"
            density="compact"
            hide-details
          />
          <v-radio-group v-else-if="header.type == 'radio'" v-model="searchedData[header.key]" :disabled="header.disabled" hide-details>
            <v-row>
              <v-radio v-for="item in header.option" :label="item.title" :value="item.value" :key="item" />
            </v-row>
          </v-radio-group>
          <v-text-field
            v-else-if="header.type == 'time'"
            v-model="searchedData[header.key]"
            type="input"
            variant="outlined"
            density="compact"
            hide-details
          />
        </v-row>
      </v-col>
    </template>
    <v-spacer />
    <slot name="header_btn"></slot>
  </v-row>
</template>

<script setup>
import { nextTick, ref, onUpdated } from 'vue';
import { getRegex } from '@/utils/common';

const searchedData = defineModel({
  type: Object,
  required: false,
});

const props = defineProps({
  headers: {
    type: Array,
    required: false,
  },
});

const inputField = ref(null);
const inputIdx = ref([]);

onUpdated(() => {
  inputIdx.value.forEach((value, idx) => {
    inputField.value[idx].$el.querySelector('input').focus();
    inputField.value[idx].$el.querySelector('input').blur();
  });
});

const getPairState = (state) => state.replace(/^START|END/, (match) => (match == 'START' ? 'END' : 'START'));
const getPairKey = (pair) => props.headers.find((obj) => obj.pair.state == getPairState(pair.state) && obj.pair.name == pair.name)?.key;

const dateRangeCheck = (date, key, pairKey, state) => {
  if (state == 'START_DATE') {
    if (date > searchedData.value[pairKey] || date == '') searchedData.value[key] = searchedData.value[pairKey];
  } else if (state == 'END_DATE') {
    if (date < searchedData.value[pairKey] || date == '') searchedData.value[key] = searchedData.value[pairKey];
  } else console.error('SearcheData header type error');
};
/**
 * 차기 프로젝트에서 pair 만 쓰는걸로 통일.
 * pair.state == 기존 key (START_DATE, END_DATE)
 * @param date
 * @param key
 * @param pair: {name: WORK_DATE, state: START_DATE}, {name: WORK_DATE, state: END_DATE} name이 같고 state는 START,END 묶음이 한쌍
 */
const updateDate = (date, key, pair = { name: null, state: null }) => {
  if (pair.name) dateRangeCheck(date, key, getPairKey(pair), pair.state);
  // 기존 호환용 _ 차기 프로젝트에서는 필요 X
  else if (key == 'START_DATE' || key == 'END_DATE') dateRangeCheck(date, key, getPairState(key), key);
};
const getValidDate = (key, pair = { name: null, state: null }) => {
  if (pair.name) return { [getPairState(pair.state)]: searchedData.value[getPairKey(pair)] };
  // 기존 호환용 _ 차기 프로젝트에서는 필요 X
  else if (key == 'START_DATE' || key == 'END_DATE') return { [getPairState(key)]: searchedData.value[getPairState(key)] };
  return {};
};

/* text-field 입력제한 함수 */
const clickClear = (key) => (searchedData.value[key] = '');

const updateFocus = async (event, key, idx) => {
  await nextTick();
  inputField.value[idx].$el.querySelector('input').value = searchedData.value[key];
};

let koreanCheck = false;
const validation = (event, key, valid, idx) => {
  if (valid) {
    const validList = valid.split('|');

    let originValue = event.target._value;
    let regexString = '';
    validList.forEach((element) => {
      regexString += getRegex(element);
    });

    const regex = new RegExp(`[^${regexString}]`, 'gi');
    originValue = originValue.replaceAll(regex, '');

    if (validList.indexOf('korean') < 0) {
      if (event.inputType === 'insertCompositionText') koreanCheck = true;
      else if (event.inputType === 'deleteContentBackward' && koreanCheck) {
        originValue = searchedData.value[key];
        koreanCheck = false;
      } else koreanCheck = false;
    }

    inputField.value[idx].$el.querySelector('input').value = originValue;
    searchedData.value[key] = originValue;
  } else {
    searchedData.value[key] = inputField.value[idx].$el.querySelector('input').value;
  }
};
</script>

<style scoped>
.v-row {
  margin: 3px 0 !important;
  align-items: center;
}
.v-col {
  flex-basis: content;
  align-items: center;
  min-width: 50px;
  max-width: 300px;
}
.v-text-field,
.v-date-field:deep(.v-text-field) {
  min-width: 50px;
  max-width: 240px;
}
.v-label,
.v-date-field:deep(.v-label) {
  margin: 0 5px;
  font-weight: bold;
}
.v-checkbox:deep(.v-label) {
  margin: 0;
}
.v-number-input:deep(.v-number-input__control .v-btn) {
  width: auto;
}
.v-text-field:deep(input::-webkit-calendar-picker-indicator) {
  margin: 0;
  cursor: pointer;
}
.v-btn {
  height: 24px;
}
</style>
