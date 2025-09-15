package com.example.R.DBodega_ProAPI.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaGuardar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaModificar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IMovimientoEntradaSalidaService;

@RestController
@RequestMapping("/api/movimientosEntradaSalida")
public class MovimientoEntradaSalidaController {

    @Autowired
    private IMovimientoEntradaSalidaService movimientoEntradaSalidaService;

    @GetMapping
    public ResponseEntity<Page<MovimientoEntradaSalidaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<MovimientoEntradaSalidaSalida> movimientosEntradaSalida = movimientoEntradaSalidaService
                .obtenerTodosPaginados(pageable);
        if (movimientosEntradaSalida.hasContent()) {
            return ResponseEntity.ok(movimientosEntradaSalida);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<MovimientoEntradaSalidaSalida>> mostrarTodos() {
        List<MovimientoEntradaSalidaSalida> movimientosEntradaSalida = movimientoEntradaSalidaService.obtenerTodos();
        if (!movimientosEntradaSalida.isEmpty()) {
            return ResponseEntity.ok(movimientosEntradaSalida);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoEntradaSalidaSalida> buscarPorId(@PathVariable Integer id) {
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService.obtenerPorId(id);

        if (movimientosEntradaSalida != null) {
            return ResponseEntity.ok(movimientosEntradaSalida);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MovimientoEntradaSalidaSalida> crear(
            @RequestBody MovimientoEntradaSalidaGuardar movimientoEntradaSalidaGuardar) {
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService
                .crear(movimientoEntradaSalidaGuardar);
        return ResponseEntity.ok(movimientosEntradaSalida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoEntradaSalidaSalida> editar(@PathVariable Integer id,
            @RequestBody MovimientoEntradaSalidaModificar movimientoEntradaSalidaModificar) {
        MovimientoEntradaSalidaSalida movimientosEntradaSalida = movimientoEntradaSalidaService
                .editar(movimientoEntradaSalidaModificar);
        return ResponseEntity.ok(movimientosEntradaSalida);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> eliminar(@PathVariable Integer id){
    // categoriaService.eliminarPorId(id);
    // return ResponseEntity.ok("Categoría eliminada correctamente");
    // }
}