interface LanguageToolReplacement {
  value?: string;
}

interface LanguageToolMatch {
  shortMessage?: string;
  message?: string;
  offset: number;
  length: number;
  replacements?: LanguageToolReplacement[];
  rule?: {
    issueType?: string;
  };
}

interface LanguageToolResponse {
  matches?: LanguageToolMatch[];
}

export interface SugestaoCorrecao {
  original: string;
  sugestao: string;
  inicio: number;
  fim: number;
}

const LANGUAGE_TOOL_URL = "https://api.languagetool.org/v2/check";

export async function buscarCorrecaoPortugues(
  texto: string,
): Promise<SugestaoCorrecao | null> {
  const textoLimpo = texto.trim();

  if (textoLimpo.length < 4) {
    return null;
  }

  const body = new URLSearchParams({
    text: texto,
    language: "pt-BR",
  });

  const response = await fetch(LANGUAGE_TOOL_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body,
  });

  if (!response.ok) {
    return null;
  }

  const data = (await response.json()) as LanguageToolResponse;
  const primeiraCorrecao = data.matches?.find((match) => {
    const sugestao = match.replacements?.[0]?.value?.trim();

    return (
      Boolean(sugestao) &&
      (match.rule?.issueType === "misspelling" ||
        match.shortMessage?.toLowerCase().includes("erro") ||
        match.message?.toLowerCase().includes("ortografia"))
    );
  });

  const sugestao = primeiraCorrecao?.replacements?.[0]?.value?.trim();

  if (!primeiraCorrecao || !sugestao) {
    return null;
  }

  return {
    original: texto.slice(
      primeiraCorrecao.offset,
      primeiraCorrecao.offset + primeiraCorrecao.length,
    ),
    sugestao,
    inicio: primeiraCorrecao.offset,
    fim: primeiraCorrecao.offset + primeiraCorrecao.length,
  };
}
