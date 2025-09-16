package com.example.R.DBodega_ProAPI.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaGuardar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaModificar;
import com.example.R.DBodega_ProAPI.dtos.movimientoEntradaSalida.MovimientoEntradaSalidaSalida;
import com.example.R.DBodega_ProAPI.dtos.producto.ProductoSalida;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;
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


      // Guardar → Entidad
        modelMapper.typeMap(MovimientoEntradaSalidaGuardar.class, MovimientoEntradaSalida.class)
            .addMappings(mapper -> {
                mapper.map(MovimientoEntradaSalidaGuardar::getIdProducto,
                           (dest, v) -> dest.getProducto().setId((Integer) v));
                mapper.map(MovimientoEntradaSalidaGuardar::getIdTipoMovimiento,
                           (dest, v) -> dest.getTipoMovimiento().setId((Integer) v));
            });

          // Modificar → Entidad
        modelMapper.typeMap(MovimientoEntradaSalidaModificar.class, MovimientoEntradaSalida.class)
            .addMappings(mapper -> {
                mapper.map(MovimientoEntradaSalidaModificar::getIdProducto,
                           (dest, v) -> dest.getProducto().setId((Integer) v));
                mapper.map(MovimientoEntradaSalidaModificar::getIdTipoMovimiento,
                           (dest, v) -> dest.getTipoMovimiento().setId((Integer) v));
            });

             // Entidad → Salida
        modelMapper.typeMap(MovimientoEntradaSalida.class, MovimientoEntradaSalidaSalida.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getProducto().getId(),
                           MovimientoEntradaSalidaSalida::setIdProducto);
                mapper.map(src -> src.getProducto().getNombre(),
                           MovimientoEntradaSalidaSalida::setProductoNombre);
                mapper.map(src -> src.getTipoMovimiento().getId(),
                           MovimientoEntradaSalidaSalida::setIdTipoMovimiento);
                mapper.map(src -> src.getTipoMovimiento().getNombre(),
                           MovimientoEntradaSalidaSalida::setTipoMovimientoNombre);
                mapper.map(MovimientoEntradaSalida::getFechaStr,
                           MovimientoEntradaSalidaSalida::setFechaStr);
            });

    return modelMapper;

    }
}