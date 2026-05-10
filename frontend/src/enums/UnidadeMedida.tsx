/* eslint-disable react-refresh/only-export-components */

export const UnidadeMedida = {
  GRAMA: "GRAMA",
  QUILOGRAMA: "QUILOGRAMA",
  MILILITRO: "MILILITRO",
  LITRO: "LITRO",
  COLHER_DE_SOPA: "COLHER_DE_SOPA",
  COLHER_DE_CHA: "COLHER_DE_CHA",
  XICARA: "XICARA",
  PITADA: "PITADA",
  UNIDADE: "UNIDADE",
  FATIA: "FATIA",
  PACOTE: "PACOTE",
} as const;

export type UnidadeMedida = (typeof UnidadeMedida)[keyof typeof UnidadeMedida];

export const UnidadeMedidaLabel: Record<UnidadeMedida, string> = {
  GRAMA: "g",
  QUILOGRAMA: "kg",
  MILILITRO: "ml",
  LITRO: "L",
  COLHER_DE_SOPA: "colher (sopa)",
  COLHER_DE_CHA: "colher (chá)",
  XICARA: "xícara",
  PITADA: "pitada",
  UNIDADE: "un",
  FATIA: "fatia",
  PACOTE: "pacote",
};
