import { useEffect, useState } from "react";
import { Trash2 } from "lucide-react";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/ConfirmDialog";
import { adminService } from "@/services/adminService";
import type { Comentario } from "@/types";

export function AdminComments() {
  const [comments, setComments] = useState<Comentario[]>([]);
  const [pendingCommentId, setPendingCommentId] = useState<string | null>(null);

  useEffect(() => {
    adminService.comentarios().then(setComments);
  }, []);

  async function remove(id: string) {
    setPendingCommentId(id);
  }

  async function confirmRemove() {
    if (!pendingCommentId) {
      return;
    }

    try {
      await adminService.removerComentario(pendingCommentId);
      setComments((current) =>
        current.filter((item) => String(item.id) !== pendingCommentId),
      );
      toast.success("Comentário removido");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível remover o comentário",
      );
    } finally {
      setPendingCommentId(null);
    }
  }

  return (
    <section className="rounded-3xl bg-white p-6 shadow-lg shadow-gray-200/60">
      <h1 className="text-2xl font-black text-gray-900">Comentários</h1>
      <p className="mt-2 text-sm text-gray-500">
        Exclusão administrativa de comentários.
      </p>
      <div className="mt-5 space-y-3">
        {comments.map((comment) => (
          <div
            key={String(comment.id)}
            className="rounded-2xl border border-gray-100 p-4"
          >
            <div className="flex items-start justify-between gap-3">
              <div>
                <div className="font-semibold text-gray-900">
                  {comment.userName}
                </div>
                <div className="text-sm text-gray-500">{comment.text}</div>
              </div>
              <button
                type="button"
                onClick={() => remove(String(comment.id))}
                className="rounded-xl border border-red-200 px-3 py-2 text-sm font-medium text-red-600"
              >
                <Trash2 size={14} />
              </button>
            </div>
          </div>
        ))}
      </div>

      <ConfirmDialog
        open={pendingCommentId !== null}
        title="Excluir comentário"
        message="Tem certeza que deseja excluir este comentário?"
        confirmLabel="Excluir"
        onConfirm={confirmRemove}
        onCancel={() => setPendingCommentId(null)}
      />
    </section>
  );
}
