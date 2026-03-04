import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Layout } from "@/components/Layout";
import { Home } from "@/pages/Home";
import { ResultadosBusca } from "@/pages/ResultadosBusca";
import { DetalheReceita } from "@/pages/DetalheDeReceita";
import { Login } from "@/pages/Login";
import { Cadastro } from "@/pages/Cadastro";

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/busca" element={<ResultadosBusca />} />
          <Route path="/receita/:id" element={<DetalheReceita />} />
          <Route path="/minhas-receitas" element={<div>Minhas receitas</div>} />
          <Route path="/favoritos" element={<div>Favoritos</div>} />
          <Route path="/criar-receita" element={<div>Criar Receita</div>} />
          <Route path="/perfil" element={<div>Perfil</div>} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="*" element={<div>Página não encontrada</div>} />
      </Routes>
    </BrowserRouter>
  );
}
