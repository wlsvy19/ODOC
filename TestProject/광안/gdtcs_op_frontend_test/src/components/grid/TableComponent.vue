<template>
  <div v-if="isLoading" class="loading-overlay">
    <div class="loading-content">
      <v-progress-circular indeterminate color="#0086E5" />
      <div class="loading-text">정렬중...</div>
    </div>
  </div>
  <div class="table-container" tabindex="0" @keydown="handleKeyDown" ref="tableContainer">
    <div class="table-header" :class="[{ 'header-style-no-data': !mainItems.length }, customHeaderRowStyle()]" ref="tableHeader">
      <div v-if="props.rowType === '1'">
        <div class="flex-class">
          <template v-for="header in headers" :key="header.key">
            <div :class="['table-header-style', header.customHeaderCellStyle]" :style="{ flex: '0 0 ' + header.width + 'px' }">
              <template v-if="header.key === 'checkBox'">
                <input type="checkbox" style="margin-left: 5px" v-model="allSelected" @change="toggleAll" />
              </template>
              <template v-else>
                <span>{{ header.title }}</span>
                <i
                  :class="[
                    'mdi',
                    sortStates[header.key] === 'initial'
                      ? 'mdi-arrow-up-down'
                      : sortStates[header.key] === 'desc'
                      ? 'mdi-arrow-down'
                      : 'mdi-arrow-up',
                    'sort-icon',
                    { active: sortStates[header.key] !== 'initial' },
                  ]"
                  @click="toggleSort(header.key)"
                >
                  <span v-if="sortPriority.indexOf(header.key) >= 0 && sortPriority.length >= 2" class="sort-priority">
                    {{ sortPriority.indexOf(header.key) + 1 }}
                  </span>
                </i>
              </template>
            </div>
          </template>
        </div>
      </div>

      <div v-if="props.rowType === '2'">
        <div class="flex-class">
          <div v-for="header in headers" :key="header.key" :class="['table-header-style', customHeaderRowStyle]" :style="twoRowHeaderStyle(header)">
            <span>{{ header.title }}</span>
          </div>
        </div>
        <div class="flex-class">
          <template v-for="header in headers" :key="header.key">
            <div
              v-for="subHeader in header.children"
              :key="subHeader.key"
              :class="['table-header-style', subHeader.customHeaderCellStyle]"
              :style="{ flex: '0 0 ' + subHeader.width + 'px' }"
            >
              <span>{{ subHeader.title }}</span>
              <i
                v-if="subHeader.key !== 'checkBox'"
                :class="[
                  'mdi',
                  sortStates[subHeader.key] === 'initial'
                    ? 'mdi-arrow-up-down'
                    : sortStates[subHeader.key] === 'desc'
                    ? 'mdi-arrow-down'
                    : 'mdi-arrow-up',
                  'sort-icon',
                  { active: sortStates[subHeader.key] !== 'initial' },
                ]"
                @click="toggleSort(subHeader.key)"
              >
                <span v-if="sortPriority.indexOf(subHeader.key) >= 0 && sortPriority.length >= 2" class="sort-priority">
                  {{ sortPriority.indexOf(subHeader.key) + 1 }}
                </span>
              </i>
            </div>
          </template>
          <div class="table-header-style-dummy"></div>
        </div>
      </div>

      <div v-if="props.rowType === 'mix'" class="flex-class">
        <template v-for="header in headers" :key="header.key">
          <div :style="mixRowHeaderContainerStyle(header)">
            <div :class="['table-header-style', customHeaderRowStyle]" :style="mixRowHeaderStyle(header)">
              <span>{{ header.title }}</span>
              <i
                v-if="!header.children"
                :class="[
                  'mdi',
                  sortStates[header.key] === 'initial' ? 'mdi-arrow-up-down' : sortStates[header.key] === 'desc' ? 'mdi-arrow-down' : 'mdi-arrow-up',
                  'sort-icon',
                  { active: sortStates[header.key] !== 'initial' },
                ]"
                @click="toggleSort(header.key)"
              >
                <span v-if="sortPriority.indexOf(header.key) >= 0 && sortPriority.length >= 2" class="sort-priority">
                  {{ sortPriority.indexOf(header.key) + 1 }}
                </span>
              </i>
            </div>
            <template v-if="header.children">
              <div class="flex-class">
                <div
                  v-for="subHeader in header.children"
                  :key="subHeader.key"
                  :class="['table-header-style', subHeader.customHeaderCellStyle]"
                  :style="mixRowSubHeaderStyle(subHeader, header)"
                >
                  <span v-if="sortPriority.indexOf(subHeader.key) >= 0 && sortPriority.length >= 2" class="sort-priority">
                    {{ sortPriority.indexOf(subHeader.key) + 1 }}
                  </span>
                  <span>{{ subHeader.title }}</span>
                  <i
                    v-if="subHeader.key !== 'checkBox'"
                    :class="[
                      'mdi',
                      sortStates[subHeader.key] === 'initial'
                        ? 'mdi-arrow-up-down'
                        : sortStates[subHeader.key] === 'desc'
                        ? 'mdi-arrow-down'
                        : 'mdi-arrow-up',
                      'sort-icon',
                      { active: sortStates[subHeader.key] !== 'initial' },
                    ]"
                    @click="toggleSort(subHeader.key)"
                  >
                  </i>
                </div>
              </div>
            </template>
          </div>
        </template>
      </div>
    </div>

    <VirtualScroller
      :items="mainItems.length ? mainItems : [{}]"
      :itemSize="props.itemSize"
      :style="{ height: virtualHeight }"
      @scroll="handleScroll"
      ref="virtualScroller"
    >
      <template #item="{ item, options }">
        <div
          v-if="mainItems.length"
          class="table-body"
          :class="[customBodyRowStyle(item), { 'selected-row': item.isSelected && !item.isChecked }]"
          :style="{ width: totalWidth + 'px' }"
          @mouseover="handleMouseOver"
          @mouseleave="handleMouseLeave"
          @click="(event) => handleRowClick(item, event)"
          @dblclick="(event) => handleRowDblClick(options.index, item)"
        >
          <div
            v-for="body in flatHeaders()"
            :key="body.key"
            :class="['table-body-style', body.customBodyCellStyle, { 'selected-row': item.isSelected && !item.isChecked }]"
            :style="{ flex: '0 0 ' + body.width + 'px' }"
          >
            <template v-if="body.key === 'checkBox'">
              <input type="checkbox" style="margin-left: 5px" v-model="item.isChecked" @change="emitSelectedItems" />
            </template>
            <template v-else-if="body.key === 'HAND_CNT'">
              {{ comma(item[body.key]) }}
            </template>
            <template v-else>
              {{ item[body.key] }}
            </template>
          </div>
        </div>
        <div v-else class="body-style-no-data" :style="[{ width: totalWidth + 'px' }]">조회된 데이터가 없습니다.</div>
      </template>
    </VirtualScroller>
    <v-row v-if="footerContents" class="footer-container">
      <template v-for="contents in footerContents" :key="contents.key">
        <span class="footer-contents-title">{{ contents.title }}</span>
        <input
          class="footer-contents-value"
          :value="contents.value"
          :style="[{ width: contents.width ? contents.width + 'px' : '80' + 'px' }]"
          disabled
        />
        <span class="footer-contents-unit">{{ contents.unit }}</span>
      </template>
    </v-row>
    <v-row v-if="slots['table-footer']" class="sticky-table-footer">
      <slot name="table-footer"></slot>
    </v-row>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onActivated, computed, useSlots } from 'vue';
