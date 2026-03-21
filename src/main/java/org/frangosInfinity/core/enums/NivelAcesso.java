package org.frangosInfinity.core.enums;

public enum NivelAcesso
{
    ATENDENTE(1),
    CAIXA(2),
    COZINHEIRO(3),
    ADMINISTRADOR(4),
    CLIENTE(5);

    private int codigo;

    NivelAcesso(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static NivelAcesso fromCodigo(int codigo) {
        for (NivelAcesso tipo : NivelAcesso.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
