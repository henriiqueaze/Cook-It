import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { ChefHat, Search, SlidersHorizontal } from "lucide-react";
import { IngredientTag } from "@/components/IngredientTag";
import { ReceitaCard } from "@/components/ReceitaCard";
import {
  buscarCorrecaoPortugues,
  type SugestaoCorrecao,
} from "@/services/correcaoPortuguesService";
import { ingredienteService } from "@/services/ingredienteService";
import { receitaService } from "@/services/receitaService";
import type { Ingrediente, Receita } from "@/types";
import {
  encontrarOpcaoDeReceitaRelacionada,
  normalizarSugestaoReceita,
} from "@/utils/sugestoesReceita";

type OpcaoOrdenacao = "compatibilidade" | "tempo" | "avaliacao";
type ModoBusca = "ingredientes" | "receita";

interface SugestaoBusca {
  sugestao: string;
  tipo: "autocomplete" | "correcao";
}

function normalizarTexto(valor: string) {
  return normalizarSugestaoReceita(valor);
}

function capitalizarPrimeiraLetra(valor: string) {
  return valor
    .trim()
    .replace(/\s+/g, " ")
    .replace(/^./, (letra) => letra.toUpperCase());
}

function aplicarCorrecao(texto: string, correcao: SugestaoCorrecao) {
  return `${texto.slice(0, correcao.inicio)}${correcao.sugestao}${texto.slice(
    correcao.fim,
  )}`;
}

function calcularCompatibilidade(
  receita: Receita,
  ingredientesBuscados: string[],
) {
  if (!ingredientesBuscados.length || !receita.ingredientes.length) {
    return 0;
  }

  const ingredientesDaReceita = receita.ingredientes.map((item) =>
    item.nome.toLowerCase(),
  );

  const acertos = ingredientesBuscados.filter((ingrediente) =>
    ingredientesDaReceita.some((nome) =>
      nome.includes(ingrediente.toLowerCase()),
    ),
  ).length;

  return Math.round((acertos / ingredientesDaReceita.length) * 100);
}

