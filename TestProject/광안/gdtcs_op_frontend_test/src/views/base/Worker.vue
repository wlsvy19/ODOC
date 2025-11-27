<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  </div>
  <div style="display: flex; justify-content: flex-end">
    <v-col>
      <v-row style="background-color: #f5f5f5">
        <v-card elevation="2" style="width: 33%; margin: 7px 7px 7px auto">
          <div style="margin: 7px">
            <v-row>
              <div style="display: flex; width: 36px; align-items: stretch; margin: 7px">
                <v-label text="범례" />
              </div>
              <v-col>
                <table class="revision_table">
                  <tr>
                    <th>권한</th>
                    <td>(최)상위관리자</td>
                    <td>유지보수</td>
                    <td>근무자</td>
                  </tr>
                  <tr>
                    <th>번호대역</th>
                    <td>1000 ~ 1998</td>
                    <td>2000 ~ 2999</td>
                    <td>3000 ~ 4999</td>
                  </tr>
                </table>
              </v-col>
            </v-row>
          </div>
        </v-card>
        <v-card elevation="2" style="width: 63%; margin: 7px auto 7px 7px">
          <div style="margin: 7px">
            <v-row>
              <div style="display: flex; width: 60px; align-items: stretch; margin: 7px">
                <v-label style="align-self: center" :text="`근무자 개정정보`" />
              </div>
              <v-col>
                <table class="revision_table">
                  <tr>
                    <th>적용일시</th>
                    <td><input type="datetime-local" v-model="revisionRequestContent.WORKER_APPLY_DT_FORMAT" /></td>
                    <th>수정일시</th>
                    <td><input type="datetime-local" v-model="revisionRequestContent.WORKER_MDFY_DT_FORMAT" :readonly="true" /></td>
                    <td rowspan="2" style="border: none">
                      <v-btn style="margin: auto" variant="flat" color="blue" text @click="requestRevision">개정지시</v-btn>
                    </td>
                  </tr>
                  <tr>
                    <th>개정번호</th>
                    <td>
                      <v-number-input
                        variant="outlined"
                        hide-details
                        density="compact"
                        v-model="revisionRequestContent.WORKER_REV_NO"
                        min="1"
                        max="100"
                        :rules="InputRules.requiredRule"
                        :readonly="revNoInputDisabled"
                        @update:model-value="revisionRequestContent.WORKER_REV_NO = onUpdateRevNo(revisionRequestContent.WORKER_REV_NO)"
                      />
                    </td>
                    <td colspan="2">
                      <v-row>
                        <v-spacer />
                        <v-btn
                          variant="tonal"
                          :color="colorStore.basicColor"
                          size="small"
                          @click="
                            revisionRequestContent.WORKER_REV_NO = onUpdateRevNo(revisionRequestContent.WORKER_REV_NO + 1);
                            revNoInputDisabled = true;
                          "
                          text="자동입력"
                        />
                        <v-btn variant="tonal" size="small" color="#909090" @click="revNoInputDisabled = false">수동입력</v-btn>
                      </v-row>
                    </td>
                  </tr>
                </table>
              </v-col>
            </v-row>
          </div>
        </v-card>
      </v-row>
      <v-row>
        <TableComponent
          :width="'80%'"
          scroll-key="gridKeyWorker"
          :headers="mainHeaders"
          :contents="mainContents"
          @grid-click-event="onClickContent"
          :height-offset="350"
          row-type="1"
        />
      </v-row>
    </v-col>
    <div style="background-color: #f5f5f5">
      <v-card elevation="2" style="width: 400px; margin: 7px; margin-right: 3px" :disabled="selectedContent.value">
        <v-card-text>
          <v-row>
            <div style="display: flex; width: 60px; align-items: stretch; margin: 7px">
              <v-label style="align-self: center" :text="`정보 수정`" />
            </div>
          </v-row>
          <v-row>
            <v-col cols="12" style="text-align: center">
              <div :style="`min-height: 130px; margin: 10px auto;`">
                <v-img v-if="imageProfileUrl" :src="imageProfileUrl" height="130px">
                  <template v-slot:placeholder>
                    <div class="d-flex align-center justify-center fill-height">
                      <v-progress-circular color="grey-lighten-4" indeterminate />
                    </div>
                  </template>
                </v-img>
              </div>
              <div style="margin-bottom: 17px">
                <v-file-input
                  v-model="file_IMG_PROFILE_IMAGE"
                  label="프로필 이미지 업로드"
                  accept="image/png, image/jpeg, image/bmp"
                  prepend-inner-icon="mdi-camera"
                  prepend-icon=""
                  variant="outlined"
                  hide-details
                />
              </div>
              <div style="margin-bottom: 10px">
                <InputFormGrid
                  class="input-form-style"
                  row-height="30px"
                  v-model="selectedContent"
                  :headers="dialogInfo.base.headers"
                  :cols-per-row="2"
                  :table-header-width="'25%'"
                  v-model:is-valid="dialogInfo.base.isValid"
                  v-model:input-form="dialogInfo.base.inputForm"
                />
              </div>
              <div style="margin-bottom: 10px">
                <InputFormGrid
                  class="input-form-style"
                  row-height="30px"
                  v-model="selectedContent"
                  :headers="dialogInfo.detail.headers"
                  :cols-per-row="1"
                  :table-header-width="'25%'"
                  v-model:is-valid="dialogInfo.detail.isValid"
                  v-model:input-form="dialogInfo.detail.inputForm"
                />
              </div>
              <InputFormGrid
                class="input-form-style"
                v-model="selectedContent"
                :headers="dialogInfo.other.headers"
                :cols-per-row="1"
                :table-header-width="'25%'"
                v-model:is-valid="dialogInfo.other.isValid"
                v-model:input-form="dialogInfo.other.inputForm"
              />
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn v-if="selectedContent.RESIGN_YN == 'Y'" variant="tonal" color="teal-lighten-1" text @click="requestResignNo">재직처리</v-btn>
          <v-btn v-if="selectedContent.RESIGN_YN == 'N'" variant="tonal" color="teal-lighten-1" text @click="requestResignYes">퇴사처리</v-btn>
          <v-btn variant="tonal" color="red-lighten-2" text @click="handleDelete">삭제</v-btn>
          <v-btn variant="tonal" :color="colorStore.basicColor" text @click="onClickChangePassword">비밀번호 변경</v-btn>
          <v-btn variant="flat" :color="colorStore.basicColor" text @click="handleSave">수정</v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>
  <v-dialog v-model="dialogAddContent" max-width="450px" persistent z-index="1100">
    <v-card>
      <v-toolbar class="dialog-toolbar">
        <v-toolbar-title>근무자 정보 추가</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <div style="margin-bottom: 10px">
          <InputFormGrid
            class="input-form-style"
            row-height="30px"
            v-model="newContent"
            :headers="dialogInfo.newBase.headers"
            :cols-per-row="2"
            :table-header-width="'25%'"
            v-model:is-valid="dialogInfo.newBase.isValid"
            v-model:input-form="dialogInfo.newBase.inputForm"
          />
        </div>
        <div style="margin-bottom: 10px">
          <InputFormGrid
            class="input-form-style"
            row-height="30px"
            v-model="newContent"
            :headers="dialogInfo.detail.headers"
            :cols-per-row="1"
            :table-header-width="'25%'"
            v-model:is-valid="dialogInfo.detail.isValidNew"
            v-model:input-form="dialogInfo.detail.inputFormNew"
          />
        </div>
        <InputFormGrid
          class="input-form-style"
          v-model="newContent"
          :headers="dialogInfo.other.headers"
          :cols-per-row="1"
          :table-header-width="'25%'"
          v-model:is-valid="dialogInfo.other.isValidNew"
          v-model:input-form="dialogInfo.other.inputFormNew"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="flat" color="blue darken-1" text @click="requestAddContent">추가</v-btn>
        <v-btn variant="flat" color="black darken-1" text @click="dialogAddContent = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <DialogModifyPassword v-model:is-active="isShowDialogModifyPassword" v-model:user-info="userInfo" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, onActivated, reactive } from 'vue';
