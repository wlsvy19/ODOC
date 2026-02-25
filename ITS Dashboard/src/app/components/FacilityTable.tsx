interface FacilityData {
  id: string;
  name: string;
  status: string;
  power: string;
  efficiency: string;
}

const facilities: FacilityData[] = [
  { id: '1', name: 'HVAC시스템', status: '정상', power: '12,345 kW', efficiency: '94%' },
  { id: '2', name: '조명시스템', status: '정상', power: '8,234 kW', efficiency: '92%' },
  { id: '3', name: '승강기시스템', status: '정상', power: '3,456 kW', efficiency: '88%' },
  { id: '4', name: 'IT설비', status: '점검', power: '5,678 kW', efficiency: '90%' },
  { id: '5', name: '기타설비', status: '정상', power: '2,123 kW', efficiency: '85%' },
];

export function FacilityTable() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <h3 className="text-sm font-semibold text-gray-900 mb-3">설비 사용 현황</h3>
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-gray-200">
              <th className="text-left py-2 px-3 font-semibold text-gray-700">설비명</th>
              <th className="text-center py-2 px-3 font-semibold text-gray-700">상태</th>
              <th className="text-right py-2 px-3 font-semibold text-gray-700">전력</th>
              <th className="text-right py-2 px-3 font-semibold text-gray-700">효율</th>
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
                <td className="py-2 px-3 text-right text-gray-900">{facility.power}</td>
                <td className="py-2 px-3 text-right text-gray-900">{facility.efficiency}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
