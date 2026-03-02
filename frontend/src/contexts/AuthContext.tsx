import { createContext, useContext, useState, ReactNode } from "react";
import { Usuario } from "../types";

interface AuthContextData {
  usuario: Usuario | null;
  token: string | null;
  estaAutenticado: boolean;
  salvarAuth: (token: string, usuario: Usuario) => void;
  sair: () => void;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(() => {
    const salvo = localStorage.getItem("usuario");
    return salvo ? JSON.parse(salvo) : null;
  });

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem("token");
  });

  function salvarAuth(novoToken: string, novoUsuario: Usuario) {
    setToken(novoToken);
    setUsuario(novoUsuario);
    localStorage.setItem("token", novoToken);
    localStorage.setItem("usuario", JSON.stringify(novoUsuario));
  }

  function sair() {
    setToken(null);
    setUsuario(null);
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
  }

  return (
    <AuthContext.Provider
      value={{
        usuario,
        token,
        estaAutenticado: !!token,
        salvarAuth,
        sair,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
