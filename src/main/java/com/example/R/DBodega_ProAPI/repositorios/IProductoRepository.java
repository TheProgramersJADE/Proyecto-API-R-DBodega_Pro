package com.example.R.DBodega_ProAPI.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.R.DBodega_ProAPI.modelos.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Integer> {
    Page<Producto> findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndProveedor_NombreContainingIgnoreCaseOrderByIdDesc(
        String nombre, 
        String categoriaNombre, 
        String proveedorNombre, 
        Pageable pageable);


    Optional<Producto> findByNombre(String nombre);

}