import { useAuthStore, useColorStore } from '@/stores/index';
import {
  request,
  btnHandler,
  yyyyMMddHHmmssToDatetimeLocal,
  datetimeLocalToyyyyMMddHHmmss,
  getCurrentDatetime,
  dateFormatToyyyyMMdd,
  getSystemSmallCode,
  showMessage,
} from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';
import Cookies from 'js-cookie';
import { InputRules } from '@/utils/rules';

const mainContents = ref([]);
const selectedContent = ref({ key: 'FILE_PTH', value: '' });
const revisionRequestContent = ref([]);

const isLoading = ref(false);
const imgKey = ref(0);

const dialogAddContent = ref(false);
const isShowDialogModifyPassword = ref(false);

const authStore = useAuthStore();
const colorStore = useColorStore();

const revNoInputDisabled = ref(true);

const code_all_CLASS = ref([]);
const code_CLASS = ref([]);
const code_RESIGN_YN = ref([]);
const search_code_RESIGN_YN = ref([]);

const file_IMG_PROFILE_IMAGE = ref([]);
const imageProfileUrl = ref('');

onActivated(async () => {
  code_all_CLASS.value = getSystemSmallCode('230');
  code_RESIGN_YN.value = getSystemSmallCode('231');
  search_code_RESIGN_YN.value = getSystemSmallCode('231', true);
  await nextTick();
  await handleSearch();
});

