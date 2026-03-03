import { useParams, useNavigate } from "react-router-dom";
import { ArrowLeft, Clock, Users, Star, Heart } from "lucide-react";
import { receitasMock } from "../mocks";

export function DetalheReceita() {
  const { id } = useParams();
  const navigate = useNavigate();

  const receita = receitasMock.find((r) => r.id === Number(id));

  if (!receita) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <p className="text-gray-500">Receita não encontrada.</p>
        <button
          onClick={() => navigate(-1)}
          className="mt-4 text-green-600 font-medium"
        >
          Voltar
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="relative w-full h-64 bg-gray-200">
        {receita.imagemUrl && (
          <img
            src={receita.imagemUrl}
            alt={receita.titulo}
            className="w-full h-full object-cover"
          />
        )}
        <button
          onClick={() => navigate(-1)}
          className="absolute top-4 left-4 bg-white rounded-full p-2 shadow-md cursor-pointer"
        >
          <ArrowLeft size={20} className="text-gray-700" />
        </button>
        <button className="absolute top-4 right-4 bg-white rounded-full p-2 shadow-md cursor-pointer">
          <Heart
            size={20}
            className={
              receita.favoritada ? "text-red-500 fill-red-500" : "text-gray-400"
            }
          />
        </button>
      </div>

      <div className="px-6 py-4">
        <div className="flex items-start justify-between gap-2 mb-2">
          <h1 className="text-xl font-bold text-gray-800 flex-1">
            {receita.titulo}
          </h1>
        </div>

        <p className="text-sm text-gray-500 mb-4">{receita.descricao}</p>

        <div className="flex gap-4 mb-6">
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Clock size={16} className="text-green-600" />
            {receita.tempoPreparo} min
          </div>
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Users size={16} className="text-green-600" />
            {receita.porcoes} porções
          </div>
          <div className="flex items-center gap-1 text-sm text-gray-500">
            <Star size={16} className="text-yellow-400 fill-yellow-400" />
            {receita.avaliacao}
          </div>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm mb-4">
          <h2 className="font-semibold text-gray-800 mb-3">Ingredientes</h2>
          <ul className="space-y-2">
            {receita.ingredientes.map((ing) => (
              <li key={ing.id} className="flex justify-between text-sm">
                <span className="text-gray-700">{ing.nome}</span>
                <span className="text-gray-400">
                  {ing.quantidade} {ing.unidade}
                </span>
              </li>
            ))}
          </ul>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800 mb-3">Modo de Preparo</h2>
          <ol className="space-y-3">
            {receita.instrucoes.map((passo, index) => (
              <li key={index} className="flex gap-3 text-sm">
                <span className="shrink-0 w-6 h-6 bg-green-600 text-white rounded-full flex items-center justify-center text-xs font-bold">
                  {index + 1}
                </span>
                <span className="text-gray-600 pt-0.5">{passo}</span>
              </li>
            ))}
          </ol>
        </div>
      </div>
    </div>
  );
}
