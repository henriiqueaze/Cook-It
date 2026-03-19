import { useNavigate } from "react-router-dom";
import { ChefHat } from "lucide-react";
import { receitasMock } from "../mocks";

export function Home() {
  const navigate = useNavigate();
  const totalReceitas = receitasMock.length;

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
          <h2 className="text-lg font-semibold text-gray-800 mb-2">
            Comece sua busca
          </h2>
          <p className="text-sm text-gray-600 mb-4">
            Vá para a tela de busca e adicione os ingredientes que você tem em
            casa.
          </p>

          <button
            onClick={() => navigate("/busca")}
            className="w-full bg-green-600 text-white py-3 rounded-lg font-medium hover:bg-green-700 transition-colors"
          >
            Ir para a busca
          </button>
        </div>

        <div className="mt-8 grid grid-cols-2 gap-4">
          <div className="bg-white rounded-xl p-4 text-center shadow-sm">
            <div className="text-3xl font-bold text-green-600 mb-1">
              {totalReceitas}+
            </div>
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
