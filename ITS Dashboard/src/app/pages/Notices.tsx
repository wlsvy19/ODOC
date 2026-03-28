import { Bell, AlertCircle } from 'lucide-react';

interface Notice {
  id: string;
  title: string;
  category: string;
  date: string;
  views: number;
  important: boolean;
}

const notices: Notice[] = [
  { id: '1', title: '2027년 2월 정기점검 일정 안내', category: '점검', date: '2027-02-20', views: 145, important: true },
  { id: '2', title: 'ITS 시스템 업그레이드 공지', category: '시스템', date: '2027-02-19', views: 234, important: true },
  { id: '3', title: '설비 관리 매뉴얼 업데이트', category: '자료', date: '2027-02-18', views: 89, important: false },
  { id: '4', title: '긴급 연락망 변경 안내', category: '일반', date: '2027-02-17', views: 167, important: false },
  { id: '5', title: '동계 설비 관리 주의사항', category: '안전', date: '2027-02-15', views: 201, important: true },
  { id: '6', title: '장애 대응 프로세스 개선 안내', category: '프로세스', date: '2027-02-14', views: 122, important: false },
  { id: '7', title: '신규 설비 도입 계획', category: '계획', date: '2027-02-12', views: 178, important: false },
  { id: '8', title: '월간 설비 운영 보고서', category: '보고', date: '2027-02-10', views: 156, important: false },
];

export function Notices() {
  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">공지사항</h1>
        <p className="text-sm text-gray-600 mt-1">시스템 운영 관련 중요 공지를 확인하세요</p>
      </div>

      {/* Important Notices */}
      <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
        <div className="flex items-center gap-2 mb-3">
          <Bell className="w-5 h-5 text-yellow-600" />
          <h3 className="text-sm font-semibold text-gray-900">중요 공지</h3>
        </div>
        <div className="space-y-2">
          {notices
            .filter((notice) => notice.important)
            .map((notice) => (
              <div key={notice.id} className="flex items-center gap-2 text-sm">
                <AlertCircle className="w-4 h-4 text-red-600 flex-shrink-0" />
                <a href="#" className="text-gray-900 hover:text-blue-600 flex-1">
                  {notice.title}
                </a>
                <span className="text-gray-500 text-xs">{notice.date}</span>
              </div>
            ))}
        </div>
      </div>

      {/* Search and Filter */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200 mb-6">
        <div className="flex items-center gap-4">
          <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option>전체 카테고리</option>
            <option>점검</option>
            <option>시스템</option>
            <option>안전</option>
            <option>일반</option>
          </select>
          <input
            type="text"
            placeholder="제목으로 검색"
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            검색
          </button>
        </div>
      </div>

      {/* Notices Table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-20">번호</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-24">분류</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">제목</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-32">작성일</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-24">조회수</th>
            </tr>
          </thead>
          <tbody>
            {notices.map((notice, index) => (
              <tr key={notice.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{notices.length - index}</td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded text-xs font-medium">
                    {notice.category}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-900">
                  <a href="#" className="hover:text-blue-600 flex items-center gap-2">
                    {notice.important && <AlertCircle className="w-4 h-4 text-red-600" />}
                    {notice.title}
                  </a>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{notice.date}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{notice.views}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex items-center justify-center gap-2 mt-6">
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">이전</button>
        <button className="px-3 py-1 bg-blue-600 text-white rounded">1</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">2</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">3</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">4</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">5</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">다음</button>
      </div>
    </div>
  );
}
