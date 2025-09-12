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
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private IProductoService productoService;
    
    @GetMapping
    public ResponseEntity<Page<ProductoSalida>> mostrarTodosPaginados(Pageable pageable){
        Page<ProductoSalida> productos = productoService.obtenerTodosPaginados(pageable);
        if(productos.hasContent()){
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ProductoSalida>> mostrarTodos(){
        List<ProductoSalida> productos = productoService.obtenerTodos();
        if(!productos.isEmpty()){
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoSalida> buscarPorId(@PathVariable Integer id){
        ProductoSalida productos = productoService.obtenerPorId(id);

        if(productos != null){
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductoSalida> crear(@RequestBody ProductoGuardar productoGuardar){
        ProductoSalida productos = productoService.crear(productoGuardar);
        return ResponseEntity.ok(productos);
    }

      @PutMapping("/{id}")
    public ResponseEntity<ProductoSalida> editar(@PathVariable Integer id, @RequestBody ProductoModificar productoModificar){
        ProductoSalida productos = productoService.editar(productoModificar);
        return ResponseEntity.ok(productos);
    }

      @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){
        productoService.eliminarPorId(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}