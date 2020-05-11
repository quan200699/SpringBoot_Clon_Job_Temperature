package com.codegym.demo.controller;

import com.codegym.demo.model.Temperatures;
import com.codegym.demo.service.city.ICityService;
import com.codegym.demo.service.temperature.ITemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/temperatures")
public class TemperatureController {
    @Autowired
    private ITemperatureService temperatureService;

    @Autowired
    private ICityService cityService;

    @GetMapping
    public ResponseEntity<Iterable<Temperatures>> getAllTemperature() {
        return new ResponseEntity<>(temperatureService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Temperatures> createNewTemperature(@RequestBody Temperatures temperatures) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Temperatures> deleteTemperature(@PathVariable Long id) {
        Optional<Temperatures> temperaturesOptional = temperatureService.findById(id);
        return temperaturesOptional.map(temperatures -> {
            temperatureService.remove(id);
            return new ResponseEntity<>(temperatures, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
