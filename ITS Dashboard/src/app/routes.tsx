import { createBrowserRouter } from 'react-router';
import { Layout } from './Layout';
import { Dashboard } from './pages/Dashboard';
import { FacilityManagement } from './pages/FacilityManagement';
import { IncidentManagement } from './pages/IncidentManagement';
import { InspectionManagement } from './pages/InspectionManagement';
import { HistoryStatistics } from './pages/HistoryStatistics';
import { Favorites } from './pages/Favorites';
import { Notices } from './pages/Notices';
import { Board } from './pages/Board';

export const router = createBrowserRouter([
  {
    path: '/',
    Component: Layout,
    children: [
      { index: true, Component: Dashboard },
      { path: 'facility', Component: FacilityManagement },
      { path: 'incident', Component: IncidentManagement },
      { path: 'inspection', Component: InspectionManagement },
      { path: 'history', Component: HistoryStatistics },
      { path: 'favorites', Component: Favorites },
      { path: 'notices', Component: Notices },
      { path: 'board', Component: Board },
    ],
  },
]);
