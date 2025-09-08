package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaGuardar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaModificar;
import com.example.R.DBodega_ProAPI.dtos.categoria.CategoriaSalida;
import com.example.R.DBodega_ProAPI.modelos.Categoria;
import com.example.R.DBodega_ProAPI.repositorios.ICategoriaRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.ICategoriaService;

@Service
public class CategoriaService implements ICategoriaService{

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoriaSalida> obtenerTodos() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoriaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Categoria> page = categoriaRepository.findAll(pageable);

        List<CategoriaSalida> categoriasDto = page.stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaSalida.class))
                .collect(Collectors.toList());

        return new PageImpl<>(categoriasDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public CategoriaSalida obtenerPorId(Integer id) {
        return modelMapper.map(categoriaRepository.findById(id).get(), CategoriaSalida.class);
    }

    @Override
    public CategoriaSalida crear(CategoriaGuardar categoriaGuardar) {
        Categoria categoria = categoriaRepository.save(modelMapper.map(categoriaGuardar, Categoria.class));
        return modelMapper.map(categoria, CategoriaSalida.class);
    }

    @Override
    public CategoriaSalida editar(CategoriaModificar categoriaModificar) {
        Categoria categoria = categoriaRepository.save(modelMapper.map(categoriaModificar, Categoria.class));
        return modelMapper.map(categoria, CategoriaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public Page<Categoria> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre, Pageable pageable) {
        return categoriaRepository.findByNombreContainingIgnoreCaseOrderByIdDesc(nombre, pageable);
    }

}
