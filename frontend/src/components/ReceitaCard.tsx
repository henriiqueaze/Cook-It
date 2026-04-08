import { Clock, Star, Users } from "lucide-react";
import { Link } from "react-router-dom";
import type { Receita } from "@/types";

interface ReceitaCardProps {
  receita: Receita;
  compatibilidade?: number;
}

export function ReceitaCard({ receita, compatibilidade }: ReceitaCardProps) {
  return (
    <Link
      to={`/receita/${receita.id}`}
      className="group block overflow-hidden rounded-2xl bg-white shadow-sm transition-shadow hover:shadow-md"
    >
      <div className="relative h-40 w-full bg-gray-200">
        {receita.imagemUrl ? (
          <img
            src={receita.imagemUrl}
            alt={receita.titulo}
            className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-[1.02]"
            loading="lazy"
          />
        ) : (
          <div className="flex h-full w-full items-center justify-center bg-linear-to-br from-orange-100 to-amber-50 text-orange-400">
            <span className="text-sm font-medium">Sem imagem</span>
          </div>
        )}

        {compatibilidade !== undefined && (
          <div className="absolute right-2 top-2 rounded-full bg-green-600 px-2 py-1 text-xs font-medium text-white">
            {compatibilidade}% compatível
          </div>
        )}
      </div>

      <div className="p-3">
        <div className="flex items-start justify-between gap-2">
          <h3 className="text-sm font-semibold text-gray-800">
            {receita.titulo}
          </h3>

          <div className="flex items-center gap-1 text-xs">
            <Star size={14} className="fill-yellow-400 text-yellow-400" />
            <span className="text-gray-600">{receita.avaliacao.toFixed(1)}</span>
          </div>
        </div>

        <div className="mt-3 flex items-center justify-between gap-2 text-xs text-gray-500">
          <span className="inline-flex items-center gap-1">
            <Users size={12} />
            {receita.porcoes} porç.
          </span>
          <span className="inline-flex items-center gap-1">
            <Clock size={12} />
            {receita.tempoPreparo} min
          </span>
        </div>
      </div>
    </Link>
  );
}
