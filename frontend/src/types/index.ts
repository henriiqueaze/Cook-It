export type Id = number | string;

export interface Usuario {
  id: Id;
  name: string;
  email: string;
  photo?: string | null;
  createdRecipes?: Id[];
  favoriteRecipes?: Id[];
  ratings?: Record<string, number>;
}

export interface Ingrediente {
  id: Id;
  nome: string;
  quantidade?: string;
  unidade?: string;
}

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

export interface Comentario {
  id: Id;
  recipeId: Id;
  userId: Id;
  userName: string;
  userPhoto?: string | null;
  text: string;
  createdAt: string;
}

export interface RespostaAuth {
  token: string;
  user: Usuario;
}
