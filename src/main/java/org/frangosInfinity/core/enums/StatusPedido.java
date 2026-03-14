package org.frangosInfinity.core.enums;

public enum StatusPedido
{
    CONFIMADO(1),
    EM_PREPARO(2),
    PRONTO(3),
    ENTREGUE(4),
    CANCELADO(5),
    PAGO(6);

    private int codigo;

    StatusPedido(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusPedido fromCodigo(int codigo) {
        for (StatusPedido tipo : StatusPedido.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
