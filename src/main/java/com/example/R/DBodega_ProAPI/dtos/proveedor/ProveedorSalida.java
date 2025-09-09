package com.example.R.DBodega_ProAPI.dtos.proveedor;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorSalida implements Serializable{
    private Integer id;
    private String nombre;
    private String nombreEmpresa;
    private String telefono;
    private String email;
    private String direccion;
}
