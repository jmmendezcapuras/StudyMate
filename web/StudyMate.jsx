import { useState } from "react";

const COLORS = {
  mintBg: "#E8F5F0",
  lavender: "#EDE8F5",
  blush: "#F5E8EE",
  cream: "#FAFAF7",
  white: "#FFFFFF",
  mintAccent: "#7BBFAA",
  lavenderAccent: "#9B8EC4",
  blushAccent: "#D98FA0",
  text: "#3D4F58",
  textMuted: "#7A9099",
  textLight: "#A8BEC5",
  border: "#D6EAE3",
  borderFocus: "#7BBFAA",
  error: "#C0687A",
  errorBg: "#FCEEF1",
  success: "#5A9E87",
  successBg: "#EAF5F1",
};

const styles = {
  app: {
    minHeight: "100vh",
    background: `linear-gradient(135deg, ${COLORS.mintBg} 0%, ${COLORS.lavender} 50%, ${COLORS.blush} 100%)`,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    fontFamily: "'Segoe UI', system-ui, -apple-system, sans-serif",
    padding: "24px 16px",
  },
  logo: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginBottom: "32px",
  },
  logoIcon: {
    width: "56px",
    height: "56px",
    borderRadius: "16px",
    background: `linear-gradient(135deg, ${COLORS.mintAccent}, ${COLORS.lavenderAccent})`,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    marginBottom: "12px",
    boxShadow: "0 4px 16px rgba(123,191,170,0.35)",
  },
  logoText: {
    fontSize: "26px",
    fontWeight: "700",
    color: COLORS.text,
    letterSpacing: "-0.5px",
  },
  logoSub: {
    fontSize: "13px",
    color: COLORS.textMuted,
    marginTop: "2px",
    letterSpacing: "0.3px",
  },
  card: {
    background: COLORS.white,
    borderRadius: "24px",
    padding: "40px 40px 36px",
    width: "100%",
    maxWidth: "420px",
    boxShadow: "0 8px 40px rgba(60,80,100,0.10), 0 1.5px 4px rgba(60,80,100,0.06)",
  },
  tabs: {
    display: "flex",
    background: COLORS.cream,
    borderRadius: "12px",
    padding: "4px",
    marginBottom: "32px",
  },
  tab: {
    flex: 1,
    padding: "10px 0",
    border: "none",
    borderRadius: "9px",
    fontSize: "14px",
    fontWeight: "600",
    cursor: "pointer",
    transition: "all 0.2s ease",
    letterSpacing: "0.2px",
  },
  tabActive: {
    background: COLORS.white,
    color: COLORS.mintAccent,
    boxShadow: "0 2px 8px rgba(60,80,100,0.09)",
  },
  tabInactive: {
    background: "transparent",
    color: COLORS.textMuted,
  },
  formTitle: {
    fontSize: "22px",
    fontWeight: "700",
    color: COLORS.text,
    marginBottom: "6px",
    letterSpacing: "-0.3px",
  },
  formSubtitle: {
    fontSize: "13.5px",
    color: COLORS.textMuted,
    marginBottom: "28px",
    lineHeight: "1.5",
  },
  fieldGroup: {
    marginBottom: "18px",
  },
  label: {
    display: "block",
    fontSize: "13px",
    fontWeight: "600",
    color: COLORS.text,
    marginBottom: "7px",
    letterSpacing: "0.1px",
  },
  inputWrapper: {
    position: "relative",
    display: "flex",
    alignItems: "center",
  },
  inputIcon: {
    position: "absolute",
    left: "13px",
    color: COLORS.textLight,
    fontSize: "16px",
    pointerEvents: "none",
    userSelect: "none",
  },
  input: {
    width: "100%",
    padding: "12px 14px 12px 40px",
    border: `1.5px solid ${COLORS.border}`,
    borderRadius: "12px",
    fontSize: "14.5px",
    color: COLORS.text,
    background: COLORS.cream,
    outline: "none",
    transition: "border-color 0.2s, box-shadow 0.2s, background 0.2s",
    boxSizing: "border-box",
    fontFamily: "inherit",
  },
  button: {
    width: "100%",
    padding: "14px",
    border: "none",
    borderRadius: "12px",
    fontSize: "15px",
    fontWeight: "700",
    cursor: "pointer",
    transition: "all 0.2s ease",
    marginTop: "8px",
    letterSpacing: "0.2px",
    fontFamily: "inherit",
  },
  buttonLogin: {
    background: `linear-gradient(135deg, ${COLORS.mintAccent}, #5EA98E)`,
    color: COLORS.white,
    boxShadow: "0 4px 16px rgba(123,191,170,0.40)",
  },
  buttonRegister: {
    background: `linear-gradient(135deg, ${COLORS.lavenderAccent}, #7A6AAE)`,
    color: COLORS.white,
    boxShadow: "0 4px 16px rgba(155,142,196,0.40)",
  },
  buttonDisabled: {
    opacity: 0.65,
    cursor: "not-allowed",
  },
  alert: {
    padding: "12px 14px",
    borderRadius: "10px",
    fontSize: "13.5px",
    marginBottom: "20px",
    fontWeight: "500",
    lineHeight: "1.5",
  },
  alertError: {
    background: COLORS.errorBg,
    color: COLORS.error,
    border: `1px solid #F0C0C9`,
  },
  alertSuccess: {
    background: COLORS.successBg,
    color: COLORS.success,
    border: `1px solid #B5DDD0`,
  },
  switchText: {
    textAlign: "center",
    fontSize: "13.5px",
    color: COLORS.textMuted,
    marginTop: "20px",
  },
  switchLink: {
    color: COLORS.mintAccent,
    fontWeight: "600",
    cursor: "pointer",
    background: "none",
    border: "none",
    padding: "0",
    fontSize: "13.5px",
    fontFamily: "inherit",
    textDecoration: "underline",
    textDecorationColor: "transparent",
    transition: "text-decoration-color 0.2s",
  },
  strengthBar: {
    display: "flex",
    gap: "4px",
    marginTop: "8px",
  },
  strengthSegment: {
    flex: 1,
    height: "3px",
    borderRadius: "99px",
    transition: "background 0.3s",
  },
  strengthLabel: {
    fontSize: "11.5px",
    marginTop: "5px",
    fontWeight: "500",
  },
  footer: {
    marginTop: "28px",
    fontSize: "12px",
    color: COLORS.textLight,
    textAlign: "center",
    letterSpacing: "0.2px",
  },
  dashboard: {
    width: "100%",
    maxWidth: "700px",
  },
  dashHeader: {
    background: COLORS.white,
    borderRadius: "20px",
    padding: "24px 28px",
    marginBottom: "20px",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    boxShadow: "0 4px 20px rgba(60,80,100,0.08)",
  },
  dashWelcome: {
    fontSize: "20px",
    fontWeight: "700",
    color: COLORS.text,
  },
  dashSub: {
    fontSize: "13px",
    color: COLORS.textMuted,
    marginTop: "3px",
  },
  logoutBtn: {
    padding: "9px 18px",
    border: `1.5px solid ${COLORS.border}`,
    borderRadius: "10px",
    background: "transparent",
    color: COLORS.textMuted,
    fontSize: "13.5px",
    fontWeight: "600",
    cursor: "pointer",
    fontFamily: "inherit",
    transition: "all 0.2s",
  },
  statsRow: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr 1fr",
    gap: "14px",
    marginBottom: "20px",
  },
  statCard: {
    background: COLORS.white,
    borderRadius: "16px",
    padding: "20px",
    textAlign: "center",
    boxShadow: "0 2px 12px rgba(60,80,100,0.07)",
  },
  statNumber: {
    fontSize: "28px",
    fontWeight: "800",
    letterSpacing: "-1px",
  },
  statLabel: {
    fontSize: "12px",
    color: COLORS.textMuted,
    marginTop: "4px",
    fontWeight: "500",
  },
  emptyCard: {
    background: COLORS.white,
    borderRadius: "20px",
    padding: "48px 28px",
    textAlign: "center",
    boxShadow: "0 2px 12px rgba(60,80,100,0.07)",
  },
  emptyIcon: {
    fontSize: "40px",
    marginBottom: "14px",
  },
  emptyTitle: {
    fontSize: "16px",
    fontWeight: "700",
    color: COLORS.text,
    marginBottom: "6px",
  },
  emptyDesc: {
    fontSize: "13.5px",
    color: COLORS.textMuted,
    lineHeight: "1.6",
  },
};

