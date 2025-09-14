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
                .map(this::mapearAProductoSalida)
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
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapearAProductoSalida(producto);
    }

    @Override
    public ProductoSalida crear(ProductoGuardar dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio_compra(dto.getPrecio_compra());
        producto.setPrecio_venta(dto.getPrecio_venta());
        producto.setCosto_promedio(dto.getCosto_promedio());
        producto.setStock_actual(dto.getStock_actual());
        producto.setStock_minimo(dto.getStock_minimo());
        producto.setImagen_url(dto.getImagen_url());

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        producto.setProveedor(proveedor);

        producto = productoRepository.save(producto);

        return mapearAProductoSalida(producto);
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
        Producto existente = productoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio_compra(dto.getPrecio_compra());
        existente.setPrecio_venta(dto.getPrecio_venta());
        existente.setCosto_promedio(dto.getCosto_promedio());
        existente.setStock_actual(dto.getStock_actual());
        existente.setStock_minimo(dto.getStock_minimo());

        if (dto.getImagen_url() != null) {
            existente.setImagen_url(dto.getImagen_url());
        }

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existente.setCategoria(categoria);

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        existente.setProveedor(proveedor);

        existente = productoRepository.save(existente);

        return mapearAProductoSalida(existente);

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


// --- Método privado para mapear manualmente el estado de stock ---
    private ProductoSalida mapearAProductoSalida(Producto producto) {
        ProductoSalida salida = modelMapper.map(producto, ProductoSalida.class);

        // Campos calculados de stock
        if (producto.getStock_actual() == null || producto.getStock_actual() <= 0) {
            salida.setEstadoStock("Agotado");
            salida.setEstadoStockClass("badge-danger");
        } else if (producto.getStock_actual() <= producto.getStock_minimo()) {
            salida.setEstadoStock("Terminando");
            salida.setEstadoStockClass("badge-warning");
        } else {
            salida.setEstadoStock("Disponible");
            salida.setEstadoStockClass("badge-success");
        }

        // Mapear relaciones simplificadas
        salida.setIdCategoria(producto.getCategoria().getId());
        salida.setNombreCategoria(producto.getCategoria().getNombre());
        salida.setIdProveedor(producto.getProveedor().getId());
        salida.setNombreProveedor(producto.getProveedor().getNombre());

        return salida;
    }


}