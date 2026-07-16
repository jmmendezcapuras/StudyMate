import api from "../../core/api/axios";

export function logoutUser() {
  return api.post("/auth/logout");
}
