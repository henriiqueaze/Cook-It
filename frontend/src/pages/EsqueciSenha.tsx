import { useState, type FormEvent } from "react";
import { ArrowLeft, Mail } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { authService } from "@/services/authService";
import { validarEmail } from "@/lib/utils";

function mapearMensagemErroEsqueciSenha(message: string) {
  const normalizado = message.toLowerCase();

  if (
    normalizado.includes("user not found") ||
    normalizado.includes("usuário não encontrado") ||
    normalizado.includes("usuario nao encontrado")
  ) {
    return "Não encontramos uma conta com este e-mail. Verifique o endereço digitado.";
  }

  return "Não foi possível enviar o código agora. Tente novamente em instantes.";
}

export function EsqueciSenha() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const emailLimpo = email.trim();

    if (!emailLimpo) {
      setErro("Informe seu e-mail.");
      return;
    }

    if (!validarEmail(emailLimpo)) {
      setErro("Digite um e-mail válido.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      await authService.forgotPassword(emailLimpo);
      toast.success("Código enviado por e-mail.");
      navigate("/redefinir-senha", {
        state: { email: emailLimpo },
        replace: true,
      });
    } catch (error) {
      const message = error instanceof Error ? error.message : "";
      setErro(mapearMensagemErroEsqueciSenha(message));
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="flex min-h-screen flex-col bg-linear-to-b from-orange-50 to-white">
      <div className="sticky top-0 z-10 border-b bg-white">
        <div className="flex items-center gap-3 px-4 py-4">
          <Link
            to="/login"
            className="rounded-full p-2 transition-colors hover:bg-gray-100"
            aria-label="Voltar"
          >
            <ArrowLeft size={20} />
          </Link>
          <h1 className="text-xl font-semibold">Esqueci a senha</h1>
        </div>
      </div>

      <div className="flex flex-1 items-center px-6 py-8">
        <form
          onSubmit={handleSubmit}
          className="w-full space-y-4 rounded-2xl bg-white p-6 shadow-lg"
        >
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Recuperar acesso
            </h2>
            <p className="mt-2 text-sm text-gray-500">
              Informe seu e-mail para receber o código de redefinição.
            </p>
          </div>

          {erro && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
              {erro}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              E-mail
            </label>
            <div className="relative">
              <Mail
                size={18}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />
              <input
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="seu@email.com"
                className="w-full rounded-lg border border-gray-300 py-3 pl-11 pr-4 text-sm outline-none transition focus:border-transparent focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={carregando}
            className="w-full rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
          >
            {carregando ? "Enviando..." : "Enviar código"}
          </button>
        </form>
      </div>
    </div>
  );
}
