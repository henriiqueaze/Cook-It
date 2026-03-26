import { api } from "./api";
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

export interface NovaReceitaPayload {
  titulo: string;
  descricao: string;
  imagemUrl?: string;
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
  const payload = {
    name: receita.titulo,
    description: receita.descricao,
    imageUrl: receita.imagemUrl || undefined,
    prepTime: Number(receita.tempoPreparo),
    servings: Number(receita.porcoes),
    instructions: receita.instrucoes,

    category: receita.categoria,

    ingredients: receita.ingredientes.map((ingrediente) => ({
      ingredient: ingrediente.nome,
      quantity: parseFloat(ingrediente.quantidade) || 0,
      unit: ingrediente.unidade,
    })),
  };
  return payload;
}

export function mapBackendToFrontend(receita: any) {
  return {
    id: receita.id,
    titulo: receita.name,
    descricao: "", 
    imagemUrl: receita.image,
    tempoPreparo: receita.prepTime,
    porcoes: receita.servings ?? 0,
    categoria: receita.category ?? "",

    avaliacao: receita.rating ?? 0,
    totalAvaliacoes: receita.ratingsCount ?? 0,

    autor: {
      id: receita.authorId,
      nome: receita.authorName,
      avatarUrl: receita.authorPhoto,
      email: "", 
    },

    ingredientes:
      receita.ingredients?.map((i: any) => ({
        id: crypto.randomUUID(),
        nome: i.ingredient,
        quantidade: String(i.quantity),
        unidade: i.unit,
      })) ?? [],

    instrucoes: receita.instructions ?? [],

    criadoEm: receita.createdAt,
  };
}

export const receitaService = {
  listar: async () => {
    const resposta = await api.get<BackendRecipeListResponse>("/recipes");
    return adaptBackendRecipeListToReceitas(resposta);
  },

  buscarPorId: async (id: Id) => {
    const resposta = await api.get<any>(`/recipes/${id}`);
    return mapBackendToFrontend(resposta);
  },

  buscarPorIngredientes: async (ingredientes: string[]) => {
    return api.post<Receita[]>("/recipes/search", {
      ingredients: ingredientes,
      exactMatch: false,
      sortBy: "compatibility",
    });
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
