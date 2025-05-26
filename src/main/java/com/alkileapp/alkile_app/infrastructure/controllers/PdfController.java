package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Genera un comprobante de reserva en formato PDF
     *
     * @param id ID de la reserva
     * @return ResponseEntity con el PDF generado
     */
    @GetMapping("/receipt/reservation/{id}")
    public ResponseEntity<byte[]> generateReceiptForReservation(@PathVariable Long id) {
        try {
            // Validación básica del ID
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de reserva inválido");
            }

            byte[] pdfBytes = pdfService.generateReceiptFromReservation(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=comprobante_reserva_" + id + ".pdf")
                    .body(pdfBytes);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al generar el comprobante PDF",
                    e);
        }
    }

    /**
     * Endpoint alternativo para generar comprobante de pago
     *
     * @param clientName Nombre del cliente
     * @param amount     Monto del pago
     * @return ResponseEntity con el PDF generado
     */
    @GetMapping("/receipt/payment")
    public ResponseEntity<byte[]> generatePaymentReceipt(
            @RequestParam String clientName,
            @RequestParam double amount) {

        // Validación básica de parámetros
        if (clientName == null || clientName.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del cliente es requerido"
            );
        }

        if (amount <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El monto debe ser mayor a cero"
            );
        }

        byte[] pdfBytes = pdfService.generatePaymentReceipt(clientName, amount);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=comprobante_pago.pdf")
                .body(pdfBytes);
    }
}