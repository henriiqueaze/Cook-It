const base_URL = import.meta.env.VITE_API_URL;

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

  return resposta.json();
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
