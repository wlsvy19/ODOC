<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="onProcessing.revision" :message="`개정지시 중입니다.`" />
  <v-tabs v-model="tab" @update:model-value="onChangedTab" bg-color="#F5F5F5" :color="colorStore.basicColor" height="25">
    <v-tab class="font-bold-ac" style="font-size: 13px">영업소 기초정보</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">차로정보</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">기준요금</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">차등요금</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">차로개정현황</v-tab>
    <v-tab class="font-bold-ac" style="font-size: 13px">개정정보(지시)</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <LoadingComponent v-if="isLoading" />
    <LoadingComponent v-if="onProcessing.default" :message="`처리 중입니다.`" />
    <v-window-item class="input-form-style" :style="`margin: 0 auto; margin-left: 30px;`">
      <InformationComponent style="margin-top: 14px" message="영업소 기초정보 수정 후, 상단의 저장버튼을 눌러주세요." />
      <InformationComponent
        style="margin-top: 7px"
        message="할인/할증, 할인단말기, 출퇴근할인/시간대 할인 상세정보는 상세정보 관리에서 수정해주세요."
      />
      <InformationComponent
        style="margin-top: 7px"
        icon-type="attention"
        message="제도 적용 현황의 '연속통행할인 제도 적용일자'는 연속통행할인 개정 시에 적용됩니다."
      />
      <InformationComponent
        style="margin-top: 7px"
        icon-type="attention"
        message="제도 적용 현황의 '차등요금 제도 적용일자'는 차등요금제 개정 시에 적용됩니다."
      />
      <v-card elevation="4" style="width: 840px; margin-top: 10px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>기본 정보</h3>
          <InputFormGrid
            v-model="baseInfo.contents"
            :headers="baseInfo.office"
            v-model:is-valid="isValidBaseInfoOffice"
            v-model:input-form="inputFormBaseInfoOffice"
            :cols-per-row="4"
            :table-header-width="'15%'"
            :row-height="'35px'"
          />
        </div>
      </v-card>
      <v-card elevation="4" style="width: 840px; margin-top: 20px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>일마감 정보</h3>
          <InputFormGrid v-model="baseInfo.contents" :headers="baseInfo.workfin" :cols-per-row="2" :table-header-width="'18%'" :row-height="'35px'" />
        </div>
      </v-card>
      <v-card elevation="4" style="width: 840px; margin-top: 20px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>제도 적용 현황</h3>
          <InputFormGrid v-model="baseInfo.contents" :headers="baseInfo.system" :cols-per-row="3" :table-header-width="'18%'" :row-height="'35px'" />
        </div>
      </v-card>
      <v-card elevation="4" style="width: 840px; margin-top: 20px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>차로 정보</h3>
          <InputFormGrid v-model="baseInfo.contents" :headers="baseInfo.lane" :cols-per-row="3" :table-header-width="'18%'" :row-height="'35px'" />
        </div>
      </v-card>
    </v-window-item>
    <v-window-item>
      <InformationComponent style="margin: 14px 0 10px 0" message="수정할 차로를 더블클릭하여 수정 후, 저장버튼을 눌러주세요." />
      <TableComponent
        scroll-key="gridKeyOperationLane"
        :headers="laneInfo.main.headers"
        :contents="laneInfo.main.contents"
        @grid-dbl-click-event="onDoubleClickLane"
        :height-offset="189 + 29 + 28 + 42"
        row-type="1"
      />
      <v-dialog v-model="laneInfo.dialog.isShow" max-width="600px">
        <v-card>
          <LoadingComponent v-if="onProcessing.laneInfo" :message="`저장 중입니다.`" />
          <v-toolbar class="dialog-toolbar">
            <v-toolbar-title>차로 정보 수정</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <InputFormGrid
              class="input-form-style"
              v-model="laneInfo.dialog.contents"
              :headers="laneInfo.dialog.headers"
              :cols-per-row="2"
              :table-header-width="'20%'"
              :covertNumberToString="true"
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="flat" color="blue darken-1" text @click="saveLaneInfo">저장</v-btn>
            <v-btn variant="flat" color="black darken-1" text @click="laneInfo.dialog.isShow = false">취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-window-item>
    <v-window-item class="input-form-style" :style="`margin: 0 auto; margin-left: 30px;`">
      <InformationComponent style="margin-top: 14px" message="기준요금 정보를 수정 후, 상단의 저장버튼을 눌러주세요." />
      <InformationComponent style="margin-top: 7px" message="신정보 적용일시는 개정 시에 설정합니다." />
      <v-card elevation="4" style="width: 600px; margin-top: 10px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>기본 정보</h3>
          <InputFormGrid
            v-model="standardFareInfo.contents"
            :headers="standardFareInfo.base"
            :cols-per-row="2"
            :is-loading="isLoading"
            tableHeaderWidth="15%"
            row-height="30px"
          />
        </div>
      </v-card>
      <v-card elevation="4" style="width: 600px; margin-top: 20px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>요금 정보</h3>
          <v-row>
            <v-col>
              <div style="width: 99%">
                <table class="table-header-style">
                  <tr>
                    <th>신요금</th>
                    <th>
                      <v-btn variant="flat" size="small" color="blue darken-1" text="신→구" @click="moveNewToOldStandardFareInfo" />
                    </th>
                  </tr>
                </table>
                <InputFormGrid
                  v-model="standardFareInfo.contents"
                  :headers="standardFareInfo.new"
                  :cols-per-row="1"
                  :is-loading="isLoading"
                  :tableHeaderDisplay="true"
                  tableHeaderWidth="20%"
                />
              </div>
            </v-col>
            <v-col>
              <div style="width: 99%; margin-left: auto">
                <table class="table-header-style">
                  <tr>
                    <th>구요금</th>
                    <th></th>
                  </tr>
                </table>
                <InputFormGrid
                  v-model="standardFareInfo.contents"
                  :headers="standardFareInfo.old"
                  :cols-per-row="1"
                  :is-loading="isLoading"
                  :tableHeaderDisplay="true"
                  tableHeaderWidth="20%"
                />
              </div>
            </v-col>
          </v-row>
        </div>
      </v-card>
    </v-window-item>
    <v-window-item class="input-form-style" :style="`margin: 0 auto; margin-left: 30px;`">
      <InformationComponent style="margin-top: 14px" message="차등요금 정보를 수정 후, 상단의 저장버튼을 눌러주세요." />
      <InformationComponent style="margin-top: 7px" message="신정보 적용일시는 개정 시에 설정합니다." />
      <v-card elevation="4" style="width: 600px; margin-top: 10px; margin-bottom: 5px">
        <div style="margin: 0 20px 20px 20px">
          <v-row>
            <v-col><h3>기본 정보</h3></v-col>
            <v-col><InformationComponent style="margin-top: 27px" message="제도적용일자는 영업소 기초정보에서 수정해주세요." /></v-col>
          </v-row>
          <v-row>
            <v-col>
              <div style="width: 99%">
                <InputFormGrid row-height="45px" v-model="diffFareInfo.contents" :headers="diffFareInfo.base" table-header-width="30%" />
              </div>
            </v-col>
            <v-col>
              <div style="width: 99%; margin-left: auto">
                <InputFormGrid row-height="30px" v-model="diffFareInfo.contents" :headers="diffFareInfo.state" table-header-width="30%" />
              </div>
            </v-col>
          </v-row>
        </div>
      </v-card>
      <v-card elevation="4" style="width: 600px; margin-top: 20px; margin-bottom: 5px">
        <div style="margin: 20px">
          <h3>요금 정보</h3>
          <v-row>
            <v-col>
              <div style="width: 99%">
                <table class="table-header-style">
                  <tr>
                    <th>신요금</th>
                    <th>
                      <v-btn variant="flat" size="small" color="blue darken-1" text="신→구" @click="moveNewToOldDiffFareInfo" />
                    </th>
                  </tr>
                </table>
                <InputFormGrid v-model="diffFareInfo.contents" :headers="diffFareInfo.new" table-header-width="15%" />
              </div>
            </v-col>
            <v-col>
              <div style="width: 99%; margin-left: auto">
                <table class="table-header-style">
                  <tr>
                    <th>구요금</th>
                    <th></th>
                  </tr>
                </table>
                <InputFormGrid v-model="diffFareInfo.contents" :headers="diffFareInfo.old" table-header-width="15%" />
              </div>
            </v-col>
          </v-row>
        </div>
      </v-card>
    </v-window-item>
    <v-window-item>
      <div style="margin: 20px 20px 10px 20px">
        <h3>TCS 개정현황</h3>
      </div>
      <div style="width: 587px; border-bottom: 1px solid #e0e0e0">
        <TableComponent
          scroll-key="gridKeyOperationRevisionCurrentTcs"
          :headers="revisionState.TCS.headers"
          :contents="revisionState.TCS.contents"
          row-type="mix"
          :height-offset="189 + 29 + 28 + 28 * 4 + 115"
          :height-percent="13"
        />
      </div>
      <div style="margin: 20px 20px 10px 20px">
        <h3>하이패스 개정현황</h3>
      </div>
      <TableComponent
        scroll-key="gridKeyOperationRevisionCurrent"
        :headers="revisionState.ETCS.headers"
        :contents="revisionState.ETCS.contents"
        row-type="mix"
        :height-offset="189 + 29 + 28 * 4 + 115"
        :height-percent="87"
      />
    </v-window-item>
    <v-window-item>
      <v-row>
        <v-col cols="12">
          <table id="revision_table">
            <tr>
              <th>선택</th>
              <th>개정 구분</th>
              <th>신정보 적용일시</th>
              <th>개정 번호</th>
              <th>입력 방법</th>
              <th>
                <v-btn variant="flat" :color="colorStore.basicColor" text="개정지시" @click="requestRevision" />
              </th>
            </tr>
            <tr v-for="(item, index) in revisionInfo.headers" :key="index">
              <td><input type="checkbox" v-model="item.checked" :disabled="item.code == '09' ? true : false" /></td>
              <td style="text-align: left">{{ `${item.name}` }}</td>
              <td><input type="datetime-local" v-model="revisionInfo.contents[item.keyApplyDt + '_INPUT']" /></td>
              <td>
                <v-number-input
                  variant="outlined"
                  control-variant="stacked"
                  v-model="revisionInfo.contents[item.keyRevNo]"
                  :min="1"
                  :rules="InputRules.requiredRule"
                  @update="revisionInfo.contents[item.keyRevNo] = onUpdateRevNo(revisionInfo.contents[item.keyRevNo])"
                  :readonly="item.readonly"
                  hide-details
                />
              </td>
              <td>
                <v-btn variant="tonal" :color="colorStore.basicColor" @click="onClickAutoIncrement(index)" text="자동입력" />
                <v-btn variant="tonal" color="#909090" @click="item.readonly = false" text="수동입력 " />
              </td>
              <td></td>
            </tr>
          </table>
        </v-col>
      </v-row>
    </v-window-item>
  </v-window>
