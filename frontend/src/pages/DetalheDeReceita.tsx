import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  ArrowLeft,
  Clock,
  Users,
  Heart,
  Minus,
  Plus,
  Trash2,
} from "lucide-react";
import { receitasMock } from "../mocks";
import { useAuth } from "../contexts/AuthContext";
import { useFavorites } from "../contexts/FavoritesContext";
import { RatingStars } from "../components/AvaliacaoEstrelas";
import { receitaService } from "../services/receitaService";
import type { Receita } from "../types";
import { toast } from "sonner";

function buscarReceitaMock(id?: string) {
  return receitasMock.find((receita) => String(receita.id) === String(id));
}

export function DetalheReceita() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { usuario, estaAutenticado } = useAuth();
  const { toggleFavorite, isFavorite } = useFavorites();

  const receitaMock = buscarReceitaMock(id);
  const [receita, setReceita] = useState<Receita | null>(receitaMock ?? null);
  const [carregandoReceita, setCarregandoReceita] = useState(!receitaMock);
  const [multiplicador, setMultiplicador] = useState(1);
  const [novoComentario, setNovoComentario] = useState("");
  const [avaliacaoUsuario, setAvaliacaoUsuario] = useState(0);

  useEffect(() => {
    let ativo = true;

    if (!id) {
      setReceita(null);
      setCarregandoReceita(false);
      return;
    }

    receitaService
      .getRecipeById(id)
      .then((dados) => {
        if (ativo) {
          setReceita(dados);
        }
      })
      .catch(() => {
        if (ativo) {
          setReceita(buscarReceitaMock(id) ?? null);
        }
      })
      .finally(() => {
        if (ativo) {
          setCarregandoReceita(false);
        }
      });

    return () => {
      ativo = false;
    };
  }, [id]);

  if (carregandoReceita && !receita) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <p className="text-gray-500">Carregando receita...</p>
      </div>
    );
  }

  if (!receita) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <p className="text-gray-500">Receita não encontrada.</p>
        <button
          onClick={() => navigate(-1)}
          className="mt-4 text-orange-600 font-medium"
        >
          Voltar
        </button>
      </div>
    );
  }

  const ehMinhaReceita = usuario?.id === receita.autor.id;
  const favoritada = isFavorite(receita.id);

  function handleFavoritar() {
    if (!receita) {
      return;
    }

    if (!estaAutenticado) {
      toast.error("Faça login para favoritar receitas");
      navigate("/login");
      return;
    }

    toggleFavorite(receita);
    toast.success(
      favoritada ? "Removido dos favoritos" : "Adicionado aos favoritos!",
    );
  }

  function handleAvaliar(nota: number) {
    if (!estaAutenticado) {
      toast.error("Faça login para avaliar receitas");
      navigate("/login");
      return;
    }
    setAvaliacaoUsuario(nota);
    toast.success("Avaliação enviada!");
  }

  function handleComentario() {
    if (!estaAutenticado) {
      toast.error("Faça login para comentar");
      navigate("/login");
      return;
    }
    if (!novoComentario.trim()) return;
    toast.success("Comentário adicionado!");
    setNovoComentario("");
  }

  function handleDeletar() {
    toast.success("Receita deletada!");
    navigate("/minhas-receitas");
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="relative w-full h-64 bg-gray-200">
        {receita.imagemUrl && (
          <img
            src={receita.imagemUrl}
            alt={receita.titulo}
            className="w-full h-full object-cover"
          />
        )}
        <button
          onClick={() => navigate(-1)}
          className="absolute top-4 left-4 bg-white/90 rounded-full p-2 shadow-md cursor-pointer"
        >
          <ArrowLeft size={20} className="text-gray-700" />
        </button>
        <button
          onClick={handleFavoritar}
          className="absolute top-4 right-4 bg-white/90 rounded-full p-2 shadow-md cursor-pointer"
        >
          <Heart
            size={20}
            className={
              favoritada ? "text-red-500 fill-red-500" : "text-gray-400"
            }
          />
        </button>
        {ehMinhaReceita && (
          <button
            onClick={handleDeletar}
            className="absolute top-4 right-16 bg-white/90 rounded-full p-2 shadow-md cursor-pointer"
          >
            <Trash2 size={20} className="text-red-500" />
          </button>
        )}
      </div>

      <div className="px-6 py-4 space-y-4">
        <div>
          <h1 className="text-xl font-bold text-gray-800">{receita.titulo}</h1>
          <p className="text-sm text-gray-500 mt-1">{receita.descricao}</p>
        </div>

        <div className="flex gap-4">
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Clock size={16} className="text-orange-600" />
            {receita.tempoPreparo} min
          </div>
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Users size={16} className="text-orange-600" />
            {receita.porcoes * multiplicador} porções
          </div>
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <RatingStars
              avaliacao={receita.avaliacao}
              somenteLeitura
              tamanho="sm"
            />
            <span className="text-xs text-gray-400">
              ({receita.totalAvaliacoes})
            </span>
          </div>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">Ajustar Porções</h2>
          <div className="flex items-center gap-4">
            <button
              onClick={() => setMultiplicador(Math.max(1, multiplicador - 1))}
              className="w-8 h-8 bg-orange-100 text-orange-600 rounded-full flex items-center justify-center cursor-pointer hover:bg-orange-200 transition-colors"
            >
              <Minus size={16} />
            </button>
            <span className="text-lg font-semibold text-gray-800">
              {multiplicador}x
            </span>
            <button
              onClick={() => setMultiplicador(multiplicador + 1)}
              className="w-8 h-8 bg-orange-100 text-orange-600 rounded-full flex items-center justify-center cursor-pointer hover:bg-orange-200 transition-colors"
            >
              <Plus size={16} />
            </button>
            <span className="text-sm text-gray-500">
              {receita.porcoes * multiplicador} porções no total
            </span>
          </div>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">Ingredientes</h2>
          <ul className="space-y-2">
            {receita.ingredientes.map((ing) => (
              <li key={ing.id} className="flex justify-between text-sm">
                <span className="text-gray-700">{ing.nome}</span>
                <span className="text-gray-400">
                  {(Number(ing.quantidade) * multiplicador).toFixed(
                    Number(ing.quantidade) % 1 === 0 ? 0 : 1,
                  )}{" "}
                  {ing.unidade}
                </span>
              </li>
            ))}
          </ul>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">Modo de Preparo</h2>
          <ol className="space-y-3">
            {receita.instrucoes.map((passo, index) => (
              <li key={index} className="flex gap-3 text-sm">
                <span className="shrink-0 w-6 h-6 bg-orange-600 text-white rounded-full flex items-center justify-center text-xs font-bold">
                  {index + 1}
                </span>
                <span className="text-gray-600 pt-0.5">{passo}</span>
              </li>
            ))}
          </ol>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">
            Avalie esta receita
          </h2>
          <RatingStars
            avaliacao={avaliacaoUsuario}
            onChange={handleAvaliar}
            tamanho="lg"
          />
          {avaliacaoUsuario > 0 && (
            <p className="text-sm text-gray-500 mt-2">
              Você avaliou com {avaliacaoUsuario} estrela
              {avaliacaoUsuario > 1 ? "s" : ""}
            </p>
          )}
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">Comentários</h2>

          <div className="flex gap-2">
            <input
              type="text"
              value={novoComentario}
              onChange={(e) => setNovoComentario(e.target.value)}
              placeholder="Deixe seu comentário..."
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
            />
            <button
              onClick={handleComentario}
              disabled={!novoComentario.trim()}
              className="bg-orange-600 text-white px-4 py-2 rounded-lg text-sm font-medium cursor-pointer hover:bg-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
            >
              Enviar
            </button>
          </div>

          <div className="mt-4 text-center py-8 text-gray-400 text-sm">
            Nenhum comentário ainda. Seja o primeiro!
          </div>
        </div>
      </div>
    </div>
  );
}
