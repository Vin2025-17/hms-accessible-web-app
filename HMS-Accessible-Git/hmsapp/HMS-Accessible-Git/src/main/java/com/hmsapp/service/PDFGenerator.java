package com.hmsapp.service;
import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;



import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFGenerator {
    public void generatePdf(String path, Booking booking, Property property){
        try {
            // Specify the output file path
            PdfWriter writer = new PdfWriter(path);
            // Create a PdfDocument with the PdfWriter
            PdfDocument pdfDoc = new PdfDocument(writer);
            // Create a Document instance (this helps add content to the PDF)
            Document document = new Document(pdfDoc);
            // Add content to the document
            document.add(new Paragraph("BOOK MY PLACE PVT LTD."));
            // Close the document (saves the PDF file)
            float[] columnWidths = {1f,1f,1f,1f,1f};
            Table table = new Table(columnWidths);  // Pass column widths here
            // Add table headers
            table.addHeaderCell("Name");
            table.addHeaderCell("Mobile");
            table.addHeaderCell("E-mail");
            table.addHeaderCell("Property Name");
            table.addHeaderCell("No of Guests");
            // Add table rows
            table.addCell(booking.getGuestName());
            table.addCell(booking.getMobile());
            table.addCell(booking.getEmail());
            table.addCell(property.getName());
            table.addCell(String.valueOf(booking.getNoOfGuests()));
            // Add the table to the document
            document.add(table);
            document.add(new Paragraph("THANK YOU BOOKING WITH US."));
            document.add(new Paragraph("ABOVE YOU CAN FIND THE DETAILS OF YOUR BOOKING."));
            document.close();
            System.out.println("PDF created successfully ");
        } catch (IOException e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }
}
