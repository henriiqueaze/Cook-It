import type { ReactNode } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";

interface RotaProtegidaProps {
  children: ReactNode;
  role?: "USER" | "ADMIN";
}

export function RotaProtegida({ children, role }: RotaProtegidaProps) {
  const { estaAutenticado, usuario } = useAuth();
  const location = useLocation();

  if (!estaAutenticado) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: `${location.pathname}${location.search}` }}
      />
    );
  }

  if (role && usuario?.role !== role) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}
