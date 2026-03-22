import { api } from "./api";
import type { RespostaAuth, Usuario } from "@/types";

export const authService = {
  login: (email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/login", { email, senha }),

  cadastrar: (nome: string, email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/cadastrar", { nome, email, senha }),

  atualizarPerfil: (id: number, dados: Partial<Usuario>) =>
    api.put<Usuario>(`/users/${id}`, dados),
};
