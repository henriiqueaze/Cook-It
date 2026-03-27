import type { Id, Ingrediente, Receita, Usuario } from "@/types";

interface BackendUserDTO {
  id?: Id;
  email?: string;
  displayName?: string;
  avatarUrl?: string;
}

interface BackendImageDTO {
  url?: string;
}

interface BackendRatingDTO {
  score?: number;
}

interface BackendTagDTO {
  name?: string;
}

interface BackendRecipeTagDTO {
  tag?: BackendTagDTO;
}

interface BackendIngredientDTO {
  id?: Id;
  name?: string;
  unitDefault?: string;
}

interface BackendRecipeIngredientDTO {
  id?: Id;
  quantity?: number;
  unit?: string;
  ingredient?: BackendIngredientDTO;
}

export interface BackendRecipeDTO {
  id?: Id;
  author?: BackendUserDTO;
  title?: string;
  description?: string;
  steps?: string;
  servings?: number;
  prepTimeMinutes?: number;
  createdAt?: string;
  updatedAt?: string;
  images?: BackendImageDTO[];
  ratings?: BackendRatingDTO[];
  recipeIngredients?: BackendRecipeIngredientDTO[];
  recipeTags?: BackendRecipeTagDTO[];
}

interface BackendRecipeListResponse {
  _embedded?: Record<string, BackendRecipeDTO[]>;
  content?: BackendRecipeDTO[];
}

function adaptarAutor(author?: BackendUserDTO): Usuario {
  return {
    id: author?.id ?? "sem-autor",
    name: author?.displayName ?? "Usuário",
    email: author?.email ?? "",
    photo: author?.avatarUrl,
  };
}

function adaptarIngredientes(
  ingredients?: BackendRecipeIngredientDTO[],
): Ingrediente[] {
  return (
    ingredients?.map((item, index) => ({
      id: item.ingredient?.id ?? item.id ?? `ingrediente-${index}`,
      nome: item.ingredient?.name ?? "Ingrediente",
      quantidade:
        item.quantity !== undefined && item.quantity !== null
          ? String(item.quantity)
          : "",
      unidade: item.unit ?? item.ingredient?.unitDefault ?? "",
    })) ?? []
  );
}

function adaptarInstrucoes(steps?: string) {
  if (!steps) {
    return [];
  }

  return steps
    .split(/\r?\n/)
    .map((step) => step.trim())
    .filter(Boolean);
}

function calcularMediaAvaliacoes(ratings?: BackendRatingDTO[]) {
  if (!ratings?.length) {
    return 0;
  }

  const total = ratings.reduce((soma, rating) => soma + (rating.score ?? 0), 0);
  return Number((total / ratings.length).toFixed(1));
}

export function adaptBackendRecipeToReceita(recipe: BackendRecipeDTO): Receita {
  const fallbackId = recipe.title ?? "receita-sem-id";

  return {
    id: recipe.id ?? fallbackId,
    titulo: recipe.title ?? "Receita sem título",
    descricao: recipe.description ?? "",
    imagemUrl: recipe.images?.[0]?.url,
    tempoPreparo: recipe.prepTimeMinutes ?? 0,
    porcoes: recipe.servings ?? 1,
    categoria: recipe.recipeTags?.[0]?.tag?.name ?? "Sem categoria",
    avaliacao: calcularMediaAvaliacoes(recipe.ratings),
    totalAvaliacoes: recipe.ratings?.length ?? 0,
    autor: adaptarAutor(recipe.author),
    ingredientes: adaptarIngredientes(recipe.recipeIngredients),
    instrucoes: adaptarInstrucoes(recipe.steps),
    favoritada: false,
    criadoEm: recipe.createdAt ?? recipe.updatedAt ?? new Date().toISOString(),
  };
}

export function adaptBackendRecipeListToReceitas(
  data: BackendRecipeListResponse | BackendRecipeDTO[],
): Receita[] {
  if (Array.isArray(data)) {
    return data.map(adaptBackendRecipeToReceita);
  }

  if (Array.isArray(data.content)) {
    return data.content.map(adaptBackendRecipeToReceita);
  }

  const embeddedRecipes = data._embedded
    ? Object.values(data._embedded).find(Array.isArray)
    : undefined;

  return embeddedRecipes?.map(adaptBackendRecipeToReceita) ?? [];
}
