package com.codegym.demo.service.temperature;

import com.codegym.demo.model.Cities;
import com.codegym.demo.model.Temperatures;
import com.codegym.demo.service.IGeneralService;

public interface ITemperatureService extends IGeneralService<Temperatures> {
    Iterable<Temperatures> findAllByCities(Cities cities);
}
