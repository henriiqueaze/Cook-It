import type { Receita, Usuario } from "../types";

export const usuarioMock: Usuario = {
  id: 1,
  nome: "Arthur Silva",
  email: "arthur@email.com",
  avatarUrl: "https://api.dicebear.com/7.x/avataaars/svg?seed=Arthur",
};

export const receitasMock: Receita[] = [
  {
    id: 1,
    titulo: "Frango Grelhado com Legumes",
    descricao: "Um prato leve e saudável, perfeito para o almoço do dia a dia.",
    imagemUrl:
      "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400",
    tempoPreparo: 30,
    porcoes: 2,
    dificuldade: "FACIL",
    categoria: "Almoço",
    avaliacao: 4.5,
    autor: usuarioMock,
    ingredientes: [
      { id: 1, nome: "Frango", quantidade: "500", unidade: "g" },
      { id: 2, nome: "Abobrinha", quantidade: "1", unidade: "unidade" },
      { id: 3, nome: "Cenoura", quantidade: "2", unidade: "unidades" },
      { id: 4, nome: "Azeite", quantidade: "2", unidade: "colheres" },
    ],
    instrucoes: [
      "Tempere o frango com sal, pimenta e azeite.",
      "Grelhe o frango por 10 minutos de cada lado.",
      "Refogue os legumes no azeite por 5 minutos.",
      "Sirva o frango acompanhado dos legumes.",
    ],
    favoritada: false,
    criadoEm: "2024-01-15",
  },
  {
    id: 2,
    titulo: "Bolo de Cenoura com Cobertura de Chocolate",
    descricao:
      "O clássico bolo de cenoura brasileiro com aquela cobertura cremosa.",
    imagemUrl:
      "https://images.unsplash.com/photo-1621303837174-89787a7d4729?w=400",
    tempoPreparo: 60,
    porcoes: 8,
    dificuldade: "MEDIO",
    categoria: "Sobremesa",
    avaliacao: 5,
    autor: usuarioMock,
    ingredientes: [
      { id: 5, nome: "Cenoura", quantidade: "3", unidade: "unidades" },
      { id: 6, nome: "Ovos", quantidade: "3", unidade: "unidades" },
      { id: 7, nome: "Farinha de trigo", quantidade: "2", unidade: "xícaras" },
      { id: 8, nome: "Açúcar", quantidade: "2", unidade: "xícaras" },
      { id: 9, nome: "Chocolate em pó", quantidade: "4", unidade: "colheres" },
    ],
    instrucoes: [
      "Bata no liquidificador as cenouras, ovos e óleo.",
      "Misture a farinha, açúcar e fermento.",
      "Despeje em forma untada e asse por 40 minutos.",
      "Prepare a cobertura com chocolate, manteiga e leite.",
      "Despeje a cobertura ainda quente sobre o bolo.",
    ],
    favoritada: true,
    criadoEm: "2024-02-20",
  },
  {
    id: 3,
    titulo: "Macarrão ao Molho Pesto",
    descricao:
      "Receita italiana simples e deliciosa, pronta em menos de 20 minutos.",
    imagemUrl:
      "https://images.unsplash.com/photo-1473093226555-0b9e714f9d08?w=400",
    tempoPreparo: 20,
    porcoes: 4,
    dificuldade: "FACIL",
    categoria: "Massas",
    avaliacao: 4,
    autor: usuarioMock,
    ingredientes: [
      { id: 10, nome: "Macarrão", quantidade: "400", unidade: "g" },
      { id: 11, nome: "Manjericão", quantidade: "1", unidade: "xícara" },
      { id: 12, nome: "Parmesão", quantidade: "50", unidade: "g" },
      { id: 13, nome: "Azeite", quantidade: "4", unidade: "colheres" },
      { id: 14, nome: "Alho", quantidade: "2", unidade: "dentes" },
    ],
    instrucoes: [
      "Cozinhe o macarrão conforme instruções da embalagem.",
      "Bata no processador o manjericão, alho, parmesão e azeite.",
      "Misture o pesto ao macarrão cozido.",
      "Sirva com parmesão ralado por cima.",
    ],
    favoritada: false,
    criadoEm: "2024-03-10",
  },
];
