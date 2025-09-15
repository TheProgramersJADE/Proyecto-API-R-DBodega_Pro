package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.R.DBodega_ProAPI.dtos.producto.ProductoGuardar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoModificar;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.Producto;



public interface IProductoService {

    List<ProductoSalida> obtenerTodos();

    Page<ProductoSalida> obtenerTodosPaginados(Pageable pageable);

    ProductoSalida obtenerPorId(Integer id);

    ProductoSalida crear(ProductoGuardar productoGuardar);

    ProductoSalida editar(ProductoModificar productoModificar);

    void eliminarPorId(Integer id);

   Page<Producto> findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
        String nombre, 
        String categoriaNombre, 
        String proveedorNombre, 
        Pageable pageable);


    Optional<Producto> findByNombre(String nombre);

}
