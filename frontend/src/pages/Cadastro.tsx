import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ChefHat, Eye, EyeOff } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { authService } from "@/services/authService";

export function Cadastro() {
  const navigate = useNavigate();
  const { salvarAuth } = useAuth();

  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function handleCadastro() {
    if (!nome || !email || !senha || !confirmarSenha) {
      setErro("Preencha todos os campos. Não deixe faltar nenhum.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    if (!emailRegex) {
      setErro("Digite um email válido.");
      return;
    }

    if (senha !== confirmarSenha) {
      setErro("As senhas não são iguais.");
      return;
    }

    if (senha.length < 6) {
      setErro("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      const resposta = await authService.cadastrar(nome, email, senha);
      salvarAuth(resposta.token, resposta.usuario);
      navigate("/");
    } catch (error) {
      setErro(error instanceof Error ? error.message : "Erro ao criar conta. Tente de novo.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen bg-linear-to-b from-orange-50 to-white flex flex-col">
      <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 text-white pt-10 pb-12 px-6 rounded-b-3xl text-center">
        <div className="flex justify-center mb-3">
          <ChefHat size={48} />
        </div>
        <h1 className="text-3xl font-bold">Receitas Inteligentes</h1>
        <p className="text-orange-100 text-2xl mt-2">Crie sua conta</p>
      </div>

      <div className="px-6 mt-20 flex-1">
        <div className="bg-white rounded-2xl shadow-lg p-6 space-y-4">
          {erro && (
            <div className="bg-red-50 text-red-600 text-sm px-4 py-3 rounded-lg">
              {erro}
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome
            </label>
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Seu nome completo"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              E-mail
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="seu@email.com"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Senha
            </label>
            <div className="relative">
              <input
                type={mostrarSenha ? "text" : "password"}
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                placeholder="••••••••"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm pr-12"
              />
              <button
                type="button"
                onClick={() => setMostrarSenha(!mostrarSenha)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer"
              >
                {mostrarSenha ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Confirmar Senha
            </label>
            <input
              type={mostrarSenha ? "text" : "password"}
              value={confirmarSenha}
              onChange={(e) => setConfirmarSenha(e.target.value)}
              placeholder="••••••••"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
            />
          </div>

          <button
            onClick={handleCadastro}
            disabled={carregando}
            className="w-full bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all cursor-pointer"
          >
            {carregando ? "Criando conta..." : "Criar conta"}
          </button>
        </div>

        <p className="text-center text-sm text-gray-500 mt-6">
          Já tem uma conta?{" "}
          <Link to="/login" className="text-orange-600 font-medium">
            Entrar
          </Link>
        </p>
      </div>
    </div>
  );
}
