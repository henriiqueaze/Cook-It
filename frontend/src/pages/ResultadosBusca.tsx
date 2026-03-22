import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Search, ChefHat, SlidersHorizontal } from "lucide-react";
import { ReceitaCard } from "@/components/ReceitaCard";
import { IngredientTag } from "@/components/IngredientTag";
import { ingredientesMock, receitasMock } from "@/mocks";
import type { Receita } from "@/types";

type OpcaoOrdenacao = "compatibilidade" | "tempo" | "avaliacao";

export function ResultadosBusca() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const ingredientesDaUrl = useMemo(() => {
    return searchParams.get("ingredientes")?.split(",").filter(Boolean) ?? [];
  }, [searchParams]);

  const [ingredientesSelecionados, setIngredientesSelecionados] =
    useState<string[]>(ingredientesDaUrl);
  const [ingredientesBuscados, setIngredientesBuscados] =
    useState<string[]>(ingredientesDaUrl);
  const [busca, setBusca] = useState("");
  const [mostrarSugestoes, setMostrarSugestoes] = useState(false);
  const [mostrarFiltros, setMostrarFiltros] = useState(false);
  const [ordenacao, setOrdenacao] = useState<OpcaoOrdenacao>("compatibilidade");

  useEffect(() => {
    setIngredientesSelecionados(ingredientesDaUrl);
    setIngredientesBuscados(ingredientesDaUrl);
  }, [ingredientesDaUrl]);

  const sugestoesFiltradas = useMemo(() => {
    if (!busca) return [];
    return ingredientesMock
      .filter(
        (ing) =>
          ing.nome.toLowerCase().includes(busca.toLowerCase()) &&
          !ingredientesSelecionados.includes(ing.nome),
      )
      .slice(0, 5);
  }, [busca, ingredientesSelecionados]);

  const resultados = useMemo(() => {
    if (ingredientesBuscados.length === 0) return [];

    const comCompatibilidade = receitasMock
      .map((receita) => {
        const matches = receita.ingredientes.filter((i) =>
          ingredientesBuscados.some((ingred) =>
            i.nome.toLowerCase().includes(ingred.toLowerCase()),
          ),
        ).length;
        const compatibilidade = Math.round(
          (matches / receita.ingredientes.length) * 100,
        );
        return { ...receita, compatibilidade };
      })
      .filter((r) => r.compatibilidade > 0);

    switch (ordenacao) {
      case "tempo":
        return [...comCompatibilidade].sort(
          (a, b) => a.tempoPreparo - b.tempoPreparo,
        );
      case "avaliacao":
        return [...comCompatibilidade].sort(
          (a, b) => b.avaliacao - a.avaliacao,
        );
      case "compatibilidade":
      default:
        return [...comCompatibilidade].sort(
          (a, b) => b.compatibilidade - a.compatibilidade,
        );
    }
  }, [ingredientesBuscados, ordenacao]);

  function adicionarIngrediente(nome: string) {
    if (!ingredientesSelecionados.includes(nome)) {
      setIngredientesSelecionados([...ingredientesSelecionados, nome]);
      setBusca("");
      setMostrarSugestoes(false);
    }
  }

  function removerIngrediente(nome: string) {
    setIngredientesSelecionados(
      ingredientesSelecionados.filter((i) => i !== nome),
    );
  }

  function handleBuscar() {
    const params = new URLSearchParams();
    if (ingredientesSelecionados.length > 0) {
      params.set("ingredientes", ingredientesSelecionados.join(","));
    }
    setIngredientesBuscados(ingredientesSelecionados);
    navigate(`/busca${params.toString() ? `?${params.toString()}` : ""}`);
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 text-white pt-12 pb-6 px-6 rounded-b-3xl shadow-lg">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-3">
            <ChefHat className="w-7 h-7" />
            <h1 className="text-xl font-bold">Resultados da Busca</h1>
          </div>
          <button
            onClick={() => setMostrarFiltros(!mostrarFiltros)}
            className="p-2 bg-white/20 rounded-lg hover:bg-white/30 transition-colors cursor-pointer"
          >
            <SlidersHorizontal size={20} />
          </button>
        </div>
        <p className="text-orange-100 text-sm">
          Adicione ingredientes e clique em buscar para ver as receitas
        </p>
      </div>

      {mostrarFiltros && (
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <p className="text-sm font-medium text-gray-700 mb-2">Ordenar por</p>
          <div className="flex gap-2">
            {[
              { valor: "compatibilidade", label: "Compatibilidade" },
              { valor: "tempo", label: "Tempo" },
              { valor: "avaliacao", label: "Avaliação" },
            ].map(({ valor, label }) => (
              <button
                key={valor}
                onClick={() => setOrdenacao(valor as OpcaoOrdenacao)}
                className={`px-3 py-1.5 rounded-full text-sm font-medium transition-colors cursor-pointer ${
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
          onSubmit={(e) => {
            e.preventDefault();
            handleBuscar();
          }}
          className="bg-white rounded-2xl shadow-lg p-6"
        >
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Quais ingredientes você tem?
          </label>

          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              value={busca}
              onChange={(e) => {
                setBusca(e.target.value);
                setMostrarSugestoes(true);
              }}
              onFocus={() => setMostrarSugestoes(true)}
              placeholder="Digite um ingrediente..."
              className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none"
            />

            {mostrarSugestoes && sugestoesFiltradas.length > 0 && (
              <div className="absolute z-10 w-full mt-2 bg-white border border-gray-200 rounded-lg shadow-lg max-h-48 overflow-y-auto">
                {sugestoesFiltradas.map((ing) => (
                  <button
                    key={ing.id}
                    type="button"
                    onClick={() => adicionarIngrediente(ing.nome)}
                    className="w-full px-4 py-2 text-left hover:bg-orange-50 transition-colors text-sm"
                  >
                    {ing.nome}
                  </button>
                ))}
              </div>
            )}
          </div>

          {ingredientesSelecionados.length > 0 && (
            <div className="mt-4">
              <p className="text-sm text-gray-600 mb-2">
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
            disabled={ingredientesSelecionados.length === 0}
            className="w-full mt-6 bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all"
          >
            Buscar Receitas
          </button>
        </form>

        <div className="mt-6">
          {ingredientesBuscados.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-20 text-center">
              <ChefHat size={48} className="text-gray-300 mb-4" />
              <h2 className="text-lg font-semibold text-gray-500">
                Adicione ingredientes e clique em buscar
              </h2>
              <p className="text-sm text-gray-400 mt-1">
                As receitas vão aparecer somente após a busca.
              </p>
            </div>
          ) : resultados.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-20 text-center">
              <ChefHat size={48} className="text-gray-300 mb-4" />
              <h2 className="text-lg font-semibold text-gray-500">
                Nenhuma receita encontrada!
              </h2>
              <p className="text-sm text-gray-400 mt-1">
                Adicione mais ingredientes ou tente buscar outros!
              </p>
            </div>
          ) : (
            <>
              <p className="text-sm text-gray-500 mb-4">
                {resultados.length} receita(s) encontrada(s) — ordenado por{" "}
                <span className="text-orange-600 font-medium">
                  {ordenacao === "compatibilidade"
                    ? "compatibilidade"
                    : ordenacao === "tempo"
                      ? "tempo de preparo"
                      : "avaliação"}
                </span>
              </p>
              <div className="grid grid-cols-2 gap-3">
                {resultados.map((receita) => (
                  <ReceitaCard
                    key={receita.id}
                    receita={receita as Receita}
                    compatibilidade={receita.compatibilidade}
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
