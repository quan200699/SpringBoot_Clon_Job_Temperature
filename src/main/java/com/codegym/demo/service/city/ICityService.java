package com.codegym.demo.service.city;

import com.codegym.demo.model.Cities;
import com.codegym.demo.service.IGeneralService;

import java.util.Optional;

public interface ICityService extends IGeneralService<Cities> {
    Optional<Cities> findByName(String name);
}
