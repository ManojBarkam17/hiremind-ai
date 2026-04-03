import axiosInstance from './axiosConfig';
import type { OptimizedResume } from '../types';

export const optimizationApi = {
  optimizeResume: async (analysisId: string): Promise<OptimizedResume> => {
    const response = await axiosInstance.post('/optimization/optimize', { analysisId });
    return response.data;
  },

  getOptimizedResume: async (id: string): Promise<OptimizedResume> => {
    const response = await axiosInstance.get(`/optimization/${id}`);
    return response.data;
  },

  exportOptimizedResume: async (id: string, format: 'pdf' | 'docx'): Promise<Blob> => {
    const response = await axiosInstance.get(`/optimization/${id}/export`, {
      params: { format },
      responseType: 'blob',
    });
    return response.data;
  },
};
