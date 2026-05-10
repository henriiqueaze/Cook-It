/* eslint-disable react-refresh/only-export-components */

import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import type { Usuario } from "@/types";

interface AuthContextData {
  usuario: Usuario | null;
  token: string | null;
  estaAutenticado: boolean;
  salvarAuth: (token: string, usuario: Usuario) => void;
  sair: () => void;
  atualizarUsuario: (usuario: Usuario) => void;
}

const AuthContext = createContext<AuthContextData | undefined>(undefined);

function readUsuario() {
  const salvo = localStorage.getItem("usuario");

  if (!salvo || salvo === "undefined") {
    return null;
  }

  try {
    return JSON.parse(salvo) as Usuario;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(() => readUsuario());
  const [token, setToken] = useState<string | null>(() =>
    localStorage.getItem("token"),
  );

  const salvarAuth = useCallback((novoToken: string, novoUsuario: Usuario) => {
    setToken(novoToken);
    setUsuario(novoUsuario);
    localStorage.setItem("token", novoToken);
    localStorage.setItem("usuario", JSON.stringify(novoUsuario));
  }, []);

  const sair = useCallback(() => {
    setToken(null);
    setUsuario(null);
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
  }, []);

  const atualizarUsuario = useCallback((novoUsuario: Usuario) => {
    setUsuario(novoUsuario);
    localStorage.setItem("usuario", JSON.stringify(novoUsuario));
  }, []);

  const value = useMemo(
    () => ({
      usuario,
      token,
      estaAutenticado: Boolean(token),
      salvarAuth,
      sair,
      atualizarUsuario,
    }),
    [usuario, token, salvarAuth, sair, atualizarUsuario],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider");
  }

  return context;
}
