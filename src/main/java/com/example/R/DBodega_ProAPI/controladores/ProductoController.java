package com.example.R.DBodega_ProAPI.controladores;

import java.io.IOException;
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
    public ResponseEntity<Page<ProductoSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<ProductoSalida> productos = productoService.obtenerTodosPaginados(pageable);
        if (productos.hasContent()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    // 🔹 Lista sin paginar
    @GetMapping("/lista")
    public ResponseEntity<List<ProductoSalida>> mostrarTodos() {
        List<ProductoSalida> productos = productoService.obtenerTodos();
        if (!productos.isEmpty()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

      @GetMapping("/{id}")
    public ResponseEntity<ProductoSalida> buscarPorId(@PathVariable Integer id) {
        ProductoSalida producto = productoService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    
    // Crear producto con imagen
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductoSalida> crear(
       @RequestParam String nombre,
        @RequestParam String descripcion,
        @RequestParam Double precio_compra,
        @RequestParam Double precio_venta,
        @RequestParam Double costo_promedio,
        @RequestParam Integer stock_actual,
        @RequestParam Integer stock_minimo,
        @RequestParam String categoria_nombre,
        @RequestParam String proveedor_nombre,
        @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

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
        return ResponseEntity.ok(productoCreado);
    }

    // Editar producto con posibilidad de cambiar imagen
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductoSalida> editar(
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
        @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

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
        return ResponseEntity.ok(productoActualizado);
    }


     // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        productoService.eliminarPorId(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    
    // Búsqueda personalizada
    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoSalida>> buscar(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String categoria,
            @RequestParam(defaultValue = "") String proveedor,
            Pageable pageable
    ) {
        Page<ProductoSalida> productos = productoService
                .findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
                        nombre, categoria, proveedor, pageable)
                .map(producto -> productoService.obtenerPorId(producto.getId())); // mapeo a DTO

        if (productos.hasContent()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }




    

}
