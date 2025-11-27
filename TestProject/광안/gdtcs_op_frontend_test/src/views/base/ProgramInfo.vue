<template>
  <v-tabs v-model="tab" bg-color="#F5F5F5" :color="colorStore.basicColor" height="25">
    <v-tab class="font-bold-ac" style="font-size: 13px">프로그램 관리</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">보고서 관리</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item>
      <TableComponent
        scroll-key="gridKeyProgram"
        :headers="programHeaders"
        :contents="programContents"
        @grid-click-event="onClickProgram"
        @grid-dbl-click-event="onDoubleClickProgram"
        :height-offset="189 + 28 + 29 * 2"
        row-type="mix"
      />
      <v-dialog v-model="dialogModifyProgram" max-width="540px">
        <v-card>
          <v-toolbar class="dialog-toolbar">
            <v-toolbar-title>프로그램 정보 수정</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-row>
              <v-col cols="12">
                <h3>프로그램 정보</h3>
                <InputFormGrid
                  v-model="selectedProgramContent"
                  :headers="dialogModifyProgramHeaders"
                  :cols-per-row="1"
                  :table-header-width="'20%'"
                  :row-height="'31px'"
                />
              </v-col>
            </v-row>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="flat" color="blue darken-1" text @click="requestModifyProgram">수정</v-btn>
            <v-btn variant="flat" color="black darken-1" text @click="dialogModifyProgram = false">취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-window-item>
    <v-window-item>
      <table id="report_image">
        <tr>
          <th>구분</th>
          <th>사진</th>
          <th>첨부</th>
        </tr>
        <tr>
          <td>로고</td>
          <td><img id="IMG_RPT_APP_CODE_9" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=9&cacheKey=${imgKey9}`" key="imgKey9" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_9" label="로고" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
        <tr>
          <td>결재란1</td>
          <td><img id="IMG_RPT_APP_CODE_1" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=1&cacheKey=${imgKey1}`" key="imgKey1" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_1" label="결재란1" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
        <tr>
          <td>결재란2</td>
          <td><img id="IMG_RPT_APP_CODE_2" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=2&cacheKey=${imgKey2}`" key="imgKey2" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_2" label="결재란2" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
        <tr>
          <td>결재란3</td>
          <td><img id="IMG_RPT_APP_CODE_3" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=3&cacheKey=${imgKey3}`" key="imgKey3" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_3" label="결재란3" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
        <tr>
          <td>결재란4</td>
          <td><img id="IMG_RPT_APP_CODE_4" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=4&cacheKey=${imgKey4}`" key="imgKey4" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_4" label="결재란4" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
        <tr>
          <td>결재란5</td>
          <td><img id="IMG_RPT_APP_CODE_5" :src="`/api/base/getReportImageFile?IC_CODE=${authStore.getIcCode}&RPT_APP_CODE=5&cacheKey=${imgKey5}`" key="imgKey5" /></td>
          <td><v-file-input v-model="file_RPT_APP_CODE_5" label="결재란5" accept="image/png, image/jpeg, image/bmp" prepend-icon="mdi-camera" /></td>
        </tr>
      </table>
    </v-window-item>
    <LoadingComponent v-if="isLoading" />
  </v-window>
</template>

<script setup>
import { ref, onActivated, watch, nextTick } from 'vue';
import { request, btnHandler, changeButtonStatusSave, changeButtonStatusSearch, getSystemSmallCode } from '@/utils/common';
import { useColorStore } from '@/stores/index';
import { useAuthStore } from '@/stores/index';
import axios from 'axios';
import Cookies from 'js-cookie';

const authStore = useAuthStore();
const colorStore = useColorStore();

const isLoading = ref(false);
const imgKey9 = ref(0);
const imgKey1 = ref(0);
const imgKey2 = ref(0);
const imgKey3 = ref(0);
const imgKey4 = ref(0);
const imgKey5 = ref(0);

const tab = ref(0);

const programContents = ref([]);
const dialogModifyProgram = ref(false);

const selectedProgramContent = ref([]);
const file_RPT_APP_CODE_1 = ref([]);
const file_RPT_APP_CODE_2 = ref([]);
const file_RPT_APP_CODE_3 = ref([]);
const file_RPT_APP_CODE_4 = ref([]);
const file_RPT_APP_CODE_5 = ref([]);
const file_RPT_APP_CODE_9 = ref([]);
const code_ACC = ref([]);
const code_RPT_APP_CODE = ref([]);
const code_USE_YN = ref([]);

onActivated(async () => {
  code_ACC.value = getSystemSmallCode('900');
  code_RPT_APP_CODE.value = getSystemSmallCode('241');
  code_USE_YN.value = getSystemSmallCode('047');
  await nextTick();
  await handleSearch();
});

watch(
  tab,
  async () => {
    if (tab.value === 0) {
      changeButtonStatusSearch('Y');
      changeButtonStatusSave('N');
      await handleSearch();
    } else if (tab.value === 1) {
      changeButtonStatusSearch('N');
      changeButtonStatusSave('Y');
      //await handleSearch();
    }
  },
  { immediate: false },
);

const programHeaders = ref([
{
    title: '상위 프로그램',
    align: 'center',
    children: [
      { title: '프로그램ID', key: 'HIGH_PRG_CODE', align: 'center', width: '110' },
      { title: '프로그램명', key: 'HIGH_PRG_NM', align: 'center', width: '110' },
    ],
  },
  {
    title: '하위 프로그램',
    align: 'center',
    children: [
      { title: '프로그램ID', key: 'PRG_CODE', align: 'center', width: '90' },
      { title: '프로그램명', key: 'PRG_NM', align: 'center', width: '210', customBodyCellStyle: 'table-body-style-left' },
      // { title: '순서', key: 'ORDR', align: 'center', width: '70' },
    ],
  },
  {
    title: '접근권한',
    align: 'center',
    children: [
    { title: '프로그램', key: 'USE_YN_NM', align: 'center', width: '70' },
      { title: '최상위관리자', key: 'ACC1_NM', align: 'center', width: '95' },
      { title: '상위관리자', key: 'ACC2_NM', align: 'center', width: '85' },
      { title: '영업관리자', key: 'ACC3_NM', align: 'center', width: '85' },
      { title: '일반근무자', key: 'ACC4_NM', align: 'center', width: '85' },
      { title: '유지보수', key: 'ACC7_NM', align: 'center', width: '80' },
    ],
  },
  {
    title: '기타',
    align: 'center',
    children: [
      { title: '결재란', key: 'RPT_APP_CODE_NM', align: 'center', width: '70' },
      { title: '비고', key: 'PRG_NOTE', align: 'center', width: '250', customBodyCellStyle: 'table-body-style-left' },
    ],
  },
]);

const dialogModifyProgramHeaders = ref([
  { title: '상위프로그램ID', key: 'HIGH_PRG_CODE', align: 'center', option: 'label' },
  { title: '상위프로그램명', key: 'HIGH_PRG_NM', align: 'center', option: 'label' },
  { title: '프로그램ID', key: 'PRG_CODE', align: 'center', option: 'label' },
  { title: '프로그램명', key: 'PRG_NM', align: 'center' },
  { title: '프로그램', key: 'USE_YN', align: 'center', option: 'select', selectItem: code_USE_YN },
  { title: '최상위관리자', key: 'ACC1', align: 'center', option: 'select', selectItem: code_ACC },
  { title: '상위관리자', key: 'ACC2', align: 'center', option: 'select', selectItem: code_ACC },
  { title: '영업관리자', key: 'ACC3', align: 'center', option: 'select', selectItem: code_ACC },
  { title: '일반근무자', key: 'ACC4', align: 'center', option: 'select', selectItem: code_ACC },
  { title: '유지보수', key: 'ACC7', align: 'center', option: 'select', selectItem: code_ACC },
  { title: '결재란', key: 'RPT_APP_CODE', align: 'center', option: 'select', selectItem: code_RPT_APP_CODE },
  { title: '비고', key: 'PRG_NOTE', align: 'center', size: 1 },
]);

const onClickProgram = async (event) => {
  selectedProgramContent.value = { ...event };
};

const onDoubleClickProgram = async (index, item) => {
  selectedProgramContent.value = { ...item };
  dialogModifyProgram.value = true;
};

const requestModifyProgram = async () => {
  isLoading.value = true;
  try {
    const data = await request('post', 'api/base/setProgram', {
      ...selectedProgramContent.value,
    });

    if (data.ERROR_CODE >= 1) {
      alert('수정되었습니다.');
    } else {
      alert(data.ERROR_MSG);
    }
    handleSearch();
  } catch (error) {
    console.error('수정 중 오류 발생:', error);
  }
  dialogModifyProgram.value = false;
  nextTick(() => (isLoading.value = false));
};

const handleSearch = async () => {
  isLoading.value = true;
  if (tab.value === 0) {
    try {
      const data = await request('post', 'api/base/getProgramList', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      programContents.value = data;

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  }
  nextTick(() => (isLoading.value = false));
};

const handleSave = async () => {
  isLoading.value = true;
  if (tab.value === 0) { // 프로그램 관리
    if (selectedProgramContent.value.IC_CODE === undefined) {
      alert('프로그램을 선택하세요.');
    } else {
      dialogModifyProgram.value = true;
    }
  } else if (tab.value === 1) { // 보고서 관리
    const token = Cookies.get('gdtcs-auth-token');
    if (file_RPT_APP_CODE_9.value[0] != null) {
      // 로고
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '9');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_9.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('로고 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_9.value = null;
          imgKey9.value++;
        } else {
          alert('로고 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('로고 이미지 파일 전송 중 오류 발생:', error);
      }
    }
    if (file_RPT_APP_CODE_1.value[0] != null) {
      // 결재란 1
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '1');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_1.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('결재란1 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_1.value = null;
          imgKey1.value++;
        } else {
          alert('결재란1 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('결재란1 이미지 파일 전송 중 오류 발생:', error);
      }
    }
    if (file_RPT_APP_CODE_2.value[0] != null) {
      // 결재란 2
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '2');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_2.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('결재란2 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_2.value = null;
          imgKey2.value++;
        } else {
          alert('결재란2 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('결재란2 이미지 파일 전송 중 오류 발생:', error);
      }
    }
    if (file_RPT_APP_CODE_3.value[0] != null) {
      // 결재란 3
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '3');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_3.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('결재란3 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_3.value = null;
          imgKey3.value++;
        } else {
          alert('결재란3 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('결재란3 이미지 파일 전송 중 오류 발생:', error);
      }
    }
    if (file_RPT_APP_CODE_4.value[0] != null) {
      // 결재란 4
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '4');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_4.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('결재란4 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_4.value = null;
          imgKey4.value++;
        } else {
          alert('결재란4 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('결재란4 이미지 파일 전송 중 오류 발생:', error);
      }
    }
    if (file_RPT_APP_CODE_5.value[0] != null) {
      // 결재란 5
      try {
        const formData = new FormData();
        formData.set('IC_CODE', '094');
        formData.set('RPT_APP_CODE', '5');
        formData.set('RPT_APP_IMAGE_FILE', file_RPT_APP_CODE_5.value[0]);
        const response = await axios.post('/api/base/saveReportImageFile', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
        if (response.data.ERROR_CODE >= 1) {
          alert('결재란5 이미지가 저장되었습니다.');
          file_RPT_APP_CODE_5.value = null;
          imgKey5.value++;
        } else {
          alert('결재란5 이미지가 저장에 실패하였습니다.');
        }
      } catch (error) {
        console.error('결재란5 이미지 파일 전송 중 오류 발생:', error);
      }
    }
  }
  nextTick(() => (isLoading.value = false));
};

btnHandler({
  Search: handleSearch,
  Save: handleSave,
});
</script>

<style scoped>
#report_image {
  width: 100%;
  text-align: center;
  border-collapse: collapse;
  text-align: center;
}
#report_image img {
  max-height: 80px;
}
#report_image td,
th {
  border: #a9a9a9 1px solid;
  padding: 5px;
}
</style>
