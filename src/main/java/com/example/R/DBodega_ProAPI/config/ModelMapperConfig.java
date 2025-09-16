package com.example.R.DBodega_ProAPI.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

         modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE)
        .setSkipNullEnabled(true)
        .setAmbiguityIgnored(true);

    // Converter de Enum a String
    Converter<Enum<?>, String> enumToString = new Converter<>() {
        @Override
        public String convert(MappingContext<Enum<?>, String> context) {
            return context.getSource() == null ? null : context.getSource().name();
        }
    };

    // Mapear estado_stock usando el converter
    modelMapper.typeMap(Producto.class, ProductoSalida.class)
        .addMappings(mapper -> mapper.using(enumToString)
                                     .map(Producto::getEstadoStock, ProductoSalida::setEstado_stock));

    return modelMapper;

    }
}