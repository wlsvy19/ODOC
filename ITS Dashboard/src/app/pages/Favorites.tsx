import { Star, Trash2 } from 'lucide-react';

interface Bookmark {
  id: string;
  name: string;
  type: string;
  facility: string;
  location: string;
  addedDate: string;
}

const bookmarks: Bookmark[] = [
  { id: '1', name: 'VMS 모니터링', type: '설비', facility: 'VMS-089', location: '서해안선 67km', addedDate: '2027-02-15' },
  { id: '2', name: 'CCTV 영상확인', type: '설비', facility: 'CCTV-234', location: '영동선 23km', addedDate: '2027-02-10' },
  { id: '3', name: '긴급 장애처리', type: '장애', facility: 'TEL-456', location: '중부선 12km', addedDate: '2027-02-08' },
  { id: '4', name: '정기점검 예정', type: '점검', facility: 'VCS-001', location: '경부선 45km', addedDate: '2027-02-05' },
];

export function Favorites() {
  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">관심관리</h1>
        <p className="text-sm text-gray-600 mt-1">자주 사용하는 설비와 작업을 관리합니다</p>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <Star className="w-6 h-6 text-yellow-600" />
            </div>
            <div>
              <div className="text-sm text-gray-600">관심 설비</div>
              <div className="text-2xl font-bold text-gray-900">12<span className="text-base font-normal text-gray-600 ml-1">개</span></div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">관심 장애</div>
          <div className="text-2xl font-bold text-gray-900">3<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
        </div>
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
          <div className="text-sm text-gray-600 mb-1">관심 점검</div>
          <div className="text-2xl font-bold text-gray-900">5<span className="text-base font-normal text-gray-600 ml-1">건</span></div>
        </div>
      </div>

      {/* Bookmarks List */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden mb-6">
        <div className="p-4 border-b border-gray-200">
          <h3 className="text-base font-semibold text-gray-900">즐겨찾기 목록</h3>
        </div>
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">이름</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">분류</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">설비</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">위치</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">등록일</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">관리</th>
            </tr>
          </thead>
          <tbody>
            {bookmarks.map((bookmark) => (
              <tr key={bookmark.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-900 font-medium flex items-center gap-2">
                  <Star className="w-4 h-4 text-yellow-500 fill-yellow-500" />
                  {bookmark.name}
                </td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded text-xs font-medium">
                    {bookmark.type}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-900">{bookmark.facility}</td>
                <td className="py-3 px-4 text-sm text-gray-600">{bookmark.location}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{bookmark.addedDate}</td>
                <td className="py-3 px-4 text-center">
                  <div className="flex items-center justify-center gap-2">
                    <button className="text-blue-600 hover:text-blue-700 text-sm">바로가기</button>
                    <button className="text-red-600 hover:text-red-700">
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Quick Access Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-pointer">
          <div className="flex items-center gap-3 mb-4">
            <Star className="w-8 h-8 text-yellow-500 fill-yellow-500" />
            <h3 className="text-lg font-semibold text-gray-900">중요 설비</h3>
          </div>
          <p className="text-sm text-gray-600 mb-3">자주 확인하는 핵심 설비 4개</p>
          <button className="text-blue-600 hover:text-blue-700 text-sm font-medium">바로가기 →</button>
        </div>

        <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-pointer">
          <div className="flex items-center gap-3 mb-4">
            <Star className="w-8 h-8 text-yellow-500 fill-yellow-500" />
            <h3 className="text-lg font-semibold text-gray-900">진행중 작업</h3>
          </div>
          <p className="text-sm text-gray-600 mb-3">현재 담당하고 있는 작업 3건</p>
          <button className="text-blue-600 hover:text-blue-700 text-sm font-medium">바로가기 →</button>
        </div>

        <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-pointer">
          <div className="flex items-center gap-3 mb-4">
            <Star className="w-8 h-8 text-yellow-500 fill-yellow-500" />
            <h3 className="text-lg font-semibold text-gray-900">예정 점검</h3>
          </div>
          <p className="text-sm text-gray-600 mb-3">이번주 예정된 점검 5건</p>
          <button className="text-blue-600 hover:text-blue-700 text-sm font-medium">바로가기 →</button>
        </div>
      </div>
    </div>
  );
}
