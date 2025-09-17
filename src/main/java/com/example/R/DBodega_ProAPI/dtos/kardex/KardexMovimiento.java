package com.example.R.DBodega_ProAPI.dtos.kardex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KardexMovimiento implements Serializable {
private Integer idMovimiento;
    private String nombreProducto;
    private String categoria;
    private String proveedor;
    private String tipoMovimiento;
    private LocalDateTime fecha;

    // 🔹 Campos que necesitas en KardexService
    private Integer cantidadEntrada;
    private Integer cantidadSalida;
    private BigDecimal precio;
    private BigDecimal total;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal costoPromedio;
    private Integer stockActual;
    private Integer stockMinimo;
    private String estadoStock;
    private String observaciones;
}