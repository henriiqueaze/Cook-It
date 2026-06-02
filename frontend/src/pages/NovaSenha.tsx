import { useState, type FormEvent } from "react";
import { ArrowLeft, Lock } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { authService } from "@/services/authService";

interface LocationState {
  email?: string;
  token?: string;
}

export function NovaSenha() {
  const navigate = useNavigate();
  const location = useLocation();
  const estado = location.state as LocationState | null;
  const email = estado?.email ?? "";
  const token = estado?.token ?? "";

  const [novaSenha, setNovaSenha] = useState("");
  const [confirmarNovaSenha, setConfirmarNovaSenha] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!token.trim()) {
      setErro("Codigo nao encontrado. Volte e informe o codigo novamente.");
      return;
    }

    const novaSenhaLimpa = novaSenha.trim();
    const confirmarNovaSenhaLimpa = confirmarNovaSenha.trim();

    if (!novaSenhaLimpa || !confirmarNovaSenhaLimpa) {
      setErro("Preencha os campos de nova senha.");
      return;
    }

    if (novaSenhaLimpa.length < 6) {
      setErro("A nova senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (novaSenhaLimpa !== confirmarNovaSenhaLimpa) {
      setErro("A confirmacao da nova senha nao confere.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      await authService.resetPassword(token.trim(), novaSenhaLimpa);
      toast.success("Senha redefinida com sucesso! Faca login novamente.");
      navigate("/login", { replace: true });
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Nao foi possivel redefinir a senha.",
      );
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="flex min-h-screen flex-col bg-linear-to-b from-orange-50 to-white">
      <div className="sticky top-0 z-10 border-b bg-white">
        <div className="flex items-center gap-3 px-4 py-4">
          <Link
            to="/redefinir-senha"
            state={{ email, token }}
            className="rounded-full p-2 transition-colors hover:bg-gray-100"
            aria-label="Voltar"
          >
            <ArrowLeft size={20} />
          </Link>
          <h1 className="text-xl font-semibold">Nova senha</h1>
        </div>
      </div>

      <div className="flex flex-1 items-center px-6 py-8">
        <form
          onSubmit={handleSubmit}
          className="w-full space-y-4 rounded-2xl bg-white p-6 shadow-lg"
        >
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Crie sua nova senha
            </h2>
            <p className="mt-2 text-sm text-gray-500">
              {email
                ? `Use uma nova senha para a conta ${email}.`
                : "Digite sua nova senha e confirme abaixo."}
            </p>
          </div>

          {erro && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Nova senha
            </label>
            <div className="relative">
              <Lock
                size={18}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />
              <input
                type="password"
                value={novaSenha}
                onChange={(event) => setNovaSenha(event.target.value)}
                placeholder="novaSenha123"
                className="w-full rounded-lg border border-gray-300 py-3 pl-11 pr-4 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Confirmar nova senha
            </label>
            <div className="relative">
              <Lock
                size={18}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />
              <input
                type="password"
                value={confirmarNovaSenha}
                onChange={(event) => setConfirmarNovaSenha(event.target.value)}
                placeholder="novaSenha123"
                className="w-full rounded-lg border border-gray-300 py-3 pl-11 pr-4 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={carregando}
            className="w-full rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
          >
            {carregando ? "Redefinindo..." : "Redefinir senha"}
          </button>
        </form>
      </div>
    </div>
  );
}
