package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorGuardar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorModificar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorSalida;

import com.example.R.DBodega_ProAPI.modelos.Proveedor;

public interface IProveedorService {

      List<ProveedorSalida> obtenerTodos();

    Page<ProveedorSalida> obtenerTodosPaginados(Pageable pageable);

    ProveedorSalida obtenerPorId(Integer id);

    ProveedorSalida crear(ProveedorGuardar proveedorGuardar);

    ProveedorSalida editar(ProveedorModificar proveedorModificar);

    void eliminarPorId(Integer id);

    Page<Proveedor> findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc(String nombre, String nombreEmpresa, Pageable pageable);

}