package com.codegym.demo.service.temperature;

import com.codegym.demo.model.Cities;
import com.codegym.demo.model.Temperatures;
import com.codegym.demo.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

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
        long currentTime = System.currentTimeMillis();
        Date createdDate = new Date(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String currentDate = dateFormat.format(createdDate);
        try {
            createdDate = dateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        temperatures.setCreatedTime(createdDate);
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
