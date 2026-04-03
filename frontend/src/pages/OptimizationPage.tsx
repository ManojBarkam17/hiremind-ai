import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { BeforeAfterViewer } from '../components/optimization/BeforeAfterViewer';
import { ChangeLog } from '../components/optimization/ChangeLog';
import { ExportButton } from '../components/optimization/ExportButton';
import { Button } from '../components/common/Button';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { optimizationApi } from '../api/optimizationApi';
import type { OptimizedResume } from '../types';
import { ArrowLeft } from 'lucide-react';

export const OptimizationPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [optimized, setOptimized] = useState<OptimizedResume | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchOptimization = async () => {
      if (!id) {
        setError('Optimization ID is missing');
        setIsLoading(false);
        return;
      }

      try {
        const data = await optimizationApi.getOptimizedResume(id);
        setOptimized(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load optimization');
      } finally {
        setIsLoading(false);
      }
    };

    fetchOptimization();
  }, [id]);

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            {error && (
              <div className="max-w-6xl mx-auto mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg">
                <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
              </div>
            )}

            {isLoading ? (
              <LoadingSpinner />
            ) : optimized ? (
              <div className="max-w-6xl mx-auto space-y-8">
                {/* Header */}
                <div className="flex items-center justify-between">
                  <div>
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Optimized Resume</h1>
                    <p className="text-gray-600 dark:text-gray-400 mt-2">AI-enhanced version ready for submission</p>
                  </div>
                  <Button variant="ghost" onClick={() => navigate(-1)} className="flex items-center gap-2">
                    <ArrowLeft size={20} />
                    Back
                  </Button>
                </div>

                {/* Info Cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="p-4 bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-900 rounded-lg">
                    <h3 className="font-semibold text-green-900 dark:text-green-200 mb-1">Total Changes</h3>
                    <p className="text-2xl font-bold text-green-600 dark:text-green-400">{optimized.changes.length}</p>
                  </div>
                  <div className="p-4 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-900 rounded-lg">
                    <h3 className="font-semibold text-blue-900 dark:text-blue-200 mb-1">High Impact</h3>
                    <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                      {optimized.changes.filter((c) => c.impact === 'high').length}
                    </p>
                  </div>
                  <div className="p-4 bg-purple-50 dark:bg-purple-900/20 border border-purple-200 dark:border-purple-900 rounded-lg">
                    <h3 className="font-semibold text-purple-900 dark:text-purple-200 mb-1">Ready to Export</h3>
                    <p className="text-2xl font-bold text-purple-600 dark:text-purple-400">Yes</p>
                  </div>
                </div>

                {/* Before/After Viewer */}
                <BeforeAfterViewer before={optimized.originalContent} after={optimized.optimizedContent} />

                {/* Change Log */}
                <ChangeLog changes={optimized.changes} />

                {/* Export Section */}
                <div className="bg-gradient-to-r from-indigo-50 to-blue-50 dark:from-indigo-900/20 dark:to-blue-900/20 border border-indigo-200 dark:border-indigo-900 rounded-lg p-8">
                  <h3 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Export Your Optimized Resume</h3>
                  <p className="text-gray-600 dark:text-gray-400 mb-6">
                    Download your AI-optimized resume in your preferred format. It's ready to send to recruiters and apply for jobs.
                  </p>
                  {id && <ExportButton optimizedResumeId={id} />}
                </div>

                {/* Next Steps */}
                <div className="bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700 shadow-sm p-6">
                  <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Next Steps</h3>
                  <ol className="space-y-3 text-gray-700 dark:text-gray-300">
                    <li className="flex gap-3">
                      <span className="font-bold text-indigo-600 dark:text-indigo-400 flex-shrink-0">1.</span>
                      <span>Download your optimized resume in PDF or DOCX format</span>
                    </li>
                    <li className="flex gap-3">
                      <span className="font-bold text-indigo-600 dark:text-indigo-400 flex-shrink-0">2.</span>
                      <span>Review the changes and make any final adjustments</span>
                    </li>
                    <li className="flex gap-3">
                      <span className="font-bold text-indigo-600 dark:text-indigo-400 flex-shrink-0">3.</span>
                      <span>Submit to your target companies</span>
                    </li>
                    <li className="flex gap-3">
                      <span className="font-bold text-indigo-600 dark:text-indigo-400 flex-shrink-0">4.</span>
                      <span>Analyze more job descriptions to improve your match rate</span>
                    </li>
                  </ol>
                </div>

                {/* Bottom Action */}
                <div className="flex gap-4">
                  <Button
                    variant="primary"
                    size="lg"
                    onClick={() => navigate('/upload')}
                    className="flex-1"
                  >
                    Analyze Another Resume
                  </Button>
                  <Button variant="secondary" size="lg" onClick={() => navigate('/dashboard')}>
                    Back to Dashboard
                  </Button>
                </div>
              </div>
            ) : null}
          </div>
        </main>
      </div>
    </div>
  );
};