import VirtualScroller from 'primevue/virtualscroller';
import { comma, showMessage } from '@/utils/common';
import { useScrollStore } from '@/stores/index';
import dayjs from 'dayjs';

// 각 컴포넌트의 메인 아이템
const mainItems = ref([]);

const getItem = (index) => {
  return mainItems.value[index];
};

defineExpose({
  mainItems,
  getItem,
});

const scrollStore = useScrollStore();

// 스크롤 위치 기억
const virtualScroller = ref(null);

const slots = useSlots();

onMounted(() => {
  mainItems.value = props.contents.map((item, index) => ({ ...item, originalIndex: index, isSelected: false }));

  if (tableContainer.value) {
    tableContainer.value.focus();
  }
});

onActivated(() => {
  virtualScroller.value.$el.scrollTop = scrollStore.getScrollValue(props.scrollKey + '-vertical');
  virtualScroller.value.$el.scrollLeft = scrollStore.getScrollValue(props.scrollKey + '-horizontal');
});

const props = defineProps({
  itemSize: {
    default: 24,
    type: Number,
    required: false,
  },
  headers: {
    type: Array,
    required: true,
  },
  contents: {
    type: Array,
    required: true,
  },
  footerContents: {
    type: Array,
    required: false,
  },
  scrollKey: {
    type: String,
    required: true,
  },
  rowType: {
    default: '1',
    type: String,
    required: false,
  },
  heightOffset: {
    type: Number,
    default: 262,
  },
  heightPercent: {
    type: Number,
    default: 100,
  },
  customHeaderRowStyle: {
    type: Function,
    default: () => '',
    required: false,
  },
  customBodyRowStyle: {
    type: Function,
    default: () => '',
    required: false,
  },
});

