package org.frangosInfinity.core.service.module.fidelidade;

import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.RegrasFidelidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegrasFidelidadeService
{
    @Autowired
    private RegrasFidelidadeRepository regrasFidelidadeRepository;

    @Transactional
    public RegrasFidelidade criarRegras(RegrasFidelidade regras)
    {
        if (regras.isAtivo())
        {
            regrasFidelidadeRepository.findByAtivoTrue().forEach(r ->
            {
            r.setAtivo(false);
            regrasFidelidadeRepository.save(r);
            });
        }

        return regrasFidelidadeRepository.save(regras);
    }

    @Transactional(readOnly = true)
    public RegrasFidelidade buscarRegrasAtivas()
    {
        return regrasFidelidadeRepository.buscarAtiva().orElseGet(() -> {
            RegrasFidelidade regras = new RegrasFidelidade();
            return regrasFidelidadeRepository.save(regras);
        });
    }

    @Transactional(readOnly = true)
    public RegrasFidelidade buscarPorId(Long id)
    {

        if (!validarId(id))
        {
            throw new BusinessException("ID inválido");
        }

        return regrasFidelidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Regras não encontradas"));
    }

    @Transactional(readOnly = true)
    public List<RegrasFidelidade> listarTodas()
    {
        return regrasFidelidadeRepository.listarTodasOrdenadas();
    }

    @Transactional
    public RegrasFidelidade atualizarRegras(Long id, RegrasFidelidade regrasAtualizadas)
    {
        RegrasFidelidade existente = regrasFidelidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Regras não encontradas"));


        existente.setPontosPorReal(regrasAtualizadas.getPontosPorReal());
        existente.setDiasExpiracao(regrasAtualizadas.getDiasExpiracao());
        existente.setPontosMinimosResgate(regrasAtualizadas.getPontosMinimosResgate());
        existente.setValorDescontoPorBloco(regrasAtualizadas.getValorDescontoPorBloco());
        existente.setPontosPorBloco(regrasAtualizadas.getPontosPorBloco());
        existente.setValorMinimoProdutoDesconto(regrasAtualizadas.getValorMinimoProdutoDesconto());

        if (regrasAtualizadas.isAtivo() && !existente.isAtivo())
        {
            regrasFidelidadeRepository.findByAtivoTrue().stream()
                    .filter(r -> r.getId().equals(id))
                    .forEach(r ->
                    {
                        r.setAtivo(false);
                        regrasFidelidadeRepository.save(r);
                    });
            existente.setAtivo(true);
        }
        else
        {
            existente.setAtivo(regrasAtualizadas.isAtivo());
        }

        return regrasFidelidadeRepository.save(existente);
    }

    @Transactional
    public RegrasFidelidade setAtivo(Long id, boolean ativo)
    {

        RegrasFidelidade regras = regrasFidelidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Regras não encontradas"));

        if (ativo && !regras.isAtivo())
        {
            regrasFidelidadeRepository.findByAtivoTrue().stream()
                    .filter(r -> r.getId().equals(id))
                    .forEach(r ->
                    {
                        r.setAtivo(false);
                        regrasFidelidadeRepository.save(r);
                    });
        }

        regras.setAtivo(ativo);

        return regrasFidelidadeRepository.save(regras);
    }

    @Transactional(readOnly = true)
    public void deletar(Long id)
    {
        RegrasFidelidade regras = regrasFidelidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Regras não encontradas"));


        if (regras.isAtivo())
        {
            throw new BusinessException("Não é possível deletar regras ativas");
        }

        regrasFidelidadeRepository.delete(regras);
    }

    private boolean validarId(Long id)
    {
        return id != null && id > 0;
    }
}
