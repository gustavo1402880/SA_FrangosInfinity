import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { Card, CardContent } from '@/components/ui/card';
import { Loader2 } from 'lucide-react';

interface OrderTrackingProps {
  subPedidoId: number;
}

const OrderTracking = ({ subPedidoId }: OrderTrackingProps) => {
  const { data: subPedido, isLoading } = useQuery({
    queryKey: ['subpedido-tracking', subPedidoId],
    queryFn: async () => {
      const { data } = await api.get(`/pedidos/subpedidos/${subPedidoId}`);
      return data;
    },
    refetchInterval: 10000,
  });

  const steps = ['PENDENTE', 'CONFIRMADO', 'EM_PREPARO', 'PRONTO', 'ENTREGUE'];
  const stepLabels = ['Pendente', 'Confirmado', 'Em Preparo', 'Pronto', 'Entregue'];
  const currentStep = subPedido ? steps.indexOf(subPedido.status) : 0;

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="max-w-lg mx-auto space-y-6">
      <div className="text-center">
        <h1 className="text-2xl font-heading font-bold">Acompanhar Pedido</h1>
        <p className="text-muted-foreground">Pedido #{subPedido?.numeroPedido || subPedidoId}</p>
      </div>

      <div className="space-y-4">
        {steps.map((step, i) => (
          <div key={step} className="flex items-center gap-3">
            <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
              i <= currentStep ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground'
            }`}>
              {i + 1}
            </div>
            <span className={`text-sm ${i <= currentStep ? 'font-semibold text-foreground' : 'text-muted-foreground'}`}>
              {stepLabels[i]}
            </span>
            {i === currentStep && (
              <div className="ml-auto">
                <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded-full animate-pulse">
                  Atual
                </span>
              </div>
            )}
          </div>
        ))}
      </div>

      {subPedido?.itens && (
        <Card>
          <CardContent className="p-4 space-y-2">
            <h3 className="font-heading font-semibold">Itens do Pedido</h3>
            {subPedido.itens.map((item: any) => (
              <div key={item.id} className="flex justify-between text-sm">
                <span>{item.quantidade}x {item.produtoNome}</span>
                <span className="font-medium">R$ {item.subtotal?.toFixed(2)}</span>
              </div>
            ))}
            <div className="border-t pt-2 flex justify-between font-heading font-bold">
              <span>Total</span>
              <span className="text-primary">R$ {subPedido.valorTotal?.toFixed(2)}</span>
            </div>
          </CardContent>
        </Card>
      )}

      <p className="text-center text-xs text-muted-foreground">Atualizando automaticamente a cada 10 segundos</p>
    </div>
  );
};

export default OrderTracking;
