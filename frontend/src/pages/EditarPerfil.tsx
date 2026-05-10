import { useEffect, useState, type ChangeEvent, type FormEvent } from "react";
import {
  ArrowLeft,
  Camera,
  Lock,
  Mail,
  Save,
  Trash2,
  User,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { useAuth } from "@/contexts/AuthContext";
import { authService } from "@/services/authService";
import { validarEmail } from "@/lib/utils";

function getMensagemErroExclusao(error: unknown) {
  const mensagemPadrao = "Não foi possível validar a senha. Tente novamente.";

  if (!(error instanceof Error) || !error.message) {
    return mensagemPadrao;
  }

  const mensagem = error.message.toLowerCase();

  if (
    mensagem.includes("senha incorreta") ||
    mensagem.includes("senha inválida") ||
    mensagem.includes("senha invalida") ||
    mensagem.includes("e-mail ou senha inválidos") ||
    mensagem.includes("email ou senha invalidos") ||
    mensagem.includes("invalid password") ||
    mensagem.includes("invalid credentials") ||
    mensagem.includes("401")
  ) {
    return "Senha incorreta. Verifique e tente novamente.";
  }

  if (mensagem.includes("failed to fetch") || mensagem.includes("network")) {
    return "Não foi possível conectar ao servidor. Tente novamente.";
  }

  if (mensagem.includes("500")) {
    return mensagemPadrao;
  }

  return error.message;
}

export function EditarPerfil() {
  const navigate = useNavigate();
  const { usuario, atualizarUsuario, sair } = useAuth();
  const [carregandoPerfil, setCarregandoPerfil] = useState(false);
  const [apagandoPerfil, setApagandoPerfil] = useState(false);
  const [validandoSenhaExclusao, setValidandoSenhaExclusao] = useState(false);
  const [modalExclusaoAberto, setModalExclusaoAberto] = useState(false);
  const [etapaExclusao, setEtapaExclusao] = useState<"senha" | "confirmar">(
    "senha",
  );
  const [senhaExclusao, setSenhaExclusao] = useState("");
  const [erroExclusao, setErroExclusao] = useState("");
  const [fotoArquivo, setFotoArquivo] = useState<File | null>(null);
  const [formData, setFormData] = useState({
    nome: usuario?.name ?? "",
    email: usuario?.email ?? "",
  });

  const [fotoPreview, setFotoPreview] = useState(usuario?.photo ?? "");

  useEffect(() => {
    if (!fotoArquivo) {
      setFotoPreview(usuario?.photo ?? "");
      return;
    }

    const url = URL.createObjectURL(fotoArquivo);
    setFotoPreview(url);

    return () => URL.revokeObjectURL(url);
  }, [fotoArquivo, usuario?.photo]);

  function handleFotoChange(event: ChangeEvent<HTMLInputElement>) {
    const arquivo = event.target.files?.[0] ?? null;
    setFotoArquivo(arquivo);
  }

  function handleInputChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  }

  function abrirExclusao() {
    setSenhaExclusao("");
    setErroExclusao("");
    setEtapaExclusao("senha");
    setModalExclusaoAberto(true);
  }

  function fecharExclusao() {
    if (apagandoPerfil) {
      return;
    }

    setModalExclusaoAberto(false);
    setSenhaExclusao("");
    setErroExclusao("");
    setEtapaExclusao("senha");
  }

  async function continuarExclusao() {
    if (!usuario?.id) {
      toast.error("Usuário não encontrado");
      return;
    }

    if (!senhaExclusao.trim()) {
      setErroExclusao("Digite sua senha para continuar.");
      return;
    }

    setErroExclusao("");
    setValidandoSenhaExclusao(true);

    try {
      await authService.validarSenhaExclusao(usuario.id, senhaExclusao.trim());
      setEtapaExclusao("confirmar");
    } catch (error) {
      setErroExclusao(getMensagemErroExclusao(error));
    } finally {
      setValidandoSenhaExclusao(false);
    }
  }

  async function confirmarExclusao() {
    if (!usuario?.id) {
      toast.error("Usuário não encontrado");
      return;
    }

    setApagandoPerfil(true);

    try {
      await authService.excluirUsuario(usuario.id, senhaExclusao.trim());
      sair();
      toast.success("Perfil excluído com sucesso.");
      navigate("/login", { replace: true });
    } catch (error) {
      setEtapaExclusao("senha");
      setErroExclusao(getMensagemErroExclusao(error));
    } finally {
      setApagandoPerfil(false);
    }
  }

  async function handleSalvar(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!usuario?.id) {
      toast.error("Usuário não encontrado");
      return;
    }

    if (!validarEmail(formData.email)) {
      toast.error("Digite um e-mail válido");
      return;
    }

    setCarregandoPerfil(true);

    try {
      const atualizado = await authService.atualizarPerfil(usuario.id, {
        name: formData.nome.trim(),
        email: formData.email.trim(),
        photoFile: fotoArquivo,
      });

      atualizarUsuario(atualizado);
      toast.success("Perfil atualizado com sucesso!");
      navigate("/perfil");
    } catch {
      toast.error("Erro ao atualizar perfil. Tente novamente.");
    } finally {
      setCarregandoPerfil(false);
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
          <h1 className="text-xl font-semibold">Editar perfil</h1>
        </div>
      </div>

      <form onSubmit={handleSalvar} className="space-y-4 px-6 py-6">
        <div className="rounded-2xl bg-white p-6 text-center shadow-sm">
          <label className="cursor-pointer">
            <div className="relative mx-auto mb-3 h-32 w-32">
              {fotoPreview ? (
                <img
                  src={fotoPreview}
                  alt="Foto do perfil"
                  className="h-32 w-32 rounded-full border-4 border-gray-100 object-cover"
                />
              ) : (
                <div className="flex h-32 w-32 items-center justify-center rounded-full border-4 border-gray-100 bg-gray-100">
                  <User size={40} className="text-gray-400" />
                </div>
              )}
              <div className="absolute bottom-0 right-0 rounded-full bg-orange-600 p-2 shadow-lg">
                <Camera size={18} className="text-white" />
              </div>
            </div>
            <input
              type="file"
              accept="image/*"
              onChange={handleFotoChange}
              className="hidden"
            />
            <span className="text-sm font-medium text-orange-600">
              Alterar foto
            </span>
          </label>
        </div>

        <div className="space-y-3 rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800">Informações pessoais</h2>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              <User size={14} className="mr-1 inline" /> Nome
            </label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
              className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              <Mail size={14} className="mr-1 inline" /> E-mail
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
            />
          </div>
        </div>

        <button
          type="button"
          onClick={() => navigate("/alterar-senha")}
          className="flex w-full cursor-pointer items-center justify-center gap-2 rounded-lg border border-orange-200 bg-orange-50 py-3 font-medium text-orange-700 transition-colors hover:bg-orange-100"
        >
          <Lock size={18} />
          Alterar senha
        </button>

        <button
          type="button"
          onClick={abrirExclusao}
          className="flex w-full cursor-pointer items-center justify-center gap-2 rounded-lg border border-red-200 bg-red-50 py-3 font-medium text-red-700 transition-colors hover:bg-red-100"
        >
          <Trash2 size={18} />
          Apagar perfil
        </button>

        <button
          type="submit"
          disabled={carregandoPerfil}
          className="flex w-full cursor-pointer items-center justify-center gap-2 rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
        >
          <Save size={18} />
          {carregandoPerfil ? "Salvando..." : "Salvar alterações"}
        </button>
      </form>

      {modalExclusaoAberto && (
        <div
          className="fixed inset-0 z-50 flex items-end justify-center bg-black/50 px-4 py-6 sm:items-center"
          onClick={fecharExclusao}
        >
          <div
            className="w-full max-w-md rounded-3xl bg-white p-6 shadow-2xl"
            onClick={(event) => event.stopPropagation()}
            role="dialog"
            aria-modal="true"
            aria-labelledby="exclusao-perfil-titulo"
          >
            <div className="mb-4 flex items-center gap-3">
              <div className="rounded-full bg-red-100 p-3 text-red-600">
                <Trash2 size={20} />
              </div>
              <div>
                <h2
                  id="exclusao-perfil-titulo"
                  className="text-lg font-semibold text-gray-900"
                >
                  Apagar perfil
                </h2>
                <p className="text-sm text-gray-500">
                  Essa ação remove sua conta permanentemente.
                </p>
              </div>
            </div>

            {etapaExclusao === "senha" ? (
              <div className="space-y-4">
                <p className="text-sm text-gray-600">
                  Digite a sua senha de login para continuar.
                </p>

                {erroExclusao && (
                  <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
                    {erroExclusao}
                  </div>
                )}

                <div>
                  <label className="mb-1 block text-sm font-medium text-gray-700">
                    Senha
                  </label>
                  <input
                    type="password"
                    value={senhaExclusao}
                    onChange={(event) => setSenhaExclusao(event.target.value)}
                    className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
                    placeholder="••••••••"
                  />
                </div>

                <div className="flex gap-3">
                  <button
                    type="button"
                    onClick={fecharExclusao}
                    disabled={apagandoPerfil || validandoSenhaExclusao}
                    className="flex-1 rounded-lg border border-gray-300 bg-white py-3 font-medium text-gray-700 transition-colors hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-60"
                  >
                    Cancelar
                  </button>
                  <button
                    type="button"
                    onClick={continuarExclusao}
                    disabled={apagandoPerfil || validandoSenhaExclusao}
                    className="flex-1 rounded-lg bg-red-600 py-3 font-medium text-white transition-colors hover:bg-red-700 disabled:cursor-not-allowed disabled:bg-gray-300"
                  >
                    {validandoSenhaExclusao ? "Validando..." : "Continuar"}
                  </button>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                <p className="text-sm text-gray-600">
                  Deseja apagar mesmo? Depois disso, não será possível recuperar
                  seu perfil.
                </p>

                {erroExclusao && (
                  <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
                    {erroExclusao}
                  </div>
                )}

                <div className="flex gap-3">
                  <button
                    type="button"
                    onClick={() => setEtapaExclusao("senha")}
                    disabled={apagandoPerfil}
                    className="flex-1 rounded-lg border border-gray-300 bg-white py-3 font-medium text-gray-700 transition-colors hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-60"
                  >
                    Voltar
                  </button>
                  <button
                    type="button"
                    onClick={confirmarExclusao}
                    disabled={apagandoPerfil}
                    className="flex-1 rounded-lg bg-red-600 py-3 font-medium text-white transition-colors hover:bg-red-700 disabled:cursor-not-allowed disabled:bg-gray-300"
                  >
                    {apagandoPerfil ? "Apagando..." : "Sim, apagar"}
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
