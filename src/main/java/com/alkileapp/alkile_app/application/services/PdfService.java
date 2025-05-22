package com.alkileapp.alkile_app.application.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PdfService {

    public byte[] generatePaymentReceipt(String clientName, double amount) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
               
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("COMPROBANTE DE PAGO - RENTALTERMINALS");
                contentStream.endText();
                
              
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 650);
                contentStream.showText("Cliente: " + clientName);
                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Monto: $" + amount);
                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Fecha: " + LocalDateTime.now());
                contentStream.endText();
            }
            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}