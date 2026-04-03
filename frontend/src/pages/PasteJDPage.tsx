import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { Button } from '../components/common/Button';
import { AlertCircle, Copy, Check } from 'lucide-react';

export const PasteJDPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const resumeId = searchParams.get('resumeId');

  const [jobTitle, setJobTitle] = useState('');
  const [company, setCompany] = useState('');
  const [jobDescription, setJobDescription] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [copied, setCopied] = useState(false);

  const exampleJD = `Senior Software Engineer

Company: Tech Corp
Location: San Francisco, CA

About the Role:
We are looking for a talented Senior Software Engineer to join our growing team. You will work on scalable backend systems serving millions of users.

Requirements:
- 5+ years of software development experience
- Strong knowledge of Python, Java, or Go
- Experience with microservices architecture
- Familiarity with Docker and Kubernetes
- Experience with cloud platforms (AWS, GCP, Azure)
- Strong problem-solving skills

Responsibilities:
- Design and implement backend services
- Collaborate with cross-functional teams
- Mentor junior developers
- Participate in code reviews
- Contribute to system design and architecture`;

  const copyExample = () => {
    navigator.clipboard.writeText(exampleJD);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleAnalyze = async () => {
    if (!jobDescription.trim()) {
      setError('Please paste the job description');
      return;
    }

    if (!resumeId) {
      setError('Resume ID is missing. Please upload a resume first.');
      return;
    }

    setIsLoading(true);
    try {
      // For now, navigate to analysis results
      // In production, this would create a job description and analyze the resume
      navigate(`/analysis?resumeId=${resumeId}&jobTitle=${encodeURIComponent(jobTitle || 'Untitled')}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Analysis failed');
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
            <div className="max-w-3xl mx-auto space-y-8">
              {/* Header */}
              <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Paste Job Description</h1>
                <p className="text-gray-600 dark:text-gray-400 mt-2">
                  Paste the job description you're interested in. We'll analyze how well your resume matches.
                </p>
              </div>

              {!resumeId && (
                <div className="p-4 bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-900 rounded-lg flex gap-3">
                  <AlertCircle className="text-yellow-600 dark:text-yellow-400 flex-shrink-0 mt-0.5" size={20} />
                  <p className="text-sm text-yellow-700 dark:text-yellow-400">
                    No resume selected. Please go back and upload a resume first.
                  </p>
                </div>
              )}

              {error && (
                <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-900 rounded-lg flex gap-3">
                  <AlertCircle className="text-red-600 dark:text-red-400 flex-shrink-0 mt-0.5" size={20} />
                  <p className="text-sm text-red-700 dark:text-red-400">{error}</p>
                </div>
              )}

              <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* Form */}
                <div className="space-y-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Job Title
                    </label>
                    <input
                      type="text"
                      value={jobTitle}
                      onChange={(e) => setJobTitle(e.target.value)}
                      placeholder="e.g., Senior Software Engineer"
                      className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Company (Optional)
                    </label>
                    <input
                      type="text"
                      value={company}
                      onChange={(e) => setCompany(e.target.value)}
                      placeholder="e.g., Tech Corp"
                      className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Job Description
                    </label>
                    <textarea
                      value={jobDescription}
                      onChange={(e) => setJobDescription(e.target.value)}
                      placeholder="Paste the full job description here..."
                      rows={12}
                      className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 resize-none"
                    />
                  </div>

                  <div className="flex gap-3">
                    <Button
                      variant="primary"
                      size="lg"
                      isLoading={isLoading}
                      onClick={handleAnalyze}
                      className="flex-1"
                      disabled={!resumeId}
                    >
                      Analyze Resume
                    </Button>
                    <Button
                      variant="secondary"
                      size="lg"
                      onClick={() => navigate('/upload')}
                    >
                      Back
                    </Button>
                  </div>
                </div>

                {/* Example */}
                <div className="space-y-4">
                  <div className="p-6 bg-gradient-to-br from-indigo-50 to-blue-50 dark:from-indigo-900/20 dark:to-blue-900/20 border border-indigo-200 dark:border-indigo-900 rounded-lg">
                    <h3 className="font-semibold text-gray-900 dark:text-white mb-4">Example Job Description</h3>
                    <pre className="text-xs text-gray-700 dark:text-gray-300 whitespace-pre-wrap break-words max-h-64 overflow-y-auto">
                      {exampleJD}
                    </pre>
                    <Button
                      variant="secondary"
                      size="sm"
                      onClick={copyExample}
                      className="mt-4 w-full flex items-center justify-center gap-2"
                    >
                      {copied ? <Check size={16} /> : <Copy size={16} />}
                      {copied ? 'Copied!' : 'Copy Example'}
                    </Button>
                  </div>

                  <div className="p-6 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-900 rounded-lg">
                    <h3 className="font-semibold text-blue-900 dark:text-blue-200 mb-3">Tips</h3>
                    <ul className="space-y-2 text-sm text-blue-800 dark:text-blue-300">
                      <li>• Include the full job description for best results</li>
                      <li>• Include requirements and responsibilities sections</li>
                      <li>• The more detailed, the better the analysis</li>
                      <li>• Required qualifications are especially important</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};
