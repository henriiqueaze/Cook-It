import { useNavigate } from "react-router-dom";
import { Heart } from "lucide-react";
import { ReceitaCard } from "../components/ReceitaCard";
import { receitasMock } from "../mocks";
import { useAuth } from "../contexts/AuthContext";

export function Favoritos() {
  const navigate = useNavigate();
  const { estaAutenticado } = useAuth();

  const receitasFavoritas = receitasMock.filter((r) => r.favoritada);

  if (!estaAutenticado) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-6 text-center">
        <Heart size={48} className="text-gray-300 mb-4" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="text-sm text-gray-400 mt-1 mb-6">
          Entre na sua conta para ver seus favoritos
        </p>
        <button
          onClick={() => navigate("/login")}
          className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer"
        >
          Fazer login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-green-600 text-white pt-12 pb-6 px-6 rounded-b-3xl">
        <h1 className="text-xl font-bold">Favoritos</h1>
        <p className="text-green-100 text-sm mt-1">
          {receitasFavoritas.length} receita
          {receitasFavoritas.length !== 1 ? "s" : ""} favorita
          {receitasFavoritas.length !== 1 ? "s" : ""}
        </p>
      </div>

      <div className="px-6 mt-6">
        {receitasFavoritas.length === 0 && (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <Heart size={48} className="text-gray-300 mb-4" />
            <h2 className="text-lg font-semibold text-gray-500">
              Nenhum favorito ainda
            </h2>
            <p className="text-sm text-gray-400 mt-1 mb-6">
              Explore receitas e favorite as que você mais gostar
            </p>
            <button
              onClick={() => navigate("/")}
              className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer"
            >
              Explorar receitas
            </button>
          </div>
        )}

        {receitasFavoritas.length > 0 && (
          <div className="grid grid-cols-2 gap-3">
            {receitasFavoritas.map((receita) => (
              <ReceitaCard key={receita.id} receita={receita} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
