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
        return modelMapper.map(productoRepository.findById(id).get(), ProductoSalida.class);
    }

     @Override
    public ProductoSalida crear(ProductoGuardar dto) {
        Producto producto = modelMapper.map(dto, Producto.class);
        producto.setCategoria(buscarCategoriaPorNombre(dto.getCategoria_nombre()));
        producto.setProveedor(buscarProveedorPorNombre(dto.getProveedor_nombre()));
        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoSalida.class);
    }

    @Override
    public ProductoSalida editar(ProductoModificar dto) {
        Producto producto = modelMapper.map(dto, Producto.class);
        producto.setCategoria(buscarCategoriaPorNombre(dto.getCategoria_nombre()));
        producto.setProveedor(buscarProveedorPorNombre(dto.getProveedor_nombre()));
        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoSalida.class);
    }

    /*@Override
    public ProductoSalida crear(ProductoGuardar productoGuardar) {
        Producto producto = productoRepository.save(modelMapper.map(productoGuardar, Producto.class));
        return modelMapper.map(producto, ProductoSalida.class);
    }

    @Override
    public ProductoSalida editar(ProductoModificar productoModificar) {
        Producto producto = productoRepository.save(modelMapper.map(productoModificar, Producto.class));
        return modelMapper.map(producto, ProductoSalida.class);
    }*/

    @Override
    public void eliminarPorId(Integer id) {
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

}
