package com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoEntradaSalidaGuardar implements Serializable {
    private Integer idProducto;
    private Integer idTipoMovimiento;
    private Integer cantidad;
    private BigDecimal precio;
    private String usuario;
    private String observaciones;
}