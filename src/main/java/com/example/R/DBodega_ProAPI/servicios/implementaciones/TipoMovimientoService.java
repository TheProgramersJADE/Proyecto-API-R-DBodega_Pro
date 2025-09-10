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

import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoGuardar;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoModificar;
import com.example.R.DBodega_ProAPI.dtos.tipoMovimiento.TipoMovimientoSalida;
import com.example.R.DBodega_ProAPI.modelos.TipoMovimiento;
import com.example.R.DBodega_ProAPI.repositorios.ITipoMovimientoRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.ITipoMovimientoService;

@Service
public class TipoMovimientoService implements ITipoMovimientoService {

    @Autowired
    private ITipoMovimientoRepository tipoMovimientoRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public List<TipoMovimientoSalida> obtenerTodos() {
        List<TipoMovimiento> tiposMovimiento = tipoMovimientoRepository.findAll();
        return tiposMovimiento.stream()
                .map(tipoMovimiento -> modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TipoMovimientoSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<TipoMovimiento> page = tipoMovimientoRepository.findAll(pageable);

        List<TipoMovimientoSalida> tiposMovimientoDto = page.stream()
                .map(tipoMovimiento -> modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(tiposMovimientoDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public TipoMovimientoSalida obtenerPorId(Integer id) {
        return modelMapper.map(tipoMovimientoRepository.findById(id).get(), TipoMovimientoSalida.class);
    }

    @Override
    public TipoMovimientoSalida editar(TipoMovimientoModificar tipoMovimientoModificar) {
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.save(modelMapper.map(tipoMovimientoModificar, TipoMovimiento.class));
        return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        tipoMovimientoRepository.deleteById(id);
    }

    @Override
    public Page<TipoMovimiento> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre, Pageable pageable) {
        return tipoMovimientoRepository.findByNombreContainingIgnoreCaseOrderByIdDesc(
                nombre, pageable);
    }

    @Override
    public Optional<TipoMovimiento> findByNombre(String nombre) {
        return tipoMovimientoRepository.findByNombre(nombre);
    }

    @Override
    public TipoMovimientoSalida crear(TipoMovimientoGuardar tipoMovimientoGuardar) {
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.save(modelMapper.map(tipoMovimientoGuardar, TipoMovimiento.class));
        return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);
    }
}