</template>

<script setup>
import { ref, onMounted, reactive, onActivated } from 'vue';
import {
  request,
  btnHandler,
  yyyyMMddHHmmssToDatetimeLocal,
  datetimeLocalToyyyyMMddHHmmss,
  getCurrentDatetime,
  yyyyMMddToDateFormat,
  changeButtonStatusSave,
  showMessage,
  getSystemSmallCode,
  datetimeLocalToyyyyMMdd,
} from '@/utils/common';
import { loadSystemSmallCodeAll, useColorStore } from '@/stores/index';
import { useAuthStore } from '@/stores/index';
import { InputRules } from '@/utils/rules';
import dayjs from 'dayjs';
import { toRaw } from 'vue';
import LoadingComponent from '@/components/common/LoadingComponent.vue';
import InformationComponent from '@/components/common/InformationComponent.vue';

const authStore = useAuthStore();
const colorStore = useColorStore();

const isLoading = ref(false);
const onProcessing = reactive({
  default: false,
  revision: false,
  laneInfo: false,
});
const tab = ref(0);
const onChangedTab = () => {
  if (tab.value === 0) {
    changeButtonStatusSave('Y');
  } else if (tab.value === 1) {
    changeButtonStatusSave('N');
  } else if (tab.value === 2) {
    changeButtonStatusSave('Y');
  } else if (tab.value === 3) {
    changeButtonStatusSave('Y');
  } else if (tab.value === 4) {
    changeButtonStatusSave('N');
  } else if (tab.value === 5) {
    changeButtonStatusSave('N');
  }
  handleSearch();
};

