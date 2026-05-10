import { X } from "lucide-react";

interface IngredientTagProps {
  nome: string;
  onRemover: () => void;
}

export function IngredientTag({ nome, onRemover }: IngredientTagProps) {
  return (
    <span className="inline-flex items-center gap-1 rounded-full bg-orange-100 px-3 py-1 text-sm font-medium text-orange-700">
      {nome}
      <button
        type="button"
        onClick={onRemover}
        className="text-orange-500 transition-colors hover:text-orange-800"
        aria-label={`Remover ${nome}`}
      >
        <X size={14} />
      </button>
    </span>
  );
}
