package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Return;
import java.util.List;
import java.util.Optional;

public interface IReturnService {
    List<Return> findAll();
    Optional<Return> findById(Long id);
    Return save(Return r);
    void deleteById(Long id);
    boolean existsById(Long id);
}