<template>
  <LoadingComponent v-if="isLoading" />

  <SearchDataComponent :headers="searchHeader" :model-value="currentSearchData" @update:model-value="updateSearchData" @keyup.enter="handleSearch" />

  <v-tabs v-model="currentTab" class="tab-container" bg-color="#F5F5F5" :color="colorStore.basicColor" height="35">
    <v-tab v-for="tab in tabs" :key="tab.key" class="font-bold-ac" style="font-size: 13px">
      {{ tab.title }}
    </v-tab>
  </v-tabs>
  <v-window v-model="currentTab">
    <v-window-item v-for="(tab, index) in tabs" :key="index">
      <TableComponent
        v-if="tab.key === 'WorkStat'"
        :itemSize="20"
        :headers="getHeaders(tab.key)"
        :contents="currentSearchData.data"
        scrollKey="WorkStat"
        :heightOffset="327"
        rowType="1"
        :customBodyRowStyle="getCustomBodyRowStyle"
      />
      <v-row v-else-if="tab.key === 'WorkFin'">
        <v-col cols="7">
          <TableComponent
            :itemSize="20"
            :headers="getHeaders(tab.key)"
            :contents="currentSearchData.data"
            scrollKey="WorkFin"
            :heightOffset="327"
            rowType="1"
            :customBodyRowStyle="getCustomBodyRowStyle"
            @grid-click-event="gridClickEvent"
          />
        </v-col>
        <v-col cols="5" style="border: 1px solid #d0d0d0; border-bottom: none">
          <div style="padding: 10px">
            <GridSystemComponent :cols-per-row="3" :headers="detailHeaders" :contents="detailContents" tableThWidth="80px" />
          </div>
          <div v-if="showWorkFinForce">
            <!-- 강제근무종료 관련 UI -->
            <div style="margin-top: 40px; margin-left: 15px">
              <div style="display: flex">
                <h3><span style="color: #1686d5; margin-right: 4px">◎</span>강제근무종료처리</h3>
                <v-btn @click="onWorkFinForce" color="primary" style="font-size: 12px; font-weight: bold; margin-left: 10px" width="100" height="30"
                  >강제근무종료</v-btn
                >
              </div>
            </div>
            <!-- 안내 섹션 -->
            <div style="margin: 25px 0 0 10px">안내</div>
            <div style="border: 1px solid #b0b0b0; margin: 10px 10px 0 10px">
              <v-card variant="flat">
                <v-card-text>
                  <v-list>
                    <v-list-item>
                      <v-list-item-title>
                        <span>① 강제근무 종료처리는 차로에서 정상적으로 종료업무를 처리하지 못하였을 때 처리합니다.</span>
                      </v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title> <span>② 강제근무종료 대상</span> </v-list-item-title>
                      <v-list-item>
                        <v-list-item-title style="font-size: 15px">
                          1. 동일한 차로를 기준으로 다음 근무가 발생하였는데 근무중인 근무
                        </v-list-item-title>
                        <v-list-item-title style="font-size: 15px">
                          2. 일변경이 되어 다음날 근무가 발생하였으나 근무중으로 남아있는 근무
                        </v-list-item-title>
                      </v-list-item>
                    </v-list-item>
                  </v-list>
                </v-card-text>
              </v-card>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-window-item>
  </v-window>

  <div class="work-count-container">
    <span class="total-work">총 근무건수</span>
    <span><input class="total-count" :value="comma(currentSearchData.data.length)" disabled /> 건</span>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, toRaw } from 'vue';
import { useI18n } from 'vue-i18n';
import { comma } from '@/utils/common';
import { request, btnHandler, showMessage } from '@/utils/common';
import { useColorStore, useMenuStore } from '@/stores/index';
import { excelDownload } from '@/utils/excel';
import { isValidDate } from '@/utils/validate';
import dayjs from 'dayjs';

const { t } = useI18n();

const colorStore = useColorStore();

const currentTab = ref(0);

const tabs = [
  { title: '근무상태조회', key: 'WorkStat', apiEndpoint: '/api/work/getWorkStat', sheetName: '근무상태' },
  { title: '근무마감처리', key: 'WorkFin', apiEndpoint: '/api/work/getWorkFin', sheetName: '근무마감' },
];

const menuStore = useMenuStore();

