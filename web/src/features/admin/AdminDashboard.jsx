import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { logoutUser } from "../auth/auth.api";
import { fetchAllUsers, deleteUserAccount, createAdmin } from "./admin.api";

function AdminDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState("");
  const [actionError, setActionError] = useState("");
  const [deletingId, setDeletingId] = useState(null);
  const [confirmId, setConfirmId] = useState(null);

  const [newAdmin, setNewAdmin] = useState({ username: "", email: "", password: "" });
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState("");
  const [createSuccess, setCreateSuccess] = useState("");

  const loadUsers = async () => {
    setLoadError("");
    try {
      const res = await fetchAllUsers();
      setUsers(res.data);
    } catch (err) {
      if (err.response) {
        setLoadError(err.response.data?.error || err.response.data || `Request failed (${err.response.status}).`);
      } else {
        setLoadError("Couldn't reach the StudyMate server. Is the backend running?");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleDelete = async (targetUserId) => {
    setActionError("");
    setDeletingId(targetUserId);
    try {
      await deleteUserAccount(targetUserId);
      setUsers((prev) => prev.filter((u) => u.id !== targetUserId));
      setConfirmId(null);
    } catch (err) {
      setActionError(err.response?.data || "Couldn't delete that account.");
    } finally {
      setDeletingId(null);
    }
  };

  const handleCreateAdmin = async (e) => {
    e.preventDefault();
    setCreateError("");
    setCreateSuccess("");
    setCreating(true);
    try {
      const res = await createAdmin(newAdmin.username, newAdmin.email, newAdmin.password);
      setUsers((prev) => [...prev, res.data]);
      setCreateSuccess(`Admin account "${res.data.username}" created.`);
      setNewAdmin({ username: "", email: "", password: "" });
    } catch (err) {
      setCreateError(err.response?.data || "Couldn't create that admin account.");
    } finally {
      setCreating(false);
    }
  };

  const handleLogout = async () => {
    try {
      await logoutUser();
    } catch (err) {
      // Non-fatal: proceed with client-side logout regardless.
    }
    logout();
    navigate("/login");
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="wordmark">
          <div className="wordmark-mark" />
          <span className="wordmark-text">StudyMate</span>
          <span className="admin-badge">Admin</span>
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
          <h1>User Accounts</h1>
          <p>View every registered account and remove one if needed. Deleting a user also removes their subjects and study sessions.</p>
        </div>

        {loadError && <div className="error-banner">{loadError}</div>}
        {actionError && <div className="error-banner">{actionError}</div>}

        <div className="panel">
          <h2 className="panel-title">Create Admin Account</h2>
          <p className="empty-hint" style={{ padding: "0 0 12px" }}>
            Use this to give someone else (e.g. your instructor) their own admin login, instead of sharing the seeded account's password.
          </p>

          {createError && <div className="error-banner">{createError}</div>}
          {createSuccess && <div className="success-banner">{createSuccess}</div>}

          <form onSubmit={handleCreateAdmin} className="inline-form">
            <input
              type="text"
              placeholder="Username"
              value={newAdmin.username}
              onChange={(e) => setNewAdmin({ ...newAdmin, username: e.target.value })}
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={newAdmin.email}
              onChange={(e) => setNewAdmin({ ...newAdmin, email: e.target.value })}
              required
            />
            <input
              type="password"
              placeholder="Password (min 8 characters)"
              value={newAdmin.password}
              onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })}
              required
              minLength={8}
            />
            <button type="submit" className="btn btn-primary btn-small" disabled={creating}>
              {creating ? "Creating…" : "Create Admin"}
            </button>
          </form>
        </div>

        <div className="panel">
          <h2 className="panel-title">All Users</h2>
          {loading ? (
            <p className="empty-hint">Loading users…</p>
          ) : users.length === 0 ? (
            <p className="empty-hint">No users found.</p>
          ) : (
            <table className="admin-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.id}</td>
                    <td>{u.username}</td>
                    <td>{u.email}</td>
                    <td>
                      <span className={`role-pill role-${u.role?.toLowerCase()}`}>{u.role}</span>
                    </td>
                    <td className="admin-table-actions">
                      {u.role === "ADMIN" ? (
                        <span className="empty-hint">—</span>
                      ) : confirmId === u.id ? (
                        <>
                          <button
                            className="btn btn-danger btn-small"
                            disabled={deletingId === u.id}
                            onClick={() => handleDelete(u.id)}
                          >
                            {deletingId === u.id ? "Deleting…" : "Confirm delete"}
                          </button>
                          <button
                            className="btn btn-ghost btn-small"
                            onClick={() => setConfirmId(null)}
                          >
                            Cancel
                          </button>
                        </>
                      ) : (
                        <button
                          className="btn btn-ghost btn-small"
                          onClick={() => setConfirmId(u.id)}
                        >
                          Delete
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  );
}

export default AdminDashboard;