const emits = defineEmits(['update:selectedItems', 'grid-click-event', 'grid-dbl-click-event', 'update:mainItems']);

const emitSelectedItems = () => {
  const selectedItems = mainItems.value.filter((i) => i.isChecked);
  emits('update:selectedItems', selectedItems);
};

// 키보드 방향키 기능 start
const selectedIndex = ref(-1);
const tableContainer = ref(null);

const handleKeyDown = (event) => {
  if (!mainItems.value.length) return;

  const currentIndex = mainItems.value.findIndex((item) => item.isSelected);

  switch (event.key) {
    case 'ArrowUp':
      event.preventDefault();
      if (currentIndex > 0) {
        selectRow(currentIndex - 1);
      }
      break;
    case 'ArrowDown':
      event.preventDefault();
      if (currentIndex < mainItems.value.length - 1) {
        selectRow(currentIndex + 1);
      }
      break;
  }
};

const selectRow = (index) => {
  mainItems.value.forEach((item) => (item.isSelected = false));

  if (index >= 0 && index < mainItems.value.length) {
    const selectedItem = mainItems.value[index];
    selectedItem.isSelected = true;
    selectedIndex.value = index;

    ensureRowVisible(index);

    emits('grid-click-event', selectedItem);
  }
};

// 선택된 row가 보이도록 스크롤 조정
const ensureRowVisible = (index) => {
  if (!virtualScroller.value) return;

  const scroller = virtualScroller.value.$el;
  const itemHeight = props.itemSize;
  const scrollerHeight = scroller.clientHeight;
  const itemTop = index * itemHeight;
  const itemBottom = itemTop + itemHeight;
  const scrollTop = scroller.scrollTop;
  const scrollBottom = scrollTop + scrollerHeight;

  if (itemTop < scrollTop) {
    scroller.scrollTop = itemTop;
  } else if (itemBottom > scrollBottom) {
    scroller.scrollTop = itemBottom - scrollerHeight;
  }
};

const handleRowClick = (item, event) => {
  if (event.target.type === 'checkbox') {
    return;
  }

  mainItems.value.forEach((i) => (i.isSelected = false));

  item.isSelected = true;
  selectedIndex.value = mainItems.value.findIndex((i) => i === item);

  emitSelectedItems();
  emits('grid-click-event', item);

  if (tableContainer.value) {
    tableContainer.value.focus();
  }
};

const handleRowDblClick = (index, item) => {
  mainItems.value.forEach((i) => (i.isSelected = false));
  item.isSelected = true;
  emits('grid-dbl-click-event', index, item);
};

const handleMouseOver = (event) => {
  event.currentTarget.classList.add('hovered-row');
};

const handleMouseLeave = (event) => {
  event.currentTarget.classList.remove('hovered-row');
};

const allSelected = ref(false);

const toggleAll = () => {
  const allChecked = allSelected.value;
  mainItems.value.forEach((item) => {
    item.isChecked = allChecked;
  });
  emitSelectedItems();
};

const virtualHeight = computed(() => {
  return `calc((100vh - ${props.heightOffset}px) * ${props.heightPercent} / 100)`;
});

const totalWidth = computed(() => {
  return props.headers.reduce((sum, header) => {
    if (header.children) {
      return sum + header.children.reduce((childSum, child) => childSum + parseInt(child.width), 0);
    }
    return sum + parseInt(header.width);
  }, 0);
});

const flatHeaders = () => {
  return props.headers.flatMap((header) => header.children || [header]);
};

const twoRowHeaderStyle = (header) => {
  if (header.children) {
    const width = header.children.reduce((sum, child) => sum + parseInt(child.width), 0);
    return { flex: `0 0 ${width}px` };
  }
  return { flex: `0 0 ${header.width}px` };
};

const mixRowHeaderContainerStyle = (header) => {
  const width = header.children ? header.children.reduce((sum, child) => sum + parseInt(child.width), 0) : header.width;
  return {
    flex: `0 0 ${width}px`,
    display: 'flex',
    flexDirection: 'column',
  };
};

const mixRowHeaderStyle = () => {
  return {
    flex: '1',
    textAlign: 'center',
    alignItems: 'center',
  };
};

const mixRowSubHeaderStyle = (subHeader, parentHeader) => {
  const parentWidth = parentHeader.children.reduce((sum, child) => sum + parseInt(child.width), 0);
  const flexBasis = (parseInt(subHeader.width) / parentWidth) * 100;
  return {
    flex: `0 0 ${flexBasis}%`,
    textAlign: 'center',
  };
};

