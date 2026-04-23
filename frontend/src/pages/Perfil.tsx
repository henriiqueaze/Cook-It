import { useEffect, useState } from "react";
import { BookOpen, Edit, Heart, LogOut, Star, User } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { ReceitaCard } from "@/components/ReceitaCard";
import { useAuth } from "@/contexts/AuthContext";
import { useFavorites } from "@/contexts/FavoritesContext";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";

export function Perfil() {
  const navigate = useNavigate();
  const { usuario, estaAutenticado, sair } = useAuth();
  const { favoritos } = useFavorites();
  const [minhasReceitas, setMinhasReceitas] = useState<Receita[]>([]);

  useEffect(() => {
    if (!usuario?.id) {
      setMinhasReceitas([]);
      return;
    }

    let ativo = true;

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
      });

    return () => {
      ativo = false;
    };
  }, [usuario?.id]);

  const totalAvaliacoes = minhasReceitas.reduce(
    (soma, receita) => soma + receita.totalAvaliacoes,
    0,
  );

  function handleSair() {
    sair();
    toast.success("Até mais!");
    navigate("/login");
  }

  if (!estaAutenticado) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
        <User size={48} className="mb-4 text-gray-300" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="mt-1 mb-6 text-sm text-gray-400">
          Entre na sua conta para ver seu perfil
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
      <div className="rounded-b-3xl bg-linear-to-b from-orange-500 to-orange-600 px-6 pb-12 pt-9 text-white">
        <div className="mb-6 flex items-start justify-between">
          <h1 className="text-2xl font-bold">Perfil</h1>
        </div>

        <div className="flex items-center gap-4">
          <div className="h-20 w-20 overflow-hidden rounded-full border-4 border-white bg-white shadow-lg">
            {usuario?.photo ? (
              <img
                src={usuario.photo}
                alt={usuario.name}
                className="h-full w-full object-cover"
              />
            ) : (
              <div className="flex h-full w-full items-center justify-center">
                <User size={36} className="text-orange-600" />
              </div>
            )}
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-bold">{usuario?.name}</h2>
            <p className="text-sm text-orange-100">{usuario?.email}</p>
          </div>
        </div>

        <Link
          to="/editar-perfil"
          className="mt-4 flex w-full items-center justify-center gap-2 rounded-lg bg-white/20 px-4 py-2 font-medium transition-colors hover:bg-white/30"
        >
          <Edit size={16} />
          Configurações
        </Link>
      </div>

      <div className="mt-6 space-y-4 px-6 pb-4">
        <div className="grid grid-cols-3 gap-4 rounded-2xl bg-white p-4 text-center shadow-sm">
          <div>
            <div className="text-2xl font-bold text-orange-600">
              {minhasReceitas.length}
            </div>
            <div className="mt-1 text-xs text-gray-500">Receitas</div>
          </div>
          <div>
            <div className="text-2xl font-bold text-orange-600">
              {favoritos.length}
            </div>
            <div className="mt-1 text-xs text-gray-500">Favoritos</div>
          </div>
          <div>
            <div className="text-2xl font-bold text-orange-600">
              {totalAvaliacoes}
            </div>
            <div className="mt-1 text-xs text-gray-500">Avaliações</div>
          </div>
        </div>

        <div className="divide-y divide-gray-100 rounded-2xl bg-white shadow-sm">
          <Link
            to="/minhas-receitas"
            className="flex items-center gap-3 p-4 transition-colors hover:bg-gray-50"
          >
            <div className="rounded-lg bg-orange-100 p-2">
              <BookOpen size={20} className="text-orange-600" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Minhas receitas</h3>
              <p className="text-sm text-gray-500">
                {minhasReceitas.length} receitas criadas
              </p>
            </div>
          </Link>

          <Link
            to="/favoritos"
            className="flex items-center gap-3 p-4 transition-colors hover:bg-gray-50"
          >
            <div className="rounded-lg bg-red-100 p-2">
              <Heart size={20} className="text-red-500" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Receitas favoritas</h3>
              <p className="text-sm text-gray-500">
                {favoritos.length} receitas salvas
              </p>
            </div>
          </Link>

          <div className="flex items-center gap-3 p-4">
            <div className="rounded-lg bg-yellow-100 p-2">
              <Star size={20} className="text-yellow-500" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Minhas avaliações</h3>
              <p className="text-sm text-gray-500">
                {totalAvaliacoes} receitas avaliadas
              </p>
            </div>
          </div>
        </div>

        {minhasReceitas.length > 0 && (
          <div>
            <div className="mb-3 flex items-center justify-between">
              <h2 className="font-semibold text-gray-800">Minhas receitas</h2>
              <Link to="/minhas-receitas" className="text-sm text-orange-600">
                Ver todas
              </Link>
            </div>
            <div className="grid grid-cols-2 gap-3">
              {minhasReceitas.slice(0, 2).map((receita) => (
                <ReceitaCard key={receita.id} receita={receita} />
              ))}
            </div>
          </div>
        )}

        <button
          type="button"
          onClick={handleSair}
          className="flex w-full items-center justify-center gap-2 rounded-lg border border-red-300 bg-white py-3 font-medium text-red-600 transition-colors hover:bg-red-50"
        >
          <LogOut size={18} />
          Sair da conta
        </button>
      </div>
    </div>
  );
}
