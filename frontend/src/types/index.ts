export interface Ususario {
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
  dificuldade: "FACIL" | "MEDIA" | "DIFICIL";
  categoria: string;
  avaliacao: number;
  autor: Ususario;
  ingredientes: Ingrediente[];
  instrucoes: string[];
  favoritada?: boolean;
  criadoEm: string;
}

export interface Comentario {
  id: number;
  conteudo: string;
  avaliacao: number;
  autor: Ususario;
  criadoEm: string;
}

export interface RespostaAuth {
  token: string;
  usuario: Ususario;
}
