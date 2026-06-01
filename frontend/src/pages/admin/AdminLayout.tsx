import { Link, NavLink, Outlet } from "react-router-dom";
import {
  ArrowLeft,
  Ban,
  LayoutDashboard,
  MessageSquare,
  Trash2,
  Users,
} from "lucide-react";

const items = [
  { to: "/admin/overview", label: "Visão geral", icon: LayoutDashboard },
  { to: "/admin/words", label: "Palavras banidas", icon: Ban },
  { to: "/admin/users", label: "Usuários", icon: Users },
  { to: "/admin/recipes", label: "Receitas", icon: Trash2 },
  { to: "/admin/comments", label: "Comentários", icon: MessageSquare },
];

export function AdminLayout() {
  return (
    <div className="relative min-h-screen bg-[radial-gradient(circle_at_top,_rgba(251,146,60,0.14),_transparent_35%),linear-gradient(180deg,#fff7ed_0%,#ffffff_35%,#f8fafc_100%)]">
      <Link
        to="/perfil"
        className="absolute left-4 top-4 z-10 inline-flex h-11 w-11 items-center justify-center rounded-full border border-orange-100 bg-white/90 text-orange-700 shadow-lg shadow-orange-100/50 transition hover:bg-orange-50"
        aria-label="Voltar ao perfil"
      >
        <ArrowLeft size={18} />
      </Link>

      <div className="mx-auto grid min-h-screen max-w-7xl gap-6 px-4 py-6 lg:grid-cols-[18rem_1fr] lg:px-6">
        <aside className="rounded-3xl border border-orange-100 bg-white/90 p-5 shadow-xl shadow-orange-100/50 backdrop-blur">
          <div className="mb-6">
            <div className="text-lg font-black text-gray-900 text-center">
              Admin
            </div>
            <div className="text-sm text-gray-500 andotext-center">
              Moderação e controle centralizado
            </div>
          </div>

          <nav className="space-y-2">
            {items.map(({ to, label, icon: Icon }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium transition ${isActive ? "bg-gray-900 text-white shadow" : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"}`
                }
              >
                <Icon size={18} />
                {label}
              </NavLink>
            ))}
          </nav>
        </aside>

        <main className="space-y-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
