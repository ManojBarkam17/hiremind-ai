import React from 'react';
import { Card } from '../common/Card';

interface ScoreCardProps {
  score: number;
  maxScore?: number;
  title?: string;
}

export const ScoreCard: React.FC<ScoreCardProps> = ({ score, maxScore = 100, title = 'Overall Score' }) => {
  const percentage = (score / maxScore) * 100;
  const circumference = 2 * Math.PI * 45;
  const strokeDashoffset = circumference - (percentage / 100) * circumference;

  const getTextColor = () => {
    if (score >= 80) return 'text-green-600 dark:text-green-400';
    if (score >= 60) return 'text-yellow-600 dark:text-yellow-400';
    return 'text-red-600 dark:text-red-400';
  };

  return (
    <Card>
      <div className="text-center">
        <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-8">{title}</h3>

        <div className="flex justify-center mb-6">
          <div className="relative w-48 h-48">
            {/* Background Circle */}
            <svg className="w-full h-full -rotate-90" viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="currentColor" strokeWidth="8" className="text-gray-200 dark:text-gray-700" />

              {/* Gradient Def */}
              <defs>
                <linearGradient id="scoreGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" className={`stop-color-${score >= 80 ? 'green' : score >= 60 ? 'yellow' : 'red'}-400`} />
                  <stop offset="100%" className={`stop-color-${score >= 80 ? 'green' : score >= 60 ? 'yellow' : 'red'}-600`} />
                </linearGradient>
              </defs>

              {/* Progress Circle */}
              <circle
                cx="50"
                cy="50"
                r="45"
                fill="none"
                stroke={score >= 80 ? '#4ade80' : score >= 60 ? '#facc15' : '#f87171'}
                strokeWidth="8"
                strokeDasharray={circumference}
                strokeDashoffset={strokeDashoffset}
                strokeLinecap="round"
                className="transition-all duration-500"
              />
            </svg>

            {/* Score Text */}
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span className={`text-5xl font-bold ${getTextColor()}`}>{score}</span>
              <span className="text-sm text-gray-500 dark:text-gray-400">{maxScore === 100 ? '%' : ''}</span>
            </div>
          </div>
        </div>

        {/* Description */}
        <div className="space-y-2">
          <p className={`text-lg font-medium ${getTextColor()}`}>
            {score >= 80 ? 'Excellent Match' : score >= 60 ? 'Good Match' : 'Needs Improvement'}
          </p>
          <p className="text-sm text-gray-600 dark:text-gray-400">
            {score >= 80
              ? 'Your resume aligns well with this job description.'
              : score >= 60
              ? 'Your resume has some alignment with the requirements.'
              : 'Consider optimizing your resume for better alignment.'}
          </p>
        </div>
      </div>
    </Card>
  );
};
