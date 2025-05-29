package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Reservation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PdfService {

    private final IReservationService reservationService;

    public PdfService(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public byte[] generateReceiptFromReservation(Long reservationId) throws IOException {
        Reservation reservation = reservationService.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        try (PDDocument document = new PDDocument()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Título
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("COMPROBANTE DE RESERVA");
                contentStream.endText();

                // Datos del cliente
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 720);
                contentStream.showText("Cliente: " + reservation.getCustomer());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Herramienta: " + reservation.getTool().getName());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha inicio: " + reservation.getStartDate());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha fin: " + reservation.getEndDate());
                contentStream.newLineAtOffset(0, -15);

                // Calcular días y monto
                long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
                double amount = days * 84.96; // Ejemplo fijo por día
                contentStream.showText("Monto total: $" + String.format("%.2f", amount));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha de emisión: " + LocalDate.now());
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] generatePaymentReceipt(String clientName, double amount) {
        // falta implementación
        try (PDDocument document = new PDDocument()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Título
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("COMPROBANTE DE PAGO");
                contentStream.endText();

                // Datos del cliente y monto
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 720);
                contentStream.showText("Cliente: " + clientName);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Monto pagado: $" + String.format("%.2f", amount));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha de emisión: " + LocalDate.now());
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el comprobante de pago", e);
        } 
    }
}