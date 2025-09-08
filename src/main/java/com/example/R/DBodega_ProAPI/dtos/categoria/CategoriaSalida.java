package com.example.R.DBodega_ProAPI.dtos.categoria;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaSalida implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;

}
