import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../core/api/axios";
import { useAuth } from "./AuthContext";

function Login() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await api.post("/auth/login", form);
      login(res.data);
      navigate(res.data.role === "ADMIN" ? "/admin" : "/dashboard");
    } catch (err) {
      setError(err.response?.data || "That username or password doesn't match our records.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <div className="wordmark">
          <div className="wordmark-mark" />
          <span className="wordmark-text">StudyMate</span>
        </div>
        <p className="auth-subtitle">Log in to pick up where you left off.</p>

        {error && <div className="error-banner">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              type="text"
              name="username"
              autoComplete="username"
              value={form.username}
              onChange={handleChange}
              required
            />
          </div>

          <div className="field password-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type={showPassword ? "text" : "password"}
              name="password"
              autoComplete="current-password"
              value={form.password}
              onChange={handleChange}
              required
            />
            <button
              type="button"
              className="password-toggle"
              onClick={() => setShowPassword((v) => !v)}
            >
              {showPassword ? "Hide" : "Show"}
            </button>
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Logging in…" : "Log In"}
          </button>
        </form>

        <p className="auth-switch">
          New to StudyMate? <Link to="/register">Create an account</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
