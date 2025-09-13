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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.servicios.implementaciones.ServicioArchivos;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private ServicioArchivos servicioArchivos;

    @GetMapping
    public ResponseEntity<Page<ProductoSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<ProductoSalida> productos = productoService.obtenerTodosPaginados(pageable);
        if (productos.hasContent()) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

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
        ProductoSalida productos = productoService.obtenerPorId(id);

        if (productos != null) {
            return ResponseEntity.ok(productos);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoSalida> crear(
            @RequestPart("producto") ProductoGuardar dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        if (imagen != null && !imagen.isEmpty()) {
            // Guardar en disco
            String ruta = servicioArchivos.guardarImagen(imagen);
            dto.setImagen_url(ruta);
        }

        ProductoSalida producto = productoService.crear(dto);
        return ResponseEntity.ok(producto);
    }
    // CODIGO ANTIGUO
    // public ResponseEntity<ProductoSalida> crear(@RequestBody ProductoGuardar
    // productoGuardar){
    // ProductoSalida productos = productoService.crear(productoGuardar);
    // return ResponseEntity.ok(productos);
    // }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoSalida> editar(
            @PathVariable Integer id,
            @RequestPart("producto") ProductoModificar dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        if (imagen != null && !imagen.isEmpty()) {
            // Primero eliminar imagen anterior
            servicioArchivos.eliminarImagenExistente(id);

            // Guardar nueva imagen
            String ruta = servicioArchivos.guardarImagen(imagen);
            dto.setImagen_url(ruta);
        }

        ProductoSalida producto = productoService.editar(dto);
        return ResponseEntity.ok(producto);
    }
    // CODIGO ANTIGUO
    // @PutMapping("/{id}")
    // public ResponseEntity<ProductoSalida> editar(@PathVariable Integer id,
    // @RequestBody ProductoModificar productoModificar) {
    // ProductoSalida productos = productoService.editar(productoModificar);
    // return ResponseEntity.ok(productos);
    // }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        productoService.eliminarPorId(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}