package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

public class Caixa  extends Funcionario{
    public Caixa(String nome, String email, String senha, TipoUsuario usuario, String matricula, NivelAcesso nivelAcesso) {
        super(nome, email, senha, usuario,  matricula, nivelAcesso );
    }

    public Caixa() {

    }
}
