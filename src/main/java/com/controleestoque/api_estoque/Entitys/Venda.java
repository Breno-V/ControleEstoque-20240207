package com.controleestoque.api_estoque.Entitys;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_vendas")

public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens;

    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;

    public Venda() {
    }

    public Venda(Cliente cliente, List<ItemVenda> itens, LocalDateTime dataVenda, BigDecimal valorTotal) {
        this.cliente = cliente;
        this.itens = itens;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }   

    

}
