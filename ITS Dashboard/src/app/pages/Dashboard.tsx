import { Breadcrumb } from '../components/Breadcrumb';
import { CircularGauge } from '../components/CircularGauge';
import { DonutChart } from '../components/DonutChart';
import { FacilityBreakdownChart } from '../components/FacilityBreakdownChart';
import { FacilityStatusTable } from '../components/FacilityStatusTable';
import { TotalFacilityTable } from '../components/TotalFacilityTable';
import { TechnicalContact } from '../components/TechnicalContact';
import { AlertsPanel } from '../components/AlertsPanel';

export function Dashboard() {
  return (
    <>
      {/* Breadcrumb */}
      <Breadcrumb />

      {/* Circular Gauges Section */}
      <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 mb-6">
        <div className="flex items-center gap-2 mb-4">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            11
          </div>
          <h2 className="text-base font-semibold text-gray-900">설비 가동률</h2>
          <button className="ml-auto text-xs text-blue-600 hover:text-blue-700">+ 더보기</button>
        </div>
        <div className="grid grid-cols-5 lg:grid-cols-10 gap-6">
          <CircularGauge label="VCS" percentage={93} color="#14b8a6" />
          <CircularGauge label="AVC" percentage={98} color="#14b8a6" />
          <CircularGauge label="DGRC" percentage={91} color="#14b8a6" />
          <CircularGauge label="CCTV" percentage={89} color="#14b8a6" />
          <CircularGauge label="VMS" percentage={95} color="#14b8a6" />
          <CircularGauge label="VSL" percentage={100} color="#14b8a6" />
          <CircularGauge label="LCS" percentage={93} color="#14b8a6" />
          <CircularGauge label="긴급전화" percentage={100} color="#14b8a6" />
          <CircularGauge label="Ethernet허브" percentage={99} color="#14b8a6" />
          <CircularGauge label="추출기" percentage={90} color="#14b8a6" />
        </div>
      </div>

      {/* Middle Section */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 mb-6">
        {/* Left: Facility Breakdown Chart */}
        <div className="lg:col-span-5">
          <FacilityBreakdownChart />
        </div>

        {/* Center: Donut Chart */}
        <div className="lg:col-span-3">
          <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 h-full">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
                4
              </div>
              <h2 className="text-sm font-semibold text-gray-900">설비 노후화 현황</h2>
              <span className="text-xs text-gray-500">Gui</span>
              <button className="ml-auto text-xs text-blue-600 hover:text-blue-700">+ 더보기</button>
            </div>
            <div className="flex justify-center">
              <DonutChart 
                percentage={67.7} 
                value="28,434" 
                label="전체" 
              />
            </div>
            <div className="mt-3 text-center text-sm text-gray-600">
              100동탄 <span className="font-semibold text-blue-600">19,824</span>
            </div>
          </div>
        </div>

        {/* Right: Status Tables */}
        <div className="lg:col-span-4 space-y-4">
          <FacilityStatusTable />
          <TotalFacilityTable />
        </div>
      </div>

      {/* Bottom Section */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 mb-6">
        {/* Technical Contact */}
        <div className="lg:col-span-9">
          <TechnicalContact />
        </div>

        {/* Alerts */}
        <div className="lg:col-span-3">
          <AlertsPanel />
        </div>
      </div>
    </>
  );
}
