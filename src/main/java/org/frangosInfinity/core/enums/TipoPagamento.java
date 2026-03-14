package org.frangosInfinity.core.enums;

public enum TipoPagamento
{
    PIX(1),
    DINHEIRO(2),
    CARTAO_DEBITO(3),
    CARTAO_CREDITO(4),
    VOUCHER(5);

    private int codigo;

    TipoPagamento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoPagamento fromCodigo(int codigo) {
        for (TipoPagamento tipo : TipoPagamento.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}


