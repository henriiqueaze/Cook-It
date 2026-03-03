import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Layout } from "@/components/Layout";
import { Home } from "@/pages/Home";

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/busca" element={<div>Busca</div>} />
          <Route path="/receita/:id" element={<div>Detalhe da Receita</div>} />
          <Route path="/minhas-receitas" element={<div>Minhas receitas</div>} />
          <Route path="/favoritos" element={<div>Favoritos</div>} />
          <Route path="/criar-receita" element={<div>Criar Receita</div>} />
          <Route path="/perfil" element={<div>Perfil</div>} />
        </Route>

        <Route path="/login" element={<div>Login</div>} />
        <Route path="/cadastro" element={<div>Cadastro</div>} />
        <Route path="*" element={<div>Página não encontrada</div>} />
      </Routes>
    </BrowserRouter>
  );
}
