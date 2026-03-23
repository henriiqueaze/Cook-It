import { api } from "./api";
import type { Comentario, Receita } from "@/types";

export const comentarioService = {
  listarPorReceita: (receitaId: Receita["id"]) =>
    api.get<Comentario[]>(`/receitas/${receitaId}/comentarios`),

  adicionar: (
    receitaId: Receita["id"],
    conteudo: string,
    avaliacao: number,
  ) =>
    api.post<Comentario>(`/receitas/${receitaId}/comentarios`, {
      conteudo,
      avaliacao,
    }),

  deleter: (receitaId: Receita["id"], comentarioId: Comentario["id"]) =>
    api.delete<void>(`/receitas/${receitaId}/comentarios/${comentarioId}`),
};
