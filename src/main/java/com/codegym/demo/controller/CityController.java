package com.codegym.demo.controller;

import com.codegym.demo.model.Cities;
import com.codegym.demo.service.city.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private ICityService cityService;

    @GetMapping
    public ResponseEntity<Iterable<Cities>> getAllCity() {
        return new ResponseEntity<>(cityService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cities> createNewCity(@RequestBody Cities cities) {
        return new ResponseEntity<>(cityService.save(cities), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cities> getCity(@PathVariable Long id) {
        Optional<Cities> cityOptional = cityService.findById(id);
        return cityOptional.map(cities -> new ResponseEntity<>(cities, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cities> updateCity(@PathVariable Long id, @RequestBody Cities cities) {
        Optional<Cities> cityOptional = cityService.findById(id);
        return cityOptional.map(citiesIterator -> {
            cities.setId(citiesIterator.getId());
            cityService.save(cities);
            return new ResponseEntity<>(cities, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cities> deleteCity(@PathVariable Long id) {
        Optional<Cities> cityOptional = cityService.findById(id);
        return cityOptional.map(cities -> {
            cityService.remove(id);
            return new ResponseEntity<>(cities, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