onActivated(async () => {
  onChangedTab();
});

const code_APPLY_DIV = ref([]);
const code_USE_DIV = ref([]);
const code_LANE_TYPE = ref([]);
const code_COMM_MTD = ref([]);
const code_LANE_INFO_DIV = ref([]);
const code_UP_DOWN_DIV = ref([]);
const code_DIR_DIV = ref([]);
const code_ENT_EXIT_TYPE = ref([]);
const code_LANE_UNP_PAY_DIV = ref([]);
const code_LANE_SET = ref([]);
const code_BALC_DOWN_DIV = ref([]);

const setSystemCode = () => {
  code_APPLY_DIV.value = getSystemSmallCode('048');
  code_USE_DIV.value = getSystemSmallCode('046');
  code_LANE_TYPE.value = getSystemSmallCode('337');
  code_COMM_MTD.value = getSystemSmallCode('340');
  code_LANE_INFO_DIV.value = getSystemSmallCode('341');
  code_UP_DOWN_DIV.value = getSystemSmallCode('342');
  code_DIR_DIV.value = getSystemSmallCode('343');
  code_ENT_EXIT_TYPE.value = getSystemSmallCode('344');
  code_LANE_UNP_PAY_DIV.value = getSystemSmallCode('345');
  code_LANE_SET.value = getSystemSmallCode('346');
  code_BALC_DOWN_DIV.value = getSystemSmallCode('221');
};

onMounted(async () => {
  try {
    isLoading.value = true;
    await loadSystemSmallCodeAll().setSystemSmallCodeAll(localStorage.getItem('systemCode'));
    setSystemCode();
    await handleSearch();
  } catch (error) {
    showMessage('시스템 정보 로드 중 오류가 발생했습니다.', 'error');
  } finally {
    isLoading.value = false;
  }
});

/* 영업소 기초정보 */
const baseInfo = reactive({
  office: [
    { title: '영업소번호', key: 'IC_CODE', option: 'label' },
    { title: '영업소명', key: 'IC_NM', rules: InputRules.icNameRule },
    { title: '전화번호', key: 'IC_TEL', rules: InputRules.icTelRule },
  ],
  workfin: [
    { title: '일변경시각', key: 'DAYCHG_TM', option: 'timepicker', format: 'HHmm', readonly: true },
    { title: '일마감시각', key: 'DAYFIN_TM', option: 'timepicker', format: 'HHmm', readonly: true },
  ],
  system: [
    { title: '경차분류 적용일자', key: 'LCAR_GP_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '할인/할증 적용일자', key: 'DC_EXTRA_DT_FORMAT', option: 'date', type: 'day' },
    { title: '할인단말기 적용일자', key: 'DC_OBU_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '시간대 할인 제도 적용일자', key: 'HR_DC_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '출퇴근할인 제도 적용일자', key: 'COMMU_DC_DATE_FORMAT', option: 'date', type: 'day' },
    // 미사용{ title: '연계요금제도 적용일자', key: 'LINK_FARE_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '출구차로입구기록여부', key: 'EXIT_LANE_ENT_DIV', option: 'select', selectItem: code_USE_DIV },
    { title: '연속통행할인 제도 적용일자', key: 'CONT_DC_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '차등요금 제도 적용일자', key: 'DIFF_TOLL_DATE_FORMAT', option: 'date', type: 'day' },
    { title: '잔액부족 수납여부', key: 'BALC_DOWN_DIV', option: 'select', selectItem: code_BALC_DOWN_DIV },
  ],
  lane: [
    { title: '상행차로수', key: 'UP_LANE_CNT', option: 'label' },
    { title: '하행차로수', key: 'DOWN_LANE_CNT', option: 'label' },
    { title: '가변차로수', key: 'VRAB_LANE_CNT', option: 'label' },
  ],
  prevContents: [],
  contents: [],
});

// 영업소 기초정보 - 조회
const searchBaseInfo = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getIcInformation', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      showMessage('영업소 정보가 없습니다.', 'error');
      return;
    }
    baseInfo.contents = data[0];
    baseInfo.contents.LCAR_GP_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.LCAR_GP_DATE);
    baseInfo.contents.DC_EXTRA_DT_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.DC_EXTRA_DT);
    baseInfo.contents.COMMU_DC_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.COMMU_DC_DATE);
    baseInfo.contents.HR_DC_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.HR_DC_DATE);
    baseInfo.contents.LINK_FARE_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.LINK_FARE_DATE);
    baseInfo.contents.ETAXI_EXMT_DT_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.ETAXI_EXMT_DT);
    baseInfo.contents.DC_OBU_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.DC_OBU_DATE);
    baseInfo.contents.CONT_DC_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.CONT_DC_DATE);
    baseInfo.contents.DIFF_TOLL_DATE_FORMAT = yyyyMMddToDateFormat(baseInfo.contents.DIFF_TOLL_DATE);
    baseInfo.prevContents = { ...baseInfo.contents };
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

const isValidBaseInfoOffice = ref(true);
const inputFormBaseInfoOffice = ref();

// 영업소 기초정보 - 변경 확인
const isChangedBaseInfo = () => {
  if (JSON.stringify(toRaw(baseInfo.contents)) !== JSON.stringify(baseInfo.prevContents)) {
    return true;
  }
  return false;
};

