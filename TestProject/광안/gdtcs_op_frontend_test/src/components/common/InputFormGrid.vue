<template>
  <v-form ref="inputForm" v-model="isValid">
    <table>
      <tbody>
        {{
          ((length = 0), null)
        }}
        <tr v-for="(header, idx) in headers" :key="idx">
          <template v-if="idx == length">
            {{ ((length = length + (header.size == 1 ? header.size : colsPerRow)), null) }}
            <th v-if="gridNameHeader != ''" :style="{ width: `${gridNameHeaderWidth}` }">
              {{ gridNameHeader }}
            </th>
            <template v-for="(childHeader, childIdx) in headers.slice(idx, length)" :key="childHeader">
              <th v-if="tableHeaderDisplay" :style="{ width: `${tableHeaderWidth}`, height: `${rowHeight}` }">
                <span v-if="childHeader.required == 'Y'" style="color: red">* </span>{{ childHeader.title }}
              </th>
              <td :colspan="childHeader.size == 1 ? 2 * (colsPerRow - childIdx) - 1 : 0">
                <v-select
                  v-if="childHeader.option == 'select'"
                  v-model="inputedData[childHeader.key]"
                  item-title="title"
                  item-value="value"
                  :items="childHeader.selectItem"
                  :menu-props="{ contentClass: 'selectItemClass' }"
                  @update:modelValue="(value) => selectChanged(value, childHeader.key)"
                  :readonly="childHeader.disabled"
                  variant="outlined"
                  density="compact"
                  hide-details
                />
                <v-row v-else-if="childHeader.option == 'checkbox'">
                  <v-checkbox
                    v-for="(checkboxList, idx) in childHeader.item"
                    :key="idx"
                    v-model="inputedData[checkboxList.key]"
                    :label="checkboxList.label"
                    @update:modelValue="(value) => selectChanged(value, checkboxList.key)"
                    :disabled="checkboxList.disabled || false"
                    variant="outlined"
                    density="compact"
                    hide-details
                  />
                </v-row>
                <div class="date-component" v-else-if="childHeader.option == 'date'">
                  <DateComponent
                    :date-type="childHeader.type"
                    :read-only="childHeader.disabled"
                    v-model="inputedData[childHeader.key]"
                    :valid-date="{}"
                  />
                </div>
                <v-number-input
                  v-else-if="childHeader.option == 'numberInput'"
                  v-model="inputedData[childHeader.key]"
                  @input="
                    (event) => {
                      if (/[+-.]/.test(event.data)) inputedData[childHeader.key] = 0;
                    }
                  "
                  variant="outlined"
                  hide-details
                  density="compact"
                  :clearable="childHeader.clearable"
                  :disabled="childHeader.disabled"
                  :readonly="childHeader.readonly"
                  :min="0"
                  :max="childHeader.max ? childHeader.max : 10000"
                  :step="childHeader.step ? childHeader.step : 100"
                  :rules="childHeader.required == 'Y' ? addRequiredRule(childHeader.rules) : childHeader.rules"
                />
                <TimepickerComponent
                  v-else-if="childHeader.option == 'timepicker'"
                  v-model="inputedData[childHeader.key]"
                  :readonly="childHeader.readonly"
                  :disabled="childHeader.disabled"
                  :format="childHeader.format"
                />
                <v-label v-else-if="childHeader.option == 'label'" :text="`${!inputedData[childHeader.key] ? '-' : inputedData[childHeader.key]}`" />
                <v-text-field
                  v-else
                  v-model="inputedData[childHeader.key]"
                  :readonly="childHeader.disabled"
                  :clearable="!childHeader.disabled"
                  @input="childHeader.changed !== undefined ? childHeader.changed($event.target.value, childHeader.key) : null"
                  :type="childHeader.type"
                  :rules="childHeader.required == 'Y' ? addRequiredRule(childHeader.rules) : childHeader.rules"
                  variant="outlined"
                  density="compact"
                  hide-details
                />
              </td>
            </template>
          </template>
        </tr>
      </tbody>
    </table>
  </v-form>
</template>

<script setup>
import { InputRules } from '@/utils/rules';
import { onUpdated } from 'vue';

onUpdated(() => {
  if (props.covertNumberToString == true) {
    for (const key in inputedData.value) {
      if (typeof inputedData.value[key] == 'number') {
        inputedData.value[key] = inputedData.value[key].toString();
      }
    }
  }
  convertStringToNumberForNumberInput();
  inputForm.value?.validate();
});

const inputedData = defineModel({
  type: Object,
  required: true,
});

const isValid = defineModel('isValid', {
  type: Boolean,
  required: false,
  default: true,
});

const inputForm = defineModel('inputForm', {
  type: Object,
  required: false,
  default: {},
});

const props = defineProps({
  headers: {
    type: Array,
    required: true,
  },
  gridNameHeader: {
    type: String,
    default: '',
    required: false,
  },
  tableHeaderDisplay: {
    type: Boolean,
    default: true,
    required: false,
  },
  gridNameHeaderWidth: {
    type: String,
    default: '',
    required: false,
  },
  tableHeaderWidth: {
    type: String,
    default: '',
  },
  colsPerRow: {
    type: Number,
    required: false,
    default: 1,
  },
  selectChanged: {
    type: Function,
    required: false,
  },
  covertNumberToString: {
    // 숫자로 입력된 값을 문자열로 자동으로 변경해주는 속성
    type: Boolean,
    required: false,
    default: false,
  },
  covertStringToNumberForNumberInput: {
    // 문자열로 입력된 값을 숫자로 자동으로 변경해주는 속성
    type: Boolean,
    required: false,
    default: false,
  },
  rowHeight: {
    type: String,
    required: false,
    default: '',
  },
});

const addRequiredRule = (rules) => {
  return rules === undefined ? InputRules.requiredRule : [...rules, ...InputRules.requiredRule];
};

const selectChanged = (value, key) => {
  if (props.selectChanged === undefined) {
    return;
  }
  props.selectChanged(value, key);
};

const convertStringToNumberForNumberInput = () => {
  if (props.covertStringToNumberForNumberInput == true) {
    for (const key in props.headers) {
      if (props.headers[key].option == 'numberInput') {
        if (typeof inputedData.value[props.headers[key].key] == 'string') {
          inputedData.value[props.headers[key].key] = Number(inputedData.value[props.headers[key].key]);
        }
      }
    }
  }
};
</script>

<style scoped>
table {
  border: 1px solid #b0b0b0;
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
  border-spacing: 0;
  font-size: 12px;
}
th {
  border: 1px solid #b0b0b0;
  background-color: #f5f5f5;
  padding: 2px;
  text-align: left;
}
td {
  border: 1px solid #b0b0b0;
  text-align: center;
  white-space: nowrap;
}
.v-text-field {
  padding: 3px;
}
.date-component {
  padding: 3px;
}
.date-component:deep(.v-text-field .v-input__control) {
  width: 100%;
}
.v-text-field:deep(input:read-only),
.date-component:deep(input:read-only) {
  background-color: #eeeeee;
  opacity: 0.8 !important;
}
.v-text-field:deep(i) {
  font-size: large;
}
.v-select:deep(.v-field__input) {
  font-size: 12px;
  min-height: auto;
  padding: 2px 10px;
}
.v-label {
  color: #303133;
  opacity: 1;
}
</style>
