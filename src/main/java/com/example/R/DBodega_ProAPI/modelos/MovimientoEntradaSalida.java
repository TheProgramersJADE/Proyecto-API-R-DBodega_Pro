package com.example.R.DBodega_ProAPI.modelos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movimientosEntradaSalida")
public class MovimientoEntradaSalida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_producto",
            nullable = false,
            foreignKey = @ForeignKey(name = "movimientosEntradaSalida_ibfk_1")
    )
    private Producto producto;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_tipo_movimiento",
            nullable = false,
            foreignKey = @ForeignKey(name = "movimientosEntradaSalida_ibfk_3")
    )
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100)
    @Column( name = "usuario", nullable = false, length = 100)
    private String usuario;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    // Campo transitorio para mostrar fecha formateada
    @Transient
    private String fechaStr;

    public String getFechaStr() {
        if (fecha != null) {
            return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return "";
    }

    public void setFechaStr(String fechaStr) {
        this.fechaStr = fechaStr;
    }

    @Lob
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

}
