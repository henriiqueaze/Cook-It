const base_URL = import.meta.env.VITE_API_URL;

async function parseResposta<T>(resposta: Response): Promise<T> {
  if (resposta.status === 204) {
    return undefined as T;
  }

  const texto = await resposta.text();

  if (!texto) {
    return undefined as T;
  }

  return JSON.parse(texto) as T;
}

async function requisicao<T>(
  endpoint: string,
  opcoes?: RequestInit,
): Promise<T> {
  const token = localStorage.getItem("token");

  const resposta = await fetch(`${base_URL}${endpoint}`, {
    ...opcoes,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...opcoes?.headers,
    },
  });

  if (!resposta.ok) {
    throw new Error(`Erro ${resposta.status}: ${resposta.statusText}`);
  }

  return parseResposta<T>(resposta);
}

export const api = {
  get: <T>(endpoint: string) => requisicao<T>(endpoint),

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
