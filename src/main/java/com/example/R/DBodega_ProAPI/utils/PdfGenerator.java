package com.example.R.DBodega_ProAPI.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;

// iText
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
    public static ByteArrayInputStream generarPdf(List<KardexMovimiento> lista) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph title = new Paragraph("KARDEX DE PRODUCTO", fontHeader);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // salto de línea

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 4, 3, 3, 3, 3, 3, 3, 3});

            // Encabezados
            Stream.of("ID", "Producto", "Tipo Movimiento", "Fecha",
                      "Entrada", "Salida", "Precio", "Total", "Stock")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setPhrase(new Phrase(headerTitle, fontBody));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Filas
            for (KardexMovimiento k : lista) {
                table.addCell(new Phrase(k.getIdMovimiento().toString(), fontBody));
                table.addCell(new Phrase(k.getNombreProducto(), fontBody));
                table.addCell(new Phrase(k.getTipoMovimiento(), fontBody));
                table.addCell(new Phrase(k.getFecha().toString(), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadEntrada()), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getCantidadSalida()), fontBody));
                table.addCell(new Phrase(k.getPrecio().toString(), fontBody));
                table.addCell(new Phrase(k.getTotal().toString(), fontBody));
                table.addCell(new Phrase(String.valueOf(k.getStockActual()), fontBody));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
