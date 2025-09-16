package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;

import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;

public interface IKardexService {
    List<KardexMovimiento> obtenerKardexPorProducto(Integer productoId);

    byte[] generarPdfKardex(Integer productoId) throws Exception;
}