function getPasswordStrength(pw) {
  if (!pw) return { score: 0, label: "", color: [] };
  let score = 0;
  if (pw.length >= 8) score++;
  if (/[A-Z]/.test(pw)) score++;
  if (/[0-9]/.test(pw)) score++;
  if (/[^A-Za-z0-9]/.test(pw)) score++;
  const labels = ["", "Weak", "Fair", "Good", "Strong"];
  const colors = [COLORS.border, COLORS.blushAccent, "#E0A860", COLORS.mintAccent, COLORS.lavenderAccent];
  return { score, label: labels[score], color: colors[score] };
}

function BookIcon() {
  return (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
      <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
      <line x1="12" y1="6" x2="16" y2="6" />
      <line x1="12" y1="10" x2="16" y2="10" />
    </svg>
  );
}

function InputField({ label, type, value, onChange, placeholder, icon, id }) {
  const [focused, setFocused] = useState(false);
  return (
    <div style={styles.fieldGroup}>
      <label htmlFor={id} style={styles.label}>{label}</label>
      <div style={styles.inputWrapper}>
        <span style={styles.inputIcon}>{icon}</span>
        <input
          id={id}
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          onFocus={() => setFocused(true)}
          onBlur={() => setFocused(false)}
          style={{
            ...styles.input,
            borderColor: focused ? COLORS.borderFocus : COLORS.border,
            boxShadow: focused ? `0 0 0 3px rgba(123,191,170,0.15)` : "none",
            background: focused ? COLORS.white : COLORS.cream,
          }}
        />
      </div>
    </div>
  );
}

