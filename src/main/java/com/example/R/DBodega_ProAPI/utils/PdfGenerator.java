package com.example.R.DBodega_ProAPI.utils;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static byte[] generarPdf(List<KardexMovimiento> lista, String titulo) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph title = new Paragraph(titulo, fontHeader);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);

            // Header
            addTableHeader(table, "Fecha");
            addTableHeader(table, "Producto");
            addTableHeader(table, "Categoría");
            addTableHeader(table, "Proveedor");
            addTableHeader(table, "Tipo Movimiento");
            addTableHeader(table, "Cant. Entrada");
            addTableHeader(table, "Cant. Salida");
            addTableHeader(table, "Precio Compra");
            addTableHeader(table, "Precio Venta");
            addTableHeader(table, "Precio Movimiento");
            addTableHeader(table, "Stock Actual");
            addTableHeader(table, "Costo Promedio");

            // Filas
            for (KardexMovimiento k : lista) {
                table.addCell(new Phrase(k.getFecha().format(DATE_FORMATTER), fontBody));
                table.addCell(new Phrase(k.getNombreProducto(), fontBody));
                table.addCell(new Phrase(k.getCategoria(), fontBody));
                table.addCell(new Phrase(k.getProveedor(), fontBody));
                table.addCell(new Phrase(k.getTipoMovimiento(), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadEntrada()), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadSalida()), fontBody));
                table.addCell(new Phrase(k.getPrecioCompra().toString(), fontBody));
                table.addCell(new Phrase(k.getPrecioVenta().toString(), fontBody));
                table.addCell(new Phrase(k.getPrecio().toString(), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getStockActual()), fontBody));
                table.addCell(new Phrase(k.getCostoPromedio().toString(), fontBody));
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    private static void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(headerTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        table.addCell(header);
    }
}