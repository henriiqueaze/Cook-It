import { useEffect, useState } from "react";
import { ChefHat, Plus } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { ReceitaCard } from "@/components/ReceitaCard";
import { useAuth } from "@/contexts/AuthContext";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";

export function MinhasReceitas() {
  const navigate = useNavigate();
  const { usuario, estaAutenticado } = useAuth();
  const [minhasReceitas, setMinhasReceitas] = useState<Receita[]>([]);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    if (!usuario?.id) {
      setMinhasReceitas([]);
      setCarregando(false);
      return;
    }

    let ativo = true;
    setCarregando(true);

    receitaService
      .minhasReceitas(usuario.id)
      .then((lista) => {
        if (ativo) {
          setMinhasReceitas(lista);
        }
      })
      .catch(() => {
        if (ativo) {
          setMinhasReceitas([]);
        }
      })
      .finally(() => {
        if (ativo) {
          setCarregando(false);
        }
      });

    return () => {
      ativo = false;
    };
  }, [usuario?.id]);

  if (!estaAutenticado) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
        <ChefHat size={48} className="mb-4 text-gray-300" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="mt-1 mb-6 text-sm text-gray-400">
          Entre na sua conta para ver suas receitas
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
      <div className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-6 pt-12 text-white shadow-lg">
        <h1 className="text-xl font-bold">Minhas receitas</h1>
        <p className="mt-1 text-sm text-orange-100">
          {minhasReceitas.length} receita
          {minhasReceitas.length !== 1 ? "s" : ""} publicada
          {minhasReceitas.length !== 1 ? "s" : ""}
        </p>
      </div>

      <div className="px-6 mt-6">
        {carregando ? (
          <div className="text-sm text-gray-500">Carregando receitas...</div>
        ) : minhasReceitas.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <ChefHat size={48} className="mb-4 text-gray-300" />
            <h2 className="text-lg font-semibold text-gray-500">
              Nenhuma receita ainda
            </h2>
            <p className="mt-1 mb-6 text-sm text-gray-400">
              Que tal criar sua primeira receita?
            </p>
            <button
              type="button"
              onClick={() => navigate("/criar-receita")}
              className="rounded-lg bg-orange-600 px-6 py-3 font-medium text-white transition-colors hover:bg-orange-700"
            >
              Criar receita
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3">
            {minhasReceitas.map((receita) => (
              <ReceitaCard key={receita.id} receita={receita} />
            ))}
          </div>
        )}
      </div>

      {minhasReceitas.length > 0 && (
        <button
          type="button"
          onClick={() => navigate("/criar-receita")}
          className="fixed bottom-20 right-6 rounded-full bg-orange-600 p-4 text-white shadow-lg transition-transform hover:scale-105"
          aria-label="Criar receita"
        >
          <Plus size={24} />
        </button>
      )}
    </div>
  );
}
