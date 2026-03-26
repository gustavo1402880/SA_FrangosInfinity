import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { SubPedidoResponse, TipoPagamento } from '@/types';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Loader2, CreditCard, DollarSign, Smartphone, Receipt } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';

const PagamentosPage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();
  const [selectedSubPedido, setSelectedSubPedido] = useState<SubPedidoResponse | null>(null);
  const [paymentType, setPaymentType] = useState<TipoPagamento | null>(null);
  const [pixData, setPixData] = useState<any>(null);
  const [comprovante, setComprovante] = useState<string | null>(null);

  const { data: prontos = [] } = useQuery({
    queryKey: ['pagamentos-prontos'],
    queryFn: async () => {
      try {
        const { data } = await api.get<SubPedidoResponse[]>('/pedidos/subpedidos/status/PRONTO', { params: { status: 'PRONTO' } });
        return data || [];
      } catch { return []; }
    },
    refetchInterval: 15000,
  });

  const { data: entregues = [] } = useQuery({
    queryKey: ['pagamentos-entregues'],
    queryFn: async () => {
      try {
        const { data } = await api.get<SubPedidoResponse[]>('/pedidos/subpedidos/status/ENTREGUE', { params: { status: 'ENTREGUE' } });
        return data || [];
      } catch { return []; }
    },
    refetchInterval: 15000,
  });

  const allPedidos = [...prontos, ...entregues];

  const handleProcessPayment = async () => {
    if (!selectedSubPedido || !paymentType) return;
    try {
      const { data: pagamento } = await api.post('/pagamentos', {
        subPedidoId: selectedSubPedido.id,
        tipo: paymentType,
        valor: selectedSubPedido.valorTotal,
      });

      if (paymentType === 'PIX') {
        const { data: pix } = await api.get(`/pagamentos/pix`, {
          data: { pagamentoId: pagamento.id, valor: selectedSubPedido.valorTotal },
        });
        setPixData(pix);
      }

      await api.patch(`/pagamentos/${pagamento.id}/confirmar`);

      // Get comprovante
      try {
        const { data: comp } = await api.get(`/pagamentos/comprovantes/pagamento/${pagamento.id}`);
        if (comp?.texto) setComprovante(comp.texto);
      } catch { /* ignore */ }

      toast({ title: 'Pagamento processado com sucesso!' });
      queryClient.invalidateQueries({ queryKey: ['pagamentos-prontos'] });
      queryClient.invalidateQueries({ queryKey: ['pagamentos-entregues'] });
    } catch (err: any) {
      toast({ title: 'Erro ao processar pagamento', variant: 'destructive' });
    }
  };

  const paymentOptions: { type: TipoPagamento; label: string; icon: any }[] = [
    { type: 'DINHEIRO', label: 'Dinheiro', icon: DollarSign },
    { type: 'CARTAO_DEBITO', label: 'Débito', icon: CreditCard },
    { type: 'CARTAO_CREDITO', label: 'Crédito', icon: CreditCard },
    { type: 'PIX', label: 'PIX', icon: Smartphone },
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold flex items-center gap-2">
        <CreditCard className="h-7 w-7 text-primary" />
        Pagamentos
      </h1>

      {allPedidos.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground">
          <Receipt className="h-12 w-12 mx-auto mb-3" />
          <p>Nenhum pedido aguardando pagamento</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2">
          {allPedidos.map((sp) => (
            <Card key={sp.id}>
              <CardHeader className="pb-2">
                <CardTitle className="text-base">Pedido #{sp.numeroPedido || sp.id}</CardTitle>
                <p className="text-sm text-muted-foreground">Status: {sp.status}</p>
              </CardHeader>
              <CardContent className="space-y-2">
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
                <Button className="w-full" onClick={() => setSelectedSubPedido(sp)}>
                  Processar Pagamento
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Payment Modal */}
      <Dialog open={!!selectedSubPedido} onOpenChange={() => { setSelectedSubPedido(null); setPaymentType(null); setPixData(null); setComprovante(null); }}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle className="font-heading">Pagamento - Pedido #{selectedSubPedido?.numeroPedido || selectedSubPedido?.id}</DialogTitle>
          </DialogHeader>

          {comprovante ? (
            <div className="space-y-4">
              <div className="bg-muted p-4 rounded font-mono text-xs whitespace-pre-wrap">{comprovante}</div>
              <Button className="w-full" onClick={() => { setSelectedSubPedido(null); setComprovante(null); }}>
                Fechar
              </Button>
            </div>
          ) : (
            <div className="space-y-4">
              <p className="text-lg font-bold text-center">R$ {selectedSubPedido?.valorTotal?.toFixed(2)}</p>
              <p className="text-sm text-muted-foreground text-center">Selecione a forma de pagamento:</p>
              <div className="grid grid-cols-2 gap-3">
                {paymentOptions.map((opt) => (
                  <button
                    key={opt.type}
                    onClick={() => setPaymentType(opt.type)}
                    className={`p-4 rounded-lg border text-center transition-all ${
                      paymentType === opt.type ? 'border-primary bg-primary/10' : 'hover:bg-muted'
                    }`}
                  >
                    <opt.icon className="h-6 w-6 mx-auto mb-1" />
                    <span className="text-sm font-medium">{opt.label}</span>
                  </button>
                ))}
              </div>

              {pixData?.qrCodeBase64 && (
                <div className="text-center">
                  <img src={`data:image/png;base64,${pixData.qrCodeBase64}`} alt="QR PIX" className="mx-auto w-48 h-48" />
                </div>
              )}

              <Button className="w-full" disabled={!paymentType} onClick={handleProcessPayment}>
                Confirmar Pagamento
              </Button>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default PagamentosPage;
