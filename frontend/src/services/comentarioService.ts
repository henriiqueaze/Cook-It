import { api } from "./api";
import type { Comentario } from "@/types";

export const comentarioService = {
  listarPorReceita: (receitaId: number) => {
    api.get<Comentario[]>(`/receitas/${receitaId}/comentarios`);
  },

  adicionar: (receitaId: number, conteudo: string, avaliacao: number) => {
    api.post<Comentario>(`/receitas/${receitaId}/comentarios`, {
      conteudo,
      avaliacao,
    });
  },

  deleter: (receitaId: number, comentarioId: number) => {
    api.delete<void>(`/receitas/${receitaId}/comentarios/${comentarioId}`);
  },
};
