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
    public ResponseEntity<Page<TipoMovimientoSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<TipoMovimientoSalida> tiposMovimientos = tipoMovimientoService.obtenerTodosPaginados(pageable);
        if (tiposMovimientos.hasContent()) {
            return ResponseEntity.ok(tiposMovimientos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<TipoMovimientoSalida>> mostrarTodos() {
        List<TipoMovimientoSalida> tiposMovimientos = tipoMovimientoService.obtenerTodos();
        if (!tiposMovimientos.isEmpty()) {
            return ResponseEntity.ok(tiposMovimientos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoMovimientoSalida> buscarPorId(@PathVariable Integer id) {
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.obtenerPorId(id);
        if (tiposMovimientos != null) {
            return ResponseEntity.ok(tiposMovimientos);
        }
        return ResponseEntity.notFound().build();
    }

    // Métodos POST, PUT y DELETE pueden ser añadidos aquí si es necesario
    @PostMapping
    public ResponseEntity<TipoMovimientoSalida> crear(@RequestBody TipoMovimientoGuardar tipoMovimientoGuardar){
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.crear(tipoMovimientoGuardar);
        return ResponseEntity.ok(tiposMovimientos);
    }

      @PutMapping("/{id}")
    public ResponseEntity<TipoMovimientoSalida> editar(@PathVariable Integer id, @RequestBody TipoMovimientoModificar tipoMovimientoModificar){
        TipoMovimientoSalida tiposMovimientos = tipoMovimientoService.editar(tipoMovimientoModificar);
        return ResponseEntity.ok(tiposMovimientos);
    }

      @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){
        tipoMovimientoService.eliminarPorId(id);
        return ResponseEntity.ok("Tipo Movimiento eliminado correctamente");
    }

}