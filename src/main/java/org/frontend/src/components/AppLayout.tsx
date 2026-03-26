import { ReactNode, useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '@/stores/authStore';
import {
  SidebarProvider,
  SidebarTrigger,
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuItem,
  SidebarMenuButton,
  useSidebar,
} from '@/components/ui/sidebar';
import { NavLink } from '@/components/NavLink';
import {
  LayoutDashboard,
  UtensilsCrossed,
  ChefHat,
  CreditCard,
  Users,
  Package,
  BarChart3,
  Award,
  Bell,
  LogOut,
  Tablet,
  Settings,
  ShoppingCart,
} from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

interface AppLayoutProps {
  children: ReactNode;
}

const getMenuItems = (role: string) => {
  const items = [
    { title: 'Dashboard', url: '/dashboard', icon: LayoutDashboard, roles: ['CLIENTE', 'ADMINISTRADOR', 'ATENDENTE', 'COZINHEIRO', 'CAIXA'] },
    { title: 'Mesas', url: '/mesas', icon: Tablet, roles: ['CLIENTE', 'ADMINISTRADOR', 'ATENDENTE', 'CAIXA'] },
    { title: 'Cardápio', url: '/cardapio', icon: ShoppingCart, roles: ['CLIENTE'] },
    { title: 'Pedidos', url: '/pedidos', icon: UtensilsCrossed, roles: ['ADMINISTRADOR', 'ATENDENTE', 'CAIXA'] },
    { title: 'Cozinha', url: '/cozinha', icon: ChefHat, roles: ['ADMINISTRADOR', 'COZINHEIRO'] },
    { title: 'Pagamentos', url: '/pagamentos', icon: CreditCard, roles: ['ADMINISTRADOR', 'CAIXA'] },
    { title: 'Produtos', url: '/admin/produtos', icon: Package, roles: ['ADMINISTRADOR'] },
    { title: 'Categorias', url: '/admin/categorias', icon: Settings, roles: ['ADMINISTRADOR'] },
    { title: 'Usuários', url: '/admin/usuarios', icon: Users, roles: ['ADMINISTRADOR'] },
    { title: 'Relatórios', url: '/admin/relatorios', icon: BarChart3, roles: ['ADMINISTRADOR'] },
    { title: 'Fidelidade', url: '/admin/fidelidade', icon: Award, roles: ['ADMINISTRADOR'] },
  ];
  return items.filter((i) => i.roles.includes(role));
};

function AppSidebarContent() {
  const { state } = useSidebar();
  const collapsed = state === 'collapsed';
  const { user } = useAuthStore();
  const role = user?.nivelAcesso || (user?.tipoUsuario === 'CLIENTE' ? 'CLIENTE' : '');
  const menuItems = getMenuItems(role);
  const location = useLocation();

  return (
    <Sidebar collapsible="icon" className="border-r-0">
      <SidebarContent>
        <SidebarGroup>
          {!collapsed && (
            <div className="px-4 py-4 mb-2">
              <h2 className="text-lg font-heading font-bold text-sidebar-primary">🍗 Frango's</h2>
              <p className="text-xs text-sidebar-foreground/60">Infinity</p>
            </div>
          )}
          <SidebarGroupLabel>Menu</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {menuItems.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <NavLink
                      to={item.url}
                      end={item.url === '/dashboard'}
                      className="hover:bg-sidebar-accent/50"
                      activeClassName="bg-sidebar-accent text-sidebar-primary font-medium"
                    >
                      <item.icon className="mr-2 h-4 w-4" />
                      {!collapsed && <span>{item.title}</span>}
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}

const AppLayout = ({ children }: AppLayoutProps) => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);

  const role = user?.nivelAcesso || (user?.tipoUsuario === 'CLIENTE' ? 'CLIENTE' : '');
  const notifDest = user?.nivelAcesso || user?.email || '';

  const { data: unreadCount = 0 } = useQuery({
    queryKey: ['notifications-count', notifDest],
    queryFn: async () => {
      if (!notifDest) return 0;
      const { data } = await api.get<number>(`/notificacoes/${notifDest}/count`);
      return data;
    },
    refetchInterval: 30000,
    enabled: !!notifDest,
  });

  const { data: notifications = [] } = useQuery({
    queryKey: ['notifications', notifDest],
    queryFn: async () => {
      if (!notifDest) return [];
      const { data } = await api.get(`/notificacoes/${notifDest}/nao-lidas`);
      return data || [];
    },
    refetchInterval: 30000,
    enabled: !!notifDest,
  });

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const markAsRead = async (id: number) => {
    try {
      await api.patch(`/notificacoes/${id}/lida`);
    } catch { /* ignore */ }
  };

  return (
    <SidebarProvider>
      <div className="min-h-screen flex w-full">
        <AppSidebarContent />
        <div className="flex-1 flex flex-col min-w-0">
          <header className="h-14 flex items-center justify-between border-b bg-card px-4 sticky top-0 z-30">
            <div className="flex items-center gap-2">
              <SidebarTrigger />
              <span className="font-heading font-semibold text-sm hidden sm:inline">Frango's Infinity</span>
            </div>
            <div className="flex items-center gap-3">
              {/* Notifications */}
              <div className="relative">
                <button
                  onClick={() => setShowNotifications(!showNotifications)}
                  className="relative p-2 rounded-md hover:bg-muted transition-colors"
                >
                  <Bell className="h-5 w-5" />
                  {unreadCount > 0 && (
                    <Badge className="absolute -top-1 -right-1 h-5 w-5 flex items-center justify-center p-0 text-xs bg-primary text-primary-foreground">
                      {unreadCount > 9 ? '9+' : unreadCount}
                    </Badge>
                  )}
                </button>

                {showNotifications && (
                  <div className="absolute right-0 top-12 w-80 max-h-96 overflow-y-auto bg-card border rounded-lg shadow-lg z-50">
                    <div className="p-3 border-b font-heading font-semibold text-sm">Notificações</div>
                    {(notifications as any[]).length === 0 ? (
                      <div className="p-4 text-center text-muted-foreground text-sm">
                        Nenhuma notificação
                      </div>
                    ) : (
                      (notifications as any[]).map((n: any) => (
                        <div
                          key={n.id}
                          className="p-3 border-b hover:bg-muted/50 cursor-pointer transition-colors"
                          onClick={() => markAsRead(n.id)}
                        >
                          <p className="text-sm">{n.mensagem}</p>
                          <p className="text-xs text-muted-foreground mt-1">
                            {new Date(n.dataCriacao).toLocaleString('pt-BR')}
                          </p>
                        </div>
                      ))
                    )}
                  </div>
                )}
              </div>

              {/* User info */}
              <div className="hidden sm:flex items-center gap-2">
                <div className="text-right">
                  <p className="text-sm font-medium leading-none">{user?.nome}</p>
                  <p className="text-xs text-muted-foreground">{role}</p>
                </div>
              </div>

              <button
                onClick={handleLogout}
                className="p-2 rounded-md hover:bg-destructive/10 text-muted-foreground hover:text-destructive transition-colors"
                title="Sair"
              >
                <LogOut className="h-5 w-5" />
              </button>
            </div>
          </header>

          <main className="flex-1 p-4 md:p-6 overflow-auto">
            {children}
          </main>
        </div>
      </div>
    </SidebarProvider>
  );
};

export default AppLayout;
