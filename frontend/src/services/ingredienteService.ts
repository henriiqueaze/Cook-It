import { api } from "./api";
import type { Ingrediente } from "@/types";

export const ingredienteService = {
  listar: () => api.get<Ingrediente[]>("/ingredients"),

  buscar: (termo: string) =>
    api.get<Ingrediente[]>("/ingredients/search", { q: termo }),
};
