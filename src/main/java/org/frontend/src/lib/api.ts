import axios from 'axios';

const API_BASE_URL = 'http://localhost:8082';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// Don't auto-redirect on 401 — let ProtectedRoute and components handle auth state.
// The interceptor only clears auth state for non-auth endpoints.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Don't redirect on 401 — just propagate the error.
    // ProtectedRoute handles session restoration via checkAuth.
    return Promise.reject(error);
  }
);

export default api;

export const getImageUrl = (imagemUrl?: string): string | undefined => {
  if (!imagemUrl) return undefined;
  // If it's already a full URL, return as-is
  if (imagemUrl.startsWith('http://') || imagemUrl.startsWith('https://')) {
    return imagemUrl;
  }
  // If it's a relative path like "/images/product.jpg" or "images/product.jpg"
  // These are served from the public folder
  if (imagemUrl.startsWith('/')) {
    return imagemUrl;
  }
  return `/${imagemUrl}`;
};
