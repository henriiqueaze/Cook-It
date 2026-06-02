export function normalizarSugestaoReceita(valor: string) {
  return valor
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .toLowerCase()
    .trim()
    .replace(/\s+/g, " ");
}

function compactar(valor: string) {
  return valor.replace(/\s+/g, "");
}

function calcularDistanciaLevenshtein(a: string, b: string) {
  if (a === b) {
    return 0;
  }

  if (!a.length) {
    return b.length;
  }

  if (!b.length) {
    return a.length;
  }

  let linhaAnterior = Array.from({ length: b.length + 1 }, (_, index) => index);

  for (let i = 1; i <= a.length; i += 1) {
    const linhaAtual = [i];

    for (let j = 1; j <= b.length; j += 1) {
      const custoSubstituicao = a[i - 1] === b[j - 1] ? 0 : 1;

      linhaAtual[j] = Math.min(
        linhaAtual[j - 1] + 1,
        linhaAnterior[j] + 1,
        linhaAnterior[j - 1] + custoSubstituicao,
      );
    }

    linhaAnterior = linhaAtual;
  }

  return linhaAnterior[b.length];
}

function limiteDistancia(valor: string) {
  if (valor.length <= 4) {
    return 1;
  }

  if (valor.length <= 7) {
    return 2;
  }

  return Math.min(4, Math.floor(valor.length * 0.35));
}

function variantesComparaveis(valor: string) {
  const palavras = valor.split(" ").filter((palavra) => palavra.length >= 3);

  return Array.from(new Set([valor, compactar(valor), ...palavras])).filter(
    Boolean,
  );
}

function calcularPontuacaoFuzzy(termo: string, opcao: string) {
  const variantesTermo = variantesComparaveis(termo);
  const variantesOpcao = variantesComparaveis(opcao);

  let melhorPontuacao = Number.POSITIVE_INFINITY;

  for (const varianteTermo of variantesTermo) {
    for (const varianteOpcao of variantesOpcao) {
      const distancia = calcularDistanciaLevenshtein(
        varianteTermo,
        varianteOpcao,
      );
      const tamanhoBase = Math.max(varianteTermo.length, varianteOpcao.length);

      if (
        distancia <= limiteDistancia(varianteTermo) &&
        distancia <= limiteDistancia(varianteOpcao)
      ) {
        melhorPontuacao = Math.min(
          melhorPontuacao,
          distancia / Math.max(tamanhoBase, 1),
        );
      }
    }
  }

  return melhorPontuacao;
}

export function encontrarOpcaoDeReceitaRelacionada(
  sugestao: string,
  opcoes: string[],
) {
  const sugestaoNormalizada = normalizarSugestaoReceita(sugestao);

  if (!sugestaoNormalizada) {
    return null;
  }

  const opcoesNormalizadas = opcoes.map((nome) => ({
    nome,
    normalizado: normalizarSugestaoReceita(nome),
  }));

  return (
    opcoesNormalizadas.find(
      (item) => item.normalizado === sugestaoNormalizada,
    )?.nome ??
    opcoesNormalizadas.find(
      (item) =>
        item.normalizado.startsWith(sugestaoNormalizada) ||
        sugestaoNormalizada.startsWith(item.normalizado) ||
        item.normalizado.includes(sugestaoNormalizada) ||
        sugestaoNormalizada.includes(item.normalizado),
    )?.nome ??
    opcoesNormalizadas
      .map((item) => ({
        ...item,
        pontuacao: calcularPontuacaoFuzzy(
          sugestaoNormalizada,
          item.normalizado,
        ),
      }))
      .filter((item) => Number.isFinite(item.pontuacao))
      .sort((a, b) => a.pontuacao - b.pontuacao)[0]?.nome ??
    null
  );
}
