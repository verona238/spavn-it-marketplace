import { api } from '../api/client';

export const notificationService = {
  async getAllNotifications() {
    return api.get('/notifications');
  },

  async getUserNotifications(userId) {
    return api.get(`/notifications/${userId}`);
  },

  async sendNotification(userId, message) {
    return api.post('/notifications', { userId, message });
  },

  async markAsRead(notificationId) {
    return api.put(`/notifications/${notificationId}/read`);
  },

  async deleteNotification(notificationId) {
    return api.delete(`/notifications/${notificationId}`);
  }
};