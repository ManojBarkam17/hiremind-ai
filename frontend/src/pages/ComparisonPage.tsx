import React, { useState } from 'react';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { Card } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Link } from 'react-router-dom';

export const ComparisonPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'before' | 'after'>('before');

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            <div className="max-w-6xl mx-auto space-y-8">
              {/* Header */}
              <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Resume Comparison</h1>
                <p className="text-gray-600 dark:text-gray-400 mt-2">See exactly what changed in your optimized resume.</p>
              </div>

              {/* Tabs */}
              <div className="flex gap-4 border-b border-gray-200 dark:border-gray-700">
                {['before', 'after'].map((tab) => (
                  <button
                    key={tab}
                    onClick={() => setActiveTab(tab as any)}
                    className={`px-4 py-3 font-medium border-b-2 transition-colors ${
                      activeTab === tab
                        ? 'border-indigo-600 text-indigo-600 dark:text-indigo-400'
                        : 'border-transparent text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-gray-300'
                    }`}
                  >
                    {tab === 'before' ? 'Original Resume' : 'Optimized Resume'}
                  </button>
                ))}
              </div>

              {/* Content */}
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* Original */}
                {activeTab === 'before' && (
                  <Card>
                    <div className="space-y-4">
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">Original Resume</h3>
                        <p className="text-sm text-gray-600 dark:text-gray-400">Your current resume before optimization</p>
                      </div>
                      <div className="bg-gray-50 dark:bg-gray-900/50 rounded-lg p-6 border border-gray-200 dark:border-gray-700 h-96 overflow-y-auto">
                        <div className="text-xs text-gray-700 dark:text-gray-300 space-y-2 whitespace-pre-wrap font-mono">
                          {`JOHN DOE
San Francisco, CA | john@example.com | (555) 123-4567

EXPERIENCE

Software Engineer - TechCorp (2020-Present)
- Developed backend services
- Worked with Python and AWS
- Managed team of 3 developers

Junior Developer - StartupXYZ (2018-2020)
- Built web applications
- Collaborated with product team

EDUCATION
BS Computer Science - State University (2018)

SKILLS
Python, JavaScript, AWS, Docker`}
                        </div>
                      </div>
                    </div>
                  </Card>
                )}

                {/* Optimized */}
                {activeTab === 'after' && (
                  <Card className="bg-gradient-to-br from-green-50 dark:from-green-900/10 to-transparent">
                    <div className="space-y-4">
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">Optimized Resume</h3>
                        <p className="text-sm text-gray-600 dark:text-gray-400">AI-enhanced version with improvements</p>
                      </div>
                      <div className="bg-white dark:bg-gray-800 rounded-lg p-6 border border-green-200 dark:border-green-900 h-96 overflow-y-auto">
                        <div className="text-xs text-gray-700 dark:text-gray-300 space-y-2 whitespace-pre-wrap font-mono">
                          {`JOHN DOE
Senior Software Engineer
San Francisco, CA | john@example.com | (555) 123-4567 | linkedin.com/in/johndoe

PROFESSIONAL SUMMARY
Results-driven Senior Software Engineer with 5+ years of experience
designing and implementing scalable backend systems. Proven track
record of leading engineering teams and delivering enterprise solutions.

EXPERIENCE

Senior Software Engineer - TechCorp (2020-Present)
- Architected and deployed microservices handling 10M+ daily requests
- Led technical team of 4 engineers, mentoring 2 junior developers
- Optimized database queries, reducing API latency by 40%
- Tech stack: Python, PostgreSQL, AWS (EC2, RDS, Lambda, S3), Docker, Kubernetes

Software Developer - StartupXYZ (2018-2020)
- Built and maintained full-stack web applications using React and Django
- Implemented CI/CD pipelines, reducing deployment time by 60%

EDUCATION
BS Computer Science - State University (2018)

SKILLS
Backend: Python, Java, Go, SQL, PostgreSQL
Cloud & DevOps: AWS, Docker, Kubernetes, Jenkins, GitHub Actions
Frontend: JavaScript, React, TypeScript
Soft Skills: Leadership, Technical Mentoring, System Design`}
                        </div>
                      </div>
                    </div>
                  </Card>
                )}

                {/* Stats */}
                <div className="space-y-4">
                  <Card>
                    <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Key Improvements</h3>
                    <ul className="space-y-3">
                      <li className="flex gap-3">
                        <span className="text-green-600 dark:text-green-400 font-bold">✓</span>
                        <div>
                          <p className="font-medium text-gray-900 dark:text-white">Added Professional Summary</p>
                          <p className="text-sm text-gray-600 dark:text-gray-400">Highlights key achievements upfront</p>
                        </div>
                      </li>
                      <li className="flex gap-3">
                        <span className="text-green-600 dark:text-green-400 font-bold">✓</span>
                        <div>
                          <p className="font-medium text-gray-900 dark:text-white">Quantified Achievements</p>
                          <p className="text-sm text-gray-600 dark:text-gray-400">Added metrics: 10M+ requests, 40% latency reduction</p>
                        </div>
                      </li>
                      <li className="flex gap-3">
                        <span className="text-green-600 dark:text-green-400 font-bold">✓</span>
                        <div>
                          <p className="font-medium text-gray-900 dark:text-white">Better Keywords</p>
                          <p className="text-sm text-gray-600 dark:text-gray-400">Included: architecture, microservices, leadership</p>
                        </div>
                      </li>
                      <li className="flex gap-3">
                        <span className="text-green-600 dark:text-green-400 font-bold">✓</span>
                        <div>
                          <p className="font-medium text-gray-900 dark:text-white">Organized Skills Section</p>
                          <p className="text-sm text-gray-600 dark:text-gray-400">Categorized by Backend, Cloud, Frontend</p>
                        </div>
                      </li>
                      <li className="flex gap-3">
                        <span className="text-green-600 dark:text-green-400 font-bold">✓</span>
                        <div>
                          <p className="font-medium text-gray-900 dark:text-white">Added LinkedIn Profile</p>
                          <p className="text-sm text-gray-600 dark:text-gray-400">Recruiters can easily find your profile</p>
                        </div>
                      </li>
                    </ul>
                  </Card>

                  <Card>
                    <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Impact</h3>
                    <div className="space-y-3">
                      <div>
                        <div className="flex justify-between items-center mb-2">
                          <p className="text-sm font-medium text-gray-700 dark:text-gray-300">ATS Score</p>
                          <p className="text-sm font-bold text-indigo-600 dark:text-indigo-400">72% → 89%</p>
                        </div>
                        <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                          <div className="bg-indigo-600 dark:bg-indigo-400 h-2 rounded-full" style={{ width: '89%' }} />
                        </div>
                      </div>
                      <div>
                        <div className="flex justify-between items-center mb-2">
                          <p className="text-sm font-medium text-gray-700 dark:text-gray-300">Keyword Match</p>
                          <p className="text-sm font-bold text-indigo-600 dark:text-indigo-400">65% → 91%</p>
                        </div>
                        <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                          <div className="bg-indigo-600 dark:bg-indigo-400 h-2 rounded-full" style={{ width: '91%' }} />
                        </div>
                      </div>
                    </div>
                  </Card>

                  <Link to="/history">
                    <Button variant="primary" className="w-full">
                      View Your Analyses
                    </Button>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};
