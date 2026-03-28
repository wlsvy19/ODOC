import { Settings, User, Menu } from 'lucide-react';
import { Link, useLocation } from 'react-router';

interface NavigationProps {
  activeTab?: string;
}

export function Navigation({ activeTab }: NavigationProps) {
  const location = useLocation();
  
  const tabs = [
    { name: '대시보드', path: '/' },
    { name: '설비관리', path: '/facility' },
    { name: '장애관리', path: '/incident' },
    { name: '점검관리', path: '/inspection' },
    { name: '이력/통계관리', path: '/history' },
    { name: '관심관리', path: '/favorites' },
    { name: '공지사항', path: '/notices' },
    { name: '게시판', path: '/board' },
  ];

  return (
    <nav className="bg-white border-b border-gray-200">
      <div className="max-w-[1600px] mx-auto px-6">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center gap-3">
            <Link to="/" className="flex items-center gap-2">
              <div className="text-red-600 font-bold text-lg">ex</div>
              <div className="text-gray-900 font-bold">한국도로공사</div>
            </Link>
            <div className="text-xs text-gray-600 border-l pl-3">
              ITS 원격유지관리 플랫폼
            </div>
          </div>

          {/* Navigation Tabs */}
          <div className="flex items-center gap-1">
            {tabs.map((tab) => (
              <Link
                key={tab.name}
                to={tab.path}
                className={`px-4 py-2 text-sm font-medium transition-colors ${
                  location.pathname === tab.path
                    ? 'text-blue-600 border-b-2 border-blue-600'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                {tab.name}
              </Link>
            ))}
          </div>

          {/* Right Icons */}
          <div className="flex items-center gap-4">
            <button className="flex flex-col items-center gap-0.5 text-gray-600 hover:text-gray-900">
              <Settings className="w-5 h-5" />
              <span className="text-xs">관리자</span>
            </button>
            <button className="flex flex-col items-center gap-0.5 text-gray-600 hover:text-gray-900">
              <User className="w-5 h-5" />
              <span className="text-xs">로그인</span>
            </button>
            <button className="text-gray-600 hover:text-gray-900">
              <Menu className="w-6 h-6" />
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}