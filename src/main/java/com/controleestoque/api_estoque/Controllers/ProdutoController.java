package com.controleestoque.api_estoque.Controllers;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.controleestoque.api_estoque.Entitys.Produto;
import com.controleestoque.api_estoque.Repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

import com.controleestoque.api_estoque.Repositories.CategoriaRepository;
import com.controleestoque.api_estoque.Repositories.FornecedorRepository;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaRepository categoriaRepository;
    // Estoque normalmente é manipulado via Produto ou separadamente

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getCategoriaById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST /api/produtos
    /*Neste método, assumimos que a Categoria e o Fornecedor já existem
     * e seus IDs são passados no corpo da requisição(ProdutoDTO seria o ideal aqui)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        //1. Gerenciamento do 1:N (Categoria)
        //A categoria deve ser buscada para garantir que existe e estar no contexto da persistência
        if(produto.getCategoria() == null || produto.getCategoria().getId() == null){
            return ResponseEntity.badRequest().build(); //Categoria é obrigatória
        }

        categoriaRepository.findById(produto.getCategoria().getId()).ifPresent(produto::setCategoria); //associa a categoria gerenciada

        /*2. Gerenciamento do N:M (Fornecedores)
         * Busca todos os fornecedeores pelo ID fornecido
        */
        if(produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            //cria um Set para armazenar os fornecedores gerenciados
            produto.getFornecedores().clear();
            
            //Aqui ou em um projeto real. você buscaria Fornecedores um por um ou usando o método customizado do repositório
            //Exemplo simplificado:
            produto.getFornecedores().forEach(fornecedor -> {
                fornecedorRepository.findById(fornecedor.getId()).ifPresent(produto.getFornecedores()::add); //Adiciona o fornecedor gerenciado
            });
        }

        //3. Salva o produto (e o estoque, se o CASCADE estiver configurado)
        Produto savedProduto = produtoRepository.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    //PUT /api/produtos
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setNome(produtoDetails.getNome());
            Produto updatedProduto = produtoRepository.save(produto);
            return ResponseEntity.ok(updatedProduto);
        }).orElse(ResponseEntity.notFound().build());
    }

    //DELETE /api/produtos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if(!produtoRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
