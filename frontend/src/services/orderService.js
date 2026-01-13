import { api } from '../api/client';

export const orderService = {
  async getAllOrders() {
    return api.get('/orders');
  },

  async getUserOrders(userId) {
    return api.get(`/orders/user/${userId}`);
  },

  async getOrderById(id) {
    return api.get(`/orders/${id}`);
  },

  async createOrder(orderData) {
    return api.post('/orders', orderData);
  },

  async updateOrderStatus(id, status) {
    return api.put(`/orders/${id}/status`, { status });
  },

  async cancelOrder(id) {
    return api.delete(`/orders/${id}`);
  }
};