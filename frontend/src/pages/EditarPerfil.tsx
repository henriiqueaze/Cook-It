import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Camera, User, Mail, Save } from "lucide-react";
import { toast } from "sonner";
import { useAuth } from "../contexts/AuthContext";
import { authService } from "@/services/authService";

export function EditarPerfil() {
  const navigate = useNavigate();
  const { usuario, atualizarUsuario } = useAuth();
  const [carregando, setCarregando] = useState(false);

  const [formData, setFormData] = useState({
    nome: usuario?.name || "",
    email: usuario?.email || "",
  });

  const [fotoPreview, setFotoPreview] = useState(usuario?.photo || "");
  const [fotoArquivo, setFotoArquivo] = useState<File | null>(null);

  function handleFotoChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    if (file) {
      setFotoArquivo(file);

      const reader = new FileReader();
      reader.onloadend = () => setFotoPreview(reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  }

  async function handleSalvar(e: React.SyntheticEvent) {
    e.preventDefault();
    setCarregando(true);

    try {
      const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email);
      if (!emailValido) {
        toast.error("Digite um e-mail válido");
        return;
      }

      if (!usuario?.id) {
        toast.error("Usuário não encontrado");
        return;
      }

      const atualizado = await authService.atualizarPerfil(usuario.id, {
        name: formData.nome,
        email: formData.email,
        photoFile: fotoArquivo,
      });

      atualizarUsuario(atualizado);
      toast.success("Perfil atualizado com sucesso!");
      navigate("/perfil");
    } catch (error) {
      console.error(error);
      toast.error("Erro ao atualizar perfil. Tente novamente.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-white border-b sticky top-0 z-10">
        <div className="px-4 py-4 flex items-center gap-3">
          <button
            onClick={() => navigate(-1)}
            className="p-2 hover:bg-gray-100 rounded-full cursor-pointer"
          >
            <ArrowLeft size={20} />
          </button>
          <h1 className="text-xl font-semibold">Editar Perfil</h1>
        </div>
      </div>

      <div className="px-6 py-6 space-y-4">
        <div className="bg-white rounded-2xl p-6 shadow-sm text-center">
          <label className="cursor-pointer">
            <div className="relative w-32 h-32 mx-auto mb-3">
              {fotoPreview ? (
                <img
                  src={fotoPreview}
                  alt="Foto"
                  className="w-32 h-32 rounded-full object-cover border-4 border-gray-100"
                />
              ) : (
                <div className="w-32 h-32 rounded-full bg-gray-100 flex items-center justify-center border-4 border-gray-100">
                  <User size={40} className="text-gray-400" />
                </div>
              )}
              <div className="absolute bottom-0 right-0 p-2 bg-orange-600 rounded-full shadow-lg">
                <Camera size={18} className="text-white" />
              </div>
            </div>
            <input
              type="file"
              accept="image/*"
              onChange={handleFotoChange}
              className="hidden"
            />
            <span className="text-sm text-orange-600 font-medium">
              Alterar Foto
            </span>
          </label>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Informações Pessoais</h2>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              <User size={14} className="inline mr-1" /> Nome
            </label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              <Mail size={14} className="inline mr-1" /> E-mail
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
            />
          </div>
        </div>

        <div className="bg-blue-50 border border-blue-200 rounded-xl p-4">
          <p className="text-sm text-blue-800">
            <strong>Atenção:</strong> a troca de senha ainda não foi exposta no
            backend atual.
          </p>
        </div>

        <button
          onClick={handleSalvar}
          disabled={carregando}
          className="w-full flex items-center justify-center gap-2 bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all cursor-pointer"
        >
          <Save size={18} />
          {carregando ? "Salvando..." : "Salvar Alterações"}
        </button>
      </div>
    </div>
  );
}