export interface User {
  id: string;
  email: string;
  name: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Resume {
  id: string;
  userId: string;
  fileName: string;
  fileUrl: string;
  uploadedAt: string;
  lastAnalyzed?: string;
}

export interface JobDescription {
  id: string;
  userId: string;
  title: string;
  content: string;
  company?: string;
  createdAt: string;
}

export interface AnalysisResult {
  id: string;
  resumeId: string;
  jobDescriptionId: string;
  overallScore: number;
  scores: {
    skillsMatch: number;
    experienceRelevance: number;
    educationAlignment: number;
    keywordOptimization: number;
    formattingCompliance: number;
    achievements: number;
  };
  matchedSkills: Skill[];
  missingSkills: Skill[];
  atsIssues: ATSIssue[];
  recommendations: string[];
  createdAt: string;
  resumeFileName: string;
  jobTitle: string;
}

export interface Skill {
  name: string;
  proficiency?: string;
  frequency?: number;
}

export interface ATSIssue {
  type: 'formatting' | 'keyword' | 'structure' | 'content';
  severity: 'critical' | 'warning' | 'info';
  title: string;
  description: string;
  suggestion: string;
}

export interface OptimizedResume {
  id: string;
  analysisId: string;
  originalContent: string;
  optimizedContent: string;
  changes: Change[];
  createdAt: string;
}

export interface Change {
  type: 'added' | 'removed' | 'modified';
  section: string;
  before: string;
  after: string;
  impact: 'high' | 'medium' | 'low';
}

export interface DashboardStats {
  totalAnalyses: number;
  averageScore: number;
  totalOptimizations: number;
  recentAnalyses: AnalysisResult[];
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  limit: number;
}
