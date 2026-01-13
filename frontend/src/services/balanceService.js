import { api } from '../api/client';

export const balanceService = {
  async getUserBalance(userId) {
    return api.get(`/balance/${userId}`);
  },

  async addBalance(userId, amount) {
    return api.post('/balance/add', { userId, amount });
  },

  async withdrawBalance(userId, amount) {
    return api.post('/balance/withdraw', { userId, amount });
  }
};