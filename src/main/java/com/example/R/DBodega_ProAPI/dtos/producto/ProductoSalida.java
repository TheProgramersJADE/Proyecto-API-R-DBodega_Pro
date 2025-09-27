package com.example.R.DBodega_ProAPI.dtos.producto;

import java.io.Serializable;
import java.math.BigDecimal;


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
    private String categoriaNombre;   
     private String proveedorNombre; 
    private String estadoStock;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio_compra() { return precio_compra; }
    public void setPrecio_compra(BigDecimal precio_compra) { this.precio_compra = precio_compra; }

    public BigDecimal getPrecio_venta() { return precio_venta; }
    public void setPrecio_venta(BigDecimal precio_venta) { this.precio_venta = precio_venta; }

    public BigDecimal getCosto_promedio() { return costo_promedio; }
    public void setCosto_promedio(BigDecimal costo_promedio) { this.costo_promedio = costo_promedio; }

    public Integer getStock_actual() { return stock_actual; }
    public void setStock_actual(Integer stock_actual) { this.stock_actual = stock_actual; }

    public Integer getStock_minimo() { return stock_minimo; }
    public void setStock_minimo(Integer stock_minimo) { this.stock_minimo = stock_minimo; }

    public String getImagen_url() { return imagen_url; }
    public void setImagen_url(String imagen_url) { this.imagen_url = imagen_url; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }

    public String getEstadoStock() { return estadoStock; }
    public void setEstadoStock(String estadoStock) { this.estadoStock = estadoStock; }



}
