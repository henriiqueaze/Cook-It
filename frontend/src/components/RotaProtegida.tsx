import type { ReactNode } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";

interface RotaProtegidaProps {
  children: ReactNode;
}

export function RotaProtegida({ children }: RotaProtegidaProps) {
  const { estaAutenticado } = useAuth();
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

  return <>{children}</>;
}
