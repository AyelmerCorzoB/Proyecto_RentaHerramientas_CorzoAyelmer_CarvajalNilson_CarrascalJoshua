package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.DamageReport;

import java.util.List;
import java.util.Optional;

public interface IDamageReportService {
    List<DamageReport> findAll();
    Optional<DamageReport> findById(Long id);
    DamageReport save(DamageReport damageReport);
    void deleteById(Long id);
    boolean existsById(Long id);
}
