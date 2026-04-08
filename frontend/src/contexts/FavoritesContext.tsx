import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { favoritoService } from "@/services/favoritoService";
import { receitaService } from "@/services/receitaService";
import type { Receita } from "@/types";
import { useAuth } from "./AuthContext";

interface FavoritesContextData {
  favoritos: Receita[];
  carregandoFavoritos: boolean;
  toggleFavorite: (receita: Receita) => Promise<boolean>;
  isFavorite: (id: Receita["id"]) => boolean;
  recarregarFavoritos: () => Promise<void>;
}

const FavoritesContext = createContext<FavoritesContextData | undefined>(undefined);

export function FavoritesProvider({ children }: { children: ReactNode }) {
  const { estaAutenticado } = useAuth();
  const [favoritos, setFavoritos] = useState<Receita[]>([]);
  const [carregandoFavoritos, setCarregandoFavoritos] = useState(false);

  const recarregarFavoritos = useCallback(async () => {
    if (!estaAutenticado) {
      setFavoritos([]);
      setCarregandoFavoritos(false);
      return;
    }

    setCarregandoFavoritos(true);

    try {
      const idsFavoritos = await favoritoService.listar();
      const receitas = await Promise.allSettled(
        idsFavoritos.map((id) => receitaService.buscarPorId(id)),
      );

      setFavoritos(
        receitas
          .filter((resultado): resultado is PromiseFulfilledResult<Receita> => resultado.status === "fulfilled")
          .map((resultado) => resultado.value),
      );
    } catch {
      setFavoritos([]);
    } finally {
      setCarregandoFavoritos(false);
    }
  }, [estaAutenticado]);

  useEffect(() => {
    void recarregarFavoritos();
  }, [recarregarFavoritos]);

  const isFavorite = useCallback(
    (id: Receita["id"]) => favoritos.some((receita) => String(receita.id) === String(id)),
    [favoritos],
  );

  const toggleFavorite = useCallback(
    async (receita: Receita) => {
      const alreadyFavorite = isFavorite(receita.id);

      if (alreadyFavorite) {
        await favoritoService.remover(receita.id);
        setFavoritos((current) =>
          current.filter((item) => String(item.id) !== String(receita.id)),
        );
        return false;
      }

      await favoritoService.adicionar(receita.id);
      setFavoritos((current) => [...current, receita]);
      return true;
    },
    [isFavorite],
  );

  const value = useMemo(
    () => ({
      favoritos,
      carregandoFavoritos,
      toggleFavorite,
      isFavorite,
      recarregarFavoritos,
    }),
    [favoritos, carregandoFavoritos, toggleFavorite, isFavorite, recarregarFavoritos],
  );

  return (
    <FavoritesContext.Provider value={value}>{children}</FavoritesContext.Provider>
  );
}

export function useFavorites() {
  const context = useContext(FavoritesContext);

  if (!context) {
    throw new Error("useFavorites must be used within FavoritesProvider");
  }

  return context;
}
