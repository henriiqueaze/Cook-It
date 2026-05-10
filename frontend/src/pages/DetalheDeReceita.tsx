import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  ArrowLeft,
  Check,
  Clock,
  Heart,
  Minus,
  PencilLine,
  Plus,
  Send,
  Trash2,
  Users,
  X,
} from "lucide-react";
import { toast } from "sonner";
import { RatingStars } from "@/components/AvaliacaoEstrelas";
import { useAuth } from "@/contexts/AuthContext";
import { useFavorites } from "@/contexts/FavoritesContext";
import { comentarioService } from "@/services/comentarioService";
import { receitaService } from "@/services/receitaService";
import type { Comentario, Receita } from "@/types";
import { UnidadeMedidaLabel } from "@/enums/UnidadeMedida";

function formatarData(data?: string) {
  if (!data) {
    return "";
  }

  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(data));
}

function obterLabelUnidade(unidade?: string) {
  if (!unidade) {
    return "";
  }

  return (
    UnidadeMedidaLabel[unidade as keyof typeof UnidadeMedidaLabel] ?? unidade
  );
}

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
  const [enviandoComentario, setEnviandoComentario] = useState(false);

  const favoritada = useMemo(() => {
    if (!receita) {
      return false;
    }

    return isFavorite(receita.id);
  }, [isFavorite, receita]);

  useEffect(() => {
    if (!id) {
      setReceita(null);
      setCarregandoReceita(false);
      return;
    }

    let ativo = true;
    setCarregandoReceita(true);

    receitaService
      .buscarPorId(id)
      .then((dados) => {
        if (ativo) {
          setReceita(dados);
          setAvaliacaoUsuario(dados.avaliacaoUsuario ?? 0);
        }
      })
      .catch(() => {
        if (ativo) {
          setReceita(null);
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

  useEffect(() => {
    if (!id) {
      setComentarios([]);
      setCarregandoComentarios(false);
      return;
    }

    let ativo = true;
    setCarregandoComentarios(true);

    comentarioService
      .listarPorReceita(id)
      .then((lista) => {
        if (ativo) {
          setComentarios(Array.isArray(lista) ? lista : []);
        }
      })
      .catch(() => {
        if (ativo) {
          setComentarios([]);
        }
      })
      .finally(() => {
        if (ativo) {
          setCarregandoComentarios(false);
        }
      });

    return () => {
      ativo = false;
    };
  }, [id]);

  useEffect(() => {
    if (!receita || avaliacaoUsuario > 0 || !id || !usuario?.id) {
      return;
    }

    const chave = `rating-recipe-${id}-user-${usuario.id}`;
    const salvo = localStorage.getItem(chave);

    if (!salvo) {
      return;
    }

    const nota = Number(salvo);

    if (Number.isNaN(nota) || nota < 1 || nota > 5) {
      localStorage.removeItem(chave);
      return;
    }

    setAvaliacaoUsuario(nota);
  }, [avaliacaoUsuario, id, receita, usuario?.id]);

  function handleVoltar() {
    if (window.history.length > 1) {
      navigate(-1);
      return;
    }

    navigate("/");
  }

  async function handleFavoritar() {
    if (!receita) {
      return;
    }

    if (!estaAutenticado) {
      toast.error("Faça login para favoritar receitas");
      navigate("/login");
      return;
    }

    try {
      const adicionou = await toggleFavorite(receita);
      toast.success(
        adicionou ? "Adicionado aos favoritos" : "Removido dos favoritos",
      );
    } catch {
      toast.error("Não foi possível atualizar os favoritos");
    }
  }

  async function handleAvaliar(nota: number) {
    if (!receita) {
      return;
    }

    if (!estaAutenticado) {
      toast.error("Faça login para avaliar receitas");
      navigate("/login");
      return;
    }

    try {
      await receitaService.avaliar(receita.id, nota);
      setAvaliacaoUsuario(nota);

      if (usuario?.id && id) {
        localStorage.setItem(
          `rating-recipe-${id}-user-${usuario.id}`,
          String(nota),
        );
      }

      const atualizada = await receitaService.buscarPorId(receita.id);
      setReceita(atualizada);
      toast.success("Avaliação enviada!");
    } catch {
      toast.error("Não foi possível enviar sua avaliação");
    }
  }

  async function handleComentario() {
    if (!receita) {
      return;
    }

    if (!estaAutenticado) {
      toast.error("Faça login para comentar");
      navigate("/login");
      return;
    }

    const texto = novoComentario.trim();

    if (!texto) {
      return;
    }

    try {
      setEnviandoComentario(true);
      const comentario = await comentarioService.adicionar(receita.id, texto);
      setComentarios((current) => [comentario, ...current]);
      setNovoComentario("");
      toast.success("Comentário adicionado!");
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : "Não foi possível adicionar o comentário",
      );
    } finally {
      setEnviandoComentario(false);
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
    const texto = textoEdicaoComentario.trim();

    if (!texto) {
      return;
    }

    try {
      const atualizado = await comentarioService.atualizar(comentarioId, {
        text: texto,
      });

      setComentarios((current) =>
        current.map((comentario) =>
          comentario.id === comentarioId ? atualizado : comentario,
        ),
      );

      cancelarEdicao();
      toast.success("Comentário atualizado!");
    } catch {
      toast.error("Não foi possível atualizar o comentário");
    }
  }

  async function excluirComentario(comentarioId: Comentario["id"]) {
    try {
      await comentarioService.deletar(comentarioId);
      setComentarios((current) =>
        current.filter((comentario) => comentario.id !== comentarioId),
      );

      if (comentarioEditandoId === comentarioId) {
        cancelarEdicao();
      }

      toast.success("Comentário excluído!");
    } catch {
      toast.error("Não foi possível excluir o comentário");
    }
  }

  async function handleDeletar() {
    if (!receita) {
      return;
    }

    try {
      await receitaService.deletar(receita.id);
      toast.success("Receita deletada!");
      navigate("/minhas-receitas");
    } catch {
      try {
        const listaComentarios = await comentarioService.listarPorReceita(
          receita.id,
        );

        await Promise.allSettled(
          listaComentarios.map((comentario) =>
            comentarioService.deletar(comentario.id),
          ),
        );

        await receitaService.deletar(receita.id);
        toast.success("Receita deletada!");
        navigate("/minhas-receitas");
      } catch {
        toast.error("Não foi possível deletar a receita");
      }
    }
  }

  if (carregandoReceita && !receita) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <p className="text-gray-500">Carregando receita...</p>
      </div>
    );
  }

  if (!receita) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
        <p className="text-gray-500">Receita não encontrada.</p>
        <button
          onClick={handleVoltar}
          className="mt-4 font-medium text-orange-600"
        >
          Voltar
        </button>
      </div>
    );
  }

  const ehMinhaReceita = usuario?.id === receita.autor.id;
  const porcoesBase = receita.porcoes || 1;
  const botaoComentarioDesabilitado =
    !novoComentario.trim() || enviandoComentario;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="relative h-64 w-full bg-gray-200">
        {receita.imagemUrl ? (
          <img
            src={receita.imagemUrl}
            alt={receita.titulo}
            className="h-full w-full object-cover"
          />
        ) : null}

        <button
          type="button"
          onClick={handleVoltar}
          className="absolute left-4 top-4 rounded-full bg-white/90 p-2 shadow-md"
          aria-label="Voltar"
        >
          <ArrowLeft size={20} className="text-gray-700" />
        </button>

        <button
          type="button"
          onClick={handleFavoritar}
          className="absolute right-4 top-4 rounded-full bg-white/90 p-2 shadow-md"
          aria-label={
            favoritada ? "Remover dos favoritos" : "Adicionar aos favoritos"
          }
        >
          <Heart
            size={20}
            className={
              favoritada ? "fill-red-500 text-red-500" : "text-gray-400"
            }
          />
        </button>

        {ehMinhaReceita && (
          <button
            type="button"
            onClick={() =>
              navigate(`/editar-receita/${receita.id}`, {
                state: { receita },
              })
            }
            className="absolute right-28 top-4 rounded-full bg-white/90 p-2 shadow-md"
            aria-label="Editar receita"
          >
            <PencilLine size={20} className="text-orange-500" />
          </button>
        )}

        {ehMinhaReceita && (
          <button
            type="button"
            onClick={handleDeletar}
            className="absolute right-16 top-4 rounded-full bg-white/90 p-2 shadow-md"
            aria-label="Excluir receita"
          >
            <Trash2 size={20} className="text-red-500" />
          </button>
        )}
      </div>

      <div className="space-y-4 px-6 py-4">
        <section>
          <h1 className="text-xl font-bold text-gray-800">{receita.titulo}</h1>
          <h2 className="mt-2 text-sm font-semibold text-gray-700">
            Descrição
          </h2>
          <p className="mt-1 text-sm text-gray-500">
            {receita.descricao?.trim() || "Sem descrição informada."}
          </p>
        </section>

        <section className="flex flex-wrap gap-4">
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
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="mb-3 font-semibold text-gray-800">Ajustar porções</h2>
          <div className="flex flex-wrap items-center gap-4">
            <button
              type="button"
              onClick={() =>
                setMultiplicador((valor) => Math.max(1, valor - 1))
              }
              className="flex h-8 w-8 items-center justify-center rounded-full bg-orange-100 text-orange-600 transition-colors hover:bg-orange-200"
              aria-label="Diminuir porções"
            >
              <Minus size={16} />
            </button>
            <span className="text-lg font-semibold text-gray-800">
              {multiplicador}x
            </span>
            <button
              type="button"
              onClick={() => setMultiplicador((valor) => valor + 1)}
              className="flex h-8 w-8 items-center justify-center rounded-full bg-orange-100 text-orange-600 transition-colors hover:bg-orange-200"
              aria-label="Aumentar porções"
            >
              <Plus size={16} />
            </button>
            <span className="text-sm text-gray-500">
              {porcoesBase * multiplicador} porções no total
            </span>
          </div>
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="mb-3 font-semibold text-gray-800">Ingredientes</h2>
          <ul className="space-y-2">
            {receita.ingredientes.map((ingrediente) => {
              const quantidade =
                Number(ingrediente.quantidade || 0) * multiplicador;
              const precisaDecimal =
                Number(ingrediente.quantidade || 0) % 1 !== 0;
              const quantidadeFormatada = quantidade.toFixed(
                precisaDecimal ? 1 : 0,
              );

              return (
                <li
                  key={ingrediente.id}
                  className="flex items-center justify-between gap-3 text-sm"
                >
                  <span className="text-gray-700">{ingrediente.nome}</span>
                  <span className="text-gray-400">
                    {quantidadeFormatada}{" "}
                    {obterLabelUnidade(ingrediente.unidade)}
                  </span>
                </li>
              );
            })}
          </ul>
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="mb-3 font-semibold text-gray-800">Modo de preparo</h2>
          <ol className="space-y-3">
            {receita.instrucoes.map((passo, index) => (
              <li key={`${passo}-${index}`} className="flex gap-3 text-sm">
                <span className="flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-orange-600 text-xs font-bold text-white">
                  {index + 1}
                </span>
                <span className="pt-0.5 text-gray-600">{passo}</span>
              </li>
            ))}
          </ol>
        </section>

        <section className="flex justify-center">
          <div className="w-full max-w-md rounded-2xl bg-white p-4 text-center shadow-sm">
            <h2 className="mb-3 font-semibold text-gray-800">
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
              <p className="mt-2 text-sm text-gray-500">
                Você avaliou com {avaliacaoUsuario} estrela
                {avaliacaoUsuario > 1 ? "s" : ""}
              </p>
            )}
          </div>
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="mb-3 font-semibold text-gray-800">Comentários</h2>

          <div className="flex gap-2">
            <input
              type="text"
              value={novoComentario}
              onChange={(event) => setNovoComentario(event.target.value)}
              placeholder="Deixe seu comentário..."
              className="flex-1 rounded-lg border border-gray-300 px-4 py-2 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
            />
            <button
              type="button"
              onClick={handleComentario}
              disabled={botaoComentarioDesabilitado}
              className="rounded-lg bg-orange-600 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
              aria-label="Enviar comentário"
            >
              <Send size={16} />
            </button>
          </div>

          <div className="mt-4 space-y-3">
            {carregandoComentarios ? (
              <div className="py-4 text-center text-sm text-gray-400">
                Carregando comentários...
              </div>
            ) : comentarios.length === 0 ? (
              <div className="py-8 text-center text-sm text-gray-400">
                Nenhum comentário ainda. Seja o primeiro!
              </div>
            ) : (
              comentarios.map((comentario) => {
                const podeGerenciar = usuario?.id === comentario.userId;
                const estaEditando = comentarioEditandoId === comentario.id;

                return (
                  <article
                    key={comentario.id}
                    className="rounded-xl border border-gray-100 p-3"
                  >
                    <div className="mb-2 flex items-start justify-between gap-3">
                      <div className="flex items-center gap-2">
                        {comentario.userPhoto ? (
                          <img
                            src={comentario.userPhoto}
                            alt={comentario.userName}
                            className="h-8 w-8 rounded-full object-cover"
                          />
                        ) : (
                          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-orange-100 text-xs font-semibold text-orange-700">
                            {comentario.userName?.charAt(0)?.toUpperCase() ||
                              "U"}
                          </div>
                        )}

                        <div>
                          <span className="block text-sm font-medium text-gray-700">
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
                            type="button"
                            onClick={() => iniciarEdicao(comentario)}
                            className="text-gray-400 transition-colors hover:text-orange-600"
                            aria-label="Editar comentário"
                          >
                            <PencilLine size={16} />
                          </button>
                          <button
                            type="button"
                            onClick={() => excluirComentario(comentario.id)}
                            className="text-gray-400 transition-colors hover:text-red-600"
                            aria-label="Excluir comentário"
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
                          onChange={(event) =>
                            setTextoEdicaoComentario(event.target.value)
                          }
                          className="min-h-[90px] w-full resize-none rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
                        />

                        <div className="flex justify-end gap-2">
                          <button
                            type="button"
                            onClick={cancelarEdicao}
                            className="inline-flex items-center gap-1 rounded-lg bg-gray-100 px-3 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-200"
                          >
                            <X size={16} />
                            Cancelar
                          </button>
                          <button
                            type="button"
                            onClick={() =>
                              salvarEdicaoComentario(comentario.id)
                            }
                            disabled={!textoEdicaoComentario.trim()}
                            className="inline-flex items-center gap-1 rounded-lg bg-orange-600 px-3 py-2 text-sm font-medium text-white transition-colors hover:bg-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
                          >
                            <Check size={16} />
                            Salvar
                          </button>
                        </div>
                      </div>
                    ) : (
                      <p className="whitespace-pre-wrap text-sm text-gray-600">
                        {comentario.text}
                      </p>
                    )}
                  </article>
                );
              })
            )}
          </div>
        </section>
      </div>
    </div>
  );
}
