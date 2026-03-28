import { AlertTriangle, Clock, CheckCircle, XCircle } from 'lucide-react';

interface Incident {
  id: string;
  code: string;
  facility: string;
  type: string;
  description: string;
  status: string;
  priority: string;
  reportTime: string;
  assignee: string;
}

const incidents: Incident[] = [
  { id: '1', code: 'INC-2027-0234', facility: 'VMS-089', type: 'VMS', description: '화면 표시 불량', status: '처리중', priority: '높음', reportTime: '2027-02-20 09:15', assignee: '김철수' },
  { id: '2', code: 'INC-2027-0233', facility: 'AVC-123', type: 'AVC', description: '통신 두절', status: '접수', priority: '긴급', reportTime: '2027-02-20 08:30', assignee: '이영희' },
  { id: '3', code: 'INC-2027-0232', facility: 'CCTV-145', type: 'CCTV', description: '영상 끊김 현상', status: '완료', priority: '보통', reportTime: '2027-02-19 16:45', assignee: '박민수' },
  { id: '4', code: 'INC-2027-0231', facility: 'TEL-678', type: '긴급전화', description: '수신 불가', status: '처리중', priority: '긴급', reportTime: '2027-02-19 14:20', assignee: '최지우' },
  { id: '5', code: 'INC-2027-0230', facility: 'VCS-045', type: 'VCS', description: '차량 검지 오류', status: '접수', priority: '높음', reportTime: '2027-02-19 11:30', assignee: '정민호' },
];

export function IncidentManagement() {
  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">장애관리</h1>
        <p className="text-sm text-gray-600 mt-1">설비 장애를 신속하게 처리하고 관리합니다</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-red-100 rounded-lg">
              <AlertTriangle className="w-6 h-6 text-red-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">긴급</div>
              <div className="text-2xl font-bold text-red-600">2<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <Clock className="w-6 h-6 text-yellow-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">처리중</div>
              <div className="text-2xl font-bold text-yellow-600">12<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Clock className="w-6 h-6 text-blue-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">접수</div>
              <div className="text-2xl font-bold text-blue-600">8<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-green-100 rounded-lg">
              <CheckCircle className="w-6 h-6 text-green-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">금일 완료</div>
              <div className="text-2xl font-bold text-green-600">15<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
            </div>
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 상태</option>
            <option>접수</option>
            <option>처리중</option>
            <option>완료</option>
          </select>
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 우선순위</option>
            <option>긴급</option>
            <option>높음</option>
            <option>보통</option>
          </select>
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 설비 유형</option>
            <option>VMS</option>
            <option>CCTV</option>
            <option>긴급전화</option>
          </select>
          <input
            type="date"
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            defaultValue="2027-02-20"
          />
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            조회
          </button>
        </div>
      </div>

      {/* Incident List */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">장애번호</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">유형</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">장애내용</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">우선순위</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">상태</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">발생시간</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">담당자</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">조치</th>
            </tr>
          </thead>
          <tbody>
            {incidents.map((incident) => (
              <tr key={incident.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900 font-medium">{incident.code}</td>
                <td className="py-3 px-4 text-sm text-gray-900">{incident.facility}</td>
                <td className="py-3 px-4 text-sm text-gray-600">{incident.type}</td>
                <td className="py-3 px-4 text-sm text-gray-900">{incident.description}</td>
                <td className="py-3 px-4 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      incident.priority === '긴급'
                        ? 'bg-red-100 text-red-700'
                        : incident.priority === '높음'
                        ? 'bg-orange-100 text-orange-700'
                        : 'bg-gray-100 text-gray-700'
                    }`}
                  >
                    {incident.priority}
                  </span>
                </td>
                <td className="py-3 px-4 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      incident.status === '완료'
                        ? 'bg-green-100 text-green-700'
                        : incident.status === '처리중'
                        ? 'bg-yellow-100 text-yellow-700'
                        : 'bg-blue-100 text-blue-700'
                    }`}
                  >
                    {incident.status}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{incident.reportTime}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{incident.assignee}</td>
                <td className="py-3 px-4 text-center">
                  <button className="text-blue-600 hover:text-blue-700 text-sm">처리</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
