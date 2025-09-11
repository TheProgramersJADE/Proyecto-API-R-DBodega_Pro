package com.example.R.DBodega_ProAPI.modelos;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipoMovimientos")
public class TipoMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Column(name = "editar_costo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @NotNull(message = "El valor de editar_costo es obligatorio")
    private Boolean editarCosto;

    @Column(name = "tipo", nullable = false)
    @NotNull(message = "El valor de tipo es obligatorio")
    private Integer tipo;

}