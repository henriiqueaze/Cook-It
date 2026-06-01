import { useEffect, useState } from "react";
import type { ChangeEvent } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, ChevronDown, Plus, X } from "lucide-react";
import { useAuth } from "@/contexts/AuthContext";
import {
  UnidadeMedida,
  UnidadeMedidaLabel,
  type UnidadeMedida as UnidadeMedidaType,
} from "@/enums/UnidadeMedida";
import { receitaService } from "@/services/receitaService";

interface IngredienteForm {
  nome: string;
  quantidade: string;
  unidade: UnidadeMedidaType | "";
}

function criarIngredienteVazio(): IngredienteForm {
  return { nome: "", quantidade: "", unidade: "" };
}

export function CriarReceita() {
  const navigate = useNavigate();
  const { estaAutenticado } = useAuth();

  const [titulo, setTitulo] = useState("");
  const [imagemArquivo, setImagemArquivo] = useState<File | null>(null);
  const [descricao, setDescricao] = useState("");
  const [tempoPreparo, setTempoPreparo] = useState("");
  const [porcoes, setPorcoes] = useState("1");
  const [instrucoes, setInstrucoes] = useState<string[]>([""]);
  const [ingredientes, setIngredientes] = useState<IngredienteForm[]>([
    criarIngredienteVazio(),
  ]);
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  const [previewImagem, setPreviewImagem] = useState("");

  useEffect(() => {
    if (!imagemArquivo) {
      setPreviewImagem("");
      return;
    }

    const url = URL.createObjectURL(imagemArquivo);
    setPreviewImagem(url);

    return () => URL.revokeObjectURL(url);
  }, [imagemArquivo]);

  function adicionarIngrediente() {
    setIngredientes((current) => [...current, criarIngredienteVazio()]);
  }

  function removerIngrediente(index: number) {
    setIngredientes((current) =>
      current.filter((_, itemIndex) => itemIndex !== index),
    );
  }

  function atualizarIngrediente(
    index: number,
    campo: keyof IngredienteForm,
    valor: string,
  ) {
    setIngredientes((current) =>
      current.map((item, itemIndex) =>
        itemIndex === index ? { ...item, [campo]: valor } : item,
      ),
    );
  }

  function adicionarInstrucao() {
    setInstrucoes((current) => [...current, ""]);
  }

  function removerInstrucao(index: number) {
    setInstrucoes((current) =>
      current.filter((_, itemIndex) => itemIndex !== index),
    );
  }

  function atualizarInstrucao(index: number, valor: string) {
    setInstrucoes((current) =>
      current.map((item, itemIndex) => (itemIndex === index ? valor : item)),
    );
  }

  function handleImagemChange(event: ChangeEvent<HTMLInputElement>) {
    const arquivo = event.target.files?.[0] ?? null;

    if (arquivo && !arquivo.type.startsWith("image/")) {
      setErro("Envie apenas arquivos de imagem.");
      event.target.value = "";
      setImagemArquivo(null);
      return;
    }

    setErro("");
    setImagemArquivo(arquivo);
  }

  async function handleSalvar() {
    const tituloLimpo = titulo.trim();
    const descricaoLimpa = descricao.trim();
    const tempo = Number(tempoPreparo);
    const totalPorcoes = Number(porcoes);

    if (!tituloLimpo || !tempo || !totalPorcoes) {
      setErro("Preencha todos os campos.");
      return;
    }

    if (
      ingredientes.some(
        (item) => !item.nome.trim() || !item.quantidade.trim() || !item.unidade,
      )
    ) {
      setErro("Preencha todos os campos dos ingredientes.");
      return;
    }

    if (instrucoes.some((item) => !item.trim())) {
      setErro("Preencha todas as instruções.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      await receitaService.criar({
        titulo: tituloLimpo,
        descricao: descricaoLimpa,
        imagemArquivo,
        tempoPreparo: tempo,
        porcoes: totalPorcoes,
        ingredientes,
        instrucoes: instrucoes.map((item) => item.trim()),
      });

      navigate("/minhas-receitas");
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Erro ao salvar receita. Tente novamente.",
      );
    } finally {
      setCarregando(false);
    }
  }

  if (!estaAutenticado) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center px-6 text-center">
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado
        </h2>
        <p className="mt-1 mb-6 text-sm text-gray-400">
          Entre na sua conta para criar receitas
        </p>
        <button
          type="button"
          onClick={() => navigate("/login")}
          className="rounded-lg bg-orange-600 px-6 py-3 font-medium text-white transition-colors hover:bg-orange-700"
        >
          Fazer login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="flex items-center gap-3 rounded-b-3xl bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-6 pb-6 pt-12 text-white shadow-lg">
        <button type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft size={22} />
        </button>
        <h1 className="text-xl font-bold">Criar receita</h1>
      </div>

      <div className="mt-6 space-y-4 px-6 pb-6">
        {erro && (
          <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">
            {erro}
          </div>
        )}

        <section className="space-y-3 rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800">Informações básicas</h2>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Título *
            </label>
            <input
              type="text"
              value={titulo}
              onChange={(event) => setTitulo(event.target.value)}
              placeholder="Ex: Frango grelhado com legumes"
              className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Descrição
            </label>
            <textarea
              value={descricao}
              onChange={(event) => setDescricao(event.target.value)}
              placeholder="Descreva brevemente sua receita..."
              rows={3}
              className="w-full resize-none rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Imagem da receita
            </label>

            <label className="flex w-full cursor-pointer items-center justify-center rounded-lg border border-dashed border-gray-300 px-4 py-3 transition-colors hover:border-orange-400 hover:bg-orange-50">
              <span className="text-sm text-gray-600">
                {imagemArquivo ? "Trocar imagem" : "Selecionar imagem"}
              </span>
              <input
                type="file"
                accept="image/*"
                onChange={handleImagemChange}
                className="hidden"
              />
            </label>

            {imagemArquivo && (
              <div className="mt-2 flex items-center justify-between gap-3 rounded-lg border border-gray-200 bg-gray-50 px-3 py-2">
                <div className="min-w-0">
                  <p className="truncate text-sm font-medium text-gray-700">
                    {imagemArquivo.name}
                  </p>
                  <p className="text-xs text-gray-500">
                    {(imagemArquivo.size / 1024 / 1024).toFixed(2)} MB
                  </p>
                </div>

                <button
                  type="button"
                  onClick={() => setImagemArquivo(null)}
                  className="text-red-500"
                  aria-label="Remover imagem"
                >
                  <X size={18} />
                </button>
              </div>
            )}

            {previewImagem && (
              <img
                src={previewImagem}
                alt="Preview"
                className="mt-2 h-40 w-full rounded-lg object-cover"
              />
            )}
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">
                Tempo (min) *
              </label>
              <input
                type="number"
                min={1}
                value={tempoPreparo}
                onChange={(event) => setTempoPreparo(event.target.value)}
                placeholder="Ex: 30"
                className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">
                Porções *
              </label>
              <input
                type="number"
                min={1}
                value={porcoes}
                onChange={(event) => setPorcoes(event.target.value)}
                placeholder="Ex: 4"
                className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>
        </section>

        <section className="space-y-3 rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800">Ingredientes</h2>

          {ingredientes.map((ingrediente, index) => (
            <div key={index} className="flex items-start gap-2">
              <div className="grid flex-1 grid-cols-3 gap-2">
                <input
                  type="text"
                  value={ingrediente.nome}
                  onChange={(event) =>
                    atualizarIngrediente(index, "nome", event.target.value)
                  }
                  placeholder="Nome"
                  className="col-span-1 rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
                />

                <input
                  type="number"
                  value={ingrediente.quantidade}
                  onChange={(event) =>
                    atualizarIngrediente(
                      index,
                      "quantidade",
                      event.target.value,
                    )
                  }
                  placeholder="Quantidade"
                  className="rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
                />

                <div className="relative">
                  <select
                    value={ingrediente.unidade}
                    onChange={(event) =>
                      atualizarIngrediente(index, "unidade", event.target.value)
                    }
                    className={`w-full appearance-none rounded-lg border px-3 py-2 pr-9 text-sm outline-none transition ${
                      ingrediente.unidade
                        ? "border-gray-300 text-gray-800"
                        : "border-gray-200 text-gray-400"
                    } focus:border-orange-400 focus:ring-2 focus:ring-orange-500 bg-white`}
                  >
                    <option value="" disabled>
                      Unidade
                    </option>
                    {Object.values(UnidadeMedida).map((unidade) => (
                      <option
                        key={unidade}
                        value={unidade}
                        className="text-gray-800"
                      >
                        {UnidadeMedidaLabel[unidade]}
                      </option>
                    ))}
                  </select>
                  <ChevronDown
                    size={16}
                    className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
                  />
                </div>
              </div>

              {ingredientes.length > 1 && (
                <button
                  type="button"
                  onClick={() => removerIngrediente(index)}
                  className="mt-2 text-red-400"
                  aria-label="Remover ingrediente"
                >
                  <X size={18} />
                </button>
              )}
            </div>
          ))}

          <button
            type="button"
            onClick={adicionarIngrediente}
            className="inline-flex items-center gap-2 text-sm font-medium text-orange-600"
          >
            <Plus size={16} />
            Adicionar ingrediente
          </button>
        </section>

        <section className="space-y-3 rounded-2xl bg-white p-4 shadow-sm">
          <h2 className="font-semibold text-gray-800">Modo de preparo</h2>

          {instrucoes.map((instrucao, index) => (
            <div key={index} className="flex items-start gap-2">
              <span className="mt-2 flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-orange-600 text-xs font-bold text-white">
                {index + 1}
              </span>

              <textarea
                value={instrucao}
                onChange={(event) =>
                  atualizarInstrucao(index, event.target.value)
                }
                placeholder={`Passo ${index + 1}...`}
                rows={2}
                className="flex-1 resize-none rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none transition focus:ring-2 focus:ring-orange-500"
              />

              {instrucoes.length > 1 && (
                <button
                  type="button"
                  onClick={() => removerInstrucao(index)}
                  className="mt-2 text-red-400"
                  aria-label="Remover passo"
                >
                  <X size={18} />
                </button>
              )}
            </div>
          ))}

          <button
            type="button"
            onClick={adicionarInstrucao}
            className="inline-flex items-center gap-2 text-sm font-medium text-orange-600"
          >
            <Plus size={16} />
            Adicionar passo
          </button>
        </section>

        <button
          type="button"
          onClick={handleSalvar}
          disabled={carregando}
          className="w-full cursor-pointer rounded-lg bg-linear-to-r from-orange-500 to-orange-600 py-3 font-medium text-white transition-all hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:bg-gray-300"
        >
          {carregando ? "Salvando..." : "Salvar receita"}
        </button>
      </div>
    </div>
  );
}
