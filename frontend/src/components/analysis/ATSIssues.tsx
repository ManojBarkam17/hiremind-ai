import React, { useState } from 'react';
import { Card } from '../common/Card';
import { Badge } from '../common/Badge';
import type { ATSIssue } from '../../types';
import { AlertTriangle, AlertCircle, Info, ChevronDown } from 'lucide-react';

interface ATSIssuesProps {
  issues: ATSIssue[];
}

export const ATSIssues: React.FC<ATSIssuesProps> = ({ issues }) => {
  const [expandedIndex, setExpandedIndex] = useState<number | null>(null);

  const severityIcon = {
    critical: <AlertTriangle className="text-red-600 dark:text-red-400" size={20} />,
    warning: <AlertCircle className="text-yellow-600 dark:text-yellow-400" size={20} />,
    info: <Info className="text-blue-600 dark:text-blue-400" size={20} />,
  };

  const severityBadge = {
    critical: 'danger',
    warning: 'warning',
    info: 'info',
  } as const;

  if (issues.length === 0) {
    return (
      <Card>
        <div className="text-center py-8">
          <div className="flex justify-center mb-4">
            <div className="bg-green-100 dark:bg-green-900/30 p-3 rounded-full">
              <CheckCircle className="text-green-600 dark:text-green-400" size={24} />
            </div>
          </div>
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">No ATS Issues Detected</h3>
          <p className="text-sm text-gray-600 dark:text-gray-400">Your resume is optimized for ATS systems.</p>
        </div>
      </Card>
    );
  }

  const criticalCount = issues.filter((i) => i.severity === 'critical').length;
  const warningCount = issues.filter((i) => i.severity === 'warning').length;

  return (
    <Card>
      <div className="space-y-6">
        {/* Summary */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">ATS Issues Detected</h3>
          <div className="grid grid-cols-3 gap-4">
            <div className="p-3 bg-red-50 dark:bg-red-900/20 rounded-lg border border-red-200 dark:border-red-900">
              <p className="text-2xl font-bold text-red-600 dark:text-red-400">{criticalCount}</p>
              <p className="text-xs text-red-700 dark:text-red-300">Critical</p>
            </div>
            <div className="p-3 bg-yellow-50 dark:bg-yellow-900/20 rounded-lg border border-yellow-200 dark:border-yellow-900">
              <p className="text-2xl font-bold text-yellow-600 dark:text-yellow-400">{warningCount}</p>
              <p className="text-xs text-yellow-700 dark:text-yellow-300">Warnings</p>
            </div>
            <div className="p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg border border-blue-200 dark:border-blue-900">
              <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">{issues.length}</p>
              <p className="text-xs text-blue-700 dark:text-blue-300">Total</p>
            </div>
          </div>
        </div>

        {/* Issues List */}
        <div className="space-y-3 border-t border-gray-200 dark:border-gray-700 pt-6">
          {issues.map((issue, idx) => (
            <div
              key={idx}
              className="border border-gray-200 dark:border-gray-700 rounded-lg overflow-hidden hover:border-gray-300 dark:hover:border-gray-600 transition-colors"
            >
              {/* Issue Header */}
              <button
                onClick={() => setExpandedIndex(expandedIndex === idx ? null : idx)}
                className="w-full flex items-start justify-between p-4 hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors"
              >
                <div className="flex items-start gap-3 text-left flex-1">
                  <div className="mt-1">{severityIcon[issue.severity]}</div>
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <h4 className="font-medium text-gray-900 dark:text-white">{issue.title}</h4>
                      <Badge variant={severityBadge[issue.severity]} className="text-xs">
                        {issue.severity.charAt(0).toUpperCase() + issue.severity.slice(1)}
                      </Badge>
                    </div>
                    <p className="text-sm text-gray-600 dark:text-gray-400">{issue.description}</p>
                  </div>
                </div>
                <ChevronDown
                  size={20}
                  className={`text-gray-400 flex-shrink-0 transition-transform ${
                    expandedIndex === idx ? 'rotate-180' : ''
                  }`}
                />
              </button>

              {/* Issue Details */}
              {expandedIndex === idx && (
                <div className="px-4 pb-4 border-t border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-800/50">
                  <div className="space-y-4">
                    <div>
                      <p className="text-xs font-semibold text-gray-600 dark:text-gray-400 uppercase mb-2">
                        Suggestion
                      </p>
                      <p className="text-sm text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-900/50 p-3 rounded border border-gray-200 dark:border-gray-700">
                        {issue.suggestion}
                      </p>
                    </div>
                    <div className="flex items-center gap-2 text-xs text-gray-500 dark:text-gray-500">
                      <span className="inline-block px-2 py-1 bg-gray-200 dark:bg-gray-700 rounded">
                        Type: {issue.type.charAt(0).toUpperCase() + issue.type.slice(1)}
                      </span>
                    </div>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </Card>
  );
};

// Import CheckCircle
import { CheckCircle } from 'lucide-react';
