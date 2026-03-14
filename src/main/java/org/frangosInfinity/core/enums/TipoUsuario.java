package org.frangosInfinity.core.enums;

public enum TipoUsuario
{
    CLIENTE(1),
    FUNCIONARIO(2);

    private Integer codigo;

    TipoUsuario(Integer codigo) {this.codigo = codigo;}

    public int getCodigo() {
        return codigo;
    }

    public static TipoUsuario fromCodigo(int codigo)
    {
        for (TipoUsuario tipo : TipoUsuario.values())
        {
            if (tipo.codigo == codigo)
            {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
