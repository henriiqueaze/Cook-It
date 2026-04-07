import { useEffect, useRef, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";
import { ReceitaCard } from "@/components/ReceitaCard";

const AUTO_ADVANCE_MS = 5000;

export function Home() {
  const navigate = useNavigate();
  const carrosselRef = useRef<HTMLDivElement>(null);
  const autoTimerRef = useRef<number | null>(null);
  const itemRefs = useRef<(HTMLDivElement | null)[]>([]);

  const [receitas, setReceitas] = useState<Receita[]>([]);
  const [destaques, setDestaques] = useState<Receita[]>([]);
  const [carregandoDestaques, setCarregandoDestaques] = useState(true);
  const [activeIndex, setActiveIndex] = useState(0);
  const [direction, setDirection] = useState<"left" | "right">("right");

  useEffect(() => {
    let ativo = true;

    receitaService
      .listar()
      .then((lista) => {
        if (ativo) setReceitas(lista);
      })
      .catch((erro) => {
        console.error("Erro ao carregar receitas:", erro);
        if (ativo) setReceitas([]);
      });

    receitaService
      .destaques()
      .then((lista) => {
        if (ativo) setDestaques(lista);
      })
      .catch((erro) => {
        console.error("Erro ao carregar destaques:", erro);
        if (ativo) setDestaques([]);
      })
      .finally(() => {
        if (ativo) setCarregandoDestaques(false);
      });

    return () => {
      ativo = false;
    };
  }, []);

  const scrollToIndex = useCallback((index: number, smooth = true) => {
    const elemento = itemRefs.current[index];
    if (!elemento) return;

    elemento.scrollIntoView({
      behavior: smooth ? "smooth" : "auto",
      inline: "center",
      block: "nearest",
    });
  }, []);

  useEffect(() => {
    if (destaques.length === 0) return;

    itemRefs.current = itemRefs.current.slice(0, destaques.length);

    const initialIndex = 0;
    setActiveIndex(initialIndex);
    setDirection("right");

    const id = window.requestAnimationFrame(() => {
      scrollToIndex(initialIndex, false);
    });

    return () => {
      window.cancelAnimationFrame(id);
    };
  }, [destaques.length, scrollToIndex]);

  const goNext = useCallback(
    (smooth = true) => {
      if (destaques.length <= 1) return;

      setDirection("right");
      setActiveIndex((current) => {
        const next = Math.min(current + 1, destaques.length - 1);
        scrollToIndex(next, smooth);
        return next;
      });
    },
    [destaques.length, scrollToIndex],
  );

  const goPrev = useCallback(
    (smooth = true) => {
      if (destaques.length <= 1) return;

      setDirection("left");
      setActiveIndex((current) => {
        const prev = Math.max(current - 1, 0);
        scrollToIndex(prev, smooth);
        return prev;
      });
    },
    [destaques.length, scrollToIndex],
  );

  useEffect(() => {
    if (destaques.length <= 1) return;

    if (autoTimerRef.current) {
      window.clearTimeout(autoTimerRef.current);
    }

    autoTimerRef.current = window.setTimeout(() => {
      setActiveIndex((current) => {
        let next = current;
        let nextDirection = direction;

        if (current >= destaques.length - 1) {
          nextDirection = "left";
          next = Math.max(current - 1, 0);
        } else if (current <= 0) {
          nextDirection = "right";
          next = Math.min(current + 1, destaques.length - 1);
        } else if (direction === "right") {
          next = current + 1;
        } else {
          next = current - 1;
        }

        setDirection(nextDirection);
        scrollToIndex(next, true);
        return next;
      });
    }, AUTO_ADVANCE_MS);

    return () => {
      if (autoTimerRef.current) {
        window.clearTimeout(autoTimerRef.current);
      }
    };
  }, [activeIndex, direction, destaques.length, scrollToIndex]);

  const totalReceitas = receitas.length;

  return (
    <div className="min-h-screen bg-linear-to-b from-orange-50 via-amber-50/30 to-white pb-5">
      <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 text-white pt-12 pb-8 px-6 rounded-b-3xl shadow-lg">
        <div className="flex items-center gap-3 mb-2">
          <img
            src="/brand-cook-it.png"
            alt="Cook-It"
            className="w-8 h-8 object-contain"
          />
          <h1 className="text-2xl font-bold">COOK-IT</h1>
        </div>
        <p className="text-orange-50 text-sm text-center">
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
          <div className="flex items-center justify-between gap-2 mb-4">
            <h2 className="font-semibold text-lg">Receitas em Destaque</h2>

            {destaques.length > 0 && (
              <div className="flex gap-2">
                <button
                  type="button"
                  onClick={() => goPrev(true)}
                  className="w-9 h-9 rounded-full bg-white shadow-md flex items-center justify-center text-gray-700 hover:bg-gray-50 active:scale-95 transition"
                  aria-label="Voltar destaque"
                >
                  <ChevronLeft size={20} />
                </button>

                <button
                  type="button"
                  onClick={() => goNext(true)}
                  className="w-9 h-9 rounded-full bg-white shadow-md flex items-center justify-center text-gray-700 hover:bg-gray-50 active:scale-95 transition"
                  aria-label="Avançar destaque"
                >
                  <ChevronRight size={20} />
                </button>
              </div>
            )}
          </div>

          {carregandoDestaques ? (
            <div className="text-sm text-gray-500">Carregando destaques...</div>
          ) : destaques.length > 0 ? (
            <div className="overflow-hidden">
              <div
                ref={carrosselRef}
                className="flex items-center gap-4 overflow-x-auto scroll-smooth pb-3 px-[8%] snap-x snap-mandatory scrollbar-hide"
              >
                {destaques.map((receita, index) => (
                  <div
                    key={receita.id}
                    ref={(el) => {
                      itemRefs.current[index] = el;
                    }}
                    className="shrink-0 w-[82%] sm:w-[62%] md:w-[44%] lg:w-[32%] snap-center"
                  >
                    <ReceitaCard receita={receita} />
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="text-sm text-gray-500">
              Nenhuma receita encontrada no momento.
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
