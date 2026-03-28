import { Home, ChevronRight } from 'lucide-react';

export function Breadcrumb() {
  return (
    <div className="flex items-center gap-2 text-sm text-gray-600 mb-4">
      <Home className="w-4 h-4" />
      <span>Main</span>
      <ChevronRight className="w-4 h-4" />
      <span>대시보드</span>
      <ChevronRight className="w-4 h-4" />
      <span className="text-gray-900 font-medium">통합상황 모니터링</span>
    </div>
  );
}
