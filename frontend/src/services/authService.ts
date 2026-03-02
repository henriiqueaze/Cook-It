import { api } from "./api";
import type { RespostaAuth } from "@/types";

export const authService = {
  login: (email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/login", { email, senha }),

  cadastrar: (nome: string, email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/cadastrar", { nome, email, senha }),
};
