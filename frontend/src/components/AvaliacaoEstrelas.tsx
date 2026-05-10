import { Star } from "lucide-react";

interface RatingStarsProps {
  avaliacao: number;
  onChange?: (avaliacao: number) => void;
  somenteLeitura?: boolean;
  tamanho?: "sm" | "md" | "lg";
}

const tamanhos = {
  sm: "w-4 h-4",
  md: "w-6 h-6",
  lg: "w-8 h-8",
} as const;

export function RatingStars({
  avaliacao,
  onChange,
  somenteLeitura = false,
  tamanho = "md",
}: RatingStarsProps) {
  return (
    <div className="flex gap-1" aria-label={`Avaliação de ${avaliacao} estrelas`}>
      {[1, 2, 3, 4, 5].map((estrela) => {
        const selecionada = estrela <= avaliacao;

        return (
          <button
            key={estrela}
            type="button"
            onClick={() => {
              if (!somenteLeitura) {
                onChange?.(estrela);
              }
            }}
            disabled={somenteLeitura}
            className={`transition-transform ${somenteLeitura ? "cursor-default" : "cursor-pointer hover:scale-110"}`}
            aria-label={`${estrela} estrela${estrela > 1 ? "s" : ""}`}
          >
            <Star
              className={`${tamanhos[tamanho]} ${selecionada ? "fill-yellow-400 text-yellow-400" : "text-gray-300"}`}
            />
          </button>
        );
      })}
    </div>
  );
}
