import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const data = [
  { month: '1월', revenue: 4000, profit: 2400 },
  { month: '2월', revenue: 3000, profit: 1398 },
  { month: '3월', revenue: 2000, profit: 9800 },
  { month: '4월', revenue: 2780, profit: 3908 },
  { month: '5월', revenue: 1890, profit: 4800 },
  { month: '6월', revenue: 2390, profit: 3800 },
  { month: '7월', revenue: 3490, profit: 4300 },
  { month: '8월', revenue: 4200, profit: 4100 },
  { month: '9월', revenue: 3800, profit: 3900 },
  { month: '10월', revenue: 4500, profit: 4400 },
  { month: '11월', revenue: 4800, profit: 4600 },
  { month: '12월', revenue: 5200, profit: 5000 },
];

export function RevenueChart() {
  return (
    <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
      <h3 className="text-lg font-semibold text-gray-900 mb-4">수익 추이</h3>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis dataKey="month" stroke="#6b7280" />
          <YAxis stroke="#6b7280" />
          <Tooltip 
            contentStyle={{ 
              backgroundColor: 'white', 
              border: '1px solid #e5e7eb',
              borderRadius: '8px',
              boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
            }}
          />
          <Legend />
          <Line 
            type="monotone" 
            dataKey="revenue" 
            stroke="#3b82f6" 
            strokeWidth={2}
            name="매출"
            dot={{ fill: '#3b82f6', r: 4 }}
            activeDot={{ r: 6 }}
          />
          <Line 
            type="monotone" 
            dataKey="profit" 
            stroke="#10b981" 
            strokeWidth={2}
            name="수익"
            dot={{ fill: '#10b981', r: 4 }}
            activeDot={{ r: 6 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
