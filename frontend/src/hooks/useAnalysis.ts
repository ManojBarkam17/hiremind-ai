import { useState, useCallback } from 'react';
import type { AnalysisResult } from '../types';
import { analysisApi } from '../api/analysisApi';

export const useAnalysis = () => {
  const [analyses, setAnalyses] = useState<AnalysisResult[]>([]);
  const [currentAnalysis, setCurrentAnalysis] = useState<AnalysisResult | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const analyze = useCallback(async (resumeId: string, jobDescriptionId: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const result = await analysisApi.analyzeResume(resumeId, jobDescriptionId);
      setCurrentAnalysis(result);
      setAnalyses((prev) => [result, ...prev]);
      return result;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Analysis failed';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const fetchAnalysis = useCallback(async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const result = await analysisApi.getAnalysis(id);
      setCurrentAnalysis(result);
      return result;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to fetch analysis';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const fetchAnalyses = useCallback(async (page = 1, limit = 10) => {
    setIsLoading(true);
    setError(null);
    try {
      const result = await analysisApi.getAnalyses(page, limit);
      setAnalyses(result.data);
      return result;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to fetch analyses';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return {
    analyses,
    currentAnalysis,
    isLoading,
    error,
    analyze,
    fetchAnalysis,
    fetchAnalyses,
  };
};
