import { api } from '../api/client';

export const authService = {
  async login(email, password) {
    console.log('Отправка запроса на авторизацию:', { email });
    return api.post('/auth/login', { email, password });
  },

  async register(userData) {
    console.log('Отправка запроса на регистрацию:', { email: userData.email });
    return api.post('/auth/register', userData);
  },

  async logout() {
    return api.post('/auth/logout');
  },

  async getCurrentUser() {
    return api.get('/auth/me');
  }
};