import api from "../../core/api/axios";

export function fetchSessions(userId) {
  return api.get("/sessions", { params: { userId } });
}

export function createSession(userId, { subjectId, durationMinutes, sessionDate, notes }) {
  return api.post(
    "/sessions",
    { subjectId, durationMinutes, sessionDate, notes },
    { params: { userId } }
  );
}

export function deleteSession(userId, sessionId) {
  return api.delete(`/sessions/${sessionId}`, { params: { userId } });
}
