<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <LoadingComponent v-if="isLoading" />
  <v-tabs v-model="tab" :color="colorStore.basicColor">
    <v-tab>카드별 할인</v-tab>
    <v-tab>시간별 할인</v-tab>
    <v-tab>할인/할증</v-tab>
    <v-tab>단말기할인</v-tab>
    <v-tab>연속통행할인</v-tab>
    <!--<v-tab class="font-bold-ac" style="font-size: 13px">전자카드 충전할증</v-tab> -->
    <!--<v-tab class="font-bold-ac" style="font-size: 13px">전자카드 최대금액</v-tab> -->
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="영업소 기초정보 개정 시에 적용됩니다." />
      <InformationComponent style="margin-top: 7px" message="미사용 기능 입니다." icon-type="attention" />
      <v-row>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 420px; margin-right: 20px; margin-top: 10px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>전자카드 차종별 할인율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="ecardDiscountCarTypeContent"
                  :headers="ecardDiscountHeaders.carType.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <v-row>
                  <v-col>
                    <div style="width: 99%" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>신요금</th>
                          <th>
                            <v-btn variant="flat" size="small" color="blue" text="신→구" @click="ecardDiscountCarTypeMoveNewToOld" />
                          </th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountCarTypeContent"
                        :headers="ecardDiscountHeaders.carType.new"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                  <v-col>
                    <div style="width: 99%; margin-left: auto" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>구요금</th>
                          <th></th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountCarTypeContent"
                        :headers="ecardDiscountHeaders.carType.old"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                </v-row>
              </div>
            </div>
          </v-card>
        </div>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 420px; margin-right: 20px; margin-top: 10px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>선불할인 전자카드 할인율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="ecardDiscountContent"
                  :headers="ecardDiscountHeaders.prepaid.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <v-row>
                  <v-col>
                    <div style="width: 99%" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>신요금</th>
                          <th>
                            <v-btn variant="flat" size="small" color="blue" text="신→구" @click="ecardDiscountPrepaidMoveNewToOld" />
                          </th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountContent"
                        :headers="ecardDiscountHeaders.prepaid.new"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                  <v-col>
                    <div style="width: 99%; margin-left: auto" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>구요금</th>
                          <th></th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountContent"
                        :headers="ecardDiscountHeaders.prepaid.old"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                </v-row>
              </div>
            </div>
          </v-card>
        </div>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 420px; margin-right: 20px; margin-top: 10px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>후불할인 전자카드 할인율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="ecardDiscountContent"
                  :headers="ecardDiscountHeaders.postpaid.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <v-row>
                  <v-col>
                    <div style="width: 99%" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>신요금</th>
                          <th>
                            <v-btn variant="flat" size="small" color="blue" text="신→구" @click="ecardDiscountPostpaidMoveNewToOld" />
                          </th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountContent"
                        :headers="ecardDiscountHeaders.postpaid.new"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                  <v-col>
                    <div style="width: 99%; margin-left: auto" class="tab-window-contents-container">
                      <table class="table-header-style">
                        <tr>
                          <th>구요금</th>
                          <th></th>
                        </tr>
                      </table>
                      <InputFormGrid
                        class="input-form-style"
                        v-model="ecardDiscountContent"
                        :headers="ecardDiscountHeaders.postpaid.old"
                        :cols-per-row="1"
                        table-header-width="30%"
                      />
                    </div>
                  </v-col>
                </v-row>
              </div>
            </div>
          </v-card>
        </div>
      </v-row>
    </v-window-item>
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="영업소 기초정보 개정 시에 적용됩니다." />
      <InformationComponent style="margin: 7px 0" message="신정보 적용일시는 시간별 할인 개정 시에 설정합니다." />
      <v-row>
        <v-col>
          <div>
            <div class="tab-window-contents-container">
              <h3>평일 시간대 할인</h3>
            </div>
            <v-card elevation="4" style="width: 600px; margin: 0 20px 5px 0">
              <v-tabs style="height: 36px !important" v-model="hourDiscountWeekdaysTab" :color="colorStore.basicColor">
                <v-tab
                  style="height: 36px !important; font-size: 16px"
                  v-for="(item, index) in hourDiscountWeekdaysContents"
                  :key="index"
                  :value="'discount-weekdays-form-' + index"
                >
                  {{ item.HR_SEQ_NM }}
                </v-tab>
              </v-tabs>
              <v-tabs-window class="tab-window-container" v-model="hourDiscountWeekdaysTab">
                <v-tabs-window-item v-for="(item, index) in hourDiscountWeekdaysContents" :key="index" :value="'discount-weekdays-form-' + index">
                  <div class="tab-window-contents-buttons">
                    <!-- <v-btn style="margin-left: 10px" :disabled="true" variant="outlined" color="blue" text="취소" @click="showMessage(item.HR_SEQ_NM)" /> -->
                    <v-btn style="margin-left: 10px" color="blue" text="저장" @click="onClickSaveHourDiscount(item)" />
                  </div>
                  <div class="tab-window-contents-container">
                    <InputFormGrid
                      class="input-form-style"
                      v-model="hourDiscountWeekdaysContents[index]"
                      :headers="hourDiscountHeaders.base"
                      :cols-per-row="2"
                      table-header-width="15%"
                    />
                  </div>
                  <v-row>
                    <v-col>
                      <div style="width: 99%" class="tab-window-contents-container">
                        <table class="table-header-style">
                          <tr>
                            <th>신요금</th>
                            <th>
                              <v-btn variant="flat" size="small" color="blue" text="신→구" @click="hourDiscountWeekdaysMoveNewToOld(index)" />
                            </th>
                          </tr>
                        </table>
                        <InputFormGrid
                          class="input-form-style"
                          v-model="hourDiscountWeekdaysContents[index]"
                          :headers="hourDiscountHeaders.weekdays.new"
                          :cols-per-row="1"
                          table-header-width="30%"
                        />
                      </div>
                    </v-col>
                    <v-col>
                      <div style="width: 99%; margin-left: auto" class="tab-window-contents-container">
                        <table class="table-header-style">
                          <tr>
                            <th>구요금</th>
                            <th></th>
                          </tr>
                        </table>
                        <InputFormGrid
                          class="input-form-style"
                          v-model="hourDiscountWeekdaysContents[index]"
                          :headers="hourDiscountHeaders.weekdays.old"
                          :cols-per-row="1"
                          table-header-width="30%"
                        />
                      </div>
                    </v-col>
                  </v-row>
                </v-tabs-window-item>
              </v-tabs-window>
            </v-card>
          </div>
        </v-col>
        <v-col>
          <div style="width: 95%">
            <div class="tab-window-contents-container">
              <h3>주말 시간대 할인</h3>
            </div>
            <v-card elevation="4" style="width: 600px; margin: 0 20px 5px 0">
              <v-tabs style="height: 36px !important" v-model="hourDiscountWeekendTab" :color="colorStore.basicColor">
                <v-tab
                  style="height: 36px !important; font-size: 16px"
                  v-for="(item, index) in hourDiscountWeekendContents"
                  :key="index"
                  :value="'discount-weekend-form-' + index"
                >
                  {{ item.HR_SEQ_NM }}
                </v-tab>
              </v-tabs>
              <v-tabs-window class="tab-window-container" v-model="hourDiscountWeekendTab">
                <v-tabs-window-item v-for="(item, index) in hourDiscountWeekendContents" :key="index" :value="'discount-weekend-form-' + index">
                  <div class="tab-window-contents-buttons">
                    <!-- <v-btn style="margin-left: 10px" :disabled="true" variant="outlined" color="blue" text="취소" @click="showMessage(item.HR_SEQ_NM)" /> -->
                    <v-btn style="margin-left: 10px" color="blue" text="저장" @click="onClickSaveHourDiscount(item)" />
                  </div>
                  <div class="tab-window-contents-container">
                    <InputFormGrid
                      class="input-form-style"
                      v-model="hourDiscountWeekendContents[index]"
                      :headers="hourDiscountHeaders.base"
                      :cols-per-row="2"
                      table-header-width="15%"
                    />
                  </div>
                  <v-row>
                    <v-col>
                      <div style="width: 99%" class="tab-window-contents-container">
                        <table class="table-header-style">
                          <tr>
                            <th>신요금</th>
                            <th>
                              <v-btn variant="flat" size="small" color="blue" text="신→구" @click="hourDiscountWeekendMoveNewToOld(index)" />
                            </th>
                          </tr>
                        </table>
                        <InputFormGrid
                          class="input-form-style"
                          v-model="hourDiscountWeekendContents[index]"
                          :headers="hourDiscountHeaders.weekend.new"
                          :cols-per-row="1"
                          table-header-width="30%"
                        />
                      </div>
                    </v-col>
                    <v-col>
                      <div style="width: 99%; margin-left: auto" class="tab-window-contents-container">
                        <table class="table-header-style">
                          <tr>
                            <th>구요금</th>
                            <th></th>
                          </tr>
                        </table>
                        <InputFormGrid
                          class="input-form-style"
                          v-model="hourDiscountWeekendContents[index]"
                          :headers="hourDiscountHeaders.weekend.old"
                          :cols-per-row="1"
                          table-header-width="30%"
                        />
                      </div>
                    </v-col>
                  </v-row>
                </v-tabs-window-item>
              </v-tabs-window>
            </v-card>
          </div>
        </v-col>
      </v-row>
    </v-window-item>
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="통행요금 개정 시에 적용됩니다." />
      <InformationComponent style="margin-top: 7px" message="미사용 기능 입니다." icon-type="attention" />
      <v-row>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 300px; margin-top: 10px; margin-right: 20px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>할인시간대 할인율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="discountHourContent"
                  :headers="discountSurchargeHeaders.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="discountHourContent"
                  :headers="discountSurchargeHeaders.carType"
                  :cols-per-row="1"
                  :is-loading="isLoading"
                  :tableHeaderDisplay="true"
                  tableHeaderWidth="30%"
                  :covertStringToNumberForNumberInput="true"
                />
              </div>
            </div>
          </v-card>
        </div>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 300px; margin-top: 10px; margin-right: 20px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>할증시간대 할증율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="extraHourContent"
                  :headers="discountSurchargeHeaders.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="extraHourContent"
                  :headers="discountSurchargeHeaders.carType"
                  :cols-per-row="1"
                  :is-loading="isLoading"
                  :tableHeaderDisplay="true"
                  tableHeaderWidth="30%"
                  :covertStringToNumberForNumberInput="true"
                />
              </div>
            </div>
          </v-card>
        </div>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 300px; margin-top: 10px; margin-right: 20px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>휴일 할증율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="extraHolydayContent"
                  :headers="discountSurchargeHeaders.base"
                  :cols-per-row="1"
                  table-header-width="30%"
                />
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="extraHolydayContent"
                  :headers="discountSurchargeHeaders.carType"
                  :cols-per-row="1"
                  :is-loading="isLoading"
                  :tableHeaderDisplay="true"
                  tableHeaderWidth="30%"
                  :covertStringToNumberForNumberInput="true"
                />
              </div>
            </div>
          </v-card>
        </div>
      </v-row>
    </v-window-item>
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="영업소 기초정보 개정 시에 적용됩니다." />
      <InformationComponent
        style="margin-top: 7px"
        icon-type="attention"
        message="감면단말기 면제 적용 중으로 단말기 할인기능은 적용되지 않습니다."
      />
      <v-row>
        <div class="tab-window-contents-container">
          <v-card elevation="4" style="width: 700px; margin-top: 14px">
            <div class="tab-window-container">
              <div class="tab-window-contents-container">
                <h3>단말기 할인율</h3>
              </div>
              <div class="tab-window-contents-container">
                <InputFormGrid
                  class="input-form-style"
                  v-model="obuDiscountContent"
                  :headers="obuDiscountBaseHeaders"
                  :cols-per-row="2"
                  table-header-width="16%"
                  row-height="36px"
                />
              </div>
              <v-row>
                <v-col>
                  <div style="width: 99%" class="tab-window-contents-container">
                    <table class="table-header-style">
                      <tr>
                        <th>신요금</th>
                        <th>
                          <v-btn variant="flat" size="small" color="blue" text="신→구" @click="obuDiscountMoveNewToOld" />
                        </th>
                      </tr>
                    </table>
                    <InputFormGrid
                      class="input-form-style"
                      v-model="obuDiscountContent"
                      :headers="obuDiscountNewHeaders"
                      :cols-per-row="1"
                      :is-loading="isLoading"
                      :covertStringToNumberForNumberInput="true"
                      table-header-width="60%"
                      row-height="36px"
                    />
                  </div>
                </v-col>
                <v-col>
                  <div style="width: 99%" class="tab-window-contents-container">
                    <table class="table-header-style">
                      <tr>
                        <th>구요금</th>
                        <th />
                      </tr>
                    </table>
                    <InputFormGrid
                      class="input-form-style"
                      v-model="obuDiscountContent"
                      :headers="obuDiscountOldHeaders"
                      :cols-per-row="1"
                      :is-loading="isLoading"
                      :covertStringToNumberForNumberInput="true"
                      table-header-width="60%"
                      row-height="36px"
                    />
                  </div>
                </v-col>
              </v-row>
            </div>
          </v-card>
        </div>
      </v-row>
    </v-window-item>
    <v-window-item>
      <InformationComponent style="margin-top: 14px" message="연속통행할인 개정 시에 적용됩니다." />
      <InformationComponent style="margin: 7px 0" message="수정이 불가능한 화면입니다." icon-type="attention" />
      <TableComponent
        scroll-key="gridKeyContinuousDiscount"
        :headers="continuousDiscountHeaders"
        :contents="continuousDiscountContents"
        :height-offset="heightOffset"
        row-type="mix"
      />
    </v-window-item>
    <!--
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="통행요금 개정 시에 적용됩니다." />
      <InputFormGrid
        v-model="chargeExtraContent"
        :headers="chargeExtraHeaders"
        :cols-per-row="4"
        :covertStringToNumberForNumberInput="true"
      />
    </v-window-item>
    <v-window-item class="tab-container">
      <InformationComponent style="margin-top: 14px" message="영업소 기초정보 개정 시에 적용됩니다.(TCS)" />
      <InputFormGrid
        v-model="ecardChargeLimitContent"
        :headers="ecardChargeLimitHeaders"
        :cols-per-row="4"
      />
    </v-window-item>
    -->
  </v-window>
