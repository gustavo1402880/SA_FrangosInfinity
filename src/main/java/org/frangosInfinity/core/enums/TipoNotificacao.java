package org.frangosInfinity.core.enums;

public enum TipoNotificacao
{
    ALERTA_DEMORA(1),
    INFO_PEDIDO(2),
    ERRO_SISTEMA(3),
    ESTOQUE_BAIXO(4),
    PEDIDO_CONFIRMADO(5),
    PEDIDO_PREPERANDO(6),
    PEDIDO_PRONTO(7),
    PAGAMENTO_CONFIRMADO(8);


    private int codigo;

    TipoNotificacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoNotificacao fromCodigo(int codigo) {
        for (TipoNotificacao tipo : TipoNotificacao.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
