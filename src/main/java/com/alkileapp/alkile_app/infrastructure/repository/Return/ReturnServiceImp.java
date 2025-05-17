package com.alkileapp.alkile_app.infrastructure.repository.Return;

import com.alkileapp.alkile_app.application.services.IReturnService;
import com.alkileapp.alkile_app.domain.entities.Return;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnServiceImp implements IReturnService {

    private final ReturnRepository returnRepository;

    public ReturnServiceImp(ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

    @Override
    public List<Return> findAll() {
        return returnRepository.findAll();
    }

    @Override
    public Optional<Return> findById(Long id) {
        return returnRepository.findById(id);
    }

    @Override
    public Return save(Return r) {
        return returnRepository.save(r);
    }

    @Override
    public void deleteById(Long id) {
        returnRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return returnRepository.existsById(id);
    }
}