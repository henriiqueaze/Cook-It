import { Navigate } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import type { ReactNode } from "react";

interface RotaProtegidaProps {
  children: ReactNode;
}

export function RotaProtegida({ children }: RotaProtegidaProps) {
  const { estaAutenticado } = useAuth();

  if (!estaAutenticado) {
    return <Navigate to={"/login"} replace />;
  }

  return <>{children}</>;
}
