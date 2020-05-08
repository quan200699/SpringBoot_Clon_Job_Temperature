package com.codegym.demo.controller;

import com.codegym.demo.model.Temperatures;
import com.codegym.demo.service.temperature.ITemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/temperatures")
public class TemperatureController {
    @Autowired
    private ITemperatureService temperatureService;

    @GetMapping
    public ResponseEntity<Iterable<Temperatures>> getAllTemperature() {
        return new ResponseEntity<>(temperatureService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Temperatures> createNewTemperature(@RequestBody Temperatures temperatures) {
        return new ResponseEntity<>(temperatureService.save(temperatures), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Temperatures> getTemperature(@PathVariable Long id) {
        Optional<Temperatures> temperaturesOptional = temperatureService.findById(id);
        return temperaturesOptional.map(temperatures -> new ResponseEntity<>(temperatures, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Temperatures> updateTemperature(@PathVariable Long id, @RequestBody Temperatures temperatures) {
        Optional<Temperatures> temperaturesOptional = temperatureService.findById(id);
        return temperaturesOptional.map(temperaturesIterator -> {
            temperatures.setId(temperaturesIterator.getId());
            temperatureService.save(temperatures);
            return new ResponseEntity<>(temperatures, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
