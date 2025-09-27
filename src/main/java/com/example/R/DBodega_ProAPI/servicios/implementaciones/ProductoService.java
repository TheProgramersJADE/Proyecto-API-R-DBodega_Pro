package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.Categoria;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.modelos.Proveedor;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;
import com.example.R.DBodega_ProAPI.repositorios.ICategoriaRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProveedorRepository;

@Service
public class ProductoService implements IProductoService {

      @Autowired
    private IProductoRepository productoRepository;

     @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IProveedorRepository proveedorRepository;

     @Autowired
    private ModelMapper modelMapper;

private final Path rutaUploads = Paths.get("uploads"); // carpeta en la raíz del proyecto

     @Override
     public List<ProductoSalida> obtenerTodos() {
          List<Producto> productos = productoRepository.findAll();
         return productos.stream()
            .map(producto -> {
                ProductoSalida dto = modelMapper.map(producto, ProductoSalida.class);
                // Mapear categoría/proveedor/estadoStock en DTO
                if (producto.getCategoria() != null) dto.setCategoriaNombre(producto.getCategoria().getNombre());
                if (producto.getProveedor() != null) dto.setProveedorNombre(producto.getProveedor().getNombre());
                dto.setEstadoStock(calculateEstadoStock(producto));
                return dto;
            })
            .collect(Collectors.toList());
}

     @Override
     public Page<ProductoSalida> obtenerTodosPaginados(Pageable pageable) {
           Page<Producto> page = productoRepository.findAll(pageable);

        List<ProductoSalida> productoDto = page.stream()
            .map(producto -> {
                ProductoSalida dto = modelMapper.map(producto, ProductoSalida.class);
                if (producto.getCategoria() != null) dto.setCategoriaNombre(producto.getCategoria().getNombre());
                if (producto.getProveedor() != null) dto.setProveedorNombre(producto.getProveedor().getNombre());
                dto.setEstadoStock(calculateEstadoStock(producto));
                return dto;
            })
            .collect(Collectors.toList());

        return new PageImpl<>(productoDto, page.getPageable(), page.getTotalElements());
     }

     @Override
     public ProductoSalida obtenerPorId(Integer id) {
         Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    ProductoSalida dto = modelMapper.map(producto, ProductoSalida.class);
    if (producto.getCategoria() != null) dto.setCategoriaNombre(producto.getCategoria().getNombre());
    if (producto.getProveedor() != null) dto.setProveedorNombre(producto.getProveedor().getNombre());
    dto.setEstadoStock(calculateEstadoStock(producto));
    return dto;
     }

     @Override
     public ProductoSalida crear(ProductoGuardar productoGuardar, MultipartFile imagen) throws IOException {
       Producto producto = modelMapper.map(productoGuardar, Producto.class);
        producto.setCategoria(buscarCategoriaPorNombre(productoGuardar.getCategoria_nombre()));
        producto.setProveedor(buscarProveedorPorNombre(productoGuardar.getProveedor_nombre()));

            // En crear()
        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Files.createDirectories(rutaUploads);
            Path rutaArchivo = rutaUploads.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            producto.setImagen_url("/uploads/" + nombreArchivo);
        }

        producto = productoRepository.save(producto);

         ProductoSalida dto = modelMapper.map(producto, ProductoSalida.class);
        if (producto.getCategoria() != null) dto.setCategoriaNombre(producto.getCategoria().getNombre());
        if (producto.getProveedor() != null) dto.setProveedorNombre(producto.getProveedor().getNombre());
        dto.setEstadoStock(calculateEstadoStock(producto));
        return dto;
     }

     @Override
     public ProductoSalida editar(ProductoModificar productoModificar, MultipartFile imagen) throws IOException {
         Producto producto = productoRepository.findById(productoModificar.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Mapear propiedades (excepto imagen)
        producto.setNombre(productoModificar.getNombre());
        producto.setDescripcion(productoModificar.getDescripcion());
        producto.setPrecio_compra(productoModificar.getPrecio_compra());
        producto.setPrecio_venta(productoModificar.getPrecio_venta());
        producto.setCosto_promedio(productoModificar.getCosto_promedio());
        producto.setStock_actual(productoModificar.getStock_actual());
        producto.setStock_minimo(productoModificar.getStock_minimo());
        producto.setCategoria(buscarCategoriaPorNombre(productoModificar.getCategoria_nombre()));
        producto.setProveedor(buscarProveedorPorNombre(productoModificar.getProveedor_nombre()));

        // Actualizar imagen solo si se envía una nueva
             // En editar()
            if (imagen != null && !imagen.isEmpty()) {
                // Borrar la anterior si existe
                if (producto.getImagen_url() != null && !producto.getImagen_url().isBlank()) {
                Path rutaAnterior = rutaUploads.resolve(Paths.get(producto.getImagen_url()).getFileName().toString());
                    Files.deleteIfExists(rutaAnterior);
                }
                // Guardar nueva imagen
                String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Files.createDirectories(rutaUploads);
                Path rutaArchivo = rutaUploads.resolve(nombreArchivo);
                Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
                producto.setImagen_url("/uploads/" + nombreArchivo);
            }

        producto = productoRepository.save(producto);

         ProductoSalida dto = modelMapper.map(producto, ProductoSalida.class);
        if (producto.getCategoria() != null) dto.setCategoriaNombre(producto.getCategoria().getNombre());
        if (producto.getProveedor() != null) dto.setProveedorNombre(producto.getProveedor().getNombre());
        dto.setEstadoStock(calculateEstadoStock(producto));
        return dto;
     }

     @Override
     public void eliminarPorId(Integer id) {
       Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        // Borrar imagen si existe
        if (producto.getImagen_url() != null) {
            try {
            Path rutaArchivo = rutaUploads.resolve(Paths.get(producto.getImagen_url()).getFileName().toString());
                Files.deleteIfExists(rutaArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productoRepository.deleteById(id);
     }

     @Override
     public Page<Producto> findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
            String nombre, String categoriaNombre, String proveedorNombre, Pageable pageable) {
         return productoRepository.findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
                nombre, categoriaNombre, proveedorNombre, pageable);
     }

     @Override
     public Optional<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
     }

    private Categoria buscarCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
    }

    private Proveedor buscarProveedorPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + nombre));
    }


    // ---------------- util: calcular estado stock ----------------
private String calculateEstadoStock(Producto producto) {
    if (producto.getStock_actual() == null || producto.getStock_minimo() == null) {
        return "Desconocido";
    }

    int stockActual = producto.getStock_actual();
    int stockMinimo = producto.getStock_minimo();

    // Estado 1: no hay existencias
    if (stockActual <= 0) {
        return "AGOTADO";
    }

    // Calcula un margen de alerta del 10% por encima del mínimo
    double margenAlerta = stockMinimo * 1.10;

    // Estado 2: aún hay stock, pero ya se acerca al límite mínimo
    if (stockActual > 0 && stockActual <= margenAlerta) {
        return "TERMINÁNDOSE";
    }

    // Estado 3: hay suficiente stock
    return "DISPONIBLE";
}


}
