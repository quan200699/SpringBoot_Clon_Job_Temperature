package com.codegym.demo.controller;

import com.codegym.demo.model.Temperatures;
import com.codegym.demo.service.temperature.ITemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
