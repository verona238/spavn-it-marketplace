import { api } from '../api/client';

export const balanceService = {
  /**
   * Получить баланс текущего пользователя
   */
  async getMyBalance() {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Необходима авторизация');
    }

    return api.request('/balance', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getUserBalance(userId) {
    const token = localStorage.getItem('token');
    return api.request(`/balance/${userId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async addBalance(userId, amount) {
    const token = localStorage.getItem('token');
    return api.request('/balance/add', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ userId, amount })
    });
  },

  async withdrawBalance(userId, amount) {
    const token = localStorage.getItem('token');
    return api.request('/balance/withdraw', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ userId, amount })
    });
  }
};