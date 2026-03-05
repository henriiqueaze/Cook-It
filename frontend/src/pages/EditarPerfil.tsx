import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Camera, User, Mail, Lock, Save } from "lucide-react";
import { toast } from "sonner";
import { useAuth } from "../contexts/AuthContext";

export function EditarPerfil() {
  const navigate = useNavigate();
  const { usuario, atualizarUsuario, sair } = useAuth();
  const [carregando, setCarregando] = useState(false);

  const [formData, setFormData] = useState({
    nome: usuario?.nome || "",
    email: usuario?.email || "",
    senhaAtual: "",
    novaSenha: "",
    confirmarSenha: "",
  });

  const [fotoPreview, setFotoPreview] = useState(usuario?.avatarUrl || "");

  function handleFotoChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFotoPreview(reader.result as string);
      };
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
      if (formData.novaSenha) {
        if (formData.novaSenha.length < 6) {
          toast.error("A nova senha deve ter pelo menos 6 caracteres");
          return;
        }
        if (formData.novaSenha !== formData.confirmarSenha) {
          toast.error("As senhas não são iguais.");
          return;
        }
        if (!formData.senhaAtual) {
          toast.error("Digite sua senha atual para alterar a senha.");
          return;
        }
      }

      const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email);
      if (!emailValido) {
        toast.error("Digite um e-mail válido");
        return;
      }

      await new Promise((resolve) => setTimeout(resolve, 800)); //lembrar de substituir para authService.autilizarPerfil() qnd for integrar no back

      atualizarUsuario({
        ...usuario!,
        nome: formData.nome,
        email: formData.email,
        avatarUrl: fotoPreview,
      });

      toast.success("Perfil atualizado com sucesso!");

      if (formData.novaSenha) {
        toast.success("Senha alterada! Faça login novamente.");
        setTimeout(() => {
          sair();
          navigate("/login");
        }, 1500);
      } else {
        navigate("/perfil");
      }
    } catch {
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
              <div className="absolute bottom-0 right-0 p-2 bg-green-600 rounded-full shadow-lg">
                <Camera size={18} className="text-white" />
              </div>
            </div>
            <input
              type="file"
              accept="image/*"
              onChange={handleFotoChange}
              className="hidden"
            />
            <span className="text-sm text-green-600 font-medium">
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
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500 text-sm"
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
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500 text-sm"
            />
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Alterar Senha</h2>
          <p className="text-sm text-gray-400">
            Deixe em branco se não deseja alterar
          </p>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              <Lock size={14} className="inline mr-1" /> Senha Atual
            </label>
            <input
              type="password"
              name="senhaAtual"
              value={formData.senhaAtual}
              onChange={handleInputChange}
              placeholder="••••••••"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500 text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              <Lock size={14} className="inline mr-1" /> Nova Senha
            </label>
            <input
              type="password"
              name="novaSenha"
              value={formData.novaSenha}
              onChange={handleInputChange}
              placeholder="Mínimo 6 caracteres"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500 text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              <Lock size={14} className="inline mr-1" /> Confirmar Nova Senha
            </label>
            <input
              type="password"
              name="confirmarSenha"
              value={formData.confirmarSenha}
              onChange={handleInputChange}
              placeholder="••••••••"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500 text-sm"
            />
          </div>
        </div>

        <div className="bg-blue-50 border border-blue-200 rounded-xl p-4">
          <p className="text-sm text-blue-800">
            <strong>Atenção:</strong> Se você alterar sua senha, será necessário
            fazer login novamente.
          </p>
        </div>

        <button
          onClick={handleSalvar}
          disabled={carregando}
          className="w-full flex items-center justify-center gap-2 bg-green-600 text-white py-3 rounded-lg font-medium hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors cursor-pointer"
        >
          <Save size={18} />
          {carregando ? "Salvando..." : "Salvar Alterações"}
        </button>
      </div>
    </div>
  );
}
