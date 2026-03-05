import { useNavigate } from "react-router-dom";
import { LogOut, User, Mail, ChefHat, Heart } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { receitasMock } from "../mocks";

export function Perfil() {
  const navigate = useNavigate();
  const { usuario, estaAutenticado, sair } = useAuth();

  const minhasReceitas = receitasMock.filter((r) => r.autor.id === usuario?.id);
  const meusFavoritos = receitasMock.filter((r) => r.favoritada);

  function handleSair() {
    sair();
    navigate("/login");
  }

  if (!estaAutenticado) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-6 text-center">
        <User size={48} className="text-gray-300 mb-4" />
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="text-sm text-gray-400 mt-1 mb-6">
          Entre na sua conta para ver seu perfil
        </p>
        <button
          onClick={() => navigate("/login")}
          className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer"
        >
          Fazer login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-green-600 text-white pt-12 pb-10 px-6 rounded-b-3xl text-center">
        <div className="w-20 h-20 rounded-full bg-white mx-auto mb-3 overflow-hidden">
          {usuario?.avatarUrl ? (
            <img
              src={usuario.avatarUrl}
              alt={usuario.nome}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center">
              <User size={36} className="text-green-600" />
            </div>
          )}
        </div>
        <h1 className="text-xl font-bold">{usuario?.nome}</h1>
        <p className="text-green-100 text-sm mt-1">{usuario?.email}</p>
      </div>

      <div className="px-6 mt-6 space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div className="bg-white rounded-2xl p-4 text-center shadow-sm">
            <div className="flex justify-center mb-2">
              <ChefHat size={24} className="text-green-600" />
            </div>
            <div className="text-2xl font-bold text-gray-800">
              {minhasReceitas.length}
            </div>
            <div className="text-sm text-gray-500">Receitas criadas</div>
          </div>
          <div className="bg-white rounded-2xl p-4 text-center shadow-sm">
            <div className="flex justify-center mb-2">
              <Heart size={24} className="text-red-500" />
            </div>
            <div className="text-2xl font-bold text-gray-800">
              {meusFavoritos.length}
            </div>
            <div className="text-sm text-gray-500">Favoritos</div>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Informações</h2>

          <div className="flex items-center gap-3 text-sm text-gray-600">
            <User size={18} className="text-green-600 shrink-0" />
            <span>{usuario?.nome}</span>
          </div>

          <div className="flex items-center gap-3 text-sm text-gray-600">
            <Mail size={18} className="text-green-600 shrink-0" />
            <span>{usuario?.email}</span>
          </div>
        </div>

        <button
          onClick={handleSair}
          className="w-full flex items-center justify-center gap-2 bg-red-50 text-red-600 py-3 rounded-lg font-medium cursor-pointer hover:bg-red-100 transition-colors"
        >
          <LogOut size={18} />
          Sair da conta
        </button>
      </div>
    </div>
  );
}