</template>

<script setup>
import { ref, onActivated, watch, nextTick, reactive } from 'vue';
import { request, btnHandler, changeButtonStatusSave, showMessage, datetimeLocalToyyyyMMddHHmmss } from '@/utils/common';
import { useColorStore } from '@/stores/index';
import { useAuthStore } from '@/stores/index';

const authStore = useAuthStore();
const colorStore = useColorStore();

const isLoading = ref(false);
const heightOffset = 189 + 28 + 29 + 67 + 28;

/* 카드별 할인 */
const ecardDiscountCarTypeContent = ref([]);
const ecardDiscountContent = ref([]);
/* 시간별 할인 */
const hourDiscountWeekdaysContents = ref([]);
const hourDiscountWeekendContents = ref([]);
/* 할인/할증 */
const discountHourContent = ref([]);
const extraHourContent = ref([]);
const extraHolydayContent = ref([]);
/* 전자카드 충전 할증(미사용) */
const chargeExtraContent = ref([]);
/* 전자카드 최대금액(미사용) */
const ecardChargeLimitContent = ref([]);
/* 단말기할인 */
const obuDiscountContent = ref([]);
/* 연속통행할인 */
const continuousDiscountContents = ref([]);

const tab = ref(0);

/* 시간별할인 하위 탭 */
const hourDiscountWeekdaysTab = ref(0);
const hourDiscountWeekendTab = ref(0);

