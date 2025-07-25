<template>
  <div style="display: flex; justify-content: flex-end">
    <div id="filter-style" style="width: 100%">
      <v-row>
        <div style="margin: 10px 5px; margin-left: 20px">
          <v-select
            v-model="dateFilter.current.year"
            :items="dateFilter.items.year"
            @update:modelValue="updateFilterYear"
            :menu-props="{ contentClass: 'selectItemClass' }"
            variant="outlined"
            density="compact"
            hide-details
          />
        </div>
        <div style="margin: 10px 5px">
          <v-select
            v-model="dateFilter.current.month"
            :items="dateFilter.items.month"
            @update:modelValue="movePage"
            :menu-props="{ contentClass: 'selectItemClass' }"
            variant="outlined"
            density="compact"
            hide-details
          />
        </div>
        <div style="margin: 10px 5px">
          <v-select
            v-model="dateFilter.current.div"
            :items="dateFilter.items.div"
            :menu-props="{ contentClass: 'selectItemClass' }"
            width="100px"
            variant="outlined"
            density="compact"
            hide-details
          />
        </div>
        <v-spacer />
        <div style="margin: 10px 5px; margin-right: 20px">
          <v-btn variant="tonal" color="#333333" size="small" text @click="requestCreationOneYear">1년 자료 생성</v-btn>
        </div>
        <TableComponent
          scroll-key="DateManagement"
          :headers="dateInfo.headers"
          :contents="dateFilter.contents"
          @grid-click-event="onClickGridRow"
          :custom-body-row-style="setCustomBodyRowStyle"
          :height-offset="263"
        />
      </v-row>
    </div>
    <div style="background-color: #f5f5f5">
      <v-card elevation="2" style="width: 500px; margin: 7px; margin-right: 3px">
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <nathan-Calendar
                ref="calendarRef"
                v-model="dateInfo.selected"
                :attributes="attributes"
                @update:pages="onMovePage"
                @dayclick="onClickDay"
                view="monthly"
                expanded
              />
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
      <v-card elevation="2" style="width: 500px; margin: 7px; margin-right: 3px">
        <v-card-text>
          <v-row>
            <div style="display: flex; align-items: stretch; margin: 7px">
              <v-label style="align-self: center; font-size: 16px !important; font-weight: bold" :text="`일자 정보 수정`" />
            </div>
            <InputFormGrid
              class="input-form-style"
              v-model="dateInfo.selected"
              :headers="dialogModifyHeaders"
              :row-height="'30px'"
              :table-header-width="'30%'"
              :covertNumberToString="true"
            />
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn variant="flat" color="blue darken-1" text @click="handleSave">수정</v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>
  <LoadingComponent v-if="onProcessing.search" />
  <LoadingComponent v-if="onProcessing.create" :message="`일자정보를 생성 중입니다.`" />
  <LoadingComponent v-if="onProcessing.save" :message="`데이터를 저장 중입니다.`" />
</template>

<script setup>
import { ref, onActivated, reactive } from 'vue';
import { useAuthStore } from '@/stores/index';
import { request, btnHandler, showMessage, getSystemSmallCode } from '@/utils/common';
import dayjs from 'dayjs';

const onProcessing = reactive({
  save: false,
  search: false,
  create: false,
});
const authStore = useAuthStore();

const calendarRef = ref(null);

const code_all_DATE_DIV = ref([]);
const code_DATE_DIV = ref([]);

onActivated(async () => {
  code_all_DATE_DIV.value = getSystemSmallCode('195', true);
  code_DATE_DIV.value = getSystemSmallCode('195');
  await handleSearch();
});

// 달력 속성 설정
const attributes = reactive([
  {
    key: 'selected',
    highlight: {
      color: 'blue',
      fillMode: 'light',
    },
    dates: new Date(),
  },
  {
    key: 'today',
    dot: true,
    content: {
      style: {
        fontStyle: 'italic',
      },
    },
    dates: new Date(),
  },
  {
    key: 'exemptionHoliday',
    highlight: {
      color: 'red',
      fillMode: 'light',
    },
    dates: [],
  },
  {
    key: 'holiday',
    content: {
      color: 'red',
      style: {
        fontStyle: 'italic',
      },
    },
    dates: [],
  },
]);

// 필터 설정
const dateFilter = reactive({
  items: {
    year: Array.from({ length: 100 }, (val, idx) => ({ title: `${2000 + idx}년`, value: `${2000 + idx}` })),
    month: [
      { title: '전체', value: '' },
      { title: '1월', value: '01' },
      { title: '2월', value: '02' },
      { title: '3월', value: '03' },
      { title: '4월', value: '04' },
      { title: '5월', value: '05' },
      { title: '6월', value: '06' },
      { title: '7월', value: '07' },
      { title: '8월', value: '08' },
      { title: '9월', value: '09' },
      { title: '10월', value: '10' },
      { title: '11월', value: '11' },
      { title: '12월', value: '12' },
    ],
    div: getSystemSmallCode('195', true),
  },
  current: {
    year: ref(dayjs().format('YYYY')),
    month: ref(dayjs().format('MM')),
    div: '',
  },
  contents: [],
});

const dateInfo = reactive({
  headers: [
    { title: '일자', key: 'DAY_DATE_DP', align: 'center', width: '120' },
    { title: '일자(음력)', key: 'LUNA_DATE_DP', align: 'center', width: '120' },
    { title: '일자구분', key: 'WORK_DATE_DIV_DP', align: 'center', width: '120' },
    { title: '비고', key: 'NOTE', align: 'center', width: '400', customBodyCellStyle: 'table-body-style-left' },
  ],
  contents: [],
  selected: {
    DAY_DATE_DP: '',
    LUNA_DATE_DP: '',
    WORK_DATE_DIV: '',
    NOTE: '',
  },
});

