import axiosInstance from './axiosConfig';
import type { AnalysisResult, DashboardStats, PaginatedResponse } from '../types';

export const analysisApi = {
  analyzeResume: async (resumeId: string, jobDescriptionId: string): Promise<AnalysisResult> => {
    const response = await axiosInstance.post('/analysis/analyze', {
      resumeId,
      jobDescriptionId,
    });
    return response.data;
  },

  getAnalysis: async (id: string): Promise<AnalysisResult> => {
    const response = await axiosInstance.get(`/analysis/${id}`);
    return response.data;
  },

  getAnalyses: async (page: number = 1, limit: number = 10): Promise<PaginatedResponse<AnalysisResult>> => {
    const response = await axiosInstance.get('/analysis', { params: { page, limit } });
    return response.data;
  },

  getDashboardStats: async (): Promise<DashboardStats> => {
    const response = await axiosInstance.get('/analysis/dashboard/stats');
    return response.data;
  },

  deleteAnalysis: async (id: string): Promise<void> => {
    await axiosInstance.delete(`/analysis/${id}`);
  },
};
