import { api } from "./api";
import type { Receita } from "@/types";

export const favoritoService = {
  listar: () => api.get<Receita[]>("/users/favorites"),

  adicionar: (recipeId: Receita["id"]) =>
    api.post<void>(`/users/favorites/${recipeId}`, {}),

  remover: (recipeId: Receita["id"]) =>
    api.delete<void>(`/users/favorites/${recipeId}`),
};
