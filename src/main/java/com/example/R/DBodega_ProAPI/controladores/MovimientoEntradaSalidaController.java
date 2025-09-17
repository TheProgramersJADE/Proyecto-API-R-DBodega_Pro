package com.example.R.DBodega_ProAPI.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaGuardar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaModificar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaSalida;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IMovimientoEntradaSalidaService;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;
import com.example.R.DBodega_ProAPI.servicios.interfaces.ITipoMovimientoService;

@RestController
@RequestMapping("/api/movimientosEntradaSalida")
public class MovimientoEntradaSalidaController {

    @Autowired
    private IMovimientoEntradaSalidaService movimientoEntradaSalidaService;

     @Autowired
    private IProductoService productoService;

    @Autowired
    private ITipoMovimientoService tipoMovimientoService;

    @GetMapping
    public ResponseEntity<?> mostrarTodosPaginados(Pageable pageable) {
                try {
        Page<MovimientoEntradaSalidaSalida> movimientosEntradaSalida = movimientoEntradaSalidaService
                .obtenerTodosPaginados(pageable);
        if (movimientosEntradaSalida.hasContent()) {
                return buildResponse(true, "Movimientos obtenidos correctamente", movimientosEntradaSalida, HttpStatus.OK);
        }
            return buildResponse(false, "No se encontraron movimientos", null, HttpStatus.NOT_FOUND);
             } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error al obtener los movimientos: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<?> mostrarTodos() {
                try {
        List<MovimientoEntradaSalidaSalida> movimientosEntradaSalida = movimientoEntradaSalidaService.obtenerTodos();
        if (!movimientosEntradaSalida.isEmpty()) {
                return buildResponse(true, "Movimientos obtenidos correctamente", movimientosEntradaSalida, HttpStatus.OK);
        }
        return buildResponse(false, "No se encontraron movimientos", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Error al obtener movimientos: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService.obtenerPorId(id);

        if (movimientosEntradaSalida != null) {
                return buildResponse(true, "Movimiento encontrado", movimientosEntradaSalida, HttpStatus.OK);
        }
         return buildResponse(false, "Movimiento no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error al buscar el movimiento: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody MovimientoEntradaSalidaGuardar movimientoEntradaSalidaGuardar) {
        
                 // Validaciones básicas
    if (movimientoEntradaSalidaGuardar == null || movimientoEntradaSalidaGuardar.getIdProducto() == null
            || movimientoEntradaSalidaGuardar.getIdTipoMovimiento() == null
            || movimientoEntradaSalidaGuardar.getCantidad() == null) {
        return buildResponse(false, "Todos los datos obligatorios deben ser proporcionados", null, HttpStatus.BAD_REQUEST);
    }

    // Validar producto
    if (productoService.obtenerPorId(movimientoEntradaSalidaGuardar.getIdProducto()) == null) {
        return buildResponse(false, "Producto no válido", null, HttpStatus.BAD_REQUEST);
    }

    // Validar tipo de movimiento
    TipoMovimientoSalida tipoMovimiento = tipoMovimientoService.obtenerPorId(movimientoEntradaSalidaGuardar.getIdTipoMovimiento());
    if (tipoMovimiento == null) {
        return buildResponse(false, "Tipo de movimiento no válido", null, HttpStatus.BAD_REQUEST);
    }

    // Validar cantidad
    if (movimientoEntradaSalidaGuardar.getCantidad() <= 0) {
        return buildResponse(false, "La cantidad debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
    }

    // Validación precio si es tipo Entrada (tipo == 1)
    if (tipoMovimiento.getTipo() != null && tipoMovimiento.getTipo() == 1) {
        if (movimientoEntradaSalidaGuardar.getPrecio() == null || movimientoEntradaSalidaGuardar.getPrecio().doubleValue() <= 0) {
    return buildResponse(false, "Debe ingresar un precio válido para la entrada", null, HttpStatus.BAD_REQUEST);
}
    }

    try {
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService.crear(movimientoEntradaSalidaGuardar);
        return buildResponse(true, "Movimiento creado correctamente", movimientosEntradaSalida, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return buildResponse(false, e.getMessage(), null, HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
        @PathVariable Integer id,
        @RequestBody MovimientoEntradaSalidaModificar movimientoEntradaSalidaModificar) {
            
             // Validación básica del ID
    if (id == null || id <= 0) {
        return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
    }

    // Setear el ID en el DTO
    movimientoEntradaSalidaModificar.setId(id);

    try {
        // Obtener tipo de movimiento para validaciones
        TipoMovimientoSalida tipoMovimiento = tipoMovimientoService.obtenerPorId(
                movimientoEntradaSalidaModificar.getIdTipoMovimiento());

        if (tipoMovimiento == null) {
            return buildResponse(false, "Tipo de movimiento no válido", null, HttpStatus.BAD_REQUEST);
        }

        // Validación de precio si es tipo ENTRADA (1)
        if (tipoMovimiento.getTipo() != null && tipoMovimiento.getTipo() == 1) {
            if (movimientoEntradaSalidaModificar.getPrecio() == null ||
                    movimientoEntradaSalidaModificar.getPrecio().doubleValue() <= 0) {
                return buildResponse(false, "Debe ingresar un precio válido para la entrada", null, HttpStatus.BAD_REQUEST);
            }
        }

        // Llamada al servicio para editar
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService.editar(movimientoEntradaSalidaModificar);
        return buildResponse(true, "Movimiento editado correctamente", movimientosEntradaSalida, HttpStatus.OK);

    } catch (IllegalArgumentException e) {
        return buildResponse(false, e.getMessage(), null, HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
        return buildResponse(false, "Error al editar movimiento: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

     @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String nombreProducto,
            @RequestParam(defaultValue = "") String nombre,
            Pageable pageable
    ) {
        try {
            Page<MovimientoEntradaSalidaSalida> resultado =
                    movimientoEntradaSalidaService.findByProductoNombreContainingIgnoreCaseAndTipoMovimientoNombreOrderByIdDesc(
                            nombreProducto, nombre, pageable);

            if (resultado.hasContent()) {
                return buildResponse(true, "Búsqueda realizada correctamente", resultado, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron movimientos con los criterios proporcionados", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error al buscar: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     // ================== UTILITARIO ==================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepciones(Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String mensaje, Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("mensaje", mensaje);
        if (data != null) response.put("data", data);
        return ResponseEntity.status(status).body(response);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> eliminar(@PathVariable Integer id){
    // categoriaService.eliminarPorId(id);
    // return ResponseEntity.ok("Categoría eliminada correctamente");
    // }
}