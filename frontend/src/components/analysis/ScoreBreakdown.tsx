import React from 'react';
import { Card } from '../common/Card';
import { RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, ResponsiveContainer, Tooltip } from 'recharts';

interface ScoreBreakdownProps {
  scores: {
    skillsMatch: number;
    experienceRelevance: number;
    educationAlignment: number;
    keywordOptimization: number;
    formattingCompliance: number;
    achievements: number;
  };
}

export const ScoreBreakdown: React.FC<ScoreBreakdownProps> = ({ scores }) => {
  const data = [
    { category: 'Skills Match', value: scores.skillsMatch },
    { category: 'Experience', value: scores.experienceRelevance },
    { category: 'Education', value: scores.educationAlignment },
    { category: 'Keywords', value: scores.keywordOptimization },
    { category: 'Formatting', value: scores.formattingCompliance },
    { category: 'Achievements', value: scores.achievements },
  ];

  const avgScore = Math.round(Object.values(scores).reduce((a, b) => a + b, 0) / 6);

  return (
    <Card>
      <div className="space-y-6">
        <div>
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">Score Breakdown</h3>
          <p className="text-sm text-gray-600 dark:text-gray-400">Average: {avgScore}%</p>
        </div>

        <ResponsiveContainer width="100%" height={300}>
          <RadarChart data={data} margin={{ top: 20, right: 20, bottom: 20, left: 20 }}>
            <PolarGrid stroke="currentColor" className="text-gray-300 dark:text-gray-600" />
            <PolarAngleAxis dataKey="category" className="text-xs text-gray-600 dark:text-gray-400" />
            <PolarRadiusAxis angle={90} domain={[0, 100]} className="text-xs text-gray-500 dark:text-gray-500" />
            <Radar
              name="Score"
              dataKey="value"
              stroke="#4f46e5"
              fill="#4f46e5"
              fillOpacity={0.6}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'rgba(255, 255, 255, 0.95)',
                border: '1px solid #ccc',
                borderRadius: '8px',
              }}
              labelStyle={{ color: '#000' }}
            />
          </RadarChart>
        </ResponsiveContainer>

        {/* Detailed Scores */}
        <div className="grid grid-cols-2 gap-4">
          {data.map((item) => (
            <div key={item.category} className="p-4 bg-gray-50 dark:bg-gray-700/50 rounded-lg">
              <p className="text-sm font-medium text-gray-600 dark:text-gray-400 mb-2">{item.category}</p>
              <div className="flex items-baseline gap-2">
                <span className="text-2xl font-bold text-indigo-600 dark:text-indigo-400">{item.value}</span>
                <span className="text-sm text-gray-500 dark:text-gray-500">/100</span>
              </div>
              {/* Progress Bar */}
              <div className="mt-2 w-full bg-gray-200 dark:bg-gray-600 rounded-full h-2">
                <div
                  className="bg-gradient-to-r from-indigo-500 to-blue-500 h-2 rounded-full transition-all duration-500"
                  style={{ width: `${item.value}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </Card>
  );
};
