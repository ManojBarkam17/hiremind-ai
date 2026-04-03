import React, { useState } from 'react';
import { Card } from '../common/Card';
import { Badge } from '../common/Badge';
import type { Change } from '../../types';
import { Plus, Minus, Edit } from 'lucide-react';

interface ChangeLogProps {
  changes: Change[];
}

export const ChangeLog: React.FC<ChangeLogProps> = ({ changes }) => {
  const [filter, setFilter] = useState<'all' | 'added' | 'removed' | 'modified'>('all');

  const filteredChanges = filter === 'all' ? changes : changes.filter((c) => c.type === filter);

  const getChangeIcon = (type: Change['type']) => {
    switch (type) {
      case 'added':
        return <Plus className="text-green-600 dark:text-green-400" size={18} />;
      case 'removed':
        return <Minus className="text-red-600 dark:text-red-400" size={18} />;
      case 'modified':
        return <Edit className="text-blue-600 dark:text-blue-400" size={18} />;
    }
  };

  const getChangeColor = (type: Change['type']) => {
    switch (type) {
      case 'added':
        return 'success';
      case 'removed':
        return 'danger';
      case 'modified':
        return 'info';
    }
  };

  const getImpactColor = (impact: Change['impact']) => {
    switch (impact) {
      case 'high':
        return 'danger';
      case 'medium':
        return 'warning';
      case 'low':
        return 'info';
    }
  };

  return (
    <Card>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Changes Made</h3>

          {/* Filter */}
          <div className="flex flex-wrap gap-2">
            {['all', 'added', 'removed', 'modified'].map((f) => (
              <button
                key={f}
                onClick={() => setFilter(f as any)}
                className={`px-4 py-2 rounded-lg font-medium text-sm transition-colors ${
                  filter === f
                    ? 'bg-indigo-100 dark:bg-indigo-900/30 text-indigo-700 dark:text-indigo-300'
                    : 'bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600'
                }`}
              >
                {f.charAt(0).toUpperCase() + f.slice(1)}
                <span className="ml-2 text-xs">({changes.filter((c) => f === 'all' || c.type === f).length})</span>
              </button>
            ))}
          </div>
        </div>

        {/* Changes Timeline */}
        <div className="space-y-3 border-t border-gray-200 dark:border-gray-700 pt-6">
          {filteredChanges.length === 0 ? (
            <p className="text-center text-gray-500 dark:text-gray-400 py-8">No changes of this type</p>
          ) : (
            filteredChanges.map((change, idx) => (
              <div key={idx} className="flex gap-4 p-4 bg-gray-50 dark:bg-gray-800/50 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
                <div className="flex-shrink-0 mt-1">{getChangeIcon(change.type)}</div>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-2 flex-wrap">
                    <Badge variant={getChangeColor(change.type)} className="text-xs">
                      {change.type.charAt(0).toUpperCase() + change.type.slice(1)}
                    </Badge>
                    <span className="text-xs font-medium text-gray-600 dark:text-gray-400">{change.section}</span>
                    <Badge variant={getImpactColor(change.impact)} className="text-xs">
                      {change.impact.charAt(0).toUpperCase() + change.impact.slice(1)} Impact
                    </Badge>
                  </div>

                  {change.before && (
                    <div className="mb-3">
                      <p className="text-xs text-gray-600 dark:text-gray-400 uppercase font-semibold mb-1">Before</p>
                      <p className="text-sm text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-900/50 p-2 rounded border-l-2 border-red-400 line-through opacity-75">
                        {change.before}
                      </p>
                    </div>
                  )}

                  {change.after && (
                    <div>
                      <p className="text-xs text-gray-600 dark:text-gray-400 uppercase font-semibold mb-1">After</p>
                      <p className="text-sm text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-900/50 p-2 rounded border-l-2 border-green-400">
                        {change.after}
                      </p>
                    </div>
                  )}
                </div>
              </div>
            ))
          )}
        </div>

        {/* Summary */}
        <div className="border-t border-gray-200 dark:border-gray-700 pt-6">
          <div className="grid grid-cols-3 gap-4">
            <div className="text-center">
              <p className="text-2xl font-bold text-green-600 dark:text-green-400">
                {changes.filter((c) => c.type === 'added').length}
              </p>
              <p className="text-xs text-gray-600 dark:text-gray-400 mt-1">Added</p>
            </div>
            <div className="text-center">
              <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                {changes.filter((c) => c.type === 'modified').length}
              </p>
              <p className="text-xs text-gray-600 dark:text-gray-400 mt-1">Modified</p>
            </div>
            <div className="text-center">
              <p className="text-2xl font-bold text-red-600 dark:text-red-400">
                {changes.filter((c) => c.type === 'removed').length}
              </p>
              <p className="text-xs text-gray-600 dark:text-gray-400 mt-1">Removed</p>
            </div>
          </div>
        </div>
      </div>
    </Card>
  );
};
