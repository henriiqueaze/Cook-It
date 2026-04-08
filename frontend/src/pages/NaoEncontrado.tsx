import { ChefHat } from "lucide-react";
import { useNavigate } from "react-router-dom";

export function NaoEncontrado() {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
      <ChefHat size={64} className="mb-4 text-gray-300" />
      <h1 className="mb-2 text-6xl font-bold text-orange-600">404</h1>
      <h2 className="mb-2 text-xl font-semibold text-gray-700">
        Página não encontrada
      </h2>
      <p className="mb-8 text-sm text-gray-400">
        A página que você está procurando não existe ou foi removida.
      </p>
      <button
        type="button"
        onClick={() => navigate("/")}
        className="rounded-lg bg-orange-600 px-6 py-3 font-medium text-white transition-colors hover:bg-orange-700"
      >
        Voltar para o início
      </button>
    </div>
  );
}