/* 카드별 할인 - Headers */
const ecardDiscountHeaders = {
  carType: {
    base: [{ title: '신정보 적용일시', key: 'DC_NEW_DT_FORMAT', option: 'date', type: 'datetime' }],
    new: [
      { title: '1종', key: 'NEW_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '2종', key: 'NEW_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '3종', key: 'NEW_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '4종', key: 'NEW_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '5종', key: 'NEW_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '6종', key: 'NEW_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '7종', key: 'NEW_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '8종', key: 'NEW_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
    ],
    old: [
      { title: '1종', key: 'OLD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '2종', key: 'OLD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '3종', key: 'OLD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '4종', key: 'OLD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '5종', key: 'OLD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '6종', key: 'OLD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '7종', key: 'OLD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '8종', key: 'OLD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
    ],
  },
  prepaid: {
    base: [{ title: '신정보 적용일시', key: 'NEW_EPCARD_DC_APPLY_DT_FORMAT', option: 'date', type: 'datetime' }],
    new: [
      { title: '할인율1', key: 'NEW_EPCARD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율2', key: 'NEW_EPCARD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율3', key: 'NEW_EPCARD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율4', key: 'NEW_EPCARD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율5', key: 'NEW_EPCARD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율6', key: 'NEW_EPCARD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율7', key: 'NEW_EPCARD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율8', key: 'NEW_EPCARD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
    ],
    old: [
      { title: '할인율1', key: 'OLD_EPCARD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율2', key: 'OLD_EPCARD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율3', key: 'OLD_EPCARD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율4', key: 'OLD_EPCARD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율5', key: 'OLD_EPCARD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율6', key: 'OLD_EPCARD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율7', key: 'OLD_EPCARD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율8', key: 'OLD_EPCARD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
    ],
  },
  postpaid: {
    base: [{ title: '신정보 적용일시', key: 'NEW_ELCARD_DC_APPLY_DT_FORMAT', option: 'date', type: 'datetime' }],
    new: [
      { title: '할인율1', key: 'NEW_ELCARD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율2', key: 'NEW_ELCARD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율3', key: 'NEW_ELCARD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율4', key: 'NEW_ELCARD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율5', key: 'NEW_ELCARD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율6', key: 'NEW_ELCARD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율7', key: 'NEW_ELCARD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
      { title: '할인율8', key: 'NEW_ELCARD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
    ],
    old: [
      { title: '할인율1', key: 'OLD_ELCARD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율2', key: 'OLD_ELCARD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율3', key: 'OLD_ELCARD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율4', key: 'OLD_ELCARD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율5', key: 'OLD_ELCARD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율6', key: 'OLD_ELCARD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율7', key: 'OLD_ELCARD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
      { title: '할인율8', key: 'OLD_ELCARD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
    ],
  },
};

/* 시간별 할인 Headers */
const hourDiscountHeaders = {
  base: [
    { title: '차로구분', key: 'LANE_DIV_NM', align: 'center', disabled: true },
    { title: '등록일시', key: 'INST_DT_FORMAT', align: 'center', disabled: true },
  ],
  weekdays: {
    new: [
      { title: '시작시분', key: 'NEW_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '종료시분', key: 'NEW_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '할인율1', key: 'NEW_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율2', key: 'NEW_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율3', key: 'NEW_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율4', key: 'NEW_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율5', key: 'NEW_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율6', key: 'NEW_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율7', key: 'NEW_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율8', key: 'NEW_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    ],
    old: [
      { title: '시작시분', key: 'OLD_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '종료시분', key: 'OLD_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '할인율1', key: 'OLD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율2', key: 'OLD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율3', key: 'OLD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율4', key: 'OLD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율5', key: 'OLD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율6', key: 'OLD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율7', key: 'OLD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율8', key: 'OLD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
    ],
  },
  weekend: {
    new: [
      { title: '시작시분', key: 'NEW_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '종료시분', key: 'NEW_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '할인율1', key: 'NEW_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율2', key: 'NEW_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율3', key: 'NEW_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율4', key: 'NEW_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율5', key: 'NEW_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율6', key: 'NEW_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율7', key: 'NEW_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
      { title: '할인율8', key: 'NEW_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    ],
    old: [
      { title: '시작시분', key: 'OLD_START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '종료시분', key: 'OLD_END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
      { title: '할인율1', key: 'OLD_DC_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율2', key: 'OLD_DC_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율3', key: 'OLD_DC_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율4', key: 'OLD_DC_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율5', key: 'OLD_DC_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율6', key: 'OLD_DC_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율7', key: 'OLD_DC_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
      { title: '할인율8', key: 'OLD_DC_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100, readonly: true },
    ],
  },
};

/* 할인/할증 */
const discountSurchargeHeaders = {
  base: [
    { title: '등록일시', key: 'INST_DT_FORMAT', align: 'center', disabled: true },
    { title: '시작시분', key: 'START_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
    { title: '종료시분', key: 'END_TM', align: 'center', option: 'timepicker', format: 'HHmm' },
  ],
  carType: [
    { title: '차종1', key: 'CAR1_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종2', key: 'CAR2_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종3', key: 'CAR3_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종4', key: 'CAR4_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종5', key: 'CAR5_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종6', key: 'CAR6_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종7', key: 'CAR7_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
    { title: '차종8', key: 'CAR8_DC_EXTRA', align: 'center', option: 'numberInput', step: 1, min: 0, max: 100 },
  ],
};

// TCS 전용
// const chargeExtraHeaders = ref([
//   { title: '영업소명', key: 'IC_NM', align: 'center', disabled: true },
//   { title: '수정시각', key: 'MDFY_DT_FORMAT', align: 'center', disabled: true },
//   { title: '신 적용일시', key: 'NEW_APPLY_DT', align: 'center' },
//   { title: '구 폐쇄일시', key: 'OLD_CLS_DT', align: 'center' },
//   { title: '구 충전할증율 1 (%)', key: 'OLD_CHRG_EXTRA_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 1 (원)', key: 'OLD_FARE_UNIT1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 1 (%)', key: 'NEW_CHRG_EXTRA_RATE1', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 1 (원)', key: 'NEW_FARE_UNIT1', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 2 (%)', key: 'OLD_CHRG_EXTRA_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 2 (원)', key: 'OLD_FARE_UNIT2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 2 (%)', key: 'NEW_CHRG_EXTRA_RATE2', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 2 (원)', key: 'NEW_FARE_UNIT2', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 3 (%)', key: 'OLD_CHRG_EXTRA_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 3 (원)', key: 'OLD_FARE_UNIT3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 3 (%)', key: 'NEW_CHRG_EXTRA_RATE3', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 3 (원)', key: 'NEW_FARE_UNIT3', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 4 (%)', key: 'OLD_CHRG_EXTRA_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 4 (원)', key: 'OLD_FARE_UNIT4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 4 (%)', key: 'NEW_CHRG_EXTRA_RATE4', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 4 (원)', key: 'NEW_FARE_UNIT4', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 5 (%)', key: 'OLD_CHRG_EXTRA_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 5 (원)', key: 'OLD_FARE_UNIT5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 5 (%)', key: 'NEW_CHRG_EXTRA_RATE5', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 5 (원)', key: 'NEW_FARE_UNIT5', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 6 (%)', key: 'OLD_CHRG_EXTRA_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 6 (원)', key: 'OLD_FARE_UNIT6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 6 (%)', key: 'NEW_CHRG_EXTRA_RATE6', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 6 (원)', key: 'NEW_FARE_UNIT6', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 7 (%)', key: 'OLD_CHRG_EXTRA_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 7 (원)', key: 'OLD_FARE_UNIT7', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 7 (%)', key: 'NEW_CHRG_EXTRA_RATE7', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 7 (원)', key: 'NEW_FARE_UNIT7', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 8 (%)', key: 'OLD_CHRG_EXTRA_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 8 (원)', key: 'OLD_FARE_UNIT8', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 8 (%)', key: 'NEW_CHRG_EXTRA_RATE8', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 8 (원)', key: 'NEW_FARE_UNIT8', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 9 (%)', key: 'OLD_CHRG_EXTRA_RATE9', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 9 (원)', key: 'OLD_FARE_UNIT9', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 9 (%)', key: 'NEW_CHRG_EXTRA_RATE9', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 9 (원)', key: 'NEW_FARE_UNIT9', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '구 충전할증율 10 (%)', key: 'OLD_CHRG_EXTRA_RATE10', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '구 금액단위 10 (원)', key: 'OLD_FARE_UNIT10', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
//   { title: '신 충전할증율 10 (%)', key: 'NEW_CHRG_EXTRA_RATE10', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
//   { title: '신 금액단위 10 (원)', key: 'NEW_FARE_UNIT10', align: 'center', option: 'numberInput', step: 50, min: 0, max: 99999999 },
// ]);

// TCS 전용
// const ecardChargeLimitHeaders = ref([
//   { title: '영업소ID', key: 'IC_CODE', align: 'center', disabled: true },
//   { title: '영업소명', key: 'IC_NM', align: 'center', disabled: true },
//   { title: '수정시각', key: 'MDFY_DT_FORMAT', align: 'center', disabled: true },
//   { title: '적용일시', key: 'APPLY_DT', align: 'center' },
//   { title: '구 1회최대충전금액', key: 'OLD_MAX_CHRG', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999999999 },
//   { title: '신 1회최대충전금액', key: 'NEW_MAX_CHRG', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999999999 },
//   { title: '구 최대보유잔액', key: 'OLD_MAX_BALC', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999999999 },
//   { title: '신 최대보유잔액', key: 'NEW_MAX_BALC', align: 'center', option: 'numberInput', step: 50, min: 0, max: 999999999999 },
// ]);

const obuDiscountBaseHeaders = ref([
  { title: '영업소ID', key: 'IC_CODE', align: 'center', disabled: true },
  { title: '영업소명', key: 'IC_NM', align: 'center', disabled: true },
  { title: '수정시각', key: 'INST_DT_FORMAT', align: 'center', disabled: true },
  { title: '신 적용일시', key: 'DCRATE_NEW_DT_FORMAT', align: 'center', option: 'date', type: 'datetime' },
]);

const obuDiscountNewHeaders = ref([
  { title: '장애인(1~6급)', key: 'NEW_DCRATE10', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
  { title: '국가유공상이자(6~7급)', key: 'NEW_DCRATE30', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
  { title: '5.18민주화운동부상자(6급이하)', key: 'NEW_DCRATE50', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
  { title: '고엽제후유(의)증환자', key: 'NEW_DCRATE70', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99 },
]);

const obuDiscountOldHeaders = ref([
  { title: '장애인(1~6급)', key: 'OLD_DCRATE10', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
  { title: '국가유공상이자(6~7급)', key: 'OLD_DCRATE30', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
  { title: '5.18민주화운동부상자(6급이하)', key: 'OLD_DCRATE50', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
  { title: '고엽제후유(의)증환자', key: 'OLD_DCRATE70', align: 'center', option: 'numberInput', step: 1, min: 0, max: 99, readonly: true },
]);

const continuousDiscountHeaders = ref([
  {
    title: '구분',
    children: [
      { title: '영업소ID', key: 'IC_CODE', width: '80' },
      { title: '영업소명', key: 'IC_NM', width: '80' },
      { title: '대상 영업소ID', key: 'TRG_IC_CODE', width: '100' },
      { title: '대상 영업소명', key: 'TRG_IC_NM', width: '100' },
      { title: '기준정보 적용여부', key: 'BASE_DIV_NM', width: '120' },
    ],
  },
  {
    title: '신정보',
    children: [
      { title: '제한시간(시)', key: 'NEW_LMT_HR', width: '110' },
      { title: '제한시간(분)', key: 'NEW_LMT_MIN', width: '110' },
      { title: '경형 할인금액', key: 'NEW_DC_FARE1', width: '110' },
      { title: '중형 할인금액', key: 'NEW_DC_FARE3', width: '110' },
      { title: '소형 할인금액', key: 'NEW_DC_FARE2', width: '110' },
      { title: '대형 할인금액', key: 'NEW_DC_FARE4', width: '110' },
    ],
  },
  {
    title: '구정보',
    children: [
      { title: '제한시간(시)', key: 'OLD_LMT_HR', width: '110' },
      { title: '제한시간(분)', key: 'OLD_LMT_MIN', width: '110' },
      { title: '경형 할인금액', key: 'OLD_DC_FARE1', width: '110' },
      { title: '소형 할인금액', key: 'OLD_DC_FARE2', width: '110' },
      { title: '중형 할인금액', key: 'OLD_DC_FARE3', width: '110' },
      { title: '대형 할인금액', key: 'OLD_DC_FARE4', width: '110' },
    ],
  },
  {
    title: '구분',
    children: [
      // { title: '최초설정자', key: 'MAKE_ADMIN_NM', width: '90' },
      { title: '설정일시', key: 'MAKE_DT_FORMAT', width: '100' },
      // { title: '수정자', key: 'MDFY_ADMIN_NM', width: '90' },
      { title: '수정일시', key: 'MDFY_CONT_DT_FORMAT', width: '100' },
    ],
  },
]);

const setButtonStatusSave = () => {
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
    changeButtonStatusSave('Y');
  } else if (tab.value === 6) {
    changeButtonStatusSave('Y');
  }
};

onActivated(async () => {
  setButtonStatusSave();
  await handleSearch();
});

watch(
  tab,
  async () => {
    setButtonStatusSave();
    await handleSearch();
  },
  { immediate: false },
);

const handleSearch = async () => {
  isLoading.value = true;
  if (tab.value === 0) {
    // 카드별 할인
    try {
      // 전자카드 지불 할인율
      const data = await request('post', 'api/base/getEcardDiscountCarType', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      ecardDiscountCarTypeContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }

    try {
      // 선/후불할인 전자카드 할인율
      const data = await request('post', 'api/base/getEcardDiscount', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      ecardDiscountContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 1) {
    // 시간별 할인
    try {
      // 평일 시간대 할인율
      const data = await request('post', 'api/base/getHourDiscount', {
        ...{
          IC_CODE: authStore.getIcCode,
          DATE_DIV: '0', // 평일
          LANE_DIV: '1', // 하이패스
          HR_SEQ: '1', // 사전에 생성한 값
        },
      });
      hourDiscountWeekdaysContents.value = data;

      if (data.length == 0) {
        alert('평일 시간대 할인율 데이터가 없습니다.');
      }
    } catch (error) {
      console.error('평일 시간대 할인율 데이터 조회 중 오류 발생:', error);
    }

    try {
      // 평일 시간대 할인율
      const data = await request('post', 'api/base/getHourDiscount', {
        ...{
          IC_CODE: authStore.getIcCode,
          DATE_DIV: '1', // 주말
          LANE_DIV: '1', // 하이패스
          HR_SEQ: '2', // 사전에 생성한 값
        },
      });
      hourDiscountWeekendContents.value = data;

      if (data.length == 0) {
        alert('주말 시간대 할인율 데이터가 없습니다.');
      }
    } catch (error) {
      console.error('주말 시간대 할인율 데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 2) {
    // 할인/할증
    try {
      // 할인시간대
      const data = await request('post', 'api/base/getDiscountExtra', {
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 1, // 시간대
          DC_EXTRA_DIV: 0, // 할인
        },
      });
      console.log(data);
      discountHourContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }

    try {
      // 할증시간대
      const data = await request('post', 'api/base/getDiscountExtra', {
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 1, // 시간대
          DC_EXTRA_DIV: 1, // 할증
        },
      });
      console.log(data);
      extraHourContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }

    try {
      // 휴일할증
      const data = await request('post', 'api/base/getDiscountExtra', {
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 0, // 휴일
          DC_EXTRA_DIV: 1, // 할증
        },
      });
      console.log(data);
      extraHolydayContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 3) {
    // 할인단말기 할인율
    try {
      const data = await request('post', 'api/base/getObuDiscount', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      obuDiscountContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 4) {
    // 연속통행할인율
    try {
      const data = await request('post', 'api/base/getContinuousDiscount', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      continuousDiscountContents.value = data;

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 5) {
    // 전자카드 충전할증
    try {
      const data = await request('post', 'api/base/getChargeExtra', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      chargeExtraContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  } else if (tab.value === 6) {
    // 전자카드 최대금액
    try {
      const data = await request('post', 'api/base/getEcardChargeLimit', {
        ...{
          IC_CODE: authStore.getIcCode,
        },
      });
      console.log(data);
      ecardChargeLimitContent.value = data[0];

      if (data.length == 0) {
        alert('데이터가 없습니다.');
      }
    } catch (error) {
      console.error('데이터 조회 중 오류 발생:', error);
    }
  }

  nextTick(() => (isLoading.value = false));
};

const onClickSaveHourDiscount = async (item) => {
  // 시간별 할인
  isLoading.value = true;
  try {
    const data = await request('post', 'api/base/setHourDiscount', {
      ...item,
      ...{
        MDFY_ADMIN_ID: authStore.getWorkerNo,
      },
    });
    if (data.ERROR_CODE >= 1) {
      showMessage(`${item.DATE_DIV_NM} 시간대 ${item.HR_SEQ_NM}이 수정되었습니다.`, 'success');
    } else {
      alert(data.ERROR_MSG);
    }
  } catch (error) {
    console.error(`${item.DATE_DIV_NM} 시간대 ${item.HR_SEQ_NM} 수정 중 오류 발생:`, error);
  } finally {
    isLoading.value = false;
  }
};

const handleSave = async () => {
  isLoading.value = true;
  if (tab.value === 0) {
    // 카드별 할인
    console.log(ecardDiscountCarTypeContent.value);
    try {
      ecardDiscountCarTypeContent.value.DC_NEW_DT = datetimeLocalToyyyyMMddHHmmss(ecardDiscountCarTypeContent.value.DC_NEW_DT_FORMAT);
      const data = await request('post', 'api/base/setEcardDiscountCarType', {
        ...ecardDiscountCarTypeContent.value,
        ...{
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('전자카드 차종별 할인율이 수정되었습니다.');
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('전자카드 차종별 할인율 수정 중 오류 발생:', error);
    }

    try {
      ecardDiscountContent.value.NEW_EPCARD_DC_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(
        ecardDiscountContent.value.NEW_EPCARD_DC_APPLY_DT_FORMAT,
      ).slice(0, -2);
      ecardDiscountContent.value.NEW_ELCARD_DC_APPLY_DT = datetimeLocalToyyyyMMddHHmmss(
        ecardDiscountContent.value.NEW_ELCARD_DC_APPLY_DT_FORMAT,
      ).slice(0, -2);
      const data = await request('post', 'api/base/setEcardDiscount', {
        ...ecardDiscountContent.value,
        ...{
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('전자카드 선/후불별 할인율이 수정되었습니다.');
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('전자카드 선/후불별 할인율 수정 중 오류 발생:', error);
    }

    // handleSearch();
  } else if (tab.value === 1) {
    // 시간별 할인
    showMessage('할인율별 저장버튼을 클릭해 처리해주세요.', 'warning');
  } else if (tab.value === 2) {
    // 할인/할증
    try {
      // 할인시간대

      const data = await request('post', 'api/base/setDiscountExtra', {
        ...discountHourContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 1, // 시간대
          DC_EXTRA_DIV: 0, // 할인
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('할인시간대가 수정되었습니다.');
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('할인시간대 수정 중 오류 발생:', error);
    }

    try {
      // 할증시간대

      const data = await request('post', 'api/base/setDiscountExtra', {
        ...extraHourContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 1, // 시간대
          DC_EXTRA_DIV: 1, // 할증
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('할증시간대가 수정되었습니다.');
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('할증시간대 수정 중 오류 발생:', error);
    }

    try {
      // 휴일 할증

      const data = await request('post', 'api/base/setDiscountExtra', {
        ...extraHolydayContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          TYPE_DIV: 0, // 휴일
          DC_EXTRA_DIV: 1, // 할증
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('휴일할증이 수정되었습니다.');
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('휴일할증 수정 중 오류 발생:', error);
    }

    handleSearch();
  } else if (tab.value === 3) {
    //할인단말기 할인율
    try {
      const data = await request('post', 'api/base/setObuDiscount', {
        ...obuDiscountContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          MDFY_ADMIN_ID: authStore.getWorkerNo,
          DCRATE_NEW_DT: datetimeLocalToyyyyMMddHHmmss(obuDiscountContent.value.DCRATE_NEW_DT_FORMAT).slice(0, -2),
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('단말기 할인율이 수정되었습니다.');
        handleSearch();
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('단말기 할인율 수정 중 오류 발생:', error);
    }
  } else if (tab.value === 5) {
    // 전자카드 충전할증
    try {
      const data = await request('post', 'api/base/setChargeExtra', {
        ...chargeExtraContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('전자카드 충전할증이 수정되었습니다.');
        handleSearch();
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('전자카드 충전할증 수정 중 오류 발생:', error);
    }
  } else if (tab.value === 6) {
    // 전자카드 최대금액
    try {
      const data = await request('post', 'api/base/setEcardChargeLimit', {
        ...ecardChargeLimitContent.value,
        ...{
          IC_CODE: authStore.getIcCode,
          ADMIN_ID: authStore.getWorkerNo,
          MDFY_ADMIN_ID: authStore.getWorkerNo,
        },
      });

      if (data.ERROR_CODE >= 1) {
        alert('전자카드 최대금액이 수정되었습니다.');
        handleSearch();
      } else {
        alert(data.ERROR_MSG);
      }
    } catch (error) {
      console.error('전자카드 최대금액 수정 중 오류 발생:', error);
    }
  }
  nextTick(() => (isLoading.value = false));
};

const ecardDiscountCarTypeMoveNewToOld = () => {
  ecardDiscountCarTypeContent.value.OLD_DC_RATE1 = ecardDiscountCarTypeContent.value.NEW_DC_RATE1;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE2 = ecardDiscountCarTypeContent.value.NEW_DC_RATE2;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE3 = ecardDiscountCarTypeContent.value.NEW_DC_RATE3;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE4 = ecardDiscountCarTypeContent.value.NEW_DC_RATE4;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE5 = ecardDiscountCarTypeContent.value.NEW_DC_RATE5;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE6 = ecardDiscountCarTypeContent.value.NEW_DC_RATE6;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE7 = ecardDiscountCarTypeContent.value.NEW_DC_RATE7;
  ecardDiscountCarTypeContent.value.OLD_DC_RATE8 = ecardDiscountCarTypeContent.value.NEW_DC_RATE8;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE1 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE2 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE3 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE4 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE5 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE6 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE7 = 0;
  ecardDiscountCarTypeContent.value.NEW_DC_RATE8 = 0;
};

const ecardDiscountPrepaidMoveNewToOld = () => {
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE1 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE1;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE2 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE2;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE3 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE3;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE4 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE4;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE5 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE5;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE6 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE6;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE7 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE7;
  ecardDiscountContent.value.OLD_EPCARD_DC_RATE8 = ecardDiscountContent.value.NEW_EPCARD_DC_RATE8;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE1 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE2 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE3 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE4 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE5 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE6 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE7 = 0;
  ecardDiscountContent.value.NEW_EPCARD_DC_RATE8 = 0;
};

const ecardDiscountPostpaidMoveNewToOld = () => {
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE1 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE1;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE2 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE2;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE3 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE3;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE4 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE4;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE5 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE5;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE6 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE6;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE7 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE7;
  ecardDiscountContent.value.OLD_ELCARD_DC_RATE8 = ecardDiscountContent.value.NEW_ELCARD_DC_RATE8;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE1 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE2 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE3 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE4 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE5 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE6 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE7 = 0;
  ecardDiscountContent.value.NEW_ELCARD_DC_RATE8 = 0;
};

const hourDiscountWeekdaysMoveNewToOld = (index) => {
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE1 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE1;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE2 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE2;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE3 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE3;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE4 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE4;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE5 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE5;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE6 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE6;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE7 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE7;
  hourDiscountWeekdaysContents.value[index].OLD_DC_RATE8 = hourDiscountWeekdaysContents.value[index].NEW_DC_RATE8;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE1 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE2 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE3 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE4 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE5 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE6 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE7 = 0;
  hourDiscountWeekdaysContents.value[index].NEW_DC_RATE8 = 0;
};

const hourDiscountWeekendMoveNewToOld = (index) => {
  hourDiscountWeekendContents.value[index].OLD_DC_RATE1 = hourDiscountWeekendContents.value[index].NEW_DC_RATE1;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE2 = hourDiscountWeekendContents.value[index].NEW_DC_RATE2;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE3 = hourDiscountWeekendContents.value[index].NEW_DC_RATE3;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE4 = hourDiscountWeekendContents.value[index].NEW_DC_RATE4;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE5 = hourDiscountWeekendContents.value[index].NEW_DC_RATE5;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE6 = hourDiscountWeekendContents.value[index].NEW_DC_RATE6;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE7 = hourDiscountWeekendContents.value[index].NEW_DC_RATE7;
  hourDiscountWeekendContents.value[index].OLD_DC_RATE8 = hourDiscountWeekendContents.value[index].NEW_DC_RATE8;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE1 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE2 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE3 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE4 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE5 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE6 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE7 = 0;
  hourDiscountWeekendContents.value[index].NEW_DC_RATE8 = 0;
};

const obuDiscountMoveNewToOld = () => {
  obuDiscountContent.value.OLD_DCRATE00 = obuDiscountContent.value.NEW_DCRATE00;
  obuDiscountContent.value.OLD_DCRATE10 = obuDiscountContent.value.NEW_DCRATE10;
  obuDiscountContent.value.OLD_DCRATE20 = obuDiscountContent.value.NEW_DCRATE20;
  obuDiscountContent.value.OLD_DCRATE30 = obuDiscountContent.value.NEW_DCRATE30;
  obuDiscountContent.value.OLD_DCRATE40 = obuDiscountContent.value.NEW_DCRATE40;
  obuDiscountContent.value.OLD_DCRATE50 = obuDiscountContent.value.NEW_DCRATE50;
  obuDiscountContent.value.OLD_DCRATE60 = obuDiscountContent.value.NEW_DCRATE60;
  obuDiscountContent.value.OLD_DCRATE70 = obuDiscountContent.value.NEW_DCRATE70;
  obuDiscountContent.value.OLD_DCRATE80 = obuDiscountContent.value.NEW_DCRATE80;
  obuDiscountContent.value.OLD_DCRATE90 = obuDiscountContent.value.NEW_DCRATE90;
  obuDiscountContent.value.NEW_DCRATE00 = 0;
  obuDiscountContent.value.NEW_DCRATE10 = 0;
  obuDiscountContent.value.NEW_DCRATE20 = 0;
  obuDiscountContent.value.NEW_DCRATE30 = 0;
  obuDiscountContent.value.NEW_DCRATE40 = 0;
  obuDiscountContent.value.NEW_DCRATE50 = 0;
  obuDiscountContent.value.NEW_DCRATE60 = 0;
  obuDiscountContent.value.NEW_DCRATE70 = 0;
  obuDiscountContent.value.NEW_DCRATE80 = 0;
  obuDiscountContent.value.NEW_DCRATE90 = 0;
};

const handlePowerOff = () => {
  alert('기초정보 관리 - 종료버튼 클릭');
};

btnHandler({
  Search: handleSearch,
  Save: handleSave,
  PowerOff: handlePowerOff,
});
</script>
<style scoped>
h3 {
  margin-top: 7px;
}
.tab-container {
  margin: 0 auto;
  margin-left: 30px;
  padding-right: 30px;
}
.tab-container .v-btn {
  font-weight: bold;
}
.tab-window-container {
  /* background-color: #0086e510; */
  margin: 20px 20px 10px 20px;
}
.tab-window-contents-container {
  /* background-color: #0086e510; */
  margin-bottom: 17px;
}
.tab-window-contents-buttons {
  margin-bottom: 7px;
  display: flex;
  justify-content: end;
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
.input-form-style:deep(th) {
  text-align: center;
}
</style>
