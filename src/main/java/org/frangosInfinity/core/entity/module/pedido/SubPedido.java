package org.frangosInfinity.core.entity.module.pedido;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sub_pedido")
public class SubPedido
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteID;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false)
    private StatusPedido status;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "tempo_preparo_minutos")
    private Integer tempo_em_minutos;

    @Column(length = 500)
    private String obsevacoes;

    @OneToMany(mappedBy = "subPedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    public SubPedido()
    {
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
    }

    public SubPedido(Pedido pedidoHub, Long clienteID)
    {
        this();
        this.pedido = pedidoHub;
        this.clienteID = clienteID;
    }

    public void setObsevacoes(String obsevacoes) {
        this.obsevacoes = obsevacoes;
    }

    public int getTempo_em_minutos() {
        return tempo_em_minutos;
    }

    public void setTempo_em_minutos(int tempo_em_minutos) {
        this.tempo_em_minutos = tempo_em_minutos;
    }

    public String getObsevacoes() {
        return obsevacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Long getClienteID() {
        return clienteID;
    }

    public void setClienteID(Long clienteID) {
        this.clienteID = clienteID;
    }

    public LocalDateTime getDate() {
        return dataHora;
    }

    public void setDate(LocalDateTime date) {
        this.dataHora = date;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemPedido item)
    {
        itens.add(item);
    }

    public void recalcularValorTotal()
    {
        this.valorTotal = itens.stream()
                .mapToDouble(ItemPedido::getSubTotal)
                .max()
                .orElse(0);
    }

    public void recalcularTempoPreparo()
    {
        this.tempo_em_minutos = itens.stream()
                .mapToInt(ItemPedido::getTempoPreparoEstimado)
                .max()
                .orElse(0);
    }
}
