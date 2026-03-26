import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Loader2, FileText, Download, BarChart3, TrendingUp, DollarSign } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const AdminRelatoriosPage = () => {
  const { toast } = useToast();
  const [inicio, setInicio] = useState('');
  const [fim, setFim] = useState('');

  const { data: relatorios = [], isLoading } = useQuery({
    queryKey: ['admin-relatorios'],
    queryFn: async () => { try { const { data } = await api.get('/relatorios'); return data || []; } catch { return []; } },
  });

  const handleGerarRelatorio = async () => {
    if (!inicio || !fim) { toast({ title: 'Selecione o período', variant: 'destructive' }); return; }
    try {
      await api.post('/relatorios', { dataInicio: inicio, dataFim: fim });
      toast({ title: 'Relatório gerado!' });
    } catch { toast({ title: 'Erro ao gerar relatório', variant: 'destructive' }); }
  };

  const handleExportPDF = async (id: number) => {
    try {
      const response = await api.get(`/relatorios/${id}/pdf`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `relatorio_${id}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch { toast({ title: 'Erro ao exportar PDF', variant: 'destructive' }); }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold flex items-center gap-2">
        <BarChart3 className="h-7 w-7 text-primary" />
        Relatórios
      </h1>

      <Card>
        <CardHeader><CardTitle className="text-base font-heading">Gerar Relatório</CardTitle></CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-4 items-end">
            <div className="space-y-2">
              <Label>Data Início</Label>
              <Input type="datetime-local" value={inicio} onChange={(e) => setInicio(e.target.value)} />
            </div>
            <div className="space-y-2">
              <Label>Data Fim</Label>
              <Input type="datetime-local" value={fim} onChange={(e) => setFim(e.target.value)} />
            </div>
            <Button onClick={handleGerarRelatorio}>Gerar Relatório</Button>
          </div>
        </CardContent>
      </Card>

      {isLoading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2">
          {relatorios.map((r: any) => (
            <Card key={r.id}>
              <CardContent className="p-4 space-y-3">
                <div className="flex items-center justify-between">
                  <FileText className="h-5 w-5 text-primary" />
                  <span className="text-xs text-muted-foreground">
                    {new Date(r.dataGeracao).toLocaleDateString('pt-BR')}
                  </span>
                </div>
                <div className="grid grid-cols-3 gap-2 text-center">
                  <div>
                    <p className="text-xs text-muted-foreground">Vendas</p>
                    <p className="font-bold text-sm">R$ {r.totalVendas?.toFixed(2)}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Pedidos</p>
                    <p className="font-bold text-sm">{r.quantidadePedidos}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Ticket Médio</p>
                    <p className="font-bold text-sm">R$ {r.ticketMedio?.toFixed(2)}</p>
                  </div>
                </div>
                <Button variant="outline" size="sm" className="w-full" onClick={() => handleExportPDF(r.id)}>
                  <Download className="h-4 w-4 mr-2" /> Exportar PDF
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminRelatoriosPage;
