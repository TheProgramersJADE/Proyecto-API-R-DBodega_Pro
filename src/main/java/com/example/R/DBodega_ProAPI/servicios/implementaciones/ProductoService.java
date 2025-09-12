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
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProductoService;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository productoRepository;

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
    public ProductoSalida crear(ProductoGuardar productoGuardar) {
        Producto producto = productoRepository.save(modelMapper.map(productoGuardar, Producto.class));
        return modelMapper.map(producto, ProductoSalida.class);
    }

    @Override
    public ProductoSalida editar(ProductoModificar ProductoModificar) {
        Producto producto = productoRepository.save(modelMapper.map(ProductoModificar, Producto.class));
        return modelMapper.map(producto, ProductoSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Page<ProductoSalida> findByNombreContainingIgnoreCaseAndCategoriaNombreContainingIgnoreCaseAndProveedorNombreContainingIgnoreCaseOrderByIdDesc(String nombre, String categoriaNombre, String proveedorNombre, Pageable pageable) {
        Page<Producto> page = productoRepository.findByNombreContainingIgnoreCaseAndCategoriaNombreContainingIgnoreCaseAndProveedorNombreContainingIgnoreCaseOrderByIdDesc(nombre, categoriaNombre, proveedorNombre, pageable);
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
