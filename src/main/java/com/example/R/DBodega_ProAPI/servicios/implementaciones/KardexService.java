package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;
import com.example.R.DBodega_ProAPI.modelos.MovimientoEntradaSalida;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.repositorios.IMovimientoEntradaSalidaRepository;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IKardexService;
// iText
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class KardexService implements IKardexService {

    @Autowired
    private IMovimientoEntradaSalidaRepository movimientoRepo;

    @Autowired
    private IProductoRepository productoRepo;

    // @Autowired
    // private ModelMapper modelMapper;

    @Override
    public List<KardexMovimiento> obtenerKardexPorProducto(Integer productoId) {
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        List<MovimientoEntradaSalida> movimientos = movimientoRepo.findAll();
        List<KardexMovimiento> kardexList = new ArrayList<>();

        movimientos.stream()
                .filter(m -> m.getProducto().getId().equals(productoId))
                .sorted(Comparator.comparing(MovimientoEntradaSalida::getFecha))
                .forEach(m -> {
                    KardexMovimiento dto = new KardexMovimiento();
                    dto.setIdMovimiento(m.getId());
                    dto.setNombreProducto(producto.getNombre());
                    dto.setTipoMovimiento(m.getTipoMovimiento().getNombre());
                    dto.setFecha(m.getFecha());
                    if (m.getTipoMovimiento().getTipo() == 1) { // Entrada
                        dto.setCantidadEntrada(m.getCantidad());
                        dto.setCantidadSalida(0);
                    } else { // Salida o Ajuste
                        dto.setCantidadEntrada(0);
                        dto.setCantidadSalida(m.getCantidad());
                    }
                    dto.setPrecio(m.getPrecio());
                    dto.setTotal(m.getPrecio().multiply(BigDecimal.valueOf(m.getCantidad())));
                    // calcular stock acumulado
                    int stockActual = kardexList.isEmpty() ? 0
                            : kardexList.get(kardexList.size() - 1).getStockActual();
                    stockActual += dto.getCantidadEntrada() - dto.getCantidadSalida();
                    dto.setStockActual(stockActual);
                    dto.setObservaciones(m.getObservaciones());
                    kardexList.add(dto);
                });

        return kardexList;
    }

    @Override
    public byte[] generarPdfKardex(Integer productoId) throws Exception {
        List<KardexMovimiento> kardexList = obtenerKardexPorProducto(productoId);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

        Paragraph title = new Paragraph("KARDEX DE PRODUCTO", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3f, 4f, 4f, 3f, 3f, 3f, 3f, 3f, 5f });

        // Header
        Stream.of("ID", "Producto", "Tipo Movimiento", "Fecha", "Cant. Entrada", "Cant. Salida",
                "Precio", "Total", "Stock Actual")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPhrase(new Phrase(headerTitle, fontBody));
                    table.addCell(header);
                });

        // Body
        for (KardexMovimiento k : kardexList) {
            table.addCell(new PdfPCell(new Phrase(k.getIdMovimiento().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getNombreProducto(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getTipoMovimiento(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getFecha().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getCantidadEntrada()), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getCantidadSalida()), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getPrecio().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getTotal().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getStockActual()), fontBody)));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public List<KardexMovimiento> obtenerTodosKardex() {
    List<Producto> productos = productoRepo.findAll();
    List<KardexMovimiento> todosKardex = new ArrayList<>();

    for (Producto producto : productos) {
        List<MovimientoEntradaSalida> movimientos = movimientoRepo.findAll();
        
        movimientos.stream()
            .filter(m -> m.getProducto().getId().equals(producto.getId()))
            .sorted(Comparator.comparing(MovimientoEntradaSalida::getFecha))
            .forEach(m -> {
                KardexMovimiento dto = new KardexMovimiento();
                dto.setIdMovimiento(m.getId());
                dto.setNombreProducto(producto.getNombre());
                dto.setTipoMovimiento(m.getTipoMovimiento().getNombre());
                dto.setFecha(m.getFecha());
                if (m.getTipoMovimiento().getTipo() == 1) { // Entrada
                    dto.setCantidadEntrada(m.getCantidad());
                    dto.setCantidadSalida(0);
                } else { // Salida o Ajuste
                    dto.setCantidadEntrada(0);
                    dto.setCantidadSalida(m.getCantidad());
                }
                dto.setPrecio(m.getPrecio());
                dto.setTotal(m.getPrecio().multiply(BigDecimal.valueOf(m.getCantidad())));
                
                int stockActual = todosKardex.stream()
                                    .filter(k -> k.getNombreProducto().equals(producto.getNombre()))
                                    .mapToInt(KardexMovimiento::getStockActual)
                                    .reduce(0, (a, b) -> b); // último stock
                stockActual += dto.getCantidadEntrada() - dto.getCantidadSalida();
                dto.setStockActual(stockActual);

                dto.setObservaciones(m.getObservaciones());
                todosKardex.add(dto);
            });
    }

    return todosKardex;
}

    @Override
    public byte[] generarPdfTodosKardex() throws Exception {
        List<KardexMovimiento> todosKardex = obtenerTodosKardex();

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

        Paragraph title = new Paragraph("KARDEX DE TODOS LOS PRODUCTOS", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3f, 4f, 4f, 3f, 3f, 3f, 3f, 3f, 5f });

        // Header
        Stream.of("ID", "Producto", "Tipo Movimiento", "Fecha", "Cant. Entrada", "Cant. Salida",
                "Precio", "Total", "Stock Actual")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPhrase(new Phrase(headerTitle, fontBody));
                    table.addCell(header);
                });

        // Body
        for (KardexMovimiento k : todosKardex) {
            table.addCell(new PdfPCell(new Phrase(k.getIdMovimiento().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getNombreProducto(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getTipoMovimiento(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getFecha().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getCantidadEntrada()), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getCantidadSalida()), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getPrecio().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(k.getTotal().toString(), fontBody)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(k.getStockActual()), fontBody)));
        }

        document.add(table);
        return null;    
    }
}