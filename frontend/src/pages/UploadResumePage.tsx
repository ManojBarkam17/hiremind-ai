import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { FileUpload } from '../components/upload/FileUpload';
import { Button } from '../components/common/Button';
import { resumeApi } from '../api/resumeApi';
import { AlertCircle } from 'lucide-react';

export const UploadResumePage: React.FC = () => {
  const navigate = useNavigate();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleFileSelect = (file: File) => {
    setSelectedFile(file);
    setError(null);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError('Please select a file');
      return;
    }

    setIsLoading(true);
    try {
      const resume = await resumeApi.uploadResume(selectedFile);
      navigate(`/job-description?resumeId=${resume.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Upload failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            <div className="max-w-2xl mx-auto space-y-8">
              {/* Header */}
              <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Upload Your Resume</h1>
                <p className="text-gray-600 dark:text-gray-400 mt-2">
                  Start by uploading your resume in PDF or DOCX format. We'll analyze it against job descriptions to give you insights and optimization suggestions.
                </p>
              </div>

              {/* Info Cards */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="p-4 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-900 rounded-lg">
                  <h3 className="font-semibold text-blue-900 dark:text-blue-200 mb-1">Supported Formats</h3>
                  <p className="text-sm text-blue-800 dark:text-blue-300">PDF and DOCX files up to 10 MB</p>
                </div>
                <div className="p-4 bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-900 rounded-lg">
                  <h3 className="font-semibold text-green-900 dark:text-green-200 mb-1">Instant Analysis</h3>
                  <p className="text-sm text-green-800 dark:text-green-300">Get matched against job descriptions</p>
                </div>
                <div className="p-4 bg-purple-50 dark:bg-purple-900/20 border border-purple-200 dark:border-purple-900 rounded-lg">
                  <h3 className="font-semibold text-purple-900 dark:text-purple-200 mb-1">AI Optimization</h3>
                  <p className="text-sm text-purple-800 dark:text-purple-300">Get personalized improvement suggestions</p>
                </div>
              </div>

              {/* File Upload */}
              <FileUpload onFileSelect={handleFileSelect} isLoading={isLoading} />

              {error && (
                <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg flex gap-3">
                  <AlertCircle className="text-red-600 dark:text-red-400 flex-shrink-0 mt-0.5" size={20} />
                  <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
                </div>
              )}

              {/* Action Buttons */}
              {selectedFile && (
                <div className="flex gap-4">
                  <Button
                    variant="primary"
                    size="lg"
                    isLoading={isLoading}
                    onClick={handleUpload}
                    className="flex-1"
                  >
                    Upload & Continue
                  </Button>
                  <Button
                    variant="secondary"
                    size="lg"
                    onClick={() => {
                      setSelectedFile(null);
                      setError(null);
                    }}
                  >
                    Cancel
                  </Button>
                </div>
              )}

              {/* Tips */}
              <div className="p-6 bg-gray-100 dark:bg-gray-800 rounded-lg border border-gray-300 dark:border-gray-700">
                <h3 className="font-semibold text-gray-900 dark:text-white mb-4">Tips for Best Results</h3>
                <ul className="space-y-2 text-sm text-gray-700 dark:text-gray-300">
                  <li>• Use a clean, well-formatted resume for better ATS parsing</li>
                  <li>• Ensure all sections (contact, experience, skills) are clearly labeled</li>
                  <li>• Include relevant keywords from your target industry</li>
                  <li>• Use standard fonts and avoid excessive formatting</li>
                </ul>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};
