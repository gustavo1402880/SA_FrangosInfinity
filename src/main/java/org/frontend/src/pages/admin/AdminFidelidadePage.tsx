import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { PontosResponse, RegrasResponse } from '@/types';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Loader2, Award, Settings, RefreshCw } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const AdminFidelidadePage = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();

  const { data: contas = [], isLoading } = useQuery({
    queryKey: ['fidelidade-contas'],
    queryFn: async () => { try { const { data } = await api.get('/contas'); return data || []; } catch { return []; } },
  });

  const { data: regras } = useQuery({
    queryKey: ['fidelidade-regras'],
    queryFn: async () => { try { const { data } = await api.get('/admin/regras'); return data || []; } catch { return []; } },
  });

  const expirarMutation = useMutation({
    mutationFn: () => api.post('/admin/processar-expiracao'),
    onSuccess: (res) => {
      toast({ title: `${res.data} pontos expirados processados` });
      queryClient.invalidateQueries({ queryKey: ['fidelidade-contas'] });
    },
  });

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-heading font-bold flex items-center gap-2">
          <Award className="h-7 w-7 text-primary" />
          Programa de Fidelidade
        </h1>
        <Button variant="outline" onClick={() => expirarMutation.mutate()}>
          <RefreshCw className="h-4 w-4 mr-2" /> Processar Expiração
        </Button>
      </div>

      <Tabs defaultValue="contas">
        <TabsList>
          <TabsTrigger value="contas">Contas</TabsTrigger>
          <TabsTrigger value="regras">Regras</TabsTrigger>
        </TabsList>

        <TabsContent value="contas" className="space-y-4 mt-4">
          {isLoading ? (
            <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
          ) : contas.length === 0 ? (
            <p className="text-center py-12 text-muted-foreground">Nenhuma conta de fidelidade</p>
          ) : (
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {contas.map((c: PontosResponse) => (
                <Card key={c.id}>
                  <CardContent className="p-4 space-y-2">
                    <h3 className="font-heading font-semibold">{c.clienteNome || `Cliente #${c.clienteId}`}</h3>
                    <div className="grid grid-cols-3 gap-2 text-center">
                      <div>
                        <p className="text-xs text-muted-foreground">Atuais</p>
                        <p className="font-bold text-primary">{c.pontosAtuais}</p>
                      </div>
                      <div>
                        <p className="text-xs text-muted-foreground">Acumulados</p>
                        <p className="font-bold">{c.pontosAcumulados}</p>
                      </div>
                      <div>
                        <p className="text-xs text-muted-foreground">Resgatados</p>
                        <p className="font-bold">{c.pontosResgatados}</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        <TabsContent value="regras" className="space-y-4 mt-4">
          {(regras as RegrasResponse[] || []).map((r: RegrasResponse) => (
            <Card key={r.id}>
              <CardContent className="p-4 grid grid-cols-2 md:grid-cols-4 gap-4 text-center">
                <div>
                  <p className="text-xs text-muted-foreground">Ponto/Real</p>
                  <p className="font-bold">{r.pontoPorReal}</p>
                </div>
                <div>
                  <p className="text-xs text-muted-foreground">Valor/Ponto</p>
                  <p className="font-bold">R$ {r.valorPorPonto}</p>
                </div>
                <div>
                  <p className="text-xs text-muted-foreground">Mín. Resgate</p>
                  <p className="font-bold">{r.pontosMinimoResgate}</p>
                </div>
                <div>
                  <p className="text-xs text-muted-foreground">Validade</p>
                  <p className="font-bold">{r.validadeDias} dias</p>
                </div>
              </CardContent>
            </Card>
          ))}
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default AdminFidelidadePage;
