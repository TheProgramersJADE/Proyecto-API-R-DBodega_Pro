package com.example.R.DBodega_ProAPI.dtos.categoria;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaModificar implements Serializable {
        @JsonIgnore
    private Integer id;
    private String nombre;
    private String descripcion;

}
