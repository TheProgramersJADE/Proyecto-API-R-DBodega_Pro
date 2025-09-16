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
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.modelos.TipoMovimiento;
import com.example.R.DBodega_ProAPI.repositorios.IMovimientoEntradaSalidaRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.repositorios.ITipoMovimientoRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IMovimientoEntradaSalidaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class MovimientoEntradaSalidaService implements IMovimientoEntradaSalidaService {

    @Autowired
    private IMovimientoEntradaSalidaRepository movimientoEntradaSalidaRepository;

    @Autowired
     private  IProductoRepository productoRepository;

    @Autowired
    private  ITipoMovimientoRepository tipoMovimientoRepository;

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
        @Transactional
    public MovimientoEntradaSalidaSalida crear(MovimientoEntradaSalidaGuardar movimientoEntradaSalidaGuardar) {
          MovimientoEntradaSalida movimiento = modelMapper.map(movimientoEntradaSalidaGuardar, MovimientoEntradaSalida.class);

          // 🔑 Asegurar que Hibernate haga INSERT
        movimiento.setId(null);

        // Resolver relaciones
        Producto producto = productoRepository.findById(movimientoEntradaSalidaGuardar.getIdProducto())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        TipoMovimiento tipo = tipoMovimientoRepository.findById(movimientoEntradaSalidaGuardar.getIdTipoMovimiento())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de movimiento no encontrado"));

        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipo);

        // ✅ Corregir lógica de stock (tu código tenía un error en setStock_actual)
        if (tipo.getNombre().equalsIgnoreCase("entrada")) {
            producto.setStock_actual(producto.getStock_actual() + movimientoEntradaSalidaGuardar.getCantidad());
        } else {
            producto.setStock_actual(producto.getStock_actual() - movimientoEntradaSalidaGuardar.getCantidad());
        }
        productoRepository.save(producto);

        movimiento = movimientoEntradaSalidaRepository.save(movimiento);
        return modelMapper.map(movimiento, MovimientoEntradaSalidaSalida.class);
    }

    @Override
        @Transactional
    public MovimientoEntradaSalidaSalida editar(MovimientoEntradaSalidaModificar movimientoEntradaSalidaModificar) {
         // 🔑 Buscar primero la entidad existente para evitar detached entity
        MovimientoEntradaSalida existente = movimientoEntradaSalidaRepository.findById(movimientoEntradaSalidaModificar.getId())
                .orElseThrow(() -> new EntityNotFoundException("Movimiento con id " + movimientoEntradaSalidaModificar.getId() + " no encontrado"));

        // Mapear solo los cambios sobre la entidad encontrada
        modelMapper.map(movimientoEntradaSalidaModificar, existente);

        existente = movimientoEntradaSalidaRepository.save(existente);
        return modelMapper.map(existente, MovimientoEntradaSalidaSalida.class);

    }

   /* @Override
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

    }*/

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
