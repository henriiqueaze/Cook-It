const baseUrl = (import.meta.env.VITE_API_URL ?? "").replace(/\/+$/, "");

type QueryValue = string | number | boolean | null | undefined;

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

function buildHeaders(init?: RequestInit) {
  const headers = new Headers(init?.headers);
  const token = localStorage.getItem("token");

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
): Promise<T> {
  const response = await fetch(buildUrl(endpoint, params), {
    ...init,
    headers: buildHeaders(init),
  });

  if (!response.ok) {
    let message = `Erro ${response.status}`;

    try {
      const raw = await response.text();

      if (raw) {
        try {
          const error = JSON.parse(raw) as { message?: string; error?: string };
          message = error.message || error.error || raw;
        } catch {
          message = raw;
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

  post: <T>(endpoint: string, body: unknown, init?: RequestInit) =>
    request<T>(endpoint, {
      method: "POST",
      body:
        body instanceof FormData || body instanceof Blob
          ? body
          : typeof body === "string"
            ? body
            : JSON.stringify(body),
      ...init,
    }),

  put: <T>(endpoint: string, body: unknown, init?: RequestInit) =>
    request<T>(endpoint, {
      method: "PUT",
      body:
        body instanceof FormData || body instanceof Blob
          ? body
          : typeof body === "string"
            ? body
            : JSON.stringify(body),
      ...init,
    }),

  delete: <T>(endpoint: string) => request<T>(endpoint, { method: "DELETE" }),
};
