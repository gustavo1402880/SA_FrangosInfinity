import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { SubPedidoResponse } from '@/types';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Loader2, Clock, Check, AlertTriangle } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const statusTabs = [
  { value: 'PENDENTE', label: 'Pendentes' },
  { value: 'CONFIRMADO', label: 'Confirmados' },
  { value: 'EM_PREPARO', label: 'Em Preparo' },
  { value: 'PRONTO', label: 'Prontos' },
  { value: 'ENTREGUE', label: 'Entregues' },
];

const PedidosPage = () => {
  const [activeTab, setActiveTab] = useState('PENDENTE');
  const queryClient = useQueryClient();
  const { toast } = useToast();

  const { data: subPedidos = [], isLoading } = useQuery({
    queryKey: ['subpedidos', activeTab],
    queryFn: async () => {
      try {
        const { data } = await api.get<SubPedidoResponse[]>('/pedidos/subpedidos/status/' + activeTab, {
          params: { status: activeTab },
        });
        return data || [];
      } catch (err: any) {
        console.error('Erro ao carregar subpedidos:', err);
        return [];
      }
    },
    refetchInterval: 15000,
  });

  const confirmMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/pedidos/subpedidos/${id}/confirmar`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subpedidos'] });
      toast({ title: 'Pedido confirmado!' });
    },
  });

  const deliverMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/pedidos/subpedidos/${id}/entregar`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subpedidos'] });
      toast({ title: 'Pedido entregue!' });
    },
  });

  const cancelMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/pedidos/subpedidos/${id}/cancelar`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subpedidos'] });
      toast({ title: 'Pedido cancelado!' });
    },
  });

  const getTimeSince = (date: string) => {
    const mins = Math.floor((Date.now() - new Date(date).getTime()) / 60000);
    if (mins < 60) return `${mins} min`;
    return `${Math.floor(mins / 60)}h ${mins % 60}min`;
  };

  if (isLoading) {
    return <div className="flex justify-center py-20"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold">Gerenciamento de Pedidos</h1>

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="w-full flex overflow-x-auto">
          {statusTabs.map((tab) => (
            <TabsTrigger key={tab.value} value={tab.value} className="flex-1 text-xs sm:text-sm">
              {tab.label}
            </TabsTrigger>
          ))}
        </TabsList>

        {statusTabs.map((tab) => (
          <TabsContent key={tab.value} value={tab.value} className="space-y-4 mt-4">
            {subPedidos.length === 0 ? (
              <div className="text-center py-12 text-muted-foreground">
                <p>Nenhum pedido {tab.label.toLowerCase()}</p>
              </div>
            ) : (
              subPedidos.map((sp) => (
                <Card key={sp.id}>
                  <CardHeader className="pb-2">
                    <div className="flex items-center justify-between">
                      <CardTitle className="text-base font-heading">
                        Pedido #{sp.numeroPedido || sp.id}
                      </CardTitle>
                      <div className="flex items-center gap-2">
                        <Clock className="h-4 w-4 text-muted-foreground" />
                        <span className="text-sm text-muted-foreground">{getTimeSince(sp.dataCriacao)}</span>
                      </div>
                    </div>
                    {sp.clienteNome && (
                      <p className="text-sm text-muted-foreground">Cliente: {sp.clienteNome}</p>
                    )}
                  </CardHeader>
                  <CardContent className="space-y-3">
                    {sp.itens?.map((item) => (
                      <div key={item.id} className="flex justify-between text-sm">
                        <span>{item.quantidade}x {item.produtoNome}</span>
                        <span>R$ {item.subtotal?.toFixed(2)}</span>
                      </div>
                    ))}
                    <div className="border-t pt-2 flex justify-between font-bold">
                      <span>Total</span>
                      <span className="text-primary">R$ {sp.valorTotal?.toFixed(2)}</span>
                    </div>
                    <div className="flex gap-2 pt-2">
                      {activeTab === 'PENDENTE' && (
                        <>
                          <Button size="sm" onClick={() => confirmMutation.mutate(sp.id)}>
                            <Check className="h-4 w-4 mr-1" /> Confirmar
                          </Button>
                          <Button size="sm" variant="destructive" onClick={() => cancelMutation.mutate(sp.id)}>
                            Cancelar
                          </Button>
                        </>
                      )}
                      {activeTab === 'PRONTO' && (
                        <Button size="sm" onClick={() => deliverMutation.mutate(sp.id)}>
                          Entregar
                        </Button>
                      )}
                    </div>
                  </CardContent>
                </Card>
              ))
            )}
          </TabsContent>
        ))}
      </Tabs>
    </div>
  );
};

export default PedidosPage;
