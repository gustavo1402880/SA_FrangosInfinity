package org.frangosInfinity.core.enums;

public enum StatusPedido
{
    PENDENTE(1),
    CONFIMADO(2),
    EM_PREPARO(3),
    PRONTO(4),
    ENTREGUE(5),
    CANCELADO(6),
    PAGO(7);

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
