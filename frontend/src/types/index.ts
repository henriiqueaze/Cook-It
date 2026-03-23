export type Id = number | string;

export interface Usuario {
  id: Id;
  nome: string;
  email: string;
  avatarUrl?: string;
}

export interface Ingrediente {
  id: Id;
  nome: string;
  quantidade: string;
  unidade: string;
}

export interface Receita {
  id: Id;
  titulo: string;
  descricao: string;
  imagemUrl?: string;
  tempoPreparo: number;
  porcoes: number;
  categoria: string;
  avaliacao: number;
  totalAvaliacoes: number;
  autor: Usuario;
  ingredientes: Ingrediente[];
  instrucoes: string[];
  favoritada?: boolean;
  criadoEm: string;
}

export interface Comentario {
  id: Id;
  conteudo: string;
  avaliacao: number;
  autor: Usuario;
  criadoEm: string;
}

export interface RespostaAuth {
  token: string;
  usuario: Usuario;
}
