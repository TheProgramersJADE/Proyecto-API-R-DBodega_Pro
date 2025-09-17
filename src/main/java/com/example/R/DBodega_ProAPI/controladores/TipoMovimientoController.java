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
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoGuardar;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoModificar;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.ITipoMovimientoService;

@RestController
@RequestMapping("/api/tipoMovimientos")
public class TipoMovimientoController {

    @Autowired
    private ITipoMovimientoService tipoMovimientoService;

    @GetMapping
    public ResponseEntity<?> mostrarTodosPaginados(Pageable pageable) {
        try {
        Page<TipoMovimientoSalida> tiposMovimientos = tipoMovimientoService.obtenerTodosPaginados(pageable);
        if (tiposMovimientos.hasContent()) {
                return buildResponse(true, "Tipos de movimiento obtenidos correctamente", tiposMovimientos, HttpStatus.OK);
        }
            return buildResponse(false, "No se encontraron tipos de movimiento", null, HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(
            @RequestParam(defaultValue = "") String nombre, Pageable pageable) {

         if (nombre.length() > 100) {
            return buildResponse(false, "El nombre de búsqueda es demasiado largo", null, HttpStatus.BAD_REQUEST);
        }
        try {
            Page<TipoMovimientoSalida> tiposMovimientos = tipoMovimientoService
                    .findByNombreContainingIgnoreCaseOrderByIdDesc(nombre, pageable)
                    .map(tipo -> tipoMovimientoService.obtenerPorId(tipo.getId()));

            if (tiposMovimientos.hasContent()) {
                return buildResponse(true, "Búsqueda realizada correctamente", tiposMovimientos, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron tipos de movimiento con el nombre: " + nombre, null,
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<?> mostrarTodos() {
        try {
        List<TipoMovimientoSalida> tiposMovimientos = tipoMovimientoService.obtenerTodos();
        if (!tiposMovimientos.isEmpty()) {
                return buildResponse(true, "Tipos de movimiento obtenidos correctamente", tiposMovimientos, HttpStatus.OK);
        }
            return buildResponse(false, "No se encontraron tipos de movimiento", null, HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
         if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.obtenerPorId(id);
        if (tiposMovimientos != null) {
                return buildResponse(true, "Tipo de movimiento encontrado", tiposMovimientos, HttpStatus.OK);
        }
            return buildResponse(false, "Tipo de movimiento no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Métodos POST, PUT y DELETE pueden ser añadidos aquí si es necesario
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoMovimientoGuardar tipoMovimientoGuardar) {
          if (tipoMovimientoGuardar == null || tipoMovimientoGuardar.getNombre() == null
                || tipoMovimientoGuardar.getNombre().isBlank()) {
            return buildResponse(false, "El nombre del tipo de movimiento es obligatorio", null, HttpStatus.BAD_REQUEST);
        }
        try {
            // Validación editarCosto para SALIDA
            if (tipoMovimientoGuardar.getTipo() != null && tipoMovimientoGuardar.getTipo() == 2
                    && Boolean.TRUE.equals(tipoMovimientoGuardar.getEditarCosto())) {
                tipoMovimientoGuardar.setEditarCosto(false);
                return buildResponse(false,
                        "⚠ No se puede editar el costo en movimientos de SALIDA. Se cambió automáticamente a NO.",
                        tipoMovimientoGuardar, HttpStatus.BAD_REQUEST);
            }

            TipoMovimientoSalida tipoMovimiento = tipoMovimientoService.crear(tipoMovimientoGuardar);
            return buildResponse(true, "Tipo de movimiento creado correctamente", tipoMovimiento, HttpStatus.CREATED);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping
    public ResponseEntity<TipoMovimientoSalida> crear(@RequestBody TipoMovimientoGuardar tipoMovimientoGuardar) {
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.crear(tipoMovimientoGuardar);
        return ResponseEntity.ok(tiposMovimientos);
    }*/

     @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id,
            @RequestBody TipoMovimientoModificar tipoMovimientoModificar) {

        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            TipoMovimientoSalida existente = tipoMovimientoService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede editar, tipo de movimiento no encontrado con ID: " + id,
                        null, HttpStatus.NOT_FOUND);
            }

            // Validación editarCosto para SALIDA
            if (tipoMovimientoModificar.getTipo() != null && tipoMovimientoModificar.getTipo() == 2
                    && Boolean.TRUE.equals(tipoMovimientoModificar.getEditarCosto())) {
                tipoMovimientoModificar.setEditarCosto(false);
                return buildResponse(false,
                        "⚠ No se puede editar el costo en movimientos de SALIDA. Se cambió automáticamente a NO.",
                        tipoMovimientoModificar, HttpStatus.BAD_REQUEST);
            }

            tipoMovimientoModificar.setId(id);
            
            TipoMovimientoSalida actualizado = tipoMovimientoService.editar(tipoMovimientoModificar);
            return buildResponse(true, "Tipo de movimiento editado correctamente", actualizado, HttpStatus.OK);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*@PutMapping("/{id}")
    public ResponseEntity<TipoMovimientoSalida> editar(@PathVariable Integer id,
            @RequestBody TipoMovimientoModificar tipoMovimientoModificar) {
        tipoMovimientoModificar.setId(id);
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.editar(tipoMovimientoModificar);
        return ResponseEntity.ok(tiposMovimientos);
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            TipoMovimientoSalida existente = tipoMovimientoService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede eliminar, tipo de movimiento no encontrado con ID: " + id,
                        null, HttpStatus.NOT_FOUND);
            }
        tipoMovimientoService.eliminarPorId(id);
            return buildResponse(true, "Tipo de movimiento eliminado correctamente", null, HttpStatus.OK);
            } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


     // ===================== UTILITARIOS =====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepciones(Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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