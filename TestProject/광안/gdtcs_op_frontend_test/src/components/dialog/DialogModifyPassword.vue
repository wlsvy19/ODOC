<template>
  <v-dialog id="DialogModifyPassword" width="360px" v-if="isActive" v-model:modelValue="isActive" persistent z-index="1100">
    <LoadingComponent v-if="actionModel.isLoading" />
    <v-card>
      <v-toolbar :color="colorStore.basicColor" title="비밀번호 변경" />
      <div style="margin: 30px">
        <v-text-field style="margin-bottom: 17px" variant="outlined" label="근무자번호" v-model="dataModel.WORKER_NO" hide-details readonly />
        <v-text-field style="margin-bottom: 17px" variant="outlined" label="근무자명" v-model="dataModel.WORKER_NM" hide-details readonly />
        <v-form ref="inputForm">
          <div style="margin-bottom: 27px; border-top: solid 1px #a9a9a9" />
          <v-text-field
            v-if="!userInfo.OLD_PWD"
            style="margin-bottom: 10px"
            variant="outlined"
            v-model="dataModel.OLD_PWD"
            :append-inner-icon="actionModel.isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="InputRules.passwordRule"
            :type="actionModel.isShowPassword ? 'text' : 'password'"
            label="기존 비밀번호를 입력해주세요."
            @click:append-inner="actionModel.isShowPassword = !actionModel.isShowPassword"
            autocomplete="off"
          />
          <v-text-field
            style="margin-bottom: 10px"
            variant="outlined"
            v-model="dataModel.NEW_PWD"
            :append-inner-icon="actionModel.isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="InputRules.passwordRule"
            :type="actionModel.isShowPassword ? 'text' : 'password'"
            label="새 비밀번호를 입력해주세요."
            @click:append-inner="actionModel.isShowPassword = !actionModel.isShowPassword"
            autocomplete="off"
          />
          <v-text-field
            style="margin-bottom: 10px"
            variant="outlined"
            v-model="dataModel.NEW_PWD_2"
            :append-inner-icon="actionModel.isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="InputRules.passwordRule"
            :type="actionModel.isShowPassword ? 'text' : 'password'"
            label="비밀번호를 확인해주세요."
            @click:append-inner="actionModel.isShowPassword = !actionModel.isShowPassword"
            autocomplete="off"
          />
          <v-row>
            <v-spacer />
            <v-btn style="margin-left: 7px" width="40px" variant="tonal" :color="colorStore.basicColor" text="취소" @click="onClickCancel" />
            <v-btn style="margin-left: 7px" variant="flat" :color="colorStore.basicColor" text="비밀번호 변경" @click="onClickSubmit" />
          </v-row>
        </v-form>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { useColorStore } from '@/stores';
import { request, showMessage } from '@/utils/common';
import { InputRules } from '@/utils/rules';
import { nextTick, onUpdated, reactive, ref } from 'vue';

const colorStore = useColorStore();

const isActive = defineModel('isActive', {
  type: Boolean,
});

const userInfo = defineModel('userInfo', {
  type: Object,
  required: true,
});

const inputForm = ref();
const isValid = ref(true);

const actionModel = reactive({
  isShowPassword: false,
  isLoading: false,
  isValid: true,
});

const dataModel = reactive({
  IC_CODE: '',
  WORKER_NO: '',
  WORKER_NM: '',
  OLD_PWD: '',
  NEW_PWD: '',
  NEW_PWD_2: '',
});

onUpdated(async () => {
  dataModel.IC_CODE = userInfo.value.IC_CODE;
  dataModel.WORKER_NO = userInfo.value.WORKER_NO;
  dataModel.WORKER_NM = userInfo.value.WORKER_NM;
  dataModel.OLD_PWD = userInfo.value.OLD_PWD;
});

const resetDialog = () => {
  dataModel.OLD_PWD = '';
  dataModel.NEW_PWD = '';
  dataModel.NEW_PWD_2 = '';
  actionModel.isShowPassword = false;
  actionModel.isLoading = false;
};

const onClickCancel = async () => {
  isActive.value = false;
  await nextTick();
  resetDialog();
};

const onClickSubmit = async () => {
  if (dataModel.NEW_PWD != dataModel.NEW_PWD_2) {
    showMessage(`입력한 비밀번호가 일치하지 않습니다.`, 'error');
    return;
  }
  try {
    actionModel.isLoading = true;
    const data = await request('post', 'api/base/changeWorkerPassword', dataModel);
    await onClickCancel();
    if (data.ERROR_CODE >= 1) {
      showMessage('비밀번호가 변경되었습니다.', 'success');
    } else {
      showMessage(`[${data.ERROR_CODE}] ${data.ERROR_MSG}`, 'error');
      resetDialog();
    }
  } catch (error) {
    showMessage(`비밀번호 변경 중 오류가 발생했습니다.`, 'error');
    resetDialog();
  }
};
</script>
<style scope>
#DialogModifyPassword .v-text-field:not(.mb-1) .v-field__input {
  height: 50px;
  min-height: 20px;
  font-size: 16px;
  letter-spacing: 1px;
  padding: 5px 20px 0px 20px;
}
#DialogModifyPassword .v-text-field:not(.mb-1) .v-field__input[type='password'] {
  font-size: large;
  font-family: NanumSquare_ac;
}
#DialogModifyPassword .v-text-field .v-field.v-field--appended {
  padding-right: 20px;
}
#DialogModifyPassword .v-btn {
  font-weight: bold;
}
</style>
