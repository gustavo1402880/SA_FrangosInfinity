package org.frangosInfinity.core.entity.module.usuario;

import jakarta.persistence.Entity;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

@Entity
public class Atendente extends Funcionario
{
    public Atendente(String nome, String email, String senha, TipoUsuario usuario, String matricula)
    {
        super(nome, email, senha, usuario, matricula, NivelAcesso.ATENDENTE);
    }

    public Atendente()
    {

    }
}
