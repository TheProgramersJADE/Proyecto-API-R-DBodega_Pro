package com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoEntradaSalidaModificar implements Serializable {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;
    private Integer idProducto;
    private Integer idTipoMovimiento;
    private Integer cantidad;
    private BigDecimal precio;
    private String usuario;
    private String observaciones;
}