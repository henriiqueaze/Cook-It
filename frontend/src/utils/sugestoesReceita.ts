export function normalizarSugestaoReceita(valor: string) {
  return valor
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .toLowerCase()
    .trim()
    .replace(/\s+/g, " ");
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
        sugestaoNormalizada.startsWith(item.normalizado),
    )?.nome ??
    null
  );
}
