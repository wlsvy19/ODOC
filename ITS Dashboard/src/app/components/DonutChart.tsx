interface DonutChartProps {
  percentage: number;
  value: string;
  label: string;
}

export function DonutChart({ percentage, value, label }: DonutChartProps) {
  const radius = 80;
  const circumference = 2 * Math.PI * radius;
  const strokeDashoffset = circumference - (percentage / 100) * circumference;

  return (
    <div className="flex flex-col items-center">
      <div className="relative w-64 h-64">
        <svg className="w-full h-full transform -rotate-90">
          {/* Background circle */}
          <circle
            cx="128"
            cy="128"
            r={radius}
            stroke="#e5e7eb"
            strokeWidth="24"
            fill="none"
          />
          {/* Progress circle */}
          <circle
            cx="128"
            cy="128"
            r={radius}
            stroke="#1e40af"
            strokeWidth="24"
            fill="none"
            strokeDasharray={circumference}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            className="transition-all duration-500"
          />
        </svg>
        <div className="absolute inset-0 flex flex-col items-center justify-center">
          <div className="text-sm text-gray-600 mb-1">{label}</div>
          <div className="text-4xl font-bold text-gray-900">{value}</div>
          <div className="text-2xl font-semibold text-blue-600 mt-1">{percentage}%</div>
        </div>
      </div>
    </div>
  );
}
