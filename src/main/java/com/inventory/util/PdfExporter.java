package com.inventory.util;

import com.inventory.model.Producto;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.util.List;

public class PdfExporter {
    
    public static boolean exportarProductos(List<Producto> productos, String rutaPDF) throws Exception {
        if (!rutaPDF.endsWith(".pdf")) {
            rutaPDF += ".pdf";
        }

        PdfWriter writer = new PdfWriter(rutaPDF);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph titulo = new Paragraph("Reporte de Productos con Stock")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18);
        document.add(titulo);

        Table tabla = new Table(7);
        tabla.addHeaderCell(new Cell().add(new Paragraph("ID")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Nombre")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Descripción")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Categoría")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Proveedor")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Precio")));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Stock")));

        for (Producto p : productos) {
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(p.getIdProducto()))));
            tabla.addCell(new Cell().add(new Paragraph(p.getNombre())));
            tabla.addCell(new Cell().add(new Paragraph(p.getDescripcion())));
            tabla.addCell(new Cell().add(new Paragraph(p.getNombreCategoria())));
            tabla.addCell(new Cell().add(new Paragraph(p.getNombreProveedor())));
            tabla.addCell(new Cell().add(new Paragraph(p.getPrecio().toString())));
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(p.getStock()))));
        }

        document.add(tabla);
        document.close();

        return true;
    }
}
