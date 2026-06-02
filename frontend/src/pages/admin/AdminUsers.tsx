import { useEffect, useMemo, useState } from "react";
import { Ban, Search, ShieldCheck } from "lucide-react";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/ConfirmDialog";
import { adminService } from "@/services/adminService";
import type { Usuario } from "@/types";

export function AdminUsers() {
  const [users, setUsers] = useState<Usuario[]>([]);
  const [query, setQuery] = useState("");
  const [pendingUser, setPendingUser] = useState<Usuario | null>(null);
  const [pendingPromoteUser, setPendingPromoteUser] =
    useState<Usuario | null>(null);

  useEffect(() => {
    adminService.usuarios().then(setUsers);
  }, []);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return users;

    return users.filter((user) =>
      [user.name, user.email, user.role, user.banned ? "banido" : "ativo"].some(
        (value) => String(value).toLowerCase().includes(q),
      ),
    );
  }, [query, users]);

  async function toggle(user: Usuario) {
    if (user.role === "ADMIN") {
      return;
    }
    setPendingUser(user);
  }

  async function promote(user: Usuario) {
    if (user.role === "ADMIN") {
      return;
    }

    setPendingPromoteUser(user);
  }

  async function confirmToggle() {
    if (!pendingUser) {
      return;
    }

    try {
      const updated = pendingUser.banned
        ? await adminService.desbanirUsuario(String(pendingUser.id))
        : await adminService.banirUsuario(String(pendingUser.id));

      setUsers((current) =>
        current.map((item) => (item.id === pendingUser.id ? updated : item)),
      );
      toast.success(updated.banned ? "Usuário banido" : "Usuário desbanido");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível alterar o usuário",
      );
    } finally {
      setPendingUser(null);
    }
  }

  async function confirmPromote() {
    if (!pendingPromoteUser) {
      return;
    }

    try {
      const updated = await adminService.promoverUsuario(
        String(pendingPromoteUser.id),
      );

      setUsers((current) =>
        current.map((item) =>
          item.id === pendingPromoteUser.id ? updated : item,
        ),
      );
      toast.success("Usuário promovido a admin");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível promover o usuário",
      );
    } finally {
      setPendingPromoteUser(null);
    }
  }

  return (
    <section className="rounded-3xl bg-white p-6 shadow-lg shadow-gray-200/60">
      <h1 className="text-2xl font-black text-gray-900">Usuários</h1>
      <p className="mt-2 text-sm text-gray-500">
        Banir e liberar contas individualmente.
      </p>

      <div className="mt-5 flex items-center gap-2 rounded-2xl border border-gray-100 bg-gray-50 p-4">
        <Search size={16} className="text-gray-400" />
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Pesquisar usuário"
          className="w-full bg-transparent text-sm outline-none"
        />
      </div>

      <div className="mt-6 space-y-3">
        {filtered.map((user) => (
          <div
            key={String(user.id)}
            className="flex flex-col gap-3 rounded-2xl border border-gray-100 p-4 sm:flex-row sm:items-center sm:justify-between"
          >
            <div>
              <div className="font-semibold text-gray-900">{user.name}</div>
              <div className="text-sm text-gray-500">{user.email}</div>
              <div className="mt-2 flex flex-wrap gap-2 text-xs">
                <span className="rounded-full bg-gray-100 px-2 py-1 text-gray-700">
                  {user.role ?? "USER"}
                </span>
                <span
                  className={`rounded-full px-2 py-1 ${user.banned ? "bg-red-50 text-red-700" : "bg-emerald-50 text-emerald-700"}`}
                >
                  {user.banned ? "Banido" : "Ativo"}
                </span>
              </div>
            </div>

            {user.role !== "ADMIN" ? (
              <div className="flex flex-wrap gap-2 sm:justify-end">
                <button
                  type="button"
                  onClick={() => promote(user)}
                  className="inline-flex items-center gap-2 self-start rounded-xl border border-orange-200 px-3 py-2 text-sm font-medium text-orange-700"
                >
                  <ShieldCheck size={14} /> Promover
                </button>
                <button
                  type="button"
                  onClick={() => toggle(user)}
                  className={`inline-flex items-center gap-2 self-start rounded-xl px-3 py-2 text-sm font-medium ${user.banned ? "border border-emerald-200 text-emerald-700" : "border border-red-200 text-red-600"}`}
                >
                  <Ban size={14} /> {user.banned ? "Desbanir" : "Banir"}
                </button>
              </div>
            ) : null}
          </div>
        ))}
      </div>

      <ConfirmDialog
        open={pendingUser !== null}
        title={pendingUser?.banned ? "Desbanir usuário" : "Banir usuário"}
        message={
          pendingUser
            ? `Tem certeza que deseja ${pendingUser.banned ? "desbanir" : "banir"} ${pendingUser.name}? Essa ação pode ser revertida depois.`
            : ""
        }
        confirmLabel={pendingUser?.banned ? "Desbanir" : "Banir"}
        onConfirm={confirmToggle}
        onCancel={() => setPendingUser(null)}
      />

      <ConfirmDialog
        open={pendingPromoteUser !== null}
        title="Promover usuário"
        message={
          pendingPromoteUser
            ? `Tem certeza que deseja promover ${pendingPromoteUser.name} a administrador?`
            : ""
        }
        confirmLabel="Promover"
        onConfirm={confirmPromote}
        onCancel={() => setPendingPromoteUser(null)}
      />
    </section>
  );
}
