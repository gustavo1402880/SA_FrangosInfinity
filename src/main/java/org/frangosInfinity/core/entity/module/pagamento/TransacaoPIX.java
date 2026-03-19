package org.frangosInfinity.core.entity.module.pagamento;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao_pix")
public class TransacaoPIX
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false, unique = true)
    private Pagamento pagamento;

    @Column(name = "qr_code", length = 500)
    private String qrCode;

    @Column(name = "codigo_copia_cola", length = 500)
    private String codigoCopiaCola;

    @Column(name = "tempo_expiracao_segundos", nullable = false)
    private Integer tempoExpiracaoSegundos;

    @Column(name = "chave_pix", length = 50)
    private LocalDateTime dataExpiracao;

    public TransacaoPIX() {}

    public TransacaoPIX(Pagamento pagamento, String qrCode, String codigoCopiaCola)
    {
        this.pagamento = pagamento;
        this.qrCode = qrCode;
        this.codigoCopiaCola = codigoCopiaCola;
        this.tempoExpiracaoSegundos = 600;
        this.dataExpiracao = LocalDateTime.now().plusSeconds(tempoExpiracaoSegundos);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCodigoCopiaCola() {
        return codigoCopiaCola;
    }

    public void setCodigoCopiaCola(String codigoCopiaCola) {
        this.codigoCopiaCola = codigoCopiaCola;
    }

    public Integer getTempoExpiracaoSegundos() {
        return tempoExpiracaoSegundos;
    }

    public void setTempoExpiracaoSegundos(Integer tempoExpiracaoSegundos) {
        this.tempoExpiracaoSegundos = tempoExpiracaoSegundos;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Boolean idExpirado()
    {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }

    public void renovar()
    {
        this.dataExpiracao = LocalDateTime.now().plusSeconds(tempoExpiracaoSegundos);
    }

    @Override
    public String toString()
    {
        return "TransacaoPix{" +
                "id=" + id +
                ", expira em=" + dataExpiracao +
                ", codigo='" + codigoCopiaCola + '\'' +
                '}';
    }

}
