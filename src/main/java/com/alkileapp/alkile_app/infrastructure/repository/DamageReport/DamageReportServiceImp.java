package com.alkileapp.alkile_app.infrastructure.repository.DamageReport;

import com.alkileapp.alkile_app.application.services.IDamageReportService;
import com.alkileapp.alkile_app.domain.entities.DamageReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DamageReportServiceImp implements IDamageReportService {

    private final DamageReportRepository damageReportRepository;

    public DamageReportServiceImp(DamageReportRepository damageReportRepository) {
        this.damageReportRepository = damageReportRepository;
    }

    @Override
    public List<DamageReport> findAll() {
        return damageReportRepository.findAll();
    }

    @Override
    public Optional<DamageReport> findById(Long id) {
        return damageReportRepository.findById(id);
    }

    @Override
    public DamageReport save(DamageReport damageReport) {
        return damageReportRepository.save(damageReport);
    }

    @Override
    public void deleteById(Long id) {
        damageReportRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return damageReportRepository.existsById(id);
    }
}
