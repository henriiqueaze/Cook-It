import { useState, type FormEvent } from "react";
import { ArrowLeft, Shield } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
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

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!token.trim()) {
      setErro("Informe o código recebido por e-mail.");
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
          : "Não foi possível validar o código.",
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
            to="/esqueci-senha"
            className="rounded-full p-2 transition-colors hover:bg-gray-100"
            aria-label="Voltar"
          >
            <ArrowLeft size={20} />
          </Link>
          <h1 className="text-xl font-semibold">Validar código</h1>
        </div>
      </div>

      <div className="flex flex-1 items-center px-6 py-8">
        <form
          onSubmit={handleSubmit}
          className="w-full space-y-4 rounded-2xl bg-white p-6 shadow-lg"
        >
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Informe o código
            </h2>
            <p className="mt-2 text-sm text-gray-500">
              {email
                ? `Enviamos o código para ${email}.`
                : "Informe o código recebido por e-mail para continuar."}
            </p>
          </div>

          {erro && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Código
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
