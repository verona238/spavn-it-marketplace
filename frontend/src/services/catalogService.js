import { api } from '../api/client';

export const catalogService = {
  async getAllProducts() {
    console.log('Запрос всех товаров...');
    return api.get('/catalog/api/catalog');
  },

  async getProductById(id) {
    return api.get(`/catalog/api/catalog/${id}`);
  },

  async createProduct(productData) {
    return api.post('/catalog/api/catalog', productData);
  },

  async updateProduct(id, productData) {
    return api.put(`/catalog/api/catalog/${id}`, productData);
  },

  async deleteProduct(id) {
    return api.delete(`/catalog/api/catalog/${id}`);
  },

  async searchProducts(query) {
    return api.get(`/catalog/api/catalog/search?q=${query}`);
  }
};