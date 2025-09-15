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

        private final Path rutaUploads = Paths.get("src/main/resources/static/uploads");

     @Override
     public List<ProductoSalida> obtenerTodos() {
          List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .collect(Collectors.toList());
     }

     @Override
     public Page<ProductoSalida> obtenerTodosPaginados(Pageable pageable) {
           Page<Producto> page = productoRepository.findAll(pageable);

        List<ProductoSalida> productoDto = page.stream()
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(productoDto, page.getPageable(), page.getTotalElements());
     }

     @Override
     public ProductoSalida obtenerPorId(Integer id) {
         return modelMapper.map(productoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Producto no encontrado con id: " + id)), ProductoSalida.class);
     }

     @Override
     public ProductoSalida crear(ProductoGuardar productoGuardar, MultipartFile imagen) throws IOException {
       Producto producto = modelMapper.map(productoGuardar, Producto.class);
        producto.setCategoria(buscarCategoriaPorNombre(productoGuardar.getCategoria_nombre()));
        producto.setProveedor(buscarProveedorPorNombre(productoGuardar.getProveedor_nombre()));

        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Files.createDirectories(rutaUploads);
            Path rutaArchivo = rutaUploads.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            producto.setImagen_url("/uploads/" + nombreArchivo);
        }

        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoSalida.class);
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
        if (imagen != null && !imagen.isEmpty()) {
            // Borrar imagen anterior si existe
            if (producto.getImagen_url() != null) {
                Path rutaAnterior = Paths.get("src/main/resources/static", producto.getImagen_url());
                Files.deleteIfExists(rutaAnterior);
            }
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Files.createDirectories(rutaUploads);
            Path rutaArchivo = rutaUploads.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            producto.setImagen_url("/uploads/" + nombreArchivo);
        }

        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoSalida.class);
     }

     @Override
     public void eliminarPorId(Integer id) {
       Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        // Borrar imagen si existe
        if (producto.getImagen_url() != null) {
            try {
                Path rutaArchivo = Paths.get("src/main/resources/static", producto.getImagen_url());
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


    /*public ProductoSalida actualizarImagen(Integer id, String rutaImagen) {
    Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    producto.setImagen_url(rutaImagen);
    producto = productoRepository.save(producto);
    return modelMapper.map(producto, ProductoSalida.class);
}*/


}
