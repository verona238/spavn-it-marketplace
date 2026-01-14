import { api } from '../api/client';

/**
 * Сервис для работы с корзиной
 */
export const cartService = {
  /**
   * Получить корзину текущего пользователя
   */
  async getMyCart() {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Необходима авторизация');
      }

      const response = await api.request('/cart', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return response;
    } catch (error) {
      console.error('Ошибка получения корзины:', error);
      throw error;
    }
  },

  /**
   * Добавить товар в корзину
   * @param {number} productId - ID товара
   * @param {number} quantity - Количество (по умолчанию 1)
   */
  async addToCart(productId, quantity = 1) {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Необходима авторизация для добавления в корзину');
      }

      const response = await api.request('/cart/add', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          productId,
          quantity
        })
      });

      return response;
    } catch (error) {
      console.error('Ошибка добавления в корзину:', error);
      throw error;
    }
  },

  /**
   * Удалить товар из корзины
   * @param {number} cartItemId - ID элемента корзины
   */
  async removeFromCart(cartItemId) {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Необходима авторизация');
      }

      const response = await api.request(`/cart/${cartItemId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return response;
    } catch (error) {
      console.error('Ошибка удаления из корзины:', error);
      throw error;
    }
  },

  /**
   * Обновить количество товара в корзине
   * @param {number} cartItemId - ID элемента корзины
   * @param {number} quantity - Новое количество
   */
  async updateQuantity(cartItemId, quantity) {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Необходима авторизация');
      }

      const response = await api.request(`/cart/${cartItemId}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ quantity })
      });

      return response;
    } catch (error) {
      console.error('Ошибка обновления корзины:', error);
      throw error;
    }
  },

  /**
   * Очистить корзину
   */
  async clearCart() {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Необходима авторизация');
      }

      const response = await api.request('/cart/clear', {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return response;
    } catch (error) {
      console.error('Ошибка очистки корзины:', error);
      throw error;
    }
  },

  /**
   * Получить количество товаров в корзине
   */
  async getCartCount() {
    try {
      const cart = await this.getMyCart();
      if (cart && cart.items) {
        return cart.items.reduce((sum, item) => sum + item.quantity, 0);
      }
      return 0;
    } catch (error) {
      console.error('Ошибка получения количества товаров:', error);
      return 0;
    }
  }
};