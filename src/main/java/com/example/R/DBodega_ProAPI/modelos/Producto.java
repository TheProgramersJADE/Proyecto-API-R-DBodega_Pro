package com.example.R.DBodega_ProAPI.modelos;

import java.math.BigDecimal;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_compra;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_venta;

    @NotNull(message = "El costo promedio es obligatorio")
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "costo_promedio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo_promedio;

    @NotNull(message = "El stock actual es obligatorio")
    @Min(0)
    @Column(name = "stock_actual", nullable = false)
    private Integer stock_actual;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(0)
    @Column(name = "stock_minimo", nullable = false)
    private Integer stock_minimo;

    @Column(name = "imagen_url")
    private String imagen_url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "producto_ibfk_categoria"))
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proveedor", nullable = false, foreignKey = @ForeignKey(name = "producto_ibfk_proveedor"))
    private Proveedor proveedor;

    // --- ENUM para el estado del stock ---
    public enum EstadoStock {
        AGOTADO,
        TERMINANDO,
        BUENO
    }

    // Devuelve el texto del estado del stock
    public String getEstadoStockText() {
        if (this.getEstadoStock() == EstadoStock.AGOTADO) {
            return "Agotado";
        } else if (this.getEstadoStock() == EstadoStock.TERMINANDO) {
            return "Terminando";
        } else {
            return "Disponible"; // BUENO
        }
    }

    // Devuelve una clase CSS según el estado
    public String getEstadoStockClass() {
        if (this.getEstadoStock() == EstadoStock.AGOTADO) {
            return "badge bg-danger";
        } else if (this.getEstadoStock() == EstadoStock.TERMINANDO) {
            return "badge bg-warning text-dark";
        } else {
            return "badge bg-success"; // BUENO
        }
    }

    // método para calcular estado según stock
    public EstadoStock getEstadoStock() {
        if (this.stock_actual <= 0) {
            return EstadoStock.AGOTADO;
        } else if (this.stock_actual <= this.stock_minimo) {
            return EstadoStock.TERMINANDO;
        } else {
            return EstadoStock.BUENO;
        }
    }

}
