import { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import api, { getImageUrl } from '@/lib/api';
import { ProdutoResponse, CategoriaResponse } from '@/types';
import { useCartStore } from '@/stores/cartStore';
import { useAuthStore } from '@/stores/authStore';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Loader2, Plus, ShoppingCart, Minus, Trash2, Package } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet';
import { Textarea } from '@/components/ui/textarea';
import OrderTracking from '@/components/OrderTracking';

const CardapioPage = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const mesaId = searchParams.get('mesaId');
  const pedidoIdParam = searchParams.get('pedidoId');
  const { user } = useAuthStore();
  const [activeCategory, setActiveCategory] = useState<number | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const cart = useCartStore();
  const { toast } = useToast();
  const [pedidoId, setPedidoId] = useState<number | null>(pedidoIdParam ? Number(pedidoIdParam) : null);
  const [submitting, setSubmitting] = useState(false);
  const [trackingSubPedidoId, setTrackingSubPedidoId] = useState<number | null>(null);

  const { data: categorias = [] } = useQuery({
    queryKey: ['cardapio-categorias'],
    queryFn: async () => {
      const { data } = await api.get<CategoriaResponse[]>('/pedidos/cardapio/categorias');
      return data || [];
    },
  });

  const { data: produtos = [], isLoading } = useQuery({
    queryKey: ['cardapio-produtos'],
    queryFn: async () => {
      const { data } = await api.get<ProdutoResponse[]>('/pedidos/cardapio/produtos');
      return data || [];
    },
  });

  const filteredProducts = produtos.filter((p) => {
    const matchesCategory = !activeCategory || p.categoriaId === activeCategory;
    const matchesSearch = !searchTerm || p.nome.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const handleAddToCart = (produto: ProdutoResponse) => {
    cart.addItem({
      produtoId: produto.id,
      nome: produto.nome,
      preco: produto.preco,
      imagemUrl: produto.imagemUrl,
    });
    toast({ title: `${produto.nome} adicionado ao carrinho` });
  };

  const handleConfirmOrder = async () => {
    if (cart.items.length === 0) {
      toast({ title: 'Carrinho vazio', variant: 'destructive' });
      return;
    }

    setSubmitting(true);
    try {
      // Step 1: Create pedido if we don't have one yet
      let currentPedidoId = pedidoId;
      if (!currentPedidoId) {
        if (!mesaId) {
          toast({ title: 'Selecione uma mesa primeiro', variant: 'destructive' });
          setSubmitting(false);
          return;
        }
        const { data: pedidoData } = await api.post('/pedidos', { mesaId: Number(mesaId) });
        currentPedidoId = pedidoData.id;
        setPedidoId(currentPedidoId);
      }

      // Step 2: Clear server-side cart first
      try { await api.delete('/pedidos/carrinho'); } catch { /* ignore */ }

      // Step 3: Add all items to server-side cart
      for (const item of cart.items) {
        await api.post('/pedidos/carrinho/itens', {
          produtoId: item.produtoId,
          quantidade: item.quantidade,
          observacao: item.observacao || '',
        });
      }

      // Step 4: Create subpedido (backend reads from server cart)
      const { data: subPedido } = await api.post(`/pedidos/${currentPedidoId}/subpedidos`, {
        pedidoId: currentPedidoId,
        clienteId: user?.id,
      });

      // Step 5: Clear local cart and show tracking
      cart.clearCart();
      cart.setOpen(false);
      setTrackingSubPedidoId(subPedido.id);
      toast({ title: 'Pedido confirmado!', description: 'Aguarde a confirmação do atendente.' });
    } catch (err: any) {
      console.error('Erro ao confirmar pedido:', err);
      toast({ title: 'Erro ao confirmar pedido', description: err.response?.data?.mensagem || 'Tente novamente.', variant: 'destructive' });
    } finally {
      setSubmitting(false);
    }
  };

  if (trackingSubPedidoId) {
    return (
      <div>
        <OrderTracking subPedidoId={trackingSubPedidoId} />
        <div className="text-center mt-4">
          <Button variant="outline" onClick={() => {
            setTrackingSubPedidoId(null);
          }}>
            Fazer Novo Pedido
          </Button>
        </div>
      </div>
    );
  }

  if (!mesaId && !pedidoId) {
    return (
      <div className="max-w-md mx-auto text-center space-y-6 py-12">
        <Package className="h-16 w-16 mx-auto text-muted-foreground" />
        <h1 className="text-2xl font-heading font-bold">Cardápio</h1>
        <p className="text-muted-foreground">
          Para fazer um pedido, selecione uma mesa primeiro.
        </p>
        <Button onClick={() => navigate('/mesas')}>
          Selecionar Mesa
        </Button>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-heading font-bold">Cardápio</h1>
          {mesaId && <p className="text-muted-foreground">Mesa selecionada</p>}
        </div>
      </div>

      <Input
        placeholder="Buscar produto..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="max-w-sm"
      />

      {/* Categories */}
      <div className="flex gap-2 overflow-x-auto pb-2">
        <Button
          variant={activeCategory === null ? 'default' : 'outline'}
          size="sm"
          onClick={() => setActiveCategory(null)}
        >
          Todos
        </Button>
        {categorias.map((cat) => (
          <Button
            key={cat.id}
            variant={activeCategory === cat.id ? 'default' : 'outline'}
            size="sm"
            onClick={() => setActiveCategory(cat.id)}
          >
            {cat.nome}
          </Button>
        ))}
      </div>

      {/* Products Grid */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {filteredProducts.map((produto) => (
          <Card key={produto.id} className="overflow-hidden hover:shadow-md transition-shadow">
            <div className="h-32 bg-muted flex items-center justify-center">
              {produto.imagemUrl ? (
                <img src={getImageUrl(produto.imagemUrl)} alt={produto.nome} className="w-full h-full object-cover" />
              ) : (
                <Package className="h-10 w-10 text-muted-foreground" />
              )}
            </div>
            <CardContent className="p-3 space-y-2">
              <h3 className="font-heading font-semibold text-sm line-clamp-1">{produto.nome}</h3>
              <p className="text-xs text-muted-foreground line-clamp-2">{produto.descricao}</p>
              <div className="flex items-center justify-between">
                <span className="font-heading font-bold text-primary">
                  R$ {produto.preco.toFixed(2)}
                </span>
                {produto.disponivel ? (
                  <Button size="sm" onClick={() => handleAddToCart(produto)}>
                    <Plus className="h-4 w-4" />
                  </Button>
                ) : (
                  <Badge variant="secondary">Indisponível</Badge>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredProducts.length === 0 && (
        <div className="text-center py-12 text-muted-foreground">
          <Package className="h-12 w-12 mx-auto mb-3" />
          <p>Nenhum produto encontrado</p>
        </div>
      )}

      {/* Floating Cart Button */}
      <Sheet open={cart.isOpen} onOpenChange={cart.setOpen}>
        <SheetTrigger asChild>
          <button
            className="fixed bottom-6 right-6 bg-primary text-primary-foreground rounded-full p-4 shadow-lg hover:bg-primary/90 transition-colors z-40"
            onClick={() => cart.setOpen(true)}
          >
            <ShoppingCart className="h-6 w-6" />
            {cart.totalItems() > 0 && (
              <Badge className="absolute -top-2 -right-2 bg-foreground text-background h-6 w-6 flex items-center justify-center p-0">
                {cart.totalItems()}
              </Badge>
            )}
          </button>
        </SheetTrigger>
        <SheetContent className="w-full sm:max-w-md flex flex-col">
          <SheetHeader>
            <SheetTitle className="font-heading">Carrinho</SheetTitle>
          </SheetHeader>
          <div className="flex-1 overflow-y-auto space-y-3 mt-4">
            {cart.items.length === 0 ? (
              <div className="text-center py-12 text-muted-foreground">
                <ShoppingCart className="h-12 w-12 mx-auto mb-3" />
                <p>Carrinho vazio</p>
              </div>
            ) : (
              cart.items.map((item) => (
                <div key={item.produtoId} className="border rounded-lg p-3 space-y-2">
                  <div className="flex items-center justify-between">
                    <h4 className="font-medium text-sm">{item.nome}</h4>
                    <button onClick={() => cart.removeItem(item.produtoId)} className="text-destructive hover:text-destructive/80">
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Button variant="outline" size="icon" className="h-7 w-7" onClick={() => cart.updateQuantity(item.produtoId, item.quantidade - 1)}>
                        <Minus className="h-3 w-3" />
                      </Button>
                      <span className="text-sm font-medium w-6 text-center">{item.quantidade}</span>
                      <Button variant="outline" size="icon" className="h-7 w-7" onClick={() => cart.updateQuantity(item.produtoId, item.quantidade + 1)}>
                        <Plus className="h-3 w-3" />
                      </Button>
                    </div>
                    <span className="text-sm font-semibold">R$ {(item.preco * item.quantidade).toFixed(2)}</span>
                  </div>
                  <Textarea
                    placeholder="Observações (ex: sem cebola)"
                    value={item.observacao}
                    onChange={(e) => cart.updateObservacao(item.produtoId, e.target.value)}
                    className="text-xs h-16"
                  />
                </div>
              ))
            )}
          </div>
          {cart.items.length > 0 && (
            <div className="border-t pt-4 space-y-3">
              <div className="flex justify-between font-heading font-bold text-lg">
                <span>Total</span>
                <span className="text-primary">R$ {cart.totalPrice().toFixed(2)}</span>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" className="flex-1" onClick={() => cart.setOpen(false)}>
                  Continuar Comprando
                </Button>
                <Button className="flex-1" onClick={handleConfirmOrder} disabled={submitting}>
                  {submitting && <Loader2 className="h-4 w-4 mr-2 animate-spin" />}
                  Confirmar Pedido
                </Button>
              </div>
            </div>
          )}
        </SheetContent>
      </Sheet>
    </div>
  );
};

export default CardapioPage;
