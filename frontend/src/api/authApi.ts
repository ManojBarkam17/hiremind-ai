import axiosInstance from './axiosConfig';
import type { AuthResponse, User } from '../types';

export const authApi = {
  login: async (email: string, password: string): Promise<AuthResponse> => {
    const response = await axiosInstance.post('/auth/login', { email, password });
    return response.data;
  },

  register: async (email: string, password: string, name: string): Promise<AuthResponse> => {
    const response = await axiosInstance.post('/auth/register', { email, password, name });
    return response.data;
  },

  getProfile: async (): Promise<User> => {
    const response = await axiosInstance.get('/auth/profile');
    return response.data;
  },

  logout: async (): Promise<void> => {
    await axiosInstance.post('/auth/logout');
  },
};
