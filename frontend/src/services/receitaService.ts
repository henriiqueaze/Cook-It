import { api } from "./api";
import type { Receita } from "@/types";

export const receitaService = {
  listar: () => api.get<Receita[]>("/receitas"),

  buscarPorIngredientes: (ingredientes: string[]) => {
    api.post<Receita[]>("/recipes/search", {
      ingredients: ingredientes,
      exactMatch: false,
      sortBy: "compatibility",
    });
  },

  buscarPorId: (id: number) => {
    api.get<Receita>(`/receitas/${id}`);
  },

  criar: (receita: Partial<Receita>) => {
    api.post<Receita>("/receitas", receita);
  },

  atualizer: (id: number, receita: Partial<Receita>) => {
    api.put<Receita>(`/receitas/${id}`, receita);
  },

  deletar: (id: number) => {
    api.delete<void>(`/receitas/${id}`);
  },

  minhasReceitas: () => {
    api.get<Receita[]>("/receitas/minhas");
  },
};
