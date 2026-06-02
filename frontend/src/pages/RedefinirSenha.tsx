import { useState, type FormEvent } from "react";
import { ArrowLeft, Shield } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { authService } from "@/services/authService";

interface LocationState {
  email?: string;
}

export function RedefinirSenha() {
  const navigate = useNavigate();
  const location = useLocation();
  const estado = location.state as LocationState | null;
  const email = estado?.email ?? "";

  const [token, setToken] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");
  const [reenviandoCodigo, setReenviandoCodigo] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!token.trim()) {
      setErro("Informe o codigo recebido por e-mail.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      await authService.validateResetCode(token.trim());
      navigate("/nova-senha", {
        state: { email, token: token.trim() },
        replace: true,
      });
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Nao foi possivel validar o codigo.",
      );
    } finally {
      setCarregando(false);
    }
  }

  async function handleReenviarCodigo() {
    if (!email) {
      setErro(
        "Nao encontramos o e-mail desta solicitacao. Volte e refaca o pedido.",
      );
      return;
    }

    setReenviandoCodigo(true);

    try {
      await authService.forgotPassword(email);
      toast.success("Enviamos um novo codigo por e-mail.");
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Nao foi possivel reenviar o codigo.",
      );
    } finally {
      setReenviandoCodigo(false);
    }
  }

  return (
    <div className="flex min-h-screen flex-col bg-linear-to-b from-orange-50 to-white">
      <div className="sticky top-0 z-10 border-b bg-white">
        <div className="flex items-center gap-3 px-4 py-4">
          <Link
            to="/esqueci-senha"
            className="rounded-full p-2 transition-colors hover:bg-gray-100"
            aria-label="Voltar"
          >
            <ArrowLeft size={20} />
          </Link>
          <h1 className="text-xl font-semibold">Validar codigo</h1>
        </div>
      </div>

      <div className="flex flex-1 items-center px-6 py-8">
        <form
          onSubmit={handleSubmit}
          className="w-full space-y-4 rounded-2xl bg-white p-6 shadow-lg"
        >
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Informe o codigo
            </h2>
            <p className="mt-2 text-sm text-gray-500">
              {email
                ? `Enviamos o codigo para ${email}.`
                : "Informe o codigo recebido por e-mail para continuar."}
            </p>
            {email && (
              <button
                type="button"
                onClick={handleReenviarCodigo}
                disabled={reenviandoCodigo}
                className="mt-3 text-sm font-medium text-orange-600 transition-colors hover:text-orange-700 disabled:cursor-not-allowed disabled:opacity-60"
              >
                {reenviandoCodigo
                  ? "Reenviando codigo..."
                  : "Nao recebi o codigo. Reenviar e-mail"}
              </button>
            )}
          </div>

          {erro && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Codigo
            </label>
            <div className="relative">
              <Shield
                size={18}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />
              <input
                type="text"
                value={token}
                onChange={(event) => setToken(event.target.value)}
                placeholder="483921"
                className="w-full rounded-lg border border-gray-300 py-3 pl-11 pr-4 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={carregando}
            className="w-full rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
          >
            {carregando ? "Validando..." : "Continuar"}
          </button>
        </form>
      </div>
    </div>
  );
}
