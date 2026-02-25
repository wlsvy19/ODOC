interface Alert {
  id: string;
  title: string;
  badge: string;
}

const alerts: Alert[] = [
  { id: '1', title: '대전영신0650-0368S', badge: '관리중' },
  { id: '2', title: '천안논산0650-0426S', badge: '관리중' },
  { id: '3', title: '영남내륙8845-5884V', badge: '관리중' },
];

const oldFacilities: Alert[] = [
  { id: '1', title: '대전영신103450-3200', badge: '관리중' },
  { id: '2', title: '천안논산4I0225-3684S', badge: '관리중' },
  { id: '3', title: '영남내륙0650-0456S', badge: '관리중' },
];

export function AlertsPanel() {
  return (
    <div className="space-y-4">
      {/* Today's Alerts */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
        <div className="flex items-center gap-2 mb-3">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            7
          </div>
          <h3 className="text-sm font-semibold text-gray-900">오늘 알림</h3>
        </div>
        <div className="space-y-2">
          <div className="flex items-center justify-between text-xs">
            <span className="text-gray-700">▶ 금일 보수 완료</span>
          </div>
          {alerts.map((alert) => (
            <div key={alert.id} className="flex items-center gap-2 py-1.5">
              <span className="text-gray-600 text-xs">▪</span>
              <span className="text-xs text-gray-900 flex-1">{alert.title}</span>
              <span className="px-2 py-0.5 bg-blue-600 text-white text-xs rounded">
                {alert.badge}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* Old Facility Alarms */}
      <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
        <div className="flex items-center gap-2 mb-3">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            7
          </div>
          <h3 className="text-sm font-semibold text-gray-900">우선 설비 알람</h3>
        </div>
        <div className="space-y-2">
          <div className="flex items-center justify-between text-xs">
            <span className="text-gray-700">▶ 금일 교체일자</span>
          </div>
          {oldFacilities.map((facility) => (
            <div key={facility.id} className="flex items-center gap-2 py-1.5">
              <span className="text-gray-600 text-xs">▪</span>
              <span className="text-xs text-gray-900 flex-1">{facility.title}</span>
              <span className="px-2 py-0.5 bg-blue-600 text-white text-xs rounded">
                {facility.badge}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
