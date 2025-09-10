package com.example.R.DBodega_ProAPI.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.R.DBodega_ProAPI.modelos.TipoMovimiento;

public interface ITipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {
    Page<TipoMovimiento> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre, Pageable pageable);
    Optional<TipoMovimiento> findByNombre(String nombre);
}
