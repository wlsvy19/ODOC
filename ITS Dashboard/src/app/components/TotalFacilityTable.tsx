interface TotalFacilityData {
  id: string;
  no: string;
  section: string;
  type: string;
  count: string;
  location: string;
}

const totalFacilities: TotalFacilityData[] = [
  { id: '1', no: '1', section: '중부고속', type: 'FTMS', count: '03456-3200', location: '본선' },
  { id: '2', no: '2', section: '제주', type: 'FTMS', count: '03456-3200', location: '만수대' },
  { id: '3', no: '3', section: '중부', type: 'FTMS', count: '0370-32655', location: '만수대' },
  { id: '4', no: '4', section: '강릉', type: 'FTMS', count: '0907-3123S', location: '전북' },
  { id: '5', no: '5', section: '영산', type: 'TTMS', count: '56875-3982D', location: '전북' },
  { id: '6', no: '6', section: '대전예산', type: 'FTMS', count: '0225-3684S', location: '만수대' },
  { id: '7', no: '7', section: '전주', type: 'FTMS', count: '9968-5456S', location: '인천청' },
];

export function TotalFacilityTable() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center gap-2">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            51
          </div>
          <h3 className="text-sm font-semibold text-gray-900">전체 고장내역 현황</h3>
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
              <th className="text-center py-2 px-2 font-semibold text-gray-700">기간분류</th>
              <th className="text-center py-2 px-2 font-semibold text-gray-700">전북정보시설</th>
            </tr>
          </thead>
          <tbody>
            {totalFacilities.map((facility) => (
              <tr key={facility.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-2 px-2 text-center text-gray-900">{facility.no}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.section}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.type}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.count}</td>
                <td className="py-2 px-2 text-center text-gray-900">{facility.location}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
