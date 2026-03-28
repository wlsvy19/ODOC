interface ITSFacilityData {
  id: string;
  name: string;
  status: string;
  count: string;
  availability: string;
}

const facilities: ITSFacilityData[] = [
  { id: '1', name: 'CCTV', status: '정상', count: '1,234대', availability: '92%' },
  { id: '2', name: 'VMS(전광판)', status: '정상', count: '456대', availability: '88%' },
  { id: '3', name: '하이패스', status: '정상', count: '234대', availability: '95%' },
  { id: '4', name: '긴급전화', status: '점검', count: '567대', availability: '90%' },
  { id: '5', name: '기상감지기', status: '정상', count: '123대', availability: '85%' },
];

export function ITSFacilityTable() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <h3 className="text-sm font-semibold text-gray-900 mb-3">ITS 설비 현황</h3>
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-gray-200">
              <th className="text-left py-2 px-3 font-semibold text-gray-700">설비명</th>
              <th className="text-center py-2 px-3 font-semibold text-gray-700">상태</th>
              <th className="text-right py-2 px-3 font-semibold text-gray-700">수량</th>
              <th className="text-right py-2 px-3 font-semibold text-gray-700">가동률</th>
            </tr>
          </thead>
          <tbody>
            {facilities.map((facility) => (
              <tr key={facility.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-2 px-3 text-gray-900">{facility.name}</td>
                <td className="py-2 px-3 text-center">
                  <span
                    className={`inline-block px-2 py-1 rounded text-xs font-medium ${
                      facility.status === '정상'
                        ? 'bg-green-100 text-green-700'
                        : 'bg-yellow-100 text-yellow-700'
                    }`}
                  >
                    {facility.status}
                  </span>
                </td>
                <td className="py-2 px-3 text-right text-gray-900">{facility.count}</td>
                <td className="py-2 px-3 text-right text-gray-900">{facility.availability}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
