import { api } from "./api";
import {
  adaptBackendRecipeListToReceitas,
  type BackendRecipeDTO,
} from "./receitaAdapter";
import type { Comentario, Usuario } from "@/types";

export interface AdminSummary {
  totalUsers: number;
  adminUsers: number;
  bannedUsers: number;
  totalRecipes: number;
  totalIngredients: number;
  totalComments: number;
  bannedWords: number;
}

export interface BannedWord {
  id: string;
  term: string;
  appliesToRecipes: boolean;
  appliesToIngredients: boolean;
  appliesToComments: boolean;
  createdAt?: string;
}

export interface CreateBannedWordPayload {
  term: string;
  appliesToRecipes: boolean;
  appliesToIngredients: boolean;
  appliesToComments: boolean;
}

export const adminService = {
  resumo: () => api.get<AdminSummary>("/admin/summary"),
  usuarios: () => api.get<Usuario[]>("/admin/users"),
  receitas: async () => {
    const resposta = await api.get<BackendRecipeDTO[]>("/admin/recipes");
    return adaptBackendRecipeListToReceitas(resposta as BackendRecipeDTO[]);
  },
  comentarios: () => api.get<Comentario[]>("/admin/comments"),
  palavrasBanidas: () => api.get<BannedWord[]>("/admin/banned-words"),
  criarPalavraBanida: (payload: CreateBannedWordPayload) =>
    api.post<BannedWord>("/admin/banned-words", payload),
  editarPalavraBanida: (id: string, payload: CreateBannedWordPayload) =>
    api.put<BannedWord>(`/admin/banned-words/${id}`, payload),
  removerPalavraBanida: (id: string) =>
    api.delete<void>(`/admin/banned-words/${id}`),
  banirUsuario: (id: string) =>
    api.put<Usuario>(`/admin/users/${id}/ban`, undefined),
  desbanirUsuario: (id: string) =>
    api.put<Usuario>(`/admin/users/${id}/unban`, undefined),
  promoverUsuario: (id: string) =>
    api.put<Usuario>(`/admin/users/${id}/promote`, undefined),
  removerReceita: (id: string) => api.delete<void>(`/admin/recipes/${id}`),
  removerComentario: (id: string) => api.delete<void>(`/admin/comments/${id}`),
};
