import { useNavigate } from "react-router-dom";
import { ChefHat } from "lucide-react";

export function NaoEncontrado() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-6 text-center">
      <ChefHat size={64} className="text-gray-300 mb-4" />
      <h1 className="text-6xl font-bold text-green-600 mb-2">404</h1>
      <h2 className="text-xl font-semibold text-gray-700 mb-2">
        Página não encontrada
      </h2>
      <p className="text-gray-400 text-sm mb-8">
        A página que você está procurando não existe ou foi removida.
      </p>
      <button
        onClick={() => navigate("/")}
        className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer hover:bg-green-700 transition-colors"
      >
        Voltar para o início
      </button>
    </div>
  );
}
