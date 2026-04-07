import { api } from "./api";
import type { Id, Receita } from "@/types";
import {
  adaptBackendRecipeListToReceitas,
  type BackendRecipeDTO,
} from "./receitaAdapter";

interface BackendRecipeListResponse {
  _embedded?: Record<string, BackendRecipeDTO[]>;
  content?: BackendRecipeDTO[];
}

export interface NovaReceitaPayload {
  titulo: string;
  descricao: string;
  imagemArquivo?: File | null;
  tempoPreparo: number;
  porcoes: number;
  ingredientes: Array<{
    nome: string;
    quantidade: string;
    unidade: string;
  }>;
  instrucoes: string[];
  categoria?: string;
}

function montarPayloadBackend(receita: NovaReceitaPayload) {
  const formData = new FormData();

  const data = {
    name: receita.titulo,
    description: receita.descricao,
    prepTime: receita.tempoPreparo,
    portions: receita.porcoes,
    ingredients: receita.ingredientes.map((ingrediente) => ({
      ingredient: ingrediente.nome,
      quantity: parseFloat(ingrediente.quantidade) || 0,
      unit: ingrediente.unidade,
    })),
    instructions: receita.instrucoes,
    ...(receita.categoria ? { category: receita.categoria } : {}),
  };

  formData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" }),
  );

  if (receita.imagemArquivo) {
    formData.append("image", receita.imagemArquivo);
  }

  return formData;
}

export function mapBackendToFrontend(receita: any): Receita {
  return {
    id: receita.id,
    titulo: receita.name ?? receita.title ?? "Receita sem título",
    descricao: receita.description ?? "",
    imagemUrl: receita.imageUrl ?? receita.image ?? "",
    tempoPreparo: receita.prepTime ?? receita.prepTimeMinutes ?? 0,
    porcoes: receita.portions ?? receita.servings ?? 1,
    categoria: receita.category ?? receita.recipeTags?.[0]?.tag?.name ?? "",
    avaliacao: receita.rating ?? 0,
    totalAvaliacoes: receita.ratingsCount ?? receita.ratings?.length ?? 0,
    autor: {
      id: receita.authorId ?? receita.author?.id ?? "sem-autor",
      name: receita.authorName ?? receita.author?.displayName ?? "Usuário",
      photo: receita.authorPhoto ?? receita.author?.avatarUrl ?? null,
      email: receita.author?.email ?? "",
    },
    ingredientes:
      receita.ingredients?.map((i: any) => ({
        id: i.id ?? i.ingredient?.id ?? crypto.randomUUID(),
        nome: i.ingredient ?? i.name ?? i.ingredient?.name ?? "Ingrediente",
        quantidade: String(i.quantity ?? ""),
        unidade: i.unit ?? i.ingredient?.unitDefault ?? "",
      })) ??
      receita.recipeIngredients?.map((i: any) => ({
        id: i.id ?? i.ingredient?.id ?? crypto.randomUUID(),
        nome: i.ingredient?.name ?? "Ingrediente",
        quantidade: String(i.quantity ?? ""),
        unidade: i.unit ?? i.ingredient?.unitDefault ?? "",
      })) ??
      [],
    instrucoes:
      receita.instructions ??
      receita.steps?.split(/\r?\n/).map((step: string) => step.trim()).filter(Boolean) ??
      [],
    criadoEm: receita.createdAt ?? receita.updatedAt ?? new Date().toISOString(),
  };
}

export const receitaService = {
  listar: async () => {
    const resposta = await api.get<BackendRecipeListResponse>("/recipes");
    return adaptBackendRecipeListToReceitas(resposta);
  },

  destaques: async () => {
    const resposta = await api.get<any[]>("/recipes/top-rated");
    return resposta.map(mapBackendToFrontend);
  },

  buscarPorId: async (id: Id): Promise<Receita> => {
    const resposta = await api.get<any>(`/recipes/${id}`);
    return mapBackendToFrontend(resposta);
  },

  buscarPorIngredientes: async (ingredientes: string[]) => {
    const resposta = await api.post<any[]>("/recipes/search", {
      ingredients: ingredientes,
      exactMatch: false,
      sortBy: "compatibility",
    });

    return Array.isArray(resposta)
      ? resposta.map(mapBackendToFrontend)
      : [];
  },

  criar: (receita: NovaReceitaPayload) =>
    api.post<Receita>("/recipes", montarPayloadBackend(receita)),

  atualizar: (id: Id, receita: Partial<Receita>) =>
    api.put<Receita>(`/recipes/${id}`, receita),

  deletar: (id: Id) => api.delete<void>(`/recipes/${id}`),

  avaliar: (id: Id, nota: number) =>
    api.post<void>(`/recipes/${id}/rate`, { rating: nota, nota }),

  minhasReceitas: async (userId: Id) => {
    const resposta = await api.get<any[]>(`/users/${userId}/recipes`);
    return resposta.map(mapBackendToFrontend);
  },
};