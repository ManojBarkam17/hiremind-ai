import React from 'react';
import { Card } from '../common/Card';

interface BeforeAfterViewerProps {
  before: string;
  after: string;
}

export const BeforeAfterViewer: React.FC<BeforeAfterViewerProps> = ({ before, after }) => {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      {/* Before */}
      <Card>
        <div className="space-y-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">Original Resume</h3>
            <p className="text-sm text-gray-600 dark:text-gray-400">Your current resume</p>
          </div>
          <div className="bg-gray-50 dark:bg-gray-900/50 rounded-lg p-6 border border-gray-200 dark:border-gray-700 max-h-96 overflow-y-auto">
            <div className="prose prose-sm dark:prose-invert max-w-none">
              <pre className="text-xs text-gray-700 dark:text-gray-300 font-sans whitespace-pre-wrap break-words">
                {before.substring(0, 1000)}...
              </pre>
            </div>
          </div>
        </div>
      </Card>

      {/* After */}
      <Card className="bg-gradient-to-br from-green-50 dark:from-green-900/10 to-transparent">
        <div className="space-y-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">Optimized Resume</h3>
            <p className="text-sm text-gray-600 dark:text-gray-400">AI-enhanced version</p>
          </div>
          <div className="bg-white dark:bg-gray-800 rounded-lg p-6 border border-green-200 dark:border-green-900 max-h-96 overflow-y-auto">
            <div className="prose prose-sm dark:prose-invert max-w-none">
              <pre className="text-xs text-gray-700 dark:text-gray-300 font-sans whitespace-pre-wrap break-words">
                {after.substring(0, 1000)}...
              </pre>
            </div>
          </div>
        </div>
      </Card>
    </div>
  );
};
