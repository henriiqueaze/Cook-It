import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  ArrowLeft,
  Clock,
  Users,
  Heart,
  Minus,
  Plus,
  Trash2,
  Send,
  PencilLine,
  X,
  Check,
} from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { useFavorites } from "../contexts/FavoritesContext";
import { RatingStars } from "../components/AvaliacaoEstrelas";
import { UnidadeMedidaLabel } from "../enums/UnidadeMedida";
import { receitaService } from "../services/receitaService";
import { comentarioService } from "@/services/comentarioService";
import type { Comentario, Receita } from "../types";
import { toast } from "sonner";

export function DetalheReceita() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { usuario, estaAutenticado } = useAuth();
  const { toggleFavorite, isFavorite } = useFavorites();

  const [receita, setReceita] = useState<Receita | null>(null);
  const [carregandoReceita, setCarregandoReceita] = useState(true);
  const [carregandoComentarios, setCarregandoComentarios] = useState(false);
  const [multiplicador, setMultiplicador] = useState(1);
  const [novoComentario, setNovoComentario] = useState("");
  const [avaliacaoUsuario, setAvaliacaoUsuario] = useState(0);
  const [comentarios, setComentarios] = useState<Comentario[]>([]);
  const [comentarioEditandoId, setComentarioEditandoId] = useState<
    Comentario["id"] | null
  >(null);
  const [textoEdicaoComentario, setTextoEdicaoComentario] = useState("");

  const chaveAvaliacao = useMemo(() => {
    if (!id || !usuario?.id) return null;
    return `rating-recipe-${id}-user-${usuario.id}`;
  }, [id, usuario?.id]);

  function handleVoltar() {
    const historico = window.history.state as { idx?: number } | null;

    if (typeof historico?.idx === "number" && historico.idx > 0) {
      navigate(-1);
      return;
    }

    navigate("/");
  }

  function formatarData(data?: string) {
    if (!data) return "";
    return new Intl.DateTimeFormat("pt-BR", {
      dateStyle: "short",
      timeStyle: "short",
    }).format(new Date(data));
  }

  useEffect(() => {
    if (!chaveAvaliacao) {
      setAvaliacaoUsuario(0);
      return;
    }

    const salvo = localStorage.getItem(chaveAvaliacao);
    if (!salvo) {
      setAvaliacaoUsuario(0);
      return;
    }

    const nota = Number(salvo);
    if (Number.isNaN(nota) || nota < 1 || nota > 5) {
      localStorage.removeItem(chaveAvaliacao);
      setAvaliacaoUsuario(0);
      return;
    }

    setAvaliacaoUsuario(nota);
  }, [chaveAvaliacao]);

  useEffect(() => {
    let ativo = true;

    if (!id) {
      setReceita(null);
      setCarregandoReceita(false);
      return;
    }

    setCarregandoReceita(true);
    receitaService
      .buscarPorId(id)
      .then((dados) => {
        if (ativo) setReceita(dados);
      })
      .catch(() => {
        if (ativo) setReceita(null);
      })
      .finally(() => {
        if (ativo) setCarregandoReceita(false);
      });

    return () => {
      ativo = false;
    };
  }, [id]);

  useEffect(() => {
    let ativo = true;

    if (!id) {
      setComentarios([]);
      return;
    }

    setCarregandoComentarios(true);
    comentarioService
      .listarPorReceita(id)
      .then((lista) => {
        if (ativo) setComentarios(Array.isArray(lista) ? lista : []);
      })
      .catch(() => {
        if (ativo) setComentarios([]);
      })
      .finally(() => {
        if (ativo) setCarregandoComentarios(false);
      });

    return () => {
      ativo = false;
    };
  }, [id]);

  const favoritada = useMemo(() => {
    if (!receita) return false;
    return isFavorite(receita.id);
  }, [receita, isFavorite]);

  function obterLabelUnidade(unidade?: string) {
    if (!unidade) return "";
    return (UnidadeMedidaLabel as Record<string, string>)[unidade] ?? unidade;
  }

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
          onClick={handleVoltar}
          className="mt-4 text-orange-600 font-medium"
        >
          Voltar
        </button>
      </div>
    );
  }

  const ehMinhaReceita = usuario?.id === receita.autor.id;
  const porcoesBase = receita.porcoes ?? 1;

  async function handleFavoritar() {
    if (!receita) return;

    if (!estaAutenticado) {
      toast.error("Faça login para favoritar receitas");
      navigate("/login");
      return;
    }

    try {
      const adicionou = await toggleFavorite(receita);
      toast.success(
        adicionou ? "Adicionado aos favoritos!" : "Removido dos favoritos",
      );
    } catch {
      toast.error("Não foi possível atualizar os favoritos");
    }
  }

  async function handleAvaliar(nota: number) {
    if (!receita) return;

    if (!estaAutenticado) {
      toast.error("Faça login para avaliar receitas");
      navigate("/login");
      return;
    }

    try {
      await receitaService.avaliar(receita.id, nota);
      setAvaliacaoUsuario(nota);

      if (chaveAvaliacao) {
        localStorage.setItem(chaveAvaliacao, String(nota));
      }

      const atualizada = await receitaService.buscarPorId(receita.id);
      setReceita(atualizada);

      toast.success("Avaliação enviada!");
    } catch (error) {
      console.error(error);
      toast.error("Não foi possível enviar sua avaliação");
    }
  }

  async function handleComentario() {
    if (!receita) return;

    if (!estaAutenticado) {
      toast.error("Faça login para comentar");
      navigate("/login");
      return;
    }

    if (!novoComentario.trim()) return;

    try {
      const comentario = await comentarioService.adicionar(
        receita.id,
        novoComentario.trim(),
      );

      setComentarios((atual) => [comentario, ...atual]);
      toast.success("Comentário adicionado!");
      setNovoComentario("");
    } catch {
      toast.error("Não foi possível adicionar o comentário");
    }
  }

  function iniciarEdicao(comentario: Comentario) {
    setComentarioEditandoId(comentario.id);
    setTextoEdicaoComentario(comentario.text);
  }

  function cancelarEdicao() {
    setComentarioEditandoId(null);
    setTextoEdicaoComentario("");
  }

  async function salvarEdicaoComentario(comentarioId: Comentario["id"]) {
    if (!textoEdicaoComentario.trim()) return;

    try {
      const atualizado = await comentarioService.atualizar(comentarioId, {
        text: textoEdicaoComentario.trim(),
      });

      setComentarios((atual) =>
        atual.map((comentario) =>
          comentario.id === comentarioId ? atualizado : comentario,
        ),
      );

      toast.success("Comentário atualizado!");
      cancelarEdicao();
    } catch {
      toast.error("Não foi possível atualizar o comentário");
    }
  }

  async function excluirComentario(comentarioId: Comentario["id"]) {
    try {
      await comentarioService.deletar(comentarioId);
      setComentarios((atual) =>
        atual.filter((comentario) => comentario.id !== comentarioId),
      );
      toast.success("Comentário excluído!");
      if (comentarioEditandoId === comentarioId) {
        cancelarEdicao();
      }
    } catch {
      toast.error("Não foi possível excluir o comentário");
    }
  }

  async function handleDeletar() {
    if (!receita) return;

    try {
      await receitaService.deletar(receita.id);
      toast.success("Receita deletada!");
      navigate("/minhas-receitas");
    } catch (erroInicial) {
      const mensagemInicial =
        erroInicial instanceof Error ? erroInicial.message : "";

      const erroServidor = mensagemInicial.includes("500");
      if (!erroServidor) {
        toast.error("Não foi possível deletar a receita");
        return;
      }

      try {
        const listaComentarios = await comentarioService.listarPorReceita(
          receita.id,
        );

        if (Array.isArray(listaComentarios) && listaComentarios.length > 0) {
          await Promise.allSettled(
            listaComentarios.map((comentario) =>
              comentarioService.deletar(comentario.id),
            ),
          );
        }

        await receitaService.deletar(receita.id);
        toast.success("Receita deletada!");
        navigate("/minhas-receitas");
      } catch {
        toast.error(
          "Não foi possível deletar a receita. Remova avaliações/comentários e tente novamente.",
        );
      }
    }
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
          onClick={handleVoltar}
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

        <div className="flex gap-4 flex-wrap">
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Clock size={16} className="text-orange-600" />
            {receita.tempoPreparo} min
          </div>
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Users size={16} className="text-orange-600" />
            {porcoesBase * multiplicador} porções
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
          <div className="flex items-center gap-4 flex-wrap">
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
              {porcoesBase * multiplicador} porções no total
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
                  {obterLabelUnidade(ing.unidade)}
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

        <div className="flex justify-center">
          <div className="bg-white rounded-2xl p-4 shadow-sm text-center max-w-md w-full">
            <h2 className="font-semibold text-gray-800 mb-3">
              Avalie esta receita
            </h2>

            <div className="flex justify-center">
              <RatingStars
                avaliacao={avaliacaoUsuario}
                onChange={handleAvaliar}
                tamanho="lg"
              />
            </div>

            {avaliacaoUsuario > 0 && (
              <p className="text-sm text-gray-500 mt-2">
                Você avaliou com {avaliacaoUsuario} estrela
                {avaliacaoUsuario > 1 ? "s" : ""}
              </p>
            )}
          </div>
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
              <Send size={16} />
            </button>
          </div>

          <div className="mt-4 space-y-3">
            {carregandoComentarios ? (
              <div className="text-sm text-gray-400 py-4 text-center">
                Carregando comentários...
              </div>
            ) : comentarios.length === 0 ? (
              <div className="text-center py-8 text-gray-400 text-sm">
                Nenhum comentário ainda. Seja o primeiro!
              </div>
            ) : (
              comentarios.map((comentario) => {
                const podeGerenciar = usuario?.id === comentario.userId;
                const estaEditando = comentarioEditandoId === comentario.id;

                return (
                  <div
                    key={comentario.id}
                    className="rounded-xl border border-gray-100 p-3"
                  >
                    <div className="flex items-start justify-between gap-3 mb-2">
                      <div className="flex items-center gap-2">
                        {comentario.userPhoto ? (
                          <img
                            src={comentario.userPhoto}
                            alt={comentario.userName}
                            className="w-8 h-8 rounded-full object-cover"
                          />
                        ) : (
                          <div className="w-8 h-8 rounded-full bg-orange-100 text-orange-700 flex items-center justify-center text-xs font-semibold">
                            {comentario.userName?.charAt(0)?.toUpperCase() || "U"}
                          </div>
                        )}

                        <div>
                          <span className="font-medium text-sm text-gray-700 block">
                            {comentario.userName}
                          </span>
                          <span className="text-xs text-gray-400">
                            {formatarData(comentario.createdAt)}
                          </span>
                        </div>
                      </div>

                      {podeGerenciar && !estaEditando && (
                        <div className="flex items-center gap-2">
                          <button
                            onClick={() => iniciarEdicao(comentario)}
                            className="text-gray-400 hover:text-orange-600 transition-colors"
                            title="Editar comentário"
                          >
                            <PencilLine size={16} />
                          </button>
                          <button
                            onClick={() => excluirComentario(comentario.id)}
                            className="text-gray-400 hover:text-red-600 transition-colors"
                            title="Excluir comentário"
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      )}
                    </div>

                    {estaEditando ? (
                      <div className="space-y-2">
                        <textarea
                          value={textoEdicaoComentario}
                          onChange={(e) => setTextoEdicaoComentario(e.target.value)}
                          className="w-full min-h-[90px] px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm resize-none"
                        />

                        <div className="flex justify-end gap-2">
                          <button
                            onClick={cancelarEdicao}
                            className="inline-flex items-center gap-1 px-3 py-2 rounded-lg text-sm font-medium bg-gray-100 text-gray-700 hover:bg-gray-200 transition-colors"
                          >
                            <X size={16} />
                            Cancelar
                          </button>
                          <button
                            onClick={() => salvarEdicaoComentario(comentario.id)}
                            disabled={!textoEdicaoComentario.trim()}
                            className="inline-flex items-center gap-1 px-3 py-2 rounded-lg text-sm font-medium bg-orange-600 text-white hover:bg-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                          >
                            <Check size={16} />
                            Salvar
                          </button>
                        </div>
                      </div>
                    ) : (
                      <p className="text-sm text-gray-600 whitespace-pre-wrap">
                        {comentario.text}
                      </p>
                    )}
                  </div>
                );
              })
            )}
          </div>
        </div>
      </div>
    </div>
  );
}