import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";
import type { ReactNode } from "react";
import type { Receita } from "@/types";
import { useAuth } from "./AuthContext";
import { favoritoService } from "@/services/favoritoService";
import { receitaService } from "@/services/receitaService";

interface FavoritesContextData {
  favoritos: Receita[];
  carregandoFavoritos: boolean;
  toggleFavorite: (receita: Receita) => Promise<boolean>;
  isFavorite: (id: Receita["id"]) => boolean;
  recarregarFavoritos: () => Promise<void>;
}

const FavoritesContext = createContext<FavoritesContextData>(
  {} as FavoritesContextData,
);

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

      const receitasPossiveis = await Promise.all(
        idsFavoritos.map(async (id) => {
          try {
            return await receitaService.buscarPorId(id);
          } catch {
            return null;
          }
        }),
      );

      const receitasValidas = receitasPossiveis.filter(
        (receita): receita is Receita => receita !== null,
      );

      setFavoritos(receitasValidas);
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
    (id: Receita["id"]) => {
      return favoritos.some((receita) => String(receita.id) === String(id));
    },
    [favoritos],
  );

  const toggleFavorite = useCallback(
    async (receita: Receita) => {
      const jaFavorita = isFavorite(receita.id);

      if (jaFavorita) {
        await favoritoService.remover(receita.id);
        setFavoritos((favoritosAtuais) =>
          favoritosAtuais.filter(
            (favorita) => String(favorita.id) !== String(receita.id),
          ),
        );
        return false;
      }

      await favoritoService.adicionar(receita.id);
      setFavoritos((favoritosAtuais) => [...favoritosAtuais, receita]);
      return true;
    },
    [isFavorite],
  );

  const valor = useMemo(
    () => ({
      favoritos,
      carregandoFavoritos,
      toggleFavorite,
      isFavorite,
      recarregarFavoritos,
    }),
    [
      favoritos,
      carregandoFavoritos,
      toggleFavorite,
      isFavorite,
      recarregarFavoritos,
    ],
  );

  return (
    <FavoritesContext.Provider value={valor}>
      {children}
    </FavoritesContext.Provider>
  );
}

export function useFavorites() {
  return useContext(FavoritesContext);
}
