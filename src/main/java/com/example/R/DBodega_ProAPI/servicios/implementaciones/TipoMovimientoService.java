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

    private void validarTipo(Integer tipo) {
        if (tipo == null || (tipo < 1 || tipo > 3)) {
            throw new IllegalArgumentException("El tipo debe ser 1 (Entrada), 2 (Salida) o 3 (Ajuste Especial)");
        }
    }

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
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el tipo de movimiento con ID " + id));
        return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);
    }

    @Override
    public TipoMovimientoSalida editar(TipoMovimientoModificar tipoMovimientoModificar) {
        validarTipo(tipoMovimientoModificar.getTipo());

        // Mapear DTO a entidad
        TipoMovimiento tipoMovimiento = modelMapper.map(tipoMovimientoModificar, TipoMovimiento.class);

        // Aplicar reglas de negocio para editarCosto
        if (tipoMovimiento.getTipo() == 1) {
            tipoMovimiento.setEditarCosto(tipoMovimientoModificar.getEditarCosto() != null
                    ? tipoMovimientoModificar.getEditarCosto()
                    : false);
        } else if (tipoMovimiento.getTipo() == 2) {
            tipoMovimiento.setEditarCosto(false);
        } else if (tipoMovimiento.getTipo() == 3) {
            tipoMovimiento.setEditarCosto(tipoMovimientoModificar.getEditarCosto() != null
                    ? tipoMovimientoModificar.getEditarCosto()
                    : false);
        }

        // Guardar en BD
        tipoMovimiento = tipoMovimientoRepository.save(tipoMovimiento);
        return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);

        // TipoMovimiento tipoMovimiento = tipoMovimientoRepository
        // .save(modelMapper.map(tipoMovimientoModificar, TipoMovimiento.class));
        // return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);
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
        validarTipo(tipoMovimientoGuardar.getTipo());

        // Mapear DTO a entidad
        TipoMovimiento tipoMovimiento = modelMapper.map(tipoMovimientoGuardar, TipoMovimiento.class);

        // Aplicar reglas de negocio para editarCosto
        if (tipoMovimiento.getTipo() == 1) {
            tipoMovimiento.setEditarCosto(tipoMovimientoGuardar.getEditarCosto() != null
                    ? tipoMovimientoGuardar.getEditarCosto()
                    : false);
        } else if (tipoMovimiento.getTipo() == 2) {
            tipoMovimiento.setEditarCosto(false);
        } else if (tipoMovimiento.getTipo() == 3) {
            tipoMovimiento.setEditarCosto(tipoMovimientoGuardar.getEditarCosto() != null
                    ? tipoMovimientoGuardar.getEditarCosto()
                    : false);
        }

        // Guardar en BD
        tipoMovimiento = tipoMovimientoRepository.save(tipoMovimiento);
        return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);

        // TipoMovimiento tipoMovimiento = tipoMovimientoRepository
        // .save(modelMapper.map(tipoMovimientoGuardar, TipoMovimiento.class));
        // return modelMapper.map(tipoMovimiento, TipoMovimientoSalida.class);
    }
}