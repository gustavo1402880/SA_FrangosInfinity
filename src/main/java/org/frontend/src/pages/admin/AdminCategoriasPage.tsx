import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { CategoriaResponse } from '@/types';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Switch } from '@/components/ui/switch';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Loader2, Plus, Pencil, Trash2 } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const AdminCategoriasPage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<CategoriaResponse | null>(null);
  const [form, setForm] = useState({ nome: '', descricao: '' });

  const { data: categorias = [], isLoading } = useQuery({
    queryKey: ['admin-categorias'],
    queryFn: async () => { try { const { data } = await api.get('/produtos/categorias'); return data || []; } catch { return []; } },
  });

  const createMutation = useMutation({
    mutationFn: (data: any) => editing ? api.put(`/produtos/categorias/${editing.id}`, data) : api.post('/produtos/categorias', data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-categorias'] });
      toast({ title: editing ? 'Categoria atualizada!' : 'Categoria criada!' });
      setShowForm(false); setEditing(null); setForm({ nome: '', descricao: '' });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/produtos/categorias/${id}`),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['admin-categorias'] }); toast({ title: 'Categoria removida!' }); },
  });

  const toggleMutation = useMutation({
    mutationFn: ({ id, ativa }: { id: number; ativa: boolean }) =>
      api.patch(`/produtos/categorias/${id}/${ativa ? 'ativar' : 'desativar'}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-categorias'] }),
  });

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-heading font-bold">Gestão de Categorias</h1>
        <Button onClick={() => { setEditing(null); setForm({ nome: '', descricao: '' }); setShowForm(true); }}>
          <Plus className="h-4 w-4 mr-2" /> Nova Categoria
        </Button>
      </div>

      {isLoading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {categorias.map((c: CategoriaResponse) => (
            <Card key={c.id}>
              <CardContent className="p-4 flex items-center justify-between">
                <div>
                  <h3 className="font-heading font-semibold">{c.nome}</h3>
                  {c.descricao && <p className="text-sm text-muted-foreground">{c.descricao}</p>}
                </div>
                <div className="flex items-center gap-2">
                  <Switch checked={c.ativa} onCheckedChange={(v) => toggleMutation.mutate({ id: c.id, ativa: v })} />
                  <Button variant="ghost" size="icon" onClick={() => { setEditing(c); setForm({ nome: c.nome, descricao: c.descricao || '' }); setShowForm(true); }}>
                    <Pencil className="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" size="icon" onClick={() => { if (confirm('Remover?')) deleteMutation.mutate(c.id); }}>
                    <Trash2 className="h-4 w-4 text-destructive" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Dialog open={showForm} onOpenChange={() => setShowForm(false)}>
        <DialogContent>
          <DialogHeader><DialogTitle className="font-heading">{editing ? 'Editar' : 'Nova'} Categoria</DialogTitle></DialogHeader>
          <form onSubmit={(e) => { e.preventDefault(); createMutation.mutate(form); }} className="space-y-4">
            <div className="space-y-2"><Label>Nome</Label><Input value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} required /></div>
            <div className="space-y-2"><Label>Descrição</Label><Input value={form.descricao} onChange={(e) => setForm({ ...form, descricao: e.target.value })} /></div>
            <div className="flex gap-2 justify-end">
              <Button type="button" variant="outline" onClick={() => setShowForm(false)}>Cancelar</Button>
              <Button type="submit">{editing ? 'Atualizar' : 'Criar'}</Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminCategoriasPage;
