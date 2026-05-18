import type { Id, Ingrediente, Receita, Usuario } from "@/types";

interface BackendUserDTO {
  id?: Id;
  email?: string;
  displayName?: string;
  avatarUrl?: string;
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
  authorId?: Id;
  authorName?: string;
  authorPhoto?: string;
  title?: string;
  name?: string;
  description?: string;
  steps?: string;
  instructions?: string[];
  servings?: number;
  portions?: number;
  prepTimeMinutes?: number;
  prepTime?: number;
  createdAt?: string;
  updatedAt?: string;
  image?: string;
  imageUrl?: string;
  images?: Array<{ url?: string }>;
  rating?: number;
  ratings?: BackendRatingDTO[];
  ratingsCount?: number;
  userRating?: number;
  recipeIngredients?: BackendRecipeIngredientDTO[];
  ingredients?: Array<{
    ingredient?: string;
    quantity?: number;
    unit?: string;
    id?: Id;
  }>;
  recipeTags?: BackendRecipeTagDTO[];
  category?: string;
}

export interface BackendRecipeListResponse {
  _embedded?: Record<string, BackendRecipeDTO[]>;
  content?: BackendRecipeDTO[];
}

function buildAuthor(author?: BackendUserDTO, authorId?: Id, authorName?: string, authorPhoto?: string): Usuario {
  return {
    id: authorId ?? author?.id ?? "sem-autor",
    name: authorName ?? author?.displayName ?? "Usuário",
    email: author?.email ?? "",
    photo: authorPhoto ?? author?.avatarUrl ?? null,
  };
}

function buildIngredients(
  ingredients?: BackendRecipeIngredientDTO[] | BackendRecipeDTO["ingredients"],
): Ingrediente[] {
  if (!ingredients?.length) {
    return [];
  }

  return ingredients.map((item, index) => {
    const structuredIngredient =
      "ingredient" in item && typeof item.ingredient === "object"
        ? item.ingredient
        : undefined;

    const ingredientName =
      structuredIngredient?.name ??
      ("ingredient" in item && typeof item.ingredient === "string"
        ? item.ingredient
        : "Ingrediente");

    const quantity =
      "quantity" in item && item.quantity !== undefined && item.quantity !== null
        ? String(item.quantity)
        : "";

    const unidade =
      "unit" in item && item.unit
        ? item.unit
        : structuredIngredient?.unitDefault ?? "";

    return {
      id:
        structuredIngredient?.id ??
        ("ingredient" in item ? item.id : undefined) ??
        `ingrediente-${index}`,
      nome: ingredientName,
      quantidade: quantity,
      unidade,
    };
  });
}

function buildInstructions(steps?: string, instructions?: string[]) {
  if (instructions?.length) {
    return instructions.map((step) => step.trim()).filter(Boolean);
  }

  if (!steps) {
    return [];
  }

  return steps
    .split(/\r?\n/)
    .map((step) => step.trim())
    .filter(Boolean);
}

function buildRating(rating?: number, ratings?: BackendRatingDTO[]) {
  if (typeof rating === "number") {
    return Number(rating.toFixed(1));
  }

  if (!ratings?.length) {
    return 0;
  }

  const total = ratings.reduce((sum, item) => sum + (item.score ?? 0), 0);
  const average = total / ratings.length;
  return Number(average.toFixed(1));
}

export function adaptBackendRecipeToReceita(recipe: BackendRecipeDTO): Receita {
  const fallbackId = recipe.id ?? recipe.name ?? recipe.title ?? "receita-sem-id";
  const imagemUrl =
    recipe.image ??
    recipe.imageUrl ??
    recipe.images?.[0]?.url ??
    undefined;

  return {
    id: fallbackId,
    titulo: recipe.name ?? recipe.title ?? "Receita sem título",
    descricao: recipe.description ?? "",
    imagemUrl,
    tempoPreparo: recipe.prepTime ?? recipe.prepTimeMinutes ?? 0,
    porcoes: recipe.portions ?? recipe.servings ?? 1,
    categoria: recipe.category ?? recipe.recipeTags?.[0]?.tag?.name,
    avaliacao: buildRating(recipe.rating, recipe.ratings),
    totalAvaliacoes: recipe.ratingsCount ?? recipe.ratings?.length ?? 0,
    autor: buildAuthor(recipe.author, recipe.authorId, recipe.authorName, recipe.authorPhoto),
    ingredientes: buildIngredients(recipe.recipeIngredients ?? recipe.ingredients),
    instrucoes: buildInstructions(recipe.steps, recipe.instructions),
    favoritada: false,
    criadoEm: recipe.createdAt ?? recipe.updatedAt ?? new Date().toISOString(),
    avaliacaoUsuario: recipe.userRating,
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

  const embedded = data._embedded
    ? Object.values(data._embedded).find(Array.isArray)
    : undefined;

  return embedded?.map(adaptBackendRecipeToReceita) ?? [];
}