const searchData = ref({
  WORKER_NO: '',
  WORKER_NM: '',
  RESIGN_YN: 'N',
});

const searchHeader = ref([
  { label: '재직여부', key: 'RESIGN_YN', type: 'select', option: search_code_RESIGN_YN },
  { label: '근무자번호', key: 'WORKER_NO', type: 'input' },
  { label: '근무자명', key: 'WORKER_NM', type: 'input' },
]);

const mainHeaders = ref([
  { title: '순번', key: 'ROWNUM', align: 'center', width: '70' },
  { title: '근무자번호', key: 'WORKER_NO', align: 'center', width: '80' },
  { title: '근무자명', key: 'WORKER_NM', align: 'center', width: '80' },
  { title: '입사일자', key: 'JOIN_DATE', align: 'center', width: '90' },
  { title: '생년월일', key: 'DOB', align: 'center', width: '90' },
  { title: '직급', key: 'RNK', align: 'center', width: '70' },
  { title: '등급', key: 'CLASS_NM', align: 'center', width: '100' },
  { title: '재직여부', key: 'RESIGN_YN_FORMAT', align: 'center', width: '80' },
  { title: '전화번호', key: 'TEL', align: 'center', width: '110' },
  { title: '휴대전화', key: 'CP_NO', align: 'center', width: '110' },
  { title: '차량번호', key: 'CAR_NO', align: 'center', width: '100' },
  { title: '주소', key: 'ADDR', align: 'center', width: '200' },
]);

const setClassItems = (value) => {
  let val = Number(value);
  if (value === '' || value === null) {
    code_CLASS.value = code_all_CLASS.value;
  } else if (1000 <= val && val <= 1998) {
    code_CLASS.value = code_all_CLASS.value.filter((item) => item.value === '1' || item.value === '2');
  } else if (2000 <= val && val <= 2999) {
    code_CLASS.value = code_all_CLASS.value.filter((item) => item.value === '7');
  } else if (3000 <= val && val <= 4999) {
    code_CLASS.value = code_all_CLASS.value.filter((item) => item.value === '3' || item.value === '4');
  } else if (val === 1999) {
    code_CLASS.value = code_all_CLASS.value.filter((item) => item.value === '1');
  } else {
    code_CLASS.value = code_all_CLASS.value.filter((item) => item.value === '10');
  }
};

const onChangedNewBaseWorkerNo = (value) => {
  setClassItems(value);
  newContent.value.CLASS = code_CLASS.value[0].value;
};