const handleVerticalScroll = (event) => {
  scrollStore.setScrollValue(props.scrollKey + '-vertical', event.target.scrollTop);
};

const handleHorizontalScroll = (event) => {
  scrollStore.setScrollValue(props.scrollKey + '-horizontal', event.target.scrollLeft);
};

const tableHeader = ref(null);
const handleScroll = (event) => {
  if (tableHeader.value) {
    tableHeader.value.scrollLeft = event.target.scrollLeft;
  }
  handleVerticalScroll(event);
  handleHorizontalScroll(event);
};

watch(
  () => props.contents,
  (newVal) => {
    mainItems.value = newVal.map((item, index) => ({ ...item, originalIndex: index }));
    allSelected.value = false;

    if (mainItems.value.length > 0) {
      Object.keys(sortStates.value).forEach((key) => {
        sortStates.value[key] = 'initial';
      });
      sortPriority.value = [];
    }
  },
  { deep: true },
);

const sortStates = ref({});

watch(
  () => props.headers,
  (newVal) => {
    sortStates.value = newVal.reduce((acc, header) => {
      if (header.children) {
        header.children.forEach((child) => {
          acc[child.key] = 'initial';
        });
      } else {
        acc[header.key] = 'initial';
      }
      return acc;
    }, {});
  },
  { immediate: true },
);

// 정렬 우선순위 추적
const sortPriority = ref([]);

const toggleSort = (key) => {
  if (mainItems.value.length === 0) {
    showMessage('정렬할 데이터가 없습니다.', 'error');
    return;
  }
  const currentState = sortStates.value[key];
  let newState;

  switch (currentState) {
    case 'initial':
      newState = 'desc';
      break;
    case 'desc':
      newState = 'asc';
      break;
    case 'asc':
      newState = 'initial';
      break;
  }

  sortStates.value[key] = newState;

  if (newState === 'initial') {
    sortPriority.value = sortPriority.value.filter((k) => k !== key);
  } else if (!sortPriority.value.includes(key)) {
    sortPriority.value.push(key);
  }
  if (sortPriority.value.length > 1) {
    showMessage('컬럼의 숫자는 정렬에 대한 우선순위 입니다.', 'success');
  }
  sortData();
};

const isLoading = ref(false);

