import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const data = [
  { time: '00:00', route1: 450, route2: 380, route3: 520, route4: 410, route5: 470 },
  { time: '03:00', route1: 280, route2: 250, route3: 320, route4: 290, route5: 310 },
  { time: '06:00', route1: 820, route2: 750, route3: 880, route4: 790, route5: 850 },
  { time: '09:00', route1: 1200, route2: 1150, route3: 1280, route4: 1100, route5: 1220 },
  { time: '12:00', route1: 950, route2: 900, route3: 1020, route4: 880, route5: 970 },
  { time: '15:00', route1: 1100, route2: 1050, route3: 1180, route4: 1020, route5: 1140 },
  { time: '18:00', route1: 1350, route2: 1280, route3: 1420, route4: 1250, route5: 1380 },
  { time: '21:00', route1: 780, route2: 720, route3: 850, route4: 700, route5: 810 },
  { time: '24:00', route1: 520, route2: 480, route3: 580, route4: 460, route5: 540 },
];

export function TrafficLineChart() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-sm font-semibold text-gray-900">교통량 추이 (시간대별)</h3>
        <span className="text-xs text-gray-500">단위: 대/시간</span>
      </div>
      <ResponsiveContainer width="100%" height={200}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis 
            dataKey="time" 
            stroke="#6b7280" 
            style={{ fontSize: '11px' }}
          />
          <YAxis 
            stroke="#6b7280" 
            style={{ fontSize: '11px' }}
          />
          <Tooltip 
            contentStyle={{ 
              backgroundColor: 'white', 
              border: '1px solid #e5e7eb',
              borderRadius: '8px',
              fontSize: '12px'
            }}
          />
          <Legend 
            wrapperStyle={{ fontSize: '11px' }}
          />
          <Line 
            type="monotone" 
            dataKey="route1" 
            stroke="#ef4444" 
            strokeWidth={2}
            name="경부선"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="route2" 
            stroke="#3b82f6" 
            strokeWidth={2}
            name="영동선"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="route3" 
            stroke="#10b981" 
            strokeWidth={2}
            name="서해안선"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="route4" 
            stroke="#f59e0b" 
            strokeWidth={2}
            name="중부선"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="route5" 
            stroke="#8b5cf6" 
            strokeWidth={2}
            name="남해선"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
