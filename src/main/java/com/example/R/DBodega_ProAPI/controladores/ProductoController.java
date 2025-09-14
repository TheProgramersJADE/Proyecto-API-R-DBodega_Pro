package com.example.R.DBodega_ProAPI.controladores;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
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

    private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
    
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

   // -------------------- CREAR --------------------
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductoSalida> crearProducto(
            @RequestPart("producto") ProductoGuardar productoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        // Guardar imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(imagen.getOriginalFilename());
            Files.copy(imagen.getInputStream(), filePath);
            productoDto.setImagen_url("/uploads/" + imagen.getOriginalFilename());
        }

        ProductoSalida creado = productoService.crear(productoDto);
        return ResponseEntity.ok(creado);
    }
    // CODIGO ANTIGUO
    // public ResponseEntity<ProductoSalida> crear(@RequestBody ProductoGuardar
    // productoGuardar){
    // ProductoSalida productos = productoService.crear(productoGuardar);
    // return ResponseEntity.ok(productos);
    // }


    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductoSalida> editarProducto(
            @RequestPart("producto") ProductoModificar productoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        // Guardar imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(imagen.getOriginalFilename());
            Files.copy(imagen.getInputStream(), filePath);
            productoDto.setImagen_url("/uploads/" + imagen.getOriginalFilename());
        }

        ProductoSalida editado = productoService.editar(productoDto);
        return ResponseEntity.ok(editado);
    }

    // CODIGO ANTIGUO
    // @PutMapping("/{id}")
    // public ResponseEntity<ProductoSalida> editar(@PathVariable Integer id,
    // @RequestBody ProductoModificar productoModificar) {
    // ProductoSalida productos = productoService.editar(productoModificar);
    // return ResponseEntity.ok(productos);
    // }

    
     // -------------------- OBTENER TODOS --------------------
    @GetMapping
    public ResponseEntity<?> obtenerTodos(Pageable pageable) {
        return ResponseEntity.ok(productoService.obtenerTodosPaginados(pageable));
    }

    // -------------------- OBTENER POR ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductoSalida> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // -------------------- ELIMINAR --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        productoService.eliminarPorId(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}