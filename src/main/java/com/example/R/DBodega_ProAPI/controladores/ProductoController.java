package com.example.R.DBodega_ProAPI.controladores;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    // 🔹 Paginado
    @GetMapping
    public ResponseEntity<?> mostrarTodosPaginados(Pageable pageable) {
        try {
            Page<ProductoSalida> productos = productoService.obtenerTodosPaginados(pageable);
            if (productos.hasContent()) {
                return buildResponse(true, "Productos obtenidos correctamente", productos, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron productos", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🔹 Lista sin paginar
    @GetMapping("/lista")
    public ResponseEntity<?> mostrarTodos() {
        try {
            List<ProductoSalida> productos = productoService.obtenerTodos();
            if (!productos.isEmpty()) {
                return buildResponse(true, "Productos obtenidos correctamente", productos, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron productos", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }
        try {
            ProductoSalida producto = productoService.obtenerPorId(id);
            if (producto != null) {
                return buildResponse(true, "Producto encontrado", producto, HttpStatus.OK);
            }
            return buildResponse(false, "Producto no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear producto con imagen
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> crear(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam Double precio_compra,
            @RequestParam Double precio_venta,
            @RequestParam Double costo_promedio,
            @RequestParam Integer stock_actual,
            @RequestParam Integer stock_minimo,
            @RequestParam String categoria_nombre,
            @RequestParam String proveedor_nombre,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        // Validaciones manuales según tu modelo
        if (nombre == null || nombre.isBlank() || nombre.length() > 100) {
            return buildResponse(false, "El nombre es obligatorio y debe tener máximo 100 caracteres", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (descripcion != null && descripcion.length() > 255) {
            return buildResponse(false, "La descripción debe tener máximo 255 caracteres", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (precio_compra == null || precio_compra < 0) {
            return buildResponse(false, "El precio de compra es obligatorio y no puede ser negativo", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (precio_venta == null || precio_venta < 0) {
            return buildResponse(false, "El precio de venta es obligatorio y no puede ser negativo", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (costo_promedio == null || costo_promedio < 0) {
            return buildResponse(false, "El costo promedio es obligatorio y no puede ser negativo", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (stock_actual == null || stock_actual < 0) {
            return buildResponse(false, "El stock actual es obligatorio y no puede ser negativo", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (stock_minimo == null || stock_minimo < 0) {
            return buildResponse(false, "El stock mínimo es obligatorio y no puede ser negativo", null,
                    HttpStatus.BAD_REQUEST);
        }
        if (categoria_nombre == null || categoria_nombre.isBlank()) {
            return buildResponse(false, "Debe seleccionar una categoría", null, HttpStatus.BAD_REQUEST);
        }
        if (proveedor_nombre == null || proveedor_nombre.isBlank()) {
            return buildResponse(false, "Debe seleccionar un proveedor", null, HttpStatus.BAD_REQUEST);
        }

        try {
            ProductoGuardar dto = new ProductoGuardar();
            dto.setNombre(nombre);
            dto.setDescripcion(descripcion);
            dto.setPrecio_compra(java.math.BigDecimal.valueOf(precio_compra));
            dto.setPrecio_venta(java.math.BigDecimal.valueOf(precio_venta));
            dto.setCosto_promedio(java.math.BigDecimal.valueOf(costo_promedio));
            dto.setStock_actual(stock_actual);
            dto.setStock_minimo(stock_minimo);
            dto.setCategoria_nombre(categoria_nombre);
            dto.setProveedor_nombre(proveedor_nombre);

            ProductoSalida productoCreado = productoService.crear(dto, imagen);
            return buildResponse(true, "Producto creado correctamente", productoCreado, HttpStatus.CREATED);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Editar producto con posibilidad de cambiar imagen
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> editar(
            @PathVariable Integer id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam Double precio_compra,
            @RequestParam Double precio_venta,
            @RequestParam Double costo_promedio,
            @RequestParam Integer stock_actual,
            @RequestParam Integer stock_minimo,
            @RequestParam String categoria_nombre,
            @RequestParam String proveedor_nombre,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }

        try {
            ProductoSalida existente = productoService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede editar, producto no encontrado con ID: " + id, null,
                        HttpStatus.NOT_FOUND);
            }

            // Validaciones de campos igual que en POST
            if (nombre == null || nombre.isBlank() || nombre.length() > 100) {
                return buildResponse(false, "El nombre es obligatorio y debe tener máximo 100 caracteres", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (descripcion != null && descripcion.length() > 255) {
                return buildResponse(false, "La descripción debe tener máximo 255 caracteres", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (precio_compra == null || precio_compra < 0) {
                return buildResponse(false, "El precio de compra es obligatorio y no puede ser negativo", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (precio_venta == null || precio_venta < 0) {
                return buildResponse(false, "El precio de venta es obligatorio y no puede ser negativo", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (costo_promedio == null || costo_promedio < 0) {
                return buildResponse(false, "El costo promedio es obligatorio y no puede ser negativo", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (stock_actual == null || stock_actual < 0) {
                return buildResponse(false, "El stock actual es obligatorio y no puede ser negativo", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (stock_minimo == null || stock_minimo < 0) {
                return buildResponse(false, "El stock mínimo es obligatorio y no puede ser negativo", null,
                        HttpStatus.BAD_REQUEST);
            }
            if (categoria_nombre == null || categoria_nombre.isBlank()) {
                return buildResponse(false, "Debe seleccionar una categoría", null, HttpStatus.BAD_REQUEST);
            }
            if (proveedor_nombre == null || proveedor_nombre.isBlank()) {
                return buildResponse(false, "Debe seleccionar un proveedor", null, HttpStatus.BAD_REQUEST);
            }

            ProductoModificar dto = new ProductoModificar();
            dto.setId(id);
            dto.setNombre(nombre);
            dto.setDescripcion(descripcion);
            dto.setPrecio_compra(java.math.BigDecimal.valueOf(precio_compra));
            dto.setPrecio_venta(java.math.BigDecimal.valueOf(precio_venta));
            dto.setCosto_promedio(java.math.BigDecimal.valueOf(costo_promedio));
            dto.setStock_actual(stock_actual);
            dto.setStock_minimo(stock_minimo);
            dto.setCategoria_nombre(categoria_nombre);
            dto.setProveedor_nombre(proveedor_nombre);

            ProductoSalida productoActualizado = productoService.editar(dto, imagen);
            return buildResponse(true, "Producto editado correctamente", productoActualizado, HttpStatus.OK);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return buildResponse(false, "El ID debe ser mayor a 0", null, HttpStatus.BAD_REQUEST);
        }

        try {
            ProductoSalida existente = productoService.obtenerPorId(id);
            if (existente == null) {
                return buildResponse(false, "No se puede eliminar, producto no encontrado con ID: " + id, null,
                        HttpStatus.NOT_FOUND);
            }

            productoService.eliminarPorId(id);
            return buildResponse(true, "Producto eliminado correctamente", null, HttpStatus.OK);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String categoria,
            @RequestParam(defaultValue = "") String proveedor,
            Pageable pageable) {
        if (nombre.length() > 100 || categoria.length() > 100 || proveedor.length() > 100) {
            return buildResponse(false, "Los parámetros de búsqueda son demasiado largos", null,
                    HttpStatus.BAD_REQUEST);
        }
        try {
            Page<ProductoSalida> productos = productoService
                    .findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
                            nombre, categoria, proveedor, pageable)
                    .map(p -> productoService.obtenerPorId(p.getId()));

            if (productos.hasContent()) {
                return buildResponse(true, "Búsqueda realizada correctamente", productos, HttpStatus.OK);
            }
            return buildResponse(false, "No se encontraron productos con los filtros proporcionados", null,
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return buildResponse(false, "Ocurrió un error interno: " + ex.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ================= UTILITARIOS =================
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