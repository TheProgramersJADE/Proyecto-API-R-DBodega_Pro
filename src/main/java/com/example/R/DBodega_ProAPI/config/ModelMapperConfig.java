package com.example.R.DBodega_ProAPI.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.Producto;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper mapper = new ModelMapper();

         // Mapeo personalizado para Producto → ProductoSalida
        mapper.addMappings(new PropertyMap<Producto, ProductoSalida>() {
            @Override
            protected void configure() {
                map().setCategoria_nombre(source.getCategoria().getNombre());
                map().setProveedor_nombre(source.getProveedor().getNombre());
                map().setEstado_stock(source.getEstadoStock().name());
            }
        });

        return mapper;
    }
}