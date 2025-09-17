package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaGuardar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaModificar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaSalida;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;

public interface IMovimientoEntradaSalidaService {
    List<MovimientoEntradaSalidaSalida> obtenerTodos();

    Page<MovimientoEntradaSalidaSalida> obtenerTodosPaginados(Pageable pageable);

    MovimientoEntradaSalidaSalida obtenerPorId(Integer id);

    MovimientoEntradaSalidaSalida crear(MovimientoEntradaSalidaGuardar movimientoEntradaSalidaGuardar);

    MovimientoEntradaSalidaSalida editar(MovimientoEntradaSalidaModificar movimientoEntradaSalidaModificar);

    Page<MovimientoEntradaSalidaSalida> findByProductoNombreContainingIgnoreCaseAndTipoMovimientoNombreOrderByIdDesc(
        String nombreProducto,
        String nombre,
        Pageable pageable);


    List<MovimientoEntradaSalida> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}