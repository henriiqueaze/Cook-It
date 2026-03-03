export interface Usuario {
  id: number;
  nome: string;
  email: string;
  avatarUrl?: string;
}

export interface Ingrediente {
  id: number;
  nome: string;
  quantidade: string;
  unidade: string;
}

export interface Receita {
  id: number;
  titulo: string;
  descricao: string;
  imagemUrl?: string;
  tempoPreparo: number;
  porcoes: number;
  categoria: string;
  avaliacao: number;
  autor: Usuario;
  ingredientes: Ingrediente[];
  instrucoes: string[];
  favoritada?: boolean;
  criadoEm: string;
}

export interface Comentario {
  id: number;
  conteudo: string;
  avaliacao: number;
  autor: Usuario;
  criadoEm: string;
}

export interface RespostaAuth {
  token: string;
  usuario: Usuario;
}
