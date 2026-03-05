import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Layout } from "@/components/Layout";
import { Home } from "@/pages/Home";
import { ResultadosBusca } from "@/pages/ResultadosBusca";
import { DetalheReceita } from "@/pages/DetalheDeReceita";
import { Login } from "@/pages/Login";
import { Cadastro } from "@/pages/Cadastro";
import { MinhasReceitas } from "@/pages/MinhasReceitas";
import { Favoritos } from "@/pages/Favoritos";
import { CriarReceita } from "@/pages/CriarReceita";
import { Perfil } from "@/pages/Perfil";
import { EditarPerfil } from "@/pages/EditarPerfil";
import { RotaProtegida } from "@/components/RotaProtegida";

export function AppRoutes() {
  return (
    <BrowserRouter>
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
        <Route path="*" element={<div>Página não encontrada</div>} />
      </Routes>
    </BrowserRouter>
  );
}
