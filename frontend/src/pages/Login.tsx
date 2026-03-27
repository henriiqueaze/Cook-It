import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ChefHat, Eye, EyeOff } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { authService } from "@/services/authService";

export function Login() {
  const navigate = useNavigate();
  const { salvarAuth } = useAuth();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function handleLogin() {
    if (!email || !senha) {
      setErro("Preencha todos os campos.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    if (!emailRegex) {
      setErro("Digite um e-mail válido.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      const resposta = await authService.login(email, senha);
      salvarAuth(resposta.token, resposta.user);
      navigate("/"); 
    } catch (error: any) {
      if (error.message === "Invalid credentials") {
        setErro("Email ou senha inválidos");
      } else {
        setErro("Erro ao fazer login. Tente novamente.");
      }
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
        <h1 className="text-3xl font-bold">Cook-It</h1>
        <p className="text-orange-100 text-2xl mt-2">Entre na sua conta</p>
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

          <button
            onClick={handleLogin}
            disabled={carregando}
            className="w-full bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all cursor-pointer"
          >
            {carregando ? "Entrando..." : "Entrar"}
          </button>
        </div>

        <p className="text-center text-sm text-gray-500 mt-6">
          Não tem uma conta?{" "}
          <Link to="/cadastro" className="text-orange-600 font-medium">
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  );
}
