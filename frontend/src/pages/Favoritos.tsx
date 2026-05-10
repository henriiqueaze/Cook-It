import { useEffect } from "react";
import { Heart } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { ReceitaCard } from "@/components/ReceitaCard";
import { useAuth } from "@/contexts/AuthContext";
import { useFavorites } from "@/contexts/FavoritesContext";

export function Favoritos() {
  const navigate = useNavigate();
  const { estaAutenticado } = useAuth();
  const { favoritos, carregandoFavoritos } = useFavorites();
  const travarRolagem =
    estaAutenticado && !carregandoFavoritos && favoritos.length === 0;

  useEffect(() => {
    if (!travarRolagem) {
      return;
    }

    const overflowBodyAnterior = document.body.style.overflowY;
    const overflowHtmlAnterior = document.documentElement.style.overflowY;

    document.body.style.overflowY = "hidden";
    document.documentElement.style.overflowY = "hidden";

    return () => {
      document.body.style.overflowY = overflowBodyAnterior;
      document.documentElement.style.overflowY = overflowHtmlAnterior;
    };
  }, [travarRolagem]);

  if (!estaAutenticado) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
        <Heart size={48} className="mb-4 text-gray-300" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="mt-1 mb-6 text-sm text-gray-400">
          Entre na sua conta para ver seus favoritos
        </p>
        <button
          type="button"
          onClick={() => navigate("/login")}
          className="rounded-lg bg-orange-600 px-6 py-3 font-medium text-white transition-colors hover:bg-orange-700"
        >
          Fazer login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-6 pt-9 text-white shadow-lg">
        <h1 className="text-xl font-bold">Favoritos</h1>
        <p className="mt-1 text-sm text-orange-100">
          {favoritos.length} receita{favoritos.length !== 1 ? "s" : ""} favorita
          {favoritos.length !== 1 ? "s" : ""}
        </p>
      </div>

      <div className="px-6 mt-6">
        {carregandoFavoritos ? (
          <div className="text-sm text-gray-500">Carregando favoritos...</div>
        ) : favoritos.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <Heart size={48} className="mb-4 text-gray-300" />
            <h2 className="text-lg font-semibold text-gray-500">
              Nenhum favorito ainda
            </h2>
            <p className="mt-1 mb-6 text-sm text-gray-400">
              Explore receitas e favorite as que você mais gostar
            </p>
            <button
              type="button"
              onClick={() => navigate("/")}
              className="rounded-lg bg-orange-600 px-6 py-3 font-medium text-white transition-colors hover:bg-orange-700"
            >
              Explorar receitas
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3">
            {favoritos.map((receita) => (
              <ReceitaCard key={receita.id} receita={receita} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
