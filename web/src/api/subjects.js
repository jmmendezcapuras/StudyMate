import api from "./axios";

export function fetchSubjects(userId) {
  return api.get("/subjects", { params: { userId } });
}

export function createSubject(userId, name) {
  return api.post("/subjects", { name }, { params: { userId } });
}
