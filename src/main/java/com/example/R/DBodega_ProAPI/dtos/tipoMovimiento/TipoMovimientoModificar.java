package com.example.R.DBodega_ProAPI.dtos.tipoMovimiento;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoMovimientoModificar implements Serializable{
        @JsonIgnore
    private Integer id;
    
    private String nombre;
    private Boolean editarCosto;
    private Integer tipo;
}