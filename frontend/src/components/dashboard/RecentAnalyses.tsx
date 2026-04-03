import React from 'react';
import { Link } from 'react-router-dom';
import { Card } from '../common/Card';
import { Badge } from '../common/Badge';
import type { AnalysisResult } from '../../types';
import { ArrowRight } from 'lucide-react';

interface RecentAnalysesProps {
  analyses: AnalysisResult[];
  isLoading?: boolean;
}

export const RecentAnalyses: React.FC<RecentAnalysesProps> = ({ analyses, isLoading }) => {
  const getScoreBadgeVariant = (score: number) => {
    if (score >= 80) return 'success';
    if (score >= 60) return 'warning';
    return 'danger';
  };

  return (
    <Card>
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-lg font-semibold text-gray-900 dark:text-white">Recent Analyses</h3>
        <Link to="/history" className="text-indigo-600 hover:text-indigo-700 dark:text-indigo-400 dark:hover:text-indigo-300 text-sm font-medium flex items-center gap-1">
          View all <ArrowRight size={16} />
        </Link>
      </div>

      {isLoading ? (
        <div className="space-y-4">
          {[1, 2, 3].map((i) => (
            <div key={i} className="h-16 bg-gray-200 dark:bg-gray-700 rounded animate-pulse" />
          ))}
        </div>
      ) : analyses.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-500 dark:text-gray-400">No analyses yet</p>
        </div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-700">
                <th className="text-left py-3 px-4 font-medium text-gray-600 dark:text-gray-400">Resume</th>
                <th className="text-left py-3 px-4 font-medium text-gray-600 dark:text-gray-400">Position</th>
                <th className="text-left py-3 px-4 font-medium text-gray-600 dark:text-gray-400">Score</th>
                <th className="text-left py-3 px-4 font-medium text-gray-600 dark:text-gray-400">Date</th>
              </tr>
            </thead>
            <tbody>
              {analyses.map((analysis) => (
                <tr
                  key={analysis.id}
                  className="border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors"
                >
                  <td className="py-3 px-4 text-gray-900 dark:text-white truncate">
                    <Link to={`/analysis/${analysis.id}`} className="hover:text-indigo-600 dark:hover:text-indigo-400">
                      {analysis.resumeFileName}
                    </Link>
                  </td>
                  <td className="py-3 px-4 text-gray-600 dark:text-gray-400 truncate">{analysis.jobTitle}</td>
                  <td className="py-3 px-4">
                    <Badge variant={getScoreBadgeVariant(analysis.overallScore)}>
                      {analysis.overallScore}%
                    </Badge>
                  </td>
                  <td className="py-3 px-4 text-gray-500 dark:text-gray-500 text-xs">
                    {new Date(analysis.createdAt).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </Card>
  );
};
