package com.example.R.DBodega_ProAPI.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

 @Autowired
    private IProductoService productoService;

    // Listado de productos para catálogo (JSON)
    @GetMapping
    public ResponseEntity<List<ProductoSalida>> listarCatalogo() {
        List<ProductoSalida> productos = productoService.obtenerTodos();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    // Detalle de producto en catálogo (JSON)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoSalida> detalleCatalogo(@PathVariable Integer id) {
        ProductoSalida producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

}
