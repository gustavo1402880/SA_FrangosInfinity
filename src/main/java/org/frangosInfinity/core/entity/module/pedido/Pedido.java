package org.frangosInfinity.core.entity.module.pedido;
import jakarta.persistence.*;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", unique = true, nullable = false, length = 20)
    private String numeroPedido;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "mesa_id", nullable = false)
    private Long mesaId;

    @Column(name = "atendente_id", nullable = false)
    private Long atendenteId;

    @Column(nullable = false)
    private String tipo;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubPedido> subPedidos;

    public Pedido()
    {
        this.numeroPedido = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.dataHora = LocalDateTime.now();
    }

    public Pedido(String tipo, Long mesaId, Long atendenteId) {
        this();
        this.tipo = tipo;
        this.mesaId = mesaId;
        this.atendenteId = atendenteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId) {
        this.atendenteId = atendenteId;
    }

    public Long getMesaId() {
        return mesaId;
    }

    public void setMesaId(Long mesa_id) {
        this.mesaId = mesa_id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<SubPedido> getSubPedidos()
    {
        return subPedidos;
    }

    public void setSubPedidos(List<SubPedido> subPedidos)
    {
        this.subPedidos = subPedidos;
    }

    public void adicionarSubPedido(SubPedido subPedido)
    {
        subPedidos.add(subPedido);
        subPedido.setPedido(this);
    }

    public Double getValorTotal()
    {
        return subPedidos.stream()
                .mapToDouble(SubPedido::getValorTotal)
                .sum();
    }

    public Boolean isPedidoCompartilhado()
    {
        return subPedidos.size() > 1;
    }
}
