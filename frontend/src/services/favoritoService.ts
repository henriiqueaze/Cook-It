import { api } from "./api";
import type { Receita } from "@/types";

export const favoritoService = {
  listar: () => {
    api.get<Receita[]>("/favoritos");
  },

  adicionar: (receitaId: number) => {
    api.post<void>(`/favoritos/${receitaId}`, {});
  },

  remover: (receitaId: number) => {
    api.delete<void>(`/favoritos/${receitaId}`);
  },
};
