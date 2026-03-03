import { X } from "lucide-react";

interface IngredientTagProps {
  nome: string;
  onRemover: () => void;
}

export function IngredientTag({ nome, onRemover }: IngredientTagProps) {
  return (
    <span className="flex items-center gap-1 bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-medium">
      {nome}
      <button
        onClick={onRemover}
        className="hover:text-green-900 transition-colors"
      >
        <X size={14} />
      </button>
    </span>
  );
}
