import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { SubPedidoResponse } from '@/types';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Loader2, ChefHat, Clock, AlertTriangle } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const CozinhaPage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();

  const { data: confirmados = [] } = useQuery({
    queryKey: ['cozinha-confirmados'],
    queryFn: async () => {
      try {
        const { data } = await api.get<SubPedidoResponse[]>('/pedidos/subpedidos/status/CONFIRMADO', { params: { status: 'CONFIRMADO' } });
        return data || [];
      } catch { return []; }
    },
    refetchInterval: 10000,
  });

  const { data: emPreparo = [] } = useQuery({
    queryKey: ['cozinha-em-preparo'],
    queryFn: async () => {
      try {
        const { data } = await api.get<SubPedidoResponse[]>('/pedidos/subpedidos/status/EM_PREPARO', { params: { status: 'EM_PREPARO' } });
        return data || [];
      } catch { return []; }
    },
    refetchInterval: 10000,
  });

  const prepararMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/pedidos/subpedidos/${id}/preparar`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cozinha-confirmados'] });
      queryClient.invalidateQueries({ queryKey: ['cozinha-em-preparo'] });
      toast({ title: 'Preparo iniciado!' });
    },
  });

  const prontoMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/pedidos/subpedidos/${id}/pronto`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cozinha-em-preparo'] });
      toast({ title: 'Pedido marcado como pronto!' });
    },
  });

  const getMinsSince = (date: string) => Math.floor((Date.now() - new Date(date).getTime()) / 60000);

  const getUrgencyClass = (date: string) => {
    const mins = getMinsSince(date);
    if (mins > 30) return 'border-destructive bg-destructive/5';
    if (mins > 20) return 'border-warning bg-warning/5';
    return '';
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <ChefHat className="h-7 w-7 text-primary" />
        <h1 className="text-2xl font-heading font-bold">Painel da Cozinha</h1>
      </div>

      <div className="grid lg:grid-cols-2 gap-6">
        {/* Confirmados - Aguardando preparo */}
        <div className="space-y-4">
          <h2 className="font-heading font-semibold text-lg flex items-center gap-2">
            <span className="w-3 h-3 rounded-full bg-info" />
            Aguardando Preparo ({confirmados.length})
          </h2>
          {confirmados.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">Nenhum pedido aguardando</p>
          ) : (
            confirmados.sort((a, b) => new Date(a.dataCriacao).getTime() - new Date(b.dataCriacao).getTime()).map((sp) => (
              <Card key={sp.id} className={`transition-all ${getUrgencyClass(sp.dataCriacao)}`}>
                <CardHeader className="pb-2">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">#{sp.numeroPedido || sp.id}</CardTitle>
                    <div className="flex items-center gap-1 text-sm">
                      {getMinsSince(sp.dataCriacao) > 20 && <AlertTriangle className="h-4 w-4 text-warning" />}
                      <Clock className="h-4 w-4" />
                      <span>{getMinsSince(sp.dataCriacao)} min</span>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-2">
                  {sp.itens?.map((item) => (
                    <div key={item.id} className="text-sm">
                      <span className="font-semibold">{item.quantidade}x</span> {item.produtoNome}
                      {item.observacao && <p className="text-xs text-muted-foreground ml-4">📝 {item.observacao}</p>}
                    </div>
                  ))}
                  <Button className="w-full mt-2" onClick={() => prepararMutation.mutate(sp.id)}>
                    Iniciar Preparo
                  </Button>
                </CardContent>
              </Card>
            ))
          )}
        </div>

        {/* Em Preparo */}
        <div className="space-y-4">
          <h2 className="font-heading font-semibold text-lg flex items-center gap-2">
            <span className="w-3 h-3 rounded-full bg-warning animate-pulse-soft" />
            Em Preparo ({emPreparo.length})
          </h2>
          {emPreparo.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">Nenhum pedido em preparo</p>
          ) : (
            emPreparo.map((sp) => (
              <Card key={sp.id} className={getUrgencyClass(sp.dataCriacao)}>
                <CardHeader className="pb-2">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">#{sp.numeroPedido || sp.id}</CardTitle>
                    <div className="flex items-center gap-1 text-sm">
                      <Clock className="h-4 w-4" />
                      <span>{getMinsSince(sp.dataCriacao)} min</span>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-2">
                  {sp.itens?.map((item) => (
                    <div key={item.id} className="text-sm">
                      <span className="font-semibold">{item.quantidade}x</span> {item.produtoNome}
                    </div>
                  ))}
                  <Button className="w-full mt-2 bg-success hover:bg-success/90 text-success-foreground" onClick={() => prontoMutation.mutate(sp.id)}>
                    ✓ Marcar Pronto
                  </Button>
                </CardContent>
              </Card>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default CozinhaPage;
