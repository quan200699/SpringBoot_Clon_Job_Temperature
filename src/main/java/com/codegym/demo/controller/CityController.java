package com.codegym.demo.controller;

import com.codegym.demo.model.City;
import com.codegym.demo.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private ICityService cityService;

    @GetMapping
    public ResponseEntity<Iterable<City>> getAllCity() {
        return new ResponseEntity<>(cityService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<City> createNewCity(@RequestBody City city) {
        return new ResponseEntity<>(cityService.save(city), HttpStatus.OK);
    }
}
