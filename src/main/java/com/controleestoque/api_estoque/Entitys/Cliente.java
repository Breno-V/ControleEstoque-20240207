package com.controleestoque.api_estoque.Entitys;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_clientes")

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nome;
    private String email;
    private String telefone;

    // Relacionamento de clientes com vendas (1:N)//
    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas;

    // Construtores, getters e setters
    public Cliente() {}

    public Cliente(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}