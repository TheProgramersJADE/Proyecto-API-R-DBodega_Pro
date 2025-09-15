package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaGuardar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaModificar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaSalida;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;
import com.example.R.DBodega_ProAPI.repositorios.IMovimientoEntradaSalidaRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IMovimientoEntradaSalidaService;

@Service
public class MovimientoEntradaSalidaService implements IMovimientoEntradaSalidaService {

    @Autowired
    private IMovimientoEntradaSalidaRepository movimientoEntradaSalidaRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<MovimientoEntradaSalidaSalida> obtenerTodos() {
    List<MovimientoEntradaSalida> movimientosEntradaSalida = movimientoEntradaSalidaRepository.findAll();
        return movimientosEntradaSalida.stream()
                .map(movimiento -> modelMapper.map(movimiento, MovimientoEntradaSalidaSalida.class))
                .collect(Collectors.toList());

    }

    @Override
    public Page<MovimientoEntradaSalidaSalida> obtenerTodosPaginados(Pageable pageable) {
       Page<MovimientoEntradaSalida> page = movimientoEntradaSalidaRepository.findAll(pageable);

        List<MovimientoEntradaSalidaSalida> movimientosDto = page.stream()
                .map(movimiento -> modelMapper.map(movimiento, MovimientoEntradaSalidaSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(movimientosDto, page.getPageable(), page.getTotalElements());

    }

    @Override
    public MovimientoEntradaSalidaSalida obtenerPorId(Integer id) {
        return modelMapper.map(movimientoEntradaSalidaRepository.findById(id).get(),
                MovimientoEntradaSalidaSalida.class);
    }

    @Override
    public MovimientoEntradaSalidaSalida crear(MovimientoEntradaSalidaGuardar movimientoEntradaSalidaGuardar) {
          MovimientoEntradaSalida movimiento = movimientoEntradaSalidaRepository
                .save(modelMapper.map(movimientoEntradaSalidaGuardar, MovimientoEntradaSalida.class));
        return modelMapper.map(movimiento, MovimientoEntradaSalidaSalida.class);
    }

    @Override
    public MovimientoEntradaSalidaSalida editar(MovimientoEntradaSalidaModificar movimientoEntradaSalidaModificar) {
        MovimientoEntradaSalida movimiento = movimientoEntradaSalidaRepository
                .save(modelMapper.map(movimientoEntradaSalidaModificar, MovimientoEntradaSalida.class));
        return modelMapper.map(movimiento, MovimientoEntradaSalidaSalida.class);

    }

    @Override
    public Page<MovimientoEntradaSalida> findByProductoNombreContainingIgnoreCaseAndTipoMovimientoNombreOrderByIdDesc(
            String nombreProducto, String nombre, Pageable pageable) {
       
                return movimientoEntradaSalidaRepository.findByProductoNombreContainingIgnoreCaseAndTipoMovimientoNombreOrderByIdDesc(nombre, nombreProducto, pageable);
    }

    @Override
    public List<MovimientoEntradaSalida> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
          return movimientoEntradaSalidaRepository.findByFechaBetween(inicio,fin);
    }

}
