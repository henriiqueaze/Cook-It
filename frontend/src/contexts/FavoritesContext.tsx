import { createContext, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";
import type { Receita } from "@/types";
import { useAuth } from "./AuthContext";
import { favoritoService } from "@/services/favoritoService";

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

  async function recarregarFavoritos() {
    if (!estaAutenticado) {
      setFavoritos([]);
      return;
    }

    setCarregandoFavoritos(true);
    try {
      const lista = await favoritoService.listar();
      setFavoritos(Array.isArray(lista) ? lista : []);
    } catch {
      setFavoritos([]);
    } finally {
      setCarregandoFavoritos(false);
    }
  }

  useEffect(() => {
    void recarregarFavoritos();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [estaAutenticado]);

  function isFavorite(id: Receita["id"]) {
    return favoritos.some((receita) => String(receita.id) === String(id));
  }

  async function toggleFavorite(receita: Receita) {
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
    setFavoritos((favoritosAtuais) => [
      ...favoritosAtuais,
      { ...receita, favoritada: true },
    ]);
    return true;
  }

  const valor = useMemo(
    () => ({
      favoritos,
      carregandoFavoritos,
      toggleFavorite,
      isFavorite,
      recarregarFavoritos,
    }),
    [favoritos, carregandoFavoritos],
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
