import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { Badge } from '../components/common/Badge';
import { Button } from '../components/common/Button';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { analysisApi } from '../api/analysisApi';
import type { AnalysisResult, PaginatedResponse } from '../types';
import { ChevronRight, Trash2 } from 'lucide-react';

export const HistoryPage: React.FC = () => {
  const [analyses, setAnalyses] = useState<AnalysisResult[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    const fetchAnalyses = async () => {
      try {
        setIsLoading(true);
        const response: PaginatedResponse<AnalysisResult> = await analysisApi.getAnalyses(page, 10);
        setAnalyses(response.data);
        setTotalPages(Math.ceil(response.total / response.limit));
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load history');
      } finally {
        setIsLoading(false);
      }
    };

    fetchAnalyses();
  }, [page]);

  const getScoreBadgeVariant = (score: number) => {
    if (score >= 80) return 'success';
    if (score >= 60) return 'warning';
    return 'danger';
  };

  const getScoreColor = (score: number) => {
    if (score >= 80) return 'text-green-600 dark:text-green-400';
    if (score >= 60) return 'text-yellow-600 dark:text-yellow-400';
    return 'text-red-600 dark:text-red-400';
  };

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            <div className="max-w-6xl mx-auto space-y-8">
              {/* Header */}
              <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Analysis History</h1>
                <p className="text-gray-600 dark:text-gray-400 mt-2">View all your previous resume analyses and their results.</p>
              </div>

              {error && (
                <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg">
                  <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
                </div>
              )}

              {isLoading ? (
                <LoadingSpinner />
              ) : analyses.length === 0 ? (
                <div className="text-center py-12">
                  <p className="text-gray-500 dark:text-gray-400 mb-4">No analyses yet</p>
                  <Link to="/upload">
                    <Button variant="primary">Start Analyzing</Button>
                  </Link>
                </div>
              ) : (
                <>
                  {/* Table */}
                  <div className="bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden">
                    <div className="overflow-x-auto">
                      <table className="w-full text-sm">
                        <thead className="bg-gray-50 dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700">
                          <tr>
                            <th className="text-left py-4 px-6 font-semibold text-gray-700 dark:text-gray-300">Resume</th>
                            <th className="text-left py-4 px-6 font-semibold text-gray-700 dark:text-gray-300">Position</th>
                            <th className="text-left py-4 px-6 font-semibold text-gray-700 dark:text-gray-300">Score</th>
                            <th className="text-left py-4 px-6 font-semibold text-gray-700 dark:text-gray-300">Date</th>
                            <th className="text-left py-4 px-6 font-semibold text-gray-700 dark:text-gray-300">Actions</th>
                          </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                          {analyses.map((analysis) => (
                            <tr key={analysis.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                              <td className="py-4 px-6">
                                <Link
                                  to={`/analysis/${analysis.id}`}
                                  className="text-indigo-600 dark:text-indigo-400 hover:underline truncate max-w-xs block"
                                >
                                  {analysis.resumeFileName}
                                </Link>
                              </td>
                              <td className="py-4 px-6 text-gray-600 dark:text-gray-400 truncate max-w-xs">
                                {analysis.jobTitle}
                              </td>
                              <td className="py-4 px-6">
                                <div className="flex items-center gap-2">
                                  <span className={`text-lg font-bold ${getScoreColor(analysis.overallScore)}`}>
                                    {analysis.overallScore}%
                                  </span>
                                  <Badge variant={getScoreBadgeVariant(analysis.overallScore)}>
                                    {analysis.overallScore >= 80 ? 'Excellent' : analysis.overallScore >= 60 ? 'Good' : 'Fair'}
                                  </Badge>
                                </div>
                              </td>
                              <td className="py-4 px-6 text-gray-500 dark:text-gray-500 text-xs">
                                {new Date(analysis.createdAt).toLocaleDateString()}
                              </td>
                              <td className="py-4 px-6">
                                <div className="flex items-center gap-2">
                                  <Link to={`/analysis/${analysis.id}`}>
                                    <Button variant="ghost" size="sm" className="flex items-center gap-1">
                                      View
                                      <ChevronRight size={16} />
                                    </Button>
                                  </Link>
                                  <Button variant="ghost" size="sm" title="Delete">
                                    <Trash2 size={16} className="text-gray-400 hover:text-red-600" />
                                  </Button>
                                </div>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  </div>

                  {/* Pagination */}
                  {totalPages > 1 && (
                    <div className="flex justify-center items-center gap-4">
                      <Button
                        variant="secondary"
                        disabled={page === 1}
                        onClick={() => setPage(page - 1)}
                      >
                        Previous
                      </Button>
                      <div className="text-sm text-gray-600 dark:text-gray-400">
                        Page {page} of {totalPages}
                      </div>
                      <Button
                        variant="secondary"
                        disabled={page === totalPages}
                        onClick={() => setPage(page + 1)}
                      >
                        Next
                      </Button>
                    </div>
                  )}

                  {/* Summary Stats */}
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-gradient-to-br from-green-50 to-emerald-50 dark:from-green-900/20 dark:to-emerald-900/20 border border-green-200 dark:border-green-900 rounded-lg p-6">
                      <p className="text-sm text-green-600 dark:text-green-400 font-medium mb-1">Excellent Matches</p>
                      <p className="text-3xl font-bold text-green-700 dark:text-green-300">
                        {analyses.filter((a) => a.overallScore >= 80).length}
                      </p>
                    </div>
                    <div className="bg-gradient-to-br from-yellow-50 to-orange-50 dark:from-yellow-900/20 dark:to-orange-900/20 border border-yellow-200 dark:border-yellow-900 rounded-lg p-6">
                      <p className="text-sm text-yellow-600 dark:text-yellow-400 font-medium mb-1">Good Matches</p>
                      <p className="text-3xl font-bold text-yellow-700 dark:text-yellow-300">
                        {analyses.filter((a) => a.overallScore >= 60 && a.overallScore < 80).length}
                      </p>
                    </div>
                    <div className="bg-gradient-to-br from-red-50 to-pink-50 dark:from-red-900/20 dark:to-pink-900/20 border border-red-200 dark:border-red-900 rounded-lg p-6">
                      <p className="text-sm text-red-600 dark:text-red-400 font-medium mb-1">Needs Improvement</p>
                      <p className="text-3xl font-bold text-red-700 dark:text-red-300">
                        {analyses.filter((a) => a.overallScore < 60).length}
                      </p>
                    </div>
                  </div>
                </>
              )}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};
