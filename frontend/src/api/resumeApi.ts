import axiosInstance from './axiosConfig';
import type { Resume, PaginatedResponse } from '../types';

export const resumeApi = {
  uploadResume: async (file: File): Promise<Resume> => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axiosInstance.post('/resume/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },

  getResumes: async (page: number = 1, limit: number = 10): Promise<PaginatedResponse<Resume>> => {
    const response = await axiosInstance.get('/resume', { params: { page, limit } });
    return response.data;
  },

  getResume: async (id: string): Promise<Resume> => {
    const response = await axiosInstance.get(`/resume/${id}`);
    return response.data;
  },

  deleteResume: async (id: string): Promise<void> => {
    await axiosInstance.delete(`/resume/${id}`);
  },
};