const dialogInfo = reactive({
  base: {
    headers: [
      { title: '근무자번호', key: 'WORKER_NO', option: 'label' },
      { title: '근무자명', key: 'WORKER_NM', rules: InputRules.workerNameRule },
      { title: '퇴사여부', key: 'RESIGN_YN', option: 'label', selectItem: code_RESIGN_YN },
      { title: '사번', key: 'CO_NO' },
    ],
    inputForm: null,
    isValid: true,
  },
  newBase: {
    headers: [
      { title: '근무자번호', key: 'WORKER_NO', required: 'Y', rules: InputRules.workerNoRule, changed: onChangedNewBaseWorkerNo },
      { title: '근무자명', key: 'WORKER_NM', rules: InputRules.workerNameRule },
      { title: '퇴사여부', key: 'RESIGN_YN', required: 'Y', option: 'select', selectItem: code_RESIGN_YN },
      { title: '사번', key: 'CO_NO' },
    ],
    inputForm: null,
    isValid: true,
  },
  detail: {
    headers: [
      { title: '직급', key: 'RNK', rules: InputRules.workerRnkRule },
      { title: '등급', key: 'CLASS', required: 'Y', option: 'select', selectItem: code_CLASS },
      { title: '차량번호', key: 'CAR_NO', rules: InputRules.carNoRule },
    ],
    inputForm: null,
    isValid: true,
    inputFormNew: null,
    isValidNew: true,
  },
  other: {
    headers: [
      { title: '입사일자', key: 'JOIN_DATE_FORMAT', option: 'date', dateType: 'day' },
      { title: '생년월일', key: 'DOB_FORMAT', option: 'date', dateType: 'day' },
      { title: '전화번호', key: 'TEL', rules: InputRules.workerTelNoRule },
      { title: '휴대전화', key: 'CP_NO', rules: InputRules.workerTelNoRule },
      { title: '주소', key: 'ADDR', rules: InputRules.addrRule },
    ],
    inputForm: null,
    isValid: true,
    inputFormNew: null,
    isValidNew: true,
  },
});

const newContent = ref({});

const clearNewContent = () => {
  newContent.value = {
    WORKER_NO: '',
    WORKER_NM: '',
    RESIGN_YN: 'N',
    CO_NO: '',
    RNK: '',
    CLASS: code_CLASS.value[code_CLASS.value.length - 1],
    CAR_NO: '',
    // MCARD_FORMAT: '',
    JOIN_DATE_FORMAT: '',
    DOB_FORMAT: '',
    TEL: '',
    CP_NO: '',
    ADDR: '',
  };
  setClassItems('');
};

const userInfo = ref({});

const clearDetailWorkerInfo = () => {
  selectedContent.value = { key: 'FILE_PTH', value: '' };
  imageProfileUrl.value = '';
  file_IMG_PROFILE_IMAGE.value = [];
};

