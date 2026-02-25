import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const data = [
  { month: '1월', line1: 450, line2: 380, line3: 320, line4: 410, line5: 370, line6: 290 },
  { month: '2월', line1: 420, line2: 360, line3: 300, line4: 390, line5: 350, line6: 270 },
  { month: '3월', line1: 380, line2: 340, line3: 280, line4: 370, line5: 330, line6: 250 },
  { month: '4월', line1: 360, line2: 320, line3: 260, line4: 350, line5: 310, line6: 230 },
  { month: '5월', line1: 340, line2: 300, line3: 240, line4: 330, line5: 290, line6: 210 },
  { month: '6월', line1: 320, line2: 280, line3: 220, line4: 310, line5: 270, line6: 190 },
  { month: '7월', line1: 300, line2: 260, line3: 200, line4: 290, line5: 250, line6: 170 },
  { month: '8월', line1: 280, line2: 240, line3: 180, line4: 270, line5: 230, line6: 150 },
];

export function FacilityBreakdownChart() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center gap-2">
          <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
            11
          </div>
          <h3 className="text-sm font-semibold text-gray-900">설비 고장내역 통계</h3>
          <span className="text-xs text-gray-500">Best 11개</span>
        </div>
        <button className="text-xs text-blue-600 hover:text-blue-700">+ 더보기</button>
      </div>
      <ResponsiveContainer width="100%" height={240}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis 
            dataKey="month" 
            stroke="#6b7280" 
            style={{ fontSize: '10px' }}
          />
          <YAxis 
            stroke="#6b7280" 
            style={{ fontSize: '10px' }}
            domain={[0, 500]}
            ticks={[0, 100, 200, 300, 400, 500]}
          />
          <Tooltip 
            contentStyle={{ 
              backgroundColor: 'white', 
              border: '1px solid #e5e7eb',
              borderRadius: '8px',
              fontSize: '11px'
            }}
          />
          <Legend 
            wrapperStyle={{ fontSize: '10px' }}
            iconSize={8}
          />
          <Line 
            type="monotone" 
            dataKey="line1" 
            stroke="#ef4444" 
            strokeWidth={2}
            name="수도권"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line2" 
            stroke="#3b82f6" 
            strokeWidth={2}
            name="부산경남"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line3" 
            stroke="#10b981" 
            strokeWidth={2}
            name="대전중부"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line4" 
            stroke="#f59e0b" 
            strokeWidth={2}
            name="호남"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line5" 
            stroke="#8b5cf6" 
            strokeWidth={2}
            name="강릉"
            dot={false}
          />
          <Line 
            type="monotone" 
            dataKey="line6" 
            stroke="#ec4899" 
            strokeWidth={2}
            name="기타"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
