package com.example.R.DBodega_ProAPI.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorGuardar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorModificar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @GetMapping
    public ResponseEntity<?> mostrarTodosPaginados(Pageable pageable) {
        try {
            Page<ProveedorSalida> proveedores = proveedorService.obtenerTodosPaginados(pageable);
            if (proveedores.hasContent()) {
                return buildResponse(true, "Proveedores obtenidos correctamente", proveedores, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron proveedores", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @GetMapping("/lista")
    public ResponseEntity<?> mostrarTodos() {
        try {
            List<ProveedorSalida> proveedores = proveedorService.obtenerTodos();
            if (!proveedores.isEmpty()) {
                return buildResponse(true, "Proveedores obtenidos correctamente", proveedores, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron proveedores", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            ProveedorSalida proveedor = proveedorService.obtenerPorId(id);

            if (proveedor != null) {
                return buildResponse(true, "Proveedor encontrado", proveedor, HttpStatus.OK);
            }
            return buildResponse(false, "Proveedor no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Metodos POST, PUT y DELETE
            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProveedorGuardar proveedorGuardar) {
        if (proveedorGuardar == null || proveedorGuardar.getNombre() == null
                || proveedorGuardar.getNombre().isBlank()) {
            return buildResponse(false, "El nombre del proveedor es obligatorio", null, HttpStatus.BAD_REQUEST);
        }
        try {
            ProveedorSalida proveedor = proveedorService.crear(proveedorGuardar);
            return buildResponse(true, "Proveedor creado correctamente", proveedor, HttpStatus.CREATED);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody ProveedorModificar proveedorModificar) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            ProveedorSalida existente = proveedorService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede editar, proveedor no encontrado con ID: " + id, null,
                        HttpStatus.NOT_FOUND);
            }
            proveedorModificar.setId(id);
            ProveedorSalida actualizado = proveedorService.editar(proveedorModificar);
            return buildResponse(true, "Proveedor editado correctamente", actualizado, HttpStatus.OK);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            ProveedorSalida existente = proveedorService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede eliminar, proveedor no encontrado con ID: " + id, null,
                        HttpStatus.NOT_FOUND);
            }
            proveedorService.eliminarPorId(id);
            return buildResponse(true, "Proveedor eliminado correctamente", null, HttpStatus.OK);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
@GetMapping("/buscar")
public ResponseEntity<?> buscar(
    @RequestParam(defaultValue = "") String nombre,
    @RequestParam(defaultValue = "") String nombreEmpresa,
        Pageable pageable) {
    try {
        if (nombre.length() > 100 || nombreEmpresa.length() > 100) {
            return buildResponse(false, "Los parámetros de búsqueda son demasiado largos", null,
                    HttpStatus.BAD_REQUEST);
        }
        Page<ProveedorSalida> proveedores = proveedorService
                .findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc(nombre,
                        nombreEmpresa, pageable)
                .map(proveedor -> proveedorService.obtenerPorId(proveedor.getId()));

        if (proveedores.hasContent()) {
            return buildResponse(true, "Búsqueda realizada correctamente", proveedores, HttpStatus.OK);
        }
        return buildResponse(false,
                "No se encontraron proveedores con los criterios proporcionados", null,
                HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// ===================== UTILITARIOS =====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepciones(Exception ex) {
        return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
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