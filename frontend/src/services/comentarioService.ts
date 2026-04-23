import { api } from "./api";
import type { Comentario, Receita } from "@/types";

const BLOQUEIO_COMENTARIO_MS = 10_000;
const ultimoComentarioPorReceita = new Map<string, number>();

function obterChaveReceita(receitaId: Receita["id"]) {
  return String(receitaId);
}

async function adicionarComentarioComBloqueio(
  receitaId: Receita["id"],
  conteudo: string,
) {
  const chaveReceita = obterChaveReceita(receitaId);
  const agora = Date.now();
  const ultimoEnvio = ultimoComentarioPorReceita.get(chaveReceita) ?? 0;

  if (agora - ultimoEnvio < BLOQUEIO_COMENTARIO_MS) {
    throw new Error("Aguarde 10 segundos antes de enviar outro comentário.");
  }

  const comentario = await api.post<Comentario>("/comments", {
    recipeId: receitaId,
    text: conteudo,
  });

  ultimoComentarioPorReceita.set(chaveReceita, Date.now());
  return comentario;
}

export const comentarioService = {
  listarPorReceita: (receitaId: Receita["id"]) =>
    api.get<Comentario[]>(`/comments/recipe/${receitaId}`),

  adicionar: (receitaId: Receita["id"], conteudo: string) =>
    adicionarComentarioComBloqueio(receitaId, conteudo),

  atualizar: (comentarioId: Comentario["id"], dados: { text: string }) =>
    api.put<Comentario>(`/comments/${comentarioId}`, dados),

  deletar: (comentarioId: Comentario["id"]) =>
    api.delete<void>(`/comments/${comentarioId}`),
};
