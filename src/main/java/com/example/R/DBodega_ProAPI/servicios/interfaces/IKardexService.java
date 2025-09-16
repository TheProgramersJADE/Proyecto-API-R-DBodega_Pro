package com.example.R.DBodega_ProAPI.servicios.interfaces;

import java.util.List;

import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;

public interface IKardexService {
     // Movimientos de un producto específico
    List<KardexMovimiento> obtenerKardexPorProducto(Integer productoId);

    // Todos los movimientos de todos los productos
    List<KardexMovimiento> obtenerTodosKardex();

    // PDF de un producto específico
    byte[] generarPdfKardex(Integer productoId) throws Exception;

    // PDF de todos los movimientos
    byte[] generarPdfTodosKardex() throws Exception;
}