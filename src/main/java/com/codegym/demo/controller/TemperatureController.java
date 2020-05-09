package com.codegym.demo.controller;

import com.codegym.demo.model.Cities;
import com.codegym.demo.model.Temperatures;
import com.codegym.demo.service.city.ICityService;
import com.codegym.demo.service.temperature.ITemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Saigon")
    private void getTemperature() {
        URL url = null;
        Scanner scanner = null;
        try {
            url = new URL("https://forecast.weather.gov/MapClick.php?lat=37.7772&lon=-122.4168&fbclid=IwAR0vy1obwdR8YYh-o_R1Nmh0_lNpXzaDv1XSKfizhF1fIGASa3_TG_Mi43g#.XrWB7BMzb_T");
            scanner = new Scanner(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter("\\\\Z");
        String content = scanner.next();
        scanner.close();
        content = content.replace("\\\\R", "");
        Pattern temperature = Pattern.compile("class=\"myforecast-current-sm\">(.*?)&deg;C</p>");
        Pattern city = Pattern.compile("href=\"https://www.weather.gov/mtr\">(.*?)</a>");
        Matcher result = temperature.matcher(content);
        Cities cities = new Cities();
        Temperatures temperatures = new Temperatures();
        while (result.find()) {
            temperatures.setTemperature(result.group(1));
        }
        Matcher result1 = city.matcher(content);
        while (result1.find()) {
            cities.setName(result1.group(1));
        }
        Optional<Cities> citiesOptional = cityService.findByName(cities.getName());
        if (!citiesOptional.isPresent()) {
            cityService.save(cities);
        } else {
            cities.setId(citiesOptional.get().getId());
        }
        temperatures.setCities(cities);
        temperatureService.save(temperatures);
    }
}
