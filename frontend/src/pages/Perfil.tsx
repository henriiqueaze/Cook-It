import { useNavigate, Link } from "react-router-dom";
import {
  LogOut,
  User,
  BookOpen,
  Heart,
  Star,
  Edit,
  Settings,
} from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { receitasMock } from "../mocks";
import { toast } from "sonner";

export function Perfil() {
  const navigate = useNavigate();
  const { usuario, estaAutenticado, sair } = useAuth();

  const minhasReceitas = receitasMock.filter((r) => r.autor.id === usuario?.id);
  const meusFavoritos = receitasMock.filter((r) => r.favoritada);
  const totalAvaliacoes = 0; // lembrar sera alterado para vir do back

  function handleSair() {
    sair();
    toast.success("Até mais!");
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
      <div className="bg-linear-to-b from-green-600 to-green-500 text-white pt-12 pb-12 px-6 rounded-b-3xl">
        <div className="flex items-start justify-between mb-6">
          <h1 className="text-2xl font-bold">Perfil</h1>
          <Link
            to="/configuracoes"
            className="p-2 hover:bg-green-700 rounded-lg transition-colors"
          >
            <Settings size={20} />
          </Link>
        </div>

        <div className="flex items-center gap-4">
          <div className="w-20 h-20 rounded-full bg-white overflow-hidden border-4 border-white shadow-lg">
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
          <div className="flex-1">
            <h2 className="text-xl font-bold">{usuario?.nome}</h2>
            <p className="text-green-100 text-sm">{usuario?.email}</p>
          </div>
        </div>

        <Link
          to="/editar-perfil"
          className="mt-4 w-full flex items-center justify-center gap-2 bg-white/20 hover:bg-white/30 py-2 px-4 rounded-lg font-medium transition-colors"
        >
          <Edit size={16} />
          Editar Perfil
        </Link>
      </div>

      <div className="px-6 mt-6 space-y-4">
        <div className="bg-white rounded-2xl shadow-sm p-4 grid grid-cols-3 gap-4 text-center">
          <div>
            <div className="text-2xl font-bold text-green-600">
              {minhasReceitas.length}
            </div>
            <div className="text-xs text-gray-500 mt-1">Receitas</div>
          </div>
          <div>
            <div className="text-2xl font-bold text-green-600">
              {meusFavoritos.length}
            </div>
            <div className="text-xs text-gray-500 mt-1">Favoritos</div>
          </div>
          <div>
            <div className="text-2xl font-bold text-green-600">
              {totalAvaliacoes}
            </div>
            <div className="text-xs text-gray-500 mt-1">Avaliações</div>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-sm divide-y divide-gray-100">
          <Link
            to="/minhas-receitas"
            className="flex items-center gap-3 p-4 hover:bg-gray-50 transition-colors"
          >
            <div className="p-2 bg-green-100 rounded-lg">
              <BookOpen size={20} className="text-green-600" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Minhas Receitas</h3>
              <p className="text-sm text-gray-500">
                {minhasReceitas.length} receitas criadas
              </p>
            </div>
          </Link>

          <Link
            to="/favoritos"
            className="flex items-center gap-3 p-4 hover:bg-gray-50 transition-colors"
          >
            <div className="p-2 bg-red-100 rounded-lg">
              <Heart size={20} className="text-red-500" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Receitas Favoritas</h3>
              <p className="text-sm text-gray-500">
                {meusFavoritos.length} receitas salvas
              </p>
            </div>
          </Link>

          <div className="flex items-center gap-3 p-4">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <Star size={20} className="text-yellow-500" />
            </div>
            <div className="flex-1">
              <h3 className="font-medium">Minhas Avaliações</h3>
              <p className="text-sm text-gray-500">
                {totalAvaliacoes} receitas avaliadas
              </p>
            </div>
          </div>
        </div>

        {minhasReceitas.length > 0 && (
          <div>
            <div className="flex items-center justify-between mb-3">
              <h2 className="font-semibold text-gray-800">Minhas Receitas</h2>
              <Link to="/minhas-receitas" className="text-sm text-green-600">
                Ver todas
              </Link>
            </div>
            <div className="grid grid-cols-2 gap-3">
              {minhasReceitas.slice(0, 2).map((receita) => (
                <Link key={receita.id} to={`/receita/${receita.id}`}>
                  {receita.imagemUrl && (
                    <img
                      src={receita.imagemUrl}
                      alt={receita.titulo}
                      className="w-full aspect-square object-cover rounded-xl"
                    />
                  )}
                  <h3 className="mt-2 text-sm font-medium line-clamp-1">
                    {receita.titulo}
                  </h3>
                </Link>
              ))}
            </div>
          </div>
        )}

        <button
          onClick={handleSair}
          className="w-full flex items-center justify-center gap-2 bg-white border border-red-300 text-red-600 py-3 rounded-lg font-medium cursor-pointer hover:bg-red-50 transition-colors"
        >
          <LogOut size={18} />
          Sair da conta
        </button>
      </div>
    </div>
  );
}
