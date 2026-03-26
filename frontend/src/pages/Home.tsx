import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";

export function Home() {
  const navigate = useNavigate();
  const [receitas, setReceitas] = useState<Receita[]>([]);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    let ativo = true;

    receitaService
      .listar()
      .then((lista) => {
        if (ativo) setReceitas(lista);
      })
      .catch(() => {
        if (ativo) setReceitas([]);
      })
      .finally(() => {
        if (ativo) setCarregando(false);
      });

    return () => {
      ativo = false;
    };
  }, []);

  const totalReceitas = receitas.length;
  const destaques = receitas.slice(0, 4);

  return (
    <div className="min-h-screen bg-linear-to-b from-orange-50 via-amber-50/30 to-white pb-20">
      <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 text-white pt-12 pb-8 px-6 rounded-b-3xl shadow-lg">
        <div className="flex items-center gap-3 mb-2">
          <img
            src="/brand-cook-it.png"
            alt="Cook-It"
            className="w-8 h-8 object-contain"
          />
          <h1 className="text-2xl font-bold">COOK-IT</h1>
        </div>
        <p className="text-orange-50 text-sm">
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
            className="w-full bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 transition-all shadow-md"
          >
            Ir para a busca
          </button>
        </div>

        <div className="mt-8">
          <div className="flex items-center gap-2 mb-4">
            <h2 className="font-semibold text-lg">Receitas em Destaque</h2>
          </div>

          {carregando ? (
            <div className="text-sm text-gray-500">Carregando receitas...</div>
          ) : (
            <div className="grid grid-cols-2 gap-4">
              {destaques.length > 0 ? (
                destaques.map((receita) => (
                  <div
                    key={receita.id}
                    onClick={() => navigate(`/receita/${receita.id}`)}
                    className="bg-white rounded-xl overflow-hidden shadow-md cursor-pointer hover:shadow-lg transition-shadow"
                  >
                    <div className="relative h-32 overflow-hidden bg-gray-100">
                      {receita.imagemUrl ? (
                        <img
                          src={receita.imagemUrl}
                          alt={receita.titulo}
                          className="w-full h-full object-cover"
                        />
                      ) : null}
                      <div className="absolute top-2 right-2 bg-orange-500 text-white text-xs px-2 py-1 rounded-full font-medium">
                        {receita.tempoPreparo} min
                      </div>
                    </div>
                    <div className="p-3">
                      <h3 className="font-medium text-sm line-clamp-2">
                        {receita.titulo}
                      </h3>
                    </div>
                  </div>
                ))
              ) : (
                <div className="col-span-2 text-sm text-gray-500">
                  Nenhuma receita encontrada no momento.
                </div>
              )}
            </div>
          )}
        </div>

        <div className="mt-8 grid grid-cols-2 gap-4">
          <div className="bg-linear-to-br from-orange-500 to-orange-600 rounded-xl p-4 text-center shadow-md">
            <div className="text-3xl font-bold text-white mb-1">
              {totalReceitas}+
            </div>
            <div className="text-sm text-orange-50">Receitas</div>
          </div>
          <div className="bg-linear-to-br from-green-500 to-green-600 rounded-xl p-4 text-center shadow-md">
            <div className="text-3xl font-bold text-white mb-1">1000+</div>
            <div className="text-sm text-green-50">Usuários</div>
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
            <div
              key={n}
              className="bg-white rounded-xl p-4 shadow-sm border-l-4 border-orange-500"
            >
              <div className="flex gap-3">
                <div className="shrink-0 w-8 h-8 bg-linear-to-br from-orange-500 to-orange-600 text-white rounded-full flex items-center justify-center font-bold shadow-md">
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
