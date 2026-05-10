import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { ChefHat, Eye, EyeOff } from "lucide-react";
import { toast } from "sonner";
import { useAuth } from "@/contexts/AuthContext";
import { authService } from "@/services/authService";
import { validarEmail } from "@/lib/utils";

function getMensagemErroLogin(error: unknown) {
  const mensagemPadrao = "E-mail ou senha inválidos.";

  if (!(error instanceof Error) || !error.message) {
    return mensagemPadrao;
  }

  const mensagemNormalizada = error.message.toLowerCase();

  if (
    mensagemNormalizada.includes("não foi confirmado") ||
    mensagemNormalizada.includes("nao foi confirmado") ||
    mensagemNormalizada.includes("confirm")
  ) {
    return "Seu e-mail ainda não foi confirmado. Verifique sua caixa de entrada e confirme o cadastro para entrar.";
  }

  if (
    mensagemNormalizada.includes("credenciais") ||
    mensagemNormalizada.includes("invalid credentials") ||
    mensagemNormalizada.includes("unauthorized") ||
    mensagemNormalizada.includes("401") ||
    mensagemNormalizada.includes("não encontramos o recurso solicitado") ||
    mensagemNormalizada.includes("nao encontramos o recurso solicitado") ||
    mensagemNormalizada.includes("not found")
  ) {
    return mensagemPadrao;
  }

  if (
    mensagemNormalizada.includes("failed to fetch") ||
    mensagemNormalizada.includes("network")
  ) {
    return "Não foi possível conectar ao servidor. Tente novamente.";
  }

  return error.message;
}

function erroPedeReenvioConfirmacao(message: string) {
  const normalizada = message.toLowerCase();

  return (
    normalizada.includes("e-mail ainda não foi confirmado") ||
    normalizada.includes("email not verified") ||
    normalizada.includes("nao foi confirmado") ||
    normalizada.includes("não foi confirmado")
  );
}

export function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const { salvarAuth } = useAuth();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [reenviandoConfirmacao, setReenviandoConfirmacao] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!email.trim() || !senha.trim()) {
      setErro("Preencha todos os campos.");
      return;
    }

    if (!validarEmail(email)) {
      setErro("Digite um e-mail válido.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      const resposta = await authService.login(email.trim(), senha);
      salvarAuth(resposta.token, resposta.user);

      const destino = (location.state as { from?: string } | null)?.from ?? "/";
      navigate(destino, { replace: true });
    } catch (error) {
      setErro(getMensagemErroLogin(error));
    } finally {
      setCarregando(false);
    }
  }

  async function handleReenviarConfirmacao() {
    const emailLimpo = email.trim();

    if (!emailLimpo || !validarEmail(emailLimpo)) {
      setErro("Digite o e-mail usado no cadastro para reenviar a confirmação.");
      return;
    }

    setReenviandoConfirmacao(true);

    try {
      await authService.resendConfirmationEmail(emailLimpo);
      toast.success("Enviamos um novo e-mail de confirmação.");
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Não foi possível reenviar o e-mail.",
      );
    } finally {
      setReenviandoConfirmacao(false);
    }
  }

  return (
    <div className="flex min-h-screen flex-col bg-linear-to-b from-orange-50 to-white">
      <div className="rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-12 pt-10 text-center text-white">
        <div className="mb-3 flex justify-center">
          <ChefHat size={48} />
        </div>
        <h1 className="text-3xl font-bold">Cook-It</h1>
        <p className="mt-2 text-2xl text-orange-100">Entre na sua conta</p>
      </div>

      <div className={`mt-10 flex-1 px-6 ${erro ? "pb-12" : "pb-6"}`}>
        <form
          className="space-y-4 rounded-2xl bg-white p-6 shadow-lg"
          onSubmit={handleSubmit}
        >
          {erro && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              <p>{erro}</p>
              {erroPedeReenvioConfirmacao(erro) && (
                <button
                  type="button"
                  onClick={handleReenviarConfirmacao}
                  disabled={reenviandoConfirmacao}
                  className="mt-2 font-medium text-red-700 underline decoration-red-300 underline-offset-2 transition-colors hover:text-red-800 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  {reenviandoConfirmacao
                    ? "Reenviando e-mail..."
                    : "Reenviar e-mail de confirmação"}
                </button>
              )}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              E-mail
            </label>
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="seu@email.com"
              className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Senha
            </label>
            <div className="relative">
              <input
                type={mostrarSenha ? "text" : "password"}
                value={senha}
                onChange={(event) => setSenha(event.target.value)}
                placeholder="••••••••"
                className="w-full rounded-lg border border-gray-300 py-3 pl-4 pr-12 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
              <button
                type="button"
                onClick={() => setMostrarSenha((valor) => !valor)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 transition-colors hover:text-gray-600"
                aria-label={mostrarSenha ? "Ocultar senha" : "Mostrar senha"}
              >
                {mostrarSenha ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <div className="text-right">
            <Link
              to="/esqueci-senha"
              className="text-sm font-medium text-orange-600 transition-colors hover:text-orange-700"
            >
              Esqueci a senha
            </Link>
          </div>

          <button
            type="submit"
            disabled={carregando}
            className="w-full cursor-pointer rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
          >
            {carregando ? "Entrando..." : "Entrar"}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-500">
          Não tem uma conta?{" "}
          <Link to="/cadastro" className="font-medium text-orange-600">
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  );
}