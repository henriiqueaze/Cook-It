const base_URL = import.meta.env.VITE_API_URL;

type QueryValue = string | number | boolean | null | undefined;

function montarUrl(endpoint: string, params?: Record<string, QueryValue>) {
  const urlBase = `${base_URL}${endpoint}`;

  if (!params) return urlBase;

  const searchParams = new URLSearchParams();

  for (const [chave, valor] of Object.entries(params)) {
    if (valor === undefined || valor === null || valor === "") continue;
    searchParams.set(chave, String(valor));
  }

  const query = searchParams.toString();
  return query ? `${urlBase}${urlBase.includes("?") ? "&" : "?"}${query}` : urlBase;
}

async function parseResposta<T>(resposta: Response): Promise<T> {
  if (resposta.status === 204) return undefined as T;

  const texto = await resposta.text();
  if (!texto) return undefined as T;

  return JSON.parse(texto) as T;
}

async function requisicao<T>(
  endpoint: string,
  opcoes?: RequestInit,
  params?: Record<string, QueryValue>,
): Promise<T> {
  const token = localStorage.getItem("token");

  const resposta = await fetch(montarUrl(endpoint, params), {
    ...opcoes,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...opcoes?.headers,
    },
  });

  if (!resposta.ok) {
    let mensagem = `Erro ${resposta.status}`;

    try {
      const erro = await resposta.json();
      mensagem = erro.error || erro.message || mensagem;
    } catch {
    }

    throw new Error(mensagem);
  }

  return parseResposta<T>(resposta);
}

export const api = {
  get: <T>(endpoint: string, params?: Record<string, QueryValue>) =>
    requisicao<T>(endpoint, undefined, params),

  post: <T>(endpoint: string, corpo: unknown) =>
    requisicao<T>(endpoint, {
      method: "POST",
      body: JSON.stringify(corpo),
    }),

  put: <T>(endpoint: string, corpo: unknown) =>
    requisicao<T>(endpoint, {
      method: "PUT",
      body: JSON.stringify(corpo),
    }),

  delete: <T>(endpoint: string) =>
    requisicao<T>(endpoint, { method: "DELETE" }),
};
