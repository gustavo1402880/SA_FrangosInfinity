import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Loader2, Plus, Pencil, UserX, UserCheck } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

const AdminUsuariosPage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ nome: '', email: '', telefone: '', senha: '', nivelAcesso: 'ATENDENTE' });

  const { data: usuarios = [], isLoading } = useQuery({
    queryKey: ['admin-usuarios'],
    queryFn: async () => { try { const { data } = await api.get('/usuarios'); return data || []; } catch { return []; } },
  });

  const createMutation = useMutation({
    mutationFn: (data: any) => api.post('/usuarios/funcionarios', data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['admin-usuarios'] });
      toast({ title: 'Funcionário criado!' });
      setShowForm(false);
    },
    onError: () => toast({ title: 'Erro ao criar funcionário', variant: 'destructive' }),
  });

  const toggleMutation = useMutation({
    mutationFn: ({ id, ativo }: { id: number; ativo: boolean }) =>
      api.patch(`/usuarios/${id}/${ativo ? 'ativar' : 'desativar'}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-usuarios'] }),
  });

  const getRoleBadge = (nivel: string) => {
    const colors: Record<string, string> = {
      ADMINISTRADOR: 'bg-primary text-primary-foreground',
      ATENDENTE: 'bg-info text-info-foreground',
      COZINHEIRO: 'bg-warning text-warning-foreground',
      CAIXA: 'bg-success text-success-foreground',
    };
    return colors[nivel] || 'bg-muted text-muted-foreground';
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-heading font-bold">Gestão de Usuários</h1>
        <Button onClick={() => setShowForm(true)}>
          <Plus className="h-4 w-4 mr-2" /> Novo Funcionário
        </Button>
      </div>

      {isLoading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {usuarios.map((u: any) => (
            <Card key={u.id}>
              <CardContent className="p-4 space-y-2">
                <div className="flex items-start justify-between">
                  <div>
                    <h3 className="font-heading font-semibold">{u.nome}</h3>
                    <p className="text-sm text-muted-foreground">{u.email}</p>
                    {u.matricula && <p className="text-xs text-muted-foreground">Matrícula: {u.matricula}</p>}
                  </div>
                  <div className="flex flex-col items-end gap-1">
                    <Badge className={getRoleBadge(u.nivelAcesso)}>{u.nivelAcesso || u.tipoUsuario}</Badge>
                    <Badge variant={u.ativo ? 'default' : 'secondary'}>{u.ativo ? 'Ativo' : 'Inativo'}</Badge>
                  </div>
                </div>
                <div className="flex justify-end">
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => toggleMutation.mutate({ id: u.id, ativo: !u.ativo })}
                  >
                    {u.ativo ? <><UserX className="h-4 w-4 mr-1" /> Desativar</> : <><UserCheck className="h-4 w-4 mr-1" /> Ativar</>}
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Dialog open={showForm} onOpenChange={() => setShowForm(false)}>
        <DialogContent>
          <DialogHeader><DialogTitle className="font-heading">Novo Funcionário</DialogTitle></DialogHeader>
          <form onSubmit={(e) => { e.preventDefault(); createMutation.mutate(form); }} className="space-y-4">
            <div className="space-y-2"><Label>Nome</Label><Input value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} required /></div>
            <div className="space-y-2"><Label>Email</Label><Input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required /></div>
            <div className="space-y-2"><Label>Telefone</Label><Input value={form.telefone} onChange={(e) => setForm({ ...form, telefone: e.target.value })} /></div>
            <div className="space-y-2"><Label>Senha</Label><Input type="password" value={form.senha} onChange={(e) => setForm({ ...form, senha: e.target.value })} required /></div>
            <div className="space-y-2">
              <Label>Nível de Acesso</Label>
              <Select value={form.nivelAcesso} onValueChange={(v) => setForm({ ...form, nivelAcesso: v })}>
                <SelectTrigger><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="ATENDENTE">Atendente</SelectItem>
                  <SelectItem value="COZINHEIRO">Cozinheiro</SelectItem>
                  <SelectItem value="CAIXA">Caixa</SelectItem>
                  <SelectItem value="ADMINISTRADOR">Administrador</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex gap-2 justify-end">
              <Button type="button" variant="outline" onClick={() => setShowForm(false)}>Cancelar</Button>
              <Button type="submit" disabled={createMutation.isPending}>Criar</Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminUsuariosPage;
