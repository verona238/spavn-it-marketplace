import { api } from '../api/client';

export const cartService = {
  async getUserCart(userId) {
    return api.get(`/cart/${userId}`);
  },

  async addToCart(userId, productId, quantity = 1) {
    return api.post('/cart', { userId, productId, quantity });
  },

  async removeFromCart(userId, productId) {
    return api.delete(`/cart/${userId}/${productId}`);
  },

  async updateCartItem(userId, productId, quantity) {
    return api.put(`/cart/${userId}/${productId}`, { quantity });
  },

  async clearCart(userId) {
    return api.delete(`/cart/${userId}`);
  }
};