const getHeaders = (tabKey) => {
  const sideToggle = menuStore.sideToggle;
  if (tabKey === 'WorkStat' && !sideToggle) {
    return [
      { title: '순번', key: 'ROW_NUMBER', width: 130, excelWidth: 10 },
      { title: '근무일자', key: 'WORK_DATE', width: 120, excelWidth: 20 },
      { title: '근무번호', key: 'WORK_NO', width: 120, excelWidth: 20 },
      { title: '차로명', key: 'NOTE', width: 176, excelWidth: 30 },
      { title: '게시근무자', key: 'WORKER_INFO', width: 180, excelWidth: 30 },
      { title: '근무상태', key: 'WORK_FIN_STAT', width: 130, excelWidth: 20 },
      { title: '근무개시시각', key: 'START_DT', width: 210, excelWidth: 20 },
      { title: '근무종료시각', key: 'END_DT', width: 230, excelWidth: 20 },
      { title: '근무구분', key: 'WORK_DIV', width: 130, excelWidth: 20 },
      { title: '운영구분', key: 'WORK_OPER_DIV', width: 130, excelWidth: 20 },
      { title: '종료구분', key: 'END_DIV', width: 130, excelWidth: 20 },
      { title: '종료근무자', key: 'END_WRKR_NO_NM', width: 200, excelWidth: 40 },
    ];
  } else if (tabKey === 'WorkStat' && sideToggle) {
    return [
      { title: '순번', key: 'ROW_NUMBER', width: 120, excelWidth: 10 },
      { title: '근무일자', key: 'WORK_DATE', width: 130, excelWidth: 20 },
      { title: '근무번호', key: 'WORK_NO', width: 110, excelWidth: 20 },
      { title: '차로명', key: 'NOTE', width: 176, excelWidth: 30 },
      { title: '게시근무자', key: 'WORKER_INFO', width: 130, excelWidth: 30 },
      { title: '근무상태', key: 'WORK_FIN_STAT', width: 130, excelWidth: 20 },
      { title: '근무개시시각', key: 'START_DT', width: 200, excelWidth: 20 },
      { title: '근무종료시각', key: 'END_DT', width: 200, excelWidth: 20 },
      { title: '근무구분', key: 'WORK_DIV', width: 110, excelWidth: 20 },
      { title: '운영구분', key: 'WORK_OPER_DIV', width: 110, excelWidth: 20 },
      { title: '종료구분', key: 'END_DIV', width: 100, excelWidth: 20 },
      { title: '종료근무자', key: 'END_WRKR_NO_NM', width: 155, excelWidth: 40 },
    ];
  } else if (tabKey === 'WorkFin' && !sideToggle) {
    return [
      { title: '순번', key: 'ROW_NUMBER', width: 110, excelWidth: 10 },
      { title: '근무일자', key: 'WORK_DATE', width: 123, excelWidth: 20 },
      { title: '근무번호', key: 'WORK_NO', width: 110, excelWidth: 20 },
      { title: '차로명', key: 'NOTE', width: 180, excelWidth: 30 },
      { title: '근무자', key: 'WORKER_INFO', width: 140, excelWidth: 30 },
      { title: '근무상태', key: 'WORK_FIN_STAT', width: 110, excelWidth: 20 },
      { title: '근무구분', key: 'WORK_DIV', width: 105, excelWidth: 20 },
      { title: '운영구분', key: 'WORK_OPER_DIV', width: 105, excelWidth: 20 },
      { title: '종료구분', key: 'END_DIV', width: 110, excelWidth: 20 },
    ];
  } else if (tabKey === 'WorkFin' && sideToggle) {
    return [
      { title: '순번', key: 'ROW_NUMBER', width: 94, excelWidth: 10 },
      { title: '근무일자', key: 'WORK_DATE', width: 118, excelWidth: 20 },
      { title: '근무번호', key: 'WORK_NO', width: 90, excelWidth: 20 },
      { title: '차로명', key: 'NOTE', width: 180, excelWidth: 30 },
      { title: '근무자', key: 'WORKER_INFO', width: 116, excelWidth: 30 },
      { title: '근무상태', key: 'WORK_FIN_STAT', width: 100, excelWidth: 20 },
      { title: '근무구분', key: 'WORK_DIV', width: 90, excelWidth: 20 },
      { title: '운영구분', key: 'WORK_OPER_DIV', width: 90, excelWidth: 20 },
      { title: '종료구분', key: 'END_DIV', width: 90, excelWidth: 20 },
    ];
  }
};

