package org.frangosInfinity.core.enums;

public enum TipoTransacaoPontos
{
    ACUMULO(1),
    RESGATE(2),
    EXPIRACAO(3);

    private int codigo;

    TipoTransacaoPontos(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoTransacaoPontos fromCodigo(int codigo) {
        for (TipoTransacaoPontos tipo : TipoTransacaoPontos.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
