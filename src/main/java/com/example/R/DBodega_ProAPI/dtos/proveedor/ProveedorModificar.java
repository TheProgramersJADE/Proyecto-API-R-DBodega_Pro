package com.example.R.DBodega_ProAPI.dtos.proveedor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorModificar implements Serializable{
            @JsonIgnore
    private Integer id;
    
    private String nombre;
    private String nombreEmpresa;
    private String telefono;
    private String email;
    private String direccion;    
}
