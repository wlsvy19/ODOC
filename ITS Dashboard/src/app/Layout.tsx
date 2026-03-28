import { Outlet } from 'react-router';
import { Navigation } from './components/Navigation';
import { HelpDeskButton } from './components/HelpDeskButton';
import { Footer } from './components/Footer';

export function Layout() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Navigation */}
      <Navigation />

      {/* Main Content */}
      <main className="flex-1 max-w-[1600px] mx-auto px-6 py-6 w-full">
        <Outlet />
      </main>

      {/* Footer */}
      <Footer />

      {/* Help Desk Button */}
      <HelpDeskButton />
    </div>
  );
}
