import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { ScoreCard } from '../components/analysis/ScoreCard';
import { ScoreBreakdown } from '../components/analysis/ScoreBreakdown';
import { SkillsPanel } from '../components/analysis/SkillsPanel';
import { ATSIssues } from '../components/analysis/ATSIssues';
import { Button } from '../components/common/Button';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { analysisApi } from '../api/analysisApi';
import { optimizationApi } from '../api/optimizationApi';
import type { AnalysisResult } from '../types';
import { Zap, Download } from 'lucide-react';

export const AnalysisResultsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [analysis, setAnalysis] = useState<AnalysisResult | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isOptimizing, setIsOptimizing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAnalysis = async () => {
      if (!id) {
        setError('Analysis ID is missing');
        setIsLoading(false);
        return;
      }

      try {
        const data = await analysisApi.getAnalysis(id);
        setAnalysis(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load analysis');
      } finally {
        setIsLoading(false);
      }
    };

    fetchAnalysis();
  }, [id]);

  const handleOptimize = async () => {
    if (!id) return;
    setIsOptimizing(true);
    try {
      const optimized = await optimizationApi.optimizeResume(id);
      navigate(`/optimization/${optimized.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Optimization failed');
    } finally {
      setIsOptimizing(false);
    }
  };

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
            ) : analysis ? (
              <div className="max-w-6xl mx-auto space-y-8">
                {/* Header */}
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                  <div>
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white">{analysis.jobTitle}</h1>
                    <p className="text-gray-600 dark:text-gray-400 mt-2">Resume: {analysis.resumeFileName}</p>
                  </div>
                  <Button
                    variant="primary"
                    size="lg"
                    isLoading={isOptimizing}
                    onClick={handleOptimize}
                    className="flex items-center justify-center gap-2"
                  >
                    <Zap size={20} />
                    Optimize Resume
                  </Button>
                </div>

                {/* Score Cards */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  <ScoreCard score={analysis.overallScore} />
                  <ScoreBreakdown scores={analysis.scores} />
                </div>

                {/* Skills Panel */}
                <SkillsPanel matchedSkills={analysis.matchedSkills} missingSkills={analysis.missingSkills} />

                {/* ATS Issues */}
                <ATSIssues issues={analysis.atsIssues} />

                {/* Recommendations */}
                {analysis.recommendations.length > 0 && (
                  <div className="bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700 shadow-sm p-6">
                    <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Recommendations</h3>
                    <ul className="space-y-3">
                      {analysis.recommendations.map((rec, idx) => (
                        <li key={idx} className="flex gap-3">
                          <span className="text-indigo-600 dark:text-indigo-400 font-bold flex-shrink-0">{idx + 1}.</span>
                          <span className="text-gray-700 dark:text-gray-300">{rec}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}

                {/* Action Buttons */}
                <div className="flex flex-col sm:flex-row gap-4">
                  <Button variant="primary" size="lg" onClick={handleOptimize} isLoading={isOptimizing} className="flex items-center justify-center gap-2">
                    <Zap size={20} />
                    Get AI-Optimized Resume
                  </Button>
                  <Button variant="secondary" size="lg" onClick={() => navigate('/history')}>
                    View History
                  </Button>
                  <Button variant="ghost" size="lg" onClick={() => navigate('/upload')} className="flex items-center justify-center gap-2">
                    <Download size={20} />
                    Analyze Another Resume
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
