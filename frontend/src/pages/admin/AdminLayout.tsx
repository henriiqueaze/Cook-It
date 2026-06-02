import { Link, NavLink, Outlet } from "react-router-dom";
import {
  ArrowLeft,
  Ban,
  LayoutDashboard,
  MessageSquare,
  Trash2,
  Users,
} from "lucide-react";
import { BottomNav } from "@/components/BottomNav";

const items = [
  { to: "/admin/overview", label: "Visão geral", icon: LayoutDashboard },
  { to: "/admin/words", label: "Palavras banidas", icon: Ban },
  { to: "/admin/users", label: "Usuários", icon: Users },
  { to: "/admin/recipes", label: "Receitas", icon: Trash2 },
  { to: "/admin/comments", label: "Comentários", icon: MessageSquare },
];

export function AdminLayout() {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-4 pb-9 pt-8 text-white shadow-lg sm:px-6">
        <div className="relative mx-auto max-w-7xl">
          <Link
            to="/perfil"
            className="absolute left-0 top-0 inline-flex h-11 w-11 items-center justify-center rounded-xl bg-white/15 text-white transition-colors hover:bg-white/25"
            aria-label="Voltar ao perfil"
          >
            <ArrowLeft size={18} />
          </Link>

          <div className="mx-auto flex max-w-2xl flex-col items-center px-14 text-center">
            <h1 className="text-2xl font-bold">Painel admin</h1>
            <p className="mt-1 text-x1 text-orange-100">
              Moderação e controle centralizado do Cook-It
            </p>
          </div>
        </div>
      </header>

      <div className="mx-auto -mt-5 max-w-7xl px-4 sm:px-6">
        <nav className="flex gap-2 overflow-x-auto rounded-2xl bg-white p-2 shadow-lg shadow-gray-200/70 [scrollbar-width:none]">
          {items.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                `inline-flex h-11 shrink-0 items-center gap-2 rounded-xl px-3 text-sm font-semibold transition-colors ${
                  isActive
                    ? "bg-orange-600 text-white shadow-md shadow-orange-200"
                    : "text-gray-600 hover:bg-orange-50 hover:text-orange-700"
                }`
              }
            >
              <Icon size={16} />
              {label}
            </NavLink>
          ))}
        </nav>
      </div>

      <main className="mx-auto max-w-7xl px-4 pb-[calc(5.5rem+env(safe-area-inset-bottom))] pt-5 sm:px-6 lg:pt-6">
        <Outlet />
      </main>

      <BottomNav />
    </div>
  );
}
