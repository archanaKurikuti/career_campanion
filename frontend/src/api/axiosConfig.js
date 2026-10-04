import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const redirectedToLogin = (
      error.response && error.response.status === 401
    ) || (
      error.request && typeof error.request.responseURL === 'string' && error.request.responseURL.includes('/login')
    );

    if (redirectedToLogin) {
      const isAuthPage = window.location.pathname.includes('/login') || window.location.pathname.includes('/register');
      if (!isAuthPage) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
