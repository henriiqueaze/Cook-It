import { api } from "./api";
import type { Comentario, Receita } from "@/types";

export const comentarioService = {
  listarPorReceita: (receitaId: Receita["id"]) =>
    api.get<Comentario[]>(`/comments/recipe/${receitaId}`),

  adicionar: (receitaId: Receita["id"], conteudo: string) =>
    api.post<Comentario>("/comments", {
      recipeId: receitaId,
      text: conteudo,
    }),

  atualizar: (comentarioId: Comentario["id"], dados: { text: string }) =>
    api.put<Comentario>(`/comments/${comentarioId}`, dados),

  deletar: (comentarioId: Comentario["id"]) =>
    api.delete<void>(`/comments/${comentarioId}`),
};
