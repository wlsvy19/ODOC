<template>
  <v-container class="fill-height" fluid>
    <v-row justify="center" align="center">
      <v-col cols="12">
        <v-card class="elevation-12" width="400" style="margin: auto">
          <img src="@/assets/images/CI.png" alt="Logo" class="ci-logo" />
          <v-toolbar :color="colorStore.basicColor" dark flat style="text-align: center">
            <v-toolbar-title class="font-bold-ac" style="font-size: 22px">광안대교 운영단말시스템</v-toolbar-title>
          </v-toolbar>

          <v-card-text>
            <v-form>
              <v-text-field
                label="영업소ID를 입력하세요."
                v-model="officeId"
                prepend-icon="mdi-office-building"
                class="mb-1"
                ref="refIcCode"
                required
                @keyup.enter="submitForm"
              ></v-text-field>

              <v-text-field
                label="근무자번호를 입력하세요."
                v-model="userId"
                prepend-icon="mdi-account"
                class="mb-1"
                ref="refWorkerNo"
                required
                @keyup.enter="submitForm"
              ></v-text-field>

              <v-text-field
                label="비밀번호를 입력하세요."
                v-model="password"
                prepend-icon="mdi-lock"
                :type="showPassword ? 'text' : 'password'"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showPassword = !showPassword"
                class="mb-1"
                ref="refPassword"
                required
                hide-details
                autocomplete="off"
                @keyup.enter="submitForm"
              ></v-text-field>

              <v-checkbox v-model="rememberId" label="아이디 저장" class="mb-1" hide-details></v-checkbox>

              <v-btn class="mb-1" :color="colorStore.basicColor" block @click="submitForm" style="font-size: 18px; height: 50px">로그인</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore, useColorStore, loadSystemSmallCodeAll } from '@/stores/index';
import { request } from '@/utils/common';

const colorStore = useColorStore();
const systemCode = loadSystemSmallCodeAll();
const officeId = ref('');
const userId = ref('');
const password = ref('');
const showPassword = ref(false);
const rememberId = ref(false);

const refIcCode = ref(null);
const refWorkerNo = ref(null);
const refPassword = ref(null);
const systemCodeAll = ref([]);
const router = useRouter();
const submitForm = async () => {
  if (officeId.value === '') {
    alert('영업소ID를 입력해주세요.');
    refIcCode.value.focus();
    return;
  }
  if (userId.value === '') {
    alert('근무자번호를 입력해주세요.');
    refWorkerNo.value.focus();
    return;
  }
  if (password.value === '') {
    alert('비밀번호를 입력해주세요.');
    refPassword.value.focus();
    return;
  }

  try {
    const response = await request('post', '/api/login', {
      icCode: officeId.value,
      workerNo: userId.value,
      pw: password.value,
    });
    const authStore = useAuthStore();
    if (response.code === 'SUCCESS') {
      systemCodeAll.value = await systemCode.setSystemSmallCodeAll();
      authStore.login(response.accessToken);
      authStore.setIcNm(response.IC_NM);
      authStore.setWorkerNo(response.WORKER_NO);
      authStore.setWorkerNm(response.WORKER_NM);
      authStore.setWorkerClass(response.WORKER_CLS);
      authStore.setIcCode(response.IC_CODE);
      authStore.setLoginTime(response.loginTime);
      authStore.setIP(response.IP);
      localStorage.setItem('systemCode', JSON.stringify(systemCodeAll.value));
      localStorage.setItem('icNm', response.IC_NM);
      localStorage.setItem('loginWorkerNo', response.WORKER_NO);
      localStorage.setItem('loginIcCode', response.IC_CODE);
      localStorage.setItem('workerNm', response.WORKER_NM);
      localStorage.setItem('workerClass', response.WORKER_CLS);
      localStorage.setItem('loginTime', response.loginTime);
      if (response.IP === null || response.IP === 'undefined') {
        localStorage.setItem('IP', 'cannot be verified ip.');
      } else {
        localStorage.setItem('IP', response.IP);
      }

      router.push('/main');
      if (rememberId.value) {
        localStorage.setItem('saveOfficeId', officeId.value);
        localStorage.setItem('saveUserId', userId.value);
      } else {
        localStorage.removeItem('saveUserId');
        localStorage.removeItem('saveOfficeId');
      }
    } else if (response.code === 'FAIL') {
      alert(response.message);
      return false;
    } else if (response.code === 'NOUSER') {
      alert(response.message);
      return false;
    }
  } catch (error) {
    console.log(error);
    alert('서버 에러로 인해 로그인에 실패했습니다. 관리자한테 문의하세요.', error);
  }
};

onMounted(() => {
  const saveUserId = localStorage.getItem('saveUserId');
  const saveOfficeId = localStorage.getItem('saveOfficeId');
  if (saveUserId) {
    userId.value = saveUserId;
    rememberId.value = true;
  }
  if (saveOfficeId) {
    officeId.value = saveOfficeId;
  }
});
</script>
A

<style scoped>
.fill-height {
  background-image: url('@/assets/images/login-bg-img.png');
  background-size: cover;
  background-position: center;
}

.v-card {
  background-color: rgba(255, 255, 255);
}
.v-col-md-4 {
  max-width: 20%;
  margin-bottom: 600px;
}
.ci-logo {
  display: block;
  margin: 10px auto;
  width: 35%;
  opacity: 0.8;
}
</style>
