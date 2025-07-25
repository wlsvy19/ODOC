import { createRouter, createWebHistory } from 'vue-router';
import CorrectionCount from '@/views/office/CorrectionCount.vue';

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('@/components/authentication/AuthLogin.vue'),
  },
  {
    path: '/main',
    name: 'Dashboard',
    component: () => import('@/views/MainDashboard.vue'),
  },
  {
    path: '/0101',
    name: 'WorkStat',
    component: () => import('@/views/work/WorkStat.vue'),
  },
  {
    path: '/0102',
    name: 'WorkDoc',
    component: () => import('@/views/work/WorkDoc.vue'),
  },
  {
    path: '/0103',
    name: 'PassPrim',
    component: () => import('@/views/work/PassPrim.vue'),
  },
  {
    path: '/0104',
    name: 'PassPrimDtl',
    component: () => import('@/views/work/PassPrimDtl.vue'),
  },
  {
    path: '/0301',
    name: 'DayFin',
    component: () => import('@/views/day/DayFin.vue'),
  },
  {
    path: '/0302',
    name: 'DayFinMDFY',
    component: () => import('@/views/day/DayFinMDFY.vue'),
  },
  {
    path: '/0303',
    name: 'DayFinReport',
    component: () => import('@/views/day/DayFinReport.vue'),
  },
  {
    path: '/0304',
    name: 'DayFinPeriodReport',
    component: () => import('@/views/day/DayFinPeriodReport.vue'),
  },
  {
    path: '/0305',
    name: 'DayFinLock',
    component: () => import('@/views/day/DayFinLock.vue'),
  },
  {
    path: '/0306',
    name: 'DayFinUnlock',
    component: () => import('@/views/day/DayFinUnlock.vue'),
  },
  {
    path: '/0307',
    name: 'DayFinReportNew',
    component: () => import('@/views/day/DayFinReportNew.vue'),
  },
  {
    path: '/0308',
    name: 'DayFinPeriodReportNew',
    component: () => import('@/views/day/DayFinPeriodReportNew.vue'),
  },
  {
    path: '/0401',
    name: 'DailyTrafficProcess',
    component: () => import('@/views/sales/DailyTrafficProcess.vue'),
  },
  {
    path: '/0402',
    name: 'DailyViolation',
    component: () => import('@/views/sales/DailyViolation.vue'),
  },
  {
    path: '/0403',
    name: 'ReducedVehicleListEX',
    component: () => import('@/views/sales/ReducedVehicleListEX.vue'),
  },
  {
    path: '/0404',
    name: 'ReducedVehicleListBS',
    component: () => import('@/views/sales/ReducedVehicleListBS.vue'),
  },
  {
    path: '/0409',
    name: 'DiscountSummary',
    component: () => import('@/views/sales/DiscountSummary.vue'),
  },
  {
    path: '/0410',
    name: 'DiscountSurchargeList',
    component: () => import('@/views/sales/DiscountSurchargeList.vue'),
  },
  {
    path: '/0411', // JP-월간 통행료징수 현황(월보)
    name: 'MonthTollFeeReport',
    component: () => import('@/views/sales/MonthTollFeeReport.vue'),
  },
  {
    path: '/0412',
    name: 'MonthTollConstructionReport',
    component: () => import('@/views/sales/MonthTollConstructionReport.vue'),
  },
  {
    path: '/0414',
    name: 'DailyExemptDetail',
    component: () => import('@/views/sales/DailyExemptDetail.vue'),
  },
  {
    path: '/0415',
    name: 'BusanContinuousTrafficDC',
    component: () => import('@/views/sales/BusanContinuousTrafficDC.vue'),
  },
  {
    path: '/0416',
    name: 'RefundListEx',
    component: () => import('@/views/sales/RefundListEx.vue'),
  },
  {
    path: '/0417',
    name: 'RefundListBs',
    component: () => import('@/views/sales/RefundListBs.vue'),
  },

  {
    path: '/0418',
    name: 'DailyReductionSummaryEx',
    component: () => import('@/views/sales/DailyReductionSummaryEx.vue'),
  },
  {
    path: '/0419',
    name: 'DailyReductionSummaryBs',
    component: () => import('@/views/sales/DailyReductionSummaryBs.vue'),
  },
  {
    path: '/0420',
    name: 'SummaryByOBUType',
    component: () => import('@/views/sales/SummaryByOBUType.vue'),
  },
  {
    path: '/0421',
    name: 'TollCollectionTrends',
    component: () => import('@/views/sales/TollCollectionTrends.vue'),
  },
  {
    path: '/0423',
    name: 'DailyViolationCorrectionSummary',
    component: () => import('@/views/sales/DailyViolationCorrectionSummary.vue'),
  },
  {
    path: '/0424',
    name: 'JabicallPassList',
    component: () => import('@/views/sales/JabicallPassList.vue'),
  },
  {
    path: '/0425',
    name: 'SmartTollOperationStatus',
    component: () => import('@/views/sales/SmartTollOperationStatus.vue'),
  },
  {
    path: '/0901',
    name: 'Inter',
    component: () => import('@/views/inter/Inter.vue'),
  },
  {
    path: '/0902',
    name: 'CenterInter',
    component: () => import('@/views/inter/CenterInter.vue'),
  },
  {
    path: '/1003',
    name: 'RefundDetailEX',
    component: () => import('@/views/settle/RefundDetailEX.vue'),
  },
  {
    path: '/1004',
    name: 'RefundDetail',
    component: () => import('@/views/settle/RefundDetail.vue'),
  },
  {
    path: '/1005',
    name: 'RefundManagement',
    component: () => import('@/views/settle/RefundManagement.vue'),
  },
  {
    path: '/1101',
    name: 'UnPaidCar',
    component: () => import('@/views/unpaid/UnPaidCar.vue'),
  },
  {
    path: '/1102',
    name: 'UnPaidCollect',
    component: () => import('@/views/unpaid/UnPaidCollect.vue'),
  },
  {
    path: '/1103',
    name: 'LineCtrlOfficePayList',
    component: () => import('@/views/unpaid/LineCtrlOfficePayList.vue'),
  },
  {
    path: '/0501',
    name: 'TrafficDaily',
    component: () => import('@/views/traffic/TrafficDaily.vue'),
  },
  {
    path: '/0502',
    name: 'TrafficDirection',
    component: () => import('@/views/traffic/TrafficDirection.vue'),
  },
  {
    path: '/0503',
    name: 'TrafficHour',
    component: () => import('@/views/traffic/TrafficHour.vue'),
  },
  {
    path: '/0504',
    name: 'TrafficDailyDirection',
    component: () => import('@/views/traffic/TrafficDailyDirection.vue'),
  },
  {
    path: '/0505',
    name: 'TrafficDailyPayDiv',
    component: () => import('@/views/traffic/TrafficDailyPayDiv.vue'),
  },
  {
    path: '/0506',
    name: 'TrafficLine',
    component: () => import('@/views/traffic/TrafficLine.vue'),
  },
  {
    path: '/0507',
    name: 'TrafficLineHour',
    component: () => import('@/views/traffic/TrafficLineHour.vue'),
  },
  {
    path: '/0508',
    name: 'TrafficPreMonth',
    component: () => import('@/views/traffic/TrafficPreMonth.vue'),
  },
  {
    path: '/0509',
    name: 'TrafficMonth',
    component: () => import('@/views/traffic/TrafficMonth.vue'),
  },
  {
    path: '/0510',
    name: 'TrafficYear',
    component: () => import('@/views/traffic/TrafficYear.vue'),
  },
  {
    path: '/0511',
    name: 'TrafficDayOfWeek',
    component: () => import('@/views/traffic/TrafficDayOfWeek.vue'),
  },
  {
    path: '/0512',
    name: 'TrafficQuarter',
    component: () => import('@/views/traffic/TrafficQuarter.vue'),
  },
  {
    path: '/0513',
    name: 'TrafficYearDayOfWeek',
    component: () => import('@/views/traffic/TrafficYearDayOfWeek.vue'),
  },
  {
    path: '/0514',
    name: 'TrafficHourHoliday',
    component: () => import('@/views/traffic/TrafficHourHoliday.vue'),
  },
  {
    path: '/0515',
    name: 'DailyHipassUsageStatus',
    component: () => import('@/views/traffic/DailyHipassUsageStatus.vue'),
  },
  {
    path: '/0601',
    name: 'CardUseList',
    component: () => import('@/views/card/CardUseList.vue'),
  },
  {
    path: '/0602',
    name: 'ECardBL',
    component: () => import('@/views/card/ECardBL.vue'),
  },
  {
    path: '/0605',
    name: 'LightCarPL',
    component: () => import('@/views/card/LightCarPL.vue'),
  },
  {
    path: '/0606',
    name: 'InteWelfCardPL',
    component: () => import('@/views/card/InteWelfCardPL.vue'),
  },
  {
    path: '/0608',
    name: 'ECardBINManage',
    component: () => import('@/views/card/ECardBINManage.vue'),
  },
  {
    path: '/0609',
    name: 'CardCoDivPayList',
    component: () => import('@/views/card/CardCoDivPayList.vue'),
  },
  {
    path: '/0610',
    name: 'CptLpayEcardCardCoDiv',
    component: () => import('@/views/card/CptLpayEcardCardCoDiv.vue'),
  },
  {
    path: '/0611',
    name: 'ExemPLBusan',
    component: () => import('@/views/card/ExemPLBusan.vue'),
  },
  {
    path: '/0701',
    name: 'Operation',
    component: () => import('@/views/base/Operation.vue'),
  },
  {
    path: '/0702',
    name: 'Discount',
    component: () => import('@/views/base/Discount.vue'),
  },
  {
    path: '/0703',
    name: 'Worker',
    component: () => import('@/views/base/Worker.vue'),
  },
  {
    path: '/0704',
    name: 'DateManagement',
    component: () => import('@/views/base/DateManagement.vue'),
  },
  {
    path: '/0705',
    name: 'SystemCode',
    component: () => import('@/views/base/SystemCode.vue'),
  },
  {
    path: '/0706',
    name: 'Program',
    component: () => import('@/views/base/ProgramInfo.vue'),
  },
  {
    path: '/0707',
    name: 'RevisionHistory',
    component: () => import('@/views/base/RevisionHistory.vue'),
  },
  {
    path: '/0708',
    name: 'TollContinuousPass',
    component: () => import('@/views/base/TollContinuousPass.vue'),
  },
  {
    path: '/0709',
    name: 'VehicleManage',
    component: () => import('@/views/base/VehicleManage.vue'),
  },
  {
    path: '/0803',
    name: 'CarLineMonitor',
    component: () => import('@/views/maint/CarLineMonitor.vue'),
  },
  {
    path: '/0801',
    name: 'EquipErrorList',
    component: () => import('@/views/maint/EquipErrorList.vue'),
  },
  {
    path: '/0802',
    name: 'EquipErrorCum',
    component: () => import('@/views/maint/EquipErrorCum.vue'),
  },
  {
    path: '/0804',
    name: 'TableSpaceMonitor',
    component: () => import('@/views/maint/TableSpaceMonitor.vue'),
  },
  {
    path: '/0201',
    name: 'ImageCorrection',
    component: () => import('@/views/office/ImageCorrection.vue'),
  },
  {
    path: '/0202',
    name: 'Violation',
    component: () => import('@/views/office/Violation.vue'),
  },
  {
    path: '/0203',
    name: 'Process',
    component: () => import('@/views/office/Process.vue'),
  },
  {
    path: '/0204',
    name: 'ReductionCorrectionEX',
    component: () => import('@/views/office/ReductionCorrectionEX.vue'),
  },
  {
    path: '/0205',
    name: 'ReductionCheatListEX',
    component: () => import('@/views/office/ReductionCheatListEX.vue'),
  },
  {
    path: '/0206',
    name: 'ReductionCorrectionBS',
    component: () => import('@/views/office/ReductionCorrectionBS.vue'),
  },
  {
    path: '/0207',
    name: 'ReductionCheatListBS',
    component: () => import('@/views/office/ReductionCheatListBS.vue'),
  },
  {
    path: '/0208',
    name: 'PayCorrection',
    component: () => import('@/views/office/PayCorrection.vue'),
  },
  {
    path: '/0232',
    name: 'PreRegistration',
    component: () => import('@/views/office/PreRegistration.vue'),
  },
  {
    path: '/0209',
    name: 'EtcIncome',
    component: () => import('@/views/office/EtcIncome.vue'),
  },
  {
    path: '/0210',
    name: 'ViolationState',
    component: () => import('@/views/office/ViolationState.vue'),
  },
  {
    path: '/0211',
    name: 'CorrectionCount',
    component: () => import('@/views/office/CorrectionCount.vue'),
  },
];
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
