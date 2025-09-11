package com.example.R.DBodega_ProAPI.dtos.tipoMovimiento;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoMovimientoGuardar implements Serializable{
    private String nombre;
    private Boolean editarCosto;
    private Integer tipo;
}