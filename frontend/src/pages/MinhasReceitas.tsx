import { useNavigate } from "react-router-dom";
import { Plus, ChefHat } from "lucide-react";
import { ReceitaCard } from "../components/ReceitaCard";
import { receitasMock } from "../mocks";
import { useAuth } from "../contexts/AuthContext";

export function MinhasReceitas() {
  const navigate = useNavigate();
  const { usuario, estaAutenticado } = useAuth();

  const minhasReceitas = receitasMock.filter((r) => r.autor.id === usuario?.id);

  if (!estaAutenticado) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-6 text-center">
        <ChefHat size={48} className="text-gray-300 mb-4" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="text-sm text-gray-400 mt-1 mb-6">
          Entre na sua conta para ver suas receitas
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
        <h1 className="text-xl font-bold">Minhas Receitas</h1>
        <p className="text-green-100 text-sm mt-1">
          {minhasReceitas.length} receita
          {minhasReceitas.length !== 1 ? "s" : ""} publicada
          {minhasReceitas.length !== 1 ? "s" : ""}
        </p>
      </div>

      <div className="px-6 mt-6">
        {minhasReceitas.length === 0 && (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <ChefHat size={48} className="text-gray-300 mb-4" />
            <h2 className="text-lg font-semibold text-gray-500">
              Nenhuma receita ainda
            </h2>
            <p className="text-sm text-gray-400 mt-1 mb-6">
              Que tal criar sua primeira receita?
            </p>
            <button
              onClick={() => navigate("/criar-receita")}
              className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer"
            >
              Criar receita
            </button>
          </div>
        )}

        {minhasReceitas.length > 0 && (
          <div className="grid grid-cols-2 gap-3">
            {minhasReceitas.map((receita) => (
              <ReceitaCard key={receita.id} receita={receita} />
            ))}
          </div>
        )}
      </div>

      {minhasReceitas.length > 0 && (
        <button
          onClick={() => navigate("/criar-receita")}
          className="fixed bottom-20 right-6 bg-green-600 text-white rounded-full p-4 shadow-lg cursor-pointer"
        >
          <Plus size={24} />
        </button>
      )}
    </div>
  );
}
