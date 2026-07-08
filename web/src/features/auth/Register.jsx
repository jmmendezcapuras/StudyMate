import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../core/api/axios";
import { useAuth } from "./AuthContext";

function Register() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [confirmPassword, setConfirmPassword] = useState("");
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

    if (form.password !== confirmPassword) {
      setError("Passwords don't match.");
      return;
    }

    setLoading(true);
    try {
      const res = await api.post("/auth/register", form);
      login(res.data);
      navigate("/dashboard");
    } catch (err) {
      setError(err.response?.data || "Registration failed. Please try again.");
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
        <p className="auth-subtitle">Create an account to start tracking your subjects.</p>

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
              autoComplete="new-password"
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

          <div className="field">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              id="confirmPassword"
              type={showPassword ? "text" : "password"}
              name="confirmPassword"
              autoComplete="new-password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Creating account…" : "Register"}
          </button>
        </form>

        <p className="auth-switch">
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
