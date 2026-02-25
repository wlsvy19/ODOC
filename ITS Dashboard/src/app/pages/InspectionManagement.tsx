import { Calendar, CheckSquare, Clock } from 'lucide-react';

interface Inspection {
  id: string;
  code: string;
  facility: string;
  type: string;
  inspector: string;
  scheduledDate: string;
  status: string;
  lastInspection: string;
}

const inspections: Inspection[] = [
  { id: '1', code: 'INS-2027-0156', facility: 'VCS-001', type: '정기점검', inspector: '김철수', scheduledDate: '2027-02-25', status: '예정', lastInspection: '2027-01-25' },
  { id: '2', code: 'INS-2027-0155', facility: 'CCTV-234', type: '정기점검', inspector: '이영희', scheduledDate: '2027-02-22', status: '예정', lastInspection: '2027-01-22' },
  { id: '3', code: 'INS-2027-0154', facility: 'VMS-089', type: '긴급점검', inspector: '박민수', scheduledDate: '2027-02-20', status: '완료', lastInspection: '2027-02-20' },
  { id: '4', code: 'INS-2027-0153', facility: 'TEL-456', type: '정기점검', inspector: '최지우', scheduledDate: '2027-02-21', status: '진행중', lastInspection: '2027-01-21' },
  { id: '5', code: 'INS-2027-0152', facility: 'AVC-123', type: '긴급점검', inspector: '정민호', scheduledDate: '2027-02-20', status: '완료', lastInspection: '2027-02-20' },
  { id: '6', code: 'INS-2027-0151', facility: 'DGRC-067', type: '정기점검', inspector: '강서연', scheduledDate: '2027-02-23', status: '예정', lastInspection: '2027-01-23' },
];

export function InspectionManagement() {
  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">점검관리</h1>
        <p className="text-sm text-gray-600 mt-1">설비 정기점검 및 긴급점검을 관리합니다</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Calendar className="w-6 h-6 text-blue-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">금주 예정</div>
              <div className="text-2xl font-bold text-blue-600">24<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <Clock className="w-6 h-6 text-yellow-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">진행중</div>
              <div className="text-2xl font-bold text-yellow-600">3<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-green-100 rounded-lg">
              <CheckSquare className="w-6 h-6 text-green-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">금월 완료</div>
              <div className="text-2xl font-bold text-green-600">87<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-red-100 rounded-lg">
              <Calendar className="w-6 h-6 text-red-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">지연</div>
              <div className="text-2xl font-bold text-red-600">2<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
      </div>

      {/* Calendar View */}
      <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 mb-6">
        <h3 className="text-base font-semibold text-gray-900 mb-4">금주 점검 일정</h3>
        <div className="grid grid-cols-7 gap-2">
          {['월', '화', '수', '목', '금', '토', '일'].map((day, index) => (
            <div key={day} className="text-center">
              <div className="text-sm font-medium text-gray-700 mb-2">{day}</div>
              <div
                className={`p-4 rounded-lg border ${
                  index === 4 ? 'bg-blue-50 border-blue-300' : 'bg-gray-50 border-gray-200'
                }`}
              >
                <div className="text-lg font-semibold text-gray-900">{17 + index}</div>
                {index === 4 && (
                  <div className="mt-2 text-xs">
                    <div className="px-2 py-1 bg-blue-600 text-white rounded mb-1">점검 2건</div>
                  </div>
                )}
                {index === 5 && (
                  <div className="mt-2 text-xs">
                    <div className="px-2 py-1 bg-blue-600 text-white rounded mb-1">점검 3건</div>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Inspection Table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <div className="p-4 border-b border-gray-200">
          <h3 className="text-base font-semibold text-gray-900">점검 현황</h3>
        </div>
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">점검번호</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">점검유형</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">담당자</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">예정일</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">상태</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">최종점검일</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">관리</th>
            </tr>
          </thead>
          <tbody>
            {inspections.map((inspection) => (
              <tr key={inspection.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900 font-medium">{inspection.code}</td>
                <td className="py-3 px-4 text-sm text-gray-900">{inspection.facility}</td>
                <td className="py-3 px-4 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      inspection.type === '긴급점검'
                        ? 'bg-red-100 text-red-700'
                        : 'bg-blue-100 text-blue-700'
                    }`}
                  >
                    {inspection.type}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{inspection.inspector}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{inspection.scheduledDate}</td>
                <td className="py-3 px-4 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      inspection.status === '완료'
                        ? 'bg-green-100 text-green-700'
                        : inspection.status === '진행중'
                        ? 'bg-yellow-100 text-yellow-700'
                        : 'bg-gray-100 text-gray-700'
                    }`}
                  >
                    {inspection.status}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{inspection.lastInspection}</td>
                <td className="py-3 px-4 text-center">
                  <button className="text-blue-600 hover:text-blue-700 text-sm">상세</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
