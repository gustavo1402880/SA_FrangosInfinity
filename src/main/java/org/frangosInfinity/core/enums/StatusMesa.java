package org.frangosInfinity.core.enums;

public enum StatusMesa
{
    LIVRE(1),
    OCUPADA(2),
    RESERVADA(3),
    MANUTENCAO(4);

    private int codigo;

    StatusMesa(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusMesa fromCodigo(int codigo) {
        for (StatusMesa tipo : StatusMesa.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
