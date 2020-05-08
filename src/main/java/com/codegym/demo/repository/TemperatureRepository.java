package com.codegym.demo.repository;

import com.codegym.demo.model.Temperatures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<Temperatures, Long> {
}
