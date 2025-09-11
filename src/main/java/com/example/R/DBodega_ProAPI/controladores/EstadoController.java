package com.example.R.DBodega_ProAPI.controladores;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class EstadoController {

 @GetMapping("/estado")
 public String estadoApi() {
     return "La api esta funcionando correctamente";
 }   
}