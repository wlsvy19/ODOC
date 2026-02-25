import { Search, Filter, Plus, Download } from 'lucide-react';

interface FacilityItem {
  id: string;
  code: string;
  name: string;
  type: string;
  location: string;
  status: string;
  installDate: string;
  lastCheck: string;
}

const facilities: FacilityItem[] = [
  { id: '1', code: 'VCS-001', name: 'VCS 시스템', type: 'VCS', location: '경부선 45km', status: '정상', installDate: '2020-03-15', lastCheck: '2027-02-18' },
  { id: '2', code: 'CCTV-234', name: 'CCTV #234', type: 'CCTV', location: '영동선 23km', status: '정상', installDate: '2019-08-22', lastCheck: '2027-02-19' },
  { id: '3', code: 'VMS-089', name: '전광판 VMS', type: 'VMS', location: '서해안선 67km', status: '점검중', installDate: '2021-01-10', lastCheck: '2027-02-15' },
  { id: '4', code: 'TEL-456', name: '긴급전화', type: '긴급전화', location: '중부선 12km', status: '정상', installDate: '2018-05-30', lastCheck: '2027-02-20' },
  { id: '5', code: 'AVC-123', name: 'AVC 시스템', type: 'AVC', location: '남해선 89km', status: '고장', installDate: '2020-11-05', lastCheck: '2027-02-17' },
  { id: '6', code: 'DGRC-067', name: 'DGRC 설비', type: 'DGRC', location: '경부선 120km', status: '정상', installDate: '2019-03-18', lastCheck: '2027-02-19' },
  { id: '7', code: 'VSL-234', name: 'VSL 시스템', type: 'VSL', location: '영동선 56km', status: '정상', installDate: '2021-07-22', lastCheck: '2027-02-18' },
  { id: '8', code: 'LCS-445', name: 'LCS 제어', type: 'LCS', location: '서해안선 34km', status: '정상', installDate: '2020-09-14', lastCheck: '2027-02-20' },
];

export function FacilityManagement() {
  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">설비관리</h1>
          <p className="text-sm text-gray-600 mt-1">전체 ITS 설비를 관리하고 모니터링합니다</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          <Plus className="w-4 h-4" />
          신규 설비 등록
        </button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">전체 설비</div>
          <div className="text-2xl font-bold text-gray-900">2,367<span className="text-base font-normal text-gray-600 ml-1">대</span></div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">정상 가동</div>
          <div className="text-2xl font-bold text-green-600">2,298<span className="text-base font-normal text-gray-600 ml-1">대</span></div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">점검중</div>
          <div className="text-2xl font-bold text-yellow-600">45<span className="text-base font-normal text-gray-600 ml-1">대</span></div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">고장</div>
          <div className="text-2xl font-bold text-red-600">24<span className="text-base font-normal text-gray-600 ml-1">대</span></div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200 mb-6">
        <div className="flex items-center gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input
              type="text"
              placeholder="설비코드, 설비명, 위치로 검색"
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 유형</option>
            <option>VCS</option>
            <option>CCTV</option>
            <option>VMS</option>
            <option>긴급전화</option>
          </select>
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 상태</option>
            <option>정상</option>
            <option>점검중</option>
            <option>고장</option>
          </select>
          <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            <Filter className="w-4 h-4" />
            필터
          </button>
          <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            <Download className="w-4 h-4" />
            내보내기
          </button>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비코드</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비명</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">유형</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">위치</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">상태</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">설치일자</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">최종점검</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">관리</th>
            </tr>
          </thead>
          <tbody>
            {facilities.map((facility) => (
              <tr key={facility.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900 font-medium">{facility.code}</td>
                <td className="py-3 px-4 text-sm text-gray-900">{facility.name}</td>
                <td className="py-3 px-4 text-sm text-gray-600">{facility.type}</td>
                <td className="py-3 px-4 text-sm text-gray-600">{facility.location}</td>
                <td className="py-3 px-4 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      facility.status === '정상'
                        ? 'bg-green-100 text-green-700'
                        : facility.status === '점검중'
                        ? 'bg-yellow-100 text-yellow-700'
                        : 'bg-red-100 text-red-700'
                    }`}
                  >
                    {facility.status}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{facility.installDate}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{facility.lastCheck}</td>
                <td className="py-3 px-4 text-center">
                  <button className="text-blue-600 hover:text-blue-700 text-sm">상세보기</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
