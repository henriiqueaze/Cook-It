import { Star } from "lucide-react";

interface RatingStarsProps {
  avaliacao: number;
  onChange?: (avaliacao: number) => void;
  somenteLeitura?: boolean;
  tamanho?: "sm" | "md" | "lg";
}

export function RatingStars({
  avaliacao,
  onChange,
  somenteLeitura = false,
  tamanho = "md",
}: RatingStarsProps) {
  const tamanhos = {
    sm: "w-4 h-4",
    md: "w-6 h-6",
    lg: "w-8 h-8",
  };

  return (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((estrela) => (
        <button
          key={estrela}
          onClick={() => !somenteLeitura && onChange?.(estrela)}
          disabled={somenteLeitura}
          className={`${somenteLeitura ? "cursor-default" : "cursor-pointer hover:scale-110"} transition-transform`}
        >
          <Star
            className={`${tamanhos[tamanho]} ${
              estrela <= avaliacao
                ? "fill-yellow-400 text-yellow-400"
                : "text-gray-300"
            }`}
          />
        </button>
      ))}
    </div>
  );
}
