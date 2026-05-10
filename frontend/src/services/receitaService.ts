import { api } from "./api";
import {
  adaptBackendRecipeListToReceitas,
  adaptBackendRecipeToReceita,
  type BackendRecipeDTO,
} from "./receitaAdapter";
import type { Id, Receita } from "@/types";

interface SearchRecipePayload {
  ingredients: string[];
  exactMatch?: boolean;
  sortBy?: string;
}

interface RecipeIngredientInput {
  nome: string;
  quantidade: string;
  unidade: string;
}

export interface ReceitaFormPayload {
  titulo: string;
  descricao: string;
  imagemArquivo?: File | null;
  tempoPreparo: number;
  porcoes: number;
  ingredientes: RecipeIngredientInput[];
  instrucoes: string[];
  categoria?: string;
}

export interface AtualizarReceitaPayload extends Partial<ReceitaFormPayload> {
  imagemArquivo?: File | null;
}

function mapIngredients(ingredientes: RecipeIngredientInput[]) {
  return ingredientes.map((ingrediente) => ({
    ingredient: ingrediente.nome.trim(),
    quantity: Number.parseFloat(ingrediente.quantidade.replace(",", ".")) || 0,
    unit: ingrediente.unidade,
  }));
}

function mapInstructions(instrucoes: string[]) {
  return instrucoes.map((step) => step.trim()).filter(Boolean);
}

function buildRecipeData(
  receita: ReceitaFormPayload | AtualizarReceitaPayload,
) {
  return {
    ...(receita.titulo ? { name: receita.titulo.trim() } : {}),
    ...(receita.descricao ? { description: receita.descricao.trim() } : {}),
    ...(receita.tempoPreparo ? { prepTime: receita.tempoPreparo } : {}),
    ...(receita.porcoes ? { portions: receita.porcoes } : {}),
    ...(receita.ingredientes
      ? { ingredients: mapIngredients(receita.ingredientes) }
      : {}),
    ...(receita.instrucoes
      ? { instructions: mapInstructions(receita.instrucoes) }
      : {}),
    ...(receita.categoria?.trim()
      ? { category: receita.categoria.trim() }
      : {}),
  };
}

function buildRecipeFormData(
  receita: ReceitaFormPayload | AtualizarReceitaPayload,
) {
  const data = buildRecipeData(receita);

  const formData = new FormData();

  formData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" }),
  );

  if (receita.imagemArquivo) {
    formData.append("image", receita.imagemArquivo);
  }

  return formData;
}

export const receitaService = {
  listar: async () => {
    const resposta = await api.get<
      BackendRecipeDTO[] | { content?: BackendRecipeDTO[] }
    >("/recipes");
    return adaptBackendRecipeListToReceitas(resposta as BackendRecipeDTO[]);
  },

  destaques: async () => {
    const resposta = await api.get<BackendRecipeDTO[]>("/recipes/top-rated");
    return resposta.map(adaptBackendRecipeToReceita);
  },

  buscarPorId: async (id: Id): Promise<Receita> => {
    const resposta = await api.get<BackendRecipeDTO>(`/recipes/${id}`);
    return adaptBackendRecipeToReceita(resposta);
  },

  buscarPorIngredientes: async (ingredientes: string[]) => {
    const resposta = await api.post<BackendRecipeDTO[]>("/recipes/search", {
      ingredients: ingredientes,
      exactMatch: false,
      sortBy: "compatibility",
    } satisfies SearchRecipePayload);

    return Array.isArray(resposta)
      ? resposta.map(adaptBackendRecipeToReceita)
      : [];
  },

  criar: async (receita: ReceitaFormPayload) => {
    const resposta = await api.post<BackendRecipeDTO>(
      "/recipes",
      buildRecipeFormData(receita),
    );

    return adaptBackendRecipeToReceita(resposta);
  },

  atualizar: async (id: Id, receita: AtualizarReceitaPayload) => {
    const resposta = await api.put<BackendRecipeDTO>(
      `/recipes/${id}`,
      buildRecipeFormData(receita),
    );

    return adaptBackendRecipeToReceita(resposta);
  },

  deletar: (id: Id) => api.delete<void>(`/recipes/${id}`),

  avaliar: (id: Id, nota: number) =>
    api.post<void>(`/recipes/${id}/rate`, { rating: nota }),

  minhasReceitas: async (userId: Id) => {
    const resposta = await api.get<
      BackendRecipeDTO[] | { content?: BackendRecipeDTO[] }
    >(`/users/${userId}/recipes`);

    return adaptBackendRecipeListToReceitas(resposta as BackendRecipeDTO[]);
  },
};