function LoginForm({ onLogin, onSwitch }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLogin = () => {
    setError("");
    if (!username.trim() || !password.trim()) {
      setError("Please enter your username and password.");
      return;
    }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      if (username === "demo" && password === "demo123") {
        onLogin(username);
      } else {
        setError("Invalid username or password. Try username: demo / password: demo123");
      }
    }, 1200);
  };

  const handleKey = (e) => { if (e.key === "Enter") handleLogin(); };

  return (
    <>
      <div style={styles.formTitle}>Welcome back 👋</div>
      <div style={styles.formSubtitle}>Log in to continue tracking your study sessions.</div>

      {error && <div style={{ ...styles.alert, ...styles.alertError }}>⚠️ {error}</div>}

      <InputField id="login-user" label="Username" type="text" value={username}
        onChange={e => setUsername(e.target.value)} placeholder="Enter your username" icon="👤" />
      <div style={styles.fieldGroup}>
        <label htmlFor="login-pass" style={styles.label}>Password</label>
        <div style={styles.inputWrapper}>
          <span style={styles.inputIcon}>🔒</span>
          <input id="login-pass" type="password" value={password}
            onChange={e => setPassword(e.target.value)} onKeyDown={handleKey}
            placeholder="Enter your password"
            style={{ ...styles.input }}
          />
        </div>
      </div>

      <button onClick={handleLogin} disabled={loading}
        style={{ ...styles.button, ...styles.buttonLogin, ...(loading ? styles.buttonDisabled : {}) }}>
        {loading ? "Logging in…" : "Log In"}
      </button>

      <div style={styles.switchText}>
        Don't have an account?{" "}
        <button style={styles.switchLink} onClick={onSwitch}>Create one</button>
      </div>
      <div style={{ textAlign: "center", fontSize: "12px", color: COLORS.textLight, marginTop: "8px" }}>
        Demo: <strong>demo</strong> / <strong>demo123</strong>
      </div>
    </>
  );
}

