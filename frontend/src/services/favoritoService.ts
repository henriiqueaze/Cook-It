import { api } from "./api";
import type { Receita } from "@/types";

export const favoritoService = {
  listar: () => api.get<Receita[]>("/favoritos"),

  adicionar: (receitaId: Receita["id"]) =>
    api.post<void>(`/favoritos/${receitaId}`, {}),

  remover: (receitaId: Receita["id"]) =>
    api.delete<void>(`/favoritos/${receitaId}`),
};
