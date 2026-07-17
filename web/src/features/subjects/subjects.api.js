import api from "../../core/api/axios";

export function fetchSubjects(userId) {
  return api.get("/subjects", { params: { userId } });
}

export function createSubject(userId, name) {
  return api.post("/subjects", { name }, { params: { userId } });
}

export function deleteSubject(userId, subjectId) {
  return api.delete(`/subjects/${subjectId}`, { params: { userId } });
}
