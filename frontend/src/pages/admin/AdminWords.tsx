import { useEffect, useState, type FormEvent } from "react";
import { Check, PencilLine, Plus, Trash2, X } from "lucide-react";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/ConfirmDialog";
import { adminService, type BannedWord } from "@/services/adminService";

export function AdminWords() {
  const [words, setWords] = useState<BannedWord[]>([]);
  const [loading, setLoading] = useState(true);
  const [term, setTerm] = useState("");
  const [recipes, setRecipes] = useState(true);
  const [ingredients, setIngredients] = useState(true);
  const [comments, setComments] = useState(true);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editingTerm, setEditingTerm] = useState("");
  const [editingRecipes, setEditingRecipes] = useState(true);
  const [editingIngredients, setEditingIngredients] = useState(true);
  const [editingComments, setEditingComments] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [pendingDeleteId, setPendingDeleteId] = useState<string | null>(null);

  useEffect(() => {
    adminService
      .palavrasBanidas()
      .then((data) => {
        setWords(data);
      })
      .catch((error) => {
        toast.error(
          error instanceof Error
            ? error.message
            : "Não foi possível carregar as palavras banidas",
        );
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  function resetForm() {
    setTerm("");
    setRecipes(true);
    setIngredients(true);
    setComments(true);
    setShowForm(false);
  }

  function startEdit(word: BannedWord) {
    setEditingId(word.id);
    setEditingTerm(word.term);
    setEditingRecipes(word.appliesToRecipes);
    setEditingIngredients(word.appliesToIngredients);
    setEditingComments(word.appliesToComments);
  }

  function cancelEdit() {
    setEditingId(null);
    setEditingTerm("");
    setEditingRecipes(true);
    setEditingIngredients(true);
    setEditingComments(true);
  }

  function toggleRecipes() {
    setRecipes((value) => !value);
  }

  function toggleIngredients() {
    setIngredients((value) => !value);
  }

  function toggleComments() {
    setComments((value) => !value);
  }

  function toggleEditingRecipes() {
    setEditingRecipes((value) => !value);
  }

  function toggleEditingIngredients() {
    setEditingIngredients((value) => !value);
  }

  function toggleEditingComments() {
    setEditingComments((value) => !value);
  }

  async function save(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    try {
      const saved = await adminService.criarPalavraBanida({
        term,
        appliesToRecipes: recipes,
        appliesToIngredients: ingredients,
        appliesToComments: comments,
      });

      setWords((current) => [saved, ...current]);
      resetForm();
      toast.success("Palavra adicionada");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível salvar a palavra",
      );
    }
  }

  async function saveEdit(id: string) {
    try {
      const updated = await adminService.editarPalavraBanida(id, {
        term: editingTerm,
        appliesToRecipes: editingRecipes,
        appliesToIngredients: editingIngredients,
        appliesToComments: editingComments,
      });

      setWords((current) =>
        current.map((word) => (word.id === id ? updated : word)),
      );
      cancelEdit();
      toast.success("Palavra atualizada");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível atualizar a palavra",
      );
    }
  }

  async function remove(id: string) {
    setPendingDeleteId(id);
  }

  async function confirmRemove() {
    if (!pendingDeleteId) {
      return;
    }

    try {
      await adminService.removerPalavraBanida(pendingDeleteId);
      setWords((current) =>
        current.filter((word) => word.id !== pendingDeleteId),
      );
      if (editingId === pendingDeleteId) {
        cancelEdit();
      }
      toast.success("Palavra removida");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível remover a palavra",
      );
    } finally {
      setPendingDeleteId(null);
    }
  }

  if (loading) {
    return (
      <div className="rounded-3xl bg-white p-6">
        Carregando palavras banidas...
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <section className="rounded-3xl bg-white p-6 shadow-lg shadow-gray-200/60">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="text-2xl font-black text-gray-900">
              Palavras banidas
            </h1>
          </div>

          <button
            type="button"
            onClick={() => setShowForm((value) => !value)}
            className="inline-flex items-center gap-2 rounded-xl bg-gray-900 px-4 py-3 text-sm font-semibold text-white"
          >
            {showForm ? <X size={16} /> : <Plus size={16} />}
            {showForm ? "Fechar" : "Novo"}
          </button>
        </div>

        {showForm && (
          <form
            className="mt-6 space-y-4 rounded-2xl border border-gray-100 bg-gray-50 p-4"
            onSubmit={save}
          >
            <p className="text-sm text-gray-500">
              Crie e edite termos sem diferenciar maiúsculas e minúsculas.
            </p>

            <input
              value={term}
              onChange={(e) => setTerm(e.target.value)}
              placeholder="Termo proibido"
              className="w-full rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm outline-none"
            />

            <div className="grid gap-3 sm:grid-cols-3">
              <button
                type="button"
                onClick={toggleRecipes}
                className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${recipes ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
              >
                Receitas
              </button>
              <button
                type="button"
                onClick={toggleIngredients}
                className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${ingredients ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
              >
                Ingredientes
              </button>
              <button
                type="button"
                onClick={toggleComments}
                className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${comments ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
              >
                Comentários
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              <button className="inline-flex items-center gap-2 rounded-xl bg-gray-900 px-4 py-3 text-sm font-semibold text-white">
                <Plus size={16} /> Adicionar
              </button>
            </div>
          </form>
        )}
      </section>

      <section className="rounded-3xl bg-white p-6 shadow-lg shadow-gray-200/60">
        <h2 className="text-xl font-black text-gray-900">Lista</h2>
        <div className="mt-4 space-y-3">
          {words.map((word) => (
            <div
              key={word.id}
              className="rounded-2xl border border-gray-100 p-4"
            >
              {editingId === word.id ? (
                <div className="space-y-3">
                  <input
                    value={editingTerm}
                    onChange={(event) => setEditingTerm(event.target.value)}
                    className="w-full rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm outline-none"
                  />

                  <div className="grid gap-3 sm:grid-cols-3">
                    <button
                      type="button"
                      onClick={toggleEditingRecipes}
                      className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${editingRecipes ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
                    >
                      Receitas
                    </button>
                    <button
                      type="button"
                      onClick={toggleEditingIngredients}
                      className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${editingIngredients ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
                    >
                      Ingredientes
                    </button>
                    <button
                      type="button"
                      onClick={toggleEditingComments}
                      className={`rounded-2xl border px-4 py-3 text-left text-sm font-medium ${editingComments ? "border-orange-300 bg-orange-50 text-orange-700" : "border-gray-200 bg-white text-gray-500"}`}
                    >
                      Comentários
                    </button>
                  </div>

                  <div className="flex flex-wrap gap-2">
                    <button
                      type="button"
                      onClick={() => saveEdit(word.id)}
                      className="inline-flex items-center gap-2 rounded-xl bg-gray-900 px-4 py-3 text-sm font-semibold text-white"
                    >
                      <Check size={16} /> Salvar
                    </button>
                    <button
                      type="button"
                      onClick={cancelEdit}
                      className="inline-flex items-center gap-2 rounded-xl border border-gray-200 px-4 py-3 text-sm font-semibold text-gray-600"
                    >
                      <X size={16} /> Cancelar
                    </button>
                  </div>
                </div>
              ) : (
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <div className="font-semibold text-gray-900">
                      {word.term}
                    </div>
                    <div className="mt-1 flex flex-wrap gap-2 text-xs text-gray-500">
                      {word.appliesToRecipes && (
                        <span className="rounded-full bg-orange-50 px-2 py-1 text-orange-700">
                          Receitas
                        </span>
                      )}
                      {word.appliesToIngredients && (
                        <span className="rounded-full bg-orange-50 px-2 py-1 text-orange-700">
                          Ingredientes
                        </span>
                      )}
                      {word.appliesToComments && (
                        <span className="rounded-full bg-orange-50 px-2 py-1 text-orange-700">
                          Comentários
                        </span>
                      )}
                    </div>
                  </div>
                  <div className="flex gap-2">
                    <button
                      type="button"
                      onClick={() => startEdit(word)}
                      className="rounded-xl border border-gray-200 px-3 py-2 text-sm font-medium text-gray-600"
                    >
                      <PencilLine size={14} />
                    </button>
                    <button
                      type="button"
                      onClick={() => remove(word.id)}
                      className="rounded-xl border border-red-200 px-3 py-2 text-sm font-medium text-red-600"
                    >
                      <Trash2 size={14} />
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>

        <ConfirmDialog
          open={pendingDeleteId !== null}
          title="Excluir palavra banida"
          message="Tem certeza que deseja excluir esta palavra?"
          confirmLabel="Excluir"
          onConfirm={confirmRemove}
          onCancel={() => setPendingDeleteId(null)}
        />
      </section>
    </div>
  );
}
