import { createContext, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";
import type { Receita } from "@/types";

interface FavoritesContextData {
  favoritos: Receita[];
  toggleFavorite: (receita: Receita) => void;
  isFavorite: (id: Receita["id"]) => boolean;
}

const STORAGE_KEY = "cook-it:favoritos";

const FavoritesContext = createContext<FavoritesContextData>(
  {} as FavoritesContextData,
);

function carregarFavoritos(): Receita[] {
  const favoritosSalvos = localStorage.getItem(STORAGE_KEY);

  if (!favoritosSalvos) {
    return [];
  }

  try {
    const favoritos = JSON.parse(favoritosSalvos);
    return Array.isArray(favoritos) ? favoritos : [];
  } catch {
    return [];
  }
}

export function FavoritesProvider({ children }: { children: ReactNode }) {
  const [favoritos, setFavoritos] = useState<Receita[]>(carregarFavoritos);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(favoritos));
  }, [favoritos]);

  function isFavorite(id: Receita["id"]) {
    return favoritos.some((receita) => String(receita.id) === String(id));
  }

  function toggleFavorite(receita: Receita) {
    setFavoritos((favoritosAtuais) => {
      const favoritaJaExiste = favoritosAtuais.some(
        (favorita) => String(favorita.id) === String(receita.id),
      );

      if (favoritaJaExiste) {
        return favoritosAtuais.filter(
          (favorita) => String(favorita.id) !== String(receita.id),
        );
      }

      return [...favoritosAtuais, { ...receita, favoritada: true }];
    });
  }

  const valor = useMemo(
    () => ({
      favoritos,
      toggleFavorite,
      isFavorite,
    }),
    [favoritos],
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
