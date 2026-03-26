export interface MesaResponse {
  id: number;
  numero: number;
  capacidade: number;
  localizacao: string;
  status: 'DISPONIVEL' | 'OCUPADA' | 'RESERVADA' | 'MANUTENCAO';
  sucesso: boolean;
  mensagem?: string;
}

export interface QRCodeResponse {
  id: number;
  idMesa: number;
  url: string;
  tokenSessao: string;
  qrCodeBase64: string;
  dataGeracao: string;
  dataExpiracao: string;
  ativo: boolean;
  sucesso: boolean;
  mensagem?: string;
}

export interface CategoriaResponse {
  id: number;
  nome: string;
  descricao?: string;
  ativa: boolean;
  ordem?: number;
}

export interface ProdutoResponse {
  id: number;
  codigo: string;
  nome: string;
  descricao: string;
  preco: number;
  custo?: number;
  tempoPreparo?: number;
  imagemUrl?: string;
  categoriaId: number;
  categoriaNome?: string;
  disponivel: boolean;
  estoque?: number;
  quantidadeVendida?: number;
  precoAnterior?: number;
  precoPendente?: number;
}

export interface PedidoResponse {
  id: number;
  numeroPedido: string;
  mesaId: number;
  mesaNumero?: number;
  status: string;
  dataCriacao: string;
  dataAtualizacao?: string;
  valorTotal: number;
  sucesso: boolean;
  mensagem?: string;
}

export interface SubPedidoResponse {
  id: number;
  pedidoId: number;
  clienteID: number;
  clienteNome?: string;
  numeroPedido?: string;
  status: StatusPedido;
  itens: ItemPedidoResponse[];
  valorTotal: number;
  dataCriacao: string;
  dataAtualizacao?: string;
  tempoEstimado?: number;
}

export interface ItemPedidoResponse {
  id: number;
  produtoId: number;
  produtoNome: string;
  quantidade: number;
  precoUnitario: number;
  subtotal: number;
  observacao?: string;
}

export type StatusPedido =
  | 'PENDENTE'
  | 'CONFIRMADO'
  | 'EM_PREPARO'
  | 'PRONTO'
  | 'ENTREGUE'
  | 'CANCELADO';

export interface PagamentoResponse {
  id: number;
  subPedidoId: number;
  valor: number;
  tipo: TipoPagamento;
  status: StatusPagamento;
  dataCriacao: string;
  dataConfirmacao?: string;
}

export type TipoPagamento = 'DINHEIRO' | 'CARTAO_DEBITO' | 'CARTAO_CREDITO' | 'PIX';
export type StatusPagamento = 'PENDENTE' | 'CONFIRMADO' | 'CANCELADO' | 'REEMBOLSADO';

export interface PIXResponse {
  id: number;
  pagamentoId: number;
  qrCodeBase64: string;
  codigoPix: string;
  valor: number;
  expiracao: string;
}

export interface ComprovanteResponse {
  id: number;
  pagamentoId: number;
  numero: string;
  texto: string;
  valor: number;
  formaPagamento: TipoPagamento;
  dataCriacao: string;
}

export interface Notificacao {
  id: number;
  tipo: string;
  mensagem: string;
  destinatario: string;
  lida: boolean;
  dataCriacao: string;
}

export interface RelatorioResponse {
  id: number;
  dataInicio: string;
  dataFim: string;
  dataGeracao: string;
  totalVendas: number;
  quantidadePedidos: number;
  ticketMedio: number;
  produtosMaisVendidos?: any[];
}

export interface PontosResponse {
  id: number;
  clienteId: number;
  clienteNome?: string;
  pontosAtuais: number;
  pontosAcumulados: number;
  pontosResgatados: number;
  sucesso: boolean;
  mensagem?: string;
}

export interface RegrasResponse {
  id: number;
  pontoPorReal: number;
  valorPorPonto: number;
  pontosMinimoResgate: number;
  validadeDias: number;
  ativo: boolean;
}

export interface IotConfig {
  id: number;
  mesaId: number;
  ip: string;
  porta: number;
  online: boolean;
  versaoFirmware?: string;
}
