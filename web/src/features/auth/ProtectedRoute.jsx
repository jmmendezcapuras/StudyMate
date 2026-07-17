import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

function ProtectedRoute({ children, requireAdmin = false }) {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Admin routes are off-limits to regular students, and the seeded admin
  // account has no reason to be on the student dashboard either — keep
  // each role in its own lane on the client, on top of the server already
  // enforcing this via ROLE_ADMIN on /api/admin/**.
  if (requireAdmin && user.role !== "ADMIN") {
    return <Navigate to="/dashboard" replace />;
  }
  if (!requireAdmin && user.role === "ADMIN") {
    return <Navigate to="/admin" replace />;
  }

  return children;
}

export default ProtectedRoute;
