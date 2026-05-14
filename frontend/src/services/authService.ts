import { api } from "./api";
import type { RespostaAuth, Usuario } from "@/types";

export const authService = {
  login: (email: string, password: string) =>
    api.post<RespostaAuth>("/auth/login", { email, password }),

  cadastrar: (name: string, email: string, password: string) =>
    api.post<RespostaAuth>("/auth/register", { name, email, password }),

  logout: () => api.post<void>("/auth/logout", {}),

  me: () => api.get<Usuario>("/auth/me"),

  alterarSenha: (senhaAtual: string, novaSenha: string) =>
    api.post<void>("/auth/change-password", {
      currentPassword: senhaAtual,
      newPassword: novaSenha,
    }),

  validarSenhaExclusao: (id: Usuario["id"], password: string) =>
    api.post<void>(`/users/${id}/validate-delete-password`, { password }),

  excluirUsuario: (id: Usuario["id"], password: string) =>
    api.delete<void>(`/users/${id}`, { password }),

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
