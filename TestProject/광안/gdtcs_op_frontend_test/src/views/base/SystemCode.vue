<template>
  <v-row>
    <div style="width: 667px; border-right: solid 10px #f5f5f5">
      <v-card>
        <LoadingComponent v-if="onProcessing.lc.loading" />
        <div style="display: flex; justify-content: flex-end">
          <div id="filter-style">
            <v-row>
              <v-label text="대분류코드" />
              <v-text-field
                variant="outlined"
                density="compact"
                v-model="codesFilter.current.codelc"
                @update:model-value="updateCodesFilter"
                hide-details
                clearable
                width="50px"
              />
              <v-label text="코드명" />
              <v-text-field
                variant="outlined"
                density="compact"
                v-model="codesFilter.current.codeName"
                @update:model-value="updateCodesFilter"
                hide-details
                clearable
                width="100px"
              />
              <v-label text="설명" />
              <v-text-field
                variant="outlined"
                density="compact"
                v-model="codesFilter.current.codeDesc"
                @update:model-value="updateCodesFilter"
                hide-details
                clearable
                width="100px"
              />
              <v-spacer />
              <v-btn variant="tonal" :color="colorStore.basicColor" size="small" @click="onClickAddSystemLargeCode">추가</v-btn>
              <TableComponent
                scroll-key="gridkeySystemCodeLarge"
                :headers="headersSystemLargeCode"
                :contents="codesFilter.contents"
                @grid-click-event="onClickSystemLargeCode"
                @grid-dbl-click-event="onDoubleClickModifySystemLargeCode"
                :height-offset="248"
                row-type="1"
              />
            </v-row>
          </div>
        </div>
      </v-card>
    </div>
    <v-col>
      <div style="border-right: solid 10px #f5f5f5">
        <v-card>
          <LoadingComponent v-if="onProcessing.sc.loading" />
          <div style="display: flex; justify-content: flex-end">
            <div id="filter-style" style="width: 100%">
              <v-row>
                <v-spacer />
                <v-btn variant="tonal" :color="colorStore.basicColor" size="small" @click="onClickAddSystemSmallCode">추가</v-btn>
                <TableComponent
                  scroll-key="gridkeySystemCodeSmall"
                  :headers="headersSystemSmallCode"
                  :contents="systemSmallCodeList"
                  @grid-click-event="onClickSystemSmallCode"
                  @grid-dbl-click-event="onDoubleClickModifySystemSmallCode"
                  :height-offset="248"
                  row-type="1"
                />
              </v-row>
            </div>
          </div>
        </v-card>
      </div>
    </v-col>
  </v-row>
  <v-dialog v-model="dialogAddSystemLargeCode" max-width="360px" persistent z-index="1100">
    <v-card>
      <div v-if="onProcessing.lc.save" class="loading-overlay">
        <v-progress-circular color="grey-lighten-4" indeterminate />
      </div>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title style="margin: 0px">대분류 코드 추가</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <h3>코드 정보</h3>
            <InputFormGrid v-model="systemLargeCode" :headers="dialogAddSystemLargeCodeHeaders" :cols-per-row="1" :table-header-width="'30%'" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="requestAddSystemLargeCode">추가</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogAddSystemLargeCode = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="dialogModifySystemLargeCode" max-width="360px" persistent z-index="1100">
    <v-card>
      <div v-if="onProcessing.lc.save" class="loading-overlay">
        <v-progress-circular color="grey-lighten-4" indeterminate />
      </div>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title style="margin: 0px">대분류 코드 수정</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <h3>코드 정보</h3>
            <InputFormGrid v-model="systemLargeCode" :headers="dialogModifySystemLargeCodeHeaders" :cols-per-row="1" :table-header-width="'30%'" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn variant="tonal" color="red-lighten-2" text @click="requestDeleteSystemLargeCode">삭제</v-btn>
        <v-spacer />
        <v-btn variant="flat" color="blue darken-1" text @click="requestModifySystemLargeCode">수정</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogModifySystemLargeCode = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="dialogAddSystemSmallCode" max-width="360px" persistent z-index="1100">
    <v-card>
      <div v-if="onProcessing.sc.save" class="loading-overlay">
        <v-progress-circular color="grey-lighten-4" indeterminate />
      </div>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>소분류 코드 추가</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <h3>코드 정보</h3>
            <InputFormGrid v-model="systemSmallCode" :headers="dialogAddSystemSmallCodeHeaders" :cols-per-row="1" :table-header-width="'30%'" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="requestAddSystemSmallCode">추가</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogAddSystemSmallCode = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="dialogModifySystemSmallCode" max-width="360px" persistent z-index="1100">
    <v-card>
      <div v-if="onProcessing.sc.save" class="loading-overlay">
        <v-progress-circular color="grey-lighten-4" indeterminate />
      </div>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>소분류 코드 수정</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <h3>코드 정보</h3>
            <InputFormGrid v-model="systemSmallCode" :headers="dialogModifySystemSmallCodeHeaders" :cols-per-row="1" :table-header-width="'30%'" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn variant="tonal" color="red-lighten-2" text @click="requestDeleteSystemSmallCode">삭제</v-btn>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="requestModifySystemSmallCode">수정</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogModifySystemSmallCode = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, reactive } from 'vue';