export function ResultadosBusca() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const ingredientesDaUrl = useMemo(
    () =>
      searchParams
        .get("ingredientes")
        ?.split(",")
        .map((item) => item.trim())
        .filter(Boolean) ?? [],
    [searchParams],
  );
  const receitaDaUrl = useMemo(
    () => searchParams.get("receita")?.trim() ?? "",
    [searchParams],
  );

  const [busca, setBusca] = useState("");
  const [nomeReceita, setNomeReceita] = useState(receitaDaUrl);
  const [modoBusca, setModoBusca] = useState<ModoBusca>(
    receitaDaUrl ? "receita" : "ingredientes",
  );
  const [modoResultado, setModoResultado] = useState<ModoBusca>(
    receitaDaUrl ? "receita" : "ingredientes",
  );
  const [mostrarSugestoes, setMostrarSugestoes] = useState(false);
  const [mostrarFiltros, setMostrarFiltros] = useState(false);
  const [ordenacao, setOrdenacao] = useState<OpcaoOrdenacao>("compatibilidade");
  const [ingredientesDisponiveis, setIngredientesDisponiveis] = useState<
    Ingrediente[]
  >([]);
  const [nomesIngredientes, setNomesIngredientes] = useState<string[]>([]);
  const [nomesReceitas, setNomesReceitas] = useState<string[]>([]);
  const [carregandoIngredientes, setCarregandoIngredientes] = useState(false);
  const [ingredientesSelecionados, setIngredientesSelecionados] =
    useState<string[]>(ingredientesDaUrl);
  const [ingredientesBuscados, setIngredientesBuscados] =
    useState<string[]>(ingredientesDaUrl);
  const [receitaBuscada, setReceitaBuscada] = useState(receitaDaUrl);
  const [resultados, setResultados] = useState<Receita[]>([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");
  const [sugestaoIngrediente, setSugestaoIngrediente] =
    useState<SugestaoBusca | null>(null);
  const [sugestaoReceita, setSugestaoReceita] =
    useState<SugestaoBusca | null>(null);

  const ingredientesNormalizados = useMemo(
    () =>
      nomesIngredientes.map((nome) => ({
        nome,
        normalizado: normalizarTexto(nome),
      })),
    [nomesIngredientes],
  );

  const receitasNormalizadas = useMemo(
    () =>
      nomesReceitas.map((nome) => ({
        nome,
        normalizado: normalizarTexto(nome),
      })),
    [nomesReceitas],
  );

  useEffect(() => {
    setIngredientesSelecionados(ingredientesDaUrl);
    setIngredientesBuscados(ingredientesDaUrl);
    setNomeReceita(receitaDaUrl);
    setReceitaBuscada(receitaDaUrl);
    setModoBusca(receitaDaUrl ? "receita" : "ingredientes");
    setModoResultado(receitaDaUrl ? "receita" : "ingredientes");
  }, [ingredientesDaUrl, receitaDaUrl]);

  useEffect(() => {
    ingredienteService
      .listar()
      .then((itens) =>
        setNomesIngredientes(
          itens.map((item) => capitalizarPrimeiraLetra(item.nome)),
        ),
      )
      .catch(() => setNomesIngredientes([]));

    receitaService
      .listar()
      .then((itens) => setNomesReceitas(itens.map((item) => item.titulo)))
      .catch(() => setNomesReceitas([]));
  }, []);

  useEffect(() => {
    const termo = busca.trim();

    if (!termo) {
      setIngredientesDisponiveis([]);
      setCarregandoIngredientes(false);
      return;
    }

    setCarregandoIngredientes(true);

    const timer = window.setTimeout(() => {
      ingredienteService
        .buscar(termo)
        .then(setIngredientesDisponiveis)
        .catch(() => setIngredientesDisponiveis([]))
        .finally(() => setCarregandoIngredientes(false));
    }, 250);

    return () => window.clearTimeout(timer);
  }, [busca]);

  useEffect(() => {
    if (ingredientesDaUrl.length === 0 && !receitaDaUrl) {
      setResultados([]);
      setErro("");
      setCarregando(false);
      return;
    }

    void buscarReceitas({
      ingredientes: ingredientesDaUrl,
      nomeReceita: receitaDaUrl,
    });
  }, [ingredientesDaUrl, receitaDaUrl]);

  const sugestoesFiltradas = useMemo(() => {
    const termo = busca.trim().toLowerCase();

    if (!termo) {
      return [];
    }

    return ingredientesDisponiveis
      .filter(
        (ingrediente) =>
          ingrediente.nome.toLowerCase().includes(termo) &&
          !ingredientesSelecionados.includes(ingrediente.nome),
      )
      .slice(0, 5);
  }, [busca, ingredientesDisponiveis, ingredientesSelecionados]);

  useEffect(() => {
    const termoDigitado = busca.trim();

    if (termoDigitado.length < 3) {
      setSugestaoIngrediente(null);
      return;
    }

    const termo = normalizarTexto(termoDigitado);
    const sugestaoAutocomplete = ingredientesNormalizados.find(
      (item) =>
        item.normalizado.startsWith(termo) &&
        item.normalizado !== termo &&
        !ingredientesSelecionados.includes(item.nome),
    );

    if (sugestaoAutocomplete) {
      setSugestaoIngrediente({
        sugestao: sugestaoAutocomplete.nome,
        tipo: "autocomplete",
      });
      return;
    }

    let ativo = true;

    const timeout = window.setTimeout(() => {
      buscarCorrecaoPortugues(termoDigitado)
        .then((correcao) => {
          if (!ativo) {
            return;
          }

          setSugestaoIngrediente(
            correcao
              ? (() => {
                  const sugestaoRelacionada =
                    encontrarOpcaoDeReceitaRelacionada(
                      correcao.sugestao,
                      nomesIngredientes,
                    );

                  return sugestaoRelacionada
                    ? {
                        sugestao: capitalizarPrimeiraLetra(
                          sugestaoRelacionada,
                        ),
                        tipo: "correcao" as const,
                      }
                    : null;
                })()
              : null,
          );
        })
        .catch(() => {
          if (ativo) {
            setSugestaoIngrediente(null);
          }
        });
    }, 500);

    return () => {
      ativo = false;
      window.clearTimeout(timeout);
    };
  }, [
    busca,
    ingredientesNormalizados,
    ingredientesSelecionados,
    nomesIngredientes,
  ]);

  useEffect(() => {
    const termoDigitado = nomeReceita.trim();

    if (termoDigitado.length < 3) {
      setSugestaoReceita(null);
      return;
    }

    const termo = normalizarTexto(termoDigitado);
    const sugestaoAutocomplete = receitasNormalizadas.find(
      (item) => item.normalizado.startsWith(termo) && item.normalizado !== termo,
    );

    if (sugestaoAutocomplete) {
      setSugestaoReceita({
        sugestao: sugestaoAutocomplete.nome,
        tipo: "autocomplete",
      });
      return;
    }

    let ativo = true;

    const timeout = window.setTimeout(() => {
      buscarCorrecaoPortugues(termoDigitado)
        .then((correcao) => {
          if (!ativo) {
            return;
          }

          setSugestaoReceita(
            correcao
              ? (() => {
                  const sugestaoCorrigida = aplicarCorrecao(
                    termoDigitado,
                    correcao,
                  );
                  const sugestaoRelacionada =
                    encontrarOpcaoDeReceitaRelacionada(
                      sugestaoCorrigida,
                      nomesReceitas,
                    );

                  return sugestaoRelacionada
                    ? {
                        sugestao: sugestaoRelacionada,
                        tipo: "correcao" as const,
                      }
                    : null;
                })()
              : null,
          );
        })
        .catch(() => {
          if (ativo) {
            setSugestaoReceita(null);
          }
        });
    }, 500);

    return () => {
      ativo = false;
      window.clearTimeout(timeout);
    };
  }, [nomeReceita, nomesReceitas, receitasNormalizadas]);

  const resultadosOrdenados = useMemo(() => {
    const receitasComCompatibilidade = resultados
      .map((receita) => ({
        ...receita,
        compatibilidade: calcularCompatibilidade(receita, ingredientesBuscados),
      }))
      .filter(
        (receita) =>
          modoResultado === "receita" ||
          ingredientesBuscados.length === 0 ||
          receita.compatibilidade > 0,
      );

    const ordenados = [...receitasComCompatibilidade];

    if (ordenacao === "tempo") {
      ordenados.sort((a, b) => a.tempoPreparo - b.tempoPreparo);
    } else if (ordenacao === "avaliacao") {
      ordenados.sort((a, b) => b.avaliacao - a.avaliacao);
    } else {
      ordenados.sort((a, b) => b.compatibilidade - a.compatibilidade);
    }

    return ordenados;
  }, [ingredientesBuscados, modoResultado, ordenacao, resultados]);

  const travarRolagem =
    !carregando &&
    resultadosOrdenados.length === 0 &&
    ingredientesSelecionados.length === 0 &&
    !nomeReceita.trim();

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

  async function buscarReceitas({
    ingredientes,
    nomeReceita,
  }: {
    ingredientes: string[];
    nomeReceita?: string;
  }) {
    if (!ingredientes.length && !nomeReceita?.trim()) {
      setResultados([]);
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      const retorno = await receitaService.buscar({
        ingredientes,
        nomeReceita,
      });
      setResultados(retorno);
    } catch {
      setResultados([]);
      setErro("Não foi possível buscar as receitas agora.");
    } finally {
      setCarregando(false);
    }
  }

  function adicionarIngrediente(nome: string) {
    if (ingredientesSelecionados.includes(nome)) {
      return;
    }

    setIngredientesSelecionados((current) => [...current, nome]);
    setBusca("");
    setMostrarSugestoes(false);
    setSugestaoIngrediente(null);
  }

  function removerIngrediente(nome: string) {
    setIngredientesSelecionados((current) =>
      current.filter((item) => item !== nome),
    );
  }

  function aplicarSugestaoIngrediente() {
    if (!sugestaoIngrediente) {
      return;
    }

    if (sugestaoIngrediente.tipo === "autocomplete") {
      adicionarIngrediente(sugestaoIngrediente.sugestao);
      return;
    }

    setBusca(sugestaoIngrediente.sugestao);
    setMostrarSugestoes(true);
    setSugestaoIngrediente(null);
  }

  function aplicarSugestaoReceita() {
    if (!sugestaoReceita) {
      return;
    }

    setNomeReceita(sugestaoReceita.sugestao);
    setSugestaoReceita(null);
  }

  function handleBuscar() {
    const params = new URLSearchParams();
    const nomeReceitaNormalizado = nomeReceita.trim();

    if (modoBusca === "ingredientes" && ingredientesSelecionados.length > 0) {
      params.set("ingredientes", ingredientesSelecionados.join(","));
    }

    if (modoBusca === "receita" && nomeReceitaNormalizado) {
      params.set("receita", nomeReceitaNormalizado);
    }

    const proximosIngredientes =
      modoBusca === "ingredientes" ? ingredientesSelecionados : [];
    const proximaReceita =
      modoBusca === "receita" ? nomeReceitaNormalizado : "";

    setIngredientesBuscados(proximosIngredientes);
    setReceitaBuscada(proximaReceita);
    setModoResultado(modoBusca);
    navigate(`/busca${params.toString() ? `?${params.toString()}` : ""}`);
    void buscarReceitas({
      ingredientes: proximosIngredientes,
      nomeReceita: proximaReceita,
    });
  }

  const ingredienteNaoEncontrado =
    modoBusca === "ingredientes" &&
    mostrarSugestoes &&
    !carregandoIngredientes &&
    busca.trim().length > 0 &&
    !sugestaoIngrediente &&
    sugestoesFiltradas.length === 0;
  const temBuscaAtiva =
    ingredientesBuscados.length > 0 || receitaBuscada.trim().length > 0;
  const buscaPorReceita = modoResultado === "receita";
  const buscaVazia =
    modoBusca === "ingredientes"
      ? ingredientesSelecionados.length === 0
      : nomeReceita.trim().length === 0;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-6 pt-9 text-white shadow-lg">
        <div className="mb-2 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <ChefHat className="h-7 w-7" />
            <h1 className="text-xl font-bold">Resultados da busca</h1>
          </div>
          <button
            type="button"
            onClick={() => setMostrarFiltros((valor) => !valor)}
            className="rounded-lg bg-white/20 p-2 transition-colors hover:bg-white/30"
            aria-label="Mostrar filtros"
          >
            <SlidersHorizontal size={20} />
          </button>
        </div>
        <p className="text-sm text-orange-100">
          Busque pelo que você tem em casa ou pelo nome da receita
        </p>
      </div>

      {mostrarFiltros && (
        <div className="border-b border-gray-200 bg-white px-6 py-4">
          <p className="mb-2 text-sm font-medium text-gray-700">Ordenar por</p>
          <div className="flex flex-wrap gap-2">
            {[
              { valor: "compatibilidade", label: "Compatibilidade" },
              { valor: "tempo", label: "Tempo" },
              { valor: "avaliacao", label: "Avaliação" },
            ].map(({ valor, label }) => (
              <button
                key={valor}
                type="button"
                onClick={() => setOrdenacao(valor as OpcaoOrdenacao)}
                className={`rounded-full px-3 py-1.5 text-sm font-medium transition-colors ${
                  ordenacao === valor
                    ? "bg-orange-600 text-white"
                    : "bg-gray-100 text-gray-600 hover:bg-gray-200"
                }`}
              >
                {label}
              </button>
            ))}
          </div>
        </div>
      )}

      <div className="px-6 -mt-4">
        <form
          onSubmit={(event) => {
            event.preventDefault();
            handleBuscar();
          }}
          className="rounded-2xl bg-white p-6 shadow-lg"
        >
          <div className="mb-5 grid grid-cols-2 gap-2 rounded-lg bg-gray-100 p-1">
            <button
              type="button"
              onClick={() => {
                setModoBusca("ingredientes");
                setSugestaoReceita(null);
              }}
              className={`rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                modoBusca === "ingredientes"
                  ? "bg-white text-orange-600 shadow-sm"
                  : "text-gray-500 hover:text-gray-700"
              }`}
            >
              Ingredientes
            </button>
            <button
              type="button"
              onClick={() => {
                setModoBusca("receita");
                setSugestaoIngrediente(null);
                setMostrarSugestoes(false);
              }}
              className={`rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                modoBusca === "receita"
                  ? "bg-white text-orange-600 shadow-sm"
                  : "text-gray-500 hover:text-gray-700"
              }`}
            >
              Receita
            </button>
          </div>

          <label className="mb-2 block text-sm font-medium text-gray-700">
            {modoBusca === "ingredientes"
              ? "Quais ingredientes você tem?"
              : "Qual receita você quer encontrar?"}
          </label>

          {modoBusca === "ingredientes" ? (
            <div>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  value={busca}
                  onChange={(event) => {
                    setBusca(event.target.value);
                    setSugestaoIngrediente(null);
                    setMostrarSugestoes(true);
                  }}
                  onFocus={() => setMostrarSugestoes(true)}
                  onBlur={() =>
                    window.setTimeout(() => setMostrarSugestoes(false), 150)
                  }
                  placeholder="Digite um ingrediente..."
                  className="w-full rounded-lg border border-gray-300 py-3 pl-10 pr-4 outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
                />

                {mostrarSugestoes && sugestoesFiltradas.length > 0 && (
                  <div className="absolute z-10 mt-2 max-h-48 w-full overflow-y-auto rounded-lg border border-gray-200 bg-white shadow-lg">
                    {sugestoesFiltradas.map((ingrediente) => (
                      <button
                        key={ingrediente.id}
                        type="button"
                        onMouseDown={(event) => event.preventDefault()}
                        onClick={() => adicionarIngrediente(ingrediente.nome)}
                        className="w-full px-4 py-2 text-left text-sm transition-colors hover:bg-orange-50"
                      >
                        {ingrediente.nome}
                      </button>
                    ))}
                  </div>
                )}
              </div>

              {sugestaoIngrediente && (
                <button
                  type="button"
                  onClick={aplicarSugestaoIngrediente}
                  className="mt-2 text-left text-xs font-medium text-orange-600"
                >
                  {sugestaoIngrediente.tipo === "autocomplete"
                    ? `Sugestão: ${sugestaoIngrediente.sugestao}`
                    : `Você quis dizer: ${sugestaoIngrediente.sugestao}?`}
                </button>
              )}

              {ingredienteNaoEncontrado && (
                <p className="mt-2 text-sm text-red-600">
                  Ingrediente não encontrado
                </p>
              )}
            </div>
          ) : (
            <div>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  value={nomeReceita}
                  onChange={(event) => {
                    setNomeReceita(event.target.value);
                    setSugestaoReceita(null);
                  }}
                  placeholder="Digite o nome da receita..."
                  className="w-full rounded-lg border border-gray-300 py-3 pl-10 pr-4 outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
                />
              </div>
              {sugestaoReceita && (
                <button
                  type="button"
                  onClick={aplicarSugestaoReceita}
                  className="mt-2 text-left text-xs font-medium text-orange-600"
                >
                  {sugestaoReceita.tipo === "autocomplete"
                    ? `Sugestão: ${sugestaoReceita.sugestao}`
                    : `Você quis dizer: ${sugestaoReceita.sugestao}?`}
                </button>
              )}
            </div>
          )}

          {modoBusca === "ingredientes" && ingredientesSelecionados.length > 0 && (
            <div className="mt-4">
              <p className="mb-2 text-sm text-gray-600">
                Ingredientes selecionados:
              </p>
              <div className="flex flex-wrap gap-2">
                {ingredientesSelecionados.map((nome) => (
                  <IngredientTag
                    key={nome}
                    nome={nome}
                    onRemover={() => removerIngrediente(nome)}
                  />
                ))}
              </div>
            </div>
          )}

          <button
            type="submit"
            disabled={buscaVazia || carregando}
            className="mt-6 w-full rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
          >
            {carregando ? "Buscando..." : "Buscar receitas"}
          </button>
        </form>

        <div className={`mt-6 ${resultadosOrdenados.length > 0 ? "pb-4" : ""}`}>
          {erro && (
            <div className="mb-4 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          {!temBuscaAtiva ? (
            <div className="flex flex-col items-center justify-center pt-8 pb-24 text-center">
              <ChefHat size={48} className="mb-4 text-gray-300" />
              <h2 className="text-lg font-semibold text-gray-500">
                Escolha uma busca e clique em buscar
              </h2>
              <p className="mt-1 text-sm text-gray-400">
                As receitas vão aparecer somente após a busca.
              </p>
            </div>
          ) : resultadosOrdenados.length === 0 && !carregando ? (
            <div className="flex flex-col items-center justify-center py-20 text-center">
              <ChefHat size={48} className="mb-4 text-gray-300" />
              <h2 className="text-lg font-semibold text-gray-500">
                {buscaPorReceita
                  ? "Receita não encontrada"
                  : "Nenhuma receita encontrada"}
              </h2>
              <p className="mt-1 text-sm text-gray-400">
                {buscaPorReceita
                  ? "Confira o nome digitado ou tente outra receita."
                  : "Adicione mais ingredientes ou tente buscar outros."}
              </p>
            </div>
          ) : (
            <>
              <p className="mb-4 text-sm text-gray-500">
                {resultadosOrdenados.length} receita
                {resultadosOrdenados.length !== 1 ? "s" : ""} encontrada
                {resultadosOrdenados.length !== 1 ? "s" : ""}
                {!buscaPorReceita && (
                  <>
                    {" "}
                    — ordenado por{" "}
                    <span className="font-medium text-orange-600">
                      {ordenacao === "compatibilidade"
                        ? "compatibilidade"
                        : ordenacao === "tempo"
                          ? "tempo de preparo"
                          : "avaliação"}
                    </span>
                  </>
                )}
              </p>

              <div className="grid grid-cols-2 gap-3">
                {resultadosOrdenados.map((receita) => (
                  <ReceitaCard
                    key={receita.id}
                    receita={receita}
                    compatibilidade={
                      buscaPorReceita ? undefined : receita.compatibilidade
                    }
                  />
                ))}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