const handleSearch = async () => {
  clearDetailWorkerInfo();
  try {
    isLoading.value = true;
    const result = await request('post', 'api/base/getWorkerList', {
      ...searchData.value,
      ...{ IC_CODE: authStore.getIcCode },
    });
    if (result.length == 0) {
      showMessage('근무자 정보가 없습니다.', 'warning');
    }
    mainContents.value = result;
    nextTick(() => (isLoading.value = false));
    try {
      const data = await request('post', 'api/base/getIcRevision', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      revisionRequestContent.value = data[0];
      revisionRequestContent.value.WORKER_APPLY_DT_FORMAT = yyyyMMddHHmmssToDatetimeLocal(revisionRequestContent.value.WORKER_APPLY_DT);
      revisionRequestContent.value.WORKER_MDFY_DT_FORMAT = yyyyMMddHHmmssToDatetimeLocal(revisionRequestContent.value.WORKER_MDFY_DT);
      if (data.length == 0) {
        showMessage('개정 정보가 없습니다.', 'warning');
      }
    } catch (error) {
      showMessage('개정 조회에 실패했습니다.', 'error');
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } catch {
    showMessage('근무자정보 조회가 실패했습니다.', 'warning');
  }
};

const onClickContent = async (event) => {
  selectedContent.value = { ...event };
  setClassItems(event.WORKER_NO);
  if (selectedContent.value.FILE_PTH == null) {
    imageProfileUrl.value = `/api/base/getWorkerProfileImage?IC_CODE=${authStore.getIcCode}&FILE_PTH=`;
    imgKey.value++;
  } else {
    imageProfileUrl.value = `/api/base/getWorkerProfileImage?IC_CODE=${authStore.getIcCode}&FILE_PTH=${selectedContent.value.FILE_PTH}`;
    imgKey.value++;
  }
};

const requestRevision = async () => {
  if (revisionRequestContent.value.BASE_REV_NO == undefined) {
    alert('개정 정보 조회 후 개정 지시이 가능합니다.');
    return;
  }

  if (!confirm('개정 번호를 확인하였습니까?')) {
    return;
  }

  if (!confirm('개정을 지시하겠습니까?')) {
    return;
  }

  try {
    const send_dt = getCurrentDatetime();

    revisionRequestContent.value.WORKER_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionRequestContent.value.WORKER_APPLY_DT_FORMAT);

    await request('post', 'api/base/setIcRevisionWorker', {
      ...{
        IC_CODE: authStore.getIcCode,
        REV_NO_CODE: '02',
        ADMIN_ID: authStore.getWorkerNo,
        WORKER_APPLY_DT: revisionRequestContent.value.WORKER_APPLY_DT,
        WORKER_REV_NO: revisionRequestContent.value.WORKER_REV_NO,
        APPLY_DT: revisionRequestContent.value.WORKER_APPLY_DT,
        REV_NO: revisionRequestContent.value.WORKER_REV_NO,
        REV_DIV: '0',
      },
    });

    await request('post', 'api/base/requestRevision', {
      ...{
        IC_CODE: authStore.getIcCode,
        PACKET_SYSTEM_CODE_SEND: 'OPR094',
        PACKET_SYSTEM_CODE_RECV: 'MCOL00',
        PACKET_OP_DIV: '1000',
        PACKET_WORK_DIV: '0010',
        PACKET_SEND_DT: send_dt,
        PACKET_REPLY_CODE: '0000',
        PACKET_REVISION_DATA: '01000000000000000000',
      },
    });

    alert('개정지시가 완료되었습니다.');

    nextTick(() => (isLoading.value = false));
  } catch (error) {
    isLoading.value = false;
    alert('개정지시 중 오류 발생');
    console.error('개정지시 중 오류 발생:', error);
  }
};

const requestResignNo = async () => {
  try {
    isLoading.value = true;
    selectedContent.value.RESIGN_YN = 'N';
    const data = await request('post', 'api/base/setWorkerResign', {
      ...selectedContent.value,
    });

    if (data.ERROR_CODE >= 1) {
      alert('재직처리 되었습니다.');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    selectedContent.value.RESIGN_YN = 'Y';
    console.error('재직처리 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
  handleSearch();
};

const requestResignYes = async () => {
  try {
    isLoading.value = true;
    selectedContent.value.RESIGN_YN = 'Y';
    const data = await request('post', 'api/base/setWorkerResign', {
      ...selectedContent.value,
    });

    if (data.ERROR_CODE >= 1) {
      alert('퇴사처리 되었습니다.');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    selectedContent.value.RESIGN_YN = 'N';
    console.error('퇴사처리 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
  handleSearch();
};

const onClickChangePassword = () => {
  if (selectedContent.value.WORKER_NO === undefined) {
    showMessage('근무자를 선택하세요.', 'warning');
    return;
  }
  if (Number(selectedContent.value.CLASS) < Number(authStore.getWorkerClass)) {
    showMessage(`상위 근무자의 비밀번호는 변경할 수 없습니다.`, 'warning');
    return;
  }
  userInfo.value = {
    IC_CODE: selectedContent.value.IC_CODE,
    WORKER_NO: selectedContent.value.WORKER_NO,
    WORKER_NM: selectedContent.value.WORKER_NM,
    OLD_PWD: 'LbdLCNYQHfSHgfw9281vKYAJek865MMnBzZ12NitV216OdWSq3nhskbSE0JDpa1u',
  };
  isShowDialogModifyPassword.value = true;
};

const requestAddContent = async () => {
  if (!dialogInfo.newBase.isValid) {
    showMessage(`${dialogInfo.newBase.inputForm.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (!dialogInfo.detail.isValidNew) {
    showMessage(`${dialogInfo.detail.inputFormNew.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (!dialogInfo.other.isValidNew) {
    showMessage(`${dialogInfo.other.inputFormNew.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  /* 직원카드번호(미사용) */
  // if (newContent.value.MCARD_FORMAT && newContent.value.MCARD_FORMAT.replaceAll('-', '').length > 16) {
  //   showMessage('직원카드번호는 숫자 16자 이하로 입력해주세요.', 'warning');
  //   return;
  // }
  try {
    isLoading.value = true;
    newContent.value.JOIN_DATE = dateFormatToyyyyMMdd(newContent.value.JOIN_DATE_FORMAT);
    newContent.value.DOB = dateFormatToyyyyMMdd(newContent.value.DOB_FORMAT);
    /* 직원카드번호(미사용) */
    // if (newContent.value.MCARD_FORMAT) {
    //   newContent.value.MCARD = newContent.value.MCARD_FORMAT.replaceAll('-', '');
    // }
    const data = await request('post', 'api/base/addWorkerInfo', {
      ...newContent.value,
      ...{ IC_CODE: authStore.getIcCode },
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('추가되었습니다.');
      dialogAddContent.value = false;
      handleSearch();
    } else {
      showMessage(data.ERROR_MSG);
    }
  } catch (error) {
    showMessage('처리 중 오류가 발생했습니다.', 'error');
    console.error('처리 중 오류 발생:', error);
  }
  nextTick(() => (isLoading.value = false));
};

const handleSave = async () => {
  if (selectedContent.value.WORKER_NO === undefined) {
    showMessage('근무자를 선택하세요.');
    return;
  }
  if (!dialogInfo.base.isValid) {
    showMessage(`${dialogInfo.base.inputForm.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (!dialogInfo.detail.isValid) {
    showMessage(`${dialogInfo.detail.inputForm.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  if (!dialogInfo.other.isValid) {
    showMessage(`${dialogInfo.other.inputForm.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  /* 직원카드번호(미사용) */
  // if (selectedContent.value.MCARD_FORMAT && selectedContent.value.MCARD_FORMAT.replaceAll('-', '').length > 16) {
  //   showMessage('직원카드번호는 숫자 16자 이하로 입력해주세요.', 'warning');
  //   return;
  // }
  if (file_IMG_PROFILE_IMAGE.value[0] != null) {
    // 근무자 프로필 이미지
    try {
      isLoading.value = true;
      const token = Cookies.get('gdtcs-auth-token');
      const formData = new FormData();
      formData.set('IC_CODE', selectedContent.value.IC_CODE);
      formData.set('WORKER_NO', selectedContent.value.WORKER_NO);
      formData.set('PROFILE_IMAGE_FILE', file_IMG_PROFILE_IMAGE.value[0]);
      const response = await axios.post('/api/base/saveWorkerProfileImage', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      });
      if (response.data.ERROR_CODE >= 1) {
        showMessage('근무자 프로필 이미지가 저장되었습니다.');
        imageProfileUrl.value = `/api/base/getWorkerProfileImage?IC_CODE=${authStore.getIcCode}&FILE_PTH=${response.data.FILE_PTH}`;
        imgKey.value++;
      } else {
        showMessage('근무자 프로필 이미지가 저장에 실패하였습니다.', 'error');
        nextTick(() => (isLoading.value = false));
        return;
      }
    } catch (error) {
      showMessage(`근무자 프로필 이미지 파일 전송 중 오류가 발생했습니다.`, 'error');
      return;
    }
  }
  try {
    isLoading.value = true;
    if (selectedContent.value.JOIN_DATE_FORMAT != undefined)
      selectedContent.value.JOIN_DATE = dateFormatToyyyyMMdd(selectedContent.value.JOIN_DATE_FORMAT);
    if (selectedContent.value.DOB_FORMAT != undefined) selectedContent.value.DOB = dateFormatToyyyyMMdd(selectedContent.value.DOB_FORMAT);

    // selectedContent.value.DOB = dateFormatToyyyyMMdd(selectedContent.value.DOB_FORMAT);
    /* 직원카드번호(미사용) */
    // if (selectedContent.value.MCARD_FORMAT) {
    //   selectedContent.value.MCARD = selectedContent.value.MCARD_FORMAT.replaceAll('-', '');
    // }
    const data = await request('post', 'api/base/setWorkerInfo', {
      ...selectedContent.value,
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('근무자 정보가 수정되었습니다.');
      imgKey.value++;
    } else {
      showMessage(data.ERROR_MSG);
    }

    imgKey.value++;
    handleSearch();
  } catch (error) {
    showMessage('근무자 정보 수정 중 오류가 발생했습니다.', 'error');
    console.error('수정 중 오류 발생:', error);
  }
  nextTick(() => (isLoading.value = false));
};

const handleAdd = () => {
  clearNewContent();
  dialogAddContent.value = true;
};

const handleDelete = async () => {
  if (selectedContent.value.WORKER_NO === undefined) {
    alert('근무자를 선택하세요.');
    return;
  }
  if (confirm('삭제하시겠습니까?')) {
    try {
      isLoading.value = true;
      selectedContent.value.RESIGN_YN = 'N';
      const data = await request('post', 'api/base/removeWorker', {
        ...selectedContent.value,
      });

      if (data.ERROR_CODE >= 1) {
        showMessage('삭제되었습니다.', 'success');
        handleSearch();
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      selectedContent.value.RESIGN_YN = 'Y';
      console.error('삭제 중 오류 발생:', error);
    } finally {
      isLoading.value = false;
    }
  } else {
    showMessage(`삭제를 취소했습니다.`);
  }
};

const handleExcel = () => {
  const row = 1;
  ElMessageBox.confirm('엑셀 다운로드를 진행 하시겠습니까?', 'Excel download', {
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    type: 'success',
  })
    .then(async () => {
      const excelHeaders = mainHeaders.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
      excelDownload(row, searchHeader.value, searchData.value, excelHeaders, mainContents.value, '근무자 목록', '근무자 목록');
    })
    .catch(() => {
      ElMessage({
        type: 'warning',
        message: '액셀다운로드 취소',
      });
    });
};

btnHandler({
  Search: handleSearch,
  Add: handleAdd,
  Save: handleSave,
  Excel: handleExcel,
});

const onUpdateRevNo = (val) => {
  if (val > 99) return 1;
  else return val;
};

handleSearch();
</script>

<style scoped>
.v-label {
  color: #303133;
  opacity: 0.8;
  font-size: 14px !important;
  font-weight: bold;
  white-space: normal;
  word-break: keep-all;
}
.card-container {
  margin: 20px 20px 10px 20px;
}
.card-content-container {
  /* background-color: #0086e510; */
  margin-bottom: 17px;
}

.revision_table {
  width: 100%;
  font-size: 11px;
  border-collapse: collapse;
  /* border: #a9a9a9 1px solid; */
  text-align: center;
  color: #666666;
  height: 66px;
}
.revision_table th {
  padding: 2px 7px;
  background-color: #f5f5f5;
  border: #a9a9a9 1px solid;
}
.revision_table td {
  padding: 2px;
  border: #a9a9a9 1px solid;
}
.revision_table :deep(.v-text-field .v-field__input) {
  height: 100%;
  font-size: 13px;
  text-align: center;
  padding: 0 17px;
}
.revision_table .v-text-field :deep(input:read-only) {
  background-color: #efefef;
  opacity: 0.6 !important;
}
.revision_table input[type='text'],
.revision_table input[type='datetime-local'] {
  width: 100%;
  border-radius: 4px;
  border: #a9a9a9 1px solid;
  text-align: center;
  font-size: 13px;
  padding: 2px 10px;
}
.revision_table input[type='datetime-local']::-webkit-calendar-picker-indicator {
  width: 18px;
  height: 18px;
  opacity: 0.5;
}
.revision_table input[type='datetime-local']::-webkit-calendar-picker-indicator:hover {
  opacity: 1;
}
.revision_table .v-btn {
  margin-left: 5px;
  font-weight: bold;
}
.input-form-style:deep(th) {
  text-align: center;
}
</style>
