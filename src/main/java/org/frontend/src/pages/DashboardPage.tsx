import { useAuthStore } from '@/stores/authStore';
import { Card, CardContent } from '@/components/ui/card';
import { LayoutDashboard, UtensilsCrossed, ShoppingCart, TrendingUp, DollarSign, Award } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { PontosResponse } from '@/types';

const DashboardPage = () => {
  const { user } = useAuthStore();
  const role = user?.nivelAcesso || (user?.tipoUsuario === 'CLIENTE' ? 'CLIENTE' : '');

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-heading font-bold">Olá, {user?.nome?.split(' ')[0]}! 👋</h1>
        <p className="text-muted-foreground">Bem-vindo ao painel do Frango's Infinity</p>
      </div>

      {role === 'CLIENTE' && <ClienteDashboard userId={user?.id} />}
      {role === 'ADMINISTRADOR' && <AdminDashboard />}
      {role === 'ATENDENTE' && <AtendenteDashboard />}
      {role === 'COZINHEIRO' && <CozinheiroDashboard />}
      {role === 'CAIXA' && <CaixaDashboard />}
    </div>
  );
};

const StatCard = ({ title, value, icon: Icon, color }: { title: string; value: string | number; icon: any; color?: string }) => (
  <Card>
    <CardContent className="flex items-center gap-4 p-6">
      <div className={`p-3 rounded-lg ${color || 'bg-primary/10'}`}>
        <Icon className={`h-6 w-6 ${color ? 'text-card' : 'text-primary'}`} />
      </div>
      <div>
        <p className="text-sm text-muted-foreground">{title}</p>
        <p className="text-2xl font-heading font-bold">{value}</p>
      </div>
    </CardContent>
  </Card>
);

const ClienteDashboard = ({ userId }: { userId?: number }) => {
  const { data: pontos } = useQuery({
    queryKey: ['cliente-pontos', userId],
    queryFn: async () => {
      try {
        const { data } = await api.get<PontosResponse>(`/contas/cliente/${userId}`);
        return data;
      } catch {
        return null;
      }
    },
    enabled: !!userId,
  });

  return (
    <div className="grid gap-4 md:grid-cols-2">
      <Card className="col-span-full">
        <CardContent className="p-8 text-center">
          <ShoppingCart className="h-16 w-16 mx-auto mb-4 text-primary" />
          <h2 className="text-xl font-heading font-semibold mb-2">Fazer um Pedido</h2>
          <p className="text-muted-foreground mb-4">Selecione uma mesa para começar seu pedido</p>
          <a href="/mesas" className="inline-flex items-center justify-center rounded-md bg-primary text-primary-foreground px-6 py-3 font-medium hover:bg-primary/90 transition-colors">
            Selecionar Mesa
          </a>
        </CardContent>
      </Card>

      {pontos && pontos.sucesso && (
        <Card className="col-span-full">
          <CardContent className="p-6">
            <div className="flex items-center gap-3 mb-4">
              <Award className="h-6 w-6 text-primary" />
              <h3 className="font-heading font-semibold text-lg">Programa de Fidelidade</h3>
            </div>
            <div className="grid grid-cols-3 gap-4 text-center">
              <div>
                <p className="text-xs text-muted-foreground">Pontos Atuais</p>
                <p className="text-2xl font-heading font-bold text-primary">{pontos.pontosAtuais}</p>
              </div>
              <div>
                <p className="text-xs text-muted-foreground">Acumulados</p>
                <p className="text-2xl font-heading font-bold">{pontos.pontosAcumulados}</p>
              </div>
              <div>
                <p className="text-xs text-muted-foreground">Resgatados</p>
                <p className="text-2xl font-heading font-bold">{pontos.pontosResgatados}</p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

const AdminDashboard = () => {
  const { data: pedidos } = useQuery({
    queryKey: ['pedidos-all'],
    queryFn: async () => { try { const { data } = await api.get('/pedidos'); return data || []; } catch { return []; } },
  });

  return (
    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      <StatCard title="Pedidos Hoje" value={pedidos?.length || 0} icon={UtensilsCrossed} />
      <StatCard title="Painel" value="Ativo" icon={LayoutDashboard} />
      <StatCard title="Vendas" value="--" icon={TrendingUp} />
      <StatCard title="Receita" value="--" icon={DollarSign} />
    </div>
  );
};

const AtendenteDashboard = () => (
  <div className="grid gap-4 md:grid-cols-2">
    <StatCard title="Mesas" value="Ver mesas" icon={LayoutDashboard} />
    <StatCard title="Pedidos" value="Gerenciar" icon={UtensilsCrossed} />
  </div>
);

const CozinheiroDashboard = () => (
  <div className="grid gap-4 md:grid-cols-2">
    <Card className="col-span-full">
      <CardContent className="p-8 text-center">
        <a href="/cozinha" className="inline-flex items-center justify-center rounded-md bg-primary text-primary-foreground px-6 py-3 font-medium hover:bg-primary/90 transition-colors">
          Abrir Painel da Cozinha
        </a>
      </CardContent>
    </Card>
  </div>
);

const CaixaDashboard = () => (
  <div className="grid gap-4 md:grid-cols-2">
    <Card className="col-span-full">
      <CardContent className="p-8 text-center">
        <a href="/pagamentos" className="inline-flex items-center justify-center rounded-md bg-primary text-primary-foreground px-6 py-3 font-medium hover:bg-primary/90 transition-colors">
          Abrir Caixa
        </a>
      </CardContent>
    </Card>
  </div>
);

export default DashboardPage;