const searchHeader = ref([
  { label: '근무기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const originalSearchData = ref(null);
const searchData = ref(
  tabs.reduce((acc, tab) => {
    acc[tab.key] = {
      START_DATE: dayjs().format('YYYY-MM-DD'),
      END_DATE: dayjs().format('YYYY-MM-DD'),
      data: [],
    };
    return acc;
  }, {}),
);

const currentSearchData = computed({
  get: () => searchData.value[tabs[currentTab.value].key],
  set: (newValue) => {
    searchData.value[tabs[currentTab.value].key] = { ...searchData.value[tabs[currentTab.value].key], ...newValue };
  },
});

const currentApiEndpoint = computed(() => tabs[currentTab.value].apiEndpoint);

const updateSearchData = (newValue) => {
  currentSearchData.value = newValue;
  currentSearchData.value.data = [];
};

const getCustomBodyRowStyle = (item) => {
  if (isWorkFinForce(item, findNextWork(currentSearchData.value.data, item), dayjs().format('YYYY-MM-DD'))) {
    return 'WorkStat-custom-body-row-style-work-fin';
  }
  return '';
};

const isLoading = ref(false);

const handleSearch = async (forceSearch = false) => {
  if (!forceSearch && currentSearchData.value.data.length > 0) return;

  if (!isValidDate(searchData.value.START_DATE, searchData.value.END_DATE)) {
    alert(t('INVALID_DATE'));
    return;
  }

  try {
    isLoading.value = true;
    const response = await request('post', currentApiEndpoint.value, {
      IC_CODE: localStorage.getItem('loginIcCode'),
      START_DATE: dayjs(currentSearchData.value.START_DATE).format('YYYYMMDD'),
      END_DATE: dayjs(currentSearchData.value.END_DATE).format('YYYYMMDD'),
    });
    if (response.length === 0) {
      showMessage(t('NO_DATA'), 'error');
    } else {
      currentSearchData.value.data = response;
      if (tabs[currentTab.value].key === 'WorkFin') {
        selectFirstRow();
      }
    }

    currentSearchData.value.data = response;
    originalSearchData.value = JSON.parse(JSON.stringify(toRaw(searchData.value)));
  } catch (error) {
    alert('근무관리' + t('SEARCH_ERROR'));
  } finally {
    isLoading.value = false;
  }
};

const selectedRowIndices = ref(
  tabs.reduce((acc, tab) => {
    acc[tab.key] = -1;
    return acc;
  }, {}),
);

const isInitialTabLoad = ref(
  tabs.reduce((acc, tab) => {
    acc[tab.key] = true;
    return acc;
  }, {}),
);

watch(currentTab, async (newTab, oldTab) => {
  if (newTab !== oldTab) {
    await handleSearch(false);
    const currentTabKey = tabs[newTab].key;
    if (isInitialTabLoad.value[currentTabKey]) {
      selectFirstRow(currentTabKey);
      isInitialTabLoad.value[currentTabKey] = false;
    }
  }
});

const selectFirstRow = (tabKey) => {
  if (currentSearchData.value.data.length > 0) {
    const firstItem = currentSearchData.value.data[0];
    gridClickEvent(firstItem, 0, tabKey);
    selectedRowIndices.value[tabKey] = 0;
  }
};

onMounted(() => {
  handleSearch(true);
});

const getExcelHeaders = (tabKey) => {
  return getHeaders(tabKey).map((header) => ({
    title: header.title,
    key: header.key,
    excelWidth: header.excelWidth,
  }));
};

const handleExcel = async () => {
  const currentTabData = currentSearchData.value.data;
  if (currentTabData.length === 0) {
    alert(t('NO_DATA_EXCEL'));
    return;
  }
  if (JSON.stringify(toRaw(searchData.value)) !== JSON.stringify(originalSearchData.value)) {
    alert(t('SEARCH_CONDITION_CHANGED'));
    return;
  }

  if (confirm(`${tabs[currentTab.value].title} [${currentTabData.length}]` + t('CONFIRM_EXCEL'))) {
    try {
      const excelHeaders = getExcelHeaders(tabs[currentTab.value].key);
      const title = tabs[currentTab.value].title;
      const sheetName = tabs[currentTab.value].sheetName;
      excelDownload(1, searchHeader.value, currentSearchData.value, excelHeaders, currentTabData, sheetName, title, [
        `총 근무건수: ${currentTabData.length} 건`,
      ]);
    } catch (error) {
      alert(t('INCORRECT_PARAM_EXCEL'));
    }
  } else {
    showMessage(t('CANCEL_EXCEL'), 'error');
  }
};

const detailHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE' },
  { title: '개시시각', key: 'SDATE' },
  { title: '종료시각', key: 'EDATE' },
  { title: '근무번호', key: 'WORK_NO' },
  { title: '근무자', key: 'WORKER_INFO' },
  { title: '근무구분', key: 'WORK_DIV' },
  { title: '종료상태', key: 'END_DIV' },
]);

const detailContents = ref({});
const showWorkFinForce = ref(false);

const gridClickEvent = (selectItem, index, tabKey = tabs[currentTab.value].key) => {
  if (tabKey === 'WorkFin') {
    detailContents.value = {
      WORK_DATE: selectItem.WORK_DATE,
      SDATE: selectItem.SDATE,
      EDATE: selectItem.EDATE,
      WORK_NO: selectItem.WORK_NO,
      WORKER_INFO: selectItem.WORKER_INFO,
      WORK_DIV: selectItem.WORK_DIV,
      END_DIV: selectItem.END_DIV,
      LANE_DIV_NM: selectItem.LANE_DIV_NM,
    };

    const today = dayjs().format('YYYY-MM-DD');
    const nextWork = findNextWork(currentSearchData.value.data, selectItem);

    showWorkFinForce.value = isWorkFinForce(selectItem, nextWork, today);
  }
  selectedRowIndices.value[tabKey] = index;
};

const findNextWork = (data, currentItem) => {
  const workNoPrefix = currentItem.WORK_NO.substring(0, 2);
  return data.find(
    (item) =>
      item.WORK_DATE === currentItem.WORK_DATE && item.WORK_NO && item.WORK_NO.substring(0, 2) === workNoPrefix && item.WORK_NO > currentItem.WORK_NO,
  );
};

const isWorkFinForce = (item, nextWork, currentDate) => {
  return (item.WORK_FIN_STAT === '근무중' && nextWork) || (item.WORK_FIN_STAT === '근무중' && currentDate > item.WORK_DATE);
};

const onWorkFinForce = async () => {
  if (detailContents.value.LANE_DIV_NM === 'ETC') {
    await performWorkFinForce('ETC', detailContents.value);
  } else if (detailContents.value.LANE_DIV_NM === 'TCS') {
    await performWorkFinForce('TCS', detailContents.value);
  }
};

const performWorkFinForce = async (type, data) => {
  if (confirm(`${data.WORK_DATE} 근무번호 [${data.WORK_NO}]\n강제근무종료를 진행하시겠습니까?`)) {
    try {
      isLoading.value = true;
      let response;
      if (type === 'ETC') {
        response = await request('post', '/api/work/setWorkFinForce', {
          IC_CODE: localStorage.getItem('loginIcCode'),
          WORK_DATE: dayjs(data.WORK_DATE).format('YYYYMMDD'),
          WORK_NO: data.WORK_NO,
          END_STAT: '1', // 0:정상 1:강제
        });
        if (response[0].CODE === 'SUCCESS') {
          alert(`강제${response[0].MESSAGE}`);
        } else {
          alert(`${response[0].MESSAGE}`);
        }
      } else if (type === 'TCS') {
        response = await request('post', '/api/work/setWorkFinForceTCS', {
          IC_CODE: localStorage.getItem('loginIcCode'),
          WORK_DATE: dayjs(data.WORK_DATE).format('YYYYMMDD'),
          WORK_NO: data.WORK_NO,
        });
        if (response.SUCCESS_CODE === 1) {
          alert(`${response.SUCCESS_MSG}`);
        } else if (response.ERROR_CODE === 0 || response.ERROR_CODE === -1) {
          alert(`${response.ERROR_MSG}`);
          return;
        }
      }
    } catch (error) {
      alert(t('PROCESS_ERROR'));
    } finally {
      handleSearch(true);
      isLoading.value = false;
    }
  } else {
    showMessage(`${type} 강제근무종료를 취소했습니다.`, 'error');
  }
};

btnHandler({
  Search: () => handleSearch(true),
  Excel: handleExcel,
});
</script>

<style scoped>
.tab-container {
  margin: 10px 0 0 -10px;
}
.v-tab {
  color: #a9a9a9;
}

.work-count-container {
  border-top: 1px solid #d3d3d3;
  font-size: 12px;
}

.total-work {
  margin-left: 10px;
}

.total-count {
  width: 80px;
  text-align: center;
  margin: 10px 0px 0px 10px;
  border-radius: 3px;
  border: 1px solid #d3d3d3;
  font-weight: 400;
}
</style>
