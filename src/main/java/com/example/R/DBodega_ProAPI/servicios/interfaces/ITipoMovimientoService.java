package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoModificar;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoSalida;
import com.example.R.DBodega_ProAPI.modelos.TipoMovimiento;

public interface ITipoMovimientoService {
    List<TipoMovimientoSalida> obtenerTodos();

    Page<TipoMovimientoSalida> obtenerTodosPaginados(Pageable pageable);

    TipoMovimientoSalida obtenerPorId(Integer id);

    TipoMovimientoSalida crear(TipoMovimientoSalida tipoMovimientoSalida);

    TipoMovimientoSalida editar(TipoMovimientoModificar tipoMovimientoModificar);

    void eliminarPorId(Integer id);

    Page<TipoMovimiento> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre, Pageable pageable);
    Optional<TipoMovimiento> findByNombre(String nombre);
}
