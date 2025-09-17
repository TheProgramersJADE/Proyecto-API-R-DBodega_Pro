package com.example.R.DBodega_ProAPI.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<?> mostrarTodosPaginados(Pageable pageable) {
        Page<CategoriaSalida> categorias = categoriaService.obtenerTodosPaginados(pageable);

        if (categorias.hasContent()) {
        return buildResponse(true, "Categorías obtenidas correctamente", categorias, HttpStatus.OK);
        }

            return buildResponse(false, "No se encontraron categorías", null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> mostrarTodos() {
        List<CategoriaSalida> categorias = categoriaService.obtenerTodos();

        if (!categorias.isEmpty()) {
        return buildResponse(true, "Categorías obtenidas correctamente", categorias, HttpStatus.OK);
        }

            return buildResponse(false, "No se encontraron categorías", null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
       // Validación de ID inválido
    if (id <= 0) {
        return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
    }

    try {
        CategoriaSalida categoria = categoriaService.obtenerPorId(id);

        if (categoria != null) {
            // Registro encontrado
            return buildResponse(true, "Categoría encontrada", categoria, HttpStatus.OK);
        } else {
            // Registro no encontrado
            return buildResponse(false, "Categoría no encontrada con el ID: " + id, null, HttpStatus.NOT_FOUND);
        }
    } catch (Exception ex) {
        // Manejo de errores inesperados
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    // Metodos POST, PUT y DELETE

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CategoriaGuardar categoriaGuardar) {
         if (categoriaGuardar == null || categoriaGuardar.getNombre() == null || categoriaGuardar.getNombre().isBlank()) {
            return buildResponse(false, "El nombre de la categoría es obligatorio", null, HttpStatus.BAD_REQUEST);
        }
        if (categoriaGuardar.getNombre().length() > 100) {
            return buildResponse(false, "El nombre de la categoría no puede superar 100 caracteres", null, HttpStatus.BAD_REQUEST);
        }

        CategoriaSalida categoria = categoriaService.crear(categoriaGuardar);
        return buildResponse(true, "Categoría creada correctamente", categoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id,
            @RequestBody CategoriaModificar categoriaModificar) {

    if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        
    CategoriaSalida categoriaExistente = categoriaService.obtenerPorId(id);
        if (categoriaExistente == null) {
            return buildResponse(false, "No se puede editar, categoría no encontrada con ID: " + id, null,
                    HttpStatus.NOT_FOUND);
        }

        // 🔹 Asegúrate de que el DTO tenga el ID correcto antes de enviar al servicio
    categoriaModificar.setId(id);

        CategoriaSalida categoria = categoriaService.editar(categoriaModificar);
        return buildResponse(true, "Categoría editada correctamente", categoria, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }

        CategoriaSalida categoriaExistente = categoriaService.obtenerPorId(id);
        if (categoriaExistente == null) {
            return buildResponse(false, "No se puede eliminar, categoría no encontrada con ID: " + id, null,
                    HttpStatus.NOT_FOUND);
        }

        categoriaService.eliminarPorId(id);
        return buildResponse(true, "Categoría eliminada correctamente", null, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String nombre,
            Pageable pageable) {
         if (nombre.length() > 100) {
            return buildResponse(false, "El nombre de búsqueda es demasiado largo", null, HttpStatus.BAD_REQUEST);
        }

        Page<CategoriaSalida> categorias = categoriaService
                .findByNombreContainingIgnoreCaseOrderByIdDesc(nombre, pageable)
                .map(categoria -> categoriaService.obtenerPorId(categoria.getId()));

        if (categorias.hasContent()) {
        return buildResponse(true, "Búsqueda realizada correctamente", categorias, HttpStatus.OK);
        }
         return buildResponse(false, "No se encontraron categorías con el nombre: " + nombre, null,
                    HttpStatus.NOT_FOUND);
    }


    // ✅ Manejador genérico de excepciones para evitar "errores grandes"
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepciones(Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ✅ Método utilitario para respuestas consistentes
    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String mensaje, Object data,
            HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("mensaje", mensaje);
        if (data != null) {
            response.put("data", data);
        }
        return ResponseEntity.status(status).body(response);
    }

}
