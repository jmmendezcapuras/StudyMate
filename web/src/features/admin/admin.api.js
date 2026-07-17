import api from "../../core/api/axios";

export function fetchAllUsers() {
  return api.get("/admin/users");
}

export function createAdmin(username, email, password) {
  return api.post("/admin/users", { username, email, password });
}

export function deleteUserAccount(userId) {
  return api.delete(`/admin/users/${userId}`);
}