import { request, btnHandler, getSystemSmallCode, showMessage } from '@/utils/common';
import { useColorStore } from '@/stores/index';

const colorStore = useColorStore();

const onProcessing = reactive({
  lc: {
    save: false,
    loading: false,
  },
  sc: {
    save: false,
    loading: false,
  },
});

const code_USE_YN = ref([]);
const code_USE_YN_ALL = ref([]);

onActivated(async () => {
  code_USE_YN.value = getSystemSmallCode('047');
  code_USE_YN_ALL.value = getSystemSmallCode('047', true);
  searchData.value.CODELC_USE_YN = '';
  handleSearch();
});

const codesFilter = reactive({
  current: {
    codelc: '',
    codeName: '',
    codeDesc: '',
  },
  contents: [],
});

const updateCodesFilter = () => {
  codesFilter.contents = systemLargeCodeList.value.filter((item) => item.CODELC.includes(codesFilter.current.codelc));
  codesFilter.contents = codesFilter.contents.filter((item) => item.CODELC_NM.includes(codesFilter.current.codeName));
  codesFilter.contents = codesFilter.contents.filter((item) => item.CODELC_NOTE.includes(codesFilter.current.codeDesc));
};

const headersSystemLargeCode = ref([
  { title: '대분류코드', key: 'CODELC', align: 'center', width: '90' },
  { title: '코드명', key: 'CODELC_NM', align: 'center', width: '180', customBodyCellStyle: 'table-body-style-left' },
  { title: '설명', key: 'CODELC_NOTE', align: 'center', width: '280', customBodyCellStyle: 'table-body-style-left' },
  { title: '사용여부', key: 'CODELC_USE_YN_NM', align: 'center', width: '90' },
]);

const headersSystemSmallCode = ref([
  { title: '대분류코드', key: 'CODELC', align: 'center', width: '90' },
  { title: '소분류코드', key: 'CODESC', align: 'center', width: '90' },
  { title: '코드명', key: 'CODESC_NM', align: 'center', width: '180', customBodyCellStyle: 'table-body-style-left' },
  { title: '설명', key: 'CODESC_NOTE', align: 'center', width: '280', customBodyCellStyle: 'table-body-style-left' },
  { title: '약어', key: 'CODESC_NIC', align: 'center', width: '160' },
  { title: '정렬순서', key: 'ARY_ORDR', align: 'center', width: '90' },
  { title: '사용여부', key: 'CODESC_USE_YN_NM', align: 'center', width: '90' },
]);

const systemLargeCodeList = ref([]);
const systemSmallCodeList = ref([]);

const handleSearch = async () => {
  // systemLargeCode.value = {};
  systemSmallCode.value = {};
  systemSmallCodeList.value = [];
  try {
    onProcessing.lc.loading = true;
    const data = await request('post', 'api/base/getSystemCodeList', {});
    systemLargeCodeList.value = data;
    updateCodesFilter();
  } catch (error) {
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    onProcessing.lc.loading = false;
  }
};

const onClickSystemLargeCode = (item) => {
  systemLargeCode.value = { ...item };
  handleSearchSystemSmallCode();
};

const onClickSystemSmallCode = async (item) => {
  systemSmallCode.value = { ...item };
};

const handleSearchSystemSmallCode = async () => {
  try {
    onProcessing.sc.loading = true;
    const data = await request('post', 'api/base/getSystemSmallCode', {
      CODELC: systemLargeCode.value.CODELC,
    });
    systemSmallCodeList.value = data;
  } catch (error) {
    showMessage('소분류 코드 조회 중 오류가 발생했습니다.', 'error');
  } finally {
    onProcessing.sc.loading = false;
  }
};

btnHandler({
  Search: handleSearch,
});

const dialogAddSystemLargeCode = ref(false);
const dialogModifySystemLargeCode = ref(false);
const dialogAddSystemSmallCode = ref(false);
const dialogModifySystemSmallCode = ref(false);

const dialogAddSystemLargeCodeHeaders = ref([
  { title: '대분류코드', key: 'CODELC' },
  { title: '코드명', key: 'CODELC_NM' },
  { title: '비고', key: 'CODELC_NOTE' },
  { title: '사용여부', key: 'CODELC_USE_YN', option: 'select', selectItem: code_USE_YN },
]);

