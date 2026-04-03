import React, { useEffect, useState } from 'react';
import { BarChart3, Zap, TrendingUp, FileText } from 'lucide-react';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { StatsCard } from '../components/dashboard/StatsCard';
import { RecentAnalyses } from '../components/dashboard/RecentAnalyses';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { analysisApi } from '../api/analysisApi';
import type { DashboardStats } from '../types';

export const DashboardPage: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setIsLoading(true);
        const data = await analysisApi.getDashboardStats();
        setStats(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load dashboard');
      } finally {
        setIsLoading(false);
      }
    };

    fetchStats();
  }, []);

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            {error && (
              <div className="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg">
                <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
              </div>
            )}

            {isLoading ? (
              <LoadingSpinner />
            ) : stats ? (
              <div className="space-y-8">
                {/* Header */}
                <div>
                  <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Dashboard</h1>
                  <p className="text-gray-600 dark:text-gray-400 mt-2">Welcome back! Here's your resume analysis overview.</p>
                </div>

                {/* Stats Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                  <StatsCard
                    title="Total Analyses"
                    value={stats.totalAnalyses}
                    subtitle="all time"
                    icon={BarChart3}
                    color="indigo"
                  />
                  <StatsCard
                    title="Average Score"
                    value={`${stats.averageScore}%`}
                    subtitle="across analyses"
                    icon={TrendingUp}
                    color="blue"
                  />
                  <StatsCard
                    title="Optimizations"
                    value={stats.totalOptimizations}
                    subtitle="generated"
                    icon={Zap}
                    color="green"
                  />
                  <StatsCard
                    title="Resumes"
                    value={stats.totalAnalyses}
                    subtitle="uploaded"
                    icon={FileText}
                    color="orange"
                  />
                </div>

                {/* Recent Analyses */}
                <RecentAnalyses analyses={stats.recentAnalyses} isLoading={false} />
              </div>
            ) : null}
          </div>
        </main>
      </div>
    </div>
  );
};
