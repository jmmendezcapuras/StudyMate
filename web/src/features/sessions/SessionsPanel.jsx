import { useEffect, useState } from "react";
import { fetchSessions, createSession, deleteSession } from "./sessions.api";

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

function formatDate(iso) {
  const d = new Date(`${iso}T00:00:00`);
  return d.toLocaleDateString(undefined, { month: "short", day: "numeric", year: "numeric" });
}

// SessionsPanel is the study-session slice's UI. It only needs the list of
// subjects (owned by the subject slice) to populate the subject dropdown —
// this is the session slice's one intentional cross-slice dependency,
// mirroring how the subject slice depends on the auth slice's AuthContext.
function SessionsPanel({ userId, subjects }) {
  const [sessions, setSessions] = useState([]);
  const [loadingData, setLoadingData] = useState(true);
  const [loadError, setLoadError] = useState("");

  const [subjectId, setSubjectId] = useState("");
  const [duration, setDuration] = useState("");
  const [sessionDate, setSessionDate] = useState(todayIso());
  const [notes, setNotes] = useState("");
  const [formError, setFormError] = useState("");
  const [saving, setSaving] = useState(false);

  const loadSessions = async () => {
    setLoadError("");
    try {
      const res = await fetchSessions(userId);
      setSessions(res.data);
    } catch (err) {
      // err.response means the backend actually replied (e.g. a transient
      // 500 from a DB error, or a 401/403) — show that, not a false
      // "can't reach the server" message. Only a missing response (network
      // failure, backend genuinely down, CORS block) should say that.
      if (err.response) {
        setLoadError(err.response.data?.error || err.response.data || `Request failed (${err.response.status}).`);
      } else {
        setLoadError("Couldn't reach the StudyMate server. Is the backend running?");
      }
    } finally {
      setLoadingData(false);
    }
  };

  useEffect(() => {
    loadSessions();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  useEffect(() => {
    if (!subjectId && subjects.length > 0) {
      setSubjectId(String(subjects[0].id));
    }
  }, [subjects, subjectId]);

  const handleAddSession = async (e) => {
    e.preventDefault();
    setFormError("");

    if (!subjectId) {
      setFormError("Add a subject first, then log a session against it.");
      return;
    }
    const durationNum = Number(duration);
    if (!duration || durationNum <= 0) {
      setFormError("Enter a duration greater than 0 minutes.");
      return;
    }
    if (!sessionDate) {
      setFormError("Pick a date for this session.");
      return;
    }

    setSaving(true);
    try {
      const res = await createSession(userId, {
        subjectId: Number(subjectId),
        durationMinutes: durationNum,
        sessionDate,
        notes: notes.trim() || null,
      });
      setSessions((prev) => [res.data, ...prev]);
      setDuration("");
      setNotes("");
      setSessionDate(todayIso());
    } catch (err) {
      setFormError(err.response?.data || "Couldn't log that session.");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteSession(userId, id);
      setSessions((prev) => prev.filter((s) => s.id !== id));
    } catch (err) {
      setLoadError(err.response?.data || "Couldn't delete that session.");
    }
  };

  return (
    <div className="panel">
      <h2 className="panel-title">Study Sessions</h2>
      <p className="panel-subtitle">Log time you've spent studying each subject.</p>

      {loadError && <div className="error-banner">{loadError}</div>}

      <form onSubmit={handleAddSession}>
        <div className="field-row">
          <div className="field">
            <label>Subject</label>
            <select value={subjectId} onChange={(e) => setSubjectId(e.target.value)}>
              {subjects.length === 0 && <option value="">Add a subject first</option>}
              {subjects.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.name}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Duration (min)</label>
            <input
              type="number"
              min="1"
              placeholder="e.g. 45"
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
            />
          </div>
          <div className="field">
            <label>Date</label>
            <input
              type="date"
              value={sessionDate}
              onChange={(e) => setSessionDate(e.target.value)}
            />
          </div>
        </div>

        <div className="field" style={{ marginBottom: 8 }}>
          <label>Notes (optional)</label>
          <textarea
            placeholder="What did you work on?"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            rows={2}
          />
        </div>

        {formError && <div className="error-banner">{formError}</div>}

        <button
          type="submit"
          className="btn btn-secondary btn-small"
          disabled={saving || subjects.length === 0}
        >
          {saving ? "Saving…" : "+ Log Session"}
        </button>
      </form>

      <div className="subject-list">
        {!loadingData && sessions.length === 0 && (
          <p className="empty-hint">No study sessions logged yet.</p>
        )}
        {loadingData && <p className="empty-hint">Loading your sessions…</p>}
        {sessions.map((s) => (
          <div className="subject-chip" key={s.id} style={{ justifyContent: "space-between" }}>
            <span>
              <strong>{s.subject?.name ?? "Subject"}</strong> — {s.durationMinutes} min on{" "}
              {formatDate(s.sessionDate)}
              {s.notes ? ` — ${s.notes}` : ""}
            </span>
            <button
              type="button"
              className="icon-btn"
              aria-label="Delete session"
              onClick={() => handleDelete(s.id)}
            >
              ✕
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default SessionsPanel;
