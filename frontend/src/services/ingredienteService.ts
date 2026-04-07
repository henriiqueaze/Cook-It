import { api } from "./api";
import type { Id, Ingrediente } from "@/types";

interface BackendIngredientDTO {
  id?: Id;
  name?: string;
}

function mapBackendIngredientToFrontend(item: BackendIngredientDTO): Ingrediente {
  return {
    id: item.id ?? crypto.randomUUID(),
    nome: item.name ?? "",
    quantidade: "",
    unidade: "",
  };
}

export const ingredienteService = {
  listar: async () => {
    const resposta = await api.get<BackendIngredientDTO[]>("/ingredients");
    return resposta
      .map(mapBackendIngredientToFrontend)
      .filter((item) => item.nome.trim().length > 0);
  },

  buscar: async (termo: string) => {
    const resposta = await api.get<BackendIngredientDTO[]>("/ingredients/search", { q: termo });
    return resposta
      .map(mapBackendIngredientToFrontend)
      .filter((item) => item.nome.trim().length > 0);
  },
};