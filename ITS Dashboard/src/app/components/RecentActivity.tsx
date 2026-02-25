import { ShoppingCart, UserPlus, Package, AlertCircle } from 'lucide-react';

interface Activity {
  id: string;
  type: 'order' | 'user' | 'delivery' | 'alert';
  title: string;
  time: string;
  description: string;
}

const activities: Activity[] = [
  {
    id: '1',
    type: 'order',
    title: '새로운 주문',
    time: '5분 전',
    description: '김철수 고객님이 제품 3개를 주문했습니다.',
  },
  {
    id: '2',
    type: 'user',
    title: '신규 회원 가입',
    time: '15분 전',
    description: '이영희님이 회원가입을 완료했습니다.',
  },
  {
    id: '3',
    type: 'delivery',
    title: '배송 완료',
    time: '1시간 전',
    description: '주문번호 #12345의 배송이 완료되었습니다.',
  },
  {
    id: '4',
    type: 'alert',
    title: '재고 부족 경고',
    time: '2시간 전',
    description: '노트북 상품의 재고가 10개 이하입니다.',
  },
];

const iconMap = {
  order: ShoppingCart,
  user: UserPlus,
  delivery: Package,
  alert: AlertCircle,
};

const colorMap = {
  order: 'bg-blue-50 text-blue-600',
  user: 'bg-green-50 text-green-600',
  delivery: 'bg-purple-50 text-purple-600',
  alert: 'bg-orange-50 text-orange-600',
};

export function RecentActivity() {
  return (
    <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
      <h3 className="text-lg font-semibold text-gray-900 mb-4">최근 활동</h3>
      <div className="space-y-4">
        {activities.map((activity) => {
          const Icon = iconMap[activity.type];
          return (
            <div key={activity.id} className="flex items-start gap-3">
              <div className={`p-2 rounded-lg ${colorMap[activity.type]}`}>
                <Icon className="w-4 h-4" />
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-center justify-between mb-1">
                  <p className="font-medium text-gray-900">{activity.title}</p>
                  <span className="text-xs text-gray-500">{activity.time}</span>
                </div>
                <p className="text-sm text-gray-600">{activity.description}</p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
