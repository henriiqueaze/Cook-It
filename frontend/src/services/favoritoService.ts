import { api } from "./api";
import type { Receita } from "@/types";

export const favoritoService = {
  listar: async (): Promise<string[]> => api.get<string[]>("/users/favorites"),

  adicionar: (recipeId: Receita["id"]) =>
    api.post<void>(`/users/favorites/${recipeId}`, {}),

  remover: (recipeId: Receita["id"]) =>
    api.delete<void>(`/users/favorites/${recipeId}`),
};
