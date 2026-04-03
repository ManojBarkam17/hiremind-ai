import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/layout/ProtectedRoute';

// Pages
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { UploadResumePage } from './pages/UploadResumePage';
import { PasteJDPage } from './pages/PasteJDPage';
import { AnalysisResultsPage } from './pages/AnalysisResultsPage';
import { OptimizationPage } from './pages/OptimizationPage';
import { ComparisonPage } from './pages/ComparisonPage';
import { HistoryPage } from './pages/HistoryPage';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<LoginPage />} />

          {/* Protected Routes */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <DashboardPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/upload"
            element={
              <ProtectedRoute>
                <UploadResumePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/job-description"
            element={
              <ProtectedRoute>
                <PasteJDPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/analysis/:id"
            element={
              <ProtectedRoute>
                <AnalysisResultsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/optimization/:id"
            element={
              <ProtectedRoute>
                <OptimizationPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/comparison"
            element={
              <ProtectedRoute>
                <ComparisonPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/history"
            element={
              <ProtectedRoute>
                <HistoryPage />
              </ProtectedRoute>
            }
          />

          {/* Default Routes */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
