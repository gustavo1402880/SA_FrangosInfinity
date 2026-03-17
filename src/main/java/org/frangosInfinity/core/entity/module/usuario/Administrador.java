package org.frangosInfinity.core.entity.module.usuario;

import jakarta.persistence.Entity;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.produto.Produto;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.util.Date;

@Entity
public class Administrador extends Funcionario
{
    public Administrador(String nome, String email, String senha, TipoUsuario usuario ,  String matricula)
    {
        super(nome, email, senha, usuario, matricula, NivelAcesso.ADMINISTRADOR);
    }

    public Administrador() {}
}
