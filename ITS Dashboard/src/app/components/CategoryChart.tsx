import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const data = [
  { category: '전자제품', sales: 4000, returns: 240 },
  { category: '의류', sales: 3000, returns: 139 },
  { category: '식품', sales: 2000, returns: 980 },
  { category: '가구', sales: 2780, returns: 390 },
  { category: '도서', sales: 1890, returns: 480 },
  { category: '스포츠', sales: 2390, returns: 380 },
];

export function CategoryChart() {
  return (
    <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
      <h3 className="text-lg font-semibold text-gray-900 mb-4">카테고리별 판매</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis dataKey="category" stroke="#6b7280" />
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
          <Bar dataKey="sales" fill="#3b82f6" name="판매" radius={[8, 8, 0, 0]} />
          <Bar dataKey="returns" fill="#ef4444" name="반품" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
