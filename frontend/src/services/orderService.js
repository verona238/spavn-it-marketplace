import { api } from '../api/client';

export const orderService = {
  /**
   * Получить заказы текущего пользователя
   */
  async getMyOrders() {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Необходима авторизация');
    }

    return api.request('/orders/my', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getAllOrders() {
    const token = localStorage.getItem('token');
    return api.request('/orders', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getUserOrders(userId) {
    const token = localStorage.getItem('token');
    return api.request(`/orders/user/${userId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async getOrderById(id) {
    const token = localStorage.getItem('token');
    return api.request(`/orders/${id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  async createOrder(orderData) {
    const token = localStorage.getItem('token');
    return api.request('/orders', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(orderData)
    });
  },

  async updateOrderStatus(id, status) {
    const token = localStorage.getItem('token');
    return api.request(`/orders/${id}/status`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ status })
    });
  },

  async cancelOrder(id) {
    const token = localStorage.getItem('token');
    return api.request(`/orders/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }
};