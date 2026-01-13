import { api } from '../api/client';

export const userService = {
  async getAllUsers() {
    return api.get('/users');
  },

  async getUserById(id) {
    return api.get(`/users/${id}`);
  },

  async updateUser(id, userData) {
    return api.put(`/users/${id}`, userData);
  },

  async deleteUser(id) {
    return api.delete(`/users/${id}`);
  },

  async getUserProfile() {
    return api.get('/users/profile');
  },

  async updateUserProfile(userData) {
    return api.put('/users/profile', userData);
  }
};