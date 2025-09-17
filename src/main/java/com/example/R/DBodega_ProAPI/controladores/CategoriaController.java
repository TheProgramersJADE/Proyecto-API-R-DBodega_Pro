package com.example.R.DBodega_ProAPI.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaGuardar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaModificar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.ICategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<CategoriaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<CategoriaSalida> categorias = categoriaService.obtenerTodosPaginados(pageable);
        if (categorias.hasContent()) {
            return ResponseEntity.ok(categorias);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<CategoriaSalida>> mostrarTodos() {
        List<CategoriaSalida> categorias = categoriaService.obtenerTodos();
        if (!categorias.isEmpty()) {
            return ResponseEntity.ok(categorias);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaSalida> buscarPorId(@PathVariable Integer id) {
        CategoriaSalida categoria = categoriaService.obtenerPorId(id);

        if (categoria != null) {
            return ResponseEntity.ok(categoria);
        }
        return ResponseEntity.notFound().build();
    }

    // Metodos POST, PUT y DELETE

    @PostMapping
    public ResponseEntity<CategoriaSalida> crear(@RequestBody CategoriaGuardar categoriaGuardar) {
        CategoriaSalida categoria = categoriaService.crear(categoriaGuardar);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaSalida> editar(@PathVariable Integer id,
            @RequestBody CategoriaModificar categoriaModificar) {
        CategoriaSalida categoria = categoriaService.editar(categoriaModificar);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        categoriaService.eliminarPorId(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<CategoriaSalida>> buscar(
            @RequestParam(defaultValue = "") String nombre,
            Pageable pageable) {
        Page<CategoriaSalida> categorias = categoriaService
                .findByNombreContainingIgnoreCaseOrderByIdDesc(nombre, pageable)
                .map(categoria -> categoriaService.obtenerPorId(categoria.getId()));

        if (categorias.hasContent()) {
            return ResponseEntity.ok(categorias);
        }
        return ResponseEntity.notFound().build();
    }

}
