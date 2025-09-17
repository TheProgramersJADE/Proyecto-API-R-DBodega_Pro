package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.repositorios.IMovimientoEntradaSalidaRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IKardexService;
import com.example.R.DBodega_ProAPI.utils.PdfGenerator;

@Service
public class KardexService implements IKardexService {

    @Autowired
    private IMovimientoEntradaSalidaRepository movimientoRepo;

    @Autowired
    private IProductoRepository productoRepo;

    @Override
    public List<KardexMovimiento> obtenerKardexPorProducto(Integer productoId) {
 Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        List<MovimientoEntradaSalida> movimientos = movimientoRepo.findAll().stream()
                .filter(m -> m.getProducto().getId().equals(productoId))
                .sorted(Comparator.comparing(MovimientoEntradaSalida::getFecha))
                .collect(Collectors.toList());

        List<KardexMovimiento> kardexList = new ArrayList<>();
        int stockAcumulado = 0;

        for (MovimientoEntradaSalida m : movimientos) {
            KardexMovimiento dto = new KardexMovimiento();
            dto.setIdMovimiento(m.getId());
            dto.setNombreProducto(producto.getNombre());
            dto.setCategoria(producto.getCategoria().getNombre());
            dto.setProveedor(producto.getProveedor().getNombre());
            dto.setTipoMovimiento(m.getTipoMovimiento().getNombre());
            dto.setFecha(m.getFecha());

            if (m.getTipoMovimiento().getTipo() == 1) {
                dto.setCantidadEntrada(m.getCantidad());
                dto.setCantidadSalida(0);
            } else {
                dto.setCantidadEntrada(0);
                dto.setCantidadSalida(m.getCantidad());
            }

            dto.setPrecio(m.getPrecio());
            dto.setTotal(m.getPrecio().multiply(BigDecimal.valueOf(m.getCantidad())));
            stockAcumulado += dto.getCantidadEntrada() - dto.getCantidadSalida();
            dto.setStockActual(stockAcumulado);
            dto.setPrecioCompra(producto.getPrecio_compra());
            dto.setPrecioVenta(producto.getPrecio_venta());
            dto.setCostoPromedio(producto.getCosto_promedio());
            dto.setObservaciones(m.getObservaciones());

            kardexList.add(dto);
        }

        return kardexList;
    }

    @Override
    public List<KardexMovimiento> obtenerTodosKardex() {
        List<KardexMovimiento> todosKardex = new ArrayList<>();
        List<Producto> productos = productoRepo.findAll();

        for (Producto producto : productos) {
            List<KardexMovimiento> kardexProducto = obtenerKardexPorProducto(producto.getId());
            todosKardex.addAll(kardexProducto);
        }

        return todosKardex;
    }

    @Override
    public byte[] generarPdfKardex(Integer productoId) throws Exception {
        List<KardexMovimiento> kardex = obtenerKardexPorProducto(productoId);
        return PdfGenerator.generarPdf(kardex, "KARDEX DEL PRODUCTO: " + kardex.get(0).getNombreProducto());
    }

    @Override
    public byte[] generarPdfTodosKardex() throws Exception {
        List<KardexMovimiento> todosKardex = obtenerTodosKardex();
        return PdfGenerator.generarPdf(todosKardex, "KARDEX DE TODOS LOS PRODUCTOS");
    }
}