// 영업소 기초정보 - 저장
const saveBaseInfo = async () => {
  if (!isChangedBaseInfo()) {
    showMessage(`변경사항이 없습니다. 수정 후 다시 시도해주세요.`, 'warning', 5000);
    return;
  }
  if (!isValidBaseInfoOffice.value) {
    showMessage(`${inputFormBaseInfoOffice.value.errors[0].errorMessages[0]}`, 'warning');
    return;
  }
  try {
    onProcessing.default = true;
    baseInfo.contents.LCAR_GP_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.LCAR_GP_DATE_FORMAT);
    baseInfo.contents.DC_EXTRA_DT = datetimeLocalToyyyyMMdd(baseInfo.contents.DC_EXTRA_DT_FORMAT);
    baseInfo.contents.COMMU_DC_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.COMMU_DC_DATE_FORMAT);
    baseInfo.contents.HR_DC_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.HR_DC_DATE_FORMAT);
    baseInfo.contents.LINK_FARE_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.LINK_FARE_DATE_FORMAT);
    baseInfo.contents.ETAXI_EXMT_DT = datetimeLocalToyyyyMMdd(baseInfo.contents.ETAXI_EXMT_DT_FORMAT);
    baseInfo.contents.DC_OBU_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.DC_OBU_DATE_FORMAT);
    baseInfo.contents.CONT_DC_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.CONT_DC_DATE_FORMAT);
    baseInfo.contents.DIFF_TOLL_DATE = datetimeLocalToyyyyMMdd(baseInfo.contents.DIFF_TOLL_DATE_FORMAT);
    const data = await request('post', 'api/base/setIcInformation', {
      ...baseInfo.contents,
    });
    if (data.ERROR_CODE >= 1) {
      showMessage('저장되었습니다.', 'success');
    } else {
      showMessage(data.ERROR_MSG, 'error');
    }
  } catch (error) {
    showMessage('저장 중 오류가 발생했습니다.', 'error');
    console.error('수정 중 오류 발생:', error);
  } finally {
    onProcessing.default = false;
    searchBaseInfo();
  }
};

/* 차로 정보 */
const laneInfo = reactive({
  main: {
    headers: [
      { title: '영업소ID', key: 'IC_CODE', align: 'center', width: '70' },
      { title: '영업소명', key: 'IC_NM', align: 'center', width: '80' },
      { title: '영업소구분', key: 'IC_DIV_NM', align: 'center', width: '140' },
      { title: '차로번호', key: 'LOGC_LANE', align: 'center', width: '80' },
      { title: '차로명', key: 'LANE_NM', align: 'center', width: '80' },
      { title: '차로형태', key: 'LANE_TYPE_NM', align: 'center', width: '90' },
      { title: '차로구분', key: 'LANE_INFO_DIV_NM', align: 'center', width: '80' },
      { title: '상하행구분', key: 'UP_DOWN_DIV_NM', align: 'center', width: '90' },
      { title: '방향구분', key: 'DIR_DIV_NM', align: 'center', width: '90' },
      { title: '본선 진입진출구분', key: 'ENT_EXIT_TYPE_NM', align: 'center', width: '120' },
      { title: '제조사', key: 'COMM_MTD_NM', align: 'center', width: '100' },
      { title: 'IP 주소', key: 'IP_ADDR', align: 'center', width: '100' },
      { title: '포트', key: 'PORT', align: 'center', width: '60' },
      { title: '설치장소', key: 'LANE_SET_NM', align: 'center', width: '80' },
      { title: '비고', key: 'NOTE', align: 'center', width: '160', customBodyCellStyle: 'table-body-style-left' },
      { title: 'PSAM번호', key: 'PSAM_NO', align: 'center', width: '160' },
      { title: 'LSAM번호', key: 'LSAM_NO', align: 'center', width: '160' },
    ],
    contents: [],
  },
  dialog: {
    isShow: false,
    headers: [
      { title: '차로번호', key: 'LOGC_LANE', align: 'center', disabled: true },
      { title: '설치장소', key: 'LANE_SET', align: 'center', option: 'select', selectItem: code_LANE_SET },
      { title: '차로명', key: 'LANE_NM', align: 'center' },
      { title: '제조사', key: 'COMM_MTD', align: 'center', option: 'select', selectItem: code_COMM_MTD, width: '80' },
      { title: '차로형태', key: 'LANE_TYPE', align: 'center', option: 'select', selectItem: code_LANE_TYPE },
      { title: 'IP 주소', key: 'IP_ADDR', align: 'center' },
      { title: '차로구분', key: 'LANE_INFO_DIV', align: 'center', option: 'select', selectItem: code_LANE_INFO_DIV },
      { title: '포트', key: 'PORT', align: 'center' },
      { title: '상하행구분', key: 'UP_DOWN_DIV', align: 'center', option: 'select', selectItem: code_UP_DOWN_DIV },
      { title: 'PSAM번호', key: 'PSAM_NO', align: 'center' },
      { title: '방향구분', key: 'DIR_DIV', align: 'center', option: 'select', selectItem: code_DIR_DIV },
      { title: 'LSAM번호', key: 'LSAM_NO', align: 'center' },
      { title: '본선 진입진출구분', key: 'ENT_EXIT_TYPE', align: 'center', option: 'select', selectItem: code_ENT_EXIT_TYPE },
      { title: '비고', key: 'NOTE', align: 'center', customBodyCellStyle: 'table-body-style-left' },
    ],
    contents: {},
  },
});

// 차로정보 - 조회
const searchLaneInfo = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getLaneList', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    console.log(data);
    laneInfo.main.contents = data;
    if (data.length == 0) {
      showMessage('차로정보가 없습니다.', 'error');
    }
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

// 차로정보 - Dialog 활성화
const onDoubleClickLane = async (index, item) => {
  laneInfo.dialog.contents = { ...item };
  laneInfo.dialog.isShow = true;
};

// 차로정보 - 저장
const saveLaneInfo = async () => {
  onProcessing.laneInfo = true;
  try {
    const data = await request('post', 'api/base/setLane', {
      ...laneInfo.dialog.contents,
    });

    if (data.ERROR_CODE >= 1) {
      showMessage('저장되었습니다.', 'success');
      laneInfo.dialog.isShow = false;
      searchLaneInfo();
    } else {
      showMessage(data.ERROR_MSG, 'error');
    }
  } catch (error) {
    showMessage('차로정보 저장 중 오류가 발생했습니다.', 'error');
  } finally {
    onProcessing.laneInfo = false;
  }
};

