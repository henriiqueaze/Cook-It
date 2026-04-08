import { BookOpen, Heart, Home, Search, User } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

const itens = [
  { icone: Home, label: "Início", rota: "/" },
  { icone: Search, label: "Buscar", rota: "/busca" },
  { icone: BookOpen, label: "Minhas", rota: "/minhas-receitas" },
  { icone: Heart, label: "Favoritos", rota: "/favoritos" },
  { icone: User, label: "Perfil", rota: "/perfil" },
];

export function BottomNav() {
  const { pathname } = useLocation();

  return (
    <nav className="fixed inset-x-0 bottom-0 z-50 border-t border-gray-200 bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/80 pb-[env(safe-area-inset-bottom)]">
      <div className="mx-auto grid h-16 max-w-3xl grid-cols-5">
        {itens.map(({ icone: Icone, label, rota }) => {
          const ativo = pathname === rota || pathname.startsWith(`${rota}/`);

          return (
            <Link
              key={rota}
              to={rota}
              className={`flex flex-col items-center justify-center gap-1 text-xs transition-colors ${
                ativo ? "text-orange-600" : "text-gray-400"
              }`}
            >
              <Icone size={20} />
              <span>{label}</span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
