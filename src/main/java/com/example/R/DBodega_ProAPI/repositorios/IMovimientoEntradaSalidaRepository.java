package com.example.R.DBodega_ProAPI.repositorios;

import java.time.LocalDateTime;
import java.util.List;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.R.DBodega_ProAPI.modelos.Categoria;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;

public interface IMovimientoEntradaSalidaRepository extends JpaRepository<Categoria, Integer> {
    Page<MovimientoEntradaSalida> findByProductoNombreContainingIgnoreCaseAndTipoMovimientoNombreOrderByIdDesc(
            String nombreProducto,
            String nombre,
            Pageable pageable);

    List<MovimientoEntradaSalida> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}