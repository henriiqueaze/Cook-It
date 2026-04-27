import { useState, type ChangeEvent, type FormEvent } from "react";
import { ArrowLeft, Eye, EyeOff, Lock } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { authService } from "@/services/authService";

export function AlterarSenha() {
  const navigate = useNavigate();
  const [carregando, setCarregando] = useState(false);
  const [formSenha, setFormSenha] = useState({
    senhaAtual: "",
    novaSenha: "",
    confirmarNovaSenha: "",
  });
  const [mostrarSenhas, setMostrarSenhas] = useState({
    atual: false,
    nova: false,
    confirmar: false,
  });

  function handleSenhaChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;
    setFormSenha((current) => ({ ...current, [name]: value }));
  }

  function alternarVisualizacaoSenha(campo: keyof typeof mostrarSenhas) {
    setMostrarSenhas((current) => ({
      ...current,
      [campo]: !current[campo],
    }));
  }

  async function handleAlterarSenha(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const senhaAtual = formSenha.senhaAtual.trim();
    const novaSenha = formSenha.novaSenha.trim();
    const confirmarNovaSenha = formSenha.confirmarNovaSenha.trim();

    if (!senhaAtual || !novaSenha || !confirmarNovaSenha) {
      toast.error("Preencha todos os campos de senha.");
      return;
    }

    if (novaSenha.length < 6) {
      toast.error("A nova senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (novaSenha !== confirmarNovaSenha) {
      toast.error("A confirmação da nova senha não confere.");
      return;
    }

    if (senhaAtual === novaSenha) {
      toast.error("A nova senha deve ser diferente da senha atual.");
      return;
    }

    setCarregando(true);

    try {
      await authService.alterarSenha(senhaAtual, novaSenha);
      setFormSenha({ senhaAtual: "", novaSenha: "", confirmarNovaSenha: "" });
      toast.success("Senha alterada com sucesso!");
      navigate("/perfil");
    } catch (error) {
      const message = error instanceof Error ? error.message : "";
      toast.error(message || "Não foi possível alterar a senha.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="sticky top-0 z-10 border-b bg-white">
        <div className="flex items-center gap-3 px-4 py-4">
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="rounded-full p-2 transition-colors hover:bg-gray-100"
            aria-label="Voltar"
          >
            <ArrowLeft size={20} />
          </button>
          <h1 className="text-xl font-semibold">Alterar senha</h1>
        </div>
      </div>

      <form onSubmit={handleAlterarSenha} className="space-y-3 px-6 py-6">
        <div className="space-y-3 rounded-2xl bg-white p-4 shadow-sm">
          <p className="text-sm text-gray-500">
            Informe sua senha atual e a nova senha para atualizar o acesso.
          </p>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              <Lock size={14} className="mr-1 inline" /> Senha atual
            </label>
            <div className="relative">
              <input
                type={mostrarSenhas.atual ? "text" : "password"}
                name="senhaAtual"
                value={formSenha.senhaAtual}
                onChange={handleSenhaChange}
                className="w-full rounded-lg border border-gray-300 py-3 pl-4 pr-12 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />
              <button
                type="button"
                onClick={() => alternarVisualizacaoSenha("atual")}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 transition-colors hover:text-gray-600"
                aria-label={
                  mostrarSenhas.atual
                    ? "Ocultar senha atual"
                    : "Mostrar senha atual"
                }
              >
                {mostrarSenhas.atual ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              <Lock size={14} className="mr-1 inline" /> Nova senha
            </label>
            <div className="relative">
              <input
                type={mostrarSenhas.nova ? "text" : "password"}
                name="novaSenha"
                value={formSenha.novaSenha}
                onChange={handleSenhaChange}
                className="w-full rounded-lg border border-gray-300 py-3 pl-4 pr-12 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />
              <button
                type="button"
                onClick={() => alternarVisualizacaoSenha("nova")}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 transition-colors hover:text-gray-600"
                aria-label={
                  mostrarSenhas.nova
                    ? "Ocultar nova senha"
                    : "Mostrar nova senha"
                }
              >
                {mostrarSenhas.nova ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              <Lock size={14} className="mr-1 inline" /> Confirmar nova senha
            </label>
            <div className="relative">
              <input
                type={mostrarSenhas.confirmar ? "text" : "password"}
                name="confirmarNovaSenha"
                value={formSenha.confirmarNovaSenha}
                onChange={handleSenhaChange}
                className="w-full rounded-lg border border-gray-300 py-3 pl-4 pr-12 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />
              <button
                type="button"
                onClick={() => alternarVisualizacaoSenha("confirmar")}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 transition-colors hover:text-gray-600"
                aria-label={
                  mostrarSenhas.confirmar
                    ? "Ocultar confirmação da senha"
                    : "Mostrar confirmação da senha"
                }
              >
                {mostrarSenhas.confirmar ? (
                  <EyeOff size={18} />
                ) : (
                  <Eye size={18} />
                )}
              </button>
            </div>
          </div>
        </div>

        <button
          type="submit"
          disabled={carregando}
          className="flex w-full cursor-pointer items-center justify-center gap-2 rounded-lg border border-orange-200 bg-orange-50 py-3 font-medium text-orange-700 transition-colors hover:bg-orange-100 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-500"
        >
          <Lock size={18} />
          {carregando ? "Alterando senha..." : "Alterar senha"}
        </button>
      </form>
    </div>
  );
}
