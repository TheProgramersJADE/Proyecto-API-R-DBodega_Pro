package com.example.R.DBodega_ProAPI.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Configuración global
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Mapear Producto -> ProductoSalida sin campos calculados
        mapper.typeMap(Producto.class, ProductoSalida.class)
                .addMappings(m -> {
                    m.skip(ProductoSalida::setEstadoStock);
                    m.skip(ProductoSalida::setEstadoStockClass);
                });

        return mapper;
    }
}