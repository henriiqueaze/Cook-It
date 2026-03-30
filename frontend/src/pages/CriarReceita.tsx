import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, ChevronDown, Plus, X } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { receitaService } from "@/services/receitaService";
import { UnidadeMedida, UnidadeMedidaLabel } from "../enums/UnidadeMedida";

export function CriarReceita() {
  const navigate = useNavigate();
  const { estaAutenticado } = useAuth();

  const [titulo, setTitulo] = useState("");
  const [imagemUrl, setImagemUrl] = useState("");
  const [descricao, setDescricao] = useState("");
  const [tempoPreparo, setTempoPreparo] = useState("");
  const [porcoes, setPorcoes] = useState("");
  const [instrucoes, setInstrucoes] = useState<string[]>([""]);
  const [ingredientes, setIngredientes] = useState<
    { nome: string; quantidade: string; unidade: UnidadeMedida | "" }[]
  >([{ nome: "", quantidade: "", unidade: "" }]);
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  function adicionarIngrediente() {
    setIngredientes([
      ...ingredientes,
      { nome: "", quantidade: "", unidade: "" },
    ]);
  }

  function removerIngrediente(index: number) {
    setIngredientes(ingredientes.filter((_, i) => i !== index));
  }

  function atualizarIngrediente(index: number, campo: string, valor: string) {
    const novos = [...ingredientes];
    novos[index] = { ...novos[index], [campo]: valor };
    setIngredientes(novos);
  }

  function adicionarInstrucao() {
    setInstrucoes([...instrucoes, ""]);
  }

  function removerInstrucao(index: number) {
    setInstrucoes(instrucoes.filter((_, i) => i !== index));
  }

  function atualizarInstrucao(index: number, valor: string) {
    const novas = [...instrucoes];
    novas[index] = valor;
    setInstrucoes(novas);
  }

  async function handleSalvar() {
    if (!titulo || !descricao || !tempoPreparo || !porcoes) {
      setErro("Preencha todos os campos, não deixe faltar.");
      return;
    }

    if (ingredientes.some((i) => !i.nome || !i.quantidade || !i.unidade)) {
      setErro("Preencha todos os campos dos ingredientes.");
      return;
    }

    if (instrucoes.some((i) => !i.trim())) {
      setErro("Preencha todas as instruções.");
      return;
    }

    setCarregando(true);
    setErro("");

    try {
      await receitaService.criar({
        titulo,
        descricao,
        imagemUrl,
        tempoPreparo: Number(tempoPreparo),
        porcoes: Number(porcoes),
        ingredientes,
        instrucoes,
      });
      navigate("/minhas-receitas");
    } catch (error: any) {
      console.error("ERRO COMPLETO:", error);
      setErro("Erro ao salvar receita. Tente novamente.");
    } finally {
      setCarregando(false);
    }
  }

  if (!estaAutenticado) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-6 text-center">
        <h2 className="text-lg font-semibold text-gray-500">
          Você precisa estar logado!
        </h2>
        <p className="text-sm text-gray-400 mt-1 mb-6">
          Entre na sua conta para criar receitas!
        </p>
        <button
          onClick={() => navigate("/login")}
          className="bg-orange-600 text-white px-6 py-3 rounded-lg font-medium cursor-pointer"
        >
          Fazer login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 text-white pt-12 pb-6 px-6 rounded-b-3xl flex items-center gap-3 shadow-lg">
        <button onClick={() => navigate(-1)} className="cursor-pointer">
          <ArrowLeft size={22} />
        </button>
        <h1 className="text-xl font-bold">Criar Receita</h1>
      </div>

      <div className="px-6 mt-6 space-y-4">
        {erro && (
          <div className="bg-red-50 text-red-600 text-sm px-4 py-3 rounded-lg">
            {erro}
          </div>
        )}

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Informações básicas</h2>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Título *
            </label>
            <input
              type="text"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              placeholder="Ex: Frango grelhado com legumes"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descrição *
            </label>
            <textarea
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              placeholder="Descreva brevemente sua receita..."
              rows={3}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm resize-none"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              URL da Imagem
            </label>
            <input
              type="text"
              value={imagemUrl}
              onChange={(e) => setImagemUrl(e.target.value)}
              placeholder="https://exemplo.com/imagem.jpg"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
            />
            {imagemUrl && (
              <img
                src={imagemUrl}
                alt="Preview"
                className="mt-2 w-full h-40 object-cover rounded-lg"
              />
            )}
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Tempo (min) *
              </label>
              <input
                type="number"
                value={tempoPreparo}
                onChange={(e) => setTempoPreparo(e.target.value)}
                placeholder="Ex: 30"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Porções *
              </label>
              <input
                type="number"
                value={porcoes}
                onChange={(e) => setPorcoes(e.target.value)}
                placeholder="Ex: 4"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
              />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Ingredientes</h2>

          {ingredientes.map((ing, index) => (
            <div key={index} className="flex gap-2 items-start">
              <div className="flex-1 grid grid-cols-3 gap-2">
                <input
                  type="text"
                  value={ing.nome}
                  onChange={(e) =>
                    atualizarIngrediente(index, "nome", e.target.value)
                  }
                  placeholder="Nome"
                  className="col-span-1 px-3 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
                />
                <input
                  type="number"
                  value={ing.quantidade}
                  onChange={(e) =>
                    atualizarIngrediente(index, "quantidade", e.target.value)
                  }
                  placeholder="Quantidade (ex: 1, 2, 500, etc)"
                  className="px-3 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm"
                />
                <div className="relative">
                  <select
                    value={ing.unidade}
                    onChange={(e) =>
                      atualizarIngrediente(index, "unidade", e.target.value)
                    }
                    className={`w-full appearance-none px-3 py-2 pr-9 border rounded-lg outline-none text-sm transition-all bg-white hover:border-orange-300 focus:ring-2 focus:ring-orange-500 focus:border-orange-400 ${
                      ing.unidade
                        ? "text-gray-800 border-gray-300"
                        : "text-gray-400 border-gray-200"
                    }`}
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
                  onClick={() => removerIngrediente(index)}
                  className="text-red-400 cursor-pointer mt-2"
                >
                  <X size={18} />
                </button>
              )}
            </div>
          ))}

          <button
            onClick={adicionarIngrediente}
            className="flex items-center gap-2 text-orange-600 text-sm font-medium cursor-pointer"
          >
            <Plus size={16} />
            Adicionar ingrediente
          </button>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-4 space-y-3">
          <h2 className="font-semibold text-gray-800">Modo de Preparo</h2>

          {instrucoes.map((instrucao, index) => (
            <div key={index} className="flex gap-2 items-start">
              <span className="shrink-0 w-6 h-6 bg-orange-600 text-white rounded-full flex items-center justify-center text-xs font-bold mt-2">
                {index + 1}
              </span>
              <textarea
                value={instrucao}
                onChange={(e) => atualizarInstrucao(index, e.target.value)}
                placeholder={`Passo ${index + 1}...`}
                rows={2}
                className="flex-1 px-3 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-orange-500 text-sm resize-none"
              />
              {instrucoes.length > 1 && (
                <button
                  onClick={() => removerInstrucao(index)}
                  className="text-red-400 cursor-pointer mt-2"
                >
                  <X size={18} />
                </button>
              )}
            </div>
          ))}

          <button
            onClick={adicionarInstrucao}
            className="flex items-center gap-2 text-orange-600 text-sm font-medium cursor-pointer"
          >
            <Plus size={16} />
            Adicionar passo
          </button>
        </div>

        <button
          onClick={handleSalvar}
          disabled={carregando}
          className="w-full bg-linear-to-r from-orange-500 to-orange-600 text-white py-3 rounded-lg font-medium hover:from-orange-600 hover:to-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all cursor-pointer"
        >
          {carregando ? "Salvando..." : "Salvar Receita"}
        </button>
      </div>
    </div>
  );
}
