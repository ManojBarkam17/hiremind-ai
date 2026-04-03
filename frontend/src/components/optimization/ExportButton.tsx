import React, { useState } from 'react';
import { Button } from '../common/Button';
import { FileText, Download } from 'lucide-react';
import { optimizationApi } from '../../api/optimizationApi';

interface ExportButtonProps {
  optimizedResumeId: string;
}

export const ExportButton: React.FC<ExportButtonProps> = ({ optimizedResumeId }) => {
  const [isLoadingPdf, setIsLoadingPdf] = useState(false);
  const [isLoadingDocx, setIsLoadingDocx] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleExport = async (format: 'pdf' | 'docx') => {
    try {
      setError(null);
      if (format === 'pdf') setIsLoadingPdf(true);
      else setIsLoadingDocx(true);

      const blob = await optimizationApi.exportOptimizedResume(optimizedResumeId, format);

      // Create download link
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `optimized-resume.${format === 'pdf' ? 'pdf' : 'docx'}`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Export failed');
    } finally {
      setIsLoadingPdf(false);
      setIsLoadingDocx(false);
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-4">
        <Button
          variant="primary"
          isLoading={isLoadingPdf}
          onClick={() => handleExport('pdf')}
          className="flex items-center justify-center gap-2"
        >
          <Download size={18} />
          Export as PDF
        </Button>
        <Button
          variant="secondary"
          isLoading={isLoadingDocx}
          onClick={() => handleExport('docx')}
          className="flex items-center justify-center gap-2"
        >
          <FileText size={18} />
          Export as DOCX
        </Button>
      </div>

      {error && (
        <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg">
          <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
        </div>
      )}

      <p className="text-xs text-gray-600 dark:text-gray-400">
        Download your optimized resume in your preferred format. The file will be ready for immediate use.
      </p>
    </div>
  );
};