/* 기준요금 */
const standardFareInfo = reactive({
  base: [
    { title: '영업소ID', key: 'IC_CODE', option: 'label' },
    { title: '영업소명', key: 'IC_NM', option: 'label' },
    { title: '사용여부', key: 'USE_DIV_NM', option: 'label' },
    { title: '수정일시', key: 'MDFY_DT_FORMAT', option: 'label' },
  ],
  new: [
    { title: '시작', key: 'NEW_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
    { title: '종료', key: 'NEW_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
    { title: '1종', key: 'NEW_TOT_FARE1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '2종', key: 'NEW_TOT_FARE2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '3종', key: 'NEW_TOT_FARE3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '4종', key: 'NEW_TOT_FARE4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '5종', key: 'NEW_TOT_FARE5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '6종', key: 'NEW_TOT_FARE6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
  ],
  old: [
    { title: '시작', key: 'OLD_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
    { title: '종료', key: 'OLD_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
    { title: '1종', key: 'OLD_TOT_FARE1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '2종', key: 'OLD_TOT_FARE2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '3종', key: 'OLD_TOT_FARE3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '4종', key: 'OLD_TOT_FARE4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '5종', key: 'OLD_TOT_FARE5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '6종', key: 'OLD_TOT_FARE6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
  ],
  prevContents: [],
  contents: [],
});

// 기준요금 - 조회
const searchStandardFareInfo = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getFareInfo', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      showMessage('기준요금 정보가 없습니다.', 'error');
      return;
    }
    standardFareInfo.contents = data[0];
    standardFareInfo.prevContents = { ...standardFareInfo.contents };
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

// 기준요금 - 변경 확인
const isChangedStandardFareInfo = () => {
  if (JSON.stringify(toRaw(standardFareInfo.contents)) !== JSON.stringify(standardFareInfo.prevContents)) {
    return true;
  }
  return false;
};

// 기준요금 - 저장
const saveStandardFareInfo = async () => {
  if (!isChangedStandardFareInfo()) {
    showMessage(`변경사항이 없습니다. 수정 후 다시 시도해주세요.`, 'warning', 5000);
    return;
  }
  try {
    onProcessing.default = true;
    if (standardFareInfo.contents.OLD_START_TM_FORMAT === undefined) {
      standardFareInfo.contents.OLD_START_TM_FORMAT = '';
    }
    if (standardFareInfo.contents.OLD_END_TM_FORMAT === undefined) {
      standardFareInfo.contents.OLD_END_TM_FORMAT = '';
    }
    const data = await request('post', 'api/base/setFareInfo', {
      ...standardFareInfo.contents,
      ...{
        MDFY_ADMIN_ID: authStore.getWorkerNo,
      },
    });
    if (data.ERROR_CODE >= 1) {
      showMessage('저장되었습니다.', 'success');
    } else {
      showMessage(data.ERROR_MSG, 'error');
    }
  } catch (error) {
    showMessage('저장 중 오류가 발생했습니다.', 'error');
    console.error('수정 중 오류 발생:', error);
  } finally {
    onProcessing.default = false;
    searchStandardFareInfo();
  }
};

// 기준요금 - 신→구 이동
const moveNewToOldStandardFareInfo = () => {
  standardFareInfo.contents.OLD_TOT_FARE1 = standardFareInfo.contents.NEW_TOT_FARE1;
  standardFareInfo.contents.OLD_TOT_FARE2 = standardFareInfo.contents.NEW_TOT_FARE2;
  standardFareInfo.contents.OLD_TOT_FARE3 = standardFareInfo.contents.NEW_TOT_FARE3;
  standardFareInfo.contents.OLD_TOT_FARE4 = standardFareInfo.contents.NEW_TOT_FARE4;
  standardFareInfo.contents.OLD_TOT_FARE5 = standardFareInfo.contents.NEW_TOT_FARE5;
  standardFareInfo.contents.OLD_TOT_FARE6 = standardFareInfo.contents.NEW_TOT_FARE6;
  // 신요금 0원 적용
  standardFareInfo.contents.NEW_TOT_FARE1 = 0;
  standardFareInfo.contents.NEW_TOT_FARE2 = 0;
  standardFareInfo.contents.NEW_TOT_FARE3 = 0;
  standardFareInfo.contents.NEW_TOT_FARE4 = 0;
  standardFareInfo.contents.NEW_TOT_FARE5 = 0;
  standardFareInfo.contents.NEW_TOT_FARE6 = 0;
};

/* 차등요금 */
const diffFareInfo = reactive({
  base: [
    { title: '영업소ID', key: 'IC_CODE', option: 'label', align: 'center' },
    { title: '영업소명', key: 'IC_NM', option: 'label', align: 'center' },
  ],
  state: [
    { title: '제도적용일자', key: 'DIFF_START_DT_FORMAT', align: 'center', option: 'date', type: 'datetime', disabled: true },
    { title: '제도수정일자', key: 'DIFF_MDFY_DT_FORMAT', align: 'center', option: 'date', type: 'datetime', disabled: true },
    { title: '제도폐쇄일자', key: 'DIFF_END_DT_FORMAT', align: 'center', option: 'date', type: 'datetime' },
  ],
  new: [
    { title: '1종', key: 'NEW_TOT_TOLL1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '2종', key: 'NEW_TOT_TOLL2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '3종', key: 'NEW_TOT_TOLL3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '4종', key: 'NEW_TOT_TOLL4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '5종', key: 'NEW_TOT_TOLL5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '6종', key: 'NEW_TOT_TOLL6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '7종', key: 'NEW_TOT_TOLL7', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
    { title: '8종', key: 'NEW_TOT_TOLL8', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999 },
  ],
  old: [
    { title: '1종', key: 'OLD_TOT_TOLL1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '2종', key: 'OLD_TOT_TOLL2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '3종', key: 'OLD_TOT_TOLL3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '4종', key: 'OLD_TOT_TOLL4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '5종', key: 'OLD_TOT_TOLL5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '6종', key: 'OLD_TOT_TOLL6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '7종', key: 'OLD_TOT_TOLL7', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
    { title: '8종', key: 'OLD_TOT_TOLL8', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999, readonly: true },
  ],
  prevContents: [],
  contents: [],
});

// 차등요금 - 조회
const searchDiffFareInfo = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getDiffFare', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      showMessage('차등요금 정보가 없습니다.', 'error');
      return;
    }
    diffFareInfo.contents = {
      ...data[0],
      DIFF_START_DT_FORMAT: dayjs(data[0].DIFF_START_DT, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm'),
      DIFF_MDFY_DT_FORMAT: dayjs(data[0].DIFF_MDFY_DT, 'YYYYMMDDHHmm').format('YYYY-MM-DDTHH:mm'),
      DIFF_END_DT_FORMAT: dayjs(data[0].DIFF_END_DT, 'YYYYMMDDHHmm').format('YYYY-MM-DDTHH:mm'),
    };
    diffFareInfo.prevContents = { ...diffFareInfo.contents };
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

// 차등요금 - 변경 확인
const isChangedDiffFareInfo = () => {
  if (JSON.stringify(toRaw(diffFareInfo.contents)) !== JSON.stringify(diffFareInfo.prevContents)) {
    return true;
  }
  return false;
};

// 차등요금 - 저장
const saveDiffFareInfo = async () => {
  if (!isChangedDiffFareInfo()) {
    showMessage(`변경사항이 없습니다. 수정 후 다시 시도해주세요.`, 'warning', 5000);
    return;
  }
  try {
    onProcessing.default = true;
    const data = await request('post', 'api/base/setDiffFare', {
      ...diffFareInfo.contents,
      ...{
        MDFY_ADMIN_ID: authStore.getWorkerNo,
        DIFF_END_DT: diffFareInfo.contents.DIFF_END_DT_FORMAT.replace(/[^0-9]/g, ''),
      },
    });
    if (data.ERROR_CODE >= 1) {
      showMessage('저장되었습니다.', 'success');
    } else {
      showMessage(data.ERROR_MSG, 'error');
    }
  } catch (error) {
    showMessage('저장 중 오류가 발생했습니다.', 'error');
    console.error('수정 중 오류 발생:', error);
  } finally {
    onProcessing.default = false;
    searchDiffFareInfo();
  }
};

// 차등요금 - 신→구 이동
const moveNewToOldDiffFareInfo = () => {
  diffFareInfo.contents.OLD_TOT_TOLL1 = diffFareInfo.contents.NEW_TOT_TOLL1;
  diffFareInfo.contents.OLD_TOT_TOLL2 = diffFareInfo.contents.NEW_TOT_TOLL2;
  diffFareInfo.contents.OLD_TOT_TOLL3 = diffFareInfo.contents.NEW_TOT_TOLL3;
  diffFareInfo.contents.OLD_TOT_TOLL4 = diffFareInfo.contents.NEW_TOT_TOLL4;
  diffFareInfo.contents.OLD_TOT_TOLL5 = diffFareInfo.contents.NEW_TOT_TOLL5;
  diffFareInfo.contents.OLD_TOT_TOLL6 = diffFareInfo.contents.NEW_TOT_TOLL6;
  diffFareInfo.contents.OLD_TOT_TOLL7 = diffFareInfo.contents.NEW_TOT_TOLL7;
  diffFareInfo.contents.OLD_TOT_TOLL8 = diffFareInfo.contents.NEW_TOT_TOLL8;
  // 신요금 0원 적용
  diffFareInfo.contents.NEW_TOT_TOLL1 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL2 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL3 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL4 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL5 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL6 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL7 = 0;
  diffFareInfo.contents.NEW_TOT_TOLL8 = 0;
};

/* 차로개정현황 */
const revisionState = reactive({
  ETCS: {
    headers: [
      {
        title: '구분',
        align: 'center',
        children: [
          { title: '영업소', key: 'IC_NM', align: 'center', width: '70' },
          { title: '차로번호', key: 'LANE_NO', align: 'center', width: '70' },
          { title: '차로구분', key: 'LANE_TYPE_NM', align: 'center', width: '90' },
        ],
      },
      {
        title: '기초',
        align: 'center',
        children: [
          { title: '개정번호', key: 'BASE_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'BASE_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '통행요금',
        align: 'center',
        children: [
          { title: '개정번호', key: 'FARE_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'FARE_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '출퇴근할인',
        align: 'center',
        children: [
          { title: '개정번호', key: 'CMT_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'CMT_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '무효전자카드',
        align: 'center',
        children: [
          { title: '개정번호', key: 'BL_ECARD_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'BL_ECARD_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '무효 OBU',
        align: 'center',
        children: [
          { title: '개정번호', key: 'BL_OBU_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'BL_OBU_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '명절(특정일) 면제',
        align: 'center',
        children: [
          { title: '개정번호', key: 'HLDAY_EXMT_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'HLDAY_EXMT_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '시간별할인',
        align: 'center',
        children: [
          { title: '개정번호', key: 'HR_DC_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'HR_DC_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '연속통행할인',
        align: 'center',
        children: [
          { title: '개정번호', key: 'CONT_DC_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'CONT_DC_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '전자카드면제',
        align: 'center',
        children: [
          { title: '개정번호', key: 'PL_ECARD_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'PL_ECARD_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '차등요금제',
        align: 'center',
        children: [
          { title: '개정번호', key: 'DIFF_TOLL_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'DIFF_TOLL_STAT_NM', align: 'center', width: '100' },
        ],
      },
      // {
      //   title: '직원면제',
      //   align: 'center',
      //   children: [
      //     { title: '개정번호', key: 'EMP_EXMT_REV_NO', align: 'center', width: '70' },
      //     { title: '개정상태', key: 'EMP_EXMT_STAT_NM', align: 'center', width: '100' },
      //   ],
      // },
    ],
    contents: [],
  },
  TCS: {
    headers: [
      {
        title: '구분',
        align: 'center',
        children: [
          { title: '영업소', key: 'IC_NM', align: 'center', width: '70' },
          { title: '차로번호', key: 'LANE_NO', align: 'center', width: '70' },
          { title: '차로구분', key: 'LANE_TYPE_NM', align: 'center', width: '90' },
        ],
      },
      {
        title: '기초',
        align: 'center',
        children: [
          { title: '개정번호', key: 'BASE_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'BASE_STAT_NM', align: 'center', width: '100' },
        ],
      },
      {
        title: '근무자',
        align: 'center',
        children: [
          { title: '개정번호', key: 'WORKER_CARD_REV_NO', align: 'center', width: '70' },
          { title: '개정상태', key: 'WORKER_CARD_STAT_NM', align: 'center', width: '100' },
        ],
      },
    ],
    contents: [],
  },
});

// 개정현황 - 조회
const searchRevisionState = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getLaneRevisionStatus', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      showMessage('데이터가 없습니다.');
    }
    revisionState.ETCS.contents = data.filter((item) => item.LANE_TYPE === '2');
    revisionState.TCS.contents = data.filter((item) => item.LANE_TYPE === '1');
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

/* 개정정보 */
const revisionInfo = reactive({
  headers: [
    {
      code: '01',
      name: '영업소 기초정보',
      keyApplyDt: 'BASE_APPLY_DT',
      keyRevNo: 'BASE_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionBase',
        data: '10000000000000000000',
      },
    },
    {
      code: '02',
      name: '근무자',
      keyApplyDt: 'WORKER_APPLY_DT',
      keyRevNo: 'WORKER_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionWorker',
        data: '01000000000000000000',
      },
    },
    {
      code: '03',
      name: '통행요금',
      keyApplyDt: 'FARE_APPLY_DT',
      keyRevNo: 'FARE_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionFare',
        data: '00100000000000000000',
      },
    },
    {
      code: '04',
      name: '출퇴근할인',
      keyApplyDt: 'CMT_APPLY_DT',
      keyRevNo: 'CMT_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionCommute',
        data: '00010000000000000000',
      },
    },
    {
      code: '05',
      name: '전자카드 B/L',
      keyApplyDt: 'BL_ECARD_APPLY_DT',
      keyRevNo: 'BL_ECARD_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionBlEcard',
        data: '00001000000000000000',
      },
    },
    {
      code: '06',
      name: 'OBU B/L',
      keyApplyDt: 'BL_OBU_APPLY_DT',
      keyRevNo: 'BL_OBU_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionBlObu',
        data: '0000010000000000000',
      },
    },
    {
      code: '07',
      name: '명절(특정일)',
      keyApplyDt: 'HLDAYEXMT_APPLY_DT',
      keyRevNo: 'HLDAYEXMT_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionHolidayException',
        data: '00000010000000000000',
      },
    },
    {
      code: '08',
      name: '시간별할인',
      keyApplyDt: 'HR_DC_APPLY_DT',
      keyRevNo: 'HR_DC_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionHourDiscount',
        data: '00000001000000000000',
      },
    },
    {
      code: '09',
      name: '연속통행할인',
      keyApplyDt: 'CONT_DC_APPLY_DT',
      keyRevNo: 'CONT_DC_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionContinuousDiscount',
        data: '00000000100000000000',
      },
    },
    {
      code: '11',
      name: '전자카드 면제 P/L',
      keyApplyDt: 'PL_ECARD_APPLY_DT',
      keyRevNo: 'PL_ECARD_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionPlEcard',
        data: '00000000001000000000',
      },
    },
    {
      code: '12',
      name: '차등요금제',
      keyApplyDt: 'DIFF_TOLL_APPLY_DT',
      keyRevNo: 'DIFF_TOLL_REV_NO',
      checked: false,
      readonly: true,
      request: {
        url: 'api/base/setIcRevisionDiffFare',
        data: '00000000000100000000',
      },
    },
  ],
  contents: [],
  prevContents: [],
});

// 개정정보 - 조회
const searchRevisionInfo = async () => {
  clearSelectedRevisionInfo();
  try {
    isLoading.value = true;
    const data = await request('post', 'api/base/getIcRevision', {
      ...{
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      showMessage('개정정보가 없습니다.', 'error');
      return;
    }
    revisionInfo.contents = data[0];
    // YYYY-MM-DDThh:mm
    // 적용일시
    revisionInfo.contents.BASE_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.BASE_APPLY_DT);
    revisionInfo.contents.WORKER_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.WORKER_APPLY_DT);
    revisionInfo.contents.FARE_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.FARE_APPLY_DT);
    revisionInfo.contents.CMT_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.CMT_APPLY_DT);
    revisionInfo.contents.BL_ECARD_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.BL_ECARD_APPLY_DT);
    revisionInfo.contents.BL_OBU_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.BL_OBU_APPLY_DT);
    revisionInfo.contents.HLDAYEXMT_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.HLDAYEXMT_APPLY_DT);
    revisionInfo.contents.HR_DC_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.HR_DC_APPLY_DT);
    revisionInfo.contents.CONT_DC_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.CONT_DC_APPLY_DT);
    revisionInfo.contents.PL_ECARD_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.PL_ECARD_APPLY_DT);
    revisionInfo.contents.DIFF_TOLL_APPLY_DT_INPUT = yyyyMMddHHmmssToDatetimeLocal(revisionInfo.contents.DIFF_TOLL_APPLY_DT);
    revisionInfo.prevContents = { ...revisionInfo.contents };
  } catch (error) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
    console.error('데이터 조회 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
};

// 개정정보 - 개정번호 업데이트
const onUpdateRevNo = (val) => {
  if (val > 99) return 1;
  else return val;
};

// 개정정보 - 개정번호 자동입력
const onClickAutoIncrement = (index) => {
  revisionInfo.contents[revisionInfo.headers[index].keyRevNo] = onUpdateRevNo(revisionInfo.contents[revisionInfo.headers[index].keyRevNo] + 1);
  revisionInfo.headers[index].readonly = true;
};

// 개정정보 - 개정 항목 선택 해제
const clearSelectedRevisionInfo = () => {
  revisionInfo.headers.forEach((item) => {
    item.checked = false;
  });
};

// 개정정보 - 변경 확인
const isChangedRevisionInfo = () => {
  if (JSON.stringify(toRaw(revisionInfo.contents)) !== JSON.stringify(revisionInfo.prevContents)) {
    return true;
  }
  return false;
};

// 개정정보 - 개정지시
const requestRevision = async () => {
  if (!isChangedRevisionInfo()) {
    showMessage(`개정번호가 변경되지 않았습니다. 수정 후 다시 시도해주세요.`, 'warning', 5000);
    return;
  }
  try {
    onProcessing.revision = true;
    let target = revisionInfo.headers.filter((item) => item.checked === true);
    if (target.length === 0) {
      showMessage('선택한 개정 항목이 없습니다.', 'warning');
      return;
    }
    if (!confirm('개정을 지시하겠습니까?')) {
      return;
    }
    const send_dt = getCurrentDatetime();
    revisionInfo.contents.BASE_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.BASE_APPLY_DT_INPUT);
    revisionInfo.contents.WORKER_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.WORKER_APPLY_DT_INPUT);
    revisionInfo.contents.FARE_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.FARE_APPLY_DT_INPUT);
    revisionInfo.contents.CMT_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.CMT_APPLY_DT_INPUT);
    revisionInfo.contents.BL_ECARD_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.BL_ECARD_APPLY_DT_INPUT);
    revisionInfo.contents.BL_OBU_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.BL_OBU_APPLY_DT_INPUT);
    revisionInfo.contents.HLDAYEXMT_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.HLDAYEXMT_APPLY_DT_INPUT);
    revisionInfo.contents.HR_DC_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.HR_DC_APPLY_DT_INPUT);
    revisionInfo.contents.CONT_DC_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.CONT_DC_APPLY_DT_INPUT);
    revisionInfo.contents.PL_ECARD_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.PL_ECARD_APPLY_DT_INPUT);
    revisionInfo.contents.DIFF_TOLL_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(revisionInfo.contents.DIFF_TOLL_APPLY_DT_INPUT);
    for (let i = 0; i < target.length; i++) {
      try {
        let resSave = await request('post', target[i].request.url, {
          ...{
            IC_CODE: authStore.getIcCode,
            REV_NO_CODE: target[i].code,
            ADMIN_ID: authStore.getWorkerNo,
            APPLY_DT: revisionInfo.contents[target[i].keyApplyDt],
            REV_NO: revisionInfo.contents[target[i].keyRevNo],
            REV_DIV: '0',
          },
        });
        if (resSave === undefined) {
          showMessage(`[${target[i].code}] ${target[i].name} 개정지시 중 오류가 발생했습니다.`, 'error', 5000);
          return;
        }
        let resRevision = await request('post', 'api/base/requestRevision', {
          ...{
            IC_CODE: authStore.getIcCode,
            PACKET_SYSTEM_CODE_SEND: 'OPR094',
            PACKET_SYSTEM_CODE_RECV: 'MCOL00',
            PACKET_OP_DIV: '1000',
            PACKET_WORK_DIV: '0010',
            PACKET_SEND_DT: send_dt,
            PACKET_REPLY_CODE: '0000',
            PACKET_REVISION_DATA: target[i].request.data,
          },
        });
        if (resRevision === undefined) {
          showMessage(`[${target[i].code}] ${target[i].name} 개정지시 중 오류가 발생했습니다.`, 'error', 5000);
          return;
        }
      } catch (e) {
        showMessage(`[${target[i].code}] ${target[i].name} 개정지시 중 오류가 발생했습니다.`, 'error', 5000);
        console.log(e);
        return;
      }
    }
    showMessage('개정지시가 완료되었습니다.', 'success');
    clearSelectedRevisionInfo();
  } catch (error) {
    showMessage('개정지시 중 오류가 발생했습니다.', 'error');
    console.error('개정지시 중 오류 발생:', error);
  } finally {
    onProcessing.revision = false;
  }
};

