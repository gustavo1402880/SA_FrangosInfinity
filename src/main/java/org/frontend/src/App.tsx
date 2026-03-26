import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import LoginPage from "./pages/LoginPage";
import CadastroPage from "./pages/CadastroPage";
import DashboardPage from "./pages/DashboardPage";
import MesasPage from "./pages/MesasPage";
import CardapioPage from "./pages/CardapioPage";
import PedidosPage from "./pages/PedidosPage";
import CozinhaPage from "./pages/CozinhaPage";
import PagamentosPage from "./pages/PagamentosPage";
import AdminProdutosPage from "./pages/admin/AdminProdutosPage";
import AdminCategoriasPage from "./pages/admin/AdminCategoriasPage";
import AdminUsuariosPage from "./pages/admin/AdminUsuariosPage";
import AdminRelatoriosPage from "./pages/admin/AdminRelatoriosPage";
import AdminFidelidadePage from "./pages/admin/AdminFidelidadePage";
import ProtectedRoute from "./components/ProtectedRoute";
import AppLayout from "./components/AppLayout";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const ProtectedPage = ({ children, roles }: { children: React.ReactNode; roles?: string[] }) => (
  <ProtectedRoute roles={roles}>
    <AppLayout>{children}</AppLayout>
  </ProtectedRoute>
);

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/cadastro" element={<CadastroPage />} />

          <Route path="/dashboard" element={<ProtectedPage><DashboardPage /></ProtectedPage>} />
          <Route path="/mesas" element={<ProtectedPage roles={['CLIENTE', 'ADMINISTRADOR', 'ATENDENTE', 'CAIXA']}><MesasPage /></ProtectedPage>} />
          <Route path="/cardapio" element={<ProtectedPage roles={['CLIENTE']}><CardapioPage /></ProtectedPage>} />
          <Route path="/pedidos" element={<ProtectedPage roles={['ADMINISTRADOR', 'ATENDENTE', 'CAIXA']}><PedidosPage /></ProtectedPage>} />
          <Route path="/cozinha" element={<ProtectedPage roles={['ADMINISTRADOR', 'COZINHEIRO']}><CozinhaPage /></ProtectedPage>} />
          <Route path="/pagamentos" element={<ProtectedPage roles={['ADMINISTRADOR', 'CAIXA']}><PagamentosPage /></ProtectedPage>} />

          <Route path="/admin/produtos" element={<ProtectedPage roles={['ADMINISTRADOR']}><AdminProdutosPage /></ProtectedPage>} />
          <Route path="/admin/categorias" element={<ProtectedPage roles={['ADMINISTRADOR']}><AdminCategoriasPage /></ProtectedPage>} />
          <Route path="/admin/usuarios" element={<ProtectedPage roles={['ADMINISTRADOR']}><AdminUsuariosPage /></ProtectedPage>} />
          <Route path="/admin/relatorios" element={<ProtectedPage roles={['ADMINISTRADOR']}><AdminRelatoriosPage /></ProtectedPage>} />
          <Route path="/admin/fidelidade" element={<ProtectedPage roles={['ADMINISTRADOR']}><AdminFidelidadePage /></ProtectedPage>} />

          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
