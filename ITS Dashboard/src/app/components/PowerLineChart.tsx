import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const data = [
  { time: '00:00', line1: 20, line2: 25, line3: 22, line4: 28, line5: 18 },
  { time: '03:00', line1: 18, line2: 22, line3: 20, line4: 25, line5: 16 },
  { time: '06:00', line1: 25, line2: 30, line3: 28, line4: 32, line5: 23 },
  { time: '09:00', line1: 35, line2: 40, line3: 38, line4: 42, line5: 33 },
  { time: '12:00', line1: 45, line2: 48, line3: 46, line4: 50, line5: 42 },
  { time: '15:00', line1: 40, line2: 43, line3: 41, line4: 45, line5: 38 },
  { time: '18:00', line1: 38, line2: 41, line3: 39, line4: 43, line5: 36 },
  { time: '21:00', line1: 30, line2: 33, line3: 31, line4: 35, line5: 28 },
  { time: '24:00', line1: 22, line2: 25, line3: 23, line4: 27, line5: 20 },
];

export function PowerLineChart() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-sm font-semibold text-gray-900">전력 사용량 추이 (시간대별)</h3>
        <span className="text-xs text-gray-500">단위: kW</span>
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
            dataKey="line1" 
            stroke="#ef4444" 
            strokeWidth={2}
            name="A동"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line2" 
            stroke="#3b82f6" 
            strokeWidth={2}
            name="B동"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line3" 
            stroke="#10b981" 
            strokeWidth={2}
            name="C동"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line4" 
            stroke="#f59e0b" 
            strokeWidth={2}
            name="D동"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line5" 
            stroke="#8b5cf6" 
            strokeWidth={2}
            name="E동"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
