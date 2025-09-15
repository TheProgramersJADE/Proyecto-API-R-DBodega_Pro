package com.example.R.DBodega_ProAPI.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.R.DBodega_ProAPI.modelos.Proveedor;

public interface IProveedorRepository extends JpaRepository<Proveedor, Integer> {
    Page<Proveedor> findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc(String nombre, String nombreEmpresa, Pageable pageable);

        Optional<Proveedor> findByNombre(String nombre);
}
