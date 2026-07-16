import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

const STORAGE_KEY = "studymate_user";

// Attach the JWT issued at login/register to every outgoing request so the
// backend's JwtAuthFilter can authenticate the caller (NFR-006 / BR-008).
api.interceptors.request.use((config) => {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (raw) {
    const user = JSON.parse(raw);
    if (user?.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
  }
  return config;
});

// If the token is missing/expired/revoked, the backend responds 401.
// Clear the stale session and bounce back to login instead of showing a
// confusing error on a dead session.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(STORAGE_KEY);
      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;
