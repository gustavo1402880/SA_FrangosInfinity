package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Cozinheiro extends Funcionario
{
    public Cozinheiro(String nome, String email, String senha, TipoUsuario usuario,  String matricula, NivelAcesso nivelAcesso) {
        super(nome, email, senha, usuario,  matricula, nivelAcesso );
    }

    public Cozinheiro() {}
}
