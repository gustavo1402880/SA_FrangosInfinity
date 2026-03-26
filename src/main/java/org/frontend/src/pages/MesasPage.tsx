import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { MesaResponse } from '@/types';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Loader2, MapPin, Users, Tablet, Download, Share2 } from 'lucide-react';
import { useAuthStore } from '@/stores/authStore';
import { useToast } from '@/hooks/use-toast';

const MesasPage = () => {
  const { user } = useAuthStore();
  const role = user?.nivelAcesso || (user?.tipoUsuario === 'CLIENTE' ? 'CLIENTE' : '');
  const navigate = useNavigate();
  const { toast } = useToast();
  const [selectedMesa, setSelectedMesa] = useState<MesaResponse | null>(null);
  const [qrCode, setQrCode] = useState<any>(null);
  const [generatingQR, setGeneratingQR] = useState(false);

  const isStaff = ['ADMINISTRADOR', 'ATENDENTE', 'CAIXA'].includes(role);

  // Staff can list all mesas; clients can only see available ones
  // NOTE: Backend requires ADMIN/ATENDENTE/CAIXA role for /mesas endpoints.
  // Clients access the cardápio via QR code scanned from a mesa.
  const { data: mesas = [], isLoading, error: mesasError } = useQuery({
    queryKey: ['mesas', isStaff ? 'all' : 'disponiveis'],
    queryFn: async () => {
      const endpoint = isStaff ? '/mesas' : '/mesas/disponiveis';
      const { data } = await api.get<MesaResponse[]>(endpoint);
      return data || [];
    },
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'DISPONIVEL': return 'bg-success text-success-foreground';
      case 'OCUPADA': return 'bg-warning text-warning-foreground';
      case 'RESERVADA': return 'bg-info text-info-foreground';
      case 'MANUTENCAO': return 'bg-destructive text-destructive-foreground';
      default: return 'bg-muted text-muted-foreground';
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'DISPONIVEL': return 'Disponível';
      case 'OCUPADA': return 'Ocupada';
      case 'RESERVADA': return 'Reservada';
      case 'MANUTENCAO': return 'Manutenção';
      default: return status;
    }
  };

  const handleSelectMesa = (mesa: MesaResponse) => {
    if (role === 'CLIENTE' && mesa.status !== 'DISPONIVEL') {
      toast({ title: 'Mesa ocupada', description: 'Selecione uma mesa disponível.', variant: 'destructive' });
      return;
    }
    setSelectedMesa(mesa);
  };

  const handleGenerateQR = async () => {
    if (!selectedMesa) return;
    setGeneratingQR(true);
    try {
      const { data } = await api.post(`/mesas/${selectedMesa.id}/qrcode`, {
        idMesa: selectedMesa.id,
      });
      setQrCode(data);
      toast({ title: 'QR Code gerado com sucesso!' });
    } catch (err: any) {
      toast({ title: 'Erro ao gerar QR Code', description: err.response?.data?.mensagem || 'Tente novamente.', variant: 'destructive' });
    } finally {
      setGeneratingQR(false);
    }
  };

  const handleClientStartOrder = async () => {
    if (!selectedMesa) return;
    // Client flow: navigate to cardápio with mesaId, pedido is created there
    navigate(`/cardapio?mesaId=${selectedMesa.id}`);
  };

  const handleDownloadQR = () => {
    if (!qrCode?.qrCodeBase64) return;
    const link = document.createElement('a');
    link.href = `data:image/png;base64,${qrCode.qrCodeBase64}`;
    link.download = `qrcode-mesa-${selectedMesa?.numero}.png`;
    link.click();
  };

  const handleShareLink = async () => {
    if (!qrCode?.url) return;
    try {
      await navigator.clipboard.writeText(qrCode.url);
      toast({ title: 'Link copiado!' });
    } catch {
      toast({ title: 'Link', description: qrCode.url });
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  // If client has no access to mesas (403), show a message
  if (role === 'CLIENTE' && mesasError) {
    return (
      <div className="max-w-md mx-auto text-center space-y-6 py-12">
        <Tablet className="h-16 w-16 mx-auto text-muted-foreground" />
        <h1 className="text-2xl font-heading font-bold">Acesso ao Cardápio</h1>
        <p className="text-muted-foreground">
          Escaneie o QR Code na sua mesa para acessar o cardápio e fazer seu pedido.
        </p>
        <p className="text-sm text-muted-foreground">
          Solicite ao atendente o QR Code da sua mesa.
        </p>
      </div>
    );
  }

  // QR Code display view
  if (qrCode) {
    return (
      <div className="max-w-md mx-auto text-center space-y-6">
        <h1 className="text-2xl font-heading font-bold">QR Code - Mesa {selectedMesa?.numero}</h1>
        <p className="text-muted-foreground">Escaneie o QR Code para iniciar o pedido</p>
        <div className="bg-card p-8 rounded-lg border">
          {qrCode.qrCodeBase64 ? (
            <img src={`data:image/png;base64,${qrCode.qrCodeBase64}`} alt="QR Code" className="mx-auto w-64 h-64" />
          ) : (
            <div className="w-64 h-64 mx-auto bg-muted rounded flex items-center justify-center">
              <p className="text-muted-foreground">QR Code</p>
            </div>
          )}
        </div>
        {qrCode.url && (
          <p className="text-sm text-muted-foreground break-all">{qrCode.url}</p>
        )}
        <div className="flex gap-3 justify-center flex-wrap">
          <Button variant="outline" onClick={() => setQrCode(null)}>Voltar</Button>
          {qrCode.qrCodeBase64 && (
            <Button variant="outline" onClick={handleDownloadQR}>
              <Download className="h-4 w-4 mr-2" /> Baixar QR Code
            </Button>
          )}
          {qrCode.url && (
            <Button variant="outline" onClick={handleShareLink}>
              <Share2 className="h-4 w-4 mr-2" /> Compartilhar Link
            </Button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-heading font-bold">Mesas</h1>
          <p className="text-muted-foreground">
            {role === 'CLIENTE' ? 'Selecione uma mesa para iniciar seu pedido' : 'Gerenciamento de mesas'}
          </p>
        </div>
      </div>

      {mesas.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground">
          <Tablet className="h-12 w-12 mx-auto mb-3" />
          <p>Nenhuma mesa encontrada</p>
        </div>
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {mesas.map((mesa) => (
            <Card
              key={mesa.id}
              className={`cursor-pointer transition-all hover:shadow-md ${
                selectedMesa?.id === mesa.id ? 'ring-2 ring-primary' : ''
              } ${mesa.status !== 'DISPONIVEL' && role === 'CLIENTE' ? 'opacity-50' : ''}`}
              onClick={() => handleSelectMesa(mesa)}
            >
              <CardContent className="p-4 text-center space-y-2">
                <Tablet className="h-8 w-8 mx-auto text-muted-foreground" />
                <h3 className="font-heading font-bold text-lg">Mesa {mesa.numero}</h3>
                <Badge className={getStatusColor(mesa.status)}>
                  {getStatusLabel(mesa.status)}
                </Badge>
                <div className="flex items-center justify-center gap-1 text-sm text-muted-foreground">
                  <Users className="h-3 w-3" />
                  <span>{mesa.capacidade} lugares</span>
                </div>
                {mesa.localizacao && (
                  <div className="flex items-center justify-center gap-1 text-xs text-muted-foreground">
                    <MapPin className="h-3 w-3" />
                    <span>{mesa.localizacao}</span>
                  </div>
                )}
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {selectedMesa && (
        <div className="fixed bottom-4 left-1/2 -translate-x-1/2 bg-card border rounded-lg shadow-lg p-4 flex items-center gap-4 z-40">
          <p className="font-medium">Mesa {selectedMesa.numero} selecionada</p>
          {role === 'CLIENTE' ? (
            <Button onClick={handleClientStartOrder}>Iniciar Pedido</Button>
          ) : (
            <div className="flex gap-2">
              <Button variant="outline" onClick={handleGenerateQR} disabled={generatingQR}>
                {generatingQR ? <Loader2 className="h-4 w-4 animate-spin mr-2" /> : null}
                Gerar QR Code
              </Button>
              <Button onClick={() => navigate(`/pedidos?mesaId=${selectedMesa.id}`)}>
                Ver Pedidos
              </Button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default MesasPage;
