package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaGuardar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaModificar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaSalida;
import com.example.R.DBodega_ProAPI.modelos.Categoria;

public interface ICategoriaService {

    List<CategoriaSalida> obtenerTodos();

    Page<CategoriaSalida> obtenerTodosPaginados(Pageable pageable);

    CategoriaSalida obtenerPorId(Integer id);

    CategoriaSalida crear(CategoriaGuardar categoriaGuardar);

    CategoriaSalida editar(CategoriaModificar categoriaModificar);

    void eliminarPorId(Integer id);

    Page<Categoria> findByNombreContainingIgnoreCaseOrderByIdAsc(String nombre, Pageable pageable);

}
