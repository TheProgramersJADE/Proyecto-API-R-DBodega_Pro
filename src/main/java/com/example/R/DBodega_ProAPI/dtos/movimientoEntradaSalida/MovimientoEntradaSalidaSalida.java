package com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoEntradaSalidaSalida implements Serializable {
    private Integer id;
    private Integer idProducto;
    private String productoNombre;
    private Integer idTipoMovimiento;
    private String tipoMovimientoNombre;
    private Integer cantidad;
    private BigDecimal precio;
    private String usuario;
    private LocalDateTime fecha;
    private String fechaStr;
    private String observaciones;
}