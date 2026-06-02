import { useEffect, useState, type FormEvent } from "react";
import {
  Ban,
  Check,
  FileText,
  MessageSquare,
  PencilLine,
  Plus,
  Trash2,
  UtensilsCrossed,
  X,
} from "lucide-react";
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
      <div className="rounded-2xl bg-white p-6 text-sm text-gray-500 shadow-lg shadow-gray-200/60">
        Carregando palavras banidas...
      </div>
    );
  }

  return (
    <div className="space-y-5">
      <section className="overflow-hidden rounded-2xl bg-white shadow-lg shadow-gray-200/60">
        <div className="border-b border-orange-100 bg-orange-50/70 px-5 py-5 sm:px-6">
          <div className="flex flex-wrap items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-orange-600 text-white shadow-md shadow-orange-200">
                <Ban size={21} />
              </div>
              <div>
                <h1 className="text-2xl font-black text-gray-900">
                  Palavras banidas
                </h1>
                <p className="mt-1 text-sm text-gray-600">
                  Defina termos bloqueados por área do aplicativo.
                </p>
              </div>
            </div>

            <button
              type="button"
              onClick={() => setShowForm((value) => !value)}
              className={`inline-flex h-11 items-center gap-2 rounded-xl px-4 text-sm font-semibold shadow-sm transition-colors ${
                showForm
                  ? "border border-gray-200 bg-white text-gray-700 hover:bg-gray-50"
                  : "bg-orange-600 text-white shadow-orange-200 hover:bg-orange-700"
              }`}
            >
              {showForm ? <X size={16} /> : <Plus size={16} />}
              {showForm ? "Fechar" : "Nova palavra"}
            </button>
          </div>
        </div>

        {showForm && (
          <form className="space-y-5 px-5 py-5 sm:px-6" onSubmit={save}>
            <div>
              <label className="mb-2 block text-sm font-semibold text-gray-800">
                Termo proibido
              </label>
              <input
                value={term}
                onChange={(e) => setTerm(e.target.value)}
                placeholder="Digite uma palavra ou expressão"
                className="w-full rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
              <p className="mt-2 text-xs text-gray-500">
                A verificação não diferencia maiúsculas e minúsculas.
              </p>
            </div>

            <div>
              <p className="mb-2 text-sm font-semibold text-gray-800">
                Aplicar em
              </p>
              <div className="grid gap-3 sm:grid-cols-3">
                <button
                  type="button"
                  onClick={toggleRecipes}
                  className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                    recipes
                      ? "border-orange-300 bg-orange-50 text-orange-700"
                      : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                  }`}
                >
                  <FileText size={17} />
                  Receitas
                </button>
                <button
                  type="button"
                  onClick={toggleIngredients}
                  className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                    ingredients
                      ? "border-orange-300 bg-orange-50 text-orange-700"
                      : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                  }`}
                >
                  <UtensilsCrossed size={17} />
                  Ingredientes
                </button>
                <button
                  type="button"
                  onClick={toggleComments}
                  className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                    comments
                      ? "border-orange-300 bg-orange-50 text-orange-700"
                      : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                  }`}
                >
                  <MessageSquare size={17} />
                  Comentários
                </button>
              </div>
            </div>

            <button
              type="submit"
              className="inline-flex h-11 items-center gap-2 rounded-xl bg-orange-600 px-4 text-sm font-semibold text-white shadow-md shadow-orange-200 transition-colors hover:bg-orange-700"
            >
              <Plus size={16} /> Adicionar palavra
            </button>
          </form>
        )}
      </section>

      <section className="rounded-2xl bg-white p-5 shadow-lg shadow-gray-200/60 sm:p-6">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h2 className="text-xl font-black text-gray-900">
              Lista de bloqueios
            </h2>
            <p className="mt-1 text-sm text-gray-500">
              {words.length} palavra{words.length !== 1 ? "s" : ""} cadastrada
              {words.length !== 1 ? "s" : ""}
            </p>
          </div>
        </div>

        <div className="mt-5 space-y-3">
          {words.length === 0 ? (
            <div className="rounded-2xl border border-dashed border-gray-200 bg-gray-50 px-4 py-10 text-center">
              <Ban size={36} className="mx-auto text-gray-300" />
              <h3 className="mt-3 font-semibold text-gray-700">
                Nenhuma palavra banida
              </h3>
              <p className="mt-1 text-sm text-gray-500">
                Adicione o primeiro termo para ativar a moderação automática.
              </p>
            </div>
          ) : (
            words.map((word) => (
              <div
                key={word.id}
                className="rounded-2xl border border-gray-100 bg-white p-4 shadow-sm transition hover:border-orange-100 hover:shadow-md"
              >
                {editingId === word.id ? (
                  <div className="space-y-4">
                    <input
                      value={editingTerm}
                      onChange={(event) => setEditingTerm(event.target.value)}
                      className="w-full rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
                    />

                    <div className="grid gap-3 sm:grid-cols-3">
                      <button
                        type="button"
                        onClick={toggleEditingRecipes}
                        className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                          editingRecipes
                            ? "border-orange-300 bg-orange-50 text-orange-700"
                            : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                        }`}
                      >
                        <FileText size={17} />
                        Receitas
                      </button>
                      <button
                        type="button"
                        onClick={toggleEditingIngredients}
                        className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                          editingIngredients
                            ? "border-orange-300 bg-orange-50 text-orange-700"
                            : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                        }`}
                      >
                        <UtensilsCrossed size={17} />
                        Ingredientes
                      </button>
                      <button
                        type="button"
                        onClick={toggleEditingComments}
                        className={`flex items-center gap-2 rounded-xl border px-4 py-3 text-left text-sm font-semibold transition-colors ${
                          editingComments
                            ? "border-orange-300 bg-orange-50 text-orange-700"
                            : "border-gray-200 bg-white text-gray-500 hover:bg-gray-50"
                        }`}
                      >
                        <MessageSquare size={17} />
                        Comentários
                      </button>
                    </div>

                    <div className="flex flex-wrap gap-2">
                      <button
                        type="button"
                        onClick={() => saveEdit(word.id)}
                        className="inline-flex h-10 items-center gap-2 rounded-xl bg-orange-600 px-4 text-sm font-semibold text-white transition-colors hover:bg-orange-700"
                      >
                        <Check size={16} /> Salvar
                      </button>
                      <button
                        type="button"
                        onClick={cancelEdit}
                        className="inline-flex h-10 items-center gap-2 rounded-xl border border-gray-200 px-4 text-sm font-semibold text-gray-600 transition-colors hover:bg-gray-50"
                      >
                        <X size={16} /> Cancelar
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                    <div className="min-w-0">
                      <div className="truncate text-base font-bold text-gray-900">
                        {word.term}
                      </div>
                      <div className="mt-2 flex flex-wrap gap-2 text-xs font-medium">
                        {word.appliesToRecipes && (
                          <span className="inline-flex items-center gap-1 rounded-full bg-orange-50 px-2.5 py-1 text-orange-700">
                            <FileText size={13} />
                            Receitas
                          </span>
                        )}
                        {word.appliesToIngredients && (
                          <span className="inline-flex items-center gap-1 rounded-full bg-emerald-50 px-2.5 py-1 text-emerald-700">
                            <UtensilsCrossed size={13} />
                            Ingredientes
                          </span>
                        )}
                        {word.appliesToComments && (
                          <span className="inline-flex items-center gap-1 rounded-full bg-sky-50 px-2.5 py-1 text-sky-700">
                            <MessageSquare size={13} />
                            Comentários
                          </span>
                        )}
                      </div>
                    </div>
                    <div className="flex gap-2 sm:shrink-0">
                      <button
                        type="button"
                        onClick={() => startEdit(word)}
                        className="inline-flex h-10 w-10 items-center justify-center rounded-xl border border-gray-200 text-gray-600 transition-colors hover:bg-gray-50"
                        aria-label="Editar palavra"
                      >
                        <PencilLine size={16} />
                      </button>
                      <button
                        type="button"
                        onClick={() => remove(word.id)}
                        className="inline-flex h-10 w-10 items-center justify-center rounded-xl border border-red-200 text-red-600 transition-colors hover:bg-red-50"
                        aria-label="Excluir palavra"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                )}
              </div>
            ))
          )}
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
