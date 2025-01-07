import http from '@/http';

export const useGetEssayByUserId = (userId: string) => {
  return http.get(`/essay/${userId}`);
};