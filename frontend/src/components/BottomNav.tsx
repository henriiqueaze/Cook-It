import { Link, useLocation } from "react-router-dom";
import { Home, Search, BookOpen, Heart, User } from "lucide-react";

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
    <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 flex justify-around items-center h-16 z-50">
      {itens.map(({ icone: Icone, label, rota }) => {
        const ativo = pathname === rota;
        return (
          <Link
            key={rota}
            to={rota}
            className={`flex flex-col items-center gap-1 text-xs transition-colors ${
              ativo ? "text-green-600" : "text-gray-400"
            }`}
          >
            <Icone size={22} />
            <span>{label}</span>
          </Link>
        );
      })}
    </nav>
  );
}
