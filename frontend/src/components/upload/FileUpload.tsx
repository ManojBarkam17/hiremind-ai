import React, { useRef, useState } from 'react';
import { Upload, FileText, AlertCircle } from 'lucide-react';
import { Button } from '../common/Button';

interface FileUploadProps {
  onFileSelect: (file: File) => void;
  isLoading?: boolean;
  accept?: string;
  maxSize?: number;
}

export const FileUpload: React.FC<FileUploadProps> = ({
  onFileSelect,
  isLoading = false,
  accept = '.pdf,.docx',
  maxSize = 10 * 1024 * 1024, // 10MB
}) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [isDragging, setIsDragging] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const validateFile = (file: File): string | null => {
    const validTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (!validTypes.includes(file.type)) {
      return 'Please upload a PDF or DOCX file';
    }
    if (file.size > maxSize) {
      return `File size must be less than ${maxSize / (1024 * 1024)}MB`;
    }
    return null;
  };

  const handleFileChange = (file: File) => {
    const validationError = validateFile(file);
    if (validationError) {
      setError(validationError);
      setSelectedFile(null);
    } else {
      setError(null);
      setSelectedFile(file);
      onFileSelect(file);
    }
  };

  const handleDragEnter = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(true);
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);

    const files = e.dataTransfer.files;
    if (files.length > 0) {
      handleFileChange(files[0]);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      handleFileChange(e.target.files[0]);
    }
  };

  return (
    <div className="w-full">
      <div
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        className={`
          relative border-2 border-dashed rounded-lg p-12 text-center transition-colors duration-200
          ${
            isDragging
              ? 'border-indigo-500 bg-indigo-50 dark:bg-indigo-900/10'
              : 'border-gray-300 dark:border-gray-600 bg-gray-50 dark:bg-gray-900/50'
          }
        `}
      >
        <input
          ref={fileInputRef}
          type="file"
          accept={accept}
          onChange={handleInputChange}
          className="hidden"
        />

        {selectedFile ? (
          <div className="space-y-4">
            <div className="flex justify-center">
              <div className="bg-green-100 dark:bg-green-900/30 p-4 rounded-full">
                <FileText className="text-green-600 dark:text-green-400" size={32} />
              </div>
            </div>
            <div>
              <p className="text-lg font-medium text-gray-900 dark:text-white mb-2">File Selected</p>
              <p className="text-sm text-gray-600 dark:text-gray-400 break-words">{selectedFile.name}</p>
              <p className="text-xs text-gray-500 dark:text-gray-500 mt-1">
                {(selectedFile.size / (1024 * 1024)).toFixed(2)} MB
              </p>
            </div>
            <div className="flex gap-3 justify-center">
              <Button
                variant="primary"
                isLoading={isLoading}
                onClick={() => {
                  // The parent component should handle the upload
                }}
              >
                Proceed
              </Button>
              <Button
                variant="secondary"
                onClick={() => {
                  setSelectedFile(null);
                  if (fileInputRef.current) fileInputRef.current.value = '';
                }}
              >
                Change File
              </Button>
            </div>
          </div>
        ) : (
          <div className="space-y-4">
            <div className="flex justify-center">
              <div className="bg-indigo-100 dark:bg-indigo-900/30 p-4 rounded-full">
                <Upload className="text-indigo-600 dark:text-indigo-400" size={32} />
              </div>
            </div>
            <div>
              <p className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                Drag and drop your resume here
              </p>
              <p className="text-sm text-gray-600 dark:text-gray-400">or click to browse</p>
            </div>
            <p className="text-xs text-gray-500 dark:text-gray-500">
              Supported formats: PDF, DOCX (Max 10 MB)
            </p>
            <Button
              variant="primary"
              onClick={() => fileInputRef.current?.click()}
            >
              Select File
            </Button>
          </div>
        )}
      </div>

      {error && (
        <div className="mt-4 flex gap-3 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg">
          <AlertCircle className="text-red-600 dark:text-red-400 flex-shrink-0 mt-0.5" size={20} />
          <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
        </div>
      )}
    </div>
  );
};
