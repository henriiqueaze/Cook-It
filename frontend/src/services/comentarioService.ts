import { api } from "./api";
import type { Comentario, Receita } from "@/types";

export const comentarioService = {
  listarPorReceita: (receitaId: Receita["id"]) =>
    api.get<Comentario[]>(`/recipes/${receitaId}/comments`),

  adicionar: (
    receitaId: Receita["id"],
    conteudo: string,
    avaliacao: number,
  ) =>
    api.post<Comentario>("/comments", {
      recipeId: receitaId,
      receitaId,
      content: conteudo,
      conteudo,
      rating: avaliacao,
      avaliacao,
    }),

  atualizar: (comentarioId: Comentario["id"], dados: Partial<Comentario>) =>
    api.put<Comentario>(`/comments/${comentarioId}`, dados),

  deletar: (comentarioId: Comentario["id"]) =>
    api.delete<void>(`/comments/${comentarioId}`),
};
