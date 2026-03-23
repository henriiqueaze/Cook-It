import { api } from "./api";
import { receitasMock } from "@/mocks";
import type { Id, Receita } from "@/types";
import {
  adaptBackendRecipeListToReceitas,
  adaptBackendRecipeToReceita,
  type BackendRecipeDTO,
} from "./receitaAdapter";

interface BackendRecipeListResponse {
  _embedded?: Record<string, BackendRecipeDTO[]>;
  content?: BackendRecipeDTO[];
}

function buscarReceitaMockPorId(id: Id) {
  return receitasMock.find((receita) => String(receita.id) === String(id));
}

function buscarReceitasMockPorIngredientes(ingredientes: string[]) {
  if (ingredientes.length === 0) {
    return receitasMock;
  }

  return receitasMock.filter((receita) =>
    receita.ingredientes.some((ingrediente) =>
      ingredientes.some((termo) =>
        ingrediente.nome.toLowerCase().includes(termo.toLowerCase()),
      ),
    ),
  );
}

export const receitaService = {
  getRecipes: async () => {
    try {
      const resposta = await api.get<BackendRecipeListResponse>("/recipe");
      const receitas = adaptBackendRecipeListToReceitas(resposta);
      return receitas.length > 0 ? receitas : receitasMock;
    } catch {
      return receitasMock;
    }
  },

  getRecipeById: async (id: Id) => {
    try {
      const resposta = await api.get<BackendRecipeDTO>(`/recipe/${id}`);
      return adaptBackendRecipeToReceita(resposta);
    } catch {
      const receitaMock = buscarReceitaMockPorId(id);

      if (!receitaMock) {
        throw new Error("Receita não encontrada");
      }

      return receitaMock;
    }
  },

  listar: () => receitaService.getRecipes(),

  buscarPorIngredientes: async (ingredientes: string[]) => {
    try {
      return await api.post<Receita[]>("/recipes/search", {
        ingredients: ingredientes,
        exactMatch: false,
        sortBy: "compatibility",
      });
    } catch {
      return buscarReceitasMockPorIngredientes(ingredientes);
    }
  },

  buscarPorId: (id: Id) => receitaService.getRecipeById(id),

  criar: (receita: Partial<Receita>) => api.post<Receita>("/receitas", receita),

  atualizer: (id: Id, receita: Partial<Receita>) =>
    api.put<Receita>(`/receitas/${id}`, receita),

  deletar: (id: Id) => api.delete<void>(`/receitas/${id}`),

  minhasReceitas: () => api.get<Receita[]>("/receitas/minhas"),
};
