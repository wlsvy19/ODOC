interface FacilityStatus {
  id: string;
  section: string;
  facilityId: string;
  location: string;
  status: string;
}

const facilities: FacilityStatus[] = [
  { id: '1', section: '중부고속', facilityId: '03456-3200', location: '본선', status: '정상' },
  { id: '2', section: '제주', facilityId: 'FTMS', location: '만수대', status: '고장' },
  { id: '3', section: '중부', facilityId: '0370-32655', location: '만수대', status: '고장' },
  { id: '4', section: '강릉', facilityId: '0907-3123S', location: '전북', status: '정상' },
  { id: '5', section: '영산', facilityId: '56875-3982D', location: '전북', status: '정상' },
  { id: '6', section: '대전예산', facilityId: '0225-3684S', location: '만수대', status: '고장' },
  { id: '7', section: '전주', facilityId: '9968-5456S', location: '인천청', status: '정상' },
];

export function FacilityStatusTable() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center gap-2">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            11
          </div>
          <h3 className="text-sm font-semibold text-gray-900">설비 노후화 현황</h3>
        </div>
        <button className="text-xs text-blue-600 hover:text-blue-700">+ 더보기</button>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full text-xs">
          <thead>
            <tr className="border-b border-gray-200 bg-gray-50">
              <th className="text-center py-2 px-2 font-semibold text-gray-700">순번</th>
              <th className="text-center py-2 px-2 font-semibold text-gray-700">구간</th>
              <th className="text-center py-2 px-2 font-semibold text-gray-700">시설관리ID</th>
              <th className="text-center py-2 px-2 font-semibold text-gray-700">구간분류</th>
              <th className="text-center py-2 px-2 font-semibold text-gray-700">전북</th>
            </tr>
          </thead>
          <tbody>
            {facilities.map((facility, index) => (
              <tr key={facility.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-2 px-2 text-center text-gray-900">{index + 1}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.section}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.facilityId}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.location}</td>
                <td className="py-2 px-2 text-center">
                  <span
                    className={`inline-block px-2 py-0.5 rounded text-xs font-medium ${
                      facility.status === '정상'
                        ? 'bg-blue-100 text-blue-700'
                        : 'bg-red-100 text-red-700'
                    }`}
                  >
                    {facility.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
