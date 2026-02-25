import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend, PieChart, Pie, Cell } from 'recharts';

const monthlyData = [
  { month: '1월', incidents: 45, inspections: 120, repairs: 38 },
  { month: '2월', incidents: 52, inspections: 115, repairs: 42 },
  { month: '3월', incidents: 38, inspections: 125, repairs: 35 },
  { month: '4월', incidents: 48, inspections: 118, repairs: 40 },
  { month: '5월', incidents: 55, inspections: 122, repairs: 48 },
  { month: '6월', incidents: 42, inspections: 130, repairs: 36 },
];

const facilityBreakdown = [
  { name: 'CCTV', value: 145 },
  { name: 'VMS', value: 89 },
  { name: '긴급전화', value: 67 },
  { name: 'VCS', value: 52 },
  { name: '기타', value: 98 },
];

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

export function HistoryStatistics() {
  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">이력/통계관리</h1>
        <p className="text-sm text-gray-600 mt-1">설비 운영 이력과 통계를 분석합니다</p>
      </div>

      {/* Period Selection */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200 mb-6">
        <div className="flex items-center gap-4">
          <label className="text-sm font-medium text-gray-700">조회 기간:</label>
          <input
            type="date"
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            defaultValue="2027-01-01"
          />
          <span className="text-gray-600">~</span>
          <input
            type="date"
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            defaultValue="2027-02-20"
          />
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 노선</option>
            <option>경부선</option>
            <option>영동선</option>
            <option>서해안선</option>
          </select>
          <button className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            조회
          </button>
          <button className="ml-auto px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            엑셀 다운로드
          </button>
        </div>
      </div>

      {/* Summary Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">총 장애 건수</div>
          <div className="text-2xl font-bold text-gray-900">280<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
          <div className="text-xs text-red-600 mt-1">↑ 12% 전월 대비</div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">평균 처리 시간</div>
          <div className="text-2xl font-bold text-gray-900">2.4<span className="text-base font-normal text-gray-600 ml-1">시간</span></div>
          <div className="text-xs text-green-600 mt-1">↓ 8% 전월 대비</div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">정기 점검</div>
          <div className="text-2xl font-bold text-gray-900">720<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
          <div className="text-xs text-green-600 mt-1">완료율 98%</div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">설비 가동률</div>
          <div className="text-2xl font-bold text-gray-900">92.8<span className="text-base font-normal text-gray-600 ml-1">%</span></div>
          <div className="text-xs text-green-600 mt-1">↑ 1.2% 전월 대비</div>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        {/* Monthly Trend */}
        <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
          <h3 className="text-base font-semibold text-gray-900 mb-4">월별 추이</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
              <XAxis dataKey="month" stroke="#6b7280" style={{ fontSize: '12px' }} />
              <YAxis stroke="#6b7280" style={{ fontSize: '12px' }} />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'white',
                  border: '1px solid #e5e7eb',
                  borderRadius: '8px',
                }}
              />
              <Legend />
              <Line type="monotone" dataKey="incidents" stroke="#ef4444" strokeWidth={2} name="장애" />
              <Line type="monotone" dataKey="inspections" stroke="#3b82f6" strokeWidth={2} name="점검" />
              <Line type="monotone" dataKey="repairs" stroke="#10b981" strokeWidth={2} name="보수" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Facility Breakdown */}
        <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
          <h3 className="text-base font-semibold text-gray-900 mb-4">설비별 장애 분포</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={facilityBreakdown}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                outerRadius={100}
                fill="#8884d8"
                dataKey="value"
              >
                {facilityBreakdown.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Detailed History Table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <div className="p-4 border-b border-gray-200">
          <h3 className="text-base font-semibold text-gray-900">상세 이력</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">일자</th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">구분</th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비</th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">내용</th>
                <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">담당자</th>
                <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">상태</th>
              </tr>
            </thead>
            <tbody>
              <tr className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900">2027-02-20</td>
                <td className="py-3 px-4 text-sm text-gray-600">장애</td>
                <td className="py-3 px-4 text-sm text-gray-900">VMS-089</td>
                <td className="py-3 px-4 text-sm text-gray-900">화면 표시 불량 처리 완료</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">박민수</td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-green-100 text-green-700 rounded text-xs font-medium">완료</span>
                </td>
              </tr>
              <tr className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900">2027-02-20</td>
                <td className="py-3 px-4 text-sm text-gray-600">점검</td>
                <td className="py-3 px-4 text-sm text-gray-900">CCTV-145</td>
                <td className="py-3 px-4 text-sm text-gray-900">정기 점검 실시</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">이영희</td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-green-100 text-green-700 rounded text-xs font-medium">완료</span>
                </td>
              </tr>
              <tr className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900">2027-02-19</td>
                <td className="py-3 px-4 text-sm text-gray-600">보수</td>
                <td className="py-3 px-4 text-sm text-gray-900">TEL-678</td>
                <td className="py-3 px-4 text-sm text-gray-900">긴급전화 통신모듈 교체</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">최지우</td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-green-100 text-green-700 rounded text-xs font-medium">완료</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
