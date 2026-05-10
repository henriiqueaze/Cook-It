const baseUrl = (import.meta.env.VITE_API_URL ?? "").replace(/\/+$/, "");

type QueryValue = string | number | boolean | null | undefined;

function mensagemParecePortugues(message: string) {
  const normalizada = message.toLowerCase();

  return (
    /[ãáàâéêíóôõúç]/i.test(message) ||
    /\b(não|nao|usuário|usuario|senha|e-mail|email|código|codigo|comentário|comentario|receita|favoritos|erro|encontramos|permissão|permissao|tente|campos|validar|redefinir|alterar|login|carregando)\b/.test(
      normalizada,
    )
  );
}

function traduzirMensagemErro(message: string, status: number) {
  const normalizada = message.toLowerCase();

  if (mensagemParecePortugues(message)) {
    return message;
  }

  if (
    normalizada.includes("failed to fetch") ||
    normalizada.includes("network")
  ) {
    return "Não foi possível conectar ao servidor. Tente novamente.";
  }

  if (
    normalizada.includes("invalid credentials") ||
    normalizada.includes("unauthorized") ||
    normalizada.includes("401")
  ) {
    return "E-mail ou senha inválidos.";
  }

  if (
    normalizada.includes("user not found") ||
    normalizada.includes("not found")
  ) {
    return "Não encontramos o recurso solicitado.";
  }

  if (normalizada.includes("forbidden") || normalizada.includes("403")) {
    return "Você não tem permissão para realizar esta ação.";
  }

  if (normalizada.includes("validation") || normalizada.includes("invalid")) {
    return "Os dados informados são inválidos.";
  }

  return `Erro ${status}. Tente novamente.`;
}

function buildUrl(endpoint: string, params?: Record<string, QueryValue>) {
  const url = `${baseUrl}${endpoint.startsWith("/") ? endpoint : `/${endpoint}`}`;

  if (!params) {
    return url;
  }

  const searchParams = new URLSearchParams();

  for (const [key, value] of Object.entries(params)) {
    if (value === undefined || value === null || value === "") {
      continue;
    }

    searchParams.set(key, String(value));
  }

  const query = searchParams.toString();
  return query ? `${url}${url.includes("?") ? "&" : "?"}${query}` : url;
}

async function parseResponse<T>(response: Response): Promise<T> {
  if (response.status === 204) {
    return undefined as T;
  }

  const text = await response.text();

  if (!text) {
    return undefined as T;
  }

  return JSON.parse(text) as T;
}

function buildHeaders(init?: RequestInit, skipAuth = false) {
  const headers = new Headers(init?.headers);
  const token = skipAuth ? null : localStorage.getItem("token");

  if (!headers.has("Content-Type") && !(init?.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  return headers;
}

async function request<T>(
  endpoint: string,
  init?: RequestInit,
  params?: Record<string, QueryValue>,
  skipAuth = false,
): Promise<T> {
  const response = await fetch(buildUrl(endpoint, params), {
    ...init,
    headers: buildHeaders(init, skipAuth),
  });

  if (!response.ok) {
    let message = `Erro ${response.status}`;

    try {
      const raw = await response.text();

      if (raw) {
        try {
          const error = JSON.parse(raw) as { message?: string; error?: string };
          message = traduzirMensagemErro(
            error.message || error.error || raw,
            response.status,
          );
        } catch {
          message = traduzirMensagemErro(raw, response.status);
        }
      }
    } catch {
      // Keep fallback status message when response body cannot be read.
    }

    throw new Error(message);
  }

  return parseResponse<T>(response);
}

export const api = {
  get: <T>(endpoint: string, params?: Record<string, QueryValue>) =>
    request<T>(endpoint, undefined, params),

  post: <T>(
    endpoint: string,
    body: unknown,
    init?: RequestInit,
    skipAuth = false,
  ) =>
    request<T>(
      endpoint,
      {
        method: "POST",
        body:
          body instanceof FormData || body instanceof Blob
            ? body
            : typeof body === "string"
              ? body
              : JSON.stringify(body),
        ...init,
      },
      undefined,
      skipAuth,
    ),

  put: <T>(
    endpoint: string,
    body: unknown,
    init?: RequestInit,
    skipAuth = false,
  ) =>
    request<T>(
      endpoint,
      {
        method: "PUT",
        body:
          body instanceof FormData || body instanceof Blob
            ? body
            : typeof body === "string"
              ? body
              : JSON.stringify(body),
        ...init,
      },
      undefined,
      skipAuth,
    ),

  delete: <T>(
    endpoint: string,
    body?: unknown,
    init?: RequestInit,
    skipAuth = false,
  ) =>
    request<T>(
      endpoint,
      {
        method: "DELETE",
        body:
          body instanceof FormData || body instanceof Blob
            ? body
            : body === undefined
              ? undefined
              : typeof body === "string"
                ? body
                : JSON.stringify(body),
        ...init,
      },
      undefined,
      skipAuth,
    ),
};
