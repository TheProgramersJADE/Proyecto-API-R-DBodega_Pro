package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorGuardar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorModificar;
import com.example.R.DBodega_ProAPI.dtos.proveedor.ProveedorSalida;
import com.example.R.DBodega_ProAPI.modelos.Proveedor;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IProveedorService;

@Service
public class ProveedorService implements IProveedorService{

    @Override
    public List<ProveedorSalida> obtenerTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodos'");
    }

    @Override
    public Page<ProveedorSalida> obtenerTodosPaginados(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosPaginados'");
    }

    @Override
    public ProveedorSalida obtenerPorId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPorId'");
    }

    @Override
    public ProveedorSalida crear(ProveedorGuardar proveedorGuardar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crear'");
    }

    @Override
    public ProveedorSalida editar(ProveedorModificar proveedorModificar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editar'");
    }

    @Override
    public void eliminarPorId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPorId'");
    }

    @Override
    public Page<Proveedor> findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc(
            String nombre, String nombreEmpresa, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombreContainingIgnoreCaseAndNombreEmpresaContainingIgnoreCaseOrderByIdDesc'");
    }

    
}