import { useEffect, useLayoutEffect } from "react";
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import { Layout } from "@/components/Layout";
import { RotaProtegida } from "@/components/RotaProtegida";
import { Cadastro } from "@/pages/Cadastro";
import { CriarReceita } from "@/pages/CriarReceita";
import { EditarReceita } from "@/pages/EditarReceita";
import { DetalheReceita } from "@/pages/DetalheDeReceita";
import { EditarPerfil } from "@/pages/EditarPerfil";
import { Favoritos } from "@/pages/Favoritos";
import { Home } from "@/pages/Home";
import { Login } from "@/pages/Login";
import { MinhasReceitas } from "@/pages/MinhasReceitas";
import { NaoEncontrado } from "@/pages/NaoEncontrado";
import { Perfil } from "@/pages/Perfil";
import { ResultadosBusca } from "@/pages/ResultadosBusca";

function RotasComScrollTop() {
  const { pathname } = useLocation();

  useEffect(() => {
    if (!("scrollRestoration" in window.history)) {
      return;
    }

    const valorAnterior = window.history.scrollRestoration;
    window.history.scrollRestoration = "manual";

    return () => {
      window.history.scrollRestoration = valorAnterior;
    };
  }, []);

  useLayoutEffect(() => {
    if (window.scrollY === 0) {
      return;
    }

    window.scrollTo(0, 0);
  }, [pathname]);

  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Home />} />
        <Route path="/busca" element={<ResultadosBusca />} />
        <Route path="/receita/:id" element={<DetalheReceita />} />
        <Route
          path="/minhas-receitas"
          element={
            <RotaProtegida>
              <MinhasReceitas />
            </RotaProtegida>
          }
        />
        <Route
          path="/favoritos"
          element={
            <RotaProtegida>
              <Favoritos />
            </RotaProtegida>
          }
        />
        <Route
          path="/criar-receita"
          element={
            <RotaProtegida>
              <CriarReceita />
            </RotaProtegida>
          }
        />
        <Route
          path="/editar-receita/:id"
          element={
            <RotaProtegida>
              <EditarReceita />
            </RotaProtegida>
          }
        />
        <Route
          path="/perfil"
          element={
            <RotaProtegida>
              <Perfil />
            </RotaProtegida>
          }
        />
      </Route>

      <Route
        path="/editar-perfil"
        element={
          <RotaProtegida>
            <EditarPerfil />
          </RotaProtegida>
        }
      />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="*" element={<NaoEncontrado />} />
    </Routes>
  );
}

export function AppRoutes() {
  return (
    <BrowserRouter>
      <RotasComScrollTop />
    </BrowserRouter>
  );
}
