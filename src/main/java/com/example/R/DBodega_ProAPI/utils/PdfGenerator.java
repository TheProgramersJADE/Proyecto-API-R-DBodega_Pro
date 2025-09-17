package com.example.R.DBodega_ProAPI.utils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;
import com.itextpdf.text.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static byte[] generarPdf(List<KardexMovimiento> lista, String titulo) {
        try {
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 80, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Pie de página
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY);
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_CENTER,
                            new Phrase("R&D Bodega Pro", footerFont),
                            (document.right() + document.left()) / 2,
                            document.bottom() - 10, 0);
                }
            });

            document.open();

            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fontSubHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
            Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // --- LOGO ---
            try {
                Image logo = Image.getInstance(new ClassPathResource("static/logo.png").getURL());
                logo.scaleToFit(200, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("Logo no encontrado, se continuará sin él");
            }

            // --- TÍTULO ---
            Paragraph title = new Paragraph(titulo, fontHeader);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            title.setSpacingAfter(10);
            document.add(title);

            // --- FECHA DE EMISIÓN ---
            Paragraph fecha = new Paragraph("Fecha de emisión: " + LocalDateTime.now().format(DATE_FORMATTER),
                    fontSubHeader);
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(20);
            document.add(fecha);

            // --- TABLA ---
            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 3f, 4f, 3f, 3f, 4f, 2f, 2f, 3f, 3f, 3f, 2f, 3f });
            table.setHeaderRows(1);

            // Header
            addTableHeader(table, "Fecha", fontBody);
            addTableHeader(table, "Producto", fontBody);
            addTableHeader(table, "Categoría", fontBody);
            addTableHeader(table, "Proveedor", fontBody);
            addTableHeader(table, "Tipo Movimiento", fontBody);
            addTableHeader(table, "Cant. Entrada", fontBody);
            addTableHeader(table, "Cant. Salida", fontBody);
            addTableHeader(table, "Precio Compra", fontBody);
            addTableHeader(table, "Precio Venta", fontBody);
            addTableHeader(table, "Precio Movimiento", fontBody);
            addTableHeader(table, "Stock Actual", fontBody);
            addTableHeader(table, "Costo Promedio", fontBody);

            // Filas
            for (KardexMovimiento k : lista) {
                table.addCell(new Phrase(k.getFecha().format(DATE_FORMATTER), fontBody));
                table.addCell(new Phrase(k.getNombreProducto(), fontBody));
                table.addCell(new Phrase(k.getCategoria(), fontBody));
                table.addCell(new Phrase(k.getProveedor(), fontBody));
                table.addCell(new Phrase(k.getTipoMovimiento(), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadEntrada()), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadSalida()), fontBody));
                table.addCell(new Phrase(k.getPrecioCompra() != null ? k.getPrecioCompra().toString() : "0", fontBody));
                table.addCell(new Phrase(k.getPrecioVenta() != null ? k.getPrecioVenta().toString() : "0", fontBody));
                table.addCell(new Phrase(k.getPrecio() != null ? k.getPrecio().toString() : "0", fontBody));
                table.addCell(new Phrase(String.valueOf(k.getStockActual()), fontBody));
                table.addCell(
                        new Phrase(k.getCostoPromedio() != null ? k.getCostoPromedio().toString() : "0", fontBody));
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    private static void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(new BaseColor(200, 200, 200));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        header.setPhrase(new Phrase(headerTitle, font));
        table.addCell(header);
    }
}