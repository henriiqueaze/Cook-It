import { Link } from "react-router-dom";
import { Clock, Users, Star } from "lucide-react";
import type { Receita } from "../types";

interface ReceitaCardProps {
  receita: Receita;
}

export function ReceitaCard({ receita }: ReceitaCardProps) {
  return (
    <Link to={`/receita/${receita.id}`}>
      <div className="bg-white rounded-2xl shadow-sm overflow-hidden hover:shadow-md transition-shadow">
        <div className="w-full h-40 bg-gray-200">
          {receita.imagemUrl && (
            <img
              src={receita.imagemUrl}
              alt={receita.titulo}
              className="w-full h-full object-cover"
            />
          )}
        </div>

        <div className="p-3">
          <span className="text-xs text-green-600 font-medium">
            {receita.categoria}
          </span>
          <h3 className="font-semibold text-gray-800 mt-1 text-sm leading-tight">
            {receita.titulo}
          </h3>

          <div className="flex items-center gap-3 mt-2 text-xs text-gray-400">
            <span className="flex items-center gap-1">
              <Clock size={12} />
              {receita.tempoPreparo} min
            </span>
            <span className="flex items-center gap-1">
              <Users size={12} />
              {receita.porcoes} porções
            </span>
            <span className="flex items-center gap-1">
              <Star size={12} className="text-yellow-400 fill-yellow-400" />
              {receita.avaliacao}
            </span>
          </div>
        </div>
      </div>
    </Link>
  );
}
