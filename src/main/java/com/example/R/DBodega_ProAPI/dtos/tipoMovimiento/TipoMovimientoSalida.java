package com.example.R.DBodega_ProAPI.dtos.tipoMovimiento;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoMovimientoSalida implements Serializable{
    private Integer id;
    private String nombre;
    private String editarCosto;
    private String tipo;
}
