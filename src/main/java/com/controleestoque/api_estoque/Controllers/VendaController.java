package com.controleestoque.api_estoque.Controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import com.controleestoque.api_estoque.Entitys.*;
import com.controleestoque.api_estoque.Repositories.ClienteRepository;
import com.controleestoque.api_estoque.Repositories.ItemVendaRepository;
import com.controleestoque.api_estoque.Repositories.ProdutoRepository;
import com.controleestoque.api_estoque.Repositories.VendaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {
    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    //este ? entre as tags indica que o corpo da resposta pode ser de qualquer tipo
    public ResponseEntity<?> createVenda(@RequestBody Venda venda) {

        //Validar cliente
        Cliente cliente = clienteRepository.findById(venda.getCliente().getId()).orElse(null);

        if(cliente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado.");
        }
        venda.setCliente(cliente);

        //Validar itens (estoque)
        for(ItemVenda item : venda.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId()).orElse(null);

            if(produto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produto com ID " + item.getProduto().getId() + " não encontrado.");
            }

            Estoque estoque = produto.getEstoque();

            if(estoque.getQuantidade() < item.getQuantidade()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estoque insuficiente para o produto: " + produto.getNome());
            }

            //Atualizar estoque
            estoque.setQuantidade(estoque.getQuantidade() - item.getQuantidade());
            produto.setEstoque(estoque);
            produtoRepository.save(produto);

            //define preço unitário
            item.setPrecoUnitario(produto.getPreco());
        }

        //Salvar venda
        Venda novaVenda = vendaRepository.save(venda);

        //Salvar itens da venda
        for(ItemVenda item : venda.getItens()) {
            item.setVenda(novaVenda);
            itemVendaRepository.save(item);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }
}
