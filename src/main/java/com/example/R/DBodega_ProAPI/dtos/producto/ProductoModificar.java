package com.example.R.DBodega_ProAPI.dtos.producto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoModificar implements Serializable {

 private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio_compra;
    private BigDecimal precio_venta;
    private BigDecimal costo_promedio;
    private Integer stock_actual;
    private Integer stock_minimo;
    private String imagen_url;
    private String categoria_nombre;
    private String proveedor_nombre;
}
