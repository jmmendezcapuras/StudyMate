import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { fetchSubjects, createSubject } from "./subjects.api";
import SessionsPanel from "../sessions/SessionsPanel";

const SUBJECT_COLORS = ["#10665A", "#F2A93B", "#3454D1", "#C1443B", "#7A5AF8", "#0E9488"];

function colorForSubject(id) {
  return SUBJECT_COLORS[id % SUBJECT_COLORS.length];
}

function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [subjects, setSubjects] = useState([]);
  const [loadingData, setLoadingData] = useState(true);
  const [loadError, setLoadError] = useState("");

  const [newSubject, setNewSubject] = useState("");
  const [subjectError, setSubjectError] = useState("");
  const [addingSubject, setAddingSubject] = useState(false);

  const loadData = async () => {
    setLoadError("");
    try {
      const res = await fetchSubjects(user.id);
      setSubjects(res.data);
    } catch (err) {
      setLoadError("Couldn't reach the StudyMate server. Is the backend running?");
    } finally {
      setLoadingData(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddSubject = async (e) => {
    e.preventDefault();
    setSubjectError("");

    if (!newSubject.trim()) {
      setSubjectError("Give the subject a name first.");
      return;
    }

    setAddingSubject(true);
    try {
      const res = await createSubject(user.id, newSubject.trim());
      setSubjects((prev) => [...prev, res.data]);
      setNewSubject("");
    } catch (err) {
      setSubjectError(err.response?.data || "Couldn't add that subject.");
    } finally {
      setAddingSubject(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="wordmark">
          <div className="wordmark-mark" />
          <span className="wordmark-text">StudyMate</span>
        </div>
        <div className="topbar-right">
          <span className="topbar-username">
            Logged in as <strong>{user?.username}</strong>
          </span>
          <button className="btn btn-ghost btn-small" onClick={handleLogout}>
            Log Out
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <div className="dashboard-greeting">
          <h1>Welcome back, {user?.username}</h1>
          <p>Add the subjects you're studying to get started.</p>
        </div>

        {loadError && <div className="error-banner">{loadError}</div>}

        <div className="panel" style={{ maxWidth: 480 }}>
          <h2 className="panel-title">Subjects</h2>
          <p className="panel-subtitle">Add each course you're tracking.</p>

          <form onSubmit={handleAddSubject}>
            <div className="field" style={{ marginBottom: 8 }}>
              <input
                type="text"
                placeholder="e.g. Data Structures"
                value={newSubject}
                onChange={(e) => setNewSubject(e.target.value)}
              />
            </div>
            {subjectError && <div className="error-banner">{subjectError}</div>}
            <button type="submit" className="btn btn-secondary btn-small" disabled={addingSubject}>
              {addingSubject ? "Adding…" : "+ Add Subject"}
            </button>
          </form>

          <div className="subject-list">
            {!loadingData && subjects.length === 0 && (
              <p className="empty-hint">No subjects yet — add your first one above.</p>
            )}
            {loadingData && <p className="empty-hint">Loading your subjects…</p>}
            {subjects.map((s) => (
              <div className="subject-chip" key={s.id}>
                <span className="subject-dot" style={{ background: colorForSubject(s.id) }} />
                {s.name}
              </div>
            ))}
          </div>
        </div>

        <SessionsPanel userId={user.id} subjects={subjects} />
      </main>
    </div>
  );
}

export default Dashboard;
