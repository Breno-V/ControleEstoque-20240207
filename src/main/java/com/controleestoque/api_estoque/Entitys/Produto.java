package com.controleestoque.api_estoque.Entitys;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_produtos")

public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; //chave primária
    private String nome;
    private BigDecimal preco;
    private boolean ativo = true;

    //Relacionamento 1:1 (um-para-um)//
    //Mapeamento: Um produto tem UM registro de estoque (e vice-versa)
    //'mappedBy' indica que a chave estrangeira está na classe Estoque.
    //Cascade.ALL: Operações (como salvar) em Produto afetam Estoque
    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Estoque estoque;

    //Relacionamento N:1 (muitos-para-um)//
    //Mapeamento: Muitos produtos tem UMA categoria.
    //É o lado 'N' (Muitos) que contém a chave estrangeira (FK)
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: Carrega  acategoria apenas quando for solicitada
    @JoinColumn(name = "categoria_id", nullable = false) //define a FK na tabela 'tb_produtos'
    @JsonBackReference
    private Categoria categoria;

    //Relacionamento N:M (Muito-para-Muito)//
    //Mapeamento: Muitos produtos têm MUITOS fornceedores (e vice-versa).
    //define a tabela intermediária 'tb_produto_fornecedor' e as colunas da união
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tb_produto_fornecedor", //nome da tabela de junção
        joinColumns = @JoinColumn(name = "produto_id"), // FK desta entidade na tabela de junção
        inverseJoinColumns = @JoinColumn(name = "fornecedor_id") //FK da outra entidade
    )
    private Set<Fornecedor> fornecedores = new HashSet<>();

    //Construtores, Getters e setters
    public Produto(){}

    public Produto(String nome, BigDecimal preco, Estoque estoque, Categoria categoria, Set<Fornecedor> fornecedores) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.fornecedores = fornecedores;

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(Set<Fornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }}

