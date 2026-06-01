/**
 * Identificador genérico usado em vários tipos.
 */
export type Id = number | string;

/**
 * Representa um usuário autenticado no sistema.
 */
export interface Usuario {
  id: Id;
  name: string;
  email: string;
  photo?: string | null;
  role?: "USER" | "ADMIN";
  banned?: boolean;
  createdRecipes?: Id[];
  favoriteRecipes?: Id[];
  ratings?: Record<string, number>;
}

/**
 * Ingrediente usado em formulários e receitas.
 * Campos `quantidade` e `unidade` são strings para facilitar binding em formulários.
 */
export interface Ingrediente {
  id: Id;
  nome: string;
  quantidade?: string;
  unidade?: string;
}

/**
 * Tipo principal que representa uma receita usada na UI.
 */
export interface Receita {
  id: Id;
  titulo: string;
  descricao: string;
  imagemUrl?: string;
  tempoPreparo: number;
  porcoes: number;
  categoria?: string;
  avaliacao: number;
  totalAvaliacoes: number;
  autor: Usuario;
  ingredientes: Ingrediente[];
  instrucoes: string[];
  favoritada?: boolean;
  criadoEm: string;
  avaliacaoUsuario?: number;
}

/**
 * Comentário deixado por um usuário em uma receita.
 */
export interface Comentario {
  id: Id;
  recipeId: Id;
  userId: Id;
  userName: string;
  userPhoto?: string | null;
  text: string;
  createdAt: string;
}

/**
 * Resposta de autenticação retornada pelo backend.
 */
export interface RespostaAuth {
  token: string;
  user: Usuario;
}
