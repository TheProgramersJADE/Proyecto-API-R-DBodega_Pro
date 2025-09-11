package com.example.R.DBodega_ProAPI.dtos.producto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoSalida implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio_compra;
    private BigDecimal precio_venta;
    private BigDecimal costo_promedio;
    private Integer stock_actual;
    private Integer stock_minimo;
    private String imagen_url;

    // Relaciones simplificadas (puedes ajustar según tu necesidad)
    private Integer idCategoria;
    private String nombreCategoria;

    private Integer idProveedor;
    private String nombreProveedor;

    // Información calculada
    private String estadoStock; // Texto: Agotado, Terminando, Disponible
    private String estadoStockClass; // badge CSS
}
