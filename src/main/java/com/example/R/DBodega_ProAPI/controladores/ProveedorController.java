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
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorGuardar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorModificar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<Page<ProveedorSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<ProveedorSalida> proveedores = proveedorService.obtenerTodosPaginados(pageable);
        if (proveedores.hasContent()) {
            return ResponseEntity.ok(proveedores);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ProveedorSalida>> mostrarTodos() {
        List<ProveedorSalida> proveedores = proveedorService.obtenerTodos();
        if (!proveedores.isEmpty()) {
            return ResponseEntity.ok(proveedores);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorSalida> buscarPorId(@PathVariable Integer id) {
        ProveedorSalida proveedor = proveedorService.obtenerPorId(id);

        if (proveedor != null) {
            return ResponseEntity.ok(proveedor);
        }
        return ResponseEntity.notFound().build();
    }

    // Metodos POST, PUT y DELETE
    @PostMapping
    public ResponseEntity<ProveedorSalida> crear(@RequestBody ProveedorGuardar proveedorGuardar) {
        ProveedorSalida proveedor = proveedorService.crear(proveedorGuardar);
        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorSalida> editar(@PathVariable Integer id,
            @RequestBody ProveedorModificar proveedorModificar) {
        ProveedorSalida proveedor = proveedorService.editar(proveedorModificar);
        return ResponseEntity.ok(proveedor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        proveedorService.eliminarPorId(id);
        return ResponseEntity.ok("Proveedor eliminada correctamente");
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProveedorSalida>> buscar(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String nombreEmpresa,
            Pageable pageable) {
        Page<ProveedorSalida> proveedores = proveedorService
                .findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc(nombre,
                        nombreEmpresa, pageable)
                .map(proveedor -> proveedorService.obtenerPorId(proveedor.getId()));

        if (proveedores.hasContent()) {
            return ResponseEntity.ok(proveedores);
        }
        return ResponseEntity.notFound().build();
    }
}