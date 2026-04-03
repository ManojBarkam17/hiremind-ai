import React from 'react';
import { Card } from '../common/Card';
import { Badge } from '../common/Badge';
import type { Skill } from '../../types';
import { CheckCircle, AlertCircle } from 'lucide-react';

interface SkillsPanelProps {
  matchedSkills: Skill[];
  missingSkills: Skill[];
}

export const SkillsPanel: React.FC<SkillsPanelProps> = ({ matchedSkills, missingSkills }) => {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      {/* Matched Skills */}
      <Card>
        <div className="flex items-center gap-2 mb-4">
          <CheckCircle className="text-green-600 dark:text-green-400" size={20} />
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            Matched Skills ({matchedSkills.length})
          </h3>
        </div>

        <div className="space-y-3">
          {matchedSkills.length === 0 ? (
            <p className="text-sm text-gray-500 dark:text-gray-400">No matched skills found</p>
          ) : (
            <div className="flex flex-wrap gap-2">
              {matchedSkills.map((skill, idx) => (
                <Badge key={idx} variant="success">
                  <span className="flex items-center gap-1">
                    <CheckCircle size={14} />
                    {skill.name}
                  </span>
                </Badge>
              ))}
            </div>
          )}
        </div>

        {/* Matched Skills Details */}
        {matchedSkills.length > 0 && (
          <div className="mt-4 pt-4 border-t border-gray-200 dark:border-gray-700 space-y-2">
            <p className="text-xs font-medium text-gray-600 dark:text-gray-400 uppercase">Proficiency Levels</p>
            <div className="space-y-2">
              {matchedSkills.map((skill, idx) => (
                <div key={idx} className="flex items-center justify-between text-sm">
                  <span className="text-gray-600 dark:text-gray-400">{skill.name}</span>
                  <span className="text-gray-500 dark:text-gray-500">{skill.proficiency || 'Intermediate'}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </Card>

      {/* Missing Skills */}
      <Card>
        <div className="flex items-center gap-2 mb-4">
          <AlertCircle className="text-red-600 dark:text-red-400" size={20} />
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            Missing Skills ({missingSkills.length})
          </h3>
        </div>

        <div className="space-y-3">
          {missingSkills.length === 0 ? (
            <p className="text-sm text-gray-500 dark:text-gray-400">All key skills are covered!</p>
          ) : (
            <div className="flex flex-wrap gap-2">
              {missingSkills.map((skill, idx) => (
                <Badge key={idx} variant="danger">
                  <span className="flex items-center gap-1">
                    <AlertCircle size={14} />
                    {skill.name}
                  </span>
                </Badge>
              ))}
            </div>
          )}
        </div>

        {/* Skills Recommendation */}
        {missingSkills.length > 0 && (
          <div className="mt-4 pt-4 border-t border-gray-200 dark:border-gray-700">
            <p className="text-xs font-medium text-gray-600 dark:text-gray-400 uppercase mb-3">
              Recommendations
            </p>
            <div className="space-y-2 text-sm text-gray-600 dark:text-gray-400">
              <p>• Consider adding these skills to your resume</p>
              <p>• Include relevant certifications or courses</p>
              <p>• Highlight transferable skills from other roles</p>
            </div>
          </div>
        )}
      </Card>
    </div>
  );
};
