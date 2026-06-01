import { useEffect, useState } from "react";
import { Trash2 } from "lucide-react";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/ConfirmDialog";
import { adminService } from "@/services/adminService";
import type { Receita } from "@/types";

export function AdminRecipes() {
  const [recipes, setRecipes] = useState<Receita[]>([]);
  const [pendingRecipeId, setPendingRecipeId] = useState<string | null>(null);

  useEffect(() => {
    adminService.receitas().then(setRecipes);
  }, []);

  async function remove(id: string) {
    setPendingRecipeId(id);
  }

  async function confirmRemove() {
    if (!pendingRecipeId) {
      return;
    }

    try {
      await adminService.removerReceita(pendingRecipeId);
      setRecipes((current) =>
        current.filter((item) => String(item.id) !== pendingRecipeId),
      );
      toast.success("Receita removida");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível remover a receita",
      );
    } finally {
      setPendingRecipeId(null);
    }
  }

  return (
    <section className="rounded-3xl bg-white p-6 shadow-lg shadow-gray-200/60">
      <h1 className="text-2xl font-black text-gray-900">Receitas</h1>
      <p className="mt-2 text-sm text-gray-500">
        Exclusão administrativa de receitas.
      </p>
      <div className="mt-5 space-y-3">
        {recipes.map((recipe) => (
          <div
            key={String(recipe.id)}
            className="rounded-2xl border border-gray-100 p-4"
          >
            <div className="flex items-center justify-between gap-3">
              <div>
                <div className="font-semibold text-gray-900">
                  {recipe.titulo}
                </div>
                <div className="text-sm text-gray-500">
                  {recipe.autor?.name}
                </div>
              </div>
              <button
                type="button"
                onClick={() => remove(String(recipe.id))}
                className="rounded-xl border border-red-200 px-3 py-2 text-sm font-medium text-red-600"
              >
                <Trash2 size={14} />
              </button>
            </div>
          </div>
        ))}
      </div>

      <ConfirmDialog
        open={pendingRecipeId !== null}
        title="Excluir receita"
        message="Tem certeza que deseja excluir esta receita? Os comentários dela também serão removidos."
        confirmLabel="Excluir"
        onConfirm={confirmRemove}
        onCancel={() => setPendingRecipeId(null)}
      />
    </section>
  );
}
