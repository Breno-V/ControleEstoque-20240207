package com.controleestoque.api_estoque.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.controleestoque.api_estoque.Entitys.Fornecedor;
import com.controleestoque.api_estoque.Entitys.Produto;
import com.controleestoque.api_estoque.Repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

import com.controleestoque.api_estoque.Repositories.CategoriaRepository;
import com.controleestoque.api_estoque.Repositories.FornecedorRepository;
import com.controleestoque.api_estoque.Repositories.ItemVendaRepository;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ItemVendaRepository itemVendaRepository;
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
       // aqui criamos um novo conjunto para armazenar os fornecedores adicionados
        Set<Fornecedor> fornecedoresAdicionados = new HashSet<>();
        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            //para cada fornecedor no produto recebido, buscamos o fornecedor gerenciado pelo ID
            for (Fornecedor fornecedorReq : produto.getFornecedores()) {
                if (fornecedorReq != null && fornecedorReq.getId() != null) {
                    fornecedorRepository.findById(fornecedorReq.getId())
                    //se existir, adiciona ao conjunto de fornecedores Adicionados
                        .ifPresent(fornecedoresAdicionados::add);
                }
            }
        }
        // finalmente, associamos o conjunto de fornecedores gerenciados ao produto
        produto.setFornecedores(fornecedoresAdicionados);

        // 3. Gerenciamento do Estoque (1:1)
        if (produto.getEstoque() != null) {
            produto.getEstoque().setProduto(produto); // seta o produto no estoque
        }

        //4. Salva o produto
        Produto savedProduto = produtoRepository.save(produto);

        //5. Atualiza o lado inverso do relacionamento N:M
        for (Fornecedor f : savedProduto.getFornecedores()) {
            f.getProdutos().add(savedProduto);
            fornecedorRepository.save(f); // garante persistência da associação
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    //PUT /api/produtos
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        return produtoRepository.findById(id).map(produto -> {
            // Atualiza os campos necessários
            //Nome e preço
            produto.setNome(produtoDetails.getNome());
            produto.setPreco(produtoDetails.getPreco());

            //Categoria
            //se for fornecida uma categoria válida, atualiza a associação 
            if (produtoDetails.getCategoria() != null && produtoDetails.getCategoria().getId() != null) {
                categoriaRepository.findById(produtoDetails.getCategoria().getId()).ifPresent(produto::setCategoria);
            }

            //fornecedores

            if(produto.getFornecedores() != null) {
                produto.getFornecedores().forEach(f -> f.removeProduto(produto));
                produto.getFornecedores().clear();
            }
    
            for (var fornecedorReq : produtoDetails.getFornecedores()) {
                if (fornecedorReq != null && fornecedorReq.getId() != null) {
                    fornecedorRepository.findById(fornecedorReq.getId()).ifPresent(fornecedorGerenciado -> {
                        fornecedorGerenciado.addProduto(produto);
                    });
                }
            }     

            //Estoque
            if (produtoDetails.getEstoque() != null) {
                produto.getEstoque().setQuantidade(produtoDetails.getEstoque().getQuantidade());
            }

            Produto updatedProduto = produtoRepository.save(produto);

            for (Fornecedor f : produto.getFornecedores()) {
                fornecedorRepository.save(f); // garante que o relacionamento N:M seja persistido
            }
            return ResponseEntity.ok(updatedProduto);

        }).orElse(ResponseEntity.notFound().build());
    }

    //DELETE /api/produtos
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable Long id) {

        return produtoRepository.findById(id).map(produto -> {
            boolean isProdutoSold = itemVendaRepository.existsByProdutoId(id);

            if(isProdutoSold) {
                //Se o produto já foi vendido, não o excluímos fisicamente, apenas marcamos como inativo
                produto.setAtivo(false);
                produtoRepository.save(produto);
                return ResponseEntity.noContent().build();
            } else {
                //Se o produto nunca foi vendido, podemos excluí-lo fisicamente
                produtoRepository.delete(produto);
                return ResponseEntity.noContent().build();
            }
        })
        .orElse(ResponseEntity.notFound().build());
    }
}