const sortData = async () => {
  isLoading.value = true;
  await new Promise((resolve) => setTimeout(resolve, 100));

  mainItems.value.sort((a, b) => {
    for (const key of sortPriority.value) {
      const order = sortStates.value[key];

      if (order === 'initial') {
        continue;
      }

      const valueA = a[key];
      const valueB = b[key];

      if (valueA === null || valueA === undefined) {
        return order === 'asc' ? 1 : -1;
      }
      if (valueB === null || valueB === undefined) {
        return order === 'asc' ? -1 : 1;
      }

      let comparison;
      if (key.toLowerCase().includes('time') || isTimeFormat(valueA)) {
        const timeA = dayjs(valueA, 'HH:mm:ss');
        const timeB = dayjs(valueB, 'HH:mm:ss');
        comparison = timeA.diff(timeB);
      } else if (isDateFormat(valueA)) {
        const dateA = dayjs(valueA, isDateTimeFormat(valueA) ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD');
        const dateB = dayjs(valueB, isDateTimeFormat(valueB) ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD');
        comparison = dateA.diff(dateB);
      } else {
        const isNumericOrComma = (str) => /^[\d,]+$/.test(str);
        const numA = typeof valueA === 'string' && isNumericOrComma(valueA) ? parseInt(valueA.replace(/,/g, ''), 10) : valueA;
        const numB = typeof valueB === 'string' && isNumericOrComma(valueB) ? parseInt(valueB.replace(/,/g, ''), 10) : valueB;

        if (typeof numA === 'bigint' && typeof numB === 'bigint') {
          comparison = numA - numB;
        } else {
          comparison = String(valueA).localeCompare(String(valueB));
        }
      }

      if (comparison !== 0) {
        return order === 'asc' ? comparison : -comparison;
      }
    }

    return a.originalIndex - b.originalIndex;
  }); // end sort

  isLoading.value = false;
};

const isTimeFormat = (value) => {
  return dayjs(value, 'HH:mm', true).isValid() || dayjs(value, 'HH:mm:ss', true).isValid();
};

const isDateFormat = (value) => {
  return dayjs(value, 'YYYY-MM-DD', true).isValid() || dayjs(value, 'YYYY-MM-DD HH:mm:ss', true).isValid();
};

const isDateTimeFormat = (value) => {
  return dayjs(value, 'YYYY-MM-DD HH:mm:ss', true).isValid();
};
</script>

<style scoped>
.table-container {
  width: 100%;
}

.flex-class {
  display: flex;
}

.table-header {
  overflow-x: hidden;
  display: flex;
  background-color: #f5f5f5;
  color: #666666;
  text-align: center;
  font-size: 12px;
  font-weight: 600;
}

.table-header-style {
  border-right: 1px solid #e0e0e0;
  border-top: 1px solid #e0e0e0;
  border-bottom: 1px solid #e0e0e0;
  white-space: nowrap;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px 0px 4px 0px;
}
.table-header-style > span {
  margin-left: 10px;
  flex-grow: 1;
}

.header-style-no-data {
  width: 100%;
}

.table-body {
  display: flex;
  text-align: center;
  font-size: 12px;
}

.table-body-style {
  padding: 2px;
  border-bottom: 1px solid #e0e0e0;
  border-right: 1px solid #e0e0e0;
  flex: 1 0 auto;
}

.body-style-no-data {
  padding: 5px;
  border-bottom: 1px solid #e0e0e0;
  border-right: 1px solid #e0e0e0;
  text-align: center;
  font-size: 13px;
}

.p-virtualscroller {
  overflow-y: scroll;
}

.selected-row {
  background-color: #f0f8ff;
  color: #1976d2;
  font-weight: bold;
}

.hovered-row {
  background-color: #f0f8ff !important;
  color: #1976d2;
}

.mdi {
  cursor: pointer;
}

.sort-priority {
  font-size: 8px;
  display: flex;
  color: #0086e5;
}

.sort-icon {
  color: #ccc;
  transition: color 0.3s;
  padding: 0px 3px 0px 0px;
  cursor: pointer;
}

/* .sort-icon:hover {
  color: black;
} */

.sort-icon.active {
  color: black;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.loading-text {
  margin-top: 8px;
  color: red;
  font-weight: bold;
  font-size: 15px;
}

.footer-container {
  padding: 3px;
  height: 30px;
  border-top: 1px solid #d3d3d3;
  font-size: 12px;
  flex: auto;
  align-items: center;
}
.footer-contents-title {
  margin-left: 15px;
  margin-right: 10px;
}
.footer-contents-unit {
  margin: 0 7px;
}
.footer-contents-value {
  text-align: right;
  padding-right: 7px;
  align-items: center;
  border-radius: 3px;
  border: 1px solid #d3d3d3;
  font-weight: 400;
}

/* drag 안되게  */
div.table-container {
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
/* scroll bar css */
div.table-header {
  overflow-y: scroll;
  &::-webkit-scrollbar {
    background-color: #f1f1f1;
  }
}

.table-container:focus {
  outline: none;
}

/* customClass 시작 */
.WorkStat-custom-body-row-style-work-fin {
  text-decoration: line-through;
  color: red;
}

/* header row 스타일 추가 예시 */
/* .PassPrimDtl-custom-header-row-style {
  background-color: aqua;
} */

/* body에서 특정 row 스타일 추가 예시 */
/* .PassPrimDtl-body-row-style {
  background-color: red;
} */

/* header 특정 cell 스타일 추가 예시 */
/* .PassPrimDtl-ROW_NUMBER-header-cell-style {
  background-color: black;
} */

.PassPrimDtl-COMMENTS-body-cell-style {
  text-align: left !important;
  padding-left: 10px;
  padding-right: 20px;
}
.body-row-style-processed {
  background-color: wheat;
  color: #474747;
}
.body-row-style-processed-green {
  background-color: #eeffee;
  color: #1f7f1f;
}
.body-row-style-canceled {
  background-color: #fef2f2;
  color: #7f1d1d;
}
.body-row-style-fare {
  text-align: right !important;
  padding-left: 10px;
  padding-right: 20px;
}
.table-body-style-left {
  text-align: left !important;
  padding: 2px 2px 2px 10px;
}
.table-body-style-right {
  text-align: right !important;
  padding: 2px 10px 2px 2px;
}
.table-body-style-summary-1 {
  background-color: #f0f0ee;
  font-weight: 600;
}
.table-body-style-summary-2 {
  background-color: #f6f6f0;
}
.CarLineMonitor-COMMENTS-body-cell-style-red {
  color: red;
}
.CarLineMonitor-COMMENTS-body-cell-style-black {
  color: black;
}
.DayManagement-holiday {
  color: #dc2626;
  font-weight: bold;
}
.DayManagement-holiday-exemption {
  color: #7f1d1d;
  background-color: #fef2f2;
  font-weight: bold;
}
/* customClasss 끝 */
</style>
