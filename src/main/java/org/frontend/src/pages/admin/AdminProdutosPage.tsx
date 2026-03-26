import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api, { getImageUrl } from '@/lib/api';
import { ProdutoResponse, CategoriaResponse } from '@/types';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Textarea } from '@/components/ui/textarea';
import { Switch } from '@/components/ui/switch';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Loader2, Plus, Pencil, Trash2, Search, Package } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

const AdminProdutosPage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();
  const [search, setSearch] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<ProdutoResponse | null>(null);
  const [form, setForm] = useState({
    codigo: '', nome: '', descricao: '', preco: '', custo: '', tempoPreparo: '',
    imagemUrl: '', categoriaId: '', estoque: '',
  });

  const { data: produtos = [], isLoading } = useQuery({
    queryKey: ['admin-produtos'],
    queryFn: async () => {
      try {
        const { data } = await api.get<ProdutoResponse[]>('/produtos');
        return data || [];
      } catch (err: any) {
        console.error('Erro ao carregar produtos:', err);
        return [];
      }
    },
  });

  const { data: categorias = [] } = useQuery({
    queryKey: ['admin-categorias-list'],
    queryFn: async () => {
      try {
        const { data } = await api.get<CategoriaResponse[]>('/produtos/categorias');
        return data || [];
      } catch { return []; }
    },
  });

  const createMutation = useMutation({
    mutationFn: (data: any) => editing ? api.put(`/produtos/${editing.id}`, data) : api.post('/produtos', data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-produtos'] });
      toast({ title: editing ? 'Produto atualizado!' : 'Produto criado!' });
      resetForm();
    },
    onError: () => toast({ title: 'Erro ao salvar produto', variant: 'destructive' }),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/produtos/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-produtos'] });
      toast({ title: 'Produto removido!' });
    },
  });

  const toggleDisp = useMutation({
    mutationFn: ({ id, disponivel }: { id: number; disponivel: boolean }) =>
      api.patch(`/produtos/${id}/disponibilidade`, null, { params: { disponivel } }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-produtos'] }),
  });

  const resetForm = () => {
    setForm({ codigo: '', nome: '', descricao: '', preco: '', custo: '', tempoPreparo: '', imagemUrl: '', categoriaId: '', estoque: '' });
    setEditing(null);
    setShowForm(false);
  };

  const handleEdit = (p: ProdutoResponse) => {
    setEditing(p);
    setForm({
      codigo: p.codigo, nome: p.nome, descricao: p.descricao, preco: String(p.preco),
      custo: String(p.custo || ''), tempoPreparo: String(p.tempoPreparo || ''),
      imagemUrl: p.imagemUrl || '', categoriaId: String(p.categoriaId), estoque: String(p.estoque || ''),
    });
    setShowForm(true);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createMutation.mutate({
      codigo: form.codigo, nome: form.nome, descricao: form.descricao,
      preco: Number(form.preco), custo: form.custo ? Number(form.custo) : undefined,
      tempoPreparo: form.tempoPreparo ? Number(form.tempoPreparo) : undefined,
      imagemUrl: form.imagemUrl || undefined, categoriaId: Number(form.categoriaId),
      estoque: form.estoque ? Number(form.estoque) : undefined,
    });
  };

  const filtered = produtos.filter((p: ProdutoResponse) =>
    p.nome.toLowerCase().includes(search.toLowerCase()) || p.codigo?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-4">
        <h1 className="text-2xl font-heading font-bold">Gestão de Produtos</h1>
        <Button onClick={() => { resetForm(); setShowForm(true); }}>
          <Plus className="h-4 w-4 mr-2" /> Novo Produto
        </Button>
      </div>

      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
        <Input placeholder="Buscar por nome ou código..." value={search} onChange={(e) => setSearch(e.target.value)} className="pl-10" />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
      ) : filtered.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground">
          <Package className="h-12 w-12 mx-auto mb-3" />
          <p>{search ? 'Nenhum produto encontrado' : 'Nenhum produto cadastrado'}</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {filtered.map((p: ProdutoResponse) => (
            <Card key={p.id}>
              <CardContent className="p-4 space-y-3">
                {p.imagemUrl && (
                  <div className="h-32 bg-muted rounded-md overflow-hidden">
                    <img src={getImageUrl(p.imagemUrl)} alt={p.nome} className="w-full h-full object-cover" />
                  </div>
                )}
                <div className="flex items-start justify-between">
                  <div>
                    <h3 className="font-heading font-semibold">{p.nome}</h3>
                    <p className="text-xs text-muted-foreground">Código: {p.codigo}</p>
                  </div>
                  <Badge className={p.disponivel ? 'bg-success text-success-foreground' : 'bg-muted text-muted-foreground'}>
                    {p.disponivel ? 'Disponível' : 'Indisponível'}
                  </Badge>
                </div>
                <p className="text-sm text-muted-foreground line-clamp-2">{p.descricao}</p>
                <div className="flex items-center justify-between">
                  <span className="font-bold text-primary">R$ {p.preco?.toFixed(2)}</span>
                  {p.estoque !== undefined && <span className="text-xs text-muted-foreground">Estoque: {p.estoque}</span>}
                </div>
                <div className="flex items-center justify-between pt-2 border-t">
                  <div className="flex items-center gap-2">
                    <Switch checked={p.disponivel} onCheckedChange={(v) => toggleDisp.mutate({ id: p.id, disponivel: v })} />
                    <span className="text-xs">{p.disponivel ? 'Ativo' : 'Inativo'}</span>
                  </div>
                  <div className="flex gap-1">
                    <Button variant="ghost" size="icon" onClick={() => handleEdit(p)}><Pencil className="h-4 w-4" /></Button>
                    <Button variant="ghost" size="icon" onClick={() => { if (confirm('Remover produto?')) deleteMutation.mutate(p.id); }}>
                      <Trash2 className="h-4 w-4 text-destructive" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Dialog open={showForm} onOpenChange={() => resetForm()}>
        <DialogContent className="max-w-lg max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="font-heading">{editing ? 'Editar Produto' : 'Novo Produto'}</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>Código</Label>
                <Input value={form.codigo} onChange={(e) => setForm({ ...form, codigo: e.target.value })} required />
              </div>
              <div className="space-y-2">
                <Label>Nome</Label>
                <Input value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} required />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Descrição</Label>
              <Textarea value={form.descricao} onChange={(e) => setForm({ ...form, descricao: e.target.value })} required />
            </div>
            <div className="grid grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label>Preço (R$)</Label>
                <Input type="number" step="0.01" value={form.preco} onChange={(e) => setForm({ ...form, preco: e.target.value })} required />
              </div>
              <div className="space-y-2">
                <Label>Custo (R$)</Label>
                <Input type="number" step="0.01" value={form.custo} onChange={(e) => setForm({ ...form, custo: e.target.value })} />
              </div>
              <div className="space-y-2">
                <Label>Preparo (min)</Label>
                <Input type="number" value={form.tempoPreparo} onChange={(e) => setForm({ ...form, tempoPreparo: e.target.value })} />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>Categoria</Label>
                <Select value={form.categoriaId} onValueChange={(v) => setForm({ ...form, categoriaId: v })}>
                  <SelectTrigger><SelectValue placeholder="Selecione" /></SelectTrigger>
                  <SelectContent>
                    {categorias.map((c: CategoriaResponse) => (
                      <SelectItem key={c.id} value={String(c.id)}>{c.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Estoque</Label>
                <Input type="number" value={form.estoque} onChange={(e) => setForm({ ...form, estoque: e.target.value })} />
              </div>
            </div>
            <div className="space-y-2">
              <Label>URL da Imagem</Label>
              <Input value={form.imagemUrl} onChange={(e) => setForm({ ...form, imagemUrl: e.target.value })} placeholder="/images/produto.jpg ou https://..." />
            </div>
            <div className="flex gap-2 justify-end">
              <Button type="button" variant="outline" onClick={resetForm}>Cancelar</Button>
              <Button type="submit" disabled={createMutation.isPending}>
                {createMutation.isPending && <Loader2 className="h-4 w-4 mr-2 animate-spin" />}
                {editing ? 'Atualizar' : 'Criar'}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminProdutosPage;
