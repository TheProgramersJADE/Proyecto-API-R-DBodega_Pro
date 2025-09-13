package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.Categoria;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.modelos.Proveedor;
import com.example.R.DBodega_ProAPI.repositorios.ICategoriaRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProveedorRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;

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
        List<ProductoSalida> productosDto = page.stream()
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(productosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public ProductoSalida obtenerPorId(Integer id) {
        return modelMapper.map(productoRepository.findById(id).get(), ProductoSalida.class);
    }


    @Override
    public ProductoSalida crear(ProductoGuardar dto) {
        // Mapear datos simples
        Producto producto = modelMapper.map(dto, Producto.class);

        // Setear relaciones
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        producto.setProveedor(proveedor);

        // Guardar en BD
        producto = productoRepository.save(producto);

        return modelMapper.map(producto, ProductoSalida.class);
    }
    // CODIGO ANTIGUO
    // @Override
    // public ProductoSalida crear(ProductoGuardar productoGuardar) {
    // Producto producto = productoRepository.save(modelMapper.map(productoGuardar,
    // Producto.class));
    // return modelMapper.map(producto, ProductoSalida.class);
    // }

    @Override
    public ProductoSalida editar(ProductoModificar dto) {
        // Obtener producto actual de la BD
        Producto existente = productoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Mapear cambios (excepto imagen si no viene)
        modelMapper.map(dto, existente);

        // Si en el DTO no viene imagen_url, mantenemos la existente
        if (dto.getImagen_url() == null || dto.getImagen_url().isEmpty()) {
            existente.setImagen_url(existente.getImagen_url());
        }

        // Setear relaciones
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existente.setCategoria(categoria);

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        existente.setProveedor(proveedor);

        existente = productoRepository.save(existente);

        return modelMapper.map(existente, ProductoSalida.class);
    }
    // CODIGO ANTIGUO
    // @Override
    // public ProductoSalida editar(ProductoModificar ProductoModificar) {
    // Producto producto =
    // productoRepository.save(modelMapper.map(ProductoModificar, Producto.class));
    // return modelMapper.map(producto, ProductoSalida.class);
    // }


    @Override
    public void eliminarPorId(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Page<ProductoSalida> findByNombreContainingIgnoreCaseAndCategoriaNombreContainingIgnoreCaseAndProveedorNombreContainingIgnoreCaseOrderByIdDesc(
            String nombre, String categoriaNombre, String proveedorNombre, Pageable pageable) {
        Page<Producto> page = productoRepository
                .findByNombreContainingIgnoreCaseAndCategoriaNombreContainingIgnoreCaseAndProveedorNombreContainingIgnoreCaseOrderByIdDesc(
                        nombre, categoriaNombre, proveedorNombre, pageable);
        List<ProductoSalida> productosDto = page.stream()
                .map(producto -> modelMapper.map(producto, ProductoSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(productosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public Optional<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

}