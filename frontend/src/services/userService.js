import { api } from '../api/client';

export const userService = {
  /**
   * Получить текущего авторизованного пользователя
   */
  async getCurrentUser() {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Необходима авторизация');
    }

    return api.request('/users/profile', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getAllUsers() {
    const token = localStorage.getItem('token');
    return api.request('/users', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getUserById(id) {
    const token = localStorage.getItem('token');
    return api.request(`/users/${id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async updateUser(id, userData) {
    const token = localStorage.getItem('token');
    return api.request(`/users/${id}`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(userData)
    });
  },

  async deleteUser(id) {
    const token = localStorage.getItem('token');
    return api.request(`/users/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getUserProfile() {
    return this.getCurrentUser();
  },

  async updateUserProfile(userData) {
    const token = localStorage.getItem('token');
    return api.request('/users/profile', {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(userData)
    });
  }
};