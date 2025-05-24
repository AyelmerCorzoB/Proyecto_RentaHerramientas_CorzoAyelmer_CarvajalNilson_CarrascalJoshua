package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IDamageReportService;
import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.domain.dto.DamageReportDto;
import com.alkileapp.alkile_app.domain.entities.DamageReport;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/damage-reports")
public class DamageReportController {

    private final IDamageReportService damageReportService;
    private final IReservationService reservationService;

    public DamageReportController(IDamageReportService damageReportService, IReservationService reservationService) {
        this.damageReportService = damageReportService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<DamageReportDto>> getAll() {
        List<DamageReportDto> reports = damageReportService.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DamageReportDto> getById(@PathVariable Long id) {
        return damageReportService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DamageReportDto> create(@RequestBody DamageReportDto dto) {
        DamageReport report = toEntity(dto);
        DamageReport saved = damageReportService.save(report);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DamageReportDto> update(@PathVariable Long id, @RequestBody DamageReportDto dto) {
        if (!damageReportService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        DamageReport report = toEntity(dto);
        report.setId(id);
        DamageReport updated = damageReportService.save(report);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!damageReportService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        damageReportService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private DamageReportDto toDto(DamageReport report) {
        return new DamageReportDto(
                report.getId(),
                report.getReservation().getId(),
                report.getDescription(),
                report.getRepairCost(),
                report.getReportDate(),
                report.isResolved()
        );
    }

    private DamageReport toEntity(DamageReportDto dto) {
        DamageReport report = new DamageReport();
        report.setId(dto.id());
        report.setDescription(dto.description());
        report.setRepairCost(dto.repairCost());
        report.setReportDate(dto.reportDate());
        report.setResolved(dto.resolved());
        Reservation reservation = reservationService.findById(dto.reservationId()).orElseThrow();
        report.setReservation(reservation);
        return report;
    }
}