const dialogModifySystemLargeCodeHeaders = ref([
  { title: '대분류코드', key: 'CODELC', disabled: true },
  { title: '코드명', key: 'CODELC_NM' },
  { title: '비고', key: 'CODELC_NOTE' },
  { title: '사용여부', key: 'CODELC_USE_YN', option: 'select', selectItem: code_USE_YN },
]);

const dialogAddSystemSmallCodeHeaders = ref([
  { title: '대분류코드', key: 'CODELC', disabled: true },
  { title: '소분류코드', key: 'CODESC' },
  { title: '코드명', key: 'CODESC_NM' },
  { title: '약어', key: 'CODESC_NIC' },
  { title: '정렬순서', key: 'ARY_ORDR', option: 'numberInput', step: 1, min: 0, max: 999 },
  { title: '비고', key: 'CODESC_NOTE' },
  { title: '사용여부', key: 'CODESC_USE_YN', option: 'select', selectItem: code_USE_YN },
]);

const dialogModifySystemSmallCodeHeaders = ref([
  { title: '대분류코드', key: 'CODELC', disabled: true },
  { title: '소분류코드', key: 'CODESC', disabled: true },
  { title: '코드명', key: 'CODESC_NM' },
  { title: '약어', key: 'CODESC_NIC' },
  { title: '정렬순서', key: 'ARY_ORDR', option: 'numberInput', step: 1, min: 0, max: 999 },
  { title: '비고', key: 'CODESC_NOTE' },
  { title: '사용여부', key: 'CODESC_USE_YN', option: 'select', selectItem: code_USE_YN },
]);

const searchData = ref([]);
const systemLargeCode = ref([]);
const systemSmallCode = ref([]);

const onClickAddSystemLargeCode = () => {
  systemLargeCode.value = {};
  systemLargeCode.value.CODELC_USE_YN = 'Y';
  dialogAddSystemLargeCode.value = true;
};