// filter 값 변경
const updateFilter = () => {
  dateFilter.contents = dateInfo.contents.filter((item) => item.DAY_DATE.substr(0, 4) == dateFilter.current.year);
  if (dateFilter.current.month !== '') {
    dateFilter.contents = dateFilter.contents.filter((item) => item.DAY_DATE.substr(4, 2) == dateFilter.current.month);
  }
  if (dateFilter.current.div !== '') {
    dateFilter.contents = dateFilter.contents.filter((item) => item.WORK_DATE_DIV == dateFilter.current.div);
  }
};

const updateFilterYear = async () => {
  movePage();
  await handleSearch();
};

// 달력 페이지 이동
const movePage = () => {
  if (dateFilter.current.month != '') {
    calendarRef.value.move({ year: dateFilter.current.year, month: dateFilter.current.month });
  } else {
    calendarRef.value.move({ year: dateFilter.current.year, month: 1 });
  }
};

// 달력 페이지 이동 이벤트 처리
const onMovePage = async (pages) => {
  let day = pages[0];
  if (dateFilter.current.year !== day.year.toString()) {
    dateFilter.current.year = day.year.toString();
    await handleSearch();
  }
  if (dateFilter.current.month != '') {
    dateFilter.current.month = day.month.toString().padStart(2, '0');
  }
  updateFilter();
};

// 그리드 Row 클릭 이벤트
const onClickGridRow = (item) => {
  let clickedDay = new Date(item.DAY_DATE_DP);
  attributes[attributes.findIndex((item) => item.key === 'selected')].dates = clickedDay;
  dateInfo.selected = { ...item };
  calendarRef.value.move(clickedDay);
};

// 그리드 Row 색상 설정
const setCustomBodyRowStyle = (item) => {
  if (item.DAY_DATE_DP === dateInfo.selected.DAY_DATE_DP) {
    return 'selected-row';
  } else if (item.WORK_DATE_DIV == 2) {
    return 'DayManagement-holiday';
  } else if (item.WORK_DATE_DIV == 4) {
    return 'DayManagement-holiday-exemption';
  }
};

const dialogModifyHeaders = ref([
  { title: '일자', key: 'DAY_DATE_DP', align: 'center', option: 'label' },
  { title: '음력일자', key: 'LUNA_DATE_DP', align: 'center', option: 'label' },
  { title: '일자구분', key: 'WORK_DATE_DIV', align: 'center', option: 'select', selectItem: code_DATE_DIV },
  { title: '비고', key: 'NOTE', align: 'center' },
]);

const onClickDay = (day, mouseEvent) => {
  attributes[attributes.findIndex((item) => item.key === 'selected')].dates = day.date;
  dateInfo.selected = { ...dateInfo.contents.find((item) => item.DAY_DATE_DP == day.id) };
  dateFilter.current.year = day.year.toString();
  dateFilter.current.month = day.month.toString().padStart(2, '0');
};

const handleSave = async () => {
  if (dateInfo.selected.DAY_DATE === undefined) {
    alert('일자를 선택하세요.');
    return;
  }
  onProcessing.save = true;
  try {
    const data = await request('post', 'api/base/setDate', {
      ...dateInfo.selected,
      IC_CODE: authStore.getIcCode,
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('정상 처리되었습니다.', 'success');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    showMessage('처리 중 오류가 발생했습니다.', 'error');
  } finally {
    onProcessing.save = false;
    handleSearch();
  }
};

const requestCreationOneYear = async () => {
  if (!confirm(`${dateFilter.current.year}년 일자정보가 생성/초기화 됩니다. 계속 진행하시겠습니까?`)) return;
  try {
    onProcessing.create = true;
    const data = await request('post', 'api/base/createDateList', {
      ...{
        strYear: dateFilter.current.year,
        IC_CODE: authStore.getIcCode,
      },
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('생성되었습니다.');
    } else {
      showMessage(data.ERROR_MSG);
    }
    handleSearch();
  } catch (error) {
    console.error('생성 중 오류 발생:', error);
  } finally {
    onProcessing.create = false;
  }
};

const handleSearch = async () => {
  onProcessing.search = true;
  try {
    const data = await request('post', 'api/base/getDateList', {
      SEARCH_YEAR: dateFilter.current.year,
      IC_CODE: authStore.getIcCode,
    });
    if (data.length == 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'warning');
    }
    dateInfo.contents = data;
    // 공휴일 설정
    const holidayContents = dateInfo.contents.filter((item) => item.WORK_DATE_DIV == 2);
    attributes[attributes.findIndex((item) => item.key === 'holiday')].dates = holidayContents.map((item) => {
      return new Date(item.DAY_DATE_DP);
    });
    // 특정일 면제 설정
    const exemptionHolidayContents = dateInfo.contents.filter((item) => item.WORK_DATE_DIV == 4);
    attributes[attributes.findIndex((item) => item.key === 'exemptionHoliday')].dates = exemptionHolidayContents.map((item) => {
      return new Date(item.DAY_DATE_DP);
    });
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
  } finally {
    onProcessing.search = false;
    updateFilter();
  }
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped>
.input-form-style:deep(th) {
  text-align: center;
}
.v-btn {
  font-weight: bold;
}
#filter-style .v-text-field,
#filter-style .v-date-field:deep(.v-text-field) {
  min-width: 50px;
  max-width: 240px;
}
#filter-style .v-text-field:deep(input::-webkit-calendar-picker-indicator) {
  margin: 0;
  cursor: pointer;
}
#filter-style .v-btn {
  height: 24px;
}
</style>
