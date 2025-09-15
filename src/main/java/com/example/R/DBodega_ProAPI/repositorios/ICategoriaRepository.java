package com.example.R.DBodega_ProAPI.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.R.DBodega_ProAPI.modelos.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

        Page<Categoria> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre, Pageable pageable);

            Optional<Categoria> findByNombre(String nombre);

}

