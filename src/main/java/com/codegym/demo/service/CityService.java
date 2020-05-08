package com.codegym.demo.service;

import com.codegym.demo.model.Cities;
import com.codegym.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService implements ICityService {
    @Autowired
    private CityRepository cityRepository;

    @Override
    public Iterable<Cities> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<Cities> findById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public Cities save(Cities cities) {
        return cityRepository.save(cities);
    }

    @Override
    public void remove(Long id) {
        cityRepository.deleteById(id);
    }
}
