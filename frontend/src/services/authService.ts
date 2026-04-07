import { api } from "./api";
import type { RespostaAuth, Usuario } from "@/types";

export const authService = {
  login: (email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/login", {
      email,
      senha,
      password: senha,
    }),

  cadastrar: (nome: string, email: string, senha: string) =>
    api.post<RespostaAuth>("/auth/register", {
      nome,
      name: nome,
      email,
      senha,
      password: senha,
    }),

  logout: () => api.post<void>("/auth/logout", {}),

  me: () => api.get<Usuario>("/auth/me"),

  forgotPassword: (email: string) =>
    api.post<void>("/auth/forgot-password", { email }),

  resetPassword: (token: string, senha: string) =>
    api.post<void>("/auth/reset-password", {
      token,
      senha,
      password: senha,
    }),

  atualizarPerfil: async (
    id: Usuario["id"],
    dados: { name: string; email: string; photoFile?: File | null },
  ) => {
    const formData = new FormData();

    formData.append(
      "data",
      new Blob(
        [
          JSON.stringify({
            name: dados.name,
            email: dados.email,
          }),
        ],
        { type: "application/json" },
      ),
    );

    if (dados.photoFile) {
      formData.append("photo", dados.photoFile);
    }

    return api.put<Usuario>(`/users/${id}`, formData);
  },
};