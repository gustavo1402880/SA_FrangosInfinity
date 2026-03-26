package org.frangosInfinity.core.enums;

public enum StatusPagamento
{
    PENDENTE(1),
    CONFIRMADO(2),
    CANCELADO(3),
    ESTORNADO(4),
    REEMBOLSADO(5);

    private int codigo;

    StatusPagamento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusPagamento fromCodigo(int codigo) {
        for (StatusPagamento tipo : StatusPagamento.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