const requestAddSystemLargeCode = async () => {
  if (systemLargeCode.value.CODELC == null) {
    alert('대분류 코드를 입력하세요.');
    return;
  }
  if (systemLargeCode.value.CODELC_NM == null) {
    alert('코드명을 입력하세요.');
    return;
  }
  try {
    onProcessing.lc.save = true;
    const data = await request('post', 'api/base/addSystemLargeCode', {
      CODELC: systemLargeCode.value.CODELC,
      CODELC_NM: systemLargeCode.value.CODELC_NM,
      CODELC_NOTE: systemLargeCode.value.CODELC_NOTE,
      CODELC_USE_YN: systemLargeCode.value.CODELC_USE_YN,
    });
    if (data.ERROR_CODE >= 1) {
      handleSearch();
      showMessage('대분류 코드가 추가되었습니다.', 'success');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    showMessage(`대분류 코드 추가 중 오류가 발생했습니다.`, 'error');
    console.error('대분류 코드 추가 중 오류 발생:', error);
  } finally {
    dialogAddSystemLargeCode.value = false;
    onProcessing.lc.save = false;
  }
};

const onDoubleClickModifySystemLargeCode = async (index, item) => {
  dialogModifySystemLargeCode.value = true;
  systemLargeCode.value = { ...item };
};
const onDoubleClickModifySystemSmallCode = async (index, item) => {
  dialogModifySystemSmallCode.value = true;
  systemSmallCode.value = { ...item };
};

const requestModifySystemLargeCode = async () => {
  try {
    const data = await request('post', 'api/base/setSystemLargeCode', {
      CODELC: systemLargeCode.value.CODELC,
      CODELC_NM: systemLargeCode.value.CODELC_NM,
      CODELC_NOTE: systemLargeCode.value.CODELC_NOTE,
      CODELC_USE_YN: systemLargeCode.value.CODELC_USE_YN,
    });

    if (data.ERROR_CODE >= 1) {
      alert('대분류 코드가 수정되었습니다.');
      dialogModifySystemLargeCode.value = false;
    } else {
      alert(data.ERROR_MSG);
    }

    handleSearch();
  } catch (error) {
    console.error('대분류 코드가 수정 중 오류 발생:', error);
  }
  dialogModifySystemLargeCode.value = false;
};

const requestDeleteSystemLargeCode = async () => {
  onProcessing.lc.save = true;
  try {
    const data = await request('post', 'api/base/delSystemLargeCode', {
      CODELC: systemLargeCode.value.CODELC,
    });
    if (data.ERROR_CODE >= 1) {
      handleSearch();
      showMessage('대분류 코드가 삭제되었습니다.', 'success');
      dialogModifySystemLargeCode.value = false;
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    showMessage(`대분류코드 삭제 중 오류가 발생했습니다.`, 'error');
    console.error('대분류 코드 삭제 중 오류 발생:', error);
  } finally {
    onProcessing.lc.save = false;
  }
};

const onClickAddSystemSmallCode = async () => {
  if (systemLargeCode.value.CODELC == null) {
    alert('대분류 코드를 선택하세요.');
    return;
  }
  systemSmallCode.value = {};
  systemSmallCode.value.CODELC = systemLargeCode.value.CODELC;
  systemSmallCode.value.ARY_ORDR = 1;
  systemSmallCode.value.CODESC_USE_YN = 'Y';
  dialogAddSystemSmallCode.value = true;
};

const requestAddSystemSmallCode = async () => {
  if (systemSmallCode.value.CODESC == null) {
    showMessage('소분류 코드를 입력하세요.', 'warning');
    return;
  }
  if (systemSmallCode.value.CODESC_NM == null) {
    showMessage('코드명을 입력하세요.', 'warning');
    return;
  }
  onProcessing.sc.save = true;
  try {
    const data = await request('post', 'api/base/addSystemSmallCode', {
      CODELC: systemSmallCode.value.CODELC,
      CODESC: systemSmallCode.value.CODESC,
      BGSM_CODE: '',
      CODESC_NM: systemSmallCode.value.CODESC_NM,
      CODESC_NOTE: systemSmallCode.value.CODESC_NOTE,
      CODESC_NIC: systemSmallCode.value.CODESC_NIC,
      ARY_ORDR: systemSmallCode.value.ARY_ORDR,
      CODESC_USE_YN: systemSmallCode.value.CODESC_USE_YN,
    });

    if (data.ERROR_CODE >= 1) {
      handleSearchSystemSmallCode();
      onClickSystemLargeCode(systemLargeCode.value);
      showMessage('소분류 코드가 추가되었습니다.', 'success');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    console.error('소분류 코드 추가 중 오류 발생:', error);
  } finally {
    dialogAddSystemSmallCode.value = false;
    onProcessing.sc.save = false;
  }
};

const requestModifySystemSmallCode = async () => {
  onProcessing.sc.save = true;
  try {
    const data = await request('post', 'api/base/setSystemSmallCode', {
      CODELC: systemSmallCode.value.CODELC,
      CODESC: systemSmallCode.value.CODESC,
      BGSM_CODE: '',
      CODESC_NM: systemSmallCode.value.CODESC_NM,
      CODESC_NOTE: systemSmallCode.value.CODESC_NOTE,
      CODESC_NIC: systemSmallCode.value.CODESC_NIC,
      ARY_ORDR: systemSmallCode.value.ARY_ORDR,
      CODESC_USE_YN: systemSmallCode.value.CODESC_USE_YN,
    });

    if (data.ERROR_CODE >= 1) {
      alert('소분류 코드가 수정되었습니다.');
    } else {
      alert(data.ERROR_MSG);
    }
    await handleSearchSystemSmallCode();
  } catch (error) {
    console.error('소분류 코드가 수정 중 오류 발생:', error);
  } finally {
    dialogModifySystemSmallCode.value = false;
    onProcessing.sc.save = false;
  }
};

const requestDeleteSystemSmallCode = async () => {
  if (systemSmallCode.value.CODESC == null) {
    alert('소분류 코드를 선택하세요.');
    return;
  }
  onProcessing.sc.save = true;
  try {
    const data = await request('post', 'api/base/delSystemSmallCode', {
      CODELC: systemSmallCode.value.CODELC,
      CODESC: systemSmallCode.value.CODESC,
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('소분류 코드가 삭제되었습니다.', 'success');
    } else {
      alert(data.ERROR_MSG);
    }
    handleSearchSystemSmallCode();
  } catch (error) {
    console.error('소분류 코드 삭제 중 오류 발생:', error);
  } finally {
    onProcessing.sc.save = false;
    dialogModifySystemSmallCode.value = false;
  }
};
</script>
<style>
#filter-style .v-text-field,
#filter-style .v-text-field:not(.mb-1) .v-field__input {
  height: 24px;
  font-size: 12px;
  padding: 3px;
}
#filter-style .v-text-field:deep(input::-webkit-calendar-picker-indicator) {
  cursor: pointer;
}
#filter-style .v-label {
  margin: auto 7px auto 10px;
}
#filter-style .v-btn {
  height: 24px;
  font-weight: bold;
  margin: 3px 0px;
  margin-right: 7px;
}
.loading-overlay {
  position: absolute;
  top: 64px;
  left: 0;
  width: 100%;
  height: calc(100% - 64px);
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
</style>
