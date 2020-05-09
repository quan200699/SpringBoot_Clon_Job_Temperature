package com.codegym.demo.service.temperature;

import com.codegym.demo.model.Cities;
import com.codegym.demo.model.Temperatures;
import com.codegym.demo.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemperatureService implements ITemperatureService {
    @Autowired
    private TemperatureRepository temperatureRepository;

    @Override
    public Iterable<Temperatures> findAll() {
        return temperatureRepository.findAll();
    }

    @Override
    public Optional<Temperatures> findById(Long id) {
        return temperatureRepository.findById(id);
    }

    @Override
    public Temperatures save(Temperatures temperatures) {
        return temperatureRepository.save(temperatures);
    }

    @Override
    public void remove(Long id) {
        temperatureRepository.deleteById(id);
    }

    @Override
    public Iterable<Temperatures> findAllByCities(Cities cities) {
        return temperatureRepository.findAllByCities(cities);
    }
}
