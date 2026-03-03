import { useState, useMemo } from "react";
import { Search, ChefHat } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { ingredientesMock } from "../mocks";
import { IngredientTag } from "@/components/IngredientTag";

export function Home() {
  const navigate = useNavigate();
  const [ingredientesSelecionados, setIngredientesSelecionados] = useState<
    string[]
  >([]);
  const [busca, setBusca] = useState("");
  const [mostrarSugestoes, setMostrarSugestoes] = useState(false);

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
    if (ingredientesSelecionados.length > 0) {
      navigate(`/busca?ingredientes=${ingredientesSelecionados.join(",")}`);
    }
  }

  return (
    <div className="min-h-screen bg-linear-to-b from-green-50 to-white pb-20">
      <div className="bg-green-600 text-white pt-12 pb-8 px-6 rounded-b-3xl">
        <div className="flex items-center gap-3 mb-2">
          <ChefHat className="w-8 h-8" />
          <h1 className="text-2xl font-bold">Receitas Inteligentes</h1>
        </div>
        <p className="text-green-100 text-sm">
          Encontre receitas com os ingredientes que você tem
        </p>
      </div>

      <div className="px-6 -mt-4">
        <div className="bg-white rounded-2xl shadow-lg p-6">
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
              className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
            />

            {mostrarSugestoes && sugestoesFiltradas.length > 0 && (
              <div className="absolute z-10 w-full mt-2 bg-white border border-gray-200 rounded-lg shadow-lg max-h-48 overflow-y-auto">
                {sugestoesFiltradas.map((ing) => (
                  <button
                    key={ing.id}
                    onClick={() => adicionarIngrediente(ing.nome)}
                    className="w-full px-4 py-2 text-left hover:bg-green-50 transition-colors text-sm"
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
            onClick={handleBuscar}
            disabled={ingredientesSelecionados.length === 0}
            className="w-full mt-6 bg-green-600 text-white py-3 rounded-lg font-medium hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
          >
            Buscar Receitas
          </button>
        </div>

        <div className="mt-8 grid grid-cols-2 gap-4">
          <div className="bg-white rounded-xl p-4 text-center shadow-sm">
            <div className="text-3xl font-bold text-green-600 mb-1">500+</div>
            <div className="text-sm text-gray-600">Receitas</div>
          </div>
          <div className="bg-white rounded-xl p-4 text-center shadow-sm">
            <div className="text-3xl font-bold text-green-600 mb-1">1000+</div>
            <div className="text-sm text-gray-600">Usuários</div>
          </div>
        </div>

        <div className="mt-8 space-y-4 mb-8">
          <h2 className="font-semibold text-lg">Como funciona</h2>

          {[
            {
              n: 1,
              titulo: "Adicione ingredientes",
              desc: "Digite os ingredientes que você tem em casa",
            },
            {
              n: 2,
              titulo: "Encontre receitas",
              desc: "Veja receitas compatíveis com seus ingredientes",
            },
            {
              n: 3,
              titulo: "Cozinhe e aproveite",
              desc: "Siga o passo a passo e prepare pratos deliciosos",
            },
          ].map(({ n, titulo, desc }) => (
            <div key={n} className="bg-white rounded-xl p-4 shadow-sm">
              <div className="flex gap-3">
                <div className="shrink-0 w-8 h-8 bg-green-100 text-green-600 rounded-full flex items-center justify-center font-bold">
                  {n}
                </div>
                <div>
                  <h3 className="font-medium mb-1">{titulo}</h3>
                  <p className="text-sm text-gray-600">{desc}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
