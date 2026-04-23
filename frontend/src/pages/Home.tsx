import { useCallback, useEffect, useRef, useState } from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { ReceitaCard } from "@/components/ReceitaCard";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";

export function Home() {
  const navigate = useNavigate();
  const carrosselRef = useRef<HTMLDivElement>(null);

  const [receitas, setReceitas] = useState<Receita[]>([]);
  const [destaques, setDestaques] = useState<Receita[]>([]);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    let ativo = true;

    async function carregar() {
      try {
        const [lista, topRated] = await Promise.all([
          receitaService.listar(),
          receitaService.destaques(),
        ]);

        if (!ativo) {
          return;
        }

        setReceitas(lista);
        setDestaques(topRated);
      } catch {
        if (ativo) {
          setReceitas([]);
          setDestaques([]);
        }
      } finally {
        if (ativo) {
          setCarregando(false);
        }
      }
    }

    void carregar();

    return () => {
      ativo = false;
    };
  }, []);

  const moverCarrossel = useCallback((direcao: "left" | "right") => {
    const container = carrosselRef.current;

    if (!container) {
      return;
    }

    const deslocamento = container.clientWidth * 0.85;
    container.scrollBy({
      left: direcao === "right" ? deslocamento : -deslocamento,
      behavior: "smooth",
    });
  }, []);

  return (
    <div className="min-h-screen bg-linear-to-b from-orange-50 via-amber-50/30 to-white pb-3">
      <div className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-8 pt-9 text-white shadow-lg">
        <div className="mb-2 flex items-center gap-3">
          <img
            src="/brand-cook-it.png"
            alt="Cook-It"
            className="h-8 w-8 object-contain"
          />
          <h1 className="text-2xl font-bold">COOK-IT</h1>
        </div>
        <p className="text-center text-sm text-orange-50">
          Encontre receitas com os ingredientes que você tem
        </p>
      </div>

      <div className="-mt-4 px-6">
        <section className="rounded-2xl bg-white p-6 shadow-lg">
          <h2 className="mb-2 text-lg font-semibold text-gray-800">
            Comece sua busca
          </h2>
          <p className="mb-4 text-sm text-gray-600">
            Vá para a tela de busca e adicione os ingredientes que você tem em
            casa.
          </p>
          <button
            type="button"
            onClick={() => navigate("/busca")}
            className="w-full rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white shadow-md transition-all hover:from-orange-600 hover:to-orange-700"
          >
            Ir para a busca
          </button>
        </section>

        <section className="mt-8">
          <div className="mb-4 flex items-center justify-between gap-2">
            <h2 className="text-lg font-semibold text-gray-800">
              Receitas em destaque
            </h2>

            {destaques.length > 0 && (
              <div className="flex gap-2">
                <button
                  type="button"
                  onClick={() => moverCarrossel("left")}
                  className="flex h-9 w-9 items-center justify-center rounded-full bg-white text-gray-700 shadow-md transition hover:bg-gray-50 active:scale-95"
                  aria-label="Voltar destaque"
                >
                  <ChevronLeft size={20} />
                </button>
                <button
                  type="button"
                  onClick={() => moverCarrossel("right")}
                  className="flex h-9 w-9 items-center justify-center rounded-full bg-white text-gray-700 shadow-md transition hover:bg-gray-50 active:scale-95"
                  aria-label="Avançar destaque"
                >
                  <ChevronRight size={20} />
                </button>
              </div>
            )}
          </div>

          {carregando ? (
            <div className="text-sm text-gray-500">Carregando receitas...</div>
          ) : destaques.length > 0 ? (
            <div
              ref={carrosselRef}
              className="flex snap-x snap-mandatory gap-4 overflow-x-auto scroll-smooth pb-3 [scrollbar-width:none]"
            >
              {destaques.map((receita) => (
                <div
                  key={receita.id}
                  className="w-[82%] shrink-0 snap-center sm:w-[62%] md:w-[44%] lg:w-[32%]"
                >
                  <ReceitaCard receita={receita} />
                </div>
              ))}
            </div>
          ) : (
            <div className="text-sm text-gray-500">
              Nenhuma receita encontrada no momento.
            </div>
          )}
        </section>

        <section className="mt-8 grid grid-cols-2 gap-4">
          <div className="rounded-xl bg-linear-to-br from-orange-500 to-orange-600 p-4 text-center shadow-md">
            <div className="mb-1 text-3xl font-bold text-white">
              {receitas.length}+
            </div>
            <div className="text-sm text-orange-50">Receitas</div>
          </div>
          <div className="rounded-xl bg-linear-to-br from-green-500 to-green-600 p-4 text-center shadow-md">
            <div className="mb-1 text-3xl font-bold text-white">1000+</div>
            <div className="text-sm text-green-50">Usuários</div>
          </div>
        </section>

        <section className="mb-8 mt-8 space-y-4">
          <h2 className="text-lg font-semibold text-gray-800">Ações rápidas</h2>
          <div className="grid gap-3">
            <button
              type="button"
              onClick={() => navigate("/busca")}
              className="rounded-xl bg-white p-4 text-left shadow-sm transition hover:shadow-md"
            >
              <p className="font-semibold text-gray-800">
                Buscar por ingredientes
              </p>
              <p className="mt-1 text-sm text-gray-500">
                Monte uma busca rápida com o que você tem em casa.
              </p>
            </button>

            <button
              type="button"
              onClick={() => navigate("/favoritos")}
              className="rounded-xl bg-white p-4 text-left shadow-sm transition hover:shadow-md"
            >
              <p className="font-semibold text-gray-800">Ver favoritos</p>
              <p className="mt-1 text-sm text-gray-500">
                Acesse as receitas que você salvou.
              </p>
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}