function RegisterForm({ onRegister, onSwitch }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const strength = getPasswordStrength(password);

  const handleRegister = () => {
    setError(""); setSuccess("");
    if (!username.trim() || !email.trim() || !password || !confirm) {
      setError("All fields are required."); return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError("Please enter a valid email address."); return;
    }
    if (password.length < 6) {
      setError("Password must be at least 6 characters."); return;
    }
    if (password !== confirm) {
      setError("Passwords do not match."); return;
    }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      setSuccess("Account created! Redirecting to login…");
      setTimeout(() => onRegister(), 1400);
    }, 1400);
  };

  return (
    <>
      <div style={styles.formTitle}>Create account ✨</div>
      <div style={styles.formSubtitle}>Join StudyMate and start tracking your study sessions today.</div>

      {error && <div style={{ ...styles.alert, ...styles.alertError }}>⚠️ {error}</div>}
      {success && <div style={{ ...styles.alert, ...styles.alertSuccess }}>✅ {success}</div>}

      <InputField id="reg-user" label="Username" type="text" value={username}
        onChange={e => setUsername(e.target.value)} placeholder="Choose a username" icon="👤" />
      <InputField id="reg-email" label="Email" type="email" value={email}
        onChange={e => setEmail(e.target.value)} placeholder="your@email.com" icon="✉️" />

      <div style={styles.fieldGroup}>
        <label htmlFor="reg-pass" style={styles.label}>Password</label>
        <div style={styles.inputWrapper}>
          <span style={styles.inputIcon}>🔒</span>
          <input id="reg-pass" type="password" value={password}
            onChange={e => setPassword(e.target.value)} placeholder="Create a password"
            style={{ ...styles.input, borderColor: password && strength.score < 2 ? COLORS.blushAccent : COLORS.border }}
          />
        </div>
        {password && (
          <>
            <div style={styles.strengthBar}>
              {[1, 2, 3, 4].map(i => (
                <div key={i} style={{ ...styles.strengthSegment, background: i <= strength.score ? strength.color : COLORS.border }} />
              ))}
            </div>
            <div style={{ ...styles.strengthLabel, color: strength.color }}>{strength.label}</div>
          </>
        )}
      </div>

      <InputField id="reg-confirm" label="Confirm Password" type="password" value={confirm}
        onChange={e => setConfirm(e.target.value)} placeholder="Repeat your password" icon="🔒" />

      <button onClick={handleRegister} disabled={loading}
        style={{ ...styles.button, ...styles.buttonRegister, ...(loading ? styles.buttonDisabled : {}) }}>
        {loading ? "Creating account…" : "Create Account"}
      </button>

      <div style={styles.switchText}>
        Already have an account?{" "}
        <button style={styles.switchLink} onClick={onSwitch}>Log in</button>
      </div>
    </>
  );
}

function Dashboard({ username, onLogout }) {
  return (
    <div style={styles.dashboard}>
      <div style={styles.dashHeader}>
        <div>
          <div style={styles.dashWelcome}>Hi, {username}! 📚</div>
          <div style={styles.dashSub}>Here's your study overview</div>
        </div>
        <button style={styles.logoutBtn} onClick={onLogout}>Log out</button>
      </div>

      <div style={styles.statsRow}>
        {[
          { number: "0", label: "Sessions Logged", color: COLORS.mintAccent },
          { number: "0", label: "Subjects Added", color: COLORS.lavenderAccent },
          { number: "0h", label: "Total Study Time", color: COLORS.blushAccent },
        ].map(stat => (
          <div key={stat.label} style={styles.statCard}>
            <div style={{ ...styles.statNumber, color: stat.color }}>{stat.number}</div>
            <div style={styles.statLabel}>{stat.label}</div>
          </div>
        ))}
      </div>

      <div style={styles.emptyCard}>
        <div style={styles.emptyIcon}>📖</div>
        <div style={styles.emptyTitle}>No study sessions yet</div>
        <div style={styles.emptyDesc}>
          Once the Spring Boot + Supabase backend is connected,<br />
          your study sessions will appear here.
        </div>
      </div>
    </div>
  );
}

export default function StudyMate() {
  const [tab, setTab] = useState("login");
  const [loggedInUser, setLoggedInUser] = useState(null);

  if (loggedInUser) {
    return (
      <div style={styles.app}>
        <Dashboard username={loggedInUser} onLogout={() => setLoggedInUser(null)} />
        <div style={styles.footer}>StudyMate · edu.cit.capuras.studymate · IT342-G01</div>
      </div>
    );
  }

  return (
    <div style={styles.app}>
      <div style={styles.logo}>
        <div style={styles.logoIcon}><BookIcon /></div>
        <div style={styles.logoText}>StudyMate</div>
        <div style={styles.logoSub}>Student Study Planner</div>
      </div>

      <div style={styles.card}>
        <div style={styles.tabs}>
          {["login", "register"].map(t => (
            <button key={t} onClick={() => setTab(t)}
              style={{ ...styles.tab, ...(tab === t ? styles.tabActive : styles.tabInactive) }}>
              {t === "login" ? "Log In" : "Register"}
            </button>
          ))}
        </div>

        {tab === "login"
          ? <LoginForm onLogin={user => setLoggedInUser(user)} onSwitch={() => setTab("register")} />
          : <RegisterForm onRegister={() => setTab("login")} onSwitch={() => setTab("login")} />
        }
      </div>

      <div style={styles.footer}>
        StudyMate · edu.cit.capuras.studymate · IT342-G01 · ReactJS + Spring Boot + Supabase
      </div>
    </div>
  );
}
