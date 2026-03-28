interface TrafficMetricProps {
  label: string;
  value: string;
  unit: string;
  trend?: 'up' | 'down';
}

export function TrafficMetric({ label, value, unit, trend }: TrafficMetricProps) {
  return (
    <div className="bg-white rounded-lg p-3 shadow-sm border border-gray-200">
      <div className="text-xs text-gray-600 mb-1">{label}</div>
      <div className="flex items-baseline gap-1">
        <span className="text-2xl font-bold text-gray-900">{value}</span>
        <span className="text-sm text-gray-600">{unit}</span>
        {trend && (
          <span className={`text-xs ml-1 ${trend === 'up' ? 'text-red-600' : 'text-green-600'}`}>
            {trend === 'up' ? '↑' : '↓'}
          </span>
        )}
      </div>
    </div>
  );
}
