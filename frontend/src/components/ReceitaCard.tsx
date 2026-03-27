import { Link } from "react-router-dom";
import { Clock, Users, Star } from "lucide-react";
import type { Receita } from "../types";

interface ReceitaCardProps {
  receita: Receita;
  compatibilidade?: number;
}

export function ReceitaCard({ receita, compatibilidade }: ReceitaCardProps) {
  return (
    <Link to={`/receita/${receita.id}`}>
      <div className="bg-white rounded-2xl shadow-sm overflow-hidden hover:shadow-md transition-shadow">
        <div className="w-full h-40 bg-gray-200 relative">
          {receita.imagemUrl && (
            <img
              src={receita.imagemUrl}
              alt={receita.titulo}
              className="w-full h-full object-cover"
            />
          )}

          {compatibilidade !== undefined && (
            <div className="absolute top-2 right-2 bg-green-600 text-white text-xs px-2 py-1 rounded-full font-medium">
              {compatibilidade}% compatível
            </div>
          )}
        </div>

        <div className="p-3 relative h-20">
          <h3 className="absolute top-3 left-3 font-semibold text-gray-800 text-sm max-w-[70%] leading-tight">
            {receita.titulo}
          </h3>

          <div className="absolute top-3 right-3 flex items-center gap-1 text-xs">
            <Star size={14} className="text-yellow-400 fill-yellow-400" />
            <span className="text-gray-600">{receita.avaliacao}</span>
          </div>

          <div className="absolute bottom-3 left-3 flex items-center gap-1 text-xs text-gray-500">
            <Users size={12} />
            {receita.porcoes} porç.
          </div>

          <div className="absolute bottom-3 right-3 flex items-center gap-1 text-xs text-gray-500">
            <Clock size={12} />
            {receita.tempoPreparo} min
          </div>
        </div>
      </div>
    </Link>
  );
}
