import { useSearchParams } from "react-router-dom";
import { ChefHat } from "lucide-react";
import { ReceitaCard } from "@/components/ReceitaCard";
import { receitasMock } from "@/mocks";

export function ResultadosBusca() {
  const [searchParams] = useSearchParams();
  const ingredientes = searchParams.get("ingredientes")?.split(",") ?? [];

  const resultados = receitasMock.filter((receita) =>
    ingredientes.some((ingred) =>
      receita.ingredientes.some((i) =>
        i.nome.toLowerCase().includes(ingred.toLowerCase()),
      ),
    ),
  );

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-green-600 text-white pt-12 pb-6 px-6 rounded-b-3xl">
        <h1 className="text-xl font-bold mb-1">Resultados da Busca</h1>
        <p className="text-green-100 text-sm">
          Ingredientes: {ingredientes.join(", ")}
        </p>
      </div>

      <div className="px-6 mt-6">
        {resultados.length === 0 && (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <ChefHat size={48} className="text-gray-300 mb-4" />
            <h2 className="text-lg font-semibold text-gray-500">
              Nenhuma receita encontrada!
            </h2>
            <p className="text-sm text-gray-400 mt-1">
              Adicione mais ingredientes ou tente buscar outros!
            </p>
          </div>
        )}

        {resultados.length > 0 && (
          <>
            <p className="text-sm text-gray-500 mb-4">
              {resultados.length} receita(s) encontrada(s).
            </p>
            <div className="grid grid-cols-2 gap-3">
              {resultados.map((receota) => (
                <ReceitaCard key={receota.id} receita={receota} />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
