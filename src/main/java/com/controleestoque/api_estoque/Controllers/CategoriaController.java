package com.controleestoque.api_estoque.Controllers;

import com.controleestoque.api_estoque.Repositories.CategoriaRepository;
import com.controleestoque.api_estoque.Entitys.Categoria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorias") // define o caminho base da rota categoria
@RequiredArgsConstructor // injeta automaticamente o CategoriaRepository via construtor
public class CategoriaController {
    private final CategoriaRepository categoriaRepository;

    // GET da /api/categorias
    @GetMapping
    public List<Categoria> getAllCategorias() {
        // retorna todas as categorias do banco de dados
        return categoriaRepository.findAll();
    }

    // GET da /api/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        // Busca a categoria pelo ID. usa orElse para retornar 404 caso não encontre
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST /api/categorias
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //retorna código 201 (created)
    public Categoria createCategoria(@RequestBody Categoria categoria) {
        //salva uma nova categoria no banco de dados
        return categoriaRepository.save(categoria);
    }

    //PUT /api/categorias
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoriaDetails) {
        //tenta encontrar categoria existente
        return categoriaRepository.findById(id).map(categoria -> {
            //atualiza os dados da categoria encontrada
            categoria.setNome(categoriaDetails.getNome());
            Categoria updateCategoria = categoriaRepository.save(categoria);
            return ResponseEntity.ok(updateCategoria);
        }).orElse(ResponseEntity.notFound().build());
    }

    //DELETE /api/categorias
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id){
        //tenta encontrar e deletar categoria
        if(!categoriaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //retorna código 204 (no content)

    }
}