const handleSearch = async () => {
  if (tab.value === 0) {
    await searchBaseInfo();
  } else if (tab.value === 1) {
    await searchLaneInfo();
  } else if (tab.value === 2) {
    await searchStandardFareInfo();
  } else if (tab.value === 3) {
    await searchDiffFareInfo();
  } else if (tab.value === 4) {
    await searchRevisionState();
  } else if (tab.value === 5) {
    await searchRevisionInfo();
  }
};

const handleSave = () => {
  if (tab.value === 0) {
    saveBaseInfo();
  } else if (tab.value === 2) {
    saveStandardFareInfo();
  } else if (tab.value === 3) {
    saveDiffFareInfo();
  }
};

btnHandler({
  Search: handleSearch,
  Save: handleSave,
});
</script>

<style scoped>
h3 {
  margin-top: 20px;
}
.v-btn {
  font-weight: bold;
}
.table-header-style {
  background-color: #f5f5f5;
  border: 1px solid #b0b0b0;
  border-bottom: none;
  font-size: 13px;
  width: 100%;
}
.table-header-style th {
  text-align: start !important;
  padding: 0 5px;
  height: 36px;
}
.table-header-style th:last-child {
  text-align: end !important;
  padding: 0 8px;
}
#revision_table {
  width: 100%;
  font-size: 13px;
  border-collapse: collapse;
  text-align: center;
}
#revision_table tr:hover {
  background-color: #0086e510;
}
#revision_table th {
  border-top: 1px solid #b0b0b0;
  border-bottom: 1px solid #b0b0b0;
  background-color: #f5f5f5;
  height: 40px;
  padding: 0px 10px;
  color: #666666;
  width: auto;
}
#revision_table th:last-child {
  text-align: end;
  width: 100%;
}
#revision_table td {
  border-bottom: 1px solid #b0b0b0;
  text-align: center;
  white-space: nowrap;
  padding: 4px 40px;
  height: 46px;
}
#revision_table td:last-child {
  width: 100%;
}
#revision_table .v-number-input {
  min-width: 100px;
}
#revision_table :deep(.v-text-field .v-field__input) {
  height: 100%;
  font-size: 13px;
  text-align: end;
  padding: 0 17px;
}
#revision_table .v-text-field :deep(input:read-only) {
  background-color: #efefef;
  opacity: 0.6 !important;
}
#revision_table input[type='text'],
#revision_table input[type='datetime-local'] {
  height: 100%;
  border-radius: 7px;
  border: #a9a9a9 1px solid;
  text-align: center;
  padding: 0 3px;
}
#revision_table input[type='datetime-local']:hover {
  border: #000000 1px solid;
}
#revision_table input[type='datetime-local']::-webkit-calendar-picker-indicator {
  width: 18px;
  height: 18px;
  opacity: 0.5;
}
#revision_table input[type='datetime-local']::-webkit-calendar-picker-indicator:hover {
  opacity: 1;
}
#revision_table input[type='checkbox'] {
  width: 18px;
  height: 18px;
  border: #a9a9a9 1px solid;
  vertical-align: middle;
}
#revision_table button {
  border-radius: 4px;
  margin: 0 7px;
}
#revision_table input[disabled] {
  background-color: #eeeeee;
}
.input-form-style:deep(th) {
  text-align: center;
}